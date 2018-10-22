package org.lotuc.spring.rest.example.springmybatis;

import lombok.extern.slf4j.Slf4j;
import org.lotuc.spring.rest.example.springmybatis.repository.security.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@SpringBootApplication
@Slf4j
public class SpringMybatisApplication implements CommandLineRunner {
  private UserRepository userRepository;

  public SpringMybatisApplication(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Bean
  BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringMybatisApplication.class, args);
  }

  @Override
  public void run(String... args) {
    String adminPass = UUID.randomUUID().toString();
    log.info("admin password: {}", adminPass);
    userRepository.setPassword("admin", bCryptPasswordEncoder().encode(adminPass));
  }
}
