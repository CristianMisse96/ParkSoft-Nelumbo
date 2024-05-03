package com.nelumbo.parksoft.web.app.service.entity;

import com.nelumbo.parksoft.web.app.exception.ParkSoftException;
import com.nelumbo.parksoft.web.app.models.dto.UsuarioDTO;


public interface UsuarioService {

	UsuarioDTO registrarUsuario(UsuarioDTO userDTO) throws ParkSoftException;

}
