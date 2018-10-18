package org.lotuc.spring.rest.example.springmybatis.config.security;

import com.auth0.jwt.JWT;
import org.lotuc.spring.rest.example.springmybatis.config.properties.SecurityProperty;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
  private SecurityProperty securityProperty;

  JWTAuthorizationFilter(
      AuthenticationManager authenticationManager, SecurityProperty securityProperty) {
    super(authenticationManager);
    this.securityProperty = securityProperty;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String header = request.getHeader(securityProperty.getTokenHeader());

    if (header == null || !header.startsWith(securityProperty.getTokenPrefix())) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(securityProperty.getTokenHeader());
    if (token != null) {
      // parse the token.
      String user =
          JWT.require(
                  HMAC512(securityProperty.getJwt().getSecret().getBytes(Charset.forName("UTF-8"))))
              .build()
              .verify(token.substring(securityProperty.getTokenPrefix().length()))
              .getSubject();

      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
      }
      return null;
    }
    return null;
  }
}
