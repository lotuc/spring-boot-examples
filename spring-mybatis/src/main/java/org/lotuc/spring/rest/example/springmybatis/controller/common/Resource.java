package org.lotuc.spring.rest.example.springmybatis.controller.common;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ApiModel("资源引用")
public class Resource {
  @Getter String id;

  public Resource(String id) {
    this.id = id;
  }
}
