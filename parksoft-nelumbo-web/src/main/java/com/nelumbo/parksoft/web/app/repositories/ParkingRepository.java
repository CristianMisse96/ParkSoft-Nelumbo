package com.nelumbo.parksoft.web.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nelumbo.parksoft.web.app.models.entities.Parking;
import com.nelumbo.parksoft.web.app.models.enums.EstadoVehiculoEnum;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
	
	@Query("SELECT p FROM Parking p JOIN p.admins u WHERE u.id = :userId")
    List<Parking> findByAdminsId(Long userId);
	
	@Query("SELECT COUNT(v) FROM Vehiculo v WHERE v.parking.id = :parkingId AND v.estado = :estado")
	Integer countByParkingIdAndEstado( Long parkingId, EstadoVehiculoEnum estado);

}
