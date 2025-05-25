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
		//si es estadio, devuelve el precio base
        if (sede.esEstadio()) {
            return precioBase;
        }
        //busca el nombre del sector y lo obtiene como objeto SECTOR
        Sector sectorb = sede.getSector(sector);
        if (sectorb == null) {
            throw new RuntimeException("Sector no encontrado: " + sector);
        }
        //devuelve el preciobase+ su incremento
        return sectorb.calcularPrecio(precioBase);
    }
	
	public boolean esEstadio() {
	    return tipo.equalsIgnoreCase("ESTADIO");
	}
	
	
	
	
	

}
