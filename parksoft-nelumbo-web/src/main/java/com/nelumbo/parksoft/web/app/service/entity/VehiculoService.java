package com.nelumbo.parksoft.web.app.service.entity;

import java.util.List;

import com.nelumbo.parksoft.web.app.exception.ParkSoftException;
import com.nelumbo.parksoft.web.app.models.dto.PlacaCantidadDTO;
import com.nelumbo.parksoft.web.app.models.dto.VehiculoDTO;

public interface VehiculoService {

	String registrarIngreso(VehiculoDTO vehiculoDTO, Long parkingId) throws ParkSoftException;

	String registrarSalida(VehiculoDTO vehiculoDTO, Long parkingId) throws ParkSoftException;

	List<PlacaCantidadDTO> topTen() throws ParkSoftException;

	List<PlacaCantidadDTO> topTenParking(Long parkingId) throws ParkSoftException;

	List<PlacaCantidadDTO> firstTime(Long parkingId) throws ParkSoftException;

	List<VehiculoDTO> buscarPorPlaca(String placa) throws ParkSoftException;

	String earningsToday(Long parkingId) throws ParkSoftException;

	String earningsWeek(Long parkingId) throws ParkSoftException;

	String earningsMonth(Long parkingId) throws ParkSoftException;

	String earningsYear(Long parkingId) throws ParkSoftException;

}
