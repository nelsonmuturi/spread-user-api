package com.spread.users.service;

import java.time.format.DateTimeFormatter;

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
    public static final long EXPIRATION_TIME = 3_600_000;   // represents 60 mins.
    public static final String ROLE_CLAIM = "roles";
    public static final String AUTHORITY_PREFIX = "ROLE_";
    // ---------------------------------------------------------------------------
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
