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
		//crea un nuevo sector y lo agrega al Map sectores
        sectores.put(nombre, new Sector(nombre, incremento, capacidad));
    }
	@Override
	public boolean esEstadio() {
		//como no es estadio devuelve false siempre
		return false;
	}

	@Override
	public double calcularPrecio(String sector, double precioBase) {
		//busca el sector en el map
		Sector s= sectores.get(sector);
	    //si no lo encuentra lanza excepcion
		if (s == null) {
	        throw new IllegalArgumentException("Sector no encontrado: " + sector);
	    }
		//si lo encuentra, calcula el precio dependiendo del sector que sea
		return s.calcularPrecio(precioBase);
	}

	public int getAsientosPorFila() {
		//devuelve los asientos por fila
		return asientosPorFila;
	}
	
	@Override
	public Sector getSector(String nombre) {
		//devuelve el nombre del sector
	    return sectores.get(nombre);
	}

	public Map<String, Sector> getSectores() {
		//devuelve el map completo de todos los sectores que hay
		return sectores;
	}
	
	public void venderEntrada(String sector, int cantidad) {
        if (!sectores.containsKey(sector)) {
            throw new RuntimeException("Sector no encontrado.");
        }
        //vende varias entradas para un sector en especifico
        Sector s = sectores.get(sector);
        //bucle para vender tanta cantidad de entradas pedidas
        for (int i = 0; i < cantidad; i++) {
        	//llama a venderEntrada, para ver si quedan disponibles
            s.venderEntrada();
        }
	
	
	}	

}
