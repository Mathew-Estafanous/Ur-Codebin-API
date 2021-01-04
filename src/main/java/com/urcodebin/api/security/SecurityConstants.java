package com.urcodebin.api.security;


public class SecurityConstants {

    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 7_200_000; // 2 Hours
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static final String LOGIN_URL = "/api/account/login";
    public static final String SIGN_UP_URL = "/api/account/signup";
    public static final String PUBLIC_PASTE_URL = "/api/paste/public";
}