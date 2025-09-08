package pedidos.models;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/sistema_pedidos_nombre";
	private static final String USER = "root";
	private static final String PASS = "root"; // cambia según tu configuración

	public Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(URL, USER, PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}public static void main(String[] args) {
	    Conexion con = new Conexion();
	    try (Connection connection = con.getConnection()) {
	        if (connection != null) {
	            System.out.println("¡Conexión exitosa a la base de datos!");
	        } else {
	            System.out.println("Error: No se pudo establecer la conexión.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}