package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBinfo {
	
	private static String username = "root";
	private static String password = "";
	private static String connectin_link = "jdbc:mysql://localhost/atmdb";
	
	public static Connection DBcon() throws SQLException {
		return DriverManager.getConnection(connectin_link, username, password);
		
	}
	
	public static void errException(SQLException e) {
		System.out.println("Error: "+e.getMessage());
		System.out.println("code: "+e.getErrorCode());
		System.out.println("state: "+e.getSQLState());
		System.out.println("message: "+e.getLocalizedMessage());
		
	}

}
