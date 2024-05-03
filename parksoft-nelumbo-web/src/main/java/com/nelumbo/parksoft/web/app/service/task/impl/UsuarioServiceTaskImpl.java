package com.nelumbo.parksoft.web.app.service.task.impl;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nelumbo.parksoft.web.app.exception.ParkSoftException;
import com.nelumbo.parksoft.web.app.models.dto.MensajeDTO;
import com.nelumbo.parksoft.web.app.models.dto.RespuestaServicioDTO;
import com.nelumbo.parksoft.web.app.models.dto.UsuarioDTO;
import com.nelumbo.parksoft.web.app.models.enums.SeverityEnum;
import com.nelumbo.parksoft.web.app.service.entity.UsuarioService;
import com.nelumbo.parksoft.web.app.service.task.UsuarioServiceTask;
import com.nelumbo.parksoft.web.app.util.ExcepcionUtil;
import com.nelumbo.parksoft.web.app.util.Messages;

import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * Titulo: Proyecto ParkSoft
 * </p>
 * <p>
 * Descripción: Implementación de la interfaz UsuarioServiceTask
 * </p>
 *
 * @author Cristian Misse
 *
 **/

@Service
@Log4j2
public class UsuarioServiceTaskImpl implements UsuarioServiceTask {
	
	private static final String KEY_SUCCESS= "success";
	private static final String KEY_ERROR_LABEL 	= "error";
	private static final String ERROR_INESPERADO_USUARIO = "error inesperado usuario -> "; 
	private static final String KEY_SUCCESS_REG_EXITOSO= "usuario.registro.exitoso";
	private static final String KEY_ERROR_GENERICO	= "error.generico";
	
	private UsuarioService userService;
	private Messages messages;
	

	public UsuarioServiceTaskImpl(UsuarioService userService, Messages messages) {
		this.userService= userService;
		this.messages=messages;
	}
	
	@Override
	public ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> registarUsuario(UsuarioDTO userDTO) {
		
		RespuestaServicioDTO<UsuarioDTO> respuestaServicioUserDTO = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try{
			
			if(!userDTO.isAdmin() && !userDTO.isSocio()) {
				userDTO.setSocio(true);
			}
			userDTO.setEnabled(Boolean.TRUE);
			UsuarioDTO respuesta= userService.registrarUsuario(userDTO);
			
			respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(KEY_SUCCESS_REG_EXITOSO).replace("{1}", userDTO.getEmail())));
			respuestaServicioUserDTO.setOk(true);
			respuestaServicioUserDTO.setNegocio(respuesta); 
			
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_USUARIO + e.getMessage());
				respuestaServicioUserDTO.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		
		return new ResponseEntity<>(respuestaServicioUserDTO, HttpStatus.CREATED);
	}

}
