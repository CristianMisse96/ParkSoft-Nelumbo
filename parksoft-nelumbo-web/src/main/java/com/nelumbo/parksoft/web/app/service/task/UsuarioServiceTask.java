package com.nelumbo.parksoft.web.app.service.task;

import org.springframework.http.ResponseEntity;

import com.nelumbo.parksoft.web.app.models.dto.RespuestaServicioDTO;
import com.nelumbo.parksoft.web.app.models.dto.UsuarioDTO;

/**
 * <p>
 * Titulo: Proyecto ParkSoft
 * </p>
 * <p>
 * Descripción: Interfaz para definir los metodos realizados sobra la entidad Usuario 
 * con sus correspondientes validaciones
 * </p>
 *
 * @author Cristian Misse
 *
 **/
public interface UsuarioServiceTask {

	ResponseEntity<RespuestaServicioDTO<UsuarioDTO>> registarUsuario(UsuarioDTO userDTO);

}
