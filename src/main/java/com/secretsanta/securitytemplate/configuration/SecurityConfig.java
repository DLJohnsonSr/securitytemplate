package com.secretsanta.securitytemplate.configuration;

import com.secretsanta.securitytemplate.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserRepository userRepo;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new SSUSD(userRepo);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        String[] everyone = {};
        String[] someoneElse = {};
        String[] admin = {};

        http.authorizeRequests()
                .antMatchers().authenticated()
                .antMatchers(everyone).access("hasAuthority('USER') and hasAuthority('SOMEONE') and hasAuthority('ADMIN')")
                .antMatchers(someoneElse).access("hasAuthority('SOMEONE') and hasAuthority('ADMIN')")
                .antMatchers(admin).access("hasAuthority('ADMIN')")
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
//                .formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

        http.csrf().disable();

        http.headers().frameOptions().disable();
    }

//    REMOVE BEFORE PUBLISHING!!!!!!!!!!
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication().withUser("Test")
                .password(passwordEncoder().encode("test")).authorities("USER","SOMEONE","ADMIN")
                .and()
                .passwordEncoder(passwordEncoder());

        auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
    }
}
