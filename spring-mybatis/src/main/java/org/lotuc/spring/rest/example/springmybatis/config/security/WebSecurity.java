package org.lotuc.spring.rest.example.springmybatis.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.lotuc.spring.rest.example.springmybatis.config.properties.ApplicationProperty;
import org.lotuc.spring.rest.example.springmybatis.config.properties.SecurityProperty;
import org.lotuc.spring.rest.example.springmybatis.service.impl.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.lotuc.spring.rest.example.springmybatis.constants.ApplicationDefaults.SIGN_UP_URL;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
  private ObjectMapper objectMapper;
  private SecurityProperty securityProperty;
  private UserDetailsServiceImpl userDetailsService;
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  public WebSecurity(
      ApplicationProperty applicationProperty,
      UserDetailsServiceImpl userDetailsService,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      ObjectMapper objectMapper) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.securityProperty = applicationProperty.getSecurity();
    this.objectMapper = objectMapper;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/actuator/**")
        .hasRole("ADMIN")
        // permit swagger ui resources
        .antMatchers(
            HttpMethod.GET,
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html**",
            "/webjars/**",
            "favicon.ico")
        .permitAll()
        // permit signup
        .antMatchers(HttpMethod.POST, SIGN_UP_URL)
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        // JWT Authentication & Authorization
        .addFilter(
            new JWTAuthenticationFilter(authenticationManager(), securityProperty, objectMapper))
        .addFilter(new JWTAuthorizationFilter(authenticationManager(), securityProperty))
        // this disables session creation on Spring Security
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    if (securityProperty.getCors() != null) {
      source.registerCorsConfiguration("/**", securityProperty.getCors());
    }
    return source;
  }
}
