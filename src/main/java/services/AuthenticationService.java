/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import models.Customer;
import models.User;

public class AuthenticationService {
    private DataStore dataStore;
    private User currentUser;

    public AuthenticationService() {
        this.dataStore = DataStore.getInstance();
    }

    public User login(String email, String password) {
        User user = dataStore.findUserByEmail(email);
        if (user != null && user.validatePassword(password)) {
            user.login(email, password);
            currentUser = user;
            return user;
        }
        System.out.println("Invalid email or password.");
        return null;
    }

    public void logout() {
        if (currentUser != null) {
            currentUser.logout();
            currentUser = null;
        }
    }

    public Customer registerCustomer(String name, String email, String password, String address) {
        // Check if email already exists
        if (dataStore.findUserByEmail(email) != null) {
            System.out.println("Email already registered!");
            return null;
        }

        String userId = dataStore.generateUserId();
        Customer customer = new Customer(userId, name, email, password, address);
        customer.register();
        dataStore.addUser(customer);
        return customer;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null && currentUser.isLoggedIn();
    }
}
