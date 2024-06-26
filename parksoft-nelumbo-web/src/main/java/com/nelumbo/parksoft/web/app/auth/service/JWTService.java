package com.nelumbo.parksoft.web.app.auth.service;


import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;

public interface JWTService {
	
	public String create(Authentication auth) throws IOException ;
	
	public boolean validate(String token, HttpServletResponse response) throws JsonProcessingException, IOException;
	
	public Claims getClaims(String token);
	
	public String getUsername(String token);
	
	Collection<? extends GrantedAuthority> getRoles(String token) throws  IOException ;
	
	public String resolve(String token);
	
	//public UsuarioDTO findUser(String username);
	
}
