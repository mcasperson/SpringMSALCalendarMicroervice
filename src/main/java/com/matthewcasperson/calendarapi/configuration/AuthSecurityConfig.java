// src/main/java/com/matthewcasperson/calendarapi/configuration/AuthSecurityConfig.java

package com.matthewcasperson.calendarapi.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .anyRequest()
                .authenticated()
            .and()
            .oauth2ResourceServer()
                .jwt();
        // @formatter:on
    }
}