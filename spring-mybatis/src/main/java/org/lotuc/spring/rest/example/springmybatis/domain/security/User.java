package org.lotuc.spring.rest.example.springmybatis.domain.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class User {
  String id;
  String name;
  String password;
  String role;
}
