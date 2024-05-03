package com.nelumbo.parksoft.web.app.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nelumbo.parksoft.web.app.models.dto.MensajeDTO;
import com.nelumbo.parksoft.web.app.models.dto.PlacaCantidadDTO;
import com.nelumbo.parksoft.web.app.models.dto.RespuestaServicioDTO;
import com.nelumbo.parksoft.web.app.models.dto.VehiculoDTO;
import com.nelumbo.parksoft.web.app.models.enums.SeverityEnum;
import com.nelumbo.parksoft.web.app.service.task.VehiculoServiceTask;
import com.nelumbo.parksoft.web.app.util.Messages;

import jakarta.validation.Valid;

/**
 * <p>
 * Titulo: Proyecto ParkSoft
 * </p>
 * <p>
 * Descripción: controlador REST para realizar procesos de gestión de vehiculos.
 * </p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 *
 **/

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/vehiculo")
public class VehiculoRestController {
	
	private static final String KEY_ERROR_GENERICO = "error.peticion.generico";
	
	private Messages messages;
	private VehiculoServiceTask vehiculoServiceTask;
	
	public VehiculoRestController(Messages messages,VehiculoServiceTask vehiculoServiceTask) {
		this.messages=messages;
		this.vehiculoServiceTask=vehiculoServiceTask;
	}
	
	@PostMapping(value="/ingreso/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<String>> newInput(
			@Valid @RequestBody VehiculoDTO vehiculoDTO,BindingResult result,
			@PathVariable Long parkingId){
		
		if (result.hasErrors()) {
			RespuestaServicioDTO<String> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		
		return vehiculoServiceTask.registarIngreso(vehiculoDTO,parkingId);
	}
	
	@PostMapping(value="/salida/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<String>> newOutput(
			@Valid @RequestBody VehiculoDTO vehiculoDTO,BindingResult result,
			@PathVariable Long parkingId){
		
		if (result.hasErrors()) {
			RespuestaServicioDTO<String> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		
		return vehiculoServiceTask.registarSalida(vehiculoDTO,parkingId);
	}
	
	@GetMapping(value = "/topten")
	public ResponseEntity<RespuestaServicioDTO<List<PlacaCantidadDTO>>> topTen(){
		
		return vehiculoServiceTask.topTen();
	}
	
	@GetMapping(value = "/topten/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<List<PlacaCantidadDTO>>> topTenParking(@PathVariable Long parkingId){
		
		return vehiculoServiceTask.topTenParking(parkingId);
	}
	
	@GetMapping(value = "/first/time/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<List<PlacaCantidadDTO>>> firstTime(@PathVariable Long parkingId){
		
		return vehiculoServiceTask.firstTime(parkingId);
	}
	
	@GetMapping("/buscar-vehiculos")
	public ResponseEntity<RespuestaServicioDTO<List<VehiculoDTO>>> buscarVehiculosPorTexto(
			@RequestParam String placa) {
		
	      return vehiculoServiceTask.buscarPorPlaca(placa);
	}
	
	@GetMapping("/earnings/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<String>> earnings(@PathVariable Long parkingId) {
		
	      return vehiculoServiceTask.earningsToday(parkingId);
	}
	
	@GetMapping("/earnings/week/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<String>> earningsWeek(@PathVariable Long parkingId) {
		
	      return vehiculoServiceTask.earningsWeek(parkingId);
	}
	
	@GetMapping("/earnings/mes/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<String>> earningsMonth(@PathVariable Long parkingId) {
		
	      return vehiculoServiceTask.earningsMonth(parkingId);
	}
	
	@GetMapping("/earnings/year/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<String>> earningsYear(@PathVariable Long parkingId) {
		
	      return vehiculoServiceTask.earningsYear(parkingId);
	}
}
