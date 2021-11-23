package com.patitas_perdidas.app.servicios;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import com.patitas_perdidas.app.entidades.Mascota;
import com.patitas_perdidas.app.entidades.ConfirmationToken;
import com.patitas_perdidas.app.entidades.Persona;
import com.patitas_perdidas.app.enums.Rol;
import com.patitas_perdidas.app.excepciones.MascotaExcepcion;
import com.patitas_perdidas.app.excepciones.PersonaExcepcion;
import com.patitas_perdidas.app.repositorios.ConfirmationTokenRepository;
import com.patitas_perdidas.app.repositorios.MascotaRepositorio;
import com.patitas_perdidas.app.repositorios.PersonaRepositorio;

@Service
public class PersonaServicio implements UserDetailsService {

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Autowired
	private PersonaRepositorio personaRepositorio;

	@Autowired
	private MascotaServicio mascotaServicio;
	
	@Autowired
	private MascotaRepositorio mr;
	
	@Autowired
    private JavaMailSender javaMailSender;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void guardar(String nombre, Long telefono, String mail, String clave) throws PersonaExcepcion {

		validar(nombre, mail, clave);
		validarMail(mail);
		validarTelefono(telefono);
		Persona entidad = new Persona();
		entidad.setNombre(nombre);
		entidad.setTelefono(telefono);
		entidad.setMail(mail);
		entidad.setCreado(new Date());
		entidad.setClave(encriptarClave(clave));
		entidad.setAlta(false);
		entidad.setRol(Rol.USER);
		personaRepositorio.save(entidad);
	}


	public void sendMail(String to, String nombre) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom("patitasperdidas.egg@gmail.com");
        mail.setTo(to);
        mail.setSubject("Se ha completado su registro en Patitas Perdidas");
        mail.setText("Bienvenido "+nombre+" a la familia de Patitas Perdidas, gracias por registrarte y por formar parte de esta comunidad. Su aporte a la comunidad siempre es importante, si ve un perro perdido no dude en publicarlo, seguramente su dueño se lo va a agradecer infinitamente.");

