package com.patitas_perdidas.app.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.patitas_perdidas.app.entidades.Persona;
import com.patitas_perdidas.app.excepciones.PersonaExcepcion;
import com.patitas_perdidas.app.repositorios.PersonaRepositorio;

@Service
public class PersonaServicio {

	@Autowired
	private PersonaRepositorio personaRepositorio;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void guardar(String nombre, Long telefono, String mail, String clave) throws PersonaExcepcion {

		validar(nombre, telefono, mail, clave);

		Persona entidad = new Persona();
		entidad.setNombre(nombre);
		entidad.setTelefono(telefono);
		entidad.setMail(mail);
		entidad.setClave(clave);
		entidad.setAlta(true);
		personaRepositorio.save(entidad);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void modificar(String id, String nombre, Long telefono, String mail, String clave) throws PersonaExcepcion {

		validar(nombre, telefono, mail, clave);
		Persona usuario = personaRepositorio.findById(id).get();
		
		usuario.setNombre(nombre);
		usuario.setTelefono(telefono);
		usuario.setMail(mail);
		usuario.setClave(clave);
		personaRepositorio.save(usuario);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Persona alta(String id) throws Exception {

		Persona entidad = personaRepositorio.findById(id).get();
		entidad.setAlta(true);

		return personaRepositorio.save(entidad);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Persona baja(String id) throws PersonaExcepcion {

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
	
	@Transactional(readOnly = true)
	public Persona buscaPorId(String id) throws PersonaExcepcion{
		Optional<Persona> oPersona = personaRepositorio.findById(id);
		if(oPersona.isPresent()) {
			Persona person = oPersona.get();
			return person;
		}else {
			throw new PersonaExcepcion("No se encuentra la Persona");
		}
	} 

	public void validar(String nombre, Long telefono, String mail, String clave) throws PersonaExcepcion {
		if (nombre == null || nombre.strip().isEmpty()) {
			throw new PersonaExcepcion("No ingreso correctamente el nombre.");
		}

		if (telefono == null) {
			throw new PersonaExcepcion("No ingreso correctamente el telefono.");
		}
		
		if (String.valueOf(telefono).length() < 6)
		{
			throw new PersonaExcepcion("El formato del telefono es incorrecto");
		}

		if (mail == null || mail.strip().isEmpty()) {
			throw new PersonaExcepcion("No ingreso correctamente el mail.");
		}

		if (clave == null || clave.strip().isEmpty()) {
			throw new PersonaExcepcion("No ingreso correctamente la clave.");
		}
		if (clave.length() < 6)
		{
			throw new PersonaExcepcion("La clave tiene que tener mas de 6 digitos");
		}
		// Si el mail ya esta en la base de datos retorna un PersonaExcepcion
		Optional<Persona> rsp_mail = personaRepositorio.buscarPorMail(mail);
		if (rsp_mail.isPresent()) {
			throw new PersonaExcepcion("Ya existe el usuario con el mail: " + mail);
		}

	}

}
