package com.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

import static com.example.security.Roles.ADMIN;
import static com.example.security.Roles.OWNER_ADMIN;
import static javafx.scene.AccessibleAttribute.ROLE;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize method-level security
@ConditionalOnProperty(name = "ptc.security.enable", havingValue = "true")
public class BasicAuthenticationConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .httpBasic()
                .disable()
                .csrf()
                .disable()
                .headers().frameOptions().disable()

                .and()

                .authorizeRequests()
                .antMatchers("/",
                        "/h2-console/*")
                .permitAll()

                .antMatchers("/api/owners").hasRole(OWNER_ADMIN)
                .antMatchers("api/users").hasRole(ADMIN)
                .anyRequest()
                .authenticated()

                .and();
        // @formatter:on
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring().antMatchers(
                "/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/swagger/**",
                "/h2-console/**");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled from users where username=?")
                .authoritiesByUsernameQuery("select username,role from roles where username=?");
    }
}
