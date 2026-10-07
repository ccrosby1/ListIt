/**
 * SecurityConfig.java
 * 
 * This class configures the security settings for the application.
 * It defines the security filter chain, authentication manager, and password encoder.
 * The security filter chain specifies which URLs are public and which require authentication.
 * The authentication manager is responsible for authenticating users.
 * The password encoder is used to encode passwords for secure storage.
 */
package com.gcu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.gcu.business.LoginService;
import com.gcu.utilities.LoginFailureHandler;
import com.gcu.utilities.LoginSuccessHandler;

/**
 * This class configures the security settings for the application.
 * It defines the security filter chain, authentication manager, and password encoder.
 * The security filter chain specifies which URLs are public and which require authentication.
 * The authentication manager is responsible for authenticating users.
 * The password encoder is used to encode passwords for secure storage.
 */
@Configuration
public class SecurityConfig {

    private final LoginService loginService;
    @Autowired
    private LoginSuccessHandler successHandler;
    @Autowired
    private LoginFailureHandler failureHandler;

    public SecurityConfig(LoginService loginService) {							
        this.loginService = loginService;										
    }
    /**
	 * This method configures the security filter chain for the application.
	 * It specifies which URLs are public and which require authentication.
	 * It also configures form login and logout behavior.
	 *
	 * @param http the HttpSecurity object to configure
	 * @return the configured SecurityFilterChain
	 * @throws Exception if an error occurs during configuration
	 */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())										

            .requiresChannel(channel ->
            	channel.anyRequest().requiresSecure()
            )

            
            
            .authorizeHttpRequests(auth -> auth									
                .requestMatchers("/", 
                				 "/login/**",
                				 "/css/**",
                				 "/registration/**").permitAll()				
                .requestMatchers("/admin/**").hasRole("admin")					
                .requestMatchers("/user/**",
                				 "/images/**").hasAnyRole("user", "admin")		
                .anyRequest().authenticated()									
            )

            
            .formLogin(form -> form
                    .loginPage("/login/")                     
                    .loginProcessingUrl("/login/doLogin")      
                    .successHandler(successHandler)    
                    .failureHandler(failureHandler)         
                    .permitAll()
            )

            
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();	
    }
    /**
     * This method configures the authentication manager for the application.
     * @param http
     * @param encoder
     * @return Authentication manager bean
     * @throws Exception
     */
    @Bean
    AuthenticationManager authManager(HttpSecurity http, PasswordEncoder encoder) throws Exception {   	
    	
    	AuthenticationManagerBuilder builder = 
    		http
    			.getSharedObject(AuthenticationManagerBuilder.class);	
    	
    		// configure authentication using LoginService and password encoder
        	builder
        		.userDetailsService(loginService)							
        		.passwordEncoder(encoder);									
        
        return builder.build();												
    }
    /**
	 * This method configures the password encoder for the application.
	 * @return Password encoder bean
	 */
    @Bean
    PasswordEncoder  passwordEncoder() {									
    	return new BCryptPasswordEncoder();									
    }
}