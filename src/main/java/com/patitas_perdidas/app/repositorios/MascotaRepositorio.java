package com.patitas_perdidas.app.repositorios;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.patitas_perdidas.app.entidades.Mascota;

public interface MascotaRepositorio extends JpaRepository<Mascota, String> {

	// Busca una lista de todas las mascotas activas, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true")
	public List<Mascota> buscarListaActivos();

	// Busca una lista de mascota perdidas, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.encontrado = false")
	public List<Mascota> buscarListaPerdidos();

	// Busca una lista de mascota encontradas, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.encontrado = true")
	public List<Mascota> buscarListaEncontrados();

	// Busca una lista de mascota raza, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.raza = :raza")
	public List<Mascota> buscarListaRaza(@Param("raza") String raza);

	// Busca una lista de mascota tamaño, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.tamanio = :tamanio")
	public List<Mascota> buscarListaTamaño(@Param("tamanio") String tamao);

	// Busca una lista de mascota color, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.color = :color")
	public List<Mascota> buscarListaColor(@Param("color") String color);

	// Busca una lista de mascota especie, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.especie = :especie")
	public List<Mascota> buscarListaEspecie(@Param("especie") String especie);

	// Busca una lista de mascota zona, retorna una lista de mascotas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.zona = :zona")
	public List<Mascota> buscarListaZona(@Param("zona") String zona);
	
	@Query(value = "SELECT count(m) FROM Mascota m where m.alta = true")
	public long countAlta();
	
	@Query(value = "SELECT count(m) FROM Mascota m where m.alta = false")
	public long countBaja();
	
	@Query(value = "SELECT count(m) FROM Mascota m where m.alta = true AND m.especie = 'Perro'")
	public int countPerro();
	@Query(value = "SELECT count(m) FROM Mascota m where m.alta = true AND m.especie = 'Gato'")
	public int countGato();
	@Query(value = "SELECT count(m) FROM Mascota m where m.alta = true AND m.especie != 'Gato' AND m.especie != 'Perro'")
	public int countOtros();
	
	@Query(nativeQuery = true ,value = "SELECT creado FROM Mascota WHERE creado >= DATE(NOW()) - INTERVAL :n DAY")
	public List<Date> getDias(@Param("n") Integer dias);
	
	@Query(nativeQuery = true ,value = "SELECT COUNT(id) FROM mascota WHERE creado >= date_sub(now(),INTERVAL :n DAY)")
	public int masclastWeek(@Param("n") Integer numero);
	
	@Query(nativeQuery = true ,value = "SELECT * FROM Mascota WHERE alta = true AND encontrado = false ORDER BY creado DESC LIMIT 4")
	public List<Mascota> ultimasMascotas();
	
	@Query(nativeQuery = true ,value = "SELECT * FROM Mascota WHERE alta = true AND encontrado = true ORDER BY creado DESC LIMIT 4")
	public List<Mascota> ultimasMascotasEnc();
	
	@Query(nativeQuery = true ,value = "DELETE * FROM Mascota WHERE persona_id = :idPersona")
	public void deleteMasc(@Param("idPersona") String idPersona);
	
	

	// Buscar segun filtro todas las activas
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and (m.zona LIKE %:atributo%" + " OR m.tamanio LIKE %:atributo%"
			+ " OR m.raza LIKE %:atributo%" + " OR m.nombre LIKE %:atributo%" + " OR m.especie LIKE %:atributo%"
			+ " OR m.descripcion LIKE %:atributo%" + "  OR m.color LIKE %:atributo%)")
	public List<Mascota> buscarPorBusquedaActivos(@Param("atributo") String atributo);

	// Buscar segun filtro
	@Query("SELECT m FROM Mascota m WHERE m.alta = true and m.encontrado = :tipo AND (m.zona LIKE %:atributo%"
			+ " OR m.tamanio LIKE %:atributo%" + " OR m.raza LIKE %:atributo%" + " OR m.nombre LIKE %:atributo%"
			+ " OR m.especie LIKE %:atributo%" + " OR m.descripcion LIKE %:atributo%"
			+ "  OR m.color LIKE %:atributo%)")
	public List<Mascota> buscarPorBusqueda(@Param("atributo") String atributo, @Param("tipo") Boolean tipo);
};
