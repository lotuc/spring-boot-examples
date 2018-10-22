package org.lotuc.spring.rest.example.springmybatis.controller.security;

import org.lotuc.spring.rest.example.springmybatis.controller.to.Resource;
import org.lotuc.spring.rest.example.springmybatis.domain.security.User;
import org.lotuc.spring.rest.example.springmybatis.repository.security.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private UserRepository userRepository;

  public UserController(
      BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.userRepository = userRepository;
  }

  @PostMapping("/sign-up")
  public Resource signUp(@RequestBody User user) {
    String userId = UUID.randomUUID().toString();
    user.setId(userId).setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return new Resource(userId);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public Collection<User> getList() {
    return userRepository.findAll();
  }

  @GetMapping("/{name}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
  public User getDetail(
      @PathVariable("name") String name, HttpServletRequest request, Principal principal) {
    if (request.isUserInRole("CLIENT") && !name.equals(principal.getName())) {
      throw new AccessDeniedException("Resource not available");
    }
    return userRepository.findByName(name, false);
  }
}
