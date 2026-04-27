package f;

// import javafx.fxml.FXMLLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static String DB_URL = "jdbc:mariadb://localhost:3306/u25176502_chinook";
    private static String DB_USER = "root";
    private static String DB_PASS = "EveningMorning23/7";

// 2. The Connection Method
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
