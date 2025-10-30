
package miniproyecto;

import java.util.Scanner;

public class MiniProyecto {

    private static Directorio raiz;
    private static Directorio directorioActual;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // 1. Definir la raíz
        raiz = new Directorio("raiz");
        directorioActual = raiz;

        // 2. Iniciar el menú principal
        mostrarMenuPrincipal();
    }
    
    public static void mostrarMenuPrincipal() {
        int opcion = 0;
        while (opcion != 4) {
            System.out.println("Ubicación Actual: " + obtenerRutaActual());
            System.out.println("-----------------------------------------");
            System.out.println("1. Crear");
            System.out.println("2. Eliminar");
            System.out.println("3. Navegación");
            System.out.println("4. Salir");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce una opción válida.");
                continue;
            }

            switch (opcion) {
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
            System.out.println("\n--- Menú Crear ---");
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
                    crearArchivo();
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
        while (!opcion.equalsIgnoreCase("C")) {
            System.out.println("\n--- |Menú Eliminar| ---");
            System.out.println("A. Eliminar Directorio");
            System.out.println("B. Eliminar Archivo");
            System.out.println("C. Volver al menú principal");
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
            System.out.println("\n--- |Menú Navegación| ---");
            System.out.println("Ubicación Actual: " + obtenerRutaActual());
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
                    mostrarContenido();
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

    private static void crearDirectorio() {
        System.out.print("Nombre del directorio: ");
        String nombre = scanner.nextLine();
        if (nombre.isEmpty()) {
            System.out.println("Error: El nombre no puede estar vacío.");
            return;
        }
        Directorio nuevoDir = new Directorio(nombre);
        directorioActual.agregarHijo(nuevoDir);
    }

    private static void crearArchivo() {
        System.out.print("Nombre del archivo: ");
        String nombre = scanner.nextLine();
        if (nombre.isEmpty()) {
            System.out.println("Error: El nombre no puede estar vacío.");
            return;
        }
        Archivo nuevoArchivo = new Archivo(nombre);
        directorioActual.agregarHijo(nuevoArchivo);
    }

    private static void eliminarDirectorio() { 
        System.out.print("Directorio a eliminar: ");
        String nombre = scanner.nextLine();
        Nodo nodo = directorioActual.buscarHijo(nombre);

        if (nodo instanceof Directorio) {
            Directorio dir = (Directorio) nodo;
            if (dir.estaVacio()) {
                directorioActual.eliminarHijo(nombre);
                System.out.println("Directorio '" + nombre + "' eliminado.");
            } else {
                System.out.println("Error: No se puede borrar el directorio porque no está vacío.");
            }
        } else if (nodo == null) {
            System.out.println("Error: El directorio '" + nombre + "' no existe.");
        } else {
            System.out.println("Error: '" + nombre + "' no es un directorio.");
        }
    }

    private static void eliminarArchivo() {
        System.out.print("Archivo a eliminar: ");
        String nombre = scanner.nextLine();
        Nodo nodo = directorioActual.buscarHijo(nombre);

        if (nodo instanceof Archivo) {
            directorioActual.eliminarHijo(nombre);
            System.out.println("Archivo '" + nombre + "' eliminado.");
        } else if (nodo == null) {
            System.out.println("Error: El archivo '" + nombre + "' no existe.");
        } else {
            System.out.println("Error: '" + nombre + "' no es un archivo.");
        }
    }

    private static void mostrarContenido() {
        System.out.println("\n--- Contenido de '" + directorioActual.getNombre() + "' ---");
        if (directorioActual.estaVacio()) {
            System.out.println("(Directorio vacío)");
        } else {
            for (Nodo hijo : directorioActual.getHijos()) {
                if (hijo instanceof Directorio) {
                    System.out.println("📁 [DIR]  " + hijo.getNombre());
                } else {
                    System.out.println("📄 [FILE] " + hijo.getNombre());
                }
            }
        }
    }

    private static void accederDirectorio() {
        System.out.print("Nombre del directorio al que se quiere acceder: ");
        String nombre = scanner.nextLine();
        Nodo nodo = directorioActual.buscarHijo(nombre);

        if (nodo instanceof Directorio) {
            directorioActual = (Directorio) nodo;
        } else {
            System.out.println("Error: No se encontró el directorio '" + nombre + "'.");
        }
    }

    private static void subirNivel() {
        if (directorioActual.getPadre() != null) {
            directorioActual = directorioActual.getPadre();
        } else {
            System.out.println("Raíz, no se puede subir más.");
        }
    }

    private static void irALaRaiz() {
        directorioActual = raiz;
    }

    private static String obtenerRutaActual() {
        if (directorioActual == raiz) {
            return "/raiz";
        }
        
        StringBuilder ruta = new StringBuilder();
        Directorio temp = directorioActual;
        while (temp != null) {
            ruta.insert(0, "/" + temp.getNombre());
            temp = temp.getPadre();
        }
        // Elimina el duplicado "/raiz" al principio si existe
        if (ruta.toString().startsWith("/raiz")) {
             return ruta.substring(1);
        }
        return ruta.toString();
    }
}