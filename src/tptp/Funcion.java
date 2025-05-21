package tptp;

import java.util.ArrayList;
import java.util.List;

public class Funcion {
	private Sede sede;
	private String fecha;
	private double precioBase;
	private List<IEntrada> entradas;
	
	public Funcion(Sede sede, String fecha, double precioBase) {
		super();
		this.sede = sede;
		this.fecha = fecha;
		this.precioBase = precioBase;
		this.entradas = new ArrayList<>();
	}
	
	public Sede getSede() {
		return sede;
	}
	public String getFecha() {
		return fecha;
	}
	public double getPrecioBase() {
		return precioBase;
	}
	public List<IEntrada> getEntradas() {
		return entradas;
	}
	
	
	

}
