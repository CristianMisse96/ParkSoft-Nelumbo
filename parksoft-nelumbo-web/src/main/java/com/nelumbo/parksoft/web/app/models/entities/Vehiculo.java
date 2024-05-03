package com.nelumbo.parksoft.web.app.models.entities;

import java.sql.Timestamp;

import com.nelumbo.parksoft.web.app.models.enums.EstadoVehiculoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vehiculos")
public class Vehiculo {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "ID_VEHICULO")
	 private Long id;
	 
	 @Column(name = "PLACA", nullable = false, length = 6)
	 private String placa;
	 
	 @Column(name = "HORA_INGRESO")
	 private Timestamp horaIngreso;
	 
	 @Column(name = "HORA_SALIDA")
	 private Timestamp horaSalida;

	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "ID_PARKING")
	 private Parking parking;
	 
	 @Enumerated(EnumType.STRING)
	 @Column(name = "ESTADO", nullable = false, length = 2)
	 private EstadoVehiculoEnum estado;
	 
	 @PrePersist
	 public void prePersist() {
		 horaIngreso = new Timestamp(System.currentTimeMillis());
	 }
}
