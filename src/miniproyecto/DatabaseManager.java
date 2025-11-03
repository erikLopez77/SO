
package miniproyecto;
import DAO.DirectorioDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:filesystem.db";
    private static DatabaseManager instance;
    private Connection connection;
    
    private DatabaseManager() {
        conectar();
        initializeDatabase();
    }
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
     // Método para establecer la conexión
    private void conectar() {
        try {
            // Asegurar que solo hay una conexión
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                // Configurar para mejor manejo de concurrencia
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
    }
    
    // Inicializar la base de datos y crear tablas
    private void initializeDatabase() {
        String createTableFile = 
            "CREATE TABLE IF NOT EXISTS archivos ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombre TEXT NOT NULL,"+
                "directorio_padre_id INTEGER,"+
                "espacio INTEGER,"+
                "marcos INTEGER,"+
                "FOREIGN KEY (directorio_padre_id) REFERENCES directorios(id) ON DELETE CASCADE"+
            ");";
        String createTableFolder=
             "CREATE TABLE IF NOT EXISTS directorios ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombre TEXT NOT NULL,"+
                "directorio_padre_id INTEGER,"+
                "puede_leer BOOLEAN DEFAULT 1,"+
                "puede_escribir BOOLEAN DEFAULT 1,"+
                "FOREIGN KEY (directorio_padre_id) REFERENCES directorios(id) ON DELETE CASCADE"+
            ");";
        String createTableMemory=
             "CREATE TABLE IF NOT EXISTS memoria ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "espacioDisponible INTEGER NOT NULL,"+
                "espacioTotal INTEGER NOT NULL"+
            ");";
        String createTableMarco=
             "CREATE TABLE IF NOT EXISTS marco ("+
                "indice INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "archivo_id INTEGER,"+
                "memoria_id INTEGER,"+
                "FOREIGN KEY (archivo_id) REFERENCES archivos(id) ON DELETE CASCADE,"+
                "FOREIGN KEY (memoria_id) REFERENCES memoria(id) ON DELETE CASCADE"+
            ");";
        try (Statement stmt = connection.createStatement()) {           
            stmt.execute(createTableFolder);        
            stmt.execute(createTableMemory); 
            stmt.execute(createTableFile);
            stmt.execute(createTableMarco);  
            // Crear nodo raíz si no existe
            crearNodoRaiz();
            crearMemoriaInicial(); // 1280 total, 64 por marco
        } catch (SQLException e) {
            System.err.println("Error inicializando base de datos: " + e.getMessage());
        }
    }
    
    private void crearNodoRaiz() throws SQLException {
        String checkRootSQL = "SELECT COUNT(*) FROM directorios WHERE directorio_padre_id IS NULL AND nombre = 'C:/'";
        String insertRootSQL = 
            "INSERT INTO directorios (nombre, directorio_padre_id, puede_leer, puede_escribir) " + 
            "SELECT 'C:/', NULL, 1, 1 " +
            "WHERE NOT EXISTS (SELECT 1 FROM directorios WHERE directorio_padre_id IS NULL AND nombre = 'C:/')";

        try (Statement stmt = connection.createStatement()) {
            // Verificar si el nodo raíz ya existe
            ResultSet rs = stmt.executeQuery(checkRootSQL);
            //.next Devuelve true si hay un registro .getInt obtiene columna indicada y se compara si es vacio (==0)
            if (rs.next() && rs.getInt(1) == 0) {
                // Insertar nodo raíz si no existe
                stmt.execute(insertRootSQL);
                System.out.println("Nodo raíz 'C:/' creado exitosamente.");
            } else {
                System.out.println("Nodo raíz 'C:/' ya existe.");
            }
        }
    }

    private void crearMemoriaInicial() throws SQLException {
        String checkMemorySQL = "SELECT COUNT(*) FROM memoria";
        String insertMemorySQL = 
            "INSERT INTO memoria (espacioDisponible, espacioTotal) " +
            "SELECT 1280, 1280 " +
            "WHERE NOT EXISTS (SELECT 1 FROM memoria)";

        String crearMarcosSQL = 
            "INSERT INTO marco (archivo_id, memoria_id) " +
            "SELECT NULL, id FROM memoria " +
            "WHERE (SELECT COUNT(*) FROM marco) = 0";

        try (Statement stmt = connection.createStatement()) {
            // Verificar si ya existe memoria
            ResultSet rs = stmt.executeQuery(checkMemorySQL);
            if (rs.next() && rs.getInt(1) == 0) {
                // Crear memoria con 1280 unidades
                stmt.execute(insertMemorySQL);
                System.out.println("Memoria inicializada con 1280 unidades.");

                // Crear marcos de memoria (puedes ajustar la cantidad)
                // Por ejemplo, si cada marco tiene 64 unidades, necesitarías 20 marcos
                int cantidadMarcos = 64; // 1280 / 64 = 20 marcos
                for (int i = 0; i < cantidadMarcos; i++) {
                    stmt.addBatch("INSERT INTO marco (archivo_id, memoria_id) VALUES (NULL, 1)");
                }
                stmt.executeBatch();
                System.out.println("Se crearon " + cantidadMarcos + " marcos de memoria.");
            } else {
                System.out.println("La memoria ya estaba inicializada.");
            }
        }
    }
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo conexión: " + e.getMessage());
        }
        return connection;
    }
    
    public void cerrarConexion() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}