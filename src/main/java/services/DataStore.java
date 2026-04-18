package services;

import java.io.*; 
import enums.Priority;
import enums.StaffRole;
import models.*;
import java.util.ArrayList;
import java.util.List;
import enums.TicketStatus;
import java.util.Date;
import models.FAQ;

public class DataStore {
    private static DataStore database;
    
    private List<User> users;
    private List<Ticket> tickets;
    private List<Department> departments;
    private List<Product> products;
    private List<FAQ> faqs = new ArrayList<>();
    private List<String> auditLogs = new ArrayList<>();
    
    private int ticketCounter;
    private int userCounter;
    
    

    private DataStore() {
        users = new ArrayList<>();
        tickets = new ArrayList<>();
        departments = new ArrayList<>();
        products = new ArrayList<>();
        faqs = new ArrayList<>(); 
        
        ticketCounter = 1000;
        userCounter = 100;
        initializeSampleData();
    }
    
    public List<User> getUsers() { return users; }
    public List<Ticket> getTickets() { return tickets; }
    public List<Department> getDepartments() { return departments; }
    public List<Product> getProducts() { return products; }
    public List<FAQ> getFaqs() { return faqs; } 

    public void addUser(User user) { users.add(user); }
    public void addTicket(Ticket ticket) { tickets.add(ticket); }
    public void addFaq(FAQ faq) { faqs.add(faq); }


    public static DataStore getInstance() {
        if (database == null) {
            database = new DataStore();
        }
        return database;
    }

    private void initializeSampleData() {
        // Create departments
        Department techSupport = new Department("DEPT-001", "Technical Support");
        Department billing = new Department("DEPT-002", "Billing");
        Department sales = new Department("DEPT-003", "Sales");
        departments.add(techSupport);
        departments.add(billing);
        departments.add(sales);

        // Create admin
        Admin admin = new Admin("1", "System Admin", "admin@crm.com", "admin123");
        users.add(admin);

        // Create staff members
        Staff staff1 = new Staff("2", "John Smith", "john@crm.com", "staff123", StaffRole.SUPPORT_AGENT, "DEPT-001");
        Staff staff2 = new Staff("3", "Jane Doe", "jane@crm.com", "staff123", StaffRole.SENIOR_AGENT, "DEPT-001");
        Staff manager = new Staff("4", "Mike Johnson", "mike@crm.com", "manager123", StaffRole.MANAGER, "DEPT-001");
        users.add(staff1);
        users.add(staff2);
        users.add(manager);
        techSupport.addStaff(staff1);
        techSupport.addStaff(staff2);
        techSupport.addStaff(manager);

        // Create sample customers
        
        // Create products
        products.add(new Product("PROD-001", "CRM Basic", "Software", 99.99));
        products.add(new Product("PROD-002", "CRM Pro", "Software", 199.99));
        products.add(new Product("PROD-003", "CRM Enterprise", "Software", 499.99));

       

        // Fixed FAQs 
        // --- Fixed FAQs Initial Data using Quick Constructor ---
    faqs.add(new FAQ("How do I register an Account?", 
        "Click on Register Account and follow the steps given."));

    faqs.add(new FAQ("How do I submit a ticket?", 
        "Log in to your account, go to Support, and click 'New Ticket'."));

    faqs.add(new FAQ("What are the support hours?", 
        "Our support team is available 24/7."));

    faqs.add(new FAQ("What is the shop's return policy?", 
        "You can return any apparel within 30 days of purchase provided tags are attached."));

    faqs.add(new FAQ("How can I track my order?", 
        "Once shipped, a tracking number will be updated in your ticket details."));

    faqs.add(new FAQ("Do you offer international shipping?", 
        "Currently, we only ship within Malaysia. Plans to expand are underway."));    }

    public String generateTicketId() {
        return "TKT-" + (++ticketCounter);
    }

    public String generateUserId() {
        return String.valueOf(++userCounter);
    }
    
    // FILE I/O SAVING & LOADING 

