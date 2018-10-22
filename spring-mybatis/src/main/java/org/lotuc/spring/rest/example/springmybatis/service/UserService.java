package org.lotuc.spring.rest.example.springmybatis.service;

import org.lotuc.spring.rest.example.springmybatis.domain.security.User;
import org.lotuc.spring.rest.example.springmybatis.repository.security.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public String createUser(User user) {
    //    userRepository.findByName(user.getName(), )
    return null;
  }
}
