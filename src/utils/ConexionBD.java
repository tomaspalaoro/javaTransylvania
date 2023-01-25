package utils;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mariadb.jdbc.Connection;
import org.mariadb.jdbc.Statement;

import modelo.Usuario;

public class ConexionBD {
	String db;
	Connection conexion;
	Connection c;
	Statement s;
	ResultSet rs;
	public ConexionBD() {
		db = "gestion_hotelera";
		conexion = null;
		c = null;
		s = null;
		rs = null;
		conectarBaseDeDatos();
	}

	public void conectarBaseDeDatos() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			//objeto conexion inicializar la conexion
			conexion =	(Connection) DriverManager.getConnection("jdbc:mariadb://localhost:3306/"+db,"root","");
			if (conexion != null){
				System.out.println("Conexión a base de datos "+ db +" OK");		
			}else {
				System.err.println("Conexión vacía");
			}
		} catch (ClassNotFoundException e) {
			System.
			out.println(e.getMessage());
		} catch (SQLException e) {
			System.
			out.println(e.getMessage());
		}
	}
	
	public ArrayList<Usuario> obtenerTodosUsuarios() {
		ArrayList<Usuario> array = new ArrayList<Usuario>();
		try {
			Statement st = conexion.createStatement();
			//a partir de la sentencia un execute query
			ResultSet rs = st.executeQuery("SELECT * FROM users");
			while(rs.next()) {				
				array.add(new Usuario(rs.getString("email"),rs.getString("password"),rs.getString("token"),rs.getString("fecha_validez_token"),rs.getString("nombre"),rs.getString("apellidos"),rs.getString("telefono"),rs.getString("fecha_baja"),rs.getString("created_at"),rs.getString("updated_at")));
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return array;		
	}

	public void desconectar() {		
		try {
			if (s != null) s.close();
			if (rs != null) rs.close();
			if (conexion != null) conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}