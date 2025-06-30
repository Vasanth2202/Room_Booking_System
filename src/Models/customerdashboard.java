package Models;

import java.util.Scanner;

public class customerdashboard {
    public static void customerdashboard(int customerId,String customerUsername) {
        Scanner input = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n Welcome to Customer Dashboard - " + customerUsername);
            System.out.println("-------------------------------------------------");
            System.out.println("1. Book a Room");
            System.out.println("2. View My Bookings");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Checkout");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                	customermanager.bookroom(customerId);
                    break;
                case 2:
                	customermanager.viewmybookings(customerId);
                    break;
                case 3:
                	customermanager.cancelbooking(customerId); 
                    break;
                case 4:
                    customermanager.checkoutroom(customerId);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;  
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
