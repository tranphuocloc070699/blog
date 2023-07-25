package com.loctran.server.config;

import com.loctran.server.exception.custom.CustomAccessDeniedHandler;
import com.loctran.server.exception.custom.ForbiddenException;
import com.loctran.server.filter.FilterChainExceptionHandler;
import com.loctran.server.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.loctran.server.user.model.Role.ADMIN;
import static com.loctran.server.user.model.Role.USER;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;
  private final AuthenticationEntryPoint authenticationEntryPoint;
  private final FilterChainExceptionHandler filterChainExceptionHandler;
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    
    httpSecurity
            .authorizeHttpRequests(
                    auth -> auth
                            .requestMatchers("/api/v1/auth/**","/api/v1/story/**")
                            .permitAll()
                            .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())
                            .requestMatchers("/api/v1/user/**").hasRole(USER.name())
                            .anyRequest()
                            .authenticated()
            )
            .authenticationProvider(authenticationProvider)
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class)
            .csrf(AbstractHttpConfigurer::disable);
//            .sessionManagement(session ->
//                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            
    return httpSecurity.build();
    
  }
 
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("http://localhost:4200");
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    configuration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    
    return source;
  }
}
