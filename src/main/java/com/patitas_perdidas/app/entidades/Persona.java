package com.patitas_perdidas.app.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.patitas_perdidas.app.enums.Rol;

@Entity
public class Persona {
	@Id
	@GeneratedValue (generator = "uuid")
	@GenericGenerator(name = "uuid",strategy = "uuid2")
	private String id;
	private String nombre;
	private Long telefono;
	private String mail;
	private String clave;
	private Boolean alta;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creado;
	@OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Mascota> mascotas;
	@Enumerated(EnumType.STRING)
	private Rol rol;
	
	public Boolean getAlta() {
		return alta;
	}
	public void setAlta(Boolean alta) {
		this.alta = alta;
	}
		
	public Date getCreado() {
		return creado;
	}
	public void setCreado(Date creado) {
		this.creado = creado;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Long getTelefono() {
		return telefono;
	}
	public void setTelefono(Long telefono) {
		this.telefono = telefono;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public List<Mascota> getMascotas() {
		return mascotas;
	}
	public void setMascotas(List<Mascota> mascotas) {
		this.mascotas = mascotas;
	}
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(alta, clave, id, mail, mascotas, nombre, telefono);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Persona other = (Persona) obj;
		return Objects.equals(alta, other.alta) && Objects.equals(clave, other.clave) && Objects.equals(id, other.id)
				&& Objects.equals(mail, other.mail) && Objects.equals(mascotas, other.mascotas)
				&& Objects.equals(nombre, other.nombre) && Objects.equals(telefono, other.telefono);
	}
	@Override
	public String toString() {
		return "Persona [id=" + id + ", nombre=" + nombre + ", telefono=" + telefono + ", mail=" + mail + ", clave="
				+ clave + ", alta=" + alta + ", mascotas=" + mascotas + "]";
	}
	public List<Mascota> getMascotasActivas() {
		List<Mascota> mascotasActivas = new ArrayList<>();
		for (Mascota mascota : mascotas) 
		{
			if(mascota.getAlta())
			{
				mascotasActivas.add(mascota);
			}
			
		}
		return mascotasActivas;
	}
	public int getNumeroMascotasActivas()
	{
		int k = 0;
		for (Mascota mascota : mascotas) 
		{
			if(mascota.getAlta())
			{
				k++;
			}
		}
		return k;
	}
	
	
}
