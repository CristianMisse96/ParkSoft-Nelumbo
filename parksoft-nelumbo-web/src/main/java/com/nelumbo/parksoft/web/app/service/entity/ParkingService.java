package com.nelumbo.parksoft.web.app.service.entity;

import java.util.List;

import com.nelumbo.parksoft.web.app.exception.ParkSoftException;
import com.nelumbo.parksoft.web.app.models.dto.ParkingDTO;
import com.nelumbo.parksoft.web.app.models.dto.VehiculoDTO;

public interface ParkingService {

	ParkingDTO registrarParking(ParkingDTO parkingDTO, Long userId) throws ParkSoftException;

	List<VehiculoDTO> getVehiculosParking(Long parkingId) throws ParkSoftException;

	List<ParkingDTO> getParkingUser(String usernameClaim) throws ParkSoftException;

	List<VehiculoDTO> getVehiculosParkingUser(String usernameClaim, Long parkingId) throws ParkSoftException;

	List<ParkingDTO> getParkings() throws ParkSoftException;

	String deleteParking(Long parkingId) throws ParkSoftException;

	String activarParking(Long parkingId) throws ParkSoftException;

	ParkingDTO updateParking(ParkingDTO parkingDTO, Long parkingId) throws ParkSoftException;

}
