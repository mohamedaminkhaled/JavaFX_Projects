package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBinfo {
	public static Connection connDB() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection("jdbc:sqlite:D:\\CaffeMaster\\Database\\caffemaster_db.sqlite");
	}
}