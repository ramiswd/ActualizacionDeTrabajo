package tptp;

public class MiniEstadio extends Teatro {
	private double costoAdicional;
	
	public MiniEstadio(String nombre, String direccion, int capacidad,int asientoPorFila,double costoAdicional) {
		super(nombre, direccion, capacidad,asientoPorFila);
		this.costoAdicional=costoAdicional;
		
	}
	
	public double calcularPrecio(String sector, double precioBase) {
        return super.calcularPrecio(sector, precioBase) + costoAdicional;
    }
}
