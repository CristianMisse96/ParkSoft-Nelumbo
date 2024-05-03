package com.nelumbo.parksoft.web.app.models.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.UniqueConstraint;

@Entity
@Getter
@Setter
@Table(name = "parkings")
public class Parking {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "ID_PARKING")
	 private Long id;
	 
	 @Column(name = "NOMBRE", nullable = false, length = 100)
	 private String name;
	 
	 @Column(name= "CANTIDAD", nullable = false)
	 private Integer capacidad;
	 
	 @Column(name = "COSTO",scale = 2, precision = 10, nullable = false)
	 private BigDecimal precio;
	 
	 @Column(name = "ENABLED", nullable = false)
	 private Boolean enabled;
	 
	 @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	    @JoinTable(
	            name = "parking_admins",
	            joinColumns = @JoinColumn(name = "ID_PARKING"),
	            inverseJoinColumns = @JoinColumn(name = "ID_USERS"),
	            uniqueConstraints = {@UniqueConstraint(columnNames = {"ID_PARKING", "ID_USERS"})})
	 private List<Usuario> admins;
	 
	 @OneToMany(mappedBy = "parking")
	 private List<Vehiculo> vehiculos;
	 
	 public Parking() {
			this.admins=new ArrayList<>();
	 }
}
