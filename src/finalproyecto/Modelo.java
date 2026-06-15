package finalproyecto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Modelo {
    private final String URL = "jdbc:mysql://localhost:3306/PracticaEntornos";
    private final String USER = "root";
    private final String PASSWORD = "1234"; 

    public Connection obtenerConexion() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }

    public String obtenerEventosDisponibles() {
        StringBuilder resultado = new StringBuilder("--- EVENTOS DISPONIBLES ---\n");
        String sql = "SELECT * FROM eventos";

        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultado.append("ID: ").append(rs.getInt("idEvento"))
                         .append(" - ").append(rs.getString("nombreEvento")).append("\n")
                         .append("Cartel: ").append(rs.getString("cartelEvento")).append("\n\n");
            }
            return resultado.toString();
        } catch (Exception ex) {
            return "Error al cargar eventos: " + ex.getMessage();
        }
    }

    public String obtenerTiposDisponibles() {
        StringBuilder resultado = new StringBuilder("--- TIPOS DE ENTRADA ---\n");
        String sql = "SELECT * FROM tipos";

        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // --- CAMBIADO: Ahora incluimos la descripción en la lista ---
                resultado.append("ID Tipo: ").append(rs.getInt("idTipo"))
                         .append(" - ").append(rs.getString("nombreTipo"))
                         .append(" (").append(rs.getDouble("precioTipo")).append("€)\n")
                         .append("    Info: ").append(rs.getString("descripcionTipo")).append("\n\n");
            }
            return resultado.toString();
        } catch (Exception ex) {
            return "Error al cargar tipos: " + ex.getMessage();
        }
    }

    // --- CAMBIADO: Ahora extraemos precio, nombre y descripción a la vez ---
    public String[] obtenerDatosTipo(int idTipo) throws Exception {
        String sql = "SELECT precioTipo, nombreTipo, descripcionTipo FROM tipos WHERE idTipo = ?";
        
        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idTipo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Devolvemos un array con [0]=precio, [1]=nombre, [2]=descripcion
                    return new String[] {
                        String.valueOf(rs.getDouble("precioTipo")),
                        rs.getString("nombreTipo"),
                        rs.getString("descripcionTipo")
                    };
                } else {
                    throw new Exception("El ID de Tipo de entrada no existe.");
                }
            }
        }
    }

    public void registrarVenta(String nombre, String dniEmail, int cantidad, double total, int idEvento, int idTipo) throws Exception {
        String sqlInsert = "INSERT INTO ventas (nombreClienteVenta, dniEmailClienteVenta, cantidadTicketsVenta, totalPagadoVenta, idEventoFK, idTipoFK) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, dniEmail);
            stmt.setInt(3, cantidad);
            stmt.setDouble(4, total);
            stmt.setInt(5, idEvento);
            stmt.setInt(6, idTipo);
            
            stmt.executeUpdate();
        }
    }
}