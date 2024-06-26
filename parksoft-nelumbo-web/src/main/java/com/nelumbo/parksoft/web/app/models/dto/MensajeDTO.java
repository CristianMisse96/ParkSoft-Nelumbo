package com.nelumbo.parksoft.web.app.models.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nelumbo.parksoft.web.app.models.enums.SeverityEnum;

import lombok.Getter;
import lombok.Setter;


/**
 * <p>
 * Titulo: Proyecto ParkSoft
 * </p>
 * <p>
 * Descripción: Clase para manejo de la estructura de mensajes con la que se entregan estos como respuesta a una petición
 * </p>
 *
 * @author Cristian Misse
 *
 **/
@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class MensajeDTO {
	
	@JsonAlias
	private SeverityEnum severity;
	@JsonAlias
	private String summary;
	@JsonAlias
	private String detail;
	
	public MensajeDTO(SeverityEnum severity, String summary, String detail) {
		this.severity= severity;
		this.summary = summary;
		this.detail = detail;
	}
}
