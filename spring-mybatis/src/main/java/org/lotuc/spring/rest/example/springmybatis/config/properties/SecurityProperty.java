package org.lotuc.spring.rest.example.springmybatis.config.properties;

import lombok.Data;
import org.lotuc.spring.rest.example.springmybatis.constants.ApplicationDefaults;
import org.springframework.web.cors.CorsConfiguration;

@Data
public class SecurityProperty {
  String tokenHeader = ApplicationDefaults.Security.tokenHeader;

  String tokenPrefix = ApplicationDefaults.Security.tokenPrefix;

  JwtConfig jwt = new JwtConfig();

  CorsConfiguration cors = new CorsConfiguration();

  @Data
  public static class JwtConfig {
    String secret;

    Long expirationMS = ApplicationDefaults.Security.Jwt.expirationMS;
  }
}
