package com.scm.scm20.Config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.scm20.services.impl.SecurityCustomUserDetailService;




@Configuration
public class SecurityConfig {
    

    //user create and login using java code with in memory service
    
    // @Bean
    // public UserDetailsService userDetailsService()
    // {
    //    UserDetails user1= User.username("admin123").password("admin123").roles("ADMIN","USER").build();
    //    UserDetails user2= User.username("user123").password("password").build();


    //     var inMemoryUserDetailsManager=new InMemoryUserDetailsManager();
    //     return inMemoryUserDetailsManager;
    // }

    @Autowired
    private SecurityCustomUserDetailService  userDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private AuthFailureHandler authFailureHandler;


    //configuration of authentication provider spring security
    @Bean
    public AuthenticationProvider authenticationProvider()
    {


        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();

        //user detaoil service ka object
        daoAuthenticationProvider.setUserDetailsService(userDetailService);

        //password encode ka object
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
    {
          //configuration
          //urls configure kiya hai ki kon se public rahenge or konse private
        httpSecurity.authorizeHttpRequests(authorize->{
            // authorize.requestMatchers("/home","/register","/services").permitAll();

            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();

        });

        //form default login
        //agar hame kuch bhi change krna hua to hame yaha ayeenge:form login se related
        httpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login")
            .loginProcessingUrl("/authenticate")
            .successForwardUrl("/user/profile")
            .failureForwardUrl("/login?error=true")
            .usernameParameter("email")
            .passwordParameter("password");
                


            formLogin.failureHandler(authFailureHandler);
        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        
        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true");
        });




        //oauth configuration

        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login")
            .successHandler(handler);
        });


        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
