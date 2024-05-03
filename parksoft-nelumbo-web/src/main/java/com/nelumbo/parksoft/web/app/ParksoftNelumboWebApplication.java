package com.nelumbo.parksoft.web.app;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nelumbo.parksoft.web.app.models.entities.Role;
import com.nelumbo.parksoft.web.app.models.entities.Usuario;
import com.nelumbo.parksoft.web.app.models.enums.RolEnum;
import com.nelumbo.parksoft.web.app.repositories.RoleRepository;
import com.nelumbo.parksoft.web.app.repositories.UsuarioRepository;



@SpringBootApplication
public class ParksoftNelumboWebApplication implements CommandLineRunner{
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired 
	private RoleRepository roleRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(ParksoftNelumboWebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Optional<Usuario> user= usuarioRepository.findByCorreo("admin@mail.com");
		
		if(user.isEmpty()) {
			Usuario userAdmin= new Usuario();
			userAdmin.setEmail("admin@mail.com");
			userAdmin.setPassword(passwordEncoder.encode("admin"));
			userAdmin.setEnabled(true);
			
			Optional<Role> rolAdmin= roleRepository.findByRol(RolEnum.ROLE_ADMIN);
			rolAdmin.ifPresentOrElse(r-> userAdmin.getRoles().add(r),()-> userAdmin.getRoles().add(new Role(RolEnum.ROLE_ADMIN)));
			
			usuarioRepository.save(userAdmin);
			
		}
		
	}
	
}
