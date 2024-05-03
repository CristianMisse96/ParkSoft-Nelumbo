package com.nelumbo.parksoft.web.app.service.entity.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nelumbo.parksoft.web.app.exception.ParkSoftException;
import com.nelumbo.parksoft.web.app.models.dto.UsuarioDTO;
import com.nelumbo.parksoft.web.app.models.entities.Role;
import com.nelumbo.parksoft.web.app.models.entities.Usuario;
import com.nelumbo.parksoft.web.app.models.enums.RolEnum;
import com.nelumbo.parksoft.web.app.repositories.RoleRepository;
import com.nelumbo.parksoft.web.app.repositories.UsuarioRepository;
import com.nelumbo.parksoft.web.app.service.entity.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private static final String ERROR_USUARIO_REGISTRAR_EXISTE = "error.usuario.registrar.existe";
	
	private UsuarioRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private ModelMapper modelMapper;
	private RoleRepository roleRepository;
	
	public UsuarioServiceImpl(UsuarioRepository userRepository,PasswordEncoder passwordEncoder,ModelMapper modelMapper,
            RoleRepository roleRepository) {
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
		this.modelMapper=modelMapper;
		this.roleRepository= roleRepository;

	}
	
	@Override
	@Transactional
	public UsuarioDTO registrarUsuario(UsuarioDTO userDTO) throws ParkSoftException {
		
		if(userRepository.existsByEmail(userDTO.getEmail())) {
			throw new ParkSoftException(ERROR_USUARIO_REGISTRAR_EXISTE);
		}
		
		Usuario user= modelMapper.map(userDTO, Usuario.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		if(userDTO.isSocio()) {
			userDTO.setAdmin(false);
			Optional<Role> rolSocio= roleRepository.findByRol(RolEnum.ROLE_SOCIO);
			rolSocio.ifPresentOrElse(r-> user.getRoles().add(r),()-> user.getRoles().add(new Role(RolEnum.ROLE_SOCIO)));
		}
		
		if(userDTO.isAdmin()) {
			Optional<Role> rolAdmin= roleRepository.findByRol(RolEnum.ROLE_ADMIN);
			rolAdmin.ifPresentOrElse(r-> user.getRoles().add(r),()-> user.getRoles().add(new Role(RolEnum.ROLE_ADMIN)));
		}
		
		return modelMapper.map(userRepository.save(user), UsuarioDTO.class);
	}

}
