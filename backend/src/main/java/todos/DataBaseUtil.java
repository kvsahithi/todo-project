package todos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBaseUtil {
        private static final String URL = "jdbc:postgresql://localhost:5433/mydb";
        private static final String USER = "postgres";
        private static final String PASSWORD = "postgres";

        static {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new ExceptionInInitializerError("PostgreSQL JDBC Driver not found.");
            }
        }

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }



