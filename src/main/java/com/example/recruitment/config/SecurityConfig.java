package com.example.recruitment.config;

import com.example.recruitment.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors().and()
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers("/authenticate").permitAll()
                .antMatchers(HttpMethod.GET,"/api/skills").permitAll()
                .antMatchers(HttpMethod.GET,"/api/users/usernames").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users").permitAll()
                .antMatchers(HttpMethod.GET,"/api/internships/**").permitAll()
                .antMatchers(HttpMethod.GET,"/api/users/employers").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE,"/api/users/").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/attachment/download").hasAuthority("ROLE_USER")
                .antMatchers("/api/profiles").hasAuthority("ROLE_USER")

                .antMatchers("/api/internship-applications").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET,"/api/departments").permitAll()
                .antMatchers(HttpMethod.POST,"/api/departments").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/departments/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/departments/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST,"/api/internships").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/internships/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/internships/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated().and()
                .exceptionHandling().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
