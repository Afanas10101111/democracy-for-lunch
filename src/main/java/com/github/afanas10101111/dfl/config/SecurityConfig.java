package com.github.afanas10101111.dfl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(12);

    private final UserDetailsService userDetailsService;

    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/v1/profile/register").anonymous()
                .antMatchers("/v1/**").authenticated()
                .antMatchers(
                        "/v2/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/springfox-swagger-ui/**",
                        "/",
                        "/csrf",
                        "/swagger-ui.html",
                        "/index.jsp"
                ).permitAll()
                .antMatchers("/**").denyAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        if (daoAuthenticationProvider == null) {
            daoAuthenticationProvider = new DaoAuthenticationProvider();
            daoAuthenticationProvider.setPasswordEncoder(PASSWORD_ENCODER);
            daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        }
        return daoAuthenticationProvider;
    }
}
