package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL = "jdbc:sqlite:App.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }
}
