
package DAO;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import miniproyecto.*;

public class ArchivoDAO {
    private Connection connection;
    
    public ArchivoDAO(){
        this.connection=DatabaseManager.getInstance().getConnection();
    }
    public void crearArchivo(Archivo a){
        // Antes de agregar, verifica si ya existe un archivo con ese nombre
        String buscaCarpeta="SELECT COUNT (*) from archivos WHERE directorio_padre_id=? AND nombre=?";
        try(PreparedStatement pstmt=connection.prepareStatement(buscaCarpeta)){
            pstmt.setInt(1,a.getDirectorio_padre_id());
            pstmt.setString(2,a.getNombre());
            // Verificar si rs tiene coincidencias y si su primer resultado ==0 (vacio)
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1)==0) {
                //Crear directorio si no existe
                String creaArchivo="INSERT INTO archivos(nombre,directorio_padre_id,espacio,marcos) VALUES(?,?,?,?)";
               
                try(PreparedStatement pstmt2=connection.prepareStatement(creaArchivo)){
                    pstmt2.setString(1,a.getNombre());
                    pstmt2.setInt(2, a.getDirectorio_padre_id());
                    pstmt2.setInt(3, a.getEspacio());
                    pstmt2.setInt(4, a.getMarcos());
                    pstmt2.executeUpdate();                
                  
                    //llenarmarcos
                    System.out.println("Se ha creado el archivo "+a.getNombre());                    
                }
            } else {
                System.out.println("Error: Ya existe un archivo o directorio con el nombre "+a.getNombre());
            }
        } catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int obtenerIDArchivo(Archivo a){
        int id=-1;
        String sql="SELECT id FROM archivos WHERE nombre=? AND directorio_padre_id=?";
        try(PreparedStatement pstmt=connection.prepareStatement(sql)){
            pstmt.setString(1,a.getNombre());
            pstmt.setInt(2,a.getDirectorio_padre_id());
            ResultSet rs=pstmt.executeQuery();
            
            id=rs.getInt("id");
            
        } catch (SQLException ex) {
            Logger.getLogger(ArchivoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
}
