package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBinfo {
	
	private static String username="admin";
	private static String password="mohamed@admin2020";
	private static String con_string="jdbc:mysql://localhost/pharmaster_db?useUnicode=yes&characterEncoding=UTF-8";
	
	public static Connection connDB() throws SQLException {
		return DriverManager.getConnection(con_string, username, password);
	}
}
