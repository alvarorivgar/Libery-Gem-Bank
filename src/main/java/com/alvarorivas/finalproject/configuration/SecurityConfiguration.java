package com.alvarorivas.finalproject.configuration;

import com.alvarorivas.finalproject.service.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and().build();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.httpBasic();
        http.csrf().disable();
        http.authorizeRequests()
//              .mvcMatchers(HttpMethod.GET, "/public/**").permitAll()
                .mvcMatchers(HttpMethod.POST).hasRole("ADMIN")
//                .mvcMatchers(HttpMethod.POST, "/savings").hasRole("ADMIN")
//                .mvcMatchers(HttpMethod.POST, "/credit-card").hasRole("ADMIN")
//                .mvcMatchers(HttpMethod.POST, "/student-checking").hasRole("ADMIN")
//                .mvcMatchers(HttpMethod.POST, "/account-holder").hasRole("ADMIN")
//                .mvcMatchers(HttpMethod.POST, "/admin").anonymous()
//                .mvcMatchers(HttpMethod.POST, "/third-party").hasRole("ADMIN")
//                .mvcMatchers(HttpMethod.GET, "/checking/**").hasAnyRole("ADMIN", "ACCOUNT_HOLDER")
//                .mvcMatchers(HttpMethod.GET, "/savings/**").hasAnyRole("ADMIN", "ACCOUNT_HOLDER")
//                .mvcMatchers(HttpMethod.GET, "/credit-card/**").hasAnyRole("ADMIN", "ACCOUNT_HOLDER")
//                .mvcMatchers(HttpMethod.GET, "/student-checking/**").hasAnyRole("ADMIN", "ACCOUNT_HOLDER")



                .anyRequest().permitAll();

        return http.build();
    }
}
