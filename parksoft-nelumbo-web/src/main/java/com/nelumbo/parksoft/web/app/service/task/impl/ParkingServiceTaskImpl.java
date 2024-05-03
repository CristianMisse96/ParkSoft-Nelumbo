package com.nelumbo.parksoft.web.app.service.task.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nelumbo.parksoft.web.app.exception.ParkSoftException;
import com.nelumbo.parksoft.web.app.models.dto.MensajeDTO;
import com.nelumbo.parksoft.web.app.models.dto.ParkingDTO;
import com.nelumbo.parksoft.web.app.models.dto.RespuestaServicioDTO;
import com.nelumbo.parksoft.web.app.models.dto.VehiculoDTO;
import com.nelumbo.parksoft.web.app.models.enums.SeverityEnum;
import com.nelumbo.parksoft.web.app.service.entity.ParkingService;
import com.nelumbo.parksoft.web.app.service.task.ParkingServiceTask;
import com.nelumbo.parksoft.web.app.util.ExcepcionUtil;
import com.nelumbo.parksoft.web.app.util.Messages;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ParkingServiceTaskImpl implements ParkingServiceTask {
	
	private static final String EXITO_ACTUALIZAR_PARKING = "exito.actualizar.parking";
	private static final String PARKING_ACTIVAR_SUCCESS = "parking.activar.success";
	private static final String DELETE_DETALLE = "delete.detalle";
	private static final String KEY_SUCCESS_REG_EXITOSO= "parking.registro.exitoso";
	private static final String KEY_ERROR_LABEL 	= "error";
	private static final String KEY_SUCCESS= "success";
	private static final String ERROR_INESPERADO_PARKING = "error inesperado parking -> ";
	private static final String KEY_ERROR_GENERICO	= "error.generico";
	private static final String SUCCESS_DETALLE = "success.detalle";
	
	private Messages messages;
	private ParkingService parkingService;
	
	public ParkingServiceTaskImpl(Messages messages,ParkingService parkingService) {
		this.messages=messages;
		this.parkingService=parkingService;
	}
	
	@Override
	public ResponseEntity<RespuestaServicioDTO<ParkingDTO>> registarParking(ParkingDTO parkingDTO, Long userId) {
		
		RespuestaServicioDTO<ParkingDTO> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			ParkingDTO respuesta= parkingService.registrarParking(parkingDTO,userId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(KEY_SUCCESS_REG_EXITOSO).replace("{1}", parkingDTO.getName())));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_PARKING + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<List<VehiculoDTO>>> obtenerVehiculosParking(Long parkingId) {
		
		RespuestaServicioDTO<List<VehiculoDTO>> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			List<VehiculoDTO> respuesta= parkingService.getVehiculosParking(parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_PARKING + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<List<ParkingDTO>>> obtenerParkingUser(String usernameClaim) {
		
		RespuestaServicioDTO<List<ParkingDTO>> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			List<ParkingDTO> respuesta= parkingService.getParkingUser(usernameClaim);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_PARKING + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<List<VehiculoDTO>>> obtenerVehiculosParkingUser(String usernameClaim,
			Long parkingId) {
		
		RespuestaServicioDTO<List<VehiculoDTO>> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			List<VehiculoDTO> respuesta= parkingService.getVehiculosParkingUser(usernameClaim,parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_PARKING + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<List<ParkingDTO>>> obtenerParkings() {
		
		RespuestaServicioDTO<List<ParkingDTO>> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			List<ParkingDTO> respuesta= parkingService.getParkings();
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_PARKING + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> eliminarParking(Long parkingId) {
		
		RespuestaServicioDTO<String> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			String respuesta= parkingService.deleteParking(parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(DELETE_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_PARKING + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
		
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> activarParking(Long parkingId) {
		
		RespuestaServicioDTO<String> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			String respuesta= parkingService.activarParking(parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(PARKING_ACTIVAR_SUCCESS)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_PARKING + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<ParkingDTO>> updateParking(Long parkingId, ParkingDTO parkingDTO) {
		
		RespuestaServicioDTO<ParkingDTO> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			ParkingDTO respuesta= parkingService.updateParking(parkingDTO,parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(EXITO_ACTUALIZAR_PARKING)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.CONFLICT);
			}else {
			    log.error(ERROR_INESPERADO_PARKING + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
		
	}

	

}
