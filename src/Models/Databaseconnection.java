package Models;

import java.sql.Connection;
import java.sql.DriverManager;

public class Databaseconnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/hotel_booking";
            String user = "root";
            String pass = "Vasanth2202@";
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println(e);;
            return null;
        }
    }
}
