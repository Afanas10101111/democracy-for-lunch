package com.github.afanas10101111.dfl.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String REGISTER_URL = "/v1/profile/register";
    public static final String ADMIN_URL = "/v1/admin/**";
    public static final String RESTAURANTS_ADMIN_URL = "/v1/restaurants/**";
    public static final String OTHER_URLS = "/v1/**";

    private final UserDetailsService userDetailsService;

    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(REGISTER_URL).anonymous()
                .antMatchers(ADMIN_URL).hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.POST, RESTAURANTS_ADMIN_URL).hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.PUT, RESTAURANTS_ADMIN_URL).hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.DELETE, RESTAURANTS_ADMIN_URL).hasRole(ADMIN_ROLE)
                .antMatchers(OTHER_URLS).authenticated()
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
