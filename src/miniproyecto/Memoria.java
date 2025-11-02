
package miniproyecto;
//disponible 640gb, 64 marcos de 20 gb 1280 gb
public class Memoria {
    private int id;
    private int espacioDisponible;
    private int espacioTotal;
    
    public Memoria() { }
    
    public Memoria( int espacioDisponible,int espacioTotal){
        this.espacioDisponible=espacioDisponible;
        this.espacioTotal=espacioTotal;
    }
    public int getId(){
        return this.id;
    }
    public int getEspacioDisponible(){
        return this.espacioDisponible;
    }
    public void setEspacioDisponible(int espacioDisponible){
        this.espacioDisponible=espacioDisponible;
    }
    public int getEspacioTotal(){
        return this.espacioTotal;
    }
    public void setEspacioTotal(int espacioTotal){
        this.espacioTotal=espacioTotal;
    }
}
