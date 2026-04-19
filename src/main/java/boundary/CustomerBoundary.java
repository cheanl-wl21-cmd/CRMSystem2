package boundary;

import enums.Priority;
import enums.TicketStatus;
import models.*;
import services.*;
import java.util.List;
import java.util.Scanner;

public class CustomerBoundary {
    private Scanner scanner;
    private AuthenticationService authService;
    private TicketService ticketService;
    private DataStore dataStore = DataStore.getInstance();

    public CustomerBoundary(Scanner scanner, AuthenticationService authService, TicketService ticketService) {
        this.scanner = scanner;
        this.authService = authService;
        this.ticketService = ticketService;
    }

    public void showCustomerMenu(Customer customer) {
        System.out.println("\n+------------------------------------------+");
        System.out.println("|            CUSTOMER DASHBOARD            |");
        System.out.printf("|  Welcome, %-30s |%n", customer.getName());
        System.out.println("+------------------------------------------+");
        System.out.printf("|  %-38s  |%n", "1. Submit New Ticket");
        System.out.printf("|  %-38s  |%n", "2. View My Tickets");
        System.out.printf("|  %-38s  |%n", "3. View Ticket Details");
        System.out.printf("|  %-38s  |%n", "4. Close Ticket");
        System.out.printf("|  %-38s  |%n", "5. Give Feedback");
        System.out.printf("|  %-38s  |%n", "6. Edit Profile");
        System.out.printf("|  %-38s  |%n", "7. View FAQs");
        System.out.printf("|  %-38s  |%n", "0. Logout");
        System.out.println("+------------------------------------------+");
        System.out.print("Enter your choice: ");

        int choice = Utility.getIntInput();

        switch (choice) {
            case 1: submitNewTicket(customer); break;
            case 2: viewCustomerTickets(customer); break;
            case 3: viewTicketDetails(customer); break;
            case 4: closeTicket(customer); break;
            case 5: giveFeedback(customer); break;
            case 6: editProfile(customer); break;
            case 7: viewFAQs(); break;
            case 0: authService.logout(); break;
            default: System.out.println("Invalid choice.");
        }
        if (choice != 0){
            Utility.pauseAndClear();
        }
    }

