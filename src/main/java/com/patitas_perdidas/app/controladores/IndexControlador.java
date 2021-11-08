package com.patitas_perdidas.app.controladores;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class IndexControlador {

	@GetMapping()
	public String index(@RequestParam(required = false) String error, ModelMap model)
	{
		if(error != null)
		{
			System.out.println("usuario incorrecto");
			model.put("error", "Usuario o clave incorrectos");
		}
		return "index.html";
	}
	//a esto no le den bola, es una prueba de diseño
	@GetMapping("/index2")
	public String index2()
	{
		return "index2.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/inicio")
	public String inicio(@RequestParam(required = false) String error, ModelMap model) {
		if(error != null)
		{
			model.put("error", "Usuario o clave incorrectos");
			return "index.html";
		}
		else
		{
			return "inicio.html";
		}
		
	}
}
