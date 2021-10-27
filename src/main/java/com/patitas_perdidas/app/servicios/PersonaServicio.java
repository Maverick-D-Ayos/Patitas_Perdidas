package com.patitas_perdidas.app.servicios;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.patitas_perdidas.app.entidades.Persona;
import com.patitas_perdidas.app.repositorios.PersonaRepositorio;


@Service
public class PersonaServicio {
	
	@Autowired 
	private PersonaRepositorio personaRepositorio;
	

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void guardar(String id, String nombre, Long telefono, String mail, String clave, Boolean alta) throws Exception {
		
		validar(id,nombre,telefono,mail,clave,alta);
		
		Persona entidad = new Persona();

		entidad.setId(id);
		entidad.setNombre(nombre);
		entidad.setTelefono(telefono);
		entidad.setMail(mail);
		entidad.setClave(clave);
		entidad.setAlta(true);
			
	}


	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Persona alta(String id) throws Exception {
		
		Persona entidad = personaRepositorio.findById(id).get();
		entidad.setAlta(true);
		
		return personaRepositorio.save(entidad);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
	public Persona baja(String id) throws Exception {
		
		Persona entidad = personaRepositorio.findById(id).get();
		entidad.setAlta(false);
		
		return personaRepositorio.save(entidad);
	}

	@Transactional(readOnly = true)
	public List<Persona> listarTodos() { 
		return personaRepositorio.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<Persona> listarActivos() { 
		return personaRepositorio.buscarListaPersonas();
	}
	
	public void validar(String id, String nombre, Long telefono, String mail, String clave, Boolean alta) throws Exception {

		if (id == null || id.isEmpty() || id.contains("  ")) {
			throw new Exception();
		}
		
		if (nombre == null || nombre.isEmpty() || nombre.contains("  ")) {
			throw new Exception();
		}
		
		if (telefono == null) {
			throw new Exception();
		}
		
		if (mail == null || mail.isEmpty() || mail.contains("  ")) {
			throw new Exception();
		}
		
		if (clave == null || clave.isEmpty() || clave.contains("  ")) {
			throw new Exception();
	}
		
		if (alta == null) {
			throw new Exception();
	}
	}
	

	
}
	

