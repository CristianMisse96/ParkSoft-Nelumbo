package com.nelumbo.parksoft.web.app.service.entity.impl;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nelumbo.parksoft.web.app.exception.ParkSoftException;
import com.nelumbo.parksoft.web.app.models.dto.PlacaCantidadDTO;
import com.nelumbo.parksoft.web.app.models.dto.VehiculoDTO;
import com.nelumbo.parksoft.web.app.models.entities.Parking;
import com.nelumbo.parksoft.web.app.models.entities.Vehiculo;
import com.nelumbo.parksoft.web.app.models.enums.EstadoVehiculoEnum;
import com.nelumbo.parksoft.web.app.repositories.ParkingRepository;
import com.nelumbo.parksoft.web.app.repositories.VehiculoRepository;
import com.nelumbo.parksoft.web.app.service.entity.VehiculoService;

@Service
public class VehiculoServiceImpl implements VehiculoService {
	
	private static final String ERROR_VEHICULO_INGRESO_YA_EXISTE = "error.vehiculo.ingreso.ya.existe";
	private static final String ERROR_VEHICULO_INGRESO_PARQUEADERO_NO_HAY_CAPACIDAD = "error.vehiculo.ingreso.parqueadero.no.hay.capacidad";
	private static final String ERROR_SALIDA_VEHICULO_NO_EXISTE = "error.vehiculo.salida.no.existe";
	private static final String ERROR_VEHICULO_INGRESO_PARQUEADERO_INACTIVO = "error.vehiculo.ingreso.parqueadero.inactivo";
	private static final String ERROR_VEHICULO_INGRESO_PARQUEADERO_NO_EXISTE = "error.vehiculo.ingreso.parqueadero.no.existe";
	
	private VehiculoRepository vehiculoRepository;
	private ParkingRepository parkingRepository;
	private ModelMapper modelMapper;
	
	public VehiculoServiceImpl(VehiculoRepository vehiculoRepository,ParkingRepository parkingRepository,ModelMapper modelMapper) {
		this.vehiculoRepository=vehiculoRepository;
		this.parkingRepository=parkingRepository;
		this.modelMapper= modelMapper;
	}
	
	@Override
	@Transactional
	public String registrarIngreso(VehiculoDTO vehiculoDTO, Long parkingId) throws ParkSoftException {
		
		//validar que exista el parqueadero
		Parking parkingDB= parkingRepository.findById(parkingId)
											.orElseThrow(()-> new ParkSoftException(ERROR_VEHICULO_INGRESO_PARQUEADERO_NO_EXISTE));
		//validar que el parqueadero se encuentre activo
		if(parkingDB.getEnabled().equals(Boolean.FALSE)) {
			throw new ParkSoftException(ERROR_VEHICULO_INGRESO_PARQUEADERO_INACTIVO);
		}
		//validar que haya capacidad para poder parquear en el parqueadero
		if(vehiculoRepository.countByParkingIdAndEstado(parkingId, EstadoVehiculoEnum.P)
				.equals(parkingDB.getCapacidad())) {
			throw new ParkSoftException(ERROR_VEHICULO_INGRESO_PARQUEADERO_NO_HAY_CAPACIDAD);
		}
		//validar que el vehiculo no se encuentre registrado en otro parqueadero
		if(vehiculoRepository.existsByPlacaAndEstado(vehiculoDTO.getPlaca().toUpperCase(), EstadoVehiculoEnum.P)) {
			throw new ParkSoftException(ERROR_VEHICULO_INGRESO_YA_EXISTE);
		}
		
		Vehiculo vehiculo = new Vehiculo();
		vehiculo.setEstado(EstadoVehiculoEnum.P);
		vehiculo.setPlaca(vehiculoDTO.getPlaca().toUpperCase());
		vehiculo.setParking(parkingDB);
		
		Vehiculo vehiculoSave= vehiculoRepository.save(vehiculo);
		
		return "Vehiculo con placa "
				.concat(vehiculoDTO.getPlaca().toUpperCase())
				.concat(" parqueado exitosamente, Número de registro generado ")
				.concat(String.valueOf(vehiculoSave.getId()));
	}

