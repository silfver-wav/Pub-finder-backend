package com.pubfinder.pubfinder.security;

import com.pubfinder.pubfinder.security.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

public class AuthenticationService {

    // @Value("${security.auth.token.header_name}")
    private static String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    // @Value("${security.auth.token}")
    private static String AUTH_TOKEN = "Pub";

    public static Authentication getAuthentication(HttpServletRequest request)
    {
        System.out.println("AUTH_TOKEN_HEADER_NAME: " + AUTH_TOKEN_HEADER_NAME);
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        System.out.println("apiKey:" + apiKey);
        if (apiKey == null || !apiKey.equals(AUTH_TOKEN))
        {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
