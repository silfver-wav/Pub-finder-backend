package com.pubfinder.pubfinder.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {
    @Value("${security.api-header-name}")
    private String HEADER_NAME;

    @Value("${security.api-key}")
    private String API_KEY;

    public Authentication getAuthentication(HttpServletRequest request)
    {
        String apiKey = request.getHeader(HEADER_NAME);
        if (apiKey == null || !apiKey.equals(API_KEY))
        {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
