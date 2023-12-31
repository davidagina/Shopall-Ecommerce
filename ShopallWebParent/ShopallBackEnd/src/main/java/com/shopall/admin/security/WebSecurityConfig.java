package com.shopall.admin.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	UserDetailsService userDetailsService() {
		return new ShopallUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.authenticationProvider(authenticationProvider());
			http
				.authorizeHttpRequests((auth) -> auth
						.anyRequest().authenticated()
						).formLogin(form -> form
								.loginPage("/login")
								.usernameParameter("email")
								.permitAll())
				
				.logout(logout -> logout.permitAll());
			
		return http.build();

    }
    
   @Bean
   public WebSecurityCustomizer webSecurityCustomizer() {
	   return(web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
   }
   
   
}