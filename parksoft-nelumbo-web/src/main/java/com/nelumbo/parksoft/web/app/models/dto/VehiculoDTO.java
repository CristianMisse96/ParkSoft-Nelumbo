package com.nelumbo.parksoft.web.app.models.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nelumbo.parksoft.web.app.models.enums.EstadoVehiculoEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class VehiculoDTO implements Serializable{
	
	private Long id;
	
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	@NotBlank
	@Size(min = 6, max = 6)
	private String placa;
	
	private Timestamp horaIngreso;
	 
	private Timestamp horaSalida;

	private EstadoVehiculoEnum estado;
	
	private static final long serialVersionUID = 1L;

}
