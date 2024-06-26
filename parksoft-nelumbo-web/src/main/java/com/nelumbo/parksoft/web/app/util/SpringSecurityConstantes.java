package com.nelumbo.parksoft.web.app.util;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class SpringSecurityConstantes {
	
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String PREFIX_TOKEN = "Bearer ";
	public static final long EXPIRATION_DATE=21600000L;//14400000L
	public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
	public static final String CONTENT_TYPE = "application/json";
	public static final String MESSAGE = "message";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_SOCIO = "SOCIO";
}
