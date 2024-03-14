package com.pubfinder.pubfinder.config;

import com.pubfinder.pubfinder.security.AuthenticationFilter;
import com.pubfinder.pubfinder.security.AuthenticationService;
import com.pubfinder.pubfinder.service.UserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.pubfinder.pubfinder.models.Role.ADMIN;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationService authenticationService;
    private final AuthenticationProvider authenticationProvider;
    private final UserDetailsService userDetailsService;
    private static final String[] WHITE_LIST_URL = {
            "/pub/test",
            "/pub/getPubs/**",
            "/pub/getPub/**",
            "/pub/savePub",
            "/user/register",
            "/user/login"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers(POST,"/pub/savePub").hasAnyRole(ADMIN.name())
                                .requestMatchers(PUT, "/pub/editPub").hasAnyRole(ADMIN.name())
                                .requestMatchers(DELETE, "/pub/deletePub").hasAnyRole(ADMIN.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new AuthenticationFilter(authenticationService, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                /*
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
                 */
        ;

        return http.build();
    }
}
