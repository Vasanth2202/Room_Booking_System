package Models;

import java.util.Scanner;

public class Main {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("--Hotel Room Booking System--");
            System.out.println("*****************************");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            System.out.print("Choose your role: ");
            int choice = input.nextInt();

            switch (choice) {
                case 1:
                    handleadmin();
                    break;
                case 2:
                    handlecustomer();
                    break;
                case 3:
                    System.out.println("Thank you for using the system. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid role option. Try again.");
            }
        }
    }

    public static void handleadmin() {
        System.out.println("--- Admin Login ---");
        System.out.print("Enter username: ");
        String username = input.next();
        System.out.print("Enter password: ");
        String password = input.next();

        if (Loginsystem.validateadmin(username, password)) {
            System.out.println("Admin Login Successful!");
             Admindashboard.showdashboard(); 
        } else {
            System.out.println("Invalid admin username:.");
        }
    }
    
    public static void login() {
        System.out.println("--- Customer Login ---");
        System.out.print("Enter username: ");
        String username = input.next();
        System.out.print("Enter password: ");
        String password = input.next();

        int customerId = customermanager.logincustomer(username, password);

        if (customerId != -1) {
            System.out.println("Customer Login Successful!");
            customerdashboard.customerdashboard(customerId, username);
        } else {
            System.out.println("Invalid customer credentials.");
        }
    }


    public static void handlecustomer() {
    		System.out.println("\nWelcome to Customer page");
    		System.out.println("************************");
			System.out.println("1.Login");
			System.out.println("2.Register");
			System.out.println("Select your option:");
			int option=input.nextInt();
			
    	switch(option) {
    	case 1:
    		login();
            break;
    	case 2:
    		customermanager.registercustomer();
    	}
        
    }
}
