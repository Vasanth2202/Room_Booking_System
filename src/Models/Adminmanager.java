package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Adminmanager {
    static Scanner input = new Scanner(System.in);

    public static void addadmin() {
        try {
            System.out.println("\n Add New Admin");
            System.out.println("-------------------");

            System.out.print("Enter Username: ");
            String username = input.next();

            System.out.print("Enter Password: ");
            String password = input.next();

            System.out.print("Confirm Password: ");
            String confirmPassword = input.next();

            
            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
                return;
            }

            Connection con = Databaseconnection.getConnection();

           
            String checkSql = "SELECT COUNT(*) FROM admin WHERE username = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Username already exists. Choose another.");
                rs.close();
                checkStmt.close();
                con.close();
                return;
            }

           
            String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ps.executeUpdate();
            System.out.println("New admin added successfully.");

            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Error adding admin: " + e.getMessage());
        }
    }
   
	


}
