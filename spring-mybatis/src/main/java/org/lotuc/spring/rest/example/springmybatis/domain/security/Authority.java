package org.lotuc.spring.rest.example.springmybatis.domain.security;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Authority {
  String name;
}
