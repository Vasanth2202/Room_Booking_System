package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Bookingmanager {

	public static void viewallbookings() {
        String sql = "SELECT b.booking_id, c.name AS customer_name, r.roomno, r.roomtype, " +
                     "b.checkin, b.checkout, b.status, b.price, b.totalcost " +
                     "FROM bookings b " +
                     "JOIN customer c ON b.customerid = c.customer_id " +
                     "JOIN rooms r ON b.roomno = r.roomno " +
                     "ORDER BY b.booking_id";

        try {
        	Connection con = Databaseconnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n All Bookings:");
            System.out.println("-----------------------------------------------------------------------------------------------------");
            System.out.printf("%-10s %-15s %-8s %-12s %-12s %-12s %-10s %-10s %-10s\n",
                    "Booking ID", "Customer", "Room No", "Room Type", "Check-in", "Check-out", "Status", "Price", "Total");
            System.out.println("-----------------------------------------------------------------------------------------------------");

            boolean found = false;

            while (rs.next()) {
                found = true;
                int bookingid = rs.getInt("booking_id");
                String customername = rs.getString("customer_name");
                int roomno = rs.getInt("roomno");
                String roomtype = rs.getString("roomtype");
                String checkin = rs.getString("checkin");
                String checkout = rs.getString("checkout");
                String status = rs.getString("status");
                int price = rs.getInt("price");
                int total = rs.getInt("totalcost");

                System.out.printf("%-10d %-15s %-8d %-12s %-12s %-12s %-10s ₹%-9d ₹%-9d\n",
                        bookingid, customername, roomno, roomtype,
                        checkin, (checkout == null ? "-" : checkout),
                        status, price, total);
            }

            if (!found) {
                System.out.println("No bookings found.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
	
}

