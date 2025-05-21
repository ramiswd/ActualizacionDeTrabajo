package tptp;

import java.util.HashMap;
import java.util.Map;

public class Teatro extends Sede{
	private int asientosPorFila;
	Map<String,Sector> sectores;
	
	
	public Teatro(String nombre, String direccion, int capacidad,int asientoPorFila) {
		super(nombre, direccion, capacidad);
		this.sectores=new HashMap<>();
		this.asientosPorFila=asientoPorFila;
	}
	
	public void agregarSector(String nombre, double incremento, int capacidad) {
        sectores.put(nombre, new Sector(nombre, incremento, capacidad));
    }
	@Override
	public boolean esEstadio() {
		//como no es estadio devuelve false siempre
		return false;
	}

	@Override
	public double calcularPrecio(String sector, double precioBase) {
		Sector s= sectores.get(sector);
		return s.calcularPrecio(precioBase);
	}

	public int getAsientosPorFila() {
		return asientosPorFila;
	}
	

}
