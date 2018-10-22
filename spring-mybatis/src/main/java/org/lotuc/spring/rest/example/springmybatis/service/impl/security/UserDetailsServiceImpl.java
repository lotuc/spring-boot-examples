package org.lotuc.spring.rest.example.springmybatis.service.impl.security;

import lombok.extern.slf4j.Slf4j;
import org.lotuc.spring.rest.example.springmybatis.domain.security.User;
import org.lotuc.spring.rest.example.springmybatis.repository.security.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
  private UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug("findByName {}", username);
    User user = userRepository.findByName(username, true);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    log.debug("Loaded user {}", user);
    return new org.springframework.security.core.userdetails.User(
        username,
        user.getPassword(),
        user.getAuthorities()
            .stream()
            .map(auth -> new SimpleGrantedAuthority(auth.getName()))
            .collect(Collectors.toList()));
  }
}
