
package miniproyecto;

public class Archivo {
    private int id;
    private String nombre;
    private int directorio_padre_id;
    private int espacio;
    private int marcos;
    
    public Archivo() { }
    
    public Archivo(String nombre,int directorio_padre_id, int espacio,int marcos){
        this.nombre=nombre;
        this.directorio_padre_id=directorio_padre_id;
        this.espacio=espacio;
        this.marcos=marcos;
    }
    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return this.id;
    }
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    public String getNombre(){
        return this.nombre;
    }
    public void setDirectorio_padre_id(int directorio_padre_id){
        this.directorio_padre_id=directorio_padre_id;
    }
    public int getDirectorio_padre_id(){
        return this.directorio_padre_id;
    }
    public void setEspacio(int espacio){
        this.espacio=espacio;
    }
    public int getEspacio(){
        return this.espacio;
    }
    public void setMarcos(int marcos){
        this.marcos=marcos;
    }
    public int getMarcos(){
        return this.marcos;
    }
}
