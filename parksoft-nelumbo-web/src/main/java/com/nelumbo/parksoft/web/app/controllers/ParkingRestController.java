package com.nelumbo.parksoft.web.app.controllers;



import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.HEADER_AUTHORIZATION;
import static com.nelumbo.parksoft.web.app.util.SpringSecurityConstantes.SECRET_KEY;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nelumbo.parksoft.web.app.models.dto.MensajeDTO;
import com.nelumbo.parksoft.web.app.models.dto.ParkingDTO;
import com.nelumbo.parksoft.web.app.models.dto.RespuestaServicioDTO;
import com.nelumbo.parksoft.web.app.models.dto.VehiculoDTO;
import com.nelumbo.parksoft.web.app.models.enums.SeverityEnum;
import com.nelumbo.parksoft.web.app.service.task.ParkingServiceTask;
import com.nelumbo.parksoft.web.app.util.Messages;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;

/**
 * <p>
 * Titulo: Proyecto ParkSoft
 * </p>
 * <p>
 * Descripción: controlador REST para realizar procesos de gestión de parueaderos.
 * </p>
 *
 * @author Cristian Misse - cgmisse@gmail.com
 *
 **/

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/parking")
public class ParkingRestController {
	
	private static final String KEY_ERROR_GENERICO = "error.peticion.generico";
	
	private Messages messages;
	private ParkingServiceTask parkingServiceTask;
	
	public ParkingRestController(Messages messages, ParkingServiceTask parkingServiceTask) {
		this.messages=messages;
		this.parkingServiceTask=parkingServiceTask;
	}
	
	@PostMapping(value="/create/{userId}")
	public ResponseEntity<RespuestaServicioDTO<ParkingDTO>> newParking(
			@Valid @RequestBody ParkingDTO parkingDTO,BindingResult result,
			@PathVariable Long userId){
		
		if (result.hasErrors()) {
			RespuestaServicioDTO<ParkingDTO> respuestaServicioDTO = new RespuestaServicioDTO<>();
			
			respuestaServicioDTO.setMensajeDTO(
					new MensajeDTO(SeverityEnum.ERROR, 
							messages.getKey(SeverityEnum.ERROR.getKey()),messages.getKey(KEY_ERROR_GENERICO)));
			
			return new ResponseEntity<>(respuestaServicioDTO, HttpStatus.BAD_REQUEST);
		}
		
		return parkingServiceTask.registarParking(parkingDTO,userId);
	}
	
	@GetMapping(value="/vehiculos/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<List<VehiculoDTO>>> getVehiculos(@PathVariable Long parkingId){
		
		return parkingServiceTask.obtenerVehiculosParking(parkingId);
	}
	
	@GetMapping(value="/user/detail")
	public ResponseEntity<RespuestaServicioDTO<List<ParkingDTO>>> getParkings(@RequestHeader(HEADER_AUTHORIZATION) String authorizationHeader){
		
		String usernameClaim = obtenerUsername(authorizationHeader);
        
		return parkingServiceTask.obtenerParkingUser(usernameClaim);
	}

	@GetMapping(value="/user/vehiculos/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<List<VehiculoDTO>>> getVehiculosParkingUser(
			@RequestHeader(HEADER_AUTHORIZATION) String authorizationHeader,
			@PathVariable Long parkingId){
		
		String usernameClaim = obtenerUsername(authorizationHeader);
		
		return parkingServiceTask.obtenerVehiculosParkingUser(usernameClaim,parkingId);
	}
	
	//CRUD parqueadero
	@GetMapping(value="/all")
	public ResponseEntity<RespuestaServicioDTO<List<ParkingDTO>>> getAll(){
		
		return parkingServiceTask.obtenerParkings();
	}
	
	@DeleteMapping(value="/inactivar/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<String>> inactivarParking(@PathVariable Long parkingId){
		
		return parkingServiceTask.eliminarParking(parkingId);
	}
	
	@PutMapping(value="/activar/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<String>> activarParking(@PathVariable Long parkingId){
		
		return parkingServiceTask.activarParking(parkingId);
	}
	
	@PutMapping(value="/update/{parkingId}")
	public ResponseEntity<RespuestaServicioDTO<ParkingDTO>> actualizarParking(
			@RequestBody ParkingDTO parkingDTO,
			@PathVariable Long parkingId){
		
		return parkingServiceTask.updateParking(parkingId,parkingDTO);
		
	}
	
	private String obtenerUsername(String authorizationHeader) {
		String token = authorizationHeader.substring(7);
        Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
         
		return (String) claims.get("username");
	}
	
}
