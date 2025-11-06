
package DAO;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import miniproyecto.DatabaseManager;
public class MemoriaDAO {
    private Connection connection;
    
    public MemoriaDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }
    
    public boolean hayMarcosDisponibles(int no){
        String sql="SELECT marcosDisponibles FROM memoria WHERE id=1";
        
        try(Statement stmt=connection.createStatement()){
            ResultSet rs=stmt.executeQuery(sql);
            
            if(rs.next()){
                int marcos =rs.getInt(1);
                //t si hay disponibles
                
                return marcos>=no;
            }
            return false; 
        } catch (SQLException ex) {
            Logger.getLogger(MemoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        }
    }
    public void restarMarcos(int noMarcos){
        String queryMarcos="SELECT marcosDisponibles from memoria WHERE id=1";       
        String actualizarMarcos="UPDATE memoria SET marcosDisponibles=? WHERE id=1";
        
        try(Statement stmt=connection.createStatement();
            PreparedStatement pstmt=connection.prepareStatement(actualizarMarcos)
        ){
            ResultSet rs=stmt.executeQuery(queryMarcos);
            int marcosDisponibles=rs.getInt("marcosDisponibles");
            
            marcosDisponibles-=noMarcos;
            
            pstmt.setInt(1, marcosDisponibles);
            pstmt.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(MemoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void sumarMarcos(int noMarcos){
        String queryMarcos="SELECT marcosDisponibles from memoria WHERE id=1";       
        String actualizarMarcos="UPDATE memoria SET marcosDisponibles=? WHERE id=1";
        
        try(Statement stmt=connection.createStatement();
            PreparedStatement pstmt=connection.prepareStatement(actualizarMarcos)
        ){
            ResultSet rs=stmt.executeQuery(queryMarcos);
            int marcosDisponibles=rs.getInt("marcosDisponibles");
            
            marcosDisponibles+=noMarcos;
            
            pstmt.setInt(1, marcosDisponibles);
            pstmt.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(MemoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