	@Override
	@Transactional
	public String registrarSalida(VehiculoDTO vehiculoDTO, Long parkingId) throws ParkSoftException {
		//validar que exista el parqueadero
		Parking parkingDB= parkingRepository.findById(parkingId)
										.orElseThrow(()-> new ParkSoftException(ERROR_VEHICULO_INGRESO_PARQUEADERO_NO_EXISTE));
		//validar que el parqueadero se encuentre activo
		if(parkingDB.getEnabled().equals(Boolean.FALSE)) {
			throw new ParkSoftException(ERROR_VEHICULO_INGRESO_PARQUEADERO_INACTIVO);
		}
		//validar que el vehiculo este paruqeado
		Vehiculo vehiculoDB= vehiculoRepository.findByPlacaAndEstadoAndParking(vehiculoDTO.getPlaca().toUpperCase(),
											   EstadoVehiculoEnum.P, parkingId)
											  .orElseThrow(()-> new ParkSoftException(ERROR_SALIDA_VEHICULO_NO_EXISTE));
		//Existe vehiculo parqueado, se registra la salida
		vehiculoDB.setHoraSalida(new Timestamp(System.currentTimeMillis()));
		vehiculoDB.setEstado(EstadoVehiculoEnum.NP);
		
		vehiculoRepository.save(vehiculoDB);
		
		return "Salida registrada";
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlacaCantidadDTO> topTen() throws ParkSoftException {
		
		return vehiculoRepository.findTopTen();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlacaCantidadDTO> topTenParking(Long parkingId) throws ParkSoftException {
		
		return vehiculoRepository.findTopTenParking(parkingId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PlacaCantidadDTO> firstTime(Long parkingId) throws ParkSoftException {
		
		return vehiculoRepository.findFirstTime(parkingId);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<VehiculoDTO> buscarPorPlaca(String placa) throws ParkSoftException {
	      
	    List<Vehiculo> vehiculosFilter= vehiculoRepository.findByPlacaContaining(placa.toUpperCase());
	      
		return vehiculosFilter.stream()
				.map(v-> modelMapper.map(v, VehiculoDTO.class)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public String earningsToday(Long parkingId) throws ParkSoftException {
		
		LocalDate fechaActual = LocalDate.now();
	    LocalDateTime inicioDelDia = fechaActual.atTime(LocalTime.MIN);
	    LocalDateTime finDelDia = fechaActual.atTime(LocalTime.MAX);

	    return "Las ganancias del día de hoy: " + aproximarGanancias(calcularGananciasPorPeriodo(
	            Timestamp.valueOf(inicioDelDia),
	            Timestamp.valueOf(finDelDia),
	            parkingId
	    ));

	}

	@Override
	@Transactional(readOnly = true)
	public String earningsWeek(Long parkingId) throws ParkSoftException {
		
		 LocalDate fechaActual = LocalDate.now();
		    LocalDate primerDiaSemana = fechaActual.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		    LocalDate ultimoDiaSemana = fechaActual.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		    return "Las ganancias de la semana: " + aproximarGanancias(calcularGananciasPorPeriodo(
		            Timestamp.valueOf(primerDiaSemana.atTime(LocalTime.MIN)),
		            Timestamp.valueOf(ultimoDiaSemana.atTime(LocalTime.MAX)),
		            parkingId
		    ));
	}
	
	@Override
	@Transactional(readOnly = true)
	public String earningsMonth(Long parkingId) throws ParkSoftException {
		
		 LocalDate fechaActual = LocalDate.now();
		    LocalDate primerDiaMes = fechaActual.withDayOfMonth(1);
		    LocalDate ultimoDiaMes = fechaActual.withDayOfMonth(fechaActual.lengthOfMonth());

		    return "Las ganancias del mes: " + aproximarGanancias(calcularGananciasPorPeriodo(
		            Timestamp.valueOf(primerDiaMes.atTime(LocalTime.MIN)),
		            Timestamp.valueOf(ultimoDiaMes.atTime(LocalTime.MAX)),
		            parkingId
		    ));
	}
	
	@Override
	@Transactional(readOnly = true)
	public String earningsYear(Long parkingId) throws ParkSoftException {
		
		 LocalDate fechaActual = LocalDate.now();
		    LocalDate primerDiaAno = fechaActual.withDayOfYear(1);
		    LocalDate ultimoDiaAno = fechaActual.withDayOfYear(fechaActual.lengthOfYear());

		    return "Las ganancias del año: " + aproximarGanancias(calcularGananciasPorPeriodo(
		            Timestamp.valueOf(primerDiaAno.atTime(LocalTime.MIN)),
		            Timestamp.valueOf(ultimoDiaAno.atTime(LocalTime.MAX)),
		            parkingId
		    ));
		
	}
	
	private double calcularPrecioEstacionamiento(Vehiculo v) {
		
		 Timestamp horaIngreso = v.getHoraIngreso();
	     Timestamp horaSalida =  v.getHoraSalida();
	     
	     long tiempoEstacionadoMillis = horaSalida.getTime() - horaIngreso.getTime();
	     double horasEstacionado = tiempoEstacionadoMillis / (1000.0 * 60 * 60); 
	     
	     return horasEstacionado * v.getParking().getPrecio().doubleValue();
	}
	
	private int aproximarGanancias(double ganancias) {
		return (int) Math.ceil(ganancias);
	}
	
	private double calcularGananciasPorPeriodo(Timestamp inicioPeriodo, Timestamp finPeriodo, Long parkingId) {
	    List<Vehiculo> vehiculos = vehiculoRepository.findByHoraIngresoOrHoraSalidaBetween(inicioPeriodo, finPeriodo);

	    return vehiculos.stream()
	            .filter(v -> v.getEstado().equals(EstadoVehiculoEnum.NP) && v.getParking().getId().equals(parkingId))
	            .mapToDouble(this::calcularPrecioEstacionamiento)
	            .sum();
	}

}
