package com.dossantosh.springfirstmicroservise.common.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for JWT settings.
 * 
 * Maps properties with prefix 'jwt' from application properties or YAML.
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * Secret key used to sign JWT tokens.
     */
    private String secret;

    /**
     * Token expiration time in milliseconds.
     */
    private long expiration;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
