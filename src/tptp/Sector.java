package tptp;

import java.util.HashSet;
import java.util.Set;

public class Sector {
	private String nombre;
    private double incremento;
    private int capacidad;
    private Set<Integer> vendidos;
    private int entradasVendidas;
   
    
    
    
	public Sector(String nombre, double incremento, int capacidad) {
		this.nombre=nombre;
		this.incremento=incremento;
		this.capacidad=capacidad;
		this.vendidos = new HashSet<>(); //lleva los registros de asientos ocupados
		this.entradasVendidas=0;
	}
	public void venderEntrada() {
		//aumenta el contador de entradas vendidas, resuelve el conteo de entradas de CAMPO
        if (entradasVendidas < capacidad) {
            entradasVendidas++;
        } else {
        	//si lleva sobrepasa la capacidad, devuelve excepcion
            throw new RuntimeException("No hay más entradas disponibles en este sector.");
        }
    }

    public boolean disponible() {
    	//devuelve true si todavia hay capacidad
        return entradasVendidas < capacidad;
    }
	
	public boolean disponible(int asiento) {
		//devuelve true si el asiento no fue vendido
        return !vendidos.contains(asiento);
         
    }
	public void vender(int asiento) {
		//si el asiento esta vendido, lanza excepcion
		if (!disponible(asiento)) {
		    throw new RuntimeException("El asiento ya está vendido.");
		}
		//marca el asiento vendido y lo agrega al conjunto
        vendidos.add(asiento);
    }
	
	public double calcularPrecio(double precioBase) {
        return precioBase * (1 + incremento);
    }
	
	public double getIncremento() {
		return incremento;
	}
	public int getCapacidad() {
		return capacidad;
	}

	public String getNombre() {
		return nombre;
	}
	public int getEntradasVendidas() {
	        return entradasVendidas;
	}
	
	


}
