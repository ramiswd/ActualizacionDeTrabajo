package tptp;

import java.util.HashSet;
import java.util.Set;

public class Sector {
	private String nombre;
    private double incremento;
    private int capacidad;
    private Set<Integer> vendidos;
    
    
    
	public Sector(String nombre, double incremento, int capacidad) {
		this.nombre=nombre;
		this.incremento=incremento;
		this.capacidad=capacidad;
		this.vendidos = new HashSet<>();
	
	}
	
	public boolean disponible(int asiento) {
        return !vendidos.contains(asiento);
         
    }
	public void vender(int asiento) {
        vendidos.add(asiento);
    }
	
	 public double calcularPrecio(double base) {
	        return base + base * incremento;
	    }

	public int getCapacidad() {
		return capacidad;
	}

	public String getNombre() {
		return nombre;
	}

}
