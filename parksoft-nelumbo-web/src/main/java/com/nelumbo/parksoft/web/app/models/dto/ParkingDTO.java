package com.nelumbo.parksoft.web.app.models.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class ParkingDTO implements Serializable{

	 private Long id;
	 
	 @NotBlank
	 private String name;
	 
	 @NotNull
	 @Min(value = 1)
	 private Integer capacidad;
	 
	 @NotNull
	 @Digits(integer = 10, fraction = 2)
	 @Min(value = 1)
	 private BigDecimal precio;
	 
	private static final long serialVersionUID = 1L;

}
