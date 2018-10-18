package org.lotuc.spring.rest.example.springmybatis.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationProperty {
  SecurityProperty security = new SecurityProperty();
}