    private void submitNewTicket(Customer customer) {
        System.out.println("\n--- SUBMIT NEW TICKET ---");
        System.out.println("\nAvailable Products:");
        List<Product> products = dataStore.getProducts();
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i).getName() + " (" + products.get(i).getProductId() + ")");
        }
        
        System.out.print("\nSelect product number: ");
        int productChoice = Utility.getIntInput();
        if (productChoice < 1 || productChoice > products.size()) {
            System.out.println("Invalid product selection.");
            return;
        }
        String productId = products.get(productChoice - 1).getProductId();

        System.out.print("Description of issue: ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        Ticket ticket = ticketService.createTicket(customer, productId, description, Priority.LOW);

        System.out.print("\nDo you want to add an attachment? (y/n): ");
        String attachChoice = scanner.nextLine().trim().toLowerCase();
        if (attachChoice.equals("y")) {
            System.out.println("Please place the file inside your main project folder.");
            System.out.print("Enter the exact file name (e.g., error.txt or screenshot.png): ");
            String fileName = scanner.nextLine().trim();
            java.io.File file = new java.io.File(fileName);
            
            if (file.exists() && !file.isDirectory()) {
                double fileSizeMB = file.length() / (1024.0 * 1024.0);
                fileSizeMB = Math.round(fileSizeMB * 100.0) / 100.0;
                if (fileSizeMB == 0.0) fileSizeMB = 0.01; 
                Attachment attachment = new Attachment("ATT-" + System.currentTimeMillis(), fileName, fileSizeMB);
                ticket.addAttachment(attachment); 
                System.out.println("Success! File '" + fileName + "' attached perfectly! (Size: " + fileSizeMB + " MB)");
            } else {
                System.out.println("Error: File not found! Proceeding without attachment.");
            }
        }
        System.out.println("\nTicket submitted successfully! Your ticket ID: " + ticket.getTicketId());
        dataStore.saveTicketsToFile();
    }

    private void viewCustomerTickets(Customer customer) {
        System.out.println("\n--- MY TICKETS ---");
        List<Ticket> tickets = ticketService.getTicketsByCustomerId(customer.getCustId());
        if (tickets.isEmpty()) {
            System.out.println("You have no tickets.");
            return;
        }
        System.out.println("-".repeat(80));
        for (Ticket ticket : tickets) {
            System.out.println(ticket.getDisplayInfo());
        }
        System.out.println("-".repeat(80));
        System.out.println("Total: " + tickets.size() + " ticket(s)");
    }

    private void viewTicketDetails(Customer customer) {
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim();
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null || !ticket.getCustomerId().equals(customer.getCustId())) {
            System.out.println("Ticket not found or not yours.");
            return;
        }
        System.out.println(ticket.getTicketDetails());
    }

    private void closeTicket(Customer customer) {
        System.out.print("\nEnter Ticket ID to close: ");
        String ticketId = scanner.nextLine().trim().toUpperCase(); 
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null || !ticket.getCustomerId().equals(customer.getCustId())) {
            System.out.println("Ticket not found or not yours.");
            return;
        }
        System.out.println(customer.closeTicket(ticketId));
        dataStore.saveTicketsToFile(); 
    }

    private void giveFeedback(Customer customer) {
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim().toUpperCase(); 
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null || !ticket.getCustomerId().equals(customer.getCustId())) {
            System.out.println("Ticket not found or not yours.");
            return;
        }
        if (ticket.getStatus() != TicketStatus.CLOSED) {
            System.out.println("You can only give feedback for closed tickets.");
            return;
        }
        if (ticket.getFeedback() != null) {
            System.out.println("Feedback already submitted for this ticket.");
            return;
        }
        System.out.print("Rating (1-5): ");
        int rating = Utility.getIntInput();
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return;
        }
        System.out.print("Comment: ");
        String comment = scanner.nextLine().trim();
        System.out.println(customer.giveFeedback(ticketId, rating, comment));
        dataStore.saveTicketsToFile(); 
    }

    private void editProfile(Customer customer) {
        System.out.println("\n--- EDIT PROFILE ---");
        System.out.println("Current Name: " + customer.getName());
        System.out.println("Current Email: " + customer.getEmail());
        System.out.println("Current Address: " + customer.getAddress());
        
        System.out.print("\nNew Name (press Enter to keep current): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) customer.setName(name);

        System.out.print("New Email (press Enter to keep current): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) {
            if (authService.isValid(email)) {
                customer.setEmail(email);
            } else {
                System.out.println("--> Invalid email format. Keeping old email.");
            }
        }

        System.out.print("New Address (press Enter to keep current): ");
        String address = scanner.nextLine().trim();
        if (!address.isEmpty()) customer.setAddress(address);

        System.out.print("\nDo you want to change your password? (y/n): ");
        String changePass = scanner.nextLine().trim().toLowerCase();
        if (changePass.equals("y")) {
            System.out.print("Enter CURRENT password: ");
            String currentPass = scanner.nextLine().trim();
            if (customer.validatePassword(currentPass)) {
                System.out.print("Enter NEW password: ");
                String newPass = scanner.nextLine().trim();
                if (authService.isValidPassword(newPass)) {
                    customer.setPassword(newPass);
                    System.out.println("--> Password successfully changed!");
                } else {
                    System.out.println("--> Password must be 8-15 characters. Password not changed.");
                }
            } else {
                System.out.println("--> Incorrect current password! Password not changed.");
            }
        }

        System.out.print("\nDo you want to update your profile picture? (y/n): ");
        String changePic = scanner.nextLine().trim().toLowerCase();
        if (changePic.equals("y")) {
            System.out.println("Please place the image in your main project folder.");
            System.out.print("Enter the exact file name (e.g., avatar.png): ");
            String fileName = scanner.nextLine().trim();
            java.io.File file = new java.io.File(fileName);
            if (file.exists() && !file.isDirectory()) {
                String lowerCaseName = fileName.toLowerCase();
                if (lowerCaseName.endsWith(".png") || lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg")) {
                    customer.setProfilePic(fileName);
                    System.out.println("--> Success! Profile picture updated.");
                } else {
                    System.out.println("--> Error: Invalid format!");
                }
            } else {
                System.out.println("--> Error: File not found!");
            }
        }
        System.out.println("\nProfile updated successfully!");
        dataStore.saveUsersToFile(); 
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
}