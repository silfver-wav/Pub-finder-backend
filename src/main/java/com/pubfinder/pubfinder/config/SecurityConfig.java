package com.pubfinder.pubfinder.config;

import com.pubfinder.pubfinder.security.AuthenticationFilter;
import com.pubfinder.pubfinder.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.pubfinder.pubfinder.models.enums.Role.ADMIN;
import static com.pubfinder.pubfinder.models.enums.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationService authenticationService;
    private final AuthenticationProvider authenticationProvider;
    private final UserDetailsService userDetailsService;
    private final LogoutHandler logoutHandler;


    private static final String[] WHITE_LIST_URL = {
            "/pub/getPubs/**",
            "/v2/api-docs",
            "/pub/test",
            "/user/register",
            "/user/login",
            "/user/refreshToken",
            "/pub/searchPubs/**",
            "/pub/getPub/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/pub/createPub").hasAnyAuthority(ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/pub/editPub").hasAnyAuthority(ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/pub/deletePub").hasAnyAuthority(ADMIN.name())
                                .requestMatchers(HttpMethod.PUT, "/user/edit").hasAnyAuthority(ADMIN.name(), USER.name())
                                .requestMatchers(HttpMethod.DELETE, "/user/delete").hasAnyAuthority(ADMIN.name(), USER.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new AuthenticationFilter(authenticationService, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/user/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}