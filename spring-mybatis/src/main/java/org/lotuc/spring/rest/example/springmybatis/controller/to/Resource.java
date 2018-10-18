package org.lotuc.spring.rest.example.springmybatis.controller.to;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class Resource {
  @Getter String id;

  public Resource(String id) {
    this.id = id;
  }
}
