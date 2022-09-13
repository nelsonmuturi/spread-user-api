package com.spread.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spread.users.model.enums.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import static com.spread.users.service.Constants.*;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger =
            LoggerFactory.getLogger(SecurityConfig.class);

    private final ApiAccessDeniedHandler accessDeniedHandler;
    private final ApiAuthenticationEntryPoint authenticationEntryPoint;
    private final FilterChainFailureHandler failureHandler;
    private final ObjectMapper mapper;

    @Value("${app.security.jwt.keystore-location}")
    private String keyStorePath;
    @Value("${app.security.jwt.keystore-password}")
    private String keyStorePassword;
    @Value("${app.security.jwt.key-alias}")
    private String keyAlias;
    @Value("${app.security.jwt.private-key-passphrase}")
    private String privateKeyPassphrase;

    public SecurityConfig(final ApiAccessDeniedHandler accessDeniedHandler,
                          final ApiAuthenticationEntryPoint authenticationEntryPoint,
                          final FilterChainFailureHandler failureHandler,
                          final ObjectMapper mapper) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.failureHandler = failureHandler;
        this.mapper = mapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .formLogin().disable()
                .csrf().ignoringAntMatchers(API_URL_PREFIX)
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/users/test").permitAll()
                .mvcMatchers(HttpMethod.GET, LIVENESS).permitAll()
                .mvcMatchers(HttpMethod.GET, READINESS).permitAll()
                .antMatchers(HttpMethod.POST, TOKEN_URL).permitAll()
                .antMatchers(HttpMethod.POST, ANONYMOUS_TOKEN_URL).permitAll()
                .antMatchers(HttpMethod.DELETE, TOKEN_URL).permitAll()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.POST, REFRESH_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        getJwtAuthenticationConverter())))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new
                CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("HEAD",
                "GET", "PUT", "POST", "DELETE", "PATCH"));
        // For CORS response headers
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000", "http://localhost:3001", "https://stockcontrol.spread.co.ke", "https://spread.co.ke"));
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public KeyStore keystore() {
        try {
            KeyStore keyStore =
                    KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream resourceAsStream =
                    Thread.currentThread().getContextClassLoader()
                            .getResourceAsStream(keyStorePath);
            keyStore.load(resourceAsStream,
                    keyStorePassword.toCharArray());
            return keyStore;
        } catch (IOException | CertificateException
            | NoSuchAlgorithmException | KeyStoreException e) {
            logger.error("unable to load keystore");
        }
        throw new IllegalArgumentException("unable to load keystore");
    }

    @Bean
    public RSAPrivateKey jwtSigningKey(KeyStore keyStore) {
        try {
            Key key = keyStore.getKey(keyAlias,
                    privateKeyPassphrase.toCharArray());
            if (key instanceof RSAPrivateKey) {
                return (RSAPrivateKey) key;
            }
        } catch (UnrecoverableKeyException |
            NoSuchAlgorithmException |
            KeyStoreException e) {
            logger.error("unable to load private key");
        }
        throw new IllegalArgumentException("unable to load private key");
    }

    @Bean
    public RSAPublicKey jwtValidationKey(KeyStore keyStore) {
        try {
            java.security.cert.Certificate certificate =
                    keyStore.getCertificate(keyAlias);
            PublicKey publicKey = certificate.getPublicKey();
            if (publicKey instanceof RSAPublicKey) {
                return (RSAPublicKey) publicKey;
            }
        } catch (KeyStoreException e) {
            logger.error("unable to load public key");
        }
        throw new IllegalArgumentException("unable to load public key");
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey rsaPublicKey) {
        return NimbusJwtDecoder.withPublicKey(
                rsaPublicKey).build();
    }

    private JwtAuthenticationConverter
        getJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorityConverter =
                new JwtGrantedAuthoritiesConverter();
        authorityConverter.setAuthorityPrefix(AUTHORITY_PREFIX);
        authorityConverter.setAuthoritiesClaimName(ROLE_CLAIM);
        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorityConverter);
        return converter;
    }
}
