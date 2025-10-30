
package miniproyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Directorio extends Nodo {
    private List<Nodo> hijos;

    public Directorio(String nombre) {
        super(nombre);
        this.hijos = new ArrayList<>();
    }

    public void agregarHijo(Nodo hijo) {
        // Antes de agregar, verifica si ya existe un nodo con ese nombre
        if (buscarHijo(hijo.getNombre()) == null) {
            hijo.setPadre(this);
            this.hijos.add(hijo);
        } else {
            System.out.println("Error: Ya existe un archivo o directorio con el nombre '" + hijo.getNombre() + "'");
        }
    }

    public void eliminarHijo(String nombre) {
        hijos.removeIf(hijo -> hijo.getNombre().equals(nombre));
    }

    public Nodo buscarHijo(String nombre) {
        // Usamos Optional para manejar el caso de que no se encuentre el hijo
        Optional<Nodo> encontrado = hijos.stream()
                                         .filter(hijo -> hijo.getNombre().equals(nombre))
                                         .findFirst();
        return encontrado.orElse(null);
    }

    public List<Nodo> getHijos() {
        return hijos;
    }

    public boolean estaVacio() {
        return hijos.isEmpty();
    }
}