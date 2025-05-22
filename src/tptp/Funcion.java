package tptp;

import java.util.ArrayList;
import java.util.List;

public class Funcion {
	private Sede sede;
	private String fecha;
	private double precioBase;
	private List<IEntrada> entradas;
	private String tipo;
	
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

	public double calcularPrecio(String nombreSector) {
		if(sede.esEstadio()) {
			return precioBase;
		}
	    Sector sector = sede.getSector(nombreSector);
	    if (sector == null) {
	        throw new RuntimeException("Sector no encontrado: " + nombreSector);
	    }

	    return sector.calcularPrecio(precioBase);
	}
	
	public boolean esEstadio() {
	    return tipo.equalsIgnoreCase("ESTADIO");
	}
	
	
	
	
	

}
