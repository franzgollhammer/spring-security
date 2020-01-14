package com.franklin.springsecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.franklin.springsecurity.security.ApplicationUserRole.*;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // for PreAuthorize()
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http

            // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            // .and()

            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
            .antMatchers("/api/**").hasRole(STUDENT.name())
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/courses", true)
                .passwordParameter("password")
                .usernameParameter("username")
            .and()
            .rememberMe()
                .tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(21))
                .key("SpecialSecretKey")
                .rememberMeParameter("remember-me")
            .and()
            .logout()
                .logoutUrl("/logout")
                /** If csrf is disabled, else use POST request! */
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                /** */
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .logoutSuccessUrl("/login");
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails annaSmithUser = User.builder()
            .username("annasmith")
            .password(passwordEncoder.encode("password"))
            .authorities(STUDENT.getGrantedAuthorities())
            .build();

        UserDetails janeDoeUser = User.builder()
            .username("janedoe")
            .password(passwordEncoder.encode("password123"))
            .authorities(ADMIN.getGrantedAuthorities())
            .build();

        UserDetails tomJonesUser = User.builder()
            .username("tomjones")
            .password(passwordEncoder.encode("password123"))
            .authorities(ADMINTRAINEE.getGrantedAuthorities())
            .build();

        return new InMemoryUserDetailsManager(annaSmithUser, janeDoeUser, tomJonesUser);
    }
}