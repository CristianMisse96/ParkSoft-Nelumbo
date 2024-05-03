package com.nelumbo.parksoft.web.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nelumbo.parksoft.web.app.models.entities.Usuario;





/**
 * <p>
 * Titulo: Proyecto PruebaSoft
 * </p>
 * <p>
 * Descripción: Repositorio de la entidad Usuario
 * </p>
 *
 * @author Cristian Misse
 **/

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	boolean existsByEmail(String correo);

	@Query("SELECT u FROM Usuario u WHERE u.email = :username")
	Optional<Usuario> findByCorreo(String username);
	
}
