package com.projeto.appspringthymeleaf.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        public static final String[] ENDPOINTS_WHITELIST = {
                        "/css/**",
                        "/js/**",
                        "/",
                        "/login",
                        "/home",
                        "/h2-console/**",
                        "/public/**"
        };
        public static final String LOGIN_URL = "/login";
        public static final String LOGOUT_URL = "/logout";
        public static final String DEFAULT_SUCCESS_URL = "/home";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http.authorizeHttpRequests(request -> request.requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasRole("USER")
                                .requestMatchers("/usuario").hasAnyRole("ADMIN")
                                .requestMatchers("/cliente").hasAnyRole("ADMIN", "USER")
                                .requestMatchers("/telefone").hasAnyRole("ADMIN", "USER")
                                .anyRequest().authenticated())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers(ENDPOINTS_WHITELIST).disable())
                                .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .accessDeniedPage("/erro/403")
                                                .authenticationEntryPoint((request, response, authException) -> response
                                                                .sendRedirect(request.getContextPath() + LOGIN_URL
                                                                                + "?error=unauthenticated")))
                                .formLogin(form -> form
                                                .loginPage(LOGIN_URL)
                                                .loginProcessingUrl(LOGIN_URL)
                                                .failureUrl(LOGIN_URL + "?error=badCredentials")
                                                .usernameParameter(USERNAME)
                                                .passwordParameter(PASSWORD)
                                                .defaultSuccessUrl(DEFAULT_SUCCESS_URL))
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .logoutSuccessUrl(LOGIN_URL + "?success=logout"));
                // http.addFilterBefore(jwtFilter(),UsernamePasswordAuthenticationFilter.class);
                // http.anonymous(anonymous -> anonymous.disable());
                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // @Bean
        // public JwtFilter jwtFilter() {
        // return new JwtFilter();
        // }
        // @Bean
        // public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder)
        // {
        // UserDetails user = User.withUsername("user")
        // .password(passwordEncoder.encode("123"))
        // .roles("USER")
        // .build();

        // return new InMemoryUserDetailsManager(user);
        // }

}
