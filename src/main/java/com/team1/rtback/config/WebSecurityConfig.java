package com.team1.rtback.config;

import com.team1.rtback.util.jwt.JwtAuthFilter;
import com.team1.rtback.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


// 1. 기능 : Spring Security 설정
// 2. 작성자 : 조소영
// 3. 생성날자 : 2022-12-16
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;

    // 암호화 타입 설정
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        // h2 접근 요청은 인증 무시
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console());
    }

    //
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // JWT방식 사용하기 때문에 세션 방식 사용X
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();
//                .and()
//                .addFilterBefore(new JwtAuthFilter(jwtUtil),
//                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
