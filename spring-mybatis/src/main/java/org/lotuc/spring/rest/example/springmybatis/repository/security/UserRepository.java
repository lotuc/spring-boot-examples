package org.lotuc.spring.rest.example.springmybatis.repository.security;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lotuc.spring.rest.example.springmybatis.controller.security.Authority;
import org.lotuc.spring.rest.example.springmybatis.controller.security.User;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Mapper
public interface UserRepository {
  @Nullable
  User findByName(@Param("name") String name, @Param("withAuthorities") Boolean withAuthorities);

  @NotNull
  Collection<User> findAll();

  void save(@NotNull User user);

  void setPassword(
      @NotNull @Param("name") String name, @NotNull @Param("password") String password);

  void addAuthority(
      @NotNull @Param("userId") String userId, @NotNull @Param("authority") Authority authority);
}
