package tptp;

abstract class Sede {
	
	private String nombre, direccion;
	private int capacidad;
	
	public Sede(String nombre, String direccion, int capacidad) {
		this.nombre=nombre;
		this.direccion=direccion;
		this.capacidad=capacidad;
	}
	
	public abstract boolean esEstadio();
	
	public abstract double calcularPrecio(String sector, double precioBase);

	public String getNombre() {
		return nombre;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public String getDireccion() {
		return direccion;
	}

}
