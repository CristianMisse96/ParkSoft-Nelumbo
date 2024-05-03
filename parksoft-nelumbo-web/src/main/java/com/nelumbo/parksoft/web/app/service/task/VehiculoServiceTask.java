package com.nelumbo.parksoft.web.app.service.task;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.nelumbo.parksoft.web.app.models.dto.PlacaCantidadDTO;
import com.nelumbo.parksoft.web.app.models.dto.RespuestaServicioDTO;
import com.nelumbo.parksoft.web.app.models.dto.VehiculoDTO;

public interface VehiculoServiceTask {

	ResponseEntity<RespuestaServicioDTO<String>> registarIngreso(VehiculoDTO vehiculoDTO, Long parkingId);

	ResponseEntity<RespuestaServicioDTO<String>> registarSalida(VehiculoDTO vehiculoDTO, Long parkingId);

	ResponseEntity<RespuestaServicioDTO<List<PlacaCantidadDTO>>> topTen();

	ResponseEntity<RespuestaServicioDTO<List<PlacaCantidadDTO>>> topTenParking(Long parkingId);

	ResponseEntity<RespuestaServicioDTO<List<PlacaCantidadDTO>>> firstTime(Long parkingId);

	ResponseEntity<RespuestaServicioDTO<List<VehiculoDTO>>> buscarPorPlaca(String placa);

	ResponseEntity<RespuestaServicioDTO<String>> earningsToday(Long parkingId);

	ResponseEntity<RespuestaServicioDTO<String>> earningsWeek(Long parkingId);

	ResponseEntity<RespuestaServicioDTO<String>> earningsMonth(Long parkingId);

	ResponseEntity<RespuestaServicioDTO<String>> earningsYear(Long parkingId);


}