    public void saveUsersToFile() {
        // PrintWriter enable create a file with name user.csv
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.csv"))) {
            for (User u : users) {
                writer.println(u.toCSV()); 
            }
            System.out.println("DataStore: All users saved successfully to users.csv.");
        } catch (IOException e) {
            System.out.println("DataStore Error: Could not save users.");
        }
    }

    public void loadUsersFromFile() {
        File file = new File("users.csv");
        if (!file.exists()) {
            System.out.println("DataStore: No existing save file found. Starting fresh.");
            return; 
        }

        users.clear(); // Clear the dummy data 

        // BufferedReader  enable reads the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Cut the string at every comma
                String type = parts[0]; // determine object based on first part  Admin, Customer, or Staff

                // Rebuild the objects based on the type
                if (type.equals("Customer")) {
                   Customer c = new Customer(parts[1], parts[2], parts[3], parts[4], parts[5], "default.png");
                   if (parts.length > 6) {
                    c.setProfilePic(parts[6]); 
                    }

                    users.add(c);
                } 
                else if (type.equals("Admin")) {
                    users.add(new Admin(parts[1], parts[2], parts[3], parts[4]));
                } 
                else if (type.equals("Staff")) {
                    StaffRole role = StaffRole.valueOf(parts[5]); // Convert String back to Enum
                    users.add(new Staff(parts[1], parts[2], parts[3], parts[4], role, parts[6]));
                }
            }
            if (!users.isEmpty()) {
                int maxUserId = 100; // Our base starting point
                for (User u : users) {
                    try {
                        int currentId = Integer.parseInt(u.getUserId());
                        if (currentId > maxUserId) {
                            maxUserId = currentId; // Find the highest User ID
                        }
                    } catch (NumberFormatException e) {
                        // Ignore if it's the Admin ID "1" or Staff ID "2"
                    }
                }
                userCounter = maxUserId; // Set the system counter to the highest found!
            }
            System.out.println("DataStore: Users loaded successfully from file.");
        } catch (Exception e) {
            System.out.println("DataStore Error: File corrupted or missing.");
        }
    }
    
    public void saveTicketsToFile() {
        // Creates a file called tickets.txt
        try (PrintWriter writer = new PrintWriter(new FileWriter("tickets.txt"))) {
            for (Ticket t : tickets) {
                writer.println(t.toTXT()); // Write the ticket using the | format
            }
            System.out.println("DataStore: All tickets saved successfully to tickets.txt.");
        } catch (IOException e) {
            System.out.println("DataStore Error: Could not save tickets.");
        }
    }

    public void loadTicketsFromFile() {
        File file = new File("tickets.txt");
        if (!file.exists()) {
            System.out.println("DataStore: No existing ticket file found. Using dummy data.");
            return;
        }

        tickets.clear(); // Clear dummy data

        try (BufferedReader reader = new BufferedReader(new FileReader("tickets.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line using the | symbol
                String[] parts = line.split("\\|"); 
                
                // Rebuild the ticket!
                Ticket t = new Ticket(parts[0], parts[1], parts[4], Priority.valueOf(parts[5]));
                t.setCustomerId(parts[2]);
                
                if (!parts[3].equals("null")) {
                    t.setAssignedStaffId(parts[3]);
                }
                
                t.updateStatus(TicketStatus.valueOf(parts[6]));
                
                // Convert the timestamp back into a real Date
                long timestamp = Long.parseLong(parts[7]);
                t.setDateSubmitted(new Date(timestamp));
                
                tickets.add(t);
            }
            
            //increase ticketcounter prevent overlap
            if (!tickets.isEmpty()) {
                int maxId = 1000; // Our base starting point
                for (Ticket t : tickets) {
                    
                    String numberPart = t.getTicketId().replace("TKT-", "");
                    try {
                        int currentId = Integer.parseInt(numberPart);
                        if (currentId > maxId) {
                            maxId = currentId; 
                        }
                    } catch (NumberFormatException e) {
                        // prevent error 
                    }
                }
                ticketCounter = maxId; // Set the system counter to the highest found
            }
            
            System.out.println("DataStore: Tickets loaded successfully from file.");
        } catch (Exception e) {
            System.out.println("DataStore Error: Ticket file corrupted or missing.");
        }
        
    }
    
    // ==========================================
    //          FAQ FILE SAVING & LOADING
    // ==========================================
    public void saveFAQsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("faqs.txt"))) {
            for (FAQ faq : faqs) {
                writer.println(faq.toTXT()); 
            }
            System.out.println("DataStore: FAQs saved successfully.");
        } catch (IOException e) {
            System.out.println("DataStore Error: Could not save FAQs.");
        }
    }

    public void loadFAQsFromFile() {
        java.io.File file = new java.io.File("faqs.txt");
        if (!file.exists()) return; // If no file exists, just use the dummy data

        faqs.clear(); // Clear dummy data
        try (BufferedReader reader = new BufferedReader(new FileReader("faqs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    faqs.add(new FAQ(parts[0], parts[1])); // Rebuild the FAQ
                }
            }
            System.out.println("DataStore: FAQs loaded successfully.");
        } catch (Exception e) {
            System.out.println("DataStore Error: FAQ file corrupted.");
        }
    }

    // ==========================================
    //      AUDIT LOG FILE SAVING & LOADING
    // ==========================================
    public void saveAuditLogsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("audit_logs.txt"))) {
            for (String log : auditLogs) {
                writer.println(log); // Logs are just strings, so we print them directly!
            }
            System.out.println("DataStore: Audit logs saved successfully.");
        } catch (IOException e) {
            System.out.println("DataStore Error: Could not save Audit Logs.");
        }
    }

    public void loadAuditLogsFromFile() {
        java.io.File file = new java.io.File("audit_logs.txt");
        if (!file.exists()) return;

        auditLogs.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("audit_logs.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                auditLogs.add(line); // Add the string directly back to the list
            }
            System.out.println("DataStore: Audit logs loaded successfully.");
        } catch (Exception e) {
            System.out.println("DataStore Error: Audit logs file corrupted.");
        }
    }
    
    
   
    public User findUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u; 
            }
        }
        return null; 
    }
    
    public Ticket findTicketById(String ticketId) {
        for (Ticket t : tickets) {
            if (t.getTicketId().equalsIgnoreCase(ticketId)) {
                return t;
            }
        }
        return null;
    }
    
    public Staff findStaffById(String staffId) {
        for (User u : users) {
            if (u instanceof Staff) { //filter out the normal customers
                Staff s = (Staff) u; 
                if (s.getStaffId().equals(staffId)) {
                    return s;
                }
            }
        }
        return null;
    }
    
    public List<Ticket> getTicketsByCustomer(String custId) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (custId.equals(t.getCustomerId())) {
                result.add(t);
            }
        }
        return result;
    }
}