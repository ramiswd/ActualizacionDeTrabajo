package tptp;

public class Entrada implements IEntrada {
	private String codigo;
    private String espectaculo;
    private Funcion funcion;
    private String sector;
    private int fila;
    private int asiento;
    private double precio;

	public Entrada(String codigo, String espectaculo, Funcion funcion, String sector,int fila, int asiento, double precio) {
		
		this.codigo = codigo;
		this.espectaculo = espectaculo;
		this.sector = sector;
		this.fila=fila;
		this.funcion = funcion;
		this.asiento = asiento;
		this.precio = precio;
	}


	@Override
	public double precio() {
		return this.precio; 
	}
	
	@Override
	public String ubicacion() {
	    if ("CAMPO".equals(sector)) {
	        return sector;
	    }
	    return sector + " - Fila: " + fila + ", Asiento: " + asiento;
	}
	@Override
	public String toString() {
		if ("CAMPO".equals(sector)) {
			return   codigo + " - " + espectaculo + " - " + funcion.getFecha() + " - " + sector;
		}
        return  codigo + " - " + espectaculo + " - " + funcion.getFecha() + " - " + sector + " - " + "Fila: "+ fila +" Asiento: " + asiento;
    }


	@Override
	public Funcion getFuncion() {
		
		return funcion;
	}


	public String getNombreEspectaculo() {
		
		return espectaculo;
	}
	
	



	

}
