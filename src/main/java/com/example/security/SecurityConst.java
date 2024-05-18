package com.example.security;

public class SecurityConst {
    private SecurityConst() {}

    public static  final String JWT_SECRET_KEY = "__secret__";

    public static final String JWT_AUTHORIZE_CLAIM = "Bearer ";

    public static final String JWT_CLAIM_USERNAME = "username";

    public static final String JWT_CLAIM_ROLE = "role";

    public static final String JWT_TOKEN_HEADER_REQUEST = "X-AUTH-TOKEN";

    public static final String JWT_TOKEN_HEADER_RESPONSE = "x-auth-token";

    public static final String ALLOWED_ORIGIN_SERVER = "http://localhost:5173";

    // JWTの未保持を許容するURI
    public static final String PATTERN_JWT_IGNORE_URI_ARRAY = "/api|/api/logout|/api/error|/api/login|^/api/login/.*$";
}
