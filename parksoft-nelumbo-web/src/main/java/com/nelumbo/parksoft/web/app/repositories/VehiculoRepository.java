package com.nelumbo.parksoft.web.app.repositories;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nelumbo.parksoft.web.app.models.dto.PlacaCantidadDTO;
import com.nelumbo.parksoft.web.app.models.entities.Vehiculo;
import com.nelumbo.parksoft.web.app.models.enums.EstadoVehiculoEnum;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
	
	 @Query("SELECT COUNT(v) FROM Vehiculo v WHERE v.parking.id = :parkingId AND v.estado = :estado")
	 Integer countByParkingIdAndEstado( Long parkingId, EstadoVehiculoEnum estado);
	 
	 boolean existsByPlacaAndEstado(String placa, EstadoVehiculoEnum estado);
	 
	 @Query("SELECT v FROM Vehiculo v WHERE v.placa = :placa AND v.estado = :estado AND v.parking.id = :parkingId")
	 Optional<Vehiculo> findByPlacaAndEstadoAndParking(String placa,EstadoVehiculoEnum estado,Long parkingId);
	 
	 @Query(value = "SELECT v.placa AS placa,COUNT(v.placa) AS cantidad "
	 			  + "FROM {h-schema}vehiculos v "
	 			  + "GROUP BY v.placa "
	 			  + "ORDER BY COUNT(v.placa) DESC "
	 			  + "LIMIT 10 ", nativeQuery =true)
	 List<PlacaCantidadDTO> findTopTen();
	 
	 @Query(value = "SELECT v.placa AS placa, COUNT(v.placa) AS cantidad "
	 			  + "FROM {h-schema}vehiculos v "
	 			  + "WHERE v.id_parking = :parkingId "
	 			  + "GROUP BY v.placa "
	 			  + "ORDER BY COUNT(v.placa) DESC "
	 			  + "LIMIT 10", nativeQuery = true)
	 List<PlacaCantidadDTO> findTopTenParking(Long parkingId);
	 
	 @Query(value = "SELECT v.placa AS placa, COUNT(v.placa) AS cantidad "
	 		+ "FROM {h-schema}vehiculos v "
	 		+ "WHERE v.id_parking = :parkingId "
	 		+ "GROUP BY v.placa "
	 		+ "HAVING COUNT(v.placa) = 1", nativeQuery = true)
	 List<PlacaCantidadDTO> findFirstTime(Long parkingId);

	 List<Vehiculo> findByPlacaContaining(String placa);
	 
	 @Query("SELECT v FROM Vehiculo v WHERE (v.horaIngreso >= :inicioDelDia AND v.horaIngreso <= :finDelDia) OR (v.horaSalida >= :inicioDelDia AND v.horaSalida <= :finDelDia)")
	 List<Vehiculo> findByHoraIngresoOrHoraSalidaBetween(Timestamp inicioDelDia,  Timestamp finDelDia);
	 
	 
}
