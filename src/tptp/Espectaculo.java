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
        funciones.put(f.getFecha(), f);
    }

    public Funcion getFuncion(String fecha) {
        return funciones.get(fecha);
    }

    public List<Funcion> getFunciones() {
        return new ArrayList<>(funciones.values());
    }

	public String getNombre() {
		return nombre;
	}

}