        javaMailSender.send(mail);
    }
	
	public void sendMailContraseña(SimpleMailMessage message){
	    javaMailSender.send(message);	
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void modificar(String id, String nombre, Long telefono, String mail) throws PersonaExcepcion {
		validarSinClave(nombre, telefono, mail);
		Persona usuario = buscaPorId(id);
		if (!usuario.getMail().equals(mail)) {
			validarMail(mail);
		}
		usuario.setNombre(nombre);
		usuario.setTelefono(telefono);
		usuario.setMail(mail);
		personaRepositorio.save(usuario);
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void modificarAdmin(String id, String nombre, Long telefono, String mail, Rol rol) throws PersonaExcepcion {
		validarSinClave(nombre, telefono, mail);
		Persona usuario = buscaPorId(id);
		if (!usuario.getMail().equals(mail)) {
			validarMail(mail);
		}
		usuario.setNombre(nombre);
		usuario.setTelefono(telefono);
		usuario.setMail(mail);
		usuario.setRol(rol);
		personaRepositorio.save(usuario);
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void modificarClave(String id, String clave1, String clave2) throws PersonaExcepcion {
		validarClave(clave1, clave2);
		Persona usuario = buscaPorId(id);
		usuario.setClave(encriptarClave(clave2));
		personaRepositorio.save(usuario);
	}

	// Metodo para crear una mascota y añadirla al usuario que inicio la sesion
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public void aniadirMascota(String id_persona, String nombre, String descripcion, String color, String raza,
			String tamanio, Boolean encontrado, Date fecha, String especie, String zona, MultipartFile archivo)
			throws PersonaExcepcion, MascotaExcepcion, IOException {
		Persona persona = buscaPorId(id_persona);
		// Genero un UUID desde persona asi puedo tomar la id de la mascota para añadir
		// a la lista de la persona.
		UUID uuid = UUID.randomUUID();
		String id_mascota = uuid.toString();
		mascotaServicio.crearMascota(id_mascota, id_persona, nombre, descripcion, color, raza, tamanio, encontrado, fecha, especie,
				zona, archivo);
		persona.getMascotas().add(mascotaServicio.buscaPorId(id_mascota));
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Persona alta(String id) throws Exception {

		Persona entidad = personaRepositorio.findById(id).get();
		entidad.setAlta(true);

		return personaRepositorio.save(entidad);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Persona baja(String id) throws PersonaExcepcion {
	
		Persona entidad = buscaPorId(id);
		mr.deleteMasc(id);
		entidad.setAlta(false);
		return personaRepositorio.save(entidad);
	}

	@Transactional(readOnly = true)
	public List<Persona> listarTodos() {
		return personaRepositorio.findAll();
	}

	@Transactional(readOnly = true)
	public List<Persona> listarActivos() throws PersonaExcepcion {
		return personaRepositorio.buscarListaPersonas();
	}

	@Transactional(readOnly = true)
	public Persona buscaPorId(String id) throws PersonaExcepcion {
		Optional<Persona> oPersona = personaRepositorio.findById(id);
		if (oPersona.isPresent()) {
			Persona person = oPersona.get();
			return person;
		} else {
			throw new PersonaExcepcion("No se encuentra la Persona");
		}
	}

	public String encriptarClave(String clave) {
		String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
		return claveEncriptada;
	}

	public void validar(String nombre, String mail, String clave) throws PersonaExcepcion {
		if (nombre == null || nombre.strip().isEmpty()) {
			throw new PersonaExcepcion("No ingreso correctamente el nombre.");
		}

		if (mail == null || mail.strip().isEmpty()) {
			throw new PersonaExcepcion("No ingreso correctamente el mail.");
		}

		if (clave == null || clave.strip().isEmpty()) {
			throw new PersonaExcepcion("No ingreso correctamente la clave.");
		}
		if (clave.length() < 6) {
			throw new PersonaExcepcion("La clave tiene que tener mas de 6 digitos");
		}
	}
	public void validarSinClave(String nombre, Long telefono, String mail) throws PersonaExcepcion {
		if (nombre == null || nombre.strip().isEmpty()) {
			throw new PersonaExcepcion("No ingreso correctamente el nombre.");
		}

		if (telefono == null) {
			throw new PersonaExcepcion("No ingreso correctamente el telefono.");
		}

		if (String.valueOf(telefono).length() < 6) {
			throw new PersonaExcepcion("El formato del telefono es incorrecto");
		}

		if (mail == null || mail.strip().isEmpty()) {
			throw new PersonaExcepcion("No ingreso correctamente el mail.");
		}
	}
	public void validarClave(String clave1, String clave2) throws PersonaExcepcion {
		if(!clave1.equals(clave2))
		{
			throw new PersonaExcepcion("Las claves son diferentes");
		}
		
		if (clave1 == null || clave1.strip().isEmpty() || clave2 == null || clave2.strip().isEmpty()) {
			throw new PersonaExcepcion("No ingreso correctamente la clave.");
		}
		if (clave1.length() < 6 || clave2.length() < 6) {
			throw new PersonaExcepcion("La clave tiene que tener mas de 6 digitos");
		}
		
	}

	public void validarMail(String mail) throws PersonaExcepcion {
		// Si el mail ya esta en la base de datos retorna un PersonaExcepcion
		Optional<Persona> rsp_mail = personaRepositorio.buscarPorMail(mail);
		if (rsp_mail.isPresent()) {
			throw new PersonaExcepcion("Ya existe el usuario con el mail: " + mail);
		}

	}
	
	public void validarTelefono(Long telefono) throws PersonaExcepcion {
		if (telefono == null) {
			throw new PersonaExcepcion("No ingreso correctamente el telefono.");
		}

		if (String.valueOf(telefono).length() < 6) {
			throw new PersonaExcepcion("El formato del telefono es incorrecto");
		}
	}
	@Transactional(readOnly = true)
	public Persona buscarPorEmail(String mail) {
		Optional<Persona> oPersona = personaRepositorio.buscarPorMail(mail);
		if (oPersona.isPresent()) {
			Persona persona = oPersona.get();
			return persona;
		} else {
			return null;
		}
	}
	@Transactional(readOnly = true)
	public Persona buscarPorEmail2(String mail) {
		Persona persona = personaRepositorio.buscarPersonaPorMail(mail);
		return persona;
	}
	

	// METODO QUE CARGA EL USUARIO PARA LOGUEARSE

	// Override porque se implementa userdetailservice y hay que sobreescribir el
	// metodo
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {

		Persona persona = buscarPorEmail(mail);

		// si no existe se retorna null
		if (persona == null || persona.getAlta() == false) {
			return null;
		}

		// Se crea el listado de permisos
		List<GrantedAuthority> permisos = new ArrayList<>();

		// Se crea una autorizacion basada en el rol del cliente
		// Despues vemos en los proximos dias el tema del concatenado, asi se respeta
		// para los otros metodos
		GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + persona.getRol());
		permisos.add(p1);

		// Se extraen atributos de contexto del navegador -> INVESTIGAR
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

		// Se crea la sesion y se agrega el cliente a la misma
		HttpSession session = attr.getRequest().getSession(true);
		session.setAttribute("clientesession", persona);
		// Tanto este metodo con el anterior, en conjunto
		// sirve para que los datos de la sesion queden guardados en la cache del
		// navegador
		// con uno se guardan los datos de entorno (tiempos de session, inactividad,
		// etc)
		// y se guarda la sesion correspondiente, el navegador despues se organiza (el
		// nav sapbee)

		// Se retorna el usuario con sesion "iniciada" y con permisos
		User user = new User(persona.getMail(), persona.getClave(), permisos);
		return user;

	}
	@Transactional(readOnly = true)
	public int contarMascotasActivas(String id) throws PersonaExcepcion
	{
		Persona persona = buscaPorId(id);
		int k = 0;
		for (Mascota mascota : persona.getMascotas()) 
		{
			if(mascota.getAlta())
			{
				k++;
			}
		}
		return k;
	}

	
	public void guardarToken(ConfirmationToken token){
		 confirmationTokenRepository.save(token);
	}
	
	public ConfirmationToken confirmarToken(String token){
		return confirmationTokenRepository.findByConfirmationToken(token);
	}

	public void guardarPersona(Persona persona) {
		personaRepositorio.save(persona);

	}
	@Transactional(readOnly = true)
	public long countPersonasActivas()
	{
		return personaRepositorio.counAlta();
	}
	@Transactional(readOnly = true)
	public long countPersonasBaja()
	{
		return personaRepositorio.countBaja();
	}
	@Transactional(readOnly = true)
	public List<Persona> listarPersonasBusqueda(String atributo) throws PersonaExcepcion {
		if(atributo == null || atributo.strip().isEmpty())
			return listarActivos();
		else
		{
			return personaRepositorio.buscarListaPersonasActivas(atributo);
		}	
		
	}
	@Transactional(readOnly = true)
	public long countAdmin()
	{
		System.out.println(personaRepositorio.countAdmin());
		return personaRepositorio.countAdmin();
	}
	public long userlastWeek(Integer dias)
	{
		return personaRepositorio.getLastWeek(dias);
	}
	@Transactional(readOnly = true)
	public Map<String, Integer> grafDataPers(int dias)
	{
		List<Date> fechas = personaRepositorio.getDias(dias);		
		Map<String, Integer> graphData = new TreeMap<>();
		for (Date fecha : fechas) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd");
			String dateString = format.format(fecha);
			if(graphData.containsKey(dateString))
			{
				int n = graphData.get(dateString) + 1;
				graphData.put(dateString, n);
			}
			else
				graphData.put(dateString, 1);			
		}
		return graphData;
	}	

}
