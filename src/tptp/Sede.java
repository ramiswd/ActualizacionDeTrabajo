package tptp;

abstract class Sede {
	
	private String nombre, direccion;
	private int capacidad;
	
	public Sede(String nombre, String direccion, int capacidad) {
		this.nombre=nombre;
		this.direccion=direccion;
		this.capacidad=capacidad;
	}
	//saber si una sede es un estadio o no
	public abstract boolean esEstadio();
	//cada sede calcula el precio de forma diferente
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
	//accede al sector especifico por nombre, sirve para teatro y miniEstadio
	public abstract Sector getSector(String nombre);
}
