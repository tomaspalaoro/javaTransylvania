package conexion;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mariadb.jdbc.Connection;
import org.mariadb.jdbc.Statement;

import modelo.Habitacion;
import modelo.Reserva;
import modelo.Reserva_Habitacion;
import modelo.Usuario;

/**
 * Clase Singleton que conecta con la base de datos a través de JDBC
 * 
 * @author Tomas
 *
 */
public class ConexionBD {

	private static ConexionBD instance;

	String db;
	public Connection conexion;
	Statement st;
	ResultSet rs;

	public ConexionBD() {
		db = "gestion_hotelera";
		conexion = null;
		st = null;
		rs = null;
		conectarBaseDeDatos();
	}

	/**
	 * Instancia singleton
	 * 
	 * @return
	 */
	public static ConexionBD getInstance() {
		if (instance == null) {
			instance = new ConexionBD();
		}
		return instance;
	}

	/**
	 * Inicializa la conexión con MariaDB
	 */
	public void conectarBaseDeDatos() {
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			// objeto conexion inicializar la conexion
			conexion = (Connection) DriverManager.getConnection("jdbc:mariadb://localhost:3306/" + db, "root", "");
			if (conexion != null) {
				System.out.println("Conexión a base de datos " + db + " OK");
			} else {
				System.err.println("Conexión vacía");
			}
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Devuelve todos los usuarios sin fecha de baja
	 * 
	 * @return usuarios
	 */
	public ArrayList<Usuario> obtenerTodosUsuarios() {
		ArrayList<Usuario> array = new ArrayList<Usuario>();
		try {
			st = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery("SELECT * FROM users WHERE fecha_baja IS NULL");
			while (rs.next()) {
				array.add(new Usuario(rs.getString("email"), rs.getString("password"), rs.getString("token"),
						rs.getString("fecha_validez_token"), rs.getString("nombre"), rs.getString("apellidos"),
						rs.getString("telefono"), rs.getString("fecha_baja"), rs.getString("created_at"),
						rs.getString("updated_at")));
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * Devuelve todos los usuarios filtrando por parámetro de búsqueda
	 * 
	 * @param busqueda
	 * @return usuarios
	 */
	public ArrayList<Usuario> obtenerUsuariosWhere(String busqueda) {
		ArrayList<Usuario> array = new ArrayList<Usuario>();
		try {
			st = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery("SELECT * FROM users WHERE fecha_baja IS NULL AND (email LIKE '%" + busqueda
					+ "%' OR nombre LIKE '%" + busqueda + "%' OR apellidos LIKE '%" + busqueda
					+ "%' OR telefono LIKE '%" + busqueda + "%')");
			while (rs.next()) {
				array.add(new Usuario(rs.getString("email"), rs.getString("password"), rs.getString("token"),
						rs.getString("fecha_validez_token"), rs.getString("nombre"), rs.getString("apellidos"),
						rs.getString("telefono"), rs.getString("fecha_baja"), rs.getString("created_at"),
						rs.getString("updated_at")));
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * Desconecta
	 */
	public void desconectar() {
		try {
			if (st != null)
				st.close();
			if (rs != null)
				rs.close();
			if (conexion != null)
				conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int executeChanges(String query) {
		int res = 0;
		st = conexion.createStatement();
		try {
			res = st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * Devuelve todas las reservas sin fecha de baja
	 * 
	 * @return reservas
	 */
	public ArrayList<Reserva> obtenerTodasReservas() {
		ArrayList<Reserva> array = new ArrayList<Reserva>();
		try {
			st = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// SELECT * FROM reservas_habitaciones rh LEFT JOIN reservas r ON rh.reserva_id
			// = r.id
			rs = st.executeQuery("SELECT * FROM reservas WHERE fecha_baja IS NULL");
			while (rs.next()) {
				array.add(new Reserva(rs.getInt("id"), rs.getString("fecha"), rs.getString("fecha_entrada"),
						rs.getString("fecha_salida"), rs.getInt("numero_adultos"), rs.getInt("numero_ninyos"),
						rs.getString("user_id"), rs.getString("fecha_baja"), rs.getString("created_at"),
						rs.getString("updated_at")));
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * Devuelve todas las reservas con parámetro de búsqueda
	 * 
	 * @param busqueda
	 * @return reservas
	 */
	public ArrayList<Reserva> obtenerReservasWhere(String busqueda) {
		ArrayList<Reserva> array = new ArrayList<Reserva>();
		try {
			st = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery("SELECT * FROM reservas WHERE (user_id LIKE '%" + busqueda
					+ "%' OR fecha_entrada LIKE '%" + busqueda + "%' OR fecha_salida LIKE '%" + busqueda + "%')");
			while (rs.next()) {
				array.add(new Reserva(rs.getInt("id"), rs.getString("fecha"), rs.getString("fecha_entrada"),
						rs.getString("fecha_salida"), rs.getInt("numero_adultos"), rs.getInt("numero_ninyos"),
						rs.getString("user_id"), rs.getString("fecha_baja"), rs.getString("created_at"),
						rs.getString("updated_at")));
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * Devuelve todas las habitaciones sin fecha de baja
	 * 
	 * @return habitaciones
	 */
	public ArrayList<Habitacion> obtenerTodasHabitaciones() {
		ArrayList<Habitacion> array = new ArrayList<Habitacion>();
		try {
			st = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery("SELECT * FROM habitaciones WHERE fecha_baja IS NULL");
			while (rs.next()) {
				array.add(new Habitacion(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
						rs.getInt("cantidad"), rs.getDouble("precio"), rs.getInt("numero_maximo_personas"),
						rs.getInt("numero_camas"), rs.getString("fecha_baja"), rs.getString("created_at"),
						rs.getString("updated_at")));
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * Devuelve todas las habitaciones con filtro de búsqueda
	 * 
	 * @param busqueda
	 * @return habitaciones
	 */
	public ArrayList<Habitacion> obtenerHabitacionesWhere(String busqueda) {
		ArrayList<Habitacion> array = new ArrayList<Habitacion>();
		try {
			st = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery("SELECT * FROM habitaciones WHERE fecha_baja IS NULL AND (nombre LIKE '%" + busqueda
					+ "%' OR descripcion LIKE '%" + busqueda + "%')");
			while (rs.next()) {
				array.add(new Habitacion(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"),
						rs.getInt("cantidad"), rs.getDouble("precio"), rs.getInt("numero_maximo_personas"),
						rs.getInt("numero_camas"), rs.getString("fecha_baja"), rs.getString("created_at"),
						rs.getString("updated_at")));
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * Devuelve los registros de reserva_habitacion ligados a la reserva
	 * 
	 * @param id de reserva
	 * @return reserva_habitacion
	 */
	public ArrayList<Reserva_Habitacion> obtenerInfoReserva(int id) {
		ArrayList<Reserva_Habitacion> array = new ArrayList<Reserva_Habitacion>();
		try {
			st = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = st.executeQuery(
					"SELECT * FROM reservas_habitaciones rh LEFT JOIN reservas r ON rh.reserva_id = r.id WHERE r.id = "
							+ id);
			while (rs.next()) {
				array.add(new Reserva_Habitacion(rs.getInt("habitacion_id"), rs.getInt("reserva_id"),
						rs.getInt("cantidad"), rs.getDouble("precio")));
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * Buscar habitacion por nombre
	 * 
	 * @param busqueda
	 * @return
	 */
	public Habitacion habitacionWhere(String busqueda) {
		int id = 0;
		int cantidad = 0;
		int numero_maximo_personas = 0;
		int numero_camas = 0;
		double precio = 0.0;
		String descripcion = "";
		try {
			st = conexion.createStatement();
			rs = st.executeQuery("SELECT * FROM habitaciones WHERE nombre LIKE '%" + busqueda + "%' LIMIT 1");
			while (rs.next()) {
				id = rs.getInt("id");
				descripcion = rs.getString("descripcion");
				cantidad = rs.getInt("cantidad");
				precio = rs.getDouble("precio");
				numero_maximo_personas = rs.getInt("numero_maximo_personas");
				numero_camas = rs.getInt("numero_camas");
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return new Habitacion(id, busqueda, descripcion, cantidad, precio, numero_maximo_personas, numero_camas);
	}

	/**
	 * Buscar reserva por fecha
	 * 
	 * @param busqueda
	 * @return
	 */
	public int reservaWhere(String busqueda) {
		int id = 0;
		try {
			st = conexion.createStatement();
			rs = st.executeQuery("SELECT * FROM reservas WHERE fecha = '" + busqueda + "' LIMIT 1");
			while (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * Buscar usuario por email
	 * 
	 * @param email
	 * @return
	 */
	public boolean usuarioExiste(String email) {
		try {
			st = conexion.createStatement();
			rs = st.executeQuery("SELECT * FROM users WHERE email = '" + email + "' LIMIT 1");
			while (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.err.println("SQLException");
			e.printStackTrace();
		}
		return false;
	}
}
