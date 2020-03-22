package com.example.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.security.Roles.*;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize method-level security
@ConditionalOnProperty(name = "ptc.security.enable", havingValue = "true")
public class BasicAuthenticationConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

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
                        "/h2-console/*",
                        "/upload/*",
                        "/multi-upload/*",
                        "/files/*",
                        "/download/**")
                .permitAll()

                .antMatchers("/api/owners/").hasRole(OWNER_ADMIN)
                .antMatchers("api/pets/").hasRole(OWNER_ADMIN)
                .antMatchers("api/users/").hasRole(ADMIN)
                .antMatchers("api/pettypes/").hasAnyRole(OWNER_ADMIN,VET_ADMIN)
                .antMatchers("api/pettypes/*").hasRole(VET_ADMIN) //
                .antMatchers("api/vets/").hasRole(VET_ADMIN)
                .antMatchers("api/specialties/").hasRole(VET_ADMIN)
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
                "/h2-console/**",
                "/resources/**");
    }

    /*
    * AuthenticationManagerBuilder 를 주입해서 인증에 대한 처리를 한다.
    * 설정 방법 : InMemory, JDBC, LDAP, etc..
    * */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    /*
    * DB 인증 Provider
    * */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
