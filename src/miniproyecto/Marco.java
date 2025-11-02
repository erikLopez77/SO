
package miniproyecto;

public class Marco {
    private int indice;
    private int archivo_id;
    private int memoria_id;
    
    public Marco() { }
    public Marco(int archivo_id,int memoria_id){
        this.archivo_id=archivo_id;
        this.memoria_id=memoria_id;
    }
    
    public int getIndice(){
        return this.indice;
    }
    public int getArchivo_id(){
        return this.archivo_id;
    }
    public void setArchivo_id(int archivo_id){
        this.archivo_id=archivo_id;
    }
    public int getMemoria_id(){
        return this.memoria_id;
    }
    public void setMemoria_id(int memoria_id){
        this.memoria_id=memoria_id;
    }
}
