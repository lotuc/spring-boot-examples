package org.lotuc.spring.rest.example.springmybatis.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.lotuc.spring.rest.example.springmybatis.config.properties.SecurityProperty;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static java.util.stream.Collectors.toList;
import static org.lotuc.spring.rest.example.springmybatis.constants.ApplicationDefaults.JWT_AUTHORITY_CLAIM;

@Slf4j
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
    log.debug("Request authorization header {}: {}", securityProperty.getTokenHeader(), header);

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
      log.debug("JWT authorization with {}", token);
      // parse the token.
      DecodedJWT jwt =
          JWT.require(
                  HMAC512(securityProperty.getJwt().getSecret().getBytes(Charset.forName("UTF-8"))))
              .build()
              .verify(token.substring(securityProperty.getTokenPrefix().length()));
      String user = jwt.getSubject();
      List<String> authorities = jwt.getClaim(JWT_AUTHORITY_CLAIM).asList(String.class);

      if (user != null) {
        return new UsernamePasswordAuthenticationToken(
            user, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(toList()));
      }
    }
    return null;
  }
}
