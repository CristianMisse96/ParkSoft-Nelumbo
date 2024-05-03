package com.nelumbo.parksoft.web.app.service.entity.impl;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nelumbo.parksoft.web.app.exception.ParkSoftException;
import com.nelumbo.parksoft.web.app.models.dto.ParkingDTO;
import com.nelumbo.parksoft.web.app.models.dto.VehiculoDTO;
import com.nelumbo.parksoft.web.app.models.entities.Parking;
import com.nelumbo.parksoft.web.app.models.entities.Usuario;
import com.nelumbo.parksoft.web.app.models.enums.EstadoVehiculoEnum;
import com.nelumbo.parksoft.web.app.models.enums.RolEnum;
import com.nelumbo.parksoft.web.app.repositories.ParkingRepository;
import com.nelumbo.parksoft.web.app.repositories.UsuarioRepository;
import com.nelumbo.parksoft.web.app.service.entity.ParkingService;

@Service
public class ParkingServiceImpl implements ParkingService {
	
	private static final String ERROR_PARKING_ACTUALIZAR_CANTIDAD_MAYOR_A_ESTACIONADOS = "error.parking.actualizar.cantidad.mayor.a.estacionados";
	private static final String ERROR_ELIMINAR_PARKING_NO_EXISTE = "error.eliminar.parking.no.existe";
	private static final String ERROR_PARQUEADERO_CONSULTAR_VEHICULOS_NO_ES_ADMIN = "error.parqueadero.consultar.vehiculos.no.es.admin";
	private static final String ERROR_CONSULTAR_VEHICULOS_PARKING_NO_EXISTE = "error.consultar.vehiculos.parking.no.existe";
	private static final String ERROR_USUARIO_PARKING_NO_ES_SOCIO = "error.usuario.parking.no.es.socio";
	private static final String ERROR_USUARIO_PARKING_NO_EXISTE = "error.usuario.parking.no.existe";
	
	private UsuarioRepository userRepository;
	private ParkingRepository parkingRepository;
	private ModelMapper modelMapper;
	
	public ParkingServiceImpl(UsuarioRepository userRepository, ModelMapper modelMapper,ParkingRepository parkingRepository) {
		this.userRepository=userRepository;
		this.modelMapper=modelMapper;
		this.parkingRepository=parkingRepository;
	}
	
