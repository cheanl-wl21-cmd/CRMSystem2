
package services;

import java.util.regex.Pattern;
import models.Customer;
import models.User;

public class AuthenticationService {
    private DataStore dataStore;
    private User currentUser;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final String PASSWORD_PATTERN = "^[A-Za-z0-9+_.-]{8,15}$";
    private static final Pattern patternPassword = Pattern.compile(PASSWORD_PATTERN);

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
        
      
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Registration Failed: Name cannot be empty.");
            return null;
        }

        
        if (address == null || address.trim().isEmpty()) {
            System.out.println("Registration Failed: Address cannot be empty.");
            return null;
        }

       
        if (!isValid(email)) {
            System.out.println("Registration Failed: Invalid email format.");
            return null;
        }

        
        if (!isValidPassword(password)) {
            System.out.println("Registration Failed: Password must be 8-15 characters and contain valid symbols.");
            return null;
        }

       
        if (dataStore.findUserByEmail(email) != null) {
            System.out.println("Registration Failed: Email already registered!");
            return null;
        }

        
        String userId = dataStore.generateUserId();
        Customer customer = new Customer(userId, name, email, password, address, "default.png");
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
    
    public boolean isValid(String email) {
        System.out.println("Check Email --- " + email + "\n");
        if (email == null) {
            return false;
        }
        Boolean checkEmail = pattern.matcher(email).matches();
        System.out.println("Check Email Result --- " + checkEmail + "\n");
        return checkEmail;
    }
    
    public boolean isValidPassword(String password) {
        System.out.println("Check Password --- " + password + "\n");
        if (password == null) {
            return false;
        }
        Boolean checkPassword = patternPassword.matcher(password).matches();
        System.out.println("Check Password Result --- " + checkPassword + "\n");
        return checkPassword;
    }
}
