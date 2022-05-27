package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBinfo {
	
	private static String username="makka";
	private static String password="makkacars";
	private static String con_string="jdbc:mysql://localhost/makka_agency?useUnicode=yes&characterEncoding=UTF-8";
	
	public static Connection connDB() throws SQLException {
		return DriverManager.getConnection(con_string, username, password);
	}
}
