package com.nelumbo.parksoft.web.app.exception;

import java.util.List;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ParkSoftException extends Exception {

	private final String mensaje;

	private final List<String> parametros;

	private static final long serialVersionUID = 1L;

	public ParkSoftException(String mensaje) {
		super(mensaje);
		this.mensaje = mensaje;
		this.parametros = null;
	}

	public ParkSoftException(String mensaje, List<String> parametros) {
		super(mensaje);
		this.parametros = parametros;
		this.mensaje = mensaje;
	}

	public ParkSoftException(String mensaje, Throwable cause) {
		super(cause);
		this.mensaje = mensaje;
		this.parametros = null;
	}

	public ParkSoftException(Exception e) {
		super(e);
		this.mensaje = e.getLocalizedMessage();
		this.parametros = null;
	}

	public String getMensaje() {
		if (mensaje == null || mensaje.isEmpty()) {
			log.warn("Se intenta obtener un mensaje de excepcion vacio. Imprimiendo traza de excepcion",
					this.getMessage());
			return this.getMessage();
		}
		return mensaje;
	}

	public List<String> getParametros() {
		return parametros;
	}

}
