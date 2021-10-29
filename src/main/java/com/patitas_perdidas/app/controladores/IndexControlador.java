package com.patitas_perdidas.app.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexControlador {

	@GetMapping()
	public String index()
	{
		return "index.html";
	}
}
