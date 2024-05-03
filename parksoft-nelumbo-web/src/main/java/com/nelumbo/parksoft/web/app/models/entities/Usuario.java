package com.nelumbo.parksoft.web.app.models.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Titulo: Proyecto parksoftoft
 * </p>
 * <p>
 * Descripción: Entidad que realiza el mapeo para la tabla users
 * </p>
 * 
 * @author Cristian Misse
 *
 **/

@Entity
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
	    @UniqueConstraint(columnNames = "EMAIL", name = "USERS_EMAIL_UNIQUE"),
	    })
public class Usuario {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "ID_USERS")
	 private Long id;

	 @Column(name = "EMAIL", nullable = false, length = 254)
	 private String email;

	 @Column(name = "PASSWORD", nullable = false, length = 80)
	 private String password;

	 
	 @Column(name = "ENABLED")
	 private Boolean enabled;
	 
	 @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	 @JoinTable(
			 name = "users_roles",
			 joinColumns = @JoinColumn(name = "ID_USERS"),
			 inverseJoinColumns = @JoinColumn(name = "ID_ROLES"),
			 uniqueConstraints = {@UniqueConstraint(columnNames = {"ID_USERS","ID_ROLES"})})
	 private List<Role> roles;
	 
	 public Usuario() {
			this.roles=new ArrayList<>();
	 }
	 
}
