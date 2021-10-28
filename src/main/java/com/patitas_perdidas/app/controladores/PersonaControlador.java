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

import com.patitas_perdidas.app.entidades.Persona;
import com.patitas_perdidas.app.servicios.PersonaServicio;

@Controller
@RequestMapping("/persona")
public class PersonaControlador {

	@Autowired
	private PersonaServicio personaServicio;
	
	@GetMapping("/lista")
	public String lista(ModelMap modelo) {
		
		List<Persona>todos = personaServicio.listarTodos();
		modelo.addAttribute("personas",todos);
		
		return "list-persona";
	}
	
	@GetMapping("/registro")
	public String formulario(){	
		return "registro.html";
	}
	
	@PostMapping("/registro")
	public String guardar(ModelMap modelo, @RequestParam String id, @RequestParam String nombre, @RequestParam Long telefono, @RequestParam String mail, @RequestParam String clave, @RequestParam Boolean alta) {
		
		try {
			personaServicio.guardar(id, nombre, telefono, mail, clave, alta);
			modelo.put("exito", "Registro exitoso");
			return "registro.html";
		} catch (Exception e) {
			modelo.put("error", "Registro fallido");
			return "registro.html";
		}
		
	}
	
	@GetMapping("/baja/id")
	public String baja (@PathVariable String id) {
		try { 
			personaServicio.baja(id);
			return "redirect:/persona/lista";
		}catch (Exception e) {
			return "redirect:/";
		}
	}
	
	@GetMapping("/alta/id")
	public String alta (@PathVariable String id){
		try {
			personaServicio.alta(id);
			return "redirect:/persona/lista";
		}catch (Exception e) {
			return "redirect:/";
		}
			
	}
}
	
	
	
	

	

