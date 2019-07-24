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
    private UserRepository userRepo;

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
                // for custom login page
//                .formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

        http.csrf().disable(); // Only for H2 Console, NOT FOR PRODUCTION

        http.headers().frameOptions().disable(); // Only for the H2 console, NOT For PRODUCTION
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //    REMOVE BEFORE PUBLISHING!!!!!!!!!!
        auth.inMemoryAuthentication().withUser("Test")
                .password(passwordEncoder().encode("test")).authorities("USER","SOMEONE","ADMIN")
                .and()
                .passwordEncoder(passwordEncoder());

        //Allows database authentication
        auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
    }
}
