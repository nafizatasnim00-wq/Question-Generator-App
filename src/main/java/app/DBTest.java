package app;

import utils.DBConnection;
import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
    try (Connection conn = DBConnection.getConnection()) {
        System.out.println("Database connected successfully!");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
