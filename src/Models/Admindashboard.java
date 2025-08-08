package Models;

import java.util.Scanner;

public class Admindashboard {

    public static void showdashboard() {
        Scanner input = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. Add Room");
            System.out.println("2. View All Rooms");
            System.out.println("3. View All Users");
            System.out.println("4. Delete Room");
            System.out.println("5. All Bookings:");
            System.out.println("6. Add a admin");
//            System.out.println("7. Delete All Booking");
            System.out.println("7. Logout");
            System.out.print("Select your option: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    Roommanager.addroom();
                    break;
                case 2:
                	Roommanager.viewroom();
                    break;
                case 3:
                	customermanager.allusers();
                    break;
                case 4:
                	Roommanager.deleteroom();
                	break;
                case 5:
                	Bookingmanager.viewallbookings();
                	break;
                case 6:
                 Adminmanager.addadmin();
                	break;
//                case 7:
////                	Roommanager.deleteallrooms();
//                	break;
                case 7:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    }

   