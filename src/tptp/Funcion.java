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

	public double calcularPrecio(String sector) {
        if (sede.esEstadio()) {
            return precioBase;
        }

        Sector sectorObj = sede.getSector(sector);
        if (sectorObj == null) {
            throw new RuntimeException("Sector no encontrado: " + sector);
        }

        return sectorObj.calcularPrecio(precioBase);
    }
	
	public boolean esEstadio() {
	    return tipo.equalsIgnoreCase("ESTADIO");
	}
	
	
	
	
	

}
