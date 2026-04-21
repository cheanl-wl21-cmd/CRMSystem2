package boundary;

import enums.StaffRole;
import enums.TicketStatus;
import models.*;
import services.*;
import java.util.List;
import java.util.Scanner;

public class AdminBoundary {
    private Scanner scanner;
    private AuthenticationService authService;
    private TicketService ticketService;
    private DataStore dataStore = DataStore.getInstance();

    public AdminBoundary(Scanner scanner, AuthenticationService authService, TicketService ticketService) {
        this.scanner = scanner;
        this.authService = authService;
        this.ticketService = ticketService;
    }

    public void showAdminMenu(Admin admin) {
        System.out.println("\n+------------------------------------------+");
        System.out.println("|              ADMIN DASHBOARD             |");
        System.out.printf("|  Welcome, %-30s |%n", admin.getName());
        System.out.println("+------------------------------------------+");
        System.out.printf("|  %-38s  |%n", "1. Generate Monthly Report");
        System.out.printf("|  %-38s  |%n", "2. View All Tickets");
        System.out.printf("|  %-38s  |%n", "3. View All Users");
        System.out.printf("|  %-38s  |%n", "4. View Audit Logs");
        System.out.printf("|  %-38s  |%n", "5. Add New Staff");
        System.out.printf("|  %-38s  |%n", "6. Manage FAQs");
        System.out.printf("|  %-38s  |%n", "7. View Departments");
        System.out.printf("|  %-38s  |%n", "8. Update Ticket Status");
        System.out.printf("|  %-38s  |%n", "9. Reassign Ticket");
        System.out.printf("|  %-38s  |%n", "0. Logout");
        System.out.println("+------------------------------------------+");
        System.out.print("Enter your choice: ");

        int choice = Utility.getIntInput();

        switch (choice) {
            case 1: generateReport(admin); break;
            case 2: viewAllTicketsAdmin(); break;
            case 3: viewAllUsers(); break;
            case 4:
                System.out.println("\n===== AUDIT LOGS =====");
                System.out.println(admin.getAuditLogsAsString());
                        
                break;
            case 5: addNewStaff(admin); break;
            case 6: manageFAQs(admin); break;
            case 7: viewDepartments(); break;
            case 8: updateTicketStatusAdmin(admin); break;
            case 9: reassignTicket(admin); break;
            case 0: authService.logout(); break;
            default: System.out.println("Invalid choice.");
        }
        if (choice != 0) {
            Utility.pauseAndClear();
        }
    }

    private void generateReport(Admin admin) {
        System.out.print("\nEnter month (1-12): ");
        int month = Utility.getIntInput();
        if (month < 1 || month > 12) {
            System.out.println("Invalid month.");
            return;
        }

        System.out.print("Enter year: ");
        int year = Utility.getIntInput();
        if (year < 2000 || year > 2100) {
            System.out.println("Invalid year.");
            return;
        }

        Report report = admin.generateMonthlyReport(month, year, ticketService.getAllTickets());
        System.out.println(report.generateReport());
    }

    private void viewAllTicketsAdmin() {
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

    private void viewAllUsers() {
        System.out.println("\n--- ALL USERS ---");
        for (User user : dataStore.getUsers()) {
            System.out.println(user.getDisplayInfo());
        }
    }

    private void addNewStaff(Admin admin) {
        System.out.println("\n--- ADD NEW STAFF ---");
        String name;
        while (true) {
            System.out.print("Full Name: ");
            name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("--> Error: Name cannot be empty! Please enter a valid name.");
            } else {
                break; 
            }
        }
        
        String email;
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (!authService.isValid(email)) {
                System.out.println("--> Error: Invalid email format! Please include '@' and a domain.");
            } else if (dataStore.findUserByEmail(email) != null) {
                System.out.println("--> Error: This email is already registered to another user!");
            } else {
                break; 
            }
        }

        String password;
        while (true) {
            System.out.print("Password: ");
            password = scanner.nextLine().trim();
            if (!authService.isValidPassword(password)) {
                System.out.println("--> Error: Password must be between 8 and 15 characters long.");
            } else {
                break; 
            }
        }
        
        System.out.println("\nSelect Role:");
        System.out.println("1. Support Agent\n2. Senior Agent\n3. Team Lead\n4. Manager");
        System.out.print("Choice: ");
        int roleChoice = Utility.getIntInput();
        
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
        int deptChoice = Utility.getIntInput();
        
