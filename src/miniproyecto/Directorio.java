
package miniproyecto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Directorio {
    private int id;
    private String nombre;
    private int directorio_padre_id;
    private boolean puede_leer;
    private boolean puede_escribir;
    
    public Directorio() { }

    public Directorio(String nombre,int directorio_padre_id, boolean puede_leer,boolean puede_escribir){
        this.nombre=nombre;
        this.directorio_padre_id=directorio_padre_id;
        this.puede_leer=puede_leer;
        this.puede_escribir=puede_escribir;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getNombre(){
        return this.nombre;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    public int getDirectorio_padre_id(){
        return this.directorio_padre_id;
    }
    public void setDirectorio_padre_id(int id){   
        this.directorio_padre_id=id;
    }
    public boolean getPuede_leer(){
        return this.puede_leer;
    }
    public void setPuede_leer(boolean puede_leer){
        this.puede_leer=puede_leer;
    }
    public boolean getPuede_escribir(){
        return this.puede_escribir;
    }
    public void setPuede_escribir(boolean puede_escribir){
        this.puede_escribir=puede_escribir;
    }
}