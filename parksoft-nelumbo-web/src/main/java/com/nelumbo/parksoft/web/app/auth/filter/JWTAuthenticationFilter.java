package com.nelumbo.parksoft.web.app.auth.filter;

import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.CONTENT_TYPE;
import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.HEADER_AUTHORIZATION;
import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.MESSAGE;
import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.PREFIX_TOKEN;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nelumbo.parksoft.web.app.auth.service.JWTService;
import com.nelumbo.parksoft.web.app.models.entities.Usuario;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;
	private JWTService jWTService;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jWTService) {
		this.authenticationManager = authenticationManager;
		this.jWTService=jWTService;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login","POST"));
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		Usuario user= new Usuario();
		
		try {
			 user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class) ;
			 
			 if (user.getEmail() == null || user.getEmail().isEmpty() ||
					 user.getPassword() == null ||user.getPassword().isEmpty()) {
		          
				 Map<String, String> body= new HashMap<>();
				 
					body.put(MESSAGE, "Empty");
					body.put("error", "El nombre de usuario y la contraseña son obligatorios");
					response.getWriter().write(new ObjectMapper().writeValueAsString(body));
					response.setContentType(CONTENT_TYPE);
					response.setStatus(HttpStatus.BAD_REQUEST.value());
					return null;
		      }
			 	
				logger.info("Username desde request parameter (raw): " +user.getEmail());
				logger.info("Password desde request parameter (raw): " +user.getPassword());
				
				
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
		UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
				user.getEmail(), user.getPassword());
		
		
		return authenticationManager.authenticate(authToken);
		
	}


	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		//UsuarioDTO usuarioDTO=  jWTService.findUser(authResult.getName());
		String token = jWTService.create(authResult);
		
		response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
		Map<String, Object> body= new HashMap<>();
		body.put("token", token);
		body.put(MESSAGE, String.format("Bienvenido %s, ha iniciado sesión con éxito!", authResult.getName()));
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(HttpStatus.OK.value());
	}


	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		Map<String, String> body= new HashMap<>();
		if (failed instanceof DisabledException) {
	        body.put(MESSAGE, "Tu cuenta está deshabilitada. Por favor, contacta al administrador.");
	    } else {
	        body.put(MESSAGE, "correo o password incorrectos");
	        
	    }
		body.put("error", failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(body));
		response.setContentType(CONTENT_TYPE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
	
	
	
	
	
	
	
	
}
