package Connection;

import java.sql.*;
import java.util.logging.Logger;

public class ConnectionFactory {
    private static final Logger log = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // corect pt versiuni recente
    private static final String DBURL = "jdbc:mysql://localhost:3306/orderdb";
    private static final String USER = "root";
    private static final String PASSWORD = "Soarecimici3!";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DBURL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error getting DB connection", e);
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
    }

    public static void close(Statement stmt) {
        if (stmt != null) try { stmt.close(); } catch (SQLException ignored) {}
    }

    public static void close(Connection conn) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
    }
}
