package com.garbagecollectors.app.security;

/*import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;*/

//@EnableWebSecurity
public class SecurityConfig  {  //extends  WebSecurityConfigurerAdapter

/*    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll();


        http.csrf().disable()
                .authorizeRequests().antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
//                .antMatchers("/{userId}/**").access("@webSecurity.checkUserId(authentication,#userId)")
                .antMatchers("/{user_id}/**").access("@webSecurity.checkUserId(authentication,#user_id)")
                .
                .anyRequest().authenticated().and()
                .exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }*/

}