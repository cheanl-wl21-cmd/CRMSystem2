/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import enums.Priority;
import enums.StaffRole;
import models.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;
    
    private List<User> users;
    private List<Ticket> tickets;
    private List<Department> departments;
    private List<Product> products;
    private int ticketCounter;
    private int userCounter;

    private DataStore() {
        users = new ArrayList<>();
        tickets = new ArrayList<>();
        departments = new ArrayList<>();
        products = new ArrayList<>();
        ticketCounter = 1000;
        userCounter = 100;
        initializeSampleData();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
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
        Customer customer1 = new Customer("5", "Alice Brown", "alice@email.com", "cust123", "123 Main St");
        Customer customer2 = new Customer("6", "Bob Wilson", "bob@email.com", "cust123", "456 Oak Ave");
        users.add(customer1);
        users.add(customer2);

        // Create products
        products.add(new Product("PROD-001", "CRM Basic", "Software", 99.99));
        products.add(new Product("PROD-002", "CRM Pro", "Software", 199.99));
        products.add(new Product("PROD-003", "CRM Enterprise", "Software", 499.99));

        // Create sample tickets
        Ticket ticket1 = customer1.createTicket("TKT-1001", "PROD-001", "Cannot login to the system", Priority.HIGH);
        Ticket ticket2 = customer1.createTicket("TKT-1002", "PROD-002", "Need help with report generation", Priority.MEDIUM);
        Ticket ticket3 = customer2.createTicket("TKT-1003", "PROD-001", "Feature request: Dark mode", Priority.LOW);
        tickets.add(ticket1);
        tickets.add(ticket2);
        tickets.add(ticket3);

        // Create FAQs
        FAQ.addToDatabase(new FAQ("FAQ-001", "How do I reset my password?", 
            "Click on 'Forgot Password' on the login page and follow the instructions.", "Account", "ADMIN-1"));
        FAQ.addToDatabase(new FAQ("FAQ-002", "How do I submit a ticket?", 
            "Log in to your account, go to Support, and click 'New Ticket'.", "Support", "ADMIN-1"));
        FAQ.addToDatabase(new FAQ("FAQ-003", "What are the support hours?", 
            "Our support team is available 24/7.", "General", "ADMIN-1"));
    }

    public String generateTicketId() {
        return "TKT-" + (++ticketCounter);
    }

    public String generateUserId() {
        return String.valueOf(++userCounter);
    }

    // Getters
    public List<User> getUsers() { return users; }
    public List<Ticket> getTickets() { return tickets; }
    public List<Department> getDepartments() { return departments; }
    public List<Product> getProducts() { return products; }

    public void addUser(User user) { users.add(user); }
    public void addTicket(Ticket ticket) { tickets.add(ticket); }

    public User findUserByEmail(String email) {
        return users.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElse(null);
    }

    public Ticket findTicketById(String ticketId) {
        return tickets.stream()
            .filter(t -> t.getTicketId().equalsIgnoreCase(ticketId))
            .findFirst()
            .orElse(null);
    }

    public Staff findStaffById(String staffId) {
        return users.stream()
            .filter(u -> u instanceof Staff)
            .map(u -> (Staff) u)
            .filter(s -> s.getStaffId().equals(staffId))
            .findFirst()
            .orElse(null);
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

