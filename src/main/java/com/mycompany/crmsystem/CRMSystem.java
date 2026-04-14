/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.crmsystem;
import enums.Priority;
import enums.StaffRole;
import enums.TicketStatus;
import models.*;
import services.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CRMSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static AuthenticationService authService = new AuthenticationService();
    private static TicketService ticketService = new TicketService();
    private static ReportService reportService = new ReportService();
    private static DataStore dataStore = DataStore.getInstance();

    public static void main(String[] args) {
        System.out.println("\n+--------------------------------------------------------+");
    System.out.println("|WELCOME TO ONLINE APPAREL SHOP CUSTOMER RELATION SYSTEM |");
    System.out.println("+--------------------------------------------------------+");
        
        boolean running = true;
        while (running) {
            if (!authService.isLoggedIn()) {
                running = showMainMenu();
            } else {
                User currentUser = authService.getCurrentUser();
                if (currentUser instanceof Admin) {
                    showAdminMenu((Admin) currentUser);
                } else if (currentUser instanceof Staff) {
                    showStaffMenu((Staff) currentUser);
                } else if (currentUser instanceof Customer) {
                    showCustomerMenu((Customer) currentUser);
                }
            }
        }
        
        System.out.println("\nThank you for using CRM System. Goodbye!");
        scanner.close();
    }

    private static boolean showMainMenu() {
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

        int choice = getIntInput();
        
        switch (choice) {
            case 1:
                handleLogin();
                break;
            case 2:
                handleRegistration();
                break;
            case 3:
                viewFAQs();
                break;
            case 4:
                viewProducts();
                break;
            case 0:
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return true;
    }

    private static void handleLogin() {
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

    private static void handleRegistration() {
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
        }
    }

    private static void viewFAQs() {
        System.out.println("\n==========================================");
System.out.println("        FREQUENTLY ASKED QUESTIONS        ");
System.out.println("==========================================");
        
        List<FAQ> faqs = FAQ.getAllFAQs();
        if (faqs.isEmpty()) {
            System.out.println("No FAQs available.");
        } else {
            for (FAQ faq : faqs) {
                faq.displayFAQ();
                System.out.println("-".repeat(40));
            }
        }
    }

    private static void viewProducts() {
        System.out.println("\n==========================================");
System.out.println("        OUR PRODUCTS        ");
System.out.println("==========================================");
        
        for (Product product : dataStore.getProducts()) {
            product.displayProductInfo();
            System.out.println("-".repeat(40));
        }
    }

    // ==================== CUSTOMER MENU ====================
    private static void showCustomerMenu(Customer customer) {
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

        int choice = getIntInput();

        switch (choice) {
            case 1:
                submitNewTicket(customer);
                break;
            case 2:
                viewCustomerTickets(customer);
                break;
            case 3:
                viewTicketDetails(customer);
                break;
            case 4:
                closeTicket(customer);
                break;
            case 5:
                giveFeedback(customer);
                break;
            case 6:
                editProfile(customer);
                break;
            case 7:
                viewFAQs();
                break;
            case 0:
                authService.logout();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void submitNewTicket(Customer customer) {
        System.out.println("\n--- SUBMIT NEW TICKET ---");
        
        // Show products
        System.out.println("\nAvailable Products:");
        List<Product> products = dataStore.getProducts();
        for (int i = 0; i < products.size(); i++) {
            System.out.println((i + 1) + ". " + products.get(i).getName() + " (" + products.get(i).getProductId() + ")");
        }
        
        System.out.print("\nSelect product number: ");
        int productChoice = getIntInput();
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

        System.out.println("\nPriority Level:");
        System.out.println("1. Low");
        System.out.println("2. Medium");
        System.out.println("3. High");
        System.out.print("Select priority: ");
        int priorityChoice = getIntInput();
        
        Priority priority;
        switch (priorityChoice) {
            case 1: priority = Priority.LOW; break;
            case 2: priority = Priority.MEDIUM; break;
            case 3: priority = Priority.HIGH; break;
            default:
                System.out.println("Invalid priority. Defaulting to LOW.");
                priority = Priority.LOW;
        }

        Ticket ticket = ticketService.createTicket(customer, productId, description, priority);
        System.out.println("\nTicket submitted successfully!");
        System.out.println("Your ticket ID: " + ticket.getTicketId());
    }

    private static void viewCustomerTickets(Customer customer) {
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

    private static void viewTicketDetails(Customer customer) {
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim();
        
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }
        
        if (!ticket.getCustomerId().equals(customer.getCustId())) {
            System.out.println("You can only view your own tickets.");
            return;
        }
        
        ticket.displayTicketDetails();
    }

    private static void closeTicket(Customer customer) {
        System.out.print("\nEnter Ticket ID to close: ");
        String ticketId = scanner.nextLine().trim();
        
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }
        
        if (!ticket.getCustomerId().equals(customer.getCustId())) {
            System.out.println("You can only close your own tickets.");
            return;
        }

        customer.closeTicket(ticketId);
    }

    private static void giveFeedback(Customer customer) {
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim();
        
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
        int rating = getIntInput();
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return;
        }

        System.out.print("Comment: ");
        String comment = scanner.nextLine().trim();

        customer.giveFeedback(ticketId, rating, comment);
    }

    private static void editProfile(Customer customer) {
        System.out.println("\n--- EDIT PROFILE ---");
        System.out.println("Current Name: " + customer.getName());
        System.out.println("Current Email: " + customer.getEmail());
        System.out.println("Current Address: " + customer.getAddress());
        
        System.out.print("\nNew Name (press Enter to keep current): ");
        String name = scanner.nextLine().trim();
        System.out.print("New Email (press Enter to keep current): ");
        String email = scanner.nextLine().trim();
        System.out.print("New Address (press Enter to keep current): ");
        String address = scanner.nextLine().trim();

        if (!name.isEmpty()) customer.setName(name);
        if (!email.isEmpty()) customer.setEmail(email);
        if (!address.isEmpty()) customer.setAddress(address);

        System.out.println("Profile updated successfully!");
    }

    //staff menu
    private static void showStaffMenu(Staff staff) {
        System.out.println("\n+------------------------------------------+");
        System.out.println("|             STAFF DASHBOARD              |");
        System.out.printf("|  Welcome, %-30s |%n", staff.getName());
        System.out.printf("|  Role: %-33s |%n", staff.getStaffRole().getDisplayName());
        System.out.println("+------------------------------------------+");
        System.out.printf("|  %-38s  |%n", "1. View All Tickets");
        System.out.printf("|  %-38s  |%n", "2. View My Assigned Tickets");
        System.out.printf("|  %-38s  |%n", "3. Search Tickets");
        System.out.printf("|  %-38s  |%n", "4. Filter by Status");
        System.out.printf("|  %-38s  |%n", "5. Filter by Priority");
        System.out.printf("|  %-38s  |%n", "6. View Ticket Details");
        System.out.printf("|  %-38s  |%n", "7. Add Response to Ticket");
        System.out.printf("|  %-38s  |%n", "8. Update Ticket Status");
        System.out.printf("|  %-38s  |%n", "9. Take/Assign Ticket");

        if (staff.getStaffRole() == StaffRole.MANAGER || staff.getStaffRole() == StaffRole.TEAM_LEAD) {
        System.out.printf("|  %-38s  |%n", "10. Assign Ticket to Staff");
}

        System.out.printf("|  %-38s  |%n", "0. Logout");
        System.out.println("+------------------------------------------+");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                staff.viewAllTickets(ticketService.getAllTickets());
                break;
            case 2:
                viewAssignedTickets(staff);
                break;
            case 3:
                searchTickets(staff);
                break;
            case 4:
                filterByStatus(staff);
                break;
            case 5:
                filterByPriority(staff);
                break;
            case 6:
                viewTicketDetailsStaff();
                break;
            case 7:
                addResponseToTicket(staff);
                break;
            case 8:
                updateTicketStatus(staff);
                break;
            case 9:
                takeTicket(staff);
                break;
            case 10:
                if (staff.getStaffRole() == StaffRole.MANAGER || staff.getStaffRole() == StaffRole.TEAM_LEAD) {
                    assignTicketToStaff(staff);
                } else {
                    System.out.println("Invalid choice.");
                }
                break;
            case 0:
                authService.logout();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void viewAssignedTickets(Staff staff) {
        System.out.println("\n--- MY ASSIGNED TICKETS ---");
        List<Ticket> tickets = staff.getAssignedTickets();
        
        if (tickets.isEmpty()) {
            System.out.println("No tickets assigned to you.");
            return;
        }

        for (Ticket ticket : tickets) {
            System.out.println(ticket.getDisplayInfo());
        }
    }

    private static void searchTickets(Staff staff) {
        System.out.print("\nEnter search keyword: ");
        String keyword = scanner.nextLine().trim();
        staff.searchTicketByKeyword(ticketService.getAllTickets(), keyword);
    }

    private static void filterByStatus(Staff staff) {
        System.out.println("\nSelect Status:");
        System.out.println("1. Open");
        System.out.println("2. In Progress");
        System.out.println("3. Pending");
        System.out.println("4. Resolved");
        System.out.println("5. Closed");
        System.out.print("Choice: ");
        
        int choice = getIntInput();
        TicketStatus status;
        switch (choice) {
            case 1: status = TicketStatus.OPEN; break;
            case 2: status = TicketStatus.IN_PROGRESS; break;
            case 3: status = TicketStatus.PENDING; break;
            case 4: status = TicketStatus.RESOLVED; break;
            case 5: status = TicketStatus.CLOSED; break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        staff.filterTicketsByStatus(ticketService.getAllTickets(), status);
    }

    private static void filterByPriority(Staff staff) {
        System.out.println("\nSelect Priority:");
        System.out.println("1. Low");
        System.out.println("2. Medium");
        System.out.println("3. High");
        System.out.print("Choice: ");
        
        int choice = getIntInput();
        Priority priority;
        switch (choice) {
            case 1: priority = Priority.LOW; break;
            case 2: priority = Priority.MEDIUM; break;
            case 3: priority = Priority.HIGH; break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        staff.filterTicketsByPriority(ticketService.getAllTickets(), priority);
    }

    private static void viewTicketDetailsStaff() {
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim();
        
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }
        ticket.displayTicketDetails();
    }

    private static void addResponseToTicket(Staff staff) {
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim();
        
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        System.out.print("Enter your response: ");
        String response = scanner.nextLine().trim();
        if (response.isEmpty()) {
            System.out.println("Response cannot be empty.");
            return;
        }

        staff.addResponse(ticket, response);
    }

    private static void updateTicketStatus(Staff staff) {
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim();
        
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        System.out.println("\nSelect New Status:");
        System.out.println("1. Open");
        System.out.println("2. In Progress");
        System.out.println("3. Pending");
        System.out.println("4. Resolved");
        System.out.print("Choice: ");
        
        int choice = getIntInput();
        TicketStatus status;
        switch (choice) {
            case 1: status = TicketStatus.OPEN; break;
            case 2: status = TicketStatus.IN_PROGRESS; break;
            case 3: status = TicketStatus.PENDING; break;
            case 4: status = TicketStatus.RESOLVED; break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        staff.updateTicketStatus(ticket, status);
    }

    private static void takeTicket(Staff staff) {
        System.out.print("\nEnter Ticket ID to take: ");
        String ticketId = scanner.nextLine().trim();
        
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        staff.takeTicket(ticket);
    }

    private static void assignTicketToStaff(Staff manager) {
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim();
        
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        // Show available staff
        System.out.println("\nAvailable Staff:");
        List<User> users = dataStore.getUsers();
        int count = 1;
        for (User user : users) {
            if (user instanceof Staff) {
                Staff s = (Staff) user;
                System.out.println(count + ". " + s.getName() + " (" + s.getStaffId() + ") - " + s.getStaffRole().getDisplayName());
                count++;
            }
        }

        System.out.print("Enter Staff ID: ");
        String staffId = scanner.nextLine().trim();
        
        Staff targetStaff = dataStore.findStaffById(staffId);
        if (targetStaff == null) {
            System.out.println("Staff not found.");
            return;
        }

        manager.assignTicket(ticket, targetStaff);
    }

    // ==================== ADMIN MENU ====================
    private static void showAdminMenu(Admin admin) {
        System.out.println("\n+------------------------------------------+");
        System.out.println("|             ADMIN DASHBOARD              |");
        System.out.printf("|  Welcome, %-30s |%n", admin.getName());
        System.out.println("+------------------------------------------+");
        System.out.printf("|  %-38s  |%n", "1. Generate Monthly Report");
        System.out.printf("|  %-38s  |%n", "2. View All Tickets");
        System.out.printf("|  %-38s  |%n", "3. View All Users");
        System.out.printf("|  %-38s  |%n", "4. View Audit Logs");
        System.out.printf("|  %-38s  |%n", "5. Add New Staff");
        System.out.printf("|  %-38s  |%n", "6. Manage FAQs");
        System.out.printf("|  %-38s  |%n", "7. View Departments");
        System.out.printf("|  %-38s  |%n", "0. Logout");
        System.out.println("+------------------------------------------+");
        System.out.print("Enter your choice: ");

        int choice = getIntInput();

        switch (choice) {
            case 1:
                generateReport(admin);
                break;
            case 2:
                viewAllTicketsAdmin();
                break;
            case 3:
                viewAllUsers();
                break;
            case 4:
                admin.viewAuditLogs();
                break;
            case 5:
                addNewStaff(admin);
                break;
            case 6:
                manageFAQs(admin);
                break;
            case 7:
                viewDepartments();
                break;
            case 0:
                authService.logout();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void generateReport(Admin admin) {
        System.out.print("\nEnter month (1-12): ");
        int month = getIntInput();
        if (month < 1 || month > 12) {
            System.out.println("Invalid month.");
            return;
        }

        System.out.print("Enter year: ");
        int year = getIntInput();
        if (year < 2000 || year > 2100) {
            System.out.println("Invalid year.");
            return;
        }

        Report report = admin.generateMonthlyReport(month, year, ticketService.getAllTickets());
        report.displayReport();
    }

    private static void viewAllTicketsAdmin() {
        System.out.println("\n--- ALL TICKETS ---");
        List<Ticket> tickets = ticketService.getAllTickets();
        
        if (tickets.isEmpty()) {
            System.out.println("No tickets in the system.");
            return;
        }

        for (Ticket ticket : tickets) {
            System.out.println(ticket.getDisplayInfo());
        }
        System.out.println("\nTotal: " + tickets.size() + " ticket(s)");
    }

    private static void viewAllUsers() {
        System.out.println("\n--- ALL USERS ---");
        for (User user : dataStore.getUsers()) {
            user.display();
        }
    }

    private static void addNewStaff(Admin admin) {
        System.out.println("\n--- ADD NEW STAFF ---");
        System.out.print("Full Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        System.out.println("\nSelect Role:");
        System.out.println("1. Support Agent");
        System.out.println("2. Senior Agent");
        System.out.println("3. Team Lead");
        System.out.println("4. Manager");
        System.out.print("Choice: ");
        int roleChoice = getIntInput();
        
        StaffRole role;
        switch (roleChoice) {
            case 1: role = StaffRole.SUPPORT_AGENT; break;
            case 2: role = StaffRole.SENIOR_AGENT; break;
            case 3: role = StaffRole.TEAM_LEAD; break;
            case 4: role = StaffRole.MANAGER; break;
            default:
                System.out.println("Invalid role. Defaulting to Support Agent.");
                role = StaffRole.SUPPORT_AGENT;
        }

        System.out.println("\nAvailable Departments:");
        List<Department> departments = dataStore.getDepartments();
        for (int i = 0; i < departments.size(); i++) {
            System.out.println((i + 1) + ". " + departments.get(i).getDepartmentName());
        }
        System.out.print("Select department: ");
        int deptChoice = getIntInput();
        
        String deptId = "DEPT-001";
        if (deptChoice >= 1 && deptChoice <= departments.size()) {
            deptId = departments.get(deptChoice - 1).getDepartmentId();
        }

        String userId = dataStore.generateUserId();
        Staff newStaff = new Staff(userId, name, email, password, role, deptId);
        admin.addUser(newStaff, dataStore.getUsers());
        
        System.out.println("Staff member added successfully!");
        System.out.println("Staff ID: " + newStaff.getStaffId());
    }

    private static void manageFAQs(Admin admin) {
        System.out.println("\n--- MANAGE FAQs ---");
        System.out.println("1. View All FAQs");
        System.out.println("2. Add New FAQ");
        System.out.print("Choice: ");
        
        int choice = getIntInput();
        
        if (choice == 1) {
            viewFAQs();
        } else if (choice == 2) {
            System.out.print("Question: ");
            String question = scanner.nextLine().trim();
            System.out.print("Answer: ");
            String answer = scanner.nextLine().trim();
            System.out.print("Category: ");
            String category = scanner.nextLine().trim();
            
            FAQ faq = new FAQ("FAQ-" + System.currentTimeMillis(), question, answer, category, admin.getAdminId());
            FAQ.addToDatabase(faq);
            admin.logAction("Added new FAQ: " + question);
            System.out.println("FAQ added successfully!");
        }
    }

    private static void viewDepartments() {
        System.out.println("\n--- DEPARTMENTS ---");
        for (Department dept : dataStore.getDepartments()) {
            dept.display();
            dept.displayUnsolvedTicketNumber();
            System.out.println("-".repeat(40));
        }
    }

    // ==================== UTILITY METHODS ====================
    private static int getIntInput() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String padRight(String s, int n) {
        if (s.length() >= n) {
            return s.substring(0, n - 3) + "...";
        }
        return String.format("%-" + n + "s", s);
    }
}



