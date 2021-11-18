package com.patitas_perdidas.app.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.patitas_perdidas.app.entidades.Persona;
import com.patitas_perdidas.app.excepciones.PersonaExcepcion;
import com.patitas_perdidas.app.servicios.MascotaServicio;
import com.patitas_perdidas.app.servicios.PersonaServicio;

@Controller
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/dashboard")
public class AdminControlador {

	@Autowired
	private PersonaServicio personaServicio;
	
	@Autowired
	private MascotaServicio ms;

	@GetMapping("")
	public String lista(ModelMap modelo) {

		return "dashboard.html";
	}
	@GetMapping("/usuarios")
	public String getUsuarios(ModelMap model) throws PersonaExcepcion
	{
		try
		{
			model.addAttribute("usuarios", personaServicio.listarActivos());
		}
		catch(PersonaExcepcion p)
		{
			model.put("error", p.getMessage());
		}
		
		return "dashboard-usuarios.html";
	}
	@GetMapping("/usuarios/baja")
	public String baja(@RequestParam String id) {
		try {
			personaServicio.baja(id);
			return "redirect:/dashboard/usuarios";
		} catch (Exception e) {
			return "redirect:/dashboard/usuarios";
		}
	}
	@GetMapping("/usuarios/editar")
	public String editar(HttpSession session, ModelMap model, @RequestParam String id) throws PersonaExcepcion 
	{		
		Persona usuario = personaServicio.buscaPorId(id);
		model.addAttribute("usuario", usuario);
		return "dashboard-usuarios-editar";
	}

}