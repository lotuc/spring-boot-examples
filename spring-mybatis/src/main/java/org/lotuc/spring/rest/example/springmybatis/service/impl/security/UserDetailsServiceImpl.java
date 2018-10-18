package org.lotuc.spring.rest.example.springmybatis.service.impl.security;

import org.lotuc.spring.rest.example.springmybatis.domain.security.User;
import org.lotuc.spring.rest.example.springmybatis.repository.security.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private UserRepository userRepo;

  public UserDetailsServiceImpl(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepo.findByName(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }
    // TODO
    return new org.springframework.security.core.userdetails.User(
        username, user.getPassword(), Collections.emptyList());
  }
}
