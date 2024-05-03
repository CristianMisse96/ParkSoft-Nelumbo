package com.nelumbo.parksoft.web.app.service.task;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.nelumbo.parksoft.web.app.models.dto.ParkingDTO;
import com.nelumbo.parksoft.web.app.models.dto.RespuestaServicioDTO;
import com.nelumbo.parksoft.web.app.models.dto.VehiculoDTO;

public interface ParkingServiceTask {

	ResponseEntity<RespuestaServicioDTO<ParkingDTO>> registarParking(ParkingDTO parkingDTO, Long userId);

	ResponseEntity<RespuestaServicioDTO<List<VehiculoDTO>>> obtenerVehiculosParking(Long parkingId);

	ResponseEntity<RespuestaServicioDTO<List<ParkingDTO>>> obtenerParkingUser(String usernameClaim);

	ResponseEntity<RespuestaServicioDTO<List<VehiculoDTO>>> obtenerVehiculosParkingUser(String usernameClaim, Long parkingId);

	ResponseEntity<RespuestaServicioDTO<List<ParkingDTO>>> obtenerParkings();

	ResponseEntity<RespuestaServicioDTO<String>> eliminarParking(Long parkingId);

	ResponseEntity<RespuestaServicioDTO<String>> activarParking(Long parkingId);

	ResponseEntity<RespuestaServicioDTO<ParkingDTO>> updateParking(Long parkingId, ParkingDTO parkingDTO);


}
