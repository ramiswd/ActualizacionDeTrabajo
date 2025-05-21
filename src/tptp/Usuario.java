package tptp;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
	
	private String email,nombre,apellido,contrasenia;
	private List<IEntrada> entradas;
	public Usuario(String email, String nombre, String apellido, String contrasenia) {
		super();
		this.email = email;
		this.nombre = nombre;
		this.apellido = apellido;
		this.contrasenia = contrasenia;
		this.entradas = new ArrayList<>();
	}
	
	public boolean autenticar(String pass) {
        return contrasenia.equals(pass);
    }
	
	 public void agregarEntrada(IEntrada e) {
	        entradas.add(e);
	    }
	 
	 public boolean eliminarEntrada(IEntrada e) {
	        return entradas.remove(e);
	    }
	 
	 public List<IEntrada> getEntradas() {
	        return entradas;
	    }

	public String getEmail() {
		return email;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}
	
	

}
