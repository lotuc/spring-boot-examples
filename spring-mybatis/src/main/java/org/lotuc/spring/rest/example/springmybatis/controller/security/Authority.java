package org.lotuc.spring.rest.example.springmybatis.controller.security;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Authority {
  String name;
}
