package tptp;

public class Estadio extends Sede {

	public Estadio(String nombre, String direccion, int capacidad) {
		super(nombre, direccion, capacidad);
		
	
	}

	@Override
	public boolean esEstadio() {
		
		return true;
	}

	@Override
	public double calcularPrecio(String sector, double precioBase) {
		
		return precioBase;
	}
	
	@Override
	public Sector getSector(String nombre) {
	    return null;
	}

}
