package com.patitas_perdidas.app.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Foto {

	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy="uuid2")
	private String nombre;
	private String mime;
	
	@Lob @Basic (fetch=FetchType.LAZY)
	private byte []contenido;
	
	

	public Foto() {
		super();
	}

	

	public Foto(String nombre, String mime, byte[] contenido) {
		super();
		this.nombre = nombre;
		this.mime = mime;
		this.contenido = contenido;
	}



	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public byte[] getContenido() {
		return contenido;
	}

	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
	
	
	
}
