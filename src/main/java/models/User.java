package models;

import interfaces.Displayable;

public abstract class User implements Displayable {
    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected boolean isLoggedIn;
    
    public User(){
        
    }

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isLoggedIn = false;
    }
    
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isLoggedIn() { return isLoggedIn; }
    
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    public boolean login(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            this.isLoggedIn = true;
            return true;
        }
        return false;
    }

    public String logout() {
        this.isLoggedIn = false;
        return name + " has logged out.";
    }

    public String updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
        return "Profile updated successfully.";
    }

    public abstract String getUserType();

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String getDisplayInfo() {
        return String.format("User ID: %s | Name: %s | Email: %s | Type: %s",
                userId, name, email, getUserType());
    }
}