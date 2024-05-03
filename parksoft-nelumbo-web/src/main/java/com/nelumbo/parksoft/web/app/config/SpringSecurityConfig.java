package com.nelumbo.parksoft.web.app.config;

import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.ROLE_ADMIN;
import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.ROLE_SOCIO;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.nelumbo.parksoft.web.app.auth.filter.JWTAuthenticationFilter;
import com.nelumbo.parksoft.web.app.auth.filter.JWTValidationFilter;
import com.nelumbo.parksoft.web.app.auth.service.JWTService;


/**
 * <p>
 * Titulo: Proyecto ParkSoft
 * </p>
 * <p>
 * Descripción: Clase de configuración de seguridad en la  aplicación.
 * </p>
 *
 * @author Cristian Misse
 *
 **/

@Configuration
public class SpringSecurityConfig {
	
	
	private AuthenticationConfiguration authenticationConfiguration;
	private JWTService jwtService;
	
	public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTService jwtService) {
		this.authenticationConfiguration=authenticationConfiguration;
		this.jwtService=jwtService;
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		return http.authorizeHttpRequests((authz)-> authz
				.requestMatchers(HttpMethod.POST,"/api/users/create").hasRole(ROLE_ADMIN)
				.requestMatchers(HttpMethod.POST,"/api/parking/create/**").hasRole(ROLE_ADMIN)
				.requestMatchers(HttpMethod.GET,"/api/parking/vehiculos/**").hasRole(ROLE_ADMIN)
				.requestMatchers(HttpMethod.GET,"/api/parking/all").hasRole(ROLE_ADMIN)
				.requestMatchers(HttpMethod.DELETE,"/api/parking/inactivar/**").hasRole(ROLE_ADMIN)
				.requestMatchers(HttpMethod.PUT,"/api/parking/activar/**").hasRole(ROLE_ADMIN)
				.requestMatchers(HttpMethod.PUT,"/api/parking/update/**").hasRole(ROLE_ADMIN)
				
				.requestMatchers(HttpMethod.GET,"/api/parking/user/detail").hasRole(ROLE_SOCIO)
				.requestMatchers(HttpMethod.GET,"/api/parking/user/vehiculos/**").hasRole(ROLE_SOCIO)
				.requestMatchers(HttpMethod.GET,"/api/vehiculo/earnings/**").hasRole(ROLE_SOCIO)
				.requestMatchers(HttpMethod.POST,"/api/vehiculo/ingreso/**").hasRole(ROLE_SOCIO)
				.requestMatchers(HttpMethod.POST,"/api/vehiculo/salida/**").hasRole(ROLE_SOCIO)
				.anyRequest().authenticated())
				.addFilter(new JWTAuthenticationFilter(authenticationManager(),jwtService))
				.addFilter(new JWTValidationFilter(authenticationManager(),jwtService))
				.csrf(config-> config.disable())
				.sessionManagement(magnament-> magnament.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.build();
	}
	
	AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}
