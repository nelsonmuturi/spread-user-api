package com.spread.users.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.stream.Collectors;

import static com.spread.users.service.Constants.EXPIRATION_TIME;
import static com.spread.users.service.Constants.ROLE_CLAIM;

@Component
public class JwtManager {
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public JwtManager(RSAPrivateKey privateKey,
                      RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String create(UserDetails principal) {
        final long now = System.currentTimeMillis();
        return JWT.create()
                .withIssuer("Spread API")
                .withSubject(principal.getUsername())
                .withClaim(ROLE_CLAIM,
                        principal.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + EXPIRATION_TIME))
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }
}
