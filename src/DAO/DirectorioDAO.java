
package DAO;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import miniproyecto.DatabaseManager;
import miniproyecto.Directorio;

public class DirectorioDAO {
    private Connection connection;
    
    public DirectorioDAO (){
        this.connection=DatabaseManager.getInstance().getConnection();
    }
    public void agregarHijo(Directorio d) {
        // Antes de agregar, verifica si ya existe un nodo con ese nombre
        String buscaCarpeta="SELECT COUNT (*) from directorios WHERE directorio_padre_id=? AND nombre=?";
        try(PreparedStatement pstmt=connection.prepareStatement(buscaCarpeta)){
            pstmt.setInt(1,d.getDirectorio_padre_id());
            pstmt.setString(2,d.getNombre());
            // Verificar si el nodo raíz ya existe
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1)==0) {
                //Crear directorio si no existe
                String creaDirectorio="INSERT INTO directorios(nombre,directorio_padre_id,puede_leer,puede_escribir) VALUES(?,?,?,?)";
               
                try(PreparedStatement pstmt2=connection.prepareStatement(creaDirectorio)){
                    pstmt2.setString(1,d.getNombre());
                    pstmt2.setInt(2, d.getDirectorio_padre_id());
                    pstmt2.setBoolean(3, d.getPuede_leer());
                    pstmt2.setBoolean(4, d.getPuede_escribir());
                    pstmt2.executeUpdate();
                    System.out.println("Se ha creado la carpeta "+d.getNombre());
                }
            } else {
                System.out.println("Error: Ya existe un archivo o directorio con el nombre "+d.getNombre());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarHijo(String nombre,int directorio_padre_id) {
        String eliminarDirectorio="DELETE FROM directorios WHERE nombre=? AND directorio_padre_id=?";
        
        try(PreparedStatement pstmt=connection.prepareStatement(eliminarDirectorio)){
            pstmt.setString(1, nombre);
            pstmt.setInt(2,directorio_padre_id);
            pstmt.executeUpdate();
            System.out.println("Se ha eliminado la carpeta con èxito");
        } catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        //hijos.removeIf(hijo -> hijo.getNombre().equals(nombre));
    }
    /*
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
    */
    public boolean estaVacio(int directorio_padre_id) {
        String estaVacio="SELECT COUNT(*) FROM directorios WHERE directorio_padre_id=? AND SELECT COUNT(*) FROM archivos WHERE directorio_padre_id=?";
        
        try(PreparedStatement pstmt = connection.prepareStatement(estaVacio)){
            pstmt.setInt(1,directorio_padre_id);
            pstmt.setInt(2,directorio_padre_id);
            ResultSet rs=pstmt.executeQuery();
            if(rs.next() && rs.getInt(1)==0){
                //en este caso no hay coindidencias
                return true;
            }
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public Directorio obtenerRaiz(){
        String obtenerRaiz="SELECT COUNT(*) FROM directorios WHERE directorio_padre_id IS NULL AND nombre = 'C:/'";
        Directorio raiz=new Directorio();
        try(Statement stmt =connection.createStatement()){
            ResultSet rs=stmt.executeQuery(obtenerRaiz);
                  
            raiz.setId(rs.getInt("id"));
            raiz.setNombre(rs.getString("nombre"));
            raiz.setPuede_leer(rs.getBoolean("puede_leer"));
            raiz.setPuede_escribir(rs.getBoolean("puede_escribir"));
            
        } catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Directorio raiz = null;
        return raiz;
    }
}
