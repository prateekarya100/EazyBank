package com.tomcatdevs.Accounts.controller.JWT.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
public class SecurityConfig {


    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user1=User.builder()
                .username("tomcatdev")
                .password(passwordEncoder().encode("admin"))
                .roles("user").build();

        UserDetails user2=User.builder()
                .username("cybercube")
                .password(passwordEncoder().encode("admin"))
                .roles("admin").build();

        UserDetails user3=User.builder()
                .username("ashish")
                .password(passwordEncoder().encode("ashish"))
                .roles("admin").build();

        return new InMemoryUserDetailsManager(user1,user2,user3);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
