package org.lotuc.spring.rest.example.springmybatis.repository.security;

import org.apache.ibatis.annotations.Mapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lotuc.spring.rest.example.springmybatis.domain.security.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Mapper
public interface UserRepository {
  @Nullable
  User findByName(String name);

  Collection<User> findAll();

  void save(@NotNull User user);
}
