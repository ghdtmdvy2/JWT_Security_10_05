package com.ll.exam.app__2022_10_05.app.security;

import com.ll.exam.app__2022_10_05.app.security.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        http
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                .antMatchers("/member/login", "/member/join")
                                .permitAll()
                                .anyRequest()
                                .authenticated() // 최소자격 : 로그인
                )
                .cors().disable() // 타 도메인에서 API 호출 가능
                .csrf().disable() // CSRF 토큰 끄기
                .httpBasic().disable() // httpBasic 로그인 방식 끄기
                .formLogin().disable() // 폼 로그인 방식 끄기
                .sessionManagement(sessionManagement ->
                        // 세션 사용안함 ( 우리는 Session 대신 JWT 를 사용 할 것이기 때문에 세션을 종료 )
                        sessionManagement.sessionCreationPolicy(STATELESS)
                ).addFilterBefore( // 필터를 추가하는데 어떤 필터 전에 추가하겠다는 것이다.
                        jwtAuthorizationFilter, // JWT 인가 필터를 커스텀 마이징을 하여 만듦. ( 원래는 없음 )
                        UsernamePasswordAuthenticationFilter.class // 스프링 시큐리티에 이런 필터가 있다.
                );

        return http.build();
    }
}