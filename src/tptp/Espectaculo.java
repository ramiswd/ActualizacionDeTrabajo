package tptp;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Espectaculo {
	
	private String nombre;
	private Map<String, Funcion> funciones;


    public Espectaculo(String nombre) {
        this.nombre = nombre;
        this.funciones = new HashMap<>();
    }

    public void agregarFuncion(Funcion f) {
    	//agrega una funcion al map, con la clave de fecha, si ya hay una la sobreescribe
        funciones.put(f.getFecha(), f);
    }

    public Funcion getFuncion(String fecha) {
    	//obtiene la funcion con la clave de fecha
        return funciones.get(fecha);
    }

    public List<Funcion> getFunciones() {
    	//devuelve una lista de las funciones, sin que modifique el map
        return new ArrayList<>(funciones.values());
    }

	public String getNombre() {
		return nombre;
	}

}
