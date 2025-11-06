
package miniproyecto;

import DAO.*;
import java.util.Scanner;
import java.sql.*;

public class MiniProyecto {
    //disponible 640gb, 64 marcos de 20 gb 1280 gb
    private static String usuario="admin",password="1234";
    private static boolean sesionIniciada=false;
    private static Directorio raiz;
    private static Directorio directorioActual;
    private static DirectorioDAO directorioDAO;
    private static ArchivoDAO archivoDAO;
    private static MarcoDAO marcoDAO;
    private static MemoriaDAO memoriaDAO;
    private static StringBuilder rutaActual;
    private static final int espacioMarco = 20;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DatabaseManager dbManager=DatabaseManager.getInstance();
        
        directorioDAO=new DirectorioDAO();
        archivoDAO=new ArchivoDAO();
        marcoDAO=new MarcoDAO();
        memoriaDAO=new MemoriaDAO();
        // 1. Definir la raíz
        irALaRaiz();
        
        // 2. Iniciar el menú principal
        mostrarMenuPrincipal();
    }
    
    public static void mostrarMenuPrincipal() {
        int opcion = 0;
        while (opcion != 4) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("Ruta actual: "+rutaActual);
            System.out.println("-----------------------------------------");
            if(!sesionIniciada){
                System.out.println("0. Iniciar sesión");
            }else{
                System.out.println("0. Cerrar sesión");
            }         
            System.out.println("1. Crear");
            System.out.println("2. Eliminar/Editar");
            System.out.println("3. Navegar");
            System.out.println("4. Salir");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce una opción válida.");
                continue;
            }
            
            switch (opcion) {
                case 0:
                    if(!sesionIniciada){
                        iniciarSesion();
                    }else{
                        cerrarSesion();
                    }
                    break;
                case 1:
                    mostrarMenuCrear();
                    break;
                case 2:
                    mostrarMenuEliminar();
                    break;
                case 3:
                    mostrarMenuNavegacion();
                    break;
                case 4:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        scanner.close();
    }
   
    private static void mostrarMenuCrear() {
        String opcion = "";
        while (!opcion.equalsIgnoreCase("C")) {
            if(!sesionIniciada){
                System.out.println("\n(No ha iniciado sesión)");
            }else{
                System.out.println("\n(Se ha iniciado sesión)");
            }
            System.out.println("--- Menú Crear ---");
            System.out.println("Ruta actual: "+rutaActual);
            System.out.println("--------------------------");
            System.out.println("A. Crear Directorio");
            System.out.println("B. Crear Archivo");
            System.out.println("C. Volver al menú principal");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextLine();

            switch (opcion.toUpperCase()) {
                case "A":
                    crearDirectorio();
                    break;
                case "B":
                    crearArchivo(directorioActual.getId());
                    break;
                case "C":
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void mostrarMenuEliminar() {
        String opcion = "";
        while (!opcion.equalsIgnoreCase("E")) {
            if(!sesionIniciada){
                System.out.println("\n(No ha iniciado sesión)");
            }else{
                System.out.println("\n(Se ha iniciado sesión)");
            }
            System.out.println("--- |Menú Eliminar| ---");
            System.out.println("Ruta actual: "+rutaActual);
            System.out.println("--------------------------");
            System.out.println("A. Eliminar Directorio");
            System.out.println("B. Eliminar Archivo");
            System.out.println("C. Incrementar/Decrementar tamaño del archivo");
            System.out.println("D. Actualizar permisos de la carpeta actual");
            System.out.println("E. Volver al menú principal");
            System.out.print("Opción: ");
            opcion = scanner.nextLine();

            switch (opcion.toUpperCase()) {
                case "A":
                    eliminarDirectorio();
                    break;
                case "B":
                    eliminarArchivo();
                    break;
                case "C":
                    incrementarDecrementar();
                    break;
                case "D":
                    actualizarPermisos();
                    break;
                case "E":
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private static void mostrarMenuNavegacion() {
        String opcion = "";
        while (!opcion.equalsIgnoreCase("E")) {
            if(!sesionIniciada){
                System.out.println("\n(No ha iniciado sesión)");
            }else{
                System.out.println("\n(Se ha iniciado sesión)");
            }
            System.out.println("--- |Menú Navegación| ---");
            System.out.println("Ruta actual: "+rutaActual);
            System.out.println("--------------------------");
            System.out.println("A. Mostrar Contenido (Carpeta Actual)");
            System.out.println("B. Acceder a Carpeta");
            System.out.println("C. Subir un Nivel");
            System.out.println("D. Subir a la Raíz");
            System.out.println("E. Volver al menú principal");
            System.out.print("Opción: ");
            opcion = scanner.nextLine();

            switch (opcion.toUpperCase()) {
                case "A":
                    directorioDAO.verContenido(directorioActual.getId());
                    break;
                case "B":
                    accederDirectorio();
                    break;
                case "C":
                    subirNivel();                   
                    break;
                case "D":
                    irALaRaiz();
                    break;
                case "E":
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    // --- MÉTODOS DE ACCIÓN (Sin cambios) ---
    private static void iniciarSesion(){
        System.out.print("Escribe el nombre de usuario: ");
        String usuario2=scanner.nextLine();
        System.out.print("Escribe la contraseña: ");
        String password2=scanner.nextLine();
        
        if(usuario2.equals(usuario) && password2.equals(password)){
            sesionIniciada=true;
            System.out.println("Sesión iniciada con éxito");
        }else{
            System.out.println("Revisa tu contraseña o nombre de usuario");
        }    
    }
    private static void cerrarSesion(){
        System.out.println("Sesión terminada con éxito");
        sesionIniciada=false;
    }
    private static void crearDirectorio() {      
        //revisar permisos de la carpeta actual
        if(directorioActual.getPuede_escribir()|| sesionIniciada){
            System.out.print("Nombre del directorio: ");
            String nombre = scanner.nextLine();
            if (nombre.isEmpty()) {
                System.out.println("Error: El nombre no puede estar vacío.");
                return;
            }
            Directorio nuevoDir = new Directorio(nombre,directorioActual.getId(),true,true);
            directorioDAO.crearDirectorio(nuevoDir);
        }else{
            System.out.println("Lo sentimos, no tienes permiso de crear una carpeta");
        }
    }

    private static void crearArchivo(int directorio_padre_id) {
        //revisar permisos de la carpeta actual
        if(directorioActual.getPuede_escribir()|| sesionIniciada){
            
            System.out.print("Nombre del archivo: ");
            String nombre = scanner.nextLine();
            if (nombre.isEmpty()) {
                System.out.println("Error: El nombre no puede estar vacío.");
                return;
            }
            System.out.print("Tamaño en GB(numeros enteros):");
            int espacio=scanner.nextInt();
            //calculamos los marcos necesarios y lo redondeamos hacia arriba para tomar la parte entera
            int m = (int) Math.ceil((double) espacio / espacioMarco); 

            //si hay espacio suficiente se crea el marco
            if(memoriaDAO.hayMarcosDisponibles(m)){
                Archivo nuevoArchivo = new Archivo(nombre,directorio_padre_id,espacio,m);
                archivoDAO.crearArchivo(nuevoArchivo);
                int id=archivoDAO.obtenerIDArchivo(nuevoArchivo);
                //System.out.println("el id es: "+id);
                marcoDAO.llenarMarcos(m,id);           
                //restar marcos para el espacioDisponible
                memoriaDAO.restarMarcos(m);    
            }else{
                System.out.println("Espacio insuficiente");
            }           
        }else{
            System.out.println("Lo sentimos, la carpeta no tiene el permiso de la carpeta actual");
        }
        
    }

    private static void eliminarDirectorio() { 
        boolean hayDirectorios=directorioDAO.verDirectorios(directorioActual.getId());
        //revisar permisos de la carpeta actual
        if((hayDirectorios && directorioActual.getPuede_escribir())|| sesionIniciada){
            System.out.print("ID del Directorio a eliminar: ");
            int id = scanner.nextInt();
            //Nodo nodo = directorioActual.buscarHijo(nombre);
            if(directorioDAO.estaVacio(id)){
                directorioDAO.eliminarPorID(id);
            }   
        } else{
            System.out.println("La carpeta actual no permite hacer cambios");
        }
        
    }

    private static void eliminarArchivo() {
        boolean hayArchivos=directorioDAO.verArchivos(directorioActual.getId());
        //revisar permisos de la carpeta actual
        if((hayArchivos && directorioActual.getPuede_escribir())|| sesionIniciada){
            System.out.print("ID del archivo a eliminar: ");
            int id = scanner.nextInt();
            //obtener el archivo a Eliminar
            Archivo arch=archivoDAO.obtenerArchivo(id, directorioActual.getId());
            //liberar la memoria
            memoriaDAO.sumarMarcos(arch.getMarcos());
            marcoDAO.vaciarMarcos(arch.getId());
            archivoDAO.eliminarArchivo(id, directorioActual.getId());
        }else{
            System.out.println("La carpeta actual no permite hacer cambios");
        }    
    }
    private static void incrementarDecrementar(){
        //revisar permisos de la carpeta actual
        //comparar si el nuevo espacio es igual o menor
    }
    private static void actualizarPermisos(){
        boolean read=false,write=false;
        if(!sesionIniciada){
            System.out.println("Lo sentimos, sólo el administrador es quien puede cambiar permisos");
        }else{
            System.out.print("Digita 1 si la carpeta actual la pueden leer otros, 0 nadie más la puede leer: ");
            int leer=scanner.nextInt();
            if(leer==1){
                read=true;
            }
            System.out.print("Digita 1 si la carpeta actual la pueden escribir otros, 0 nadie más puede escribir: ");
            int escribir=scanner.nextInt();
            if(escribir==1){
                write=true;
            }
            //funcion para actualizar permisos
            directorioDAO.actualizarPermisos(read, write, directorioActual.getId());
        } 
    }
    
    private static void accederDirectorio() {
        System.out.print("Nombre del directorio al que se quiere acceder: ");
        String nombre = scanner.nextLine();
        
        Directorio directorio=directorioDAO.obtenerDirectorio(directorioActual.getId(), nombre);
        //revisamos su permiso
        if((directorio!=null  && directorio.getPuede_leer()) || sesionIniciada){
            directorioActual=directorio;
            rutaActual.append("/"+directorioActual.getNombre());
        }else{
            System.out.println("Lo sentimos, no se puede acceder a la carpeta");
        }
    }

    private static void subirNivel() {//checar aquì
        if (directorioActual.getDirectorio_padre_id() != 0) {
            //directorioActual = directoriorioActual.getPadre();
            Directorio directorio=directorioDAO.obtenerDirectorio(directorioActual.getDirectorio_padre_id());
            System.out.println("El directorio al que subimos es "+directorio.getNombre());
            if(directorio!=null){
                int lengthDir=directorioActual.getNombre().length()+1;
                directorioActual=directorio;                
             
                rutaActual.delete(rutaActual.length()-lengthDir,rutaActual.length());
            }
        } else {
            System.out.println("Raíz, no se puede subir más.");
        }
    }

    private static void irALaRaiz() {
        Directorio raiz=directorioDAO.obtenerRaiz();
        directorioActual = raiz;
        rutaActual=new StringBuilder("C:/");
    }

}