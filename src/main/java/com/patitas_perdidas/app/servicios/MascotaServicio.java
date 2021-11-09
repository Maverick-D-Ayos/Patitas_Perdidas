package com.patitas_perdidas.app.servicios;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.patitas_perdidas.app.entidades.Mascota;
import com.patitas_perdidas.app.excepciones.MascotaExcepcion;
import com.patitas_perdidas.app.excepciones.PersonaExcepcion;
import com.patitas_perdidas.app.repositorios.MascotaRepositorio;

@Service
public class MascotaServicio {

	@Autowired
	private MascotaRepositorio mr;
	
	@Autowired
	private PersonaServicio ps;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void crearMascota(String id, String person_id, String nombre, String descripcion, String color, String raza, String tamanio,
			Boolean encontrado, Date fecha, String especie, String zona, MultipartFile archivo)
			throws MascotaExcepcion, IOException, PersonaExcepcion {
		validar(nombre, descripcion, color, raza, tamanio, especie, zona, archivo);
		Mascota m = new Mascota();
		m.setId(id);
		m.setNombre(nombre);
		m.setDescripcion(descripcion);
		m.setColor(color);
		m.setRaza(raza);
		m.setTamanio(tamanio);
		m.setEncontrado(encontrado);
		m.setFecha(fecha);
		m.setEspecie(especie);
		m.setPersona(ps.buscaPorId(person_id));
		m.setAlta(true);

		try {
			m.setImage(Base64.getEncoder().encodeToString(archivo.getBytes()));
		} catch (IOException e) {
			throw new IOException("El archivo no es valido");
		}

		m.setZona(zona);

		mr.save(m);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void eliminarMascota(String id) throws MascotaExcepcion {
		Mascota m = buscaPorId(id);
		m.setAlta(false);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void modificarMascota(String id, String nombre, String descripcion, String color, String raza, String tamaño,
			Boolean encontrado, Date fecha, String especie, String zona, MultipartFile archivo)
			throws MascotaExcepcion, IOException {
		validar(nombre, descripcion, color, raza, tamaño, especie, zona, archivo);
		Mascota m = buscaPorId(id);
		if(nombre != null && !nombre.isEmpty())
		{
			m.setNombre(nombre);
		}
		m.setDescripcion(descripcion);
		m.setColor(color);
		m.setRaza(raza);
		m.setTamanio(tamaño);
		m.setEncontrado(encontrado);
		m.setFecha(fecha);
		m.setEspecie(especie);
		m.setAlta(true);
		if (archivo != null && !archivo.isEmpty()) {
			try {
				m.setImage(Base64.getEncoder().encodeToString(archivo.getBytes()));
			} catch (IOException e) {
				throw new IOException("El archivo no es valido");
			}
		}

		m.setZona(zona);

		mr.save(m);
	}

	@Transactional(readOnly = true)
	public List<Mascota> listarTodasMascotas() {
		List<Mascota> lm = mr.findAll();
		return lm;
	}

	@Transactional(readOnly = true)
	public List<Mascota> listarMascotasActivasPerdidas() {
		List<Mascota> lm = mr.buscarListaPerdidos();
		return lm;
	}
	@Transactional(readOnly = true)
	public List<Mascota> listarMascotasActivasPerdidas(String atributo) {
	if (atributo != null) {
	return mr.buscarPorBusqueda(atributo,false);
	}
	return mr.buscarListaPerdidos();
	}
	
	@Transactional(readOnly = true)
	public List<Mascota> listarMascotasActivasEncontradas() {
		List<Mascota> lm = mr.buscarListaEncontrados();
		return lm;
	}
	
	@Transactional(readOnly = true)
	public List<Mascota> listarMascotasActivasEncontradas(String atributo) {
	if (atributo != null) {
	return mr.buscarPorBusqueda(atributo,true);
	}
	return mr.buscarListaEncontrados();
	}

	@Transactional(readOnly = true)
	public List<Mascota> listarMascotasPorRaza(String raza) {
		List<Mascota> lm = mr.buscarListaRaza(raza);
		return lm;
	}

	@Transactional(readOnly = true)
	public List<Mascota> listarMascotasColor(String color) {
		List<Mascota> lm = mr.buscarListaColor(color);
		return lm;
	}

	@Transactional(readOnly = true)
	public Mascota buscaPorId(String id) throws MascotaExcepcion {
		Optional<Mascota> oMascota = mr.findById(id);
		if (oMascota.isPresent()) {
			Mascota mascota = oMascota.get();
			return mascota;
		} else {
			throw new MascotaExcepcion("No se encuentra la mascota");
		}
	}

	public void validar(String nombre, String descripcion, String color, String raza, String tamaño, String especie,
			String zona, MultipartFile archivo) throws MascotaExcepcion, IOException {
		if (nombre == null || nombre.isEmpty() || nombre.strip() == "") {
			throw new MascotaExcepcion("Es necesario introducir el nombre de su mascota");
		}

		if (descripcion == null || descripcion.isEmpty() || descripcion.strip() == "") {
			throw new MascotaExcepcion("Es necesario introducir la descripcion de la mascota");
		}

		if (color == null || color.isEmpty() || color.strip() == "") {
			throw new MascotaExcepcion("Es necesario introducir el color de la mascota");
		}

		if (raza == null || raza.isEmpty() || raza.strip() == "") {
			throw new MascotaExcepcion("Es necesario introducir la raza de la mascota");
		}

		if (tamaño == null || tamaño.isEmpty() || tamaño.strip() == "") {
			throw new MascotaExcepcion("Es necesario introducir el tamaño de la mascota");
		}

		if (especie == null || especie.isEmpty() || especie.strip() == "") {
			throw new MascotaExcepcion("Es necesario introducir de que especie es la mascota");
		}

		if (zona == null || zona.isEmpty() || zona.strip() == "") {
			throw new MascotaExcepcion("Seleccione un barrio");
		}

		String archivoNombre = StringUtils.cleanPath(archivo.getOriginalFilename());
		if (archivoNombre.contains("..")) {
			throw new IOException("El archivo no es valido");
		}
	}
	public List<Mascota> getMascotasPersona(String id) throws PersonaExcepcion
	{
		List<Mascota> lista = ps.buscaPorId(id).getMascotasActivas();
		if(lista.isEmpty())
		{
			throw new PersonaExcepcion("Usted no tiene mascotas activas");
		}
		else
		{
			return lista;
		}
	}
}
