package org.lotuc.spring.rest.example.springmybatis.constants;

public interface ApplicationDefaults {
  String SIGN_UP_URL = "/user/sign-up";
  String JWT_AUTHORITY_CLAIM = "authorities";

  interface Security {
    String tokenHeader = "Authorization";
    String tokenPrefix = "Bearer ";

    interface Jwt {
      Long expirationMS = 86_400_000L; // 1 day
    }
  }
}
