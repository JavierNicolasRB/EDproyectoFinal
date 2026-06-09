package finalproyecto;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Clase Modelo encargada de gestionar la conexión con la base de datos.
 */
public class Modelo {
	private final String URL = "jdbc:mysql://localhost:3306/PracticaEntornos";
	private final String USER = "root";
	private final String PASSWORD = "Studium2026*"; 

	/**
	 * Establece y devuelve una conexión a la base de datos MySQL.
	 * * @return Objeto Connection con la conexión activa, o null si falla.
	 */
	public Connection obtenerConexion() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (Exception e) {
			System.out.println("Error de conexion: " + e.getMessage());
			return null;
		}
	}
}