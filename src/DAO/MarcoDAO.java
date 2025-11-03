
package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
}
