package com.nelumbo.parksoft.web.app.auth.service.impl;


import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.EXPIRATION_DATE;
import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.PREFIX_TOKEN;
import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.SECRET_KEY;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelumbo.parksoft.web.app.auth.service.JWTService;
import com.nelumbo.parksoft.web.app.util.SimpleGrantedAuthorityMixin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class JWTServiceImpl implements JWTService {
	
	/*private UsuarioRepository repository;
	
	public JWTServiceImpl(UsuarioRepository repository) {
		this.repository=repository;
	}*/

	@Override
	public String create(Authentication auth) throws IOException {
		
		User user= (User) auth.getPrincipal();
		
		String username= user.getUsername();
		
		Collection<? extends GrantedAuthority> roles= auth.getAuthorities();
		
		Claims claims = Jwts.claims()
				.add("authorities", new ObjectMapper().writeValueAsString(roles))
				.add("username", username)
				.build();
		
		return Jwts.builder()
						.subject(username)
						.claims(claims)
						.expiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
						.issuedAt(new Date())
						.signWith(SECRET_KEY)
						.compact();
	}

	@Override
	public boolean validate(String token, HttpServletResponse response) {
		
		try {
			getClaims(token);
			
			return true;
		}catch(JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public Claims getClaims(String token) {
		return Jwts.parser()
				.verifyWith(SECRET_KEY).build()
				.parseSignedClaims(resolve(token))
				.getPayload();
	}

	@Override
	public String getUsername(String token) {
		
		return getClaims(token).getSubject();
	}

	@Override
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {
		
		Object roles= getClaims(token).get("authorities");
		
		return Arrays.asList( 
				new ObjectMapper()
				.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
				.readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class));
	}

	@Override
	public String resolve(String token) {
		
		 if (token == null || !token.startsWith(PREFIX_TOKEN)) {
		        return null;
		 }
		 
		 return token.substring(PREFIX_TOKEN.length());
	}
	
/*	@Override
	@Transactional(readOnly = true)
	public UsuarioDTO findUser(String username) {
		Optional<Usuario> user= repository.findByUsernameOrCorreo(username);
		
		UsuarioDTO dto= new UsuarioDTO();
		user.ifPresent(u-> {
				dto.setId(u.getId());
				dto.setNombre(u.getNombre());
				dto.setApellido(u.getApellido());
				dto.setFoto(u.getFoto());
			});
		
		return dto;
	}*/

}
