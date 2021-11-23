package com.patitas_perdidas.app.repositorios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.patitas_perdidas.app.entidades.Mascota;
import com.patitas_perdidas.app.entidades.Persona;

public interface PersonaRepositorio extends JpaRepository<Persona, String> {
	// Busca una lista de personas activas, retorna una lista de personas
	@Query("SELECT p FROM Persona p WHERE p.alta = true")
	public List<Persona> buscarListaPersonas();

	// Busca una lista de personas popr nombre, retorna una lista de personas
	@Query("SELECT p FROM Persona p WHERE p.alta = true and p.nombre LIKE %:nombre%")
	public List<Persona> buscarListaPersonasNombre(@Param("nombre") String nombre);
	
	@Query("SELECT p FROM Persona p WHERE p.alta = true and (p.nombre LIKE %:atributo%" 
			+ " OR p.mail LIKE %:atributo%"
			+ " OR p.telefono LIKE %:atributo%"
			+ " OR p.rol LIKE %:atributo%)")
	public List<Persona> buscarListaPersonasActivas(@Param("atributo") String nombre);

	// Devuelve una persona por su nombre.
	@Query("SELECT p FROM Persona p WHERE p.nombre = :nombre")
	public Optional<Persona> buscarPorNombre(@Param("nombre") String nombre);

	// Devuelve una persona por su email.
	@Query("SELECT p FROM Persona p WHERE p.mail = :mail")
	public Optional<Persona> buscarPorMail(@Param("mail") String mail);
	
	@Query("SELECT p FROM Persona p WHERE p.mail = :mail")
	public Persona buscarPersonaPorMail(@Param("mail") String mail);
	
	@Query(value = "SELECT count(p) FROM Persona p where p.alta = true")
	public long counAlta();
	
	@Query(value = "SELECT count(p) FROM Persona p where p.alta = false")
	public long countBaja();
	
	@Query(value = "SELECT count(p) FROM Persona p where p.alta = true AND p.rol = 'ADMIN'")
	public long countAdmin();
	
	@Query(nativeQuery = true ,value = "SELECT COUNT(id) FROM persona WHERE creado >= date_sub(now(),INTERVAL :n DAY);")
	public int getLastWeek(@Param("n") Integer numero);
	
	@Query(nativeQuery = true ,value = "SELECT creado FROM persona WHERE creado > DATE(NOW()) - INTERVAL :numero DAY;")
	public List<Date> getDias(@Param("numero") int numero);
		
}
