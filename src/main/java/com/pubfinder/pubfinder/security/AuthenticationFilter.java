package com.pubfinder.pubfinder.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.osgi.service.component.annotations.Component;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.server.ExportException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            if (doNotNeedJwt(request.getRequestURI())) {
                Authentication authentication = authenticationService.authenticateApiKey(request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else if(request.getHeader("Authorization").isEmpty() || !request.getHeader("Authorization").startsWith("Bearer ")) {
                throw new Exception();
            }  else {
                String jwt = request.getHeader("Authorization").substring(7);
                String username = authenticationService.extractUsername(jwt);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (authenticationService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            writer.print(e.getMessage());
            writer.flush();
            writer.close();
        }

        filterChain.doFilter(request, response);
    }

    private static final String[] WHITE_LIST_URL = {
            "/pub/test",
            "/pub/getPubs/**",
            "/pub/getPub/**",
            "/pub/savePub",
            "/user/register",
            "/user/login"
    };
    private boolean doNotNeedJwt(String uri) {
        return Arrays.stream(WHITE_LIST_URL).toList().contains(uri);
    }
}
