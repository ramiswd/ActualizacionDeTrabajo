package tptp;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticketek implements ITicketek {
    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, Sede> sedes = new HashMap<>();
    private Map<String, Espectaculo> espectaculos = new HashMap<>();
    private int contadorEntradas = 0;
    private Map<IEntrada, Usuario> entradasPorUsuario = new HashMap<>();
    
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
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        //verifica que la funcion no exista en la fecha dada
        if (espectaculo.getFuncion(fecha) != null) {
            throw new RuntimeException("Ya existe una funcion en esta Fecha");
        }
        //si todo se cumple, se crea la funcion y la agrega al espectaculo
        Funcion fun = new Funcion(sedes.get(sede), fecha, precioBase);
        espectaculo.agregarFuncion(fun);
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
            entradasPorUsuario.put(e, usuario);
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
            entradasPorUsuario.put(e, usuario);
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
    	// Verifica si el espectáculo existe
        if (!espectaculos.containsKey(nombreEspectaculo)) {
            throw new RuntimeException("Espectáculo no encontrado.");
        }

        // Obtiene el espectáculo
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);

        // Crea una lista para almacenar las entradas del espectáculo
        List<IEntrada> entradasEspectaculo = new ArrayList<>();

        // Itera sobre las funciones del espectáculo
        for (Funcion funcion : espectaculo.getFunciones()) {
            // Agrega todas las entradas de la función a la lista
            entradasEspectaculo.addAll(funcion.getEntradas());
        }

        return entradasEspectaculo;
    }

    @Override
    public List<IEntrada> listarEntradasFuturas(String email, String contrasenia) {
    	// Verifica si el usuario está registrado
        if (!usuarios.containsKey(email)) {
            throw new RuntimeException("Usuario no registrado.");
        }

        // Obtiene el usuario
        Usuario usuario = usuarios.get(email);

        // Verifica si la contraseña es correcta
        if (!usuario.autenticar(contrasenia)) {
            throw new RuntimeException("Contraseña incorrecta.");
        }

        // Obtiene la fecha actual como String en formato "dd/MM/yy"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String fechaActual = sdf.format(new Date());

        // Crea una lista para almacenar las entradas futuras
        List<IEntrada> entradasFuturas = new ArrayList<>();

        // Itera sobre las entradas del usuario
        for (IEntrada entrada : usuario.getEntradas()) {
            // Obtiene la fecha de la función de la entrada
            String fechaEntrada = entrada.getFuncion().getFecha();

            // Compara las fechas como String
            if (fechaEntrada.compareTo(fechaActual) > 0) {
                entradasFuturas.add(entrada);
            }
        }

        return entradasFuturas;
    }

    @Override
    public List<IEntrada> listarTodasLasEntradasDelUsuario(String email, String contrasenia) {
    	 // Verifica si el usuario está registrado
        if (!usuarios.containsKey(email)) {
            throw new RuntimeException("Usuario no registrado.");
        }

        // Obtiene el usuario
        Usuario usuario = usuarios.get(email);

        // Verifica si la contraseña es correcta
        if (!usuario.autenticar(contrasenia)) {
            throw new RuntimeException("Contraseña incorrecta.");
        }

        // Devuelve una copia de las entradas del usuario para evitar problemas de modificación externa
        return new ArrayList<>(usuario.getEntradas());
    }

    @Override
    public boolean anularEntrada(IEntrada entrada, String contrasenia) {
    	if (entrada == null) {
            throw new RuntimeException("La entrada no puede ser null.");
        }
    	//Obtiene el usuario
        Usuario usuario = entradasPorUsuario.get(entrada);

        if (usuario == null) {
            throw new RuntimeException("La entrada no pertenece a ningún usuario.");
        }

        if (!usuario.autenticar(contrasenia)) {
            throw new RuntimeException("Contraseña incorrecta.");
        }
        //elimina la entrada, de la lista del usuario
        usuario.getEntradas().remove(entrada);

        // Cast seguro porque todas las entradas concretas son del tipo Entrada
        Entrada entradaConcreta = (Entrada) entrada;
        entradaConcreta.getFuncion().getEntradas().remove(entrada);

        entradasPorUsuario.remove(entrada);

        return true;
    }

    @Override
    public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha, String sector, int asiento) {
    	if (entrada == null) throw new RuntimeException("Entrada inválida.");
    	//Verificacion de la entrada del usuario
        Usuario usuario = entradasPorUsuario.get(entrada);
        if (usuario == null) throw new RuntimeException("Entrada no asociada a un usuario.");
        if (!usuario.autenticar(contrasenia)) throw new RuntimeException("Contraseña incorrecta");
        //obtiene el nombre del espectaculo, desde la entrada
        String nombreEspectaculo = ((Entrada) entrada).getNombreEspectaculo();
        //Busca el espectaculo y lo obtiene
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        //busca la fecha del espectaculo
        Funcion nuevaFuncion = espectaculo.getFuncion(fecha);
        if (nuevaFuncion == null) throw new RuntimeException("Función no encontrada");
        //Obtiene la sede
        Sede sede = nuevaFuncion.getSede();
        if (!(sede instanceof Teatro)) throw new RuntimeException("La sede no es numerada");
        //Obtiene el teatro
        Teatro teatro = (Teatro) sede;
        //verifica si el sector [s] esta disponible o si existe
        Sector s = teatro.getSectores().get(sector);
        if (s == null || !s.disponible()) throw new RuntimeException("Sector no disponible");

        // Reservar asiento
        s.venderEntrada();

        // Calcular fila
        int fila = (asiento - 1) / teatro.getAsientosPorFila() + 1;
        String codigo = generarCodigoEntrada();
        double precio = sede.calcularPrecio(sector, nuevaFuncion.getPrecioBase());

        Entrada nuevaEntrada = new Entrada(codigo, nombreEspectaculo, nuevaFuncion, sector, fila, asiento, precio);

        // Agregar la nueva entrada
        nuevaFuncion.getEntradas().add(nuevaEntrada);
        usuario.agregarEntrada(nuevaEntrada);
        entradasPorUsuario.put(nuevaEntrada, usuario);

        // Eliminar la entrada anterior
        usuario.removerEntrada(entrada);
        entradasPorUsuario.remove(entrada);

        return nuevaEntrada;
    }

    @Override
    public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha) {
    	if (entrada == null) throw new RuntimeException("La entrada no puede ser null");
    	//obtiene el usuario
        Usuario usuario = entradasPorUsuario.get(entrada);
        if (usuario == null || !usuario.autenticar(contrasenia))
            throw new RuntimeException("Autenticación fallida");

        Entrada entradaConcreta = (Entrada) entrada;
        Funcion viejaFuncion = entradaConcreta.getFuncion();
        String espectaculo = entradaConcreta.getNombreEspectaculo();

        Funcion nuevaFuncion = espectaculos.get(espectaculo).getFuncion(fecha);
        if (nuevaFuncion == null) throw new RuntimeException("La función no existe");
        //obtiene la Sede
        Sede sede = nuevaFuncion.getSede();
        if (!sede.esEstadio()) throw new RuntimeException("Solo se puede cambiar entradas CAMPO en Estadios");

        // Crear nueva entrada
        String codigo = generarCodigoEntrada();
        double precio = sede.calcularPrecio("CAMPO", nuevaFuncion.getPrecioBase());

        Entrada nueva = new Entrada(codigo, espectaculo, nuevaFuncion, "CAMPO", 0, 0, precio);

        // Anular la vieja entrada
        usuario.getEntradas().remove(entrada);
        viejaFuncion.getEntradas().remove(entrada);
        entradasPorUsuario.remove(entrada);

        // Agregar nueva
        usuario.agregarEntrada(nueva);
        nuevaFuncion.getEntradas().add(nueva);
        entradasPorUsuario.put(nueva, usuario);

        return nueva;
    }

    @Override
    

    
    public double costoEntrada(String nombreEspectaculo, String fecha) {
        //Busca el nombre del espectaculo
    	Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if(espectaculo == null) {
        	throw new RuntimeException("el espectaculo no esta registrado");
        }
        //obtiene la funcion con la fecha
        Funcion funcion = (espectaculo.getFuncion(fecha));
        if(funcion == null) {
        	throw new RuntimeException("no existe una funcion para esta fecha");
        }
        //este metodo pertenece a ESTADIO, entonces devuelve el precio base
        return funcion.getPrecioBase(); 
    }

    @Override
    public double costoEntrada(String nombreEspectaculo, String fecha, String sector) {
    	//Busca el nombre del espectaculo
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if(espectaculo == null) {
        	throw new RuntimeException("el espectaculo no esta registrado");
        }
      //obtiene la funcion con la fecha
        Funcion funcion = espectaculo.getFuncion(fecha);
        if(funcion == null) {
        	throw new RuntimeException("No hay fecha para esta funcion");
        }
        //calcula el precio del sector
        return funcion.calcularPrecio(sector);
    }
 
    

    @Override
    public double totalRecaudado(String nombreEspectaculo) {
    	//Busca el nombre del espectaculo
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) {
            throw new IllegalArgumentException("No existe el espectáculo: " + nombreEspectaculo);
        }

        double total = 0;
        //Recorre todas las funciones del espectáculo.
        for (Funcion funcion : espectaculo.getFunciones()) {
            for (IEntrada entrada : funcion.getEntradas()) {
            	//Suma el precio de cada entrada
                total += entrada.precio();
            }
        }
        //Retorna el total
        return total;
    }

    @Override
    public double totalRecaudadoPorSede(String nombreEspectaculo, String nombreSede) {
    	//Busca el nombre del espectaculo
        Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
        if (espectaculo == null) {
            throw new IllegalArgumentException("No existe el espectáculo: " + nombreEspectaculo);
        }

        double total = 0;
      //Recorre todas las funciones del espectáculo.
        for (Funcion funcion : espectaculo.getFunciones()) {
            if (funcion.getSede().getNombre().equalsIgnoreCase(nombreSede)) {
                for (IEntrada entrada : funcion.getEntradas()) {
                	//Retorna el total
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
