
package DAO;

import java.sql.*;
import miniproyecto.DatabaseManager;
public class MemoriaDAO {
    private Connection connection;
    
    public MemoriaDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }
}
