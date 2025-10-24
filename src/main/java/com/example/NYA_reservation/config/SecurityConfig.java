package com.example.NYA_reservation.config;

import com.example.NYA_reservation.controller.error.CustomAccessDeniedHandler;
import com.example.NYA_reservation.controller.error.CustomAuthenticationEntryPoint;
import com.example.NYA_reservation.controller.error.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                                                   CustomAccessDeniedHandler customAccessDeniedHandler) throws Exception {

        http
                .formLogin(login -> login
                        .loginPage("/login")
                        //ログイン失敗した時のハンドリング
                        .failureHandler(customAuthenticationFailureHandler)
                        .defaultSuccessUrl("/", true)
                        .usernameParameter("account")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .clearAuthentication(true)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/user/new", "/user/add", "/search", "/restaurant/**").permitAll()
                        .requestMatchers("/webjars/**", "/css/**", "/js/**", "/storage/**").permitAll()
                        .requestMatchers("/mypage/**", "/reservation/**", "/user/edit/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/**").authenticated()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(e -> e
                        //ログインしてないユーザーがURLにアクセスした時のエラーハンドリング
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        //ログインしているが、権限のないURLにアクセスした場合のエラーハンドリング
                        .accessDeniedHandler(customAccessDeniedHandler)
                );
        return http.build();
    }

    //パスワードの暗号化
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}