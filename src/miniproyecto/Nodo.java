
package miniproyecto;

public abstract class Nodo {
    protected String nombre;
    protected Directorio padre;

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.padre = null; // Se asignará cuando se añada a un directorio
    }

    public String getNombre() {
        return nombre;
    }

    public void setPadre(Directorio padre) {
        this.padre = padre;
    }

    public Directorio getPadre() {
        return padre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}