        String deptId = "DEPT-001";
        if (deptChoice >= 1 && deptChoice <= departments.size()) {
            deptId = departments.get(deptChoice - 1).getDepartmentId();
        }

        String userId = dataStore.generateUserId();
        Staff newStaff = new Staff(userId, name, email, password, role, deptId);
        
        admin.addUser(newStaff, dataStore.getUsers());
        dataStore.saveUsersToFile(); 
        System.out.println("Staff member added successfully! Staff ID: " + newStaff.getStaffId());
    }    
    
    private void updateTicketStatusAdmin(Admin admin){
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim();
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null){
            System.out.println("Ticket not found.");
            return;
        }
        System.out.println("Current Status: " + ticket.getStatus().getDisplayName());
        System.out.println("\nSelect New Status:");
        System.out.println("1. Open\n2. In Progress\n3. Pending\n4. Resolved\n5. Closed");
        System.out.print("Choice: ");
        
        int choice = Utility.getIntInput();
        TicketStatus newStatus;
        switch(choice){
            case 1: newStatus = TicketStatus.OPEN; break;
            case 2: newStatus = TicketStatus.IN_PROGRESS; break;
            case 3: newStatus = TicketStatus.PENDING; break;
            case 4: newStatus = TicketStatus.RESOLVED; break;
            case 5: newStatus = TicketStatus.CLOSED; break;
            default: System.out.println("Invalid choice. Status not changed."); return;
        }
        
        ticketService.updateTicketStatus(ticketId, newStatus);
        System.out.println("Successfully updated Ticket " + ticketId + " to " + newStatus.getDisplayName() );
        dataStore.saveTicketsToFile(); 
    }

    private void manageFAQs(Admin admin) {
        System.out.println("\n--- MANAGE FAQs ---");
        System.out.println("1. View All FAQs");
        System.out.println("2. Add New FAQ");
        System.out.print("Choice: ");
        
        int choice = Utility.getIntInput();
        if (choice == 1) {
            viewFAQs();
        } else if (choice == 2) {
            System.out.print("Question: ");
            String question = scanner.nextLine().trim();
            System.out.print("Answer: ");
            String answer = scanner.nextLine().trim();
            
            FAQ faq = new FAQ(question, answer);
            dataStore.getFaqs().add(faq); 
            
            admin.logAction("Added new FAQ: " + question);
            System.out.println("FAQ added successfully!");
            dataStore.saveFAQsToFile(); 
            dataStore.saveAuditLogsToFile(); 
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

    private void viewDepartments() {
        System.out.println("\n--- DEPARTMENTS ---");
        for (Department dept : dataStore.getDepartments()) {
            System.out.println(dept.getDisplayInfo());
            System.out.println(dept.getUnsolvedTicketNumberString()); 
            System.out.println("-".repeat(40));
        }
    }
    private void reassignTicket(Admin admin) {
        System.out.println("\n--- REASSIGN TICKET ---");
        System.out.print("Enter Ticket ID to reassign: ");
        String ticketId = scanner.nextLine().trim().toUpperCase();

        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        
        String currentStaff = ticket.getAssignedStaffId();
        System.out.println("Current Assigned Staff ID: " + (currentStaff != null ? currentStaff : "Unassigned"));

        
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

        System.out.print("\nEnter New Staff ID: ");
        String staffId = scanner.nextLine().trim().toUpperCase();

        Staff targetStaff = dataStore.findStaffById(staffId);
        if (targetStaff == null) {
            System.out.println("Error: Staff not found in the system.");
            return;
        }

        
        ticket.setAssignedStaffId(targetStaff.getStaffId());
        
        
        if(ticket.getStatus() == TicketStatus.OPEN || ticket.getStatus() == TicketStatus.PENDING) {
             ticket.updateStatus(TicketStatus.IN_PROGRESS);
        }
        
        admin.logAction("Admin reassigned Ticket " + ticket.getTicketId() + " to Staff " + targetStaff.getStaffId());

        System.out.println("--> Success! Ticket " + ticket.getTicketId() + " has been reassigned to " + targetStaff.getName());

        
        dataStore.saveTicketsToFile();
        dataStore.saveAuditLogsToFile();
    }
}