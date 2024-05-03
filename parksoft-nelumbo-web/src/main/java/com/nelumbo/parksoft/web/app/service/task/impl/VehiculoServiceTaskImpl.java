package com.nelumbo.parksoft.web.app.service.task.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nelumbo.parksoft.web.app.exception.ParkSoftException;
import com.nelumbo.parksoft.web.app.models.dto.MensajeDTO;
import com.nelumbo.parksoft.web.app.models.dto.PlacaCantidadDTO;
import com.nelumbo.parksoft.web.app.models.dto.RespuestaServicioDTO;
import com.nelumbo.parksoft.web.app.models.dto.VehiculoDTO;
import com.nelumbo.parksoft.web.app.models.enums.SeverityEnum;
import com.nelumbo.parksoft.web.app.service.entity.VehiculoService;
import com.nelumbo.parksoft.web.app.service.task.VehiculoServiceTask;
import com.nelumbo.parksoft.web.app.util.ExcepcionUtil;
import com.nelumbo.parksoft.web.app.util.Messages;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class VehiculoServiceTaskImpl implements VehiculoServiceTask {
	
	private static final String ERROR_INESPERADO_INGRESO_VEHICULO = "error inesperado ingreso -> ";
	private static final String KEY_ERROR_LABEL 	= "error";
	private static final String KEY_SUCCESS= "success";
	private static final String KEY_ERROR_GENERICO	= "error.generico";
	private static final String KEY_SUCCESS_REG_EXITOSO= "vehiculo.registro.exitoso";
	private static final String KEY_SUCCESS_OUT_EXITOSO="vehiculo.salida.exitoso";
	private static final String SUCCESS_DETALLE = "success.detalle";
	
	private Messages messages;
	private VehiculoService vehiculoService;
	
	public VehiculoServiceTaskImpl(Messages messages,VehiculoService vehiculoService) {
		this.messages=messages;
		this.vehiculoService=vehiculoService;
	}
	
	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> registarIngreso(VehiculoDTO vehiculoDTO, Long parkingId) {
		
		RespuestaServicioDTO<String> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			String respuesta= vehiculoService.registrarIngreso(vehiculoDTO,parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(KEY_SUCCESS_REG_EXITOSO).replace("{1}", vehiculoDTO.getPlaca().toUpperCase())));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.BAD_REQUEST);
			}else {
			    log.error(ERROR_INESPERADO_INGRESO_VEHICULO + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.CREATED);
		
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> registarSalida(VehiculoDTO vehiculoDTO, Long parkingId) {
		
		RespuestaServicioDTO<String> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			String respuesta= vehiculoService.registrarSalida(vehiculoDTO,parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(KEY_SUCCESS_OUT_EXITOSO).replace("{1}", vehiculoDTO.getPlaca().toUpperCase())));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.BAD_REQUEST);
			}else {
			    log.error(ERROR_INESPERADO_INGRESO_VEHICULO + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<List<PlacaCantidadDTO>>> topTen() {
		
		RespuestaServicioDTO<List<PlacaCantidadDTO>> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			List<PlacaCantidadDTO> respuesta= vehiculoService.topTen();
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.BAD_REQUEST);
			}else {
			    log.error(ERROR_INESPERADO_INGRESO_VEHICULO + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<List<PlacaCantidadDTO>>> topTenParking(Long parkingId) {
		
		RespuestaServicioDTO<List<PlacaCantidadDTO>> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			List<PlacaCantidadDTO> respuesta= vehiculoService.topTenParking(parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.BAD_REQUEST);
			}else {
			    log.error(ERROR_INESPERADO_INGRESO_VEHICULO + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<List<PlacaCantidadDTO>>> firstTime(Long parkingId) {
		
		RespuestaServicioDTO<List<PlacaCantidadDTO>> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			List<PlacaCantidadDTO> respuesta= vehiculoService.firstTime(parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.BAD_REQUEST);
			}else {
			    log.error(ERROR_INESPERADO_INGRESO_VEHICULO + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<List<VehiculoDTO>>> buscarPorPlaca(String placa) {
		
		RespuestaServicioDTO<List<VehiculoDTO>> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			List<VehiculoDTO> respuesta= vehiculoService.buscarPorPlaca(placa);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.BAD_REQUEST);
			}else {
			    log.error(ERROR_INESPERADO_INGRESO_VEHICULO + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> earningsToday(Long parkingId) {
		
		RespuestaServicioDTO<String> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			
			String respuesta= vehiculoService.earningsToday(parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.BAD_REQUEST);
			}else {
			    log.error(ERROR_INESPERADO_INGRESO_VEHICULO + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> earningsWeek(Long parkingId) {
		
		RespuestaServicioDTO<String> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			
			String respuesta= vehiculoService.earningsWeek(parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.BAD_REQUEST);
			}else {
			    log.error(ERROR_INESPERADO_INGRESO_VEHICULO + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> earningsMonth(Long parkingId) {
		
		RespuestaServicioDTO<String> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			
			String respuesta= vehiculoService.earningsMonth(parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.BAD_REQUEST);
			}else {
			    log.error(ERROR_INESPERADO_INGRESO_VEHICULO + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<RespuestaServicioDTO<String>> earningsYear(Long parkingId) {
		
		RespuestaServicioDTO<String> respuestaServicio = new RespuestaServicioDTO<>();
		String uuid = UUID.randomUUID().toString();
		
		try {
			
			String respuesta= vehiculoService.earningsYear(parkingId);
			
			respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.SUCCESS, messages.getKey(KEY_SUCCESS), messages.getKey(SUCCESS_DETALLE)));
			respuestaServicio.setOk(true);
			respuestaServicio.setNegocio(respuesta); 
		}catch (Exception e) {
			if (ExcepcionUtil.contains(e, ParkSoftException.class)) {
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL),messages.getKey(e.getMessage())));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.BAD_REQUEST);
			}else {
			    log.error(ERROR_INESPERADO_INGRESO_VEHICULO + e.getMessage());
				respuestaServicio.setMensajeDTO(new MensajeDTO(SeverityEnum.ERROR, messages.getKey(KEY_ERROR_LABEL), messages.getKey(KEY_ERROR_GENERICO).replace("{1}", uuid)));
				return new ResponseEntity<>(respuestaServicio, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return new ResponseEntity<>(respuestaServicio, HttpStatus.OK);
	}

}