	@Override
	@Transactional
	public ParkingDTO registrarParking(ParkingDTO parkingDTO, Long userId) throws ParkSoftException {
		
		Usuario userDB= userRepository.findById(userId)
				.orElseThrow(()-> new ParkSoftException(ERROR_USUARIO_PARKING_NO_EXISTE));
		
		boolean hasRolSocio = userDB.getRoles().stream()
			    .anyMatch(role -> role.getRol().equals(RolEnum.ROLE_SOCIO));
		
		if(!hasRolSocio) {
			throw new ParkSoftException(ERROR_USUARIO_PARKING_NO_ES_SOCIO); 
		}
		
		Parking parking= modelMapper.map(parkingDTO, Parking.class);
		parking.setEnabled(Boolean.TRUE);
		parking.getAdmins().add(userDB);
		
		return modelMapper.map(parkingRepository.save(parking), ParkingDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<VehiculoDTO> getVehiculosParking(Long parkingId) throws ParkSoftException {
		
		Parking parkingDB= parkingRepository.findById(parkingId)
					.orElseThrow(()->new ParkSoftException(ERROR_CONSULTAR_VEHICULOS_PARKING_NO_EXISTE));
		
		return parkingDB.getVehiculos().stream()
				.map(v-> modelMapper.map(v, VehiculoDTO.class)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ParkingDTO> getParkingUser(String usernameClaim) throws ParkSoftException {
		
		Usuario userDB= userRepository.findByCorreo(usernameClaim)
				.orElseThrow(()-> new ParkSoftException(ERROR_USUARIO_PARKING_NO_EXISTE));
		
		List<Parking> pakingsUser= parkingRepository.findByAdminsId(userDB.getId());
		
		return pakingsUser.stream()
				.map(p-> modelMapper.map(p, ParkingDTO.class)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<VehiculoDTO> getVehiculosParkingUser(String usernameClaim, Long parkingId) throws ParkSoftException {
		
		Usuario userDB= userRepository.findByCorreo(usernameClaim)
				.orElseThrow(()-> new ParkSoftException(ERROR_USUARIO_PARKING_NO_EXISTE));
		
		Parking parkingDB= parkingRepository.findById(parkingId)
				.orElseThrow(()->new ParkSoftException(ERROR_CONSULTAR_VEHICULOS_PARKING_NO_EXISTE));
		
		if(!parkingDB.getAdmins().contains(userDB)) {
			throw new ParkSoftException(ERROR_PARQUEADERO_CONSULTAR_VEHICULOS_NO_ES_ADMIN);
		}
		
		return parkingDB.getVehiculos().stream()
				.map(v-> modelMapper.map(v, VehiculoDTO.class)).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ParkingDTO> getParkings() throws ParkSoftException {
		
		return parkingRepository.findAll().stream()
				.map(p-> modelMapper.map(p, ParkingDTO.class)).toList();
	}

	@Override
	@Transactional
	public String deleteParking(Long parkingId) throws ParkSoftException {
		
		Parking parkingDB= parkingRepository.findById(parkingId)
				.orElseThrow(()->new ParkSoftException(ERROR_ELIMINAR_PARKING_NO_EXISTE));
		
		parkingDB.setEnabled(Boolean.FALSE);
		parkingRepository.save(parkingDB);
		
		return "parqueadero eliminado";
	}

	@Override
	@Transactional
	public String activarParking(Long parkingId) throws ParkSoftException {
		
		Parking parkingDB= parkingRepository.findById(parkingId)
				.orElseThrow(()->new ParkSoftException(ERROR_ELIMINAR_PARKING_NO_EXISTE));
		
		parkingDB.setEnabled(Boolean.TRUE);
		parkingRepository.save(parkingDB);
		
		return "parqueadero activo";
	}

	@Override
	@Transactional
	public ParkingDTO updateParking(ParkingDTO parkingDTO, Long parkingId) throws ParkSoftException {
		
		Parking parkingDB= parkingRepository.findById(parkingId)
				.orElseThrow(()->new ParkSoftException(ERROR_ELIMINAR_PARKING_NO_EXISTE));
		
		boolean existenCambios = false;
		
		if(StringUtils.hasText(parkingDTO.getName()) && !parkingDTO.getName().equals(parkingDB.getName())) {
			existenCambios=true;
			parkingDB.setName(parkingDTO.getName());
		}
		
		if(parkingDTO.getCapacidad()!=null 
				&& parkingDTO.getCapacidad()>0 
				&& !parkingDTO.getCapacidad().equals(parkingDB.getCapacidad())) {
			
			if(!(parkingDTO.getCapacidad()>=parkingRepository.countByParkingIdAndEstado(parkingId, EstadoVehiculoEnum.P))) {
				throw new ParkSoftException(ERROR_PARKING_ACTUALIZAR_CANTIDAD_MAYOR_A_ESTACIONADOS);
			}
				existenCambios=true;
				parkingDB.setCapacidad(parkingDTO.getCapacidad());
			
		}
		
		if(parkingDTO.getPrecio()!=null && parkingDTO.getPrecio().compareTo(BigDecimal.ZERO) > 0
				&& !parkingDTO.getPrecio().equals(parkingDB.getPrecio())) {
			existenCambios=true;
			parkingDB.setPrecio(parkingDTO.getPrecio());
		}
		
		if(existenCambios) {
			return modelMapper.map(parkingRepository.save(parkingDB), ParkingDTO.class);
		}
		
		return parkingDTO;
	}

}
