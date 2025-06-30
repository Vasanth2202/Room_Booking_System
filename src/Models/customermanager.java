package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class customermanager {
	public static int logincustomer(String username, String password) {
	    try {
	        Connection con = Databaseconnection.getConnection();
	        String sql = "SELECT customer_id FROM customer WHERE username = ? AND password = ?";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setString(1, username);
	        ps.setString(2, password);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            int customerId = rs.getInt("customer_id");
	            rs.close();
	            ps.close();
	            con.close();
	            return customerId;
	        } else {
	            rs.close();
	            ps.close();
	            con.close();
	            return -1; 
	        }

	    } catch (Exception e) {
	        System.out.println("Login error: " + e.getMessage());
	        return -1;
	    }
	}

//	   public static boolean validatecustomer(String username, String password) {
//	        try {
//	            Connection con = Databaseconnection.getConnection();
//	            String sql = "SELECT * FROM customer WHERE username=? AND password=?";
//	            PreparedStatement ps = con.prepareStatement(sql);
//	            ps.setString(1, username);
//	            ps.setString(2, password);
//	            ResultSet rs = ps.executeQuery();
//	            return rs.next(); 
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return false;
//	        }
//	    }
	    public static void registercustomer() {
	        Scanner input = new Scanner(System.in);

	        System.out.println(" --- Customer Registration --- ");
	        System.out.print("Name: ");
	        String name = input.nextLine();

	        System.out.print("Email: ");
	        String email = input.next();

	        System.out.print("Phone: ");
	        String phone = input.next();

	        System.out.print("Username: ");
	        String username = input.next();

	        System.out.print("Password: ");
	        String password = input.next();

	        System.out.print("Confirm Password: ");
	        String confirmPassword = input.next();

	        if (!password.equals(confirmPassword)) {
	            System.out.println("Passwords do not match!");
	            return;
	        }

	        try {
	            Connection con = Databaseconnection.getConnection();

	            String checkSql = "SELECT COUNT(*) FROM customer WHERE username = ? OR email = ?";
	            PreparedStatement checkStmt = con.prepareStatement(checkSql);
	            checkStmt.setString(1, username);
	            checkStmt.setString(2, email);
	            ResultSet rs = checkStmt.executeQuery();
	            rs.next();
	            if (rs.getInt(1) > 0) {
	                System.out.println("Username or Email already exists.");
	                return;
	            }

	            String sql = "INSERT INTO customer (name, email, phone, username, password) VALUES (?, ?, ?, ?, ?)";
	            PreparedStatement ps = con.prepareStatement(sql);
	            ps.setString(1, name);
	            ps.setString(2, email);
	            ps.setString(3, phone);
	            ps.setString(4, username);
	            ps.setString(5, password);

	            ps.executeUpdate();
	            System.out.println("Registration successful!");

	            ps.close();
	            con.close();
	            Main.login();

	        } catch (Exception e) {
	            System.out.println("Error: " + e.getMessage());
	        }
	    }
	    public static void allusers() {
	        try {
	            Connection con = Databaseconnection.getConnection();
	            String sql = "SELECT * FROM customer ORDER BY customer_id";
	            PreparedStatement ps = con.prepareStatement(sql);
	            ResultSet rs = ps.executeQuery();

	            System.out.println("\n Registered Customers:");
	            System.out.println("-----------------------------------------------------------");
	            System.out.printf("%-5s %-20s %-25s %-15s\n", "ID", "Name", "Username", "Mobile");
	            System.out.println("-----------------------------------------------------------");

	            boolean found = false; 
	            while (rs.next()) {
	                int id = rs.getInt("customer_id");
	                String name = rs.getString("name");
	                String username = rs.getString("username");
	                String mobile = rs.getString("phone");

	                System.out.printf("%-5d %-20s %-25s %-15s\n", id, name, username, mobile);
	                found = true;
	            }

	            if (!found) {
	                System.out.println("No customers found.");
	            }

	            rs.close();
	            ps.close();
	            con.close();

	        } catch (Exception e) {
	            System.out.println("Error fetching customers: " + e.getMessage());
	        }
	    }
	    public static void bookroom(int customerId) {
	        Scanner input = new Scanner(System.in);

	        try  {
	        	Connection con = Databaseconnection.getConnection();
	            String sql = "SELECT * FROM rooms WHERE available = TRUE ORDER BY roomno";
	            PreparedStatement ps = con.prepareStatement(sql);
	            ResultSet rs = ps.executeQuery();
	            System.out.println("Available Rooms:");
	            System.out.println("-------------------------------------------------");
	            System.out.printf("%-10s %-15s %-10s\n", "Room No", "Room Type", "Price");
	            System.out.println("-------------------------------------------------");
	            boolean found = false;
	            while (rs.next()) {
	                System.out.printf("%-10d %-15s ₹%-10d\n",
	                        rs.getInt("roomno"),
	                        rs.getString("roomtype"),
	                        rs.getInt("price"));
	                found = true;
	            }

	            if (!found) {
	                System.out.println("No available rooms to book.");
	                return;
	            }
	            System.out.print("\nEnter Room No to book: ");
	            int roomno = input.nextInt();
	            System.out.print("Enter Check-in Date (DD-MM-YYYY): ");
	            String checkinStr = input.next();
	            LocalDate checkinDate = LocalDate.parse(checkinStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	            System.out.print("Confirm booking? (yes/no): ");
	            String confirm = input.next();
	            if (!confirm.equalsIgnoreCase("yes")) {
	                System.out.println("Booking cancelled.");
	                return;
	            }
	            String bookingSql = "INSERT INTO bookings (customerid, roomno, checkin, status) VALUES (?, ?, ?, 'Active')";
	            PreparedStatement bookStmt = con.prepareStatement(bookingSql);
	            bookStmt.setInt(1, customerId);
	            bookStmt.setInt(2, roomno);
	            bookStmt.setDate(3, java.sql.Date.valueOf(checkinDate));

	            int inserted = bookStmt.executeUpdate();
	            if (inserted > 0) {
	                String updateroomsql = "UPDATE rooms SET available = FALSE WHERE roomno = ?";
	                PreparedStatement updateStmt = con.prepareStatement(updateroomsql);
	                updateStmt.setInt(1, roomno);
	                updateStmt.executeUpdate();
	                System.out.println("Room booked successfully!");
	            } else {
	                System.out.println("Failed to book room.");
	            }
	            rs.close();
	            ps.close();
	            bookStmt.close();
	        } catch (DateTimeParseException e) {
	            System.out.println("Invalid date format. Please use DD-MM-YYYY.");
	        } catch (Exception e) {
	            System.out.println("Error during booking: " + e.getMessage());
	        }
	    }
	    public static void checkoutroom(int customerId) {
	        Scanner input = new Scanner(System.in);

	        try {
	        	Connection con = Databaseconnection.getConnection();
	            String sql = "SELECT b.booking_id, b.roomno, b.checkin, r.price " +
	                         "FROM bookings b JOIN rooms r ON b.roomno = r.roomno " +
	                         "WHERE b.customerid = ? AND b.status = 'Active'";
	            PreparedStatement ps = con.prepareStatement(sql);
	            ps.setInt(1, customerId);
	            ResultSet rs = ps.executeQuery();

	            System.out.println("\nYour Active Bookings:");
	            System.out.println("--------------------------------------------------");
	            System.out.printf("%-10s %-10s %-15s %-10s\n", "BookingID", "Room No", "Check-in", "Price");
	            System.out.println("--------------------------------------------------");

	            boolean found = false;
	            while (rs.next()) {
	                found = true;
	                System.out.printf("%-10d %-10d %-15s ₹%-10d\n",
	                        rs.getInt("booking_id"),
	                        rs.getInt("roomno"),
	                        rs.getDate("checkin"),
	                        rs.getInt("price"));
	            }

	            if (!found) {
	                System.out.println("No active bookings found.");
	                return;
	            }

	            System.out.print("Enter Booking ID to checkout: ");
	            int bookingId = input.nextInt();
	            String fetchSql = "SELECT b.checkin, r.price, b.roomno FROM bookings b " +
	                              "JOIN rooms r ON b.roomno = r.roomno " +
	                              "WHERE b.booking_id = ? AND b.customerid = ? AND b.status = 'Active'";
	            PreparedStatement fetchstmt = con.prepareStatement(fetchSql);
	            fetchstmt.setInt(1, bookingId);
	            fetchstmt.setInt(2, customerId);
	            ResultSet fetchrs = fetchstmt.executeQuery();

	            if (!fetchrs.next()) {
	                System.out.println("Invalid booking or already checked out.");
	                return;
	            }
	            LocalDate checkin = fetchrs.getDate("checkin").toLocalDate();
	            int pricePerDay = fetchrs.getInt("price");
	            int roomno = fetchrs.getInt("roomno");

	            LocalDate checkout = LocalDate.now();
	            long days = ChronoUnit.DAYS.between(checkin, checkout);         
	            if (days == 0) {
	            	days = 1;
	            	}

	            int totalCost = (int) (days * pricePerDay);

	            System.out.println("Checkout Date : " + checkout);
	            System.out.println("Days Stayed   : " + days);
	            System.out.println("Total Cost    : ₹" + totalCost);

	            System.out.print("Confirm checkout? (yes/no): ");
	            String confirm = input.next();

	            if (!confirm.equalsIgnoreCase("yes")) {
	                System.out.println("checkout cancelled.");
	                return;
	            }
	            String updatebooking = "UPDATE bookings SET checkout = ?, totalcost = ?, status = 'Completed' WHERE booking_id = ?";
	            PreparedStatement updatestmt = con.prepareStatement(updatebooking);
	            updatestmt.setDate(1, java.sql.Date.valueOf(checkout));
	            updatestmt.setInt(2, totalCost);
	            updatestmt.setInt(3, bookingId);
	            updatestmt.executeUpdate();
	            String updateroom = "UPDATE rooms SET available = TRUE WHERE roomno = ?";
	            PreparedStatement updateroomStmt = con.prepareStatement(updateroom);
	            updateroomStmt.setInt(1, roomno);
	            updateroomStmt.executeUpdate();
	            System.out.println("Checkout successful!");
	            fetchrs.close();
	            fetchstmt.close();
	            updatestmt.close();
	            updateroomStmt.close();
	        } catch (Exception e) {
	            System.out.println("Error during checkout: " + e.getMessage());
	        }
	    }
	    public static void cancelbooking(int customerId) {
	        Scanner input = new Scanner(System.in);

	        try {
	        	 Connection con = Databaseconnection.getConnection();
	            String sql = "SELECT b.booking_id, b.roomno, b.checkin, r.roomtype, r.price " +
	                         "FROM bookings b JOIN rooms r ON b.roomno = r.roomno " +
	                         "WHERE b.customerid = ? AND b.status = 'Active'";
	            PreparedStatement ps = con.prepareStatement(sql);
	            ps.setInt(1, customerId);
	            ResultSet rs = ps.executeQuery();
	            System.out.println("\nYour Active Bookings:");
	            System.out.println("------------------------------------------------------------");
	            System.out.printf("%-10s %-10s %-15s %-15s %-10s\n", "BookingID", "Room No", "Room Type", "Check-In", "Price");
	            System.out.println("------------------------------------------------------------");
	            boolean found = false;
	            while (rs.next()) {
	                found = true;
	                System.out.printf("%-10d %-10d %-15s %-15s ₹%-10d\n",
	                        rs.getInt("booking_id"),
	                        rs.getInt("roomno"),
	                        rs.getString("roomtype"),
	                        rs.getDate("checkin"),
	                        rs.getInt("price"));
	            }
	            if (!found) {
	                System.out.println("You have no active bookings to cancel.");
	                return;
	            }
	            System.out.print("\nEnter Booking ID to cancel: ");
	            int bookingId = input.nextInt();
	            String fetchSql = "SELECT roomno FROM bookings WHERE booking_id = ? AND customerid = ? AND status = 'Active'";
	            PreparedStatement fetchStmt = con.prepareStatement(fetchSql);
	            fetchStmt.setInt(1, bookingId);
	            fetchStmt.setInt(2, customerId);
	            ResultSet fetchRs = fetchStmt.executeQuery();
	            if (!fetchRs.next()) {
	                System.out.println("Invalid booking ID or booking is not active.");
	                return;
	            }
	            int roomno = fetchRs.getInt("roomno");
	            System.out.print("Are you sure you want to cancel this booking? (yes/no): ");
	            String confirm = input.next();

	            if (!confirm.equalsIgnoreCase("yes")) {
	                System.out.println("Cancellation aborted.");
	                return;
	            }
	            String cancelsql = "UPDATE bookings SET status = 'Cancelled' WHERE booking_id = ?";
	            PreparedStatement cancelStmt = con.prepareStatement(cancelsql);
	            cancelStmt.setInt(1, bookingId);
	            cancelStmt.executeUpdate();
	            String updateRoomSql = "UPDATE rooms SET available = TRUE WHERE roomno = ?";
	            PreparedStatement updateStmt = con.prepareStatement(updateRoomSql);
	            updateStmt.setInt(1, roomno);
	            updateStmt.executeUpdate();
	            System.out.println("Booking cancelled and room is now available.");
	            fetchRs.close();
	            fetchStmt.close();
	            cancelStmt.close();
	            updateStmt.close();
	        } catch (Exception e) {
	            System.out.println("Error during cancellation: " + e.getMessage());
	        }
	    }
	    public static void viewmybookings(int customerId) {
	        try  {
	        	Connection con = Databaseconnection.getConnection();
	            String sql = "SELECT b.booking_id, b.roomno, r.roomtype, b.checkin, b.status " +
	                         "FROM bookings b JOIN rooms r ON b.roomno = r.roomno " +
	                         "WHERE b.customerid = ? AND b.status = 'Active'";
	            PreparedStatement pst = con.prepareStatement(sql);
	            pst.setInt(1, customerId);
	            ResultSet rs = pst.executeQuery();

	            System.out.println("\nYour Active Bookings:");
	            System.out.println("-----------------------------------------------------------");
	            System.out.printf("%-10s %-10s %-15s %-15s %-10s\n",
	                    "BookingID", "Room No", "Room Type", "Check-In", "Status");
	            System.out.println("-----------------------------------------------------------");

	            boolean found = false;
	            while (rs.next()) {
	                found = true;
	                System.out.printf("%-10d %-10d %-15s %-15s %-10s\n",
	                        rs.getInt("booking_id"),
	                        rs.getInt("roomno"),
	                        rs.getString("roomtype"),
	                        rs.getDate("checkin"),
	                        rs.getString("status"));
	            }
	            if (!found) {
	                System.out.println("No active bookings found.");
	            }
	            rs.close();
	            pst.close();

	        } catch (Exception e) {
	            System.out.println("Error fetching bookings: " + e.getMessage());
	        }
	    }
	    
}
