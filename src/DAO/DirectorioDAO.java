
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
    public void crearDirectorio(Directorio d) {
        // Antes de agregar, verifica si ya existe un directorio con ese nombre
        String buscaCarpeta="SELECT COUNT (*) from directorios WHERE directorio_padre_id=? AND nombre=?";
        try(PreparedStatement pstmt=connection.prepareStatement(buscaCarpeta)){
            pstmt.setInt(1,d.getDirectorio_padre_id());
            pstmt.setString(2,d.getNombre());
            // Verificar si rs tiene coincidencias y si su primer resultado ==0 (vacio)
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
    public void verContenido(int id){
        verDirectorios(id);
        verArchivos(id);
    }
    public boolean verDirectorios(int id){
        String buscaCarpetas="SELECT id, nombre, puede_leer, puede_escribir FROM directorios WHERE directorio_padre_id=?";
        boolean estaVacio=false;
        try(PreparedStatement pstmt = connection.prepareStatement(buscaCarpetas)){
            pstmt.setInt(1,id);
            
            ResultSet rs=pstmt.executeQuery();
            System.out.println("====DIRECTORIOS====");
            
            while(rs.next()){
                estaVacio=true;
                System.out.println("📁 [DIR]  ID: "+rs.getInt("id")+
                        ", Nombre: "+rs.getString("nombre")+
                        ", Lectura: "+rs.getBoolean("puede_leer")+
                        ", Escritura: "+rs.getBoolean("puede_leer"));
            }                
            if(!estaVacio){
                System.out.println("No hay directorios");
                return estaVacio;
            }
        }catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return estaVacio;
    }
    public boolean verArchivos(int id){
        String buscarArchivos="SELECT id,nombre,espacio,marcos FROM archivos WHERE directorio_padre_id=?";
        boolean estaVacio=false;
            try(PreparedStatement pstmt2 = connection.prepareStatement(buscarArchivos)){
                pstmt2.setInt(1,id);
                ResultSet rs=pstmt2.executeQuery();
                System.out.println("====ARCHIVOS====");
                
                while(rs.next()){
                    estaVacio=true;
                    System.out.println("📄 [FILE] ID: "+rs.getInt("id")+
                            ", Nombre: "+rs.getString("nombre")+
                            ", Espacio: "+rs.getInt("espacio")+"GB"+
                            ", Marcos: "+rs.getInt("marcos"));                    
                }
                if(!estaVacio){
                    System.out.println("No hay archivos");
                    return estaVacio;
                } 
                
        }catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return estaVacio;
    }
    public void eliminarPorID(int id) {
        String eliminarDirectorio="DELETE FROM directorios WHERE id=?";   
        try(PreparedStatement pstmt=connection.prepareStatement(eliminarDirectorio)){
            pstmt.setInt(1, id);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Se ha eliminado la carpeta con éxito");
            } else {
                System.out.println("No se pudo eliminar la carpeta");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    public boolean estaVacio(int id) {
    // Verificar subdirectorios
    String sqlSubdirectorios = "SELECT COUNT(*) as count FROM directorios WHERE directorio_padre_id = ?";
    // Verificar archivos
    String sqlArchivos = "SELECT COUNT(*) as count FROM archivos WHERE directorio_padre_id = ?";
    
    try (
        PreparedStatement pstmtSubdir = connection.prepareStatement(sqlSubdirectorios);
        PreparedStatement pstmtArchivos = connection.prepareStatement(sqlArchivos)
    ) {
        // Verificar subdirectorios
        pstmtSubdir.setInt(1, id);
        ResultSet rsSubdir = pstmtSubdir.executeQuery();
        
        // Verificar archivos
        pstmtArchivos.setInt(1, id);
        ResultSet rsArchivos = pstmtArchivos.executeQuery();
        
        int totalSubdirectorios = 0;
        int totalArchivos = 0;
        
        if (rsSubdir.next()) {
            totalSubdirectorios = rsSubdir.getInt("count");
        }
        
        if (rsArchivos.next()) {
            totalArchivos = rsArchivos.getInt("count");
        }
        
        System.out.println("Subdirectorios encontrados: " + totalSubdirectorios);
        System.out.println("Archivos encontrados: " + totalArchivos);
        System.out.println("Total elementos: " + (totalSubdirectorios + totalArchivos));
        
        return (totalSubdirectorios + totalArchivos) == 0;
        
    } catch (SQLException ex) {
        System.err.println("Error verificando si el directorio está vacío: " + ex.getMessage());
        ex.printStackTrace();
        return false;
    }
}
    public Directorio obtenerRaiz(){
        String obtenerRaiz = "SELECT id, nombre, puede_leer, puede_escribir, directorio_padre_id FROM directorios WHERE directorio_padre_id IS NULL AND nombre = 'C:/'";
        Directorio raiz = null;
        try(Statement stmt =connection.createStatement()){
            ResultSet rs=stmt.executeQuery(obtenerRaiz);
                  
            if (rs.next()) {
                raiz = new Directorio();
                raiz.setId(rs.getInt("id"));
                raiz.setNombre(rs.getString("nombre"));
                raiz.setPuede_leer(rs.getBoolean("puede_leer"));
                raiz.setPuede_escribir(rs.getBoolean("puede_escribir"));
                raiz.setDirectorio_padre_id(rs.getInt("directorio_padre_id")); // Será 0 o null

                System.out.println("✓ Directorio raíz obtenido: " + raiz.getNombre());
            } else {
                System.err.println("✗ Error: No se encontró el directorio raíz en la base de datos");
            }           
        } catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Directorio raiz = null;
        return raiz;
    }
    public Directorio obtenerDirectorio(int directorio_padre_id,String nombre){
        String obtenerDirectorio = "SELECT id, nombre, puede_leer, puede_escribir, directorio_padre_id FROM directorios WHERE directorio_padre_id=? AND nombre=?";
        Directorio directorio=null;
        try(PreparedStatement pstmt =connection.prepareStatement(obtenerDirectorio)){
            pstmt.setInt(1, directorio_padre_id);
            pstmt.setString(2, nombre);
            ResultSet rs=pstmt.executeQuery();
                  
            if (rs.next()) {
                directorio = new Directorio();
                directorio.setId(rs.getInt("id"));
                directorio.setNombre(rs.getString("nombre"));
                directorio.setPuede_leer(rs.getBoolean("puede_leer"));
                directorio.setPuede_escribir(rs.getBoolean("puede_escribir"));
                directorio.setDirectorio_padre_id(rs.getInt("directorio_padre_id")); // Será 0 o null

                System.out.println("✓ Directorio obtenido: " + directorio.getNombre());
            } else {
                System.err.println("✗ Error: No se encontró el directorio en la base de datos");
            }           
        } catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Directorio raiz = null;
        return directorio;
    }
    public Directorio obtenerDirectorio(int directorio_padre_id){
        String obtenerDirectorio = "SELECT id, nombre, puede_leer, puede_escribir, directorio_padre_id FROM directorios WHERE directorio_padre_id=?";
        Directorio directorio=null;
        try(PreparedStatement pstmt =connection.prepareStatement(obtenerDirectorio)){
            pstmt.setInt(1, directorio_padre_id);
            
            ResultSet rs=pstmt.executeQuery();
                  
            if (rs.next()) {
                directorio = new Directorio();
                directorio.setId(rs.getInt("id"));
                directorio.setNombre(rs.getString("nombre"));
                directorio.setPuede_leer(rs.getBoolean("puede_leer"));
                directorio.setPuede_escribir(rs.getBoolean("puede_escribir"));
                directorio.setDirectorio_padre_id(rs.getInt("directorio_padre_id")); // Será 0 o null
                
                System.out.println("✓ Directorio obtenido: " + directorio.getNombre());
            } else {
                System.err.println("✗ Error: No se encontró el directorio raíz en la base de datos");
            }           
        } catch (SQLException ex) {
            Logger.getLogger(DirectorioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Directorio raiz = null;
        return directorio;
    }
}
