package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Roommanager {
    static Scanner input = new Scanner(System.in);

    public static void addroom() {

        System.out.println("Add Room details:");
        System.out.println("*****************");
        System.out.print("Enter Room No: ");
        int roomno = input.nextInt();

        if (isroomexists(roomno)) {
            System.out.println(" Cannot add. Room number already exists.");
            return;
        }
        input.nextLine();
        System.out.print("Enter Room Type (A/C / Non A/C / Delux / Normal): ");
        String roomtype = input.nextLine();
        System.out.print("Enter Price: ");
        int price = input.nextInt();
        System.out.print("Is Available? (true/false): ");
        boolean available = input.nextBoolean();

        try {
            Connection con = Databaseconnection.getConnection();
            String sql = "INSERT INTO rooms (roomno, roomtype, price, available) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, roomno);
            ps.setString(2, roomtype);
            ps.setInt(3, price);
            ps.setBoolean(4, available);
            ps.executeUpdate();
            System.out.println("Room added successfully.");
            ps.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

  
    public static boolean isroomexists(int roomno) {
        try {
            Connection con = Databaseconnection.getConnection();
            String sql = "SELECT * FROM rooms WHERE roomno = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, roomno);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String roomType = rs.getString("roomtype");
                int price = rs.getInt("price");
                boolean available = rs.getBoolean("available");

                System.out.println("❗ Room already exists:");
                System.out.println("---------------------------");
                System.out.println("Room No   : " + roomno);
                System.out.println("Room Type : " + roomType);
                System.out.println("Price     : " + price);
                System.out.println("Available : " + (available ? "Yes" : "No"));
                System.out.println("---------------------------");
                rs.close();
                ps.close();
                con.close();
                return true;
            }
            rs.close();
            ps.close();
            con.close();
            return false;

        } catch (Exception e) {
            System.out.println("Error checking room: " + e.getMessage());
            return true;
        }
    }
    public static void viewroom() {
        try {
            Connection con = Databaseconnection.getConnection();
            String sql = "SELECT * FROM rooms ORDER BY roomno";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean found = false;

            System.out.println("\nRoom Details:");
            System.out.println("------------------------------------------------------------");
            System.out.printf("%-10s %-15s %-10s %-15s\n", "Room No", "Room Type", "Price", "Availability");
            System.out.println("------------------------------------------------------------");

            while (rs.next()) {
                int roomId = rs.getInt("roomno");
                String roomType = rs.getString("roomtype");
                int price = rs.getInt("price");
                boolean available = rs.getBoolean("available");
                String status = available ? "Available" : "Not Available";

                System.out.printf("%-10d %-15s %-10d %-15s\n", roomId, roomType, price, status);
                found = true;
            }

            if (!found) {
                System.out.println("No rooms available to display.");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Error fetching room details: " + e.getMessage());
        }
    }
    public static void deleteroom() {
    	Roommanager.viewroom();
        System.out.print("Enter Room No to delete: ");
        int roomno = input.nextInt();
        input.nextLine();

        if (!isroomexists(roomno)) {
            System.out.println("Room not found.");
            return;
        }

        System.out.print("Are you sure you want to delete this room? (yes/no): ");
        String confirm = input.nextLine();

        if (!confirm.equalsIgnoreCase("yes")) {
            System.out.println("❗ Deletion cancelled.");
            return;
        }

        try {
            Connection con = Databaseconnection.getConnection();
            String sql = "DELETE FROM rooms WHERE roomno = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, roomno);

            int deleted = ps.executeUpdate();
            if (deleted > 0)
                System.out.println("Room deleted successfully.");
            else
                System.out.println("Deletion failed.");

            ps.close();
            con.close();
            Roommanager.viewroom();
            
        } catch (Exception e) {
            System.out.println("Error deleting room: " + e.getMessage());
        }
    }

    public static void deleteallrooms() {
        Connection con = null;
        try {
            con = Databaseconnection.getConnection();
            String deleteBookings = "DELETE FROM bookings";
            PreparedStatement ps1 = con.prepareStatement(deleteBookings);
            int bookingsDeleted = ps1.executeUpdate();
            String deleteRooms = "DELETE FROM rooms";
            PreparedStatement ps2 = con.prepareStatement(deleteRooms);
            int roomsDeleted = ps2.executeUpdate();
            System.out.println("All rooms deleted successfully!");
//            System.out.println("" + roomsDeleted + " room(s) and " + bookingsDeleted + " booking(s) were removed.");
            ps1.close();
            ps2.close();
            con.close();

        } catch (Exception e) {
            System.out.println("Error deleting all rooms: " + e.getMessage());
        }
    }
}
