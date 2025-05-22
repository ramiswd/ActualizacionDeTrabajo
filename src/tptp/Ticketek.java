package tptp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticketek implements ITicketek {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, Sede> sedes = new HashMap<>();
    private Map<String, Espectaculo> espectaculos = new HashMap<>();
    private int contadorEntradas = 0;
    
    //aux para generar codigo.
    private String generarCodigoEntrada() {
        //cada vez que deba generar un codigo ya suma 1, contador empieza como 0.
    	contadorEntradas++;
    	// devuelve la String+contador
        return  "ABC0"+contadorEntradas;
    }

    @Override
    public void registrarSede(String nombre, String direccion, int capacidadMaxima) {
    	//verifica si los datos son validos
        if (nombre == null || direccion == null || capacidadMaxima <= 0) {
            //si no sucede devuelve excepción.
        	throw new RuntimeException("Datos inválidos para sede.");
        }
        // verifica si el map sedes tiene el nombre de la sede
        if (sedes.containsKey(nombre)) {
        	// si es registrada, lanza excepción.
            throw new RuntimeException("Sede ya registrada.");
        }
        //si no esta en el map, la agrega con la clave nombre(nombre de la sede).
        sedes.put(nombre, new Estadio(nombre, direccion, capacidadMaxima));
    }

    @Override
    public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
            String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
    	//verifica si los datos son validos
        if (nombre == null || direccion == null || capacidadMaxima <= 0) {
        	//si no sucede devuelve excepción.
        	throw new RuntimeException("Datos invalidos para Teatro.");
        }
     // verifica si el map sedes tiene el nombre de la sede
        if (sedes.containsKey(nombre)) {
        	// si es registrada, lanza excepción.
            throw new RuntimeException("Sede ya registrada.");
        }
        //crea objeto teatro
        Teatro teatro = new Teatro(nombre, direccion,asientosPorFila, capacidadMaxima);
        //Agrega los sectores al Teatro
        for (int i = 0; i < sectores.length; i++) {
            double incremento = porcentajeAdicional[i] / 100;
            teatro.agregarSector(sectores[i], incremento, capacidad[i]);
        }
     // Guarda el Teatro en el mapa de sedes
        sedes.put(nombre, teatro);
    }

    @Override
    public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
            int cantidadPuestos, double precioConsumicion, String[] sectores, int[] capacidad,
            int[] porcentajeAdicional) {
    	//crea el objeto miniEstadio
        MiniEstadio mini = new MiniEstadio(nombre, direccion, capacidadMaxima,asientosPorFila, precioConsumicion);
        //Agrega los sectores al MiniEstadio
        for (int i = 0; i < sectores.length; i++) {
            double incremento = porcentajeAdicional[i] / 100;
            mini.agregarSector(sectores[i], incremento, capacidad[i]);
        }
        // Guarda el MiniEstadio en el mapa de sedes
        sedes.put(nombre, mini);
    }

    @Override
    public void registrarUsuario(String email, String nombre, String apellido, String contrasenia) {
    	//verifica si el usuario ya esta registrado
        if (usuarios.containsKey(email)) {
        	//devuelve excepcion si lo esta
            throw new RuntimeException("Usuario ya registrado.");
        }
        //verifica si los datos son correctos
        if (email == null || nombre == null || apellido == null || contrasenia == null) {
            // si no lo son devuelve excepcion
        	throw new RuntimeException("Datos invalidos.");
            
        }
        //agrega usuario nuevo al map usuarios
        usuarios.put(email, new Usuario(email, nombre, apellido, contrasenia));
    }

    @Override
    public void registrarEspectaculo(String nombre) {
    	//pregunta si espectaculom esta registrado
        if (nombre == null || espectaculos.containsKey(nombre)) {
            //devuelve excepcion si ya existe o si es invalido
        	throw new RuntimeException("espectaculo invalido o ya existente.");
        }
        //sino lo agrega
        espectaculos.put(nombre, new Espectaculo(nombre));
    }

    @Override
    public void agregarFuncion(String nombreEspectaculo, String fecha, String sede, double precioBase) {
    	// verifica datos si existen o no
        if (!espectaculos.containsKey(nombreEspectaculo) || !sedes.containsKey(sede) || fecha == null || precioBase <= 0) {
        	//devuelve excepcion
        	throw new RuntimeException("Datos invalidos o no existentes.");
        }
        //Obtiene el espectaculo que se agregara a funcion
        Espectaculo esp = espectaculos.get(nombreEspectaculo);
        //verifica que la funcion no exista en la fecha dada
        if (esp.getFuncion(fecha) != null) {
            throw new RuntimeException("Ya existe una funcion en esta Fecha");
        }
        //si todo se cumple, se crea la funcion y la agrega al espectaculo
        Funcion fun = new Funcion(sedes.get(sede), fecha, precioBase);
        esp.agregarFuncion(fun);
    }

    @Override
    public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia,
            int cantidadEntradas) {
    	//verifica si el usuario esta registrado
        if (!usuarios.containsKey(email)) throw new RuntimeException("Usuario no registrado.");
        //verifica si el espectaculo esta registrado
        if (!espectaculos.containsKey(nombreEspectaculo)) throw new RuntimeException("Espectaculo no encontrado.");
        //obtiene el usuario
        Usuario usuario = usuarios.get(email);
        //verifica si la contrasenia es correcta
        if (!usuario.autenticar(contrasenia)) throw new RuntimeException("Contrasenia incorrecta.");
        //obtiene la funcion
        Funcion funcion = espectaculos.get(nombreEspectaculo).getFuncion(fecha);
        //verifica que la funcion sea valida
        if (funcion == null) throw new RuntimeException("Funcion invalida.");
        // obtiene la sede de la funcion
        Sede sede = funcion.getSede();
        //verifica si la sede es un Estadio
        if (!sede.esEstadio()) throw new RuntimeException("La sede no es un Estadio.");
        //creacion de una lista para entradas vendidas
        List<IEntrada> vendidas = new ArrayList<>();
        //recorre cada entrada vendida para generarla
        for (int i = 0; i < cantidadEntradas; i++) {
        	//genera el codigo de la entrada
            String codigo = generarCodigoEntrada();
            //calcula el precio de la entrada, como es campo, no se le suma nada
            double precio = sede.calcularPrecio("CAMPO", funcion.getPrecioBase());
            //crea la entrada
            Entrada e = new Entrada(codigo, nombreEspectaculo, funcion, "CAMPO",0, 00, precio);
            //agrega entrada a la funcion y al usuario
            funcion.getEntradas().add(e);
            usuario.agregarEntrada(e);
            vendidas.add(e);
        }
        return vendidas;
    }


   

    @Override
    public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia,
            String sector, int[] asientos) {
    	// Verifica si el usuario está registrado
        if (!usuarios.containsKey(email)) throw new RuntimeException("Usuario no registrado");
        // Verifica si el espectáculo está registrado
        if (!espectaculos.containsKey(nombreEspectaculo)) throw new RuntimeException("Espectáculo no registrado");
        // Obtiene el usuario
        Usuario usuario = usuarios.get(email);
        // Verifica si la contraseña es correcta
        if (!usuario.autenticar(contrasenia)) throw new RuntimeException("Contraseña incorrecta");
        // Obtiene la función
        Funcion funcion = espectaculos.get(nombreEspectaculo).getFuncion(fecha);
        // Verifica si la función existe
        if (funcion == null) throw new RuntimeException("Función inexistente");
        // Obtiene la sede
        Sede sede = funcion.getSede();
        // Verifica si la sede no es un estadio
        if (sede.esEstadio()) throw new RuntimeException("La sede no es numerada");
        // Obtiene el teatro
        Teatro teatro = (Teatro) sede;
        // Obtiene el sector indicado
        Sector s = teatro.getSectores().get(sector);
        // Verifica si el sector existe
        if (s == null) throw new RuntimeException("Sector no existe");
        // Creación de lista de entradas vendidas
        List<IEntrada> vendidas = new ArrayList<>();
        // Recorre cada asiento para crear las entradas vendidas
        for (int asiento : asientos) {
            // Verifica si el asiento es disponible
            if (!s.disponible()) throw new RuntimeException("Asiento no disponible." + asiento);
            // Vende/asigna el asiento
            s.venderEntrada();
            // Genera el código de la entrada
            String codigo = generarCodigoEntrada();
            // Calcula el precio de la entrada (sector+precioBase)
            double precio = sede.calcularPrecio(sector, funcion.getPrecioBase());

            // Genera la entrada
            int asientosPorFila = ((Teatro) sede).getAsientosPorFila();
            int fila = (asiento - 1) / asientosPorFila + 1;
            // Genera la entrada
            Entrada e = new Entrada(codigo, nombreEspectaculo, funcion, sector, fila, asiento, precio);
            // La agrega a la función y al usuario
            funcion.getEntradas().add(e);
            usuario.agregarEntrada(e);
            vendidas.add(e);
        }
        return vendidas;
    }

    @Override
    public String listarFunciones(String nombreEspectaculo) {
    	// Verifica si el espectáculo existe
        if (!espectaculos.containsKey(nombreEspectaculo)) {
            throw new RuntimeException("Espectáculo no encontrado.");
        }

        // Obtiene el espectáculo
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);

        // Crea un StringBuilder para construir el resultado
        StringBuilder resultado = new StringBuilder();

        // Itera sobre las funciones del espectáculo
        for (Funcion funcion : espectaculo.getFunciones()) {
            Sede sede = funcion.getSede();

            // Formatea la información según el tipo de sede
            if (sede instanceof Estadio) {
                // Formato para estadio
                resultado.append(" - (");
                resultado.append(funcion.getFecha());
                resultado.append(") ");
                resultado.append(sede.getNombre());
                resultado.append(" - ");
                resultado.append(funcion.getEntradas().size());
                resultado.append("/");
                resultado.append(sede.getCapacidad());
                resultado.append("\n");
            } else {
                // Formato para teatro o miniestadio
                resultado.append(" - (");
                resultado.append(funcion.getFecha());
                resultado.append(") ");
                resultado.append(sede.getNombre());
                resultado.append(" - ");

                // Obtiene los sectores de la sede
                Map<String, Sector> sectores = ((Teatro) sede).getSectores();

                // Itera sobre los sectores
                boolean primero = true;
                for (Map.Entry<String, Sector> entry : sectores.entrySet()) {
                    if (!primero) {
                        resultado.append(" | ");
                    }
                    resultado.append(entry.getKey());
                    resultado.append(": ");
                    resultado.append(entry.getValue().getEntradasVendidas());
                    resultado.append("/");
                    resultado.append(entry.getValue().getCapacidad());
                    primero = false;
                }
                resultado.append("\n");
            }
        }

        // Retorna el resultado después de procesar todas las funciones
        return resultado.toString();
    }
        
    

    @Override
    public List<IEntrada> listarEntradasEspectaculo(String nombreEspectaculo) {
        // Implementación pendiente
        return null;
    }

    @Override
    public List<IEntrada> listarEntradasFuturas(String email, String contrasenia) {
    	return null;
    }

    @Override
    public List<IEntrada> listarTodasLasEntradasDelUsuario(String email, String contrasenia) {
        // verifica si el usuario está registrado
        if (!usuarios.containsKey(email)) {
            throw new RuntimeException("Usuario no registrado.");
        }

        // obtiene el usuario
        Usuario usuario = usuarios.get(email);

        // Verifica si la contraseña es correcta
        if (!usuario.autenticar(contrasenia)) {
            throw new RuntimeException("Contraseña incorrecta.");
        }

        // devuelve entradas compradas por el usuario
        return usuario.getEntradas();
    }

    @Override
    public boolean anularEntrada(IEntrada entrada, String contrasenia) {
        // Implementación pendiente
        return false;
    }

    @Override
    public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha, String sector, int asiento) {
        // Implementación pendiente
        return null;
    }

    @Override
    public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha) {
        // Implementación pendiente
        return null;
    }

    @Override
    
    public double costoEntrada(String nombreEspectaculo, String fecha) {
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if(espectaculo == null) {
        	throw new RuntimeException("el espectaculo no esta registrado");
        }
        
        Funcion funcion = (espectaculo.getFuncion(fecha));
        if(funcion == null) {
        	throw new RuntimeException("no existe una funcion para esta fecha");
        }
        
        return funcion.getPrecioBase(); 
    }

    @Override
    public double costoEntrada(String nombreEspectaculo, String fecha, String sector) {
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if(espectaculo == null) {
        	throw new RuntimeException("el espectaculo no esta registrado");
        }
        
        Funcion funcion = espectaculo.getFuncion(fecha);
        if(funcion == null) {
        	throw new RuntimeException("No hay fecha para esta funcion");
        }
        
        return funcion.calcularPrecio(sector);
    }

    @Override
    public double totalRecaudado(String nombreEspectaculo) {
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) {
            throw new IllegalArgumentException("No existe el espectáculo: " + nombreEspectaculo);
        }

        double total = 0;
        for (Funcion funcion : espectaculo.getFunciones()) {
            for (IEntrada entrada : funcion.getEntradas()) {
                total += entrada.precio();
            }
        }

        return total;
    }

    @Override
    public double totalRecaudadoPorSede(String nombreEspectaculo, String nombreSede) {
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) {
            throw new IllegalArgumentException("No existe el espectáculo: " + nombreEspectaculo);
        }

        double total = 0;
        for (Funcion funcion : espectaculo.getFunciones()) {
            if (funcion.getSede().getNombre().equalsIgnoreCase(nombreSede)) {
                for (IEntrada entrada : funcion.getEntradas()) {
                    total += entrada.precio();
                }
            }
        }

        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticketek:\n");
        sb.append("Usuarios: ").append(usuarios.size()).append("\n");
        sb.append("Sedes: ").append(sedes.size()).append("\n");
        sb.append("Espectáculos: ").append(espectaculos.size()).append("\n");
        return sb.toString();
    }
}
