package boundary;

import enums.Priority;
import enums.StaffRole;
import enums.TicketStatus;
import models.*;
import services.*;
import java.util.List;
import java.util.Scanner;

public class StaffBoundary {
    private Scanner scanner;
    private AuthenticationService authService;
    private TicketService ticketService;
    private DataStore dataStore = DataStore.getInstance();

    public StaffBoundary(Scanner scanner, AuthenticationService authService, TicketService ticketService) {
        this.scanner = scanner;
        this.authService = authService;
        this.ticketService = ticketService;
    }

    public void showStaffMenu(Staff staff) {
        System.out.println("\n+------------------------------------------+");
        System.out.println("|              STAFF DASHBOARD             |");
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
        System.out.printf("|  %-38s  |%n", "9. Take/Assign Ticket (Self)");

        if (staff.getStaffRole() == StaffRole.MANAGER || staff.getStaffRole() == StaffRole.TEAM_LEAD) {
            System.out.printf("|  %-38s  |%n", "10. Assign Ticket to Staff");
        }

        System.out.printf("|  %-38s  |%n", "0. Logout");
        System.out.println("+------------------------------------------+");
        System.out.print("Enter your choice: ");

        int choice = Utility.getIntInput();

        switch (choice) {
            case 1:
                List<Ticket> allTickets = ticketService.getAllTickets();
                System.out.println("\n===== ALL TICKETS =====");
                if (allTickets.isEmpty()) {
                    System.out.println("No tickets in the system.");
                } else {
                    for (Ticket ticket : allTickets) {
                        System.out.println(ticket.getDisplayInfo());
                    }
                }
                break;
            case 2: viewAssignedTickets(staff); break;
            case 3: searchTickets(staff); break;
            case 4: filterByStatus(staff); break;
            case 5: filterByPriority(staff); break;
            case 6: viewTicketDetailsStaff(); break;
            case 7: addResponseToTicket(staff); break;
            case 8: updateTicketStatusStaff(staff); break;
            case 9: takeTicket(staff); break; 
            case 10:
                if (staff.getStaffRole() == StaffRole.MANAGER || staff.getStaffRole() == StaffRole.TEAM_LEAD) {
                    assignTicketToStaff(staff);
                } else {
                    System.out.println("Invalid choice.");
                }
                break;
            case 0: authService.logout(); break;
            default: System.out.println("Invalid choice.");
        }
        if (choice != 0) {
            Utility.pauseAndClear();
        }
    }

    private void viewAssignedTickets(Staff staff) {
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

   private void searchTickets(Staff staff) {
        System.out.print("\nEnter search keyword: ");
        String keyword = scanner.nextLine().trim();
        List<Ticket> results = staff.searchTicketByKeyword(ticketService.getAllTickets(), keyword);
        System.out.println("\n--- SEARCH RESULTS ---");
        for (Ticket t : results) {
            System.out.println(t.getDisplayInfo());
        }
    }

    private void filterByStatus(Staff staff) {
        System.out.println("\nSelect Status:");
        System.out.println("1. Open");
        System.out.println("2. In Progress");
        System.out.println("3. Pending");
        System.out.println("4. Resolved");
        System.out.println("5. Closed");
        System.out.print("Choice: ");
        
        int choice = Utility.getIntInput();
        TicketStatus status;
        switch (choice) {
            case 1: status = TicketStatus.OPEN; break;
            case 2: status = TicketStatus.IN_PROGRESS; break;
            case 3: status = TicketStatus.PENDING; break;
            case 4: status = TicketStatus.RESOLVED; break;
            case 5: status = TicketStatus.CLOSED; break;
            default: System.out.println("Invalid choice."); return;
        }
       
        List<Ticket> filtered = staff.filterTicketsByStatus(ticketService.getAllTickets(), status);
        System.out.println("\n--- FILTERED TICKETS ---");
        for(Ticket t : filtered) {
            System.out.println(t.getDisplayInfo());
        }
    }

    private void filterByPriority(Staff staff) {
        System.out.println("\nSelect Priority:");
        System.out.println("1. Low");
        System.out.println("2. Medium");
        System.out.println("3. High");
        System.out.print("Choice: ");
        
        int choice = Utility.getIntInput();
        Priority priority;
        switch (choice) {
            case 1: priority = Priority.LOW; break;
            case 2: priority = Priority.MEDIUM; break;
            case 3: priority = Priority.HIGH; break;
            default: System.out.println("Invalid choice."); return;
        }
        
        List<Ticket> filtered = staff.filterTicketsByPriority(ticketService.getAllTickets(), priority);
        System.out.println("\n--- FILTERED TICKETS ---");
        for(Ticket t : filtered) {
            System.out.println(t.getDisplayInfo());
        }
    }

    private void viewTicketDetailsStaff() {
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim();
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }
        System.out.println(ticket.getTicketDetails());
    }

    private void addResponseToTicket(Staff staff) {
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
        System.out.println(staff.addResponse(ticket, response));
        dataStore.saveTicketsToFile(); 
    }

    private void updateTicketStatusStaff(Staff staff){
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

    private void takeTicket(Staff staff) {
        System.out.print("\nEnter Ticket ID to take: ");
        String ticketId = scanner.nextLine().trim().toUpperCase(); 
        
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        
        String result = staff.takeTicket(ticket);
        System.out.println(result);

        
        if (result.startsWith("Success")) {
            System.out.println("\nPlease evaluate and set the Priority for this ticket:");
            System.out.println("1. Low");
            System.out.println("2. Medium");
            System.out.println("3. High");
            System.out.println("4. Keep Current Priority (" + ticket.getPriority().getDisplayName() + ")");
            System.out.print("Choice: ");
            
            int choice = Utility.getIntInput();
            switch (choice) {
                case 1: 
                    ticket.setPriority(Priority.LOW); 
                    System.out.println("Priority updated to LOW."); 
                    break;
                case 2: 
                    ticket.setPriority(Priority.MEDIUM); 
                    System.out.println("Priority updated to MEDIUM."); 
                    break;
                case 3: 
                    ticket.setPriority(Priority.HIGH); 
                    System.out.println("Priority updated to HIGH."); 
                    break;
                case 4: 
                    System.out.println("Priority unchanged."); 
                    break;
                default: 
                    System.out.println("Invalid choice. Priority unchanged.");
            }
        }
        
        dataStore.saveTicketsToFile(); 
    }

    private void assignTicketToStaff(Staff manager) {
        System.out.print("\nEnter Ticket ID: ");
        String ticketId = scanner.nextLine().trim();
        Ticket ticket = ticketService.getTicketById(ticketId);
        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

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

        System.out.println(manager.assignTicket(ticket, targetStaff));
        dataStore.saveTicketsToFile(); 
    }
}