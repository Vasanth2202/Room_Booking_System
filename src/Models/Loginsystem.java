package Models;

import java.sql.*;
import java.util.Scanner;

public class Loginsystem {

    public static boolean validateadmin(String username, String password) {
        try {
            Connection con = Databaseconnection.getConnection();
            String sql = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println(e);;
            return false;
        }
    }

 
    

}
