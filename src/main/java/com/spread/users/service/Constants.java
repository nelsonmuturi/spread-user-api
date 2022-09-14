package com.spread.users.service;

import io.grpc.Context;
import io.grpc.Metadata;

import java.time.format.DateTimeFormatter;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public class Constants {

    private Constants() {}

    public static final String USERS = "USERS";

    // *** courtesy Sourabh Sharma ****
    public static final String ENCODER_ID = "bcrypt";
    public static final String API_URL_PREFIX = "/api/v1/**";
    public static final String SIGN_UP_URL = "/api/v1/users";
    public static final String TOKEN_URL = "/api/v1/auth/token";
    public static final String ANONYMOUS_TOKEN_URL = "/api/v1/anonymous/token";
    public static final String REFRESH_URL = "/api/v1/auth/token/refresh";
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET_KEY = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 21_600_000;   // represents 60 mins.
    public static final String ROLE_CLAIM = "roles";
    public static final String AUTHORITY_PREFIX = "ROLE_";
    // ---------------------------------------------------------------------------
    // grpc constants
    public static final String BEARER_TYPE = "Bearer";
    public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("clientId");
    // ---------------------------------------------------------------------------
    // k8s prob functions
    public static final String LIVENESS = "/users/liveness";
    public static final String READINESS = "/users/readiness";
    // ---------------------------------------------------------------------------
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
