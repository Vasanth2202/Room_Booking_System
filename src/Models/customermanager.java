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
	    String name, email, phone, username, password, confirmPassword;

	    System.out.println(" --- Customer Registration --- ");

	    System.out.print("Name: ");
	    name = input.nextLine();

	    // Email 
	    while (true) {
	        System.out.print("Email: ");
	        email = input.next();
	        if (email.contains("@") && email.contains(".")) break;
	        System.out.println("Invalid email. Must contain '@' and a domain like '.com'. Try again.");
	    }

	    // Phone number 
	    while (true) {
	        System.out.print("Phone (10 digits): ");
	        phone = input.next();
	        if (phone.matches("\\d{10}")) break;
	        System.out.println("Invalid phone number. Must be exactly 10 digits. Try again.");
	    }

	    // Username
	    while (true) {
	        System.out.print("Username: ");
	        username = input.next();

	        try {
	            Connection con = Databaseconnection.getConnection();
	            String checkSql = "SELECT COUNT(*) FROM customer WHERE username = ?";
	            PreparedStatement checkStmt = con.prepareStatement(checkSql);
	            checkStmt.setString(1, username);
	            ResultSet rs = checkStmt.executeQuery();
	            rs.next();
	            if (rs.getInt(1) > 0) {
	                System.out.println("Username already taken. Try a different one.");
	                continue;
	            }
	            checkStmt.close();
	            con.close();
	            break;
	        } catch (Exception e) {
	            System.out.println("Error checking username: " + e.getMessage());
	            return;
	        }
	    }

	    // Password 
	    while (true) {
	        System.out.print("Password: ");
	        password = input.next();

	        System.out.print("Confirm Password: ");
	        confirmPassword = input.next();

	        if (!password.equals(confirmPassword)) {
	            System.out.println("Passwords do not match. Try again.");
	            continue;
	        }

	        if (password.length() < 5 || 
	            !password.matches(".*\\d.*") || 
	            !password.matches(".*[!@#$%^&*()].*")) {
	            System.out.println("Password must be at least 5 characters, include a digit and a special character (!@#$%^&*()).");
	            continue;
	        }

	        break; 
	    }

	    try {
	        Connection con = Databaseconnection.getConnection();
	        String checkEmail = "SELECT COUNT(*) FROM customer WHERE email = ?";
	        PreparedStatement checkStmt = con.prepareStatement(checkEmail);
	        checkStmt.setString(1, email);
	        ResultSet rs = checkStmt.executeQuery();
	        rs.next();
	        if (rs.getInt(1) > 0) {
	            System.out.println("Email already registered. Try logging in.");
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

	        try {
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

	            
	            LocalDate checkinDate = LocalDate.now();
	            System.out.println("Check-in Date: " + checkinDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

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

	                System.out.println("Room booked successfully.");
	            } else {
	                System.out.println("Failed to book room.");
	            }
	            rs.close();
	            ps.close();
	            bookStmt.close();
	            con.close();
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
	                rs.close();
	                ps.close();
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
	                rs.close(); ps.close(); fetchrs.close(); fetchstmt.close();
	                return;
	            }

	            LocalDate checkin = fetchrs.getDate("checkin").toLocalDate();
	            int pricePerDay = fetchrs.getInt("price");
	            int roomno = fetchrs.getInt("roomno");

	            
	            LocalDate checkout = LocalDate.now();
	            long days = ChronoUnit.DAYS.between(checkin, checkout);
	            if (days == 0) days = 1;

	            int totalCost = (int) (days * pricePerDay);

	            System.out.println("Checkout Date : " + checkout);
	            System.out.println("Days Stayed   : " + days);
	            System.out.println("Total Cost    : ₹" + totalCost);

	          
	            System.out.print("Confirm checkout? (yes/no): ");
	            String confirm = input.next();

	            if (!confirm.equalsIgnoreCase("yes")) {
	                System.out.println("Checkout cancelled.");
	                rs.close(); ps.close(); fetchrs.close(); fetchstmt.close();
	                return;
	            }

	            String updateBooking = "UPDATE bookings SET checkout = ?, totalcost = ?, status = 'Checked Out' WHERE booking_id = ?";
	            PreparedStatement updateStmt = con.prepareStatement(updateBooking);
	            updateStmt.setDate(1, java.sql.Date.valueOf(checkout));
	            updateStmt.setInt(2, totalCost);
	            updateStmt.setInt(3, bookingId);
	            updateStmt.executeUpdate();
	            String updateRoom = "UPDATE rooms SET available = TRUE WHERE roomno = ?";
	            PreparedStatement updateRoomStmt = con.prepareStatement(updateRoom);
	            updateRoomStmt.setInt(1, roomno);
	            updateRoomStmt.executeUpdate();

	            System.out.println("Checkout successful!");
	            rs.close();
	            ps.close();
	            fetchrs.close();
	            fetchstmt.close();
	            updateStmt.close();
	            updateRoomStmt.close();

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
