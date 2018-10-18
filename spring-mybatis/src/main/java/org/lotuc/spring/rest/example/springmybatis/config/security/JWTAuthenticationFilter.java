package org.lotuc.spring.rest.example.springmybatis.config.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lotuc.spring.rest.example.springmybatis.config.properties.SecurityProperty;
import org.lotuc.spring.rest.example.springmybatis.domain.security.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private ObjectMapper objectMapper;
  private SecurityProperty securityProperty;
  private AuthenticationManager authenticationManager;

  JWTAuthenticationFilter(
      AuthenticationManager authenticationManager,
      SecurityProperty securityProperty,
      ObjectMapper objectMapper) {
    this.authenticationManager = authenticationManager;
    this.securityProperty = securityProperty;
    this.objectMapper = objectMapper;
  }

  /** 解析用户验证信息，发送给 AuthenticationManager */
  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse response)
      throws AuthenticationException {
    try {
      User cred = new ObjectMapper().readValue(req.getInputStream(), User.class);
      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              cred.getName(), cred.getPassword(), new ArrayList<>()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** 成功登陆，为用户生成一个 JWT */
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication auth)
      throws IOException, ServletException {
    SecurityProperty.JwtConfig jwt = securityProperty.getJwt();
    String token =
        JWT.create()
            .withSubject(
                ((org.springframework.security.core.userdetails.User) auth.getPrincipal())
                    .getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + jwt.getExpirationMS()))
            .sign(HMAC512(jwt.getSecret()));
    Map<String, String> tokenResult = new HashMap<>();
    tokenResult.put("token", token);
    response.addHeader("Content-Type", "application/json");
    response.addHeader(
        securityProperty.getTokenHeader(), securityProperty.getTokenPrefix() + token);
    response.getWriter().write(objectMapper.writeValueAsString(tokenResult));
  }
}
