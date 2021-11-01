package com.patitas_perdidas.app.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.patitas_perdidas.app.entidades.Persona;
import com.patitas_perdidas.app.excepciones.PersonaExcepcion;
import com.patitas_perdidas.app.servicios.PersonaServicio;

@Controller
@RequestMapping("/persona")
public class PersonaControlador {

	@Autowired
	private PersonaServicio personaServicio;

	@GetMapping("/lista")
	public String lista(ModelMap modelo) {

		List<Persona> todos = personaServicio.listarTodos();
		modelo.addAttribute("personas", todos);

		return "list-persona";
	}

	@GetMapping("/registro")
	public String formulario() {
		return "registro.html";
	}

	@PostMapping("/registro")
	public String guardar(RedirectAttributes redirAttrs, @RequestParam String nombre, @RequestParam Long telefono,
			@RequestParam String mail, @RequestParam String clave) {

		try {
			personaServicio.guardar(nombre, telefono, mail, clave);
		} catch (PersonaExcepcion e) {
			redirAttrs.addFlashAttribute("error", e.getMessage());
			return ("redirect:./registro");
		}
		redirAttrs.addFlashAttribute("exito", "Se ha registrado sastifactoriamente. Ahora puede ir a iniciar sesion.");
		return ("redirect:./registro");

	}

	@GetMapping("/baja/id")
	public String baja(@PathVariable String id) {
		try {
			personaServicio.baja(id);
			return "redirect:/persona/lista";
		} catch (Exception e) {
			return "redirect:/";
		}
	}

	@GetMapping("/alta/id")
	public String alta(@PathVariable String id) {
		try {
			personaServicio.alta(id);
			return "redirect:/persona/lista";
		} catch (Exception e) {
			return "redirect:/";
		}

	}
	
	@GetMapping("/perfil/{id}")
	public String perfilUsuario(ModelMap modelo, @PathVariable String id) throws PersonaExcepcion {
		Persona usuario = personaServicio.buscaPorId(id);
		modelo.addAttribute("usuario", usuario);
		return "perfil.html";
	}
	
	@GetMapping("/modificar/{id}")
	public String preModificar(ModelMap model, @PathVariable String id) throws PersonaExcepcion {
					
		    Persona usuario = personaServicio.buscaPorId(id);
		    model.addAttribute("usuario", usuario);
			return "modificar-usuario";
		
	}
	
	@PostMapping("/modificar")
	public String modificar(RedirectAttributes redirAttrs, ModelMap modelo, @PathVariable String id, @RequestParam String nombre, @RequestParam Long telefono, @RequestParam String mail, @RequestParam String clave) {
		System.out.println(id+ nombre+ telefono+ mail+ clave);
		try {	
			personaServicio.modificar(id, nombre, telefono, mail, clave);
			Persona usuario = personaServicio.buscaPorId(id);
			modelo.addAttribute("usuario", usuario);
			modelo.put("exito", "Perfirl modificado");
			redirAttrs.addAttribute("id", id);
			
			return "redirect:/persona/perfil/{id}";
		} catch (Exception e) {
			modelo.put("error", "Falto ingresar el nombre");
			System.out.println("Error desconocido");
			redirAttrs.addAttribute("id", id);
			
			return "redirect:/persona/perfil/{id}";
		}
	}
	
	
}
