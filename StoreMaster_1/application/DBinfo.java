package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBinfo {
	public static Connection connDB() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		return DriverManager.getConnection("jdbc:sqlite:D:\\StoreMaster\\Database\\storemaster_db_2.sqlite");
	}
}
