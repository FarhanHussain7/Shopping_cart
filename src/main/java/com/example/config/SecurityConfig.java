package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(); // your implementation
    }

    // ✅ Modern way: expose AuthenticationManager
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
    
    

	@SuppressWarnings("deprecation")
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		@SuppressWarnings("deprecation")
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/**").permitAll()
            )
            .formLogin(form -> form
                    .loginPage("/signin")
                    .loginProcessingUrl("/login")
                    .successHandler((request, response, authentication) -> {
                    	 System.out.println(",,,,,,,,,,,,,,,,,,,,");
                        var authorities = authentication.getAuthorities();
                        String redirectUrl = "/";
                        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                            redirectUrl = "/user/";
                            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
                        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                            redirectUrl = "/admin/";
                            System.out.println("ccccccccccccccccccccccccccc");
                        }
                        response.sendRedirect(redirectUrl);
                        System.out.println("dddddddddddddddddddddddddd");
                    })
                    .permitAll()
                )
                .logout(logout -> logout.permitAll());

        System.out.println("-----------------------------------------");

        return http.build();
    }
}
	