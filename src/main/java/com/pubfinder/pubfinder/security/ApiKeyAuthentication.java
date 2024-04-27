package com.pubfinder.pubfinder.security;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * The type Api key authentication.
 */
public class ApiKeyAuthentication extends AbstractAuthenticationToken {

  private final String apiKey;

  /**
   * Instantiates a new Api key authentication.
   *
   * @param apiKey      the api key
   * @param authorities the authorities
   */
  public ApiKeyAuthentication(String apiKey, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.apiKey = apiKey;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return apiKey;
  }
}
