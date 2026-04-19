package boundary;

import models.*;
import services.*;
import java.util.List;
import java.util.Scanner;

public class CRMBoundary {
    private Scanner scanner = new Scanner(System.in);
    private AuthenticationService authService = new AuthenticationService();
    private TicketService ticketService = new TicketService();
    private DataStore dataStore = DataStore.getInstance();
    
    // Wire up the new sub-boundaries!
    private CustomerBoundary customerBoundary = new CustomerBoundary(scanner, authService, ticketService);
    private StaffBoundary staffBoundary = new StaffBoundary(scanner, authService, ticketService);
    private AdminBoundary adminBoundary = new AdminBoundary(scanner, authService, ticketService);
    
    public void startApp(){
        dataStore.loadUsersFromFile();
        dataStore.loadTicketsFromFile();
        dataStore.loadFAQsFromFile();
        dataStore.loadAuditLogsFromFile();
        boolean running = true;
        
        System.out.println("\n+--------------------------------------------------------+");
        System.out.println("|WELCOME TO ONLINE APPAREL SHOP CUSTOMER RELATION SYSTEM |");
        System.out.println("+--------------------------------------------------------+");
        
        while (running) {
            if (!authService.isLoggedIn()) {
                running = showMainMenu();
            } else {
                User currentUser = authService.getCurrentUser();
                if (currentUser instanceof Admin) {
                    adminBoundary.showAdminMenu((Admin) currentUser);
                } else if (currentUser instanceof Staff) {
                    staffBoundary.showStaffMenu((Staff) currentUser);
                } else if (currentUser instanceof Customer) {
                    customerBoundary.showCustomerMenu((Customer) currentUser);
                }
            }
        }
        
        System.out.println("\nThank you for using CRM System. Goodbye!");
        scanner.close();
    }

    private boolean showMainMenu() {
        System.out.println("\n+---------------------------------------+");
        System.out.println("|               MAIN MENU               |");
        System.out.println("+---------------------------------------+");
        System.out.printf("|  %-35s  |%n", "1. Login");
        System.out.printf("|  %-35s  |%n", "2. Register (Customer)");
        System.out.printf("|  %-35s  |%n", "3. View FAQs");
        System.out.printf("|  %-35s  |%n", "4. View Products");
        System.out.printf("|  %-35s  |%n", "0. Exit");
        System.out.println("+---------------------------------------+");
        System.out.print("Enter your choice: ");

        int choice = Utility.getIntInput();
        
        switch (choice) {
            case 1: handleLogin(); break;
            case 2: handleRegistration(); break;
            case 3: viewFAQs(); break;
            case 4: viewProducts(); break;
            case 0:
                dataStore.saveUsersToFile();
                dataStore.saveTicketsToFile();
                dataStore.saveFAQsToFile();
                dataStore.saveAuditLogsToFile();
                System.out.println("Logging out...");
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return true;
    }

    private void handleLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        User user = authService.login(email, password);
        if (user == null) {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    private void handleRegistration() {
        System.out.println("\n--- CUSTOMER REGISTRATION ---");
        System.out.print("Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();
        
        System.out.println("Checking---");

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required!");
            return;
        }
        if (!authService.isValid(email)) {
            System.out.println("Email format is invalid!");
            return;
        }
        if (!authService.isValidPassword(password)) {
            System.out.println("Password format is invalid! Should be at least 8 - 15 number or alphabet");
            return;
        }

        Customer customer = authService.registerCustomer(name, email, password, address);
        if (customer != null) {
            System.out.println("Registration successful! You can now login.");
            dataStore.saveUsersToFile(); 
        }
    }

    private void viewFAQs() {
        System.out.println("\n==========================================");
        System.out.println("        FREQUENTLY ASKED QUESTIONS        ");
        System.out.println("==========================================");
        List<FAQ> faqs = dataStore.getFaqs(); 
        if (faqs.isEmpty()) {
            System.out.println("No FAQs available.");
        } else {
            for (FAQ faq : faqs) {
                System.out.println(faq.getFaqDetailsString());
                System.out.println("-".repeat(40));
            }
        }
    }

    private void viewProducts() {
        System.out.println("\n==========================================");
        System.out.println("                OUR PRODUCTS              ");
        System.out.println("==========================================");
        for (Product product : dataStore.getProducts()) {
            System.out.println(product.getProductDetailsString());
            System.out.println("-".repeat(40));
        }
    }
}