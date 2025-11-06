
package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import miniproyecto.*;

public class MarcoDAO {
    private Connection connection;
    
    public MarcoDAO (){
        this.connection=DatabaseManager.getInstance().getConnection();
    }
    public List<Marco> obtenerMarcos(){
        String sql = "SELECT * FROM marco";
        List<Marco> marcos = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Marco marco = new Marco();
                marco.setIndice(rs.getInt("indice"));
                marco.setArchivo_id(rs.getInt("archivo_id"));
                marco.setMemoria_id(rs.getInt("memoria_id"));              
                marcos.add(marco);
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo usuarios: " + e.getMessage());
        }
        return marcos;
    }
    public void llenarMarcos(int noMarcos,int id){
        List<Marco> marcos=obtenerMarcos();
        int count=0;
        //List<int> indices=new ArrayList<>;
        String sql="UPDATE marco SET archivo_id=? WHERE indice=?";
        for(Marco m:marcos){
            if(m.getArchivo_id()==0 && noMarcos!=count){
               try(PreparedStatement pstmt=connection.prepareStatement(sql)){
                   pstmt.setInt(1,id);
                   pstmt.setInt(2, m.getIndice());
                   pstmt.executeUpdate();
                   count++;
               } catch (SQLException ex) {
                    Logger.getLogger(MarcoDAO.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
        }
    }
    public void vaciarMarcos(int id){
        String sql="UPDATE marco SET archivo_id=NULL WHERE archivo_id=?";
        try(PreparedStatement pstmt=connection.prepareStatement(sql)){
           pstmt.setInt(1, id);
           pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MarcoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
