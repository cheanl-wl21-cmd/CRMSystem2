
package models;

import interfaces.Displayable;

public class Product implements Displayable {
    private String productId;
    private String name;
    private String categoryName;
    private double price;
    
    public Product(){
        
    }

    public Product(String productId, String name, String categoryName, double price) {
        this.productId = productId;
        this.name = name;
        this.categoryName = categoryName;
        this.price = price;
    }
    
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategoryName() { return categoryName; }
    public double getPrice() { return price; }

    public void setName(String name) { this.name = name; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public void setPrice(double price) { this.price = price; }

    public void displayProductInfo() {
        System.out.println("Product ID: " + productId);
        System.out.println("Name: " + name);
        System.out.println("Category: " + categoryName);
        System.out.println("Price: $" + String.format("%.2f", price));
    }

    @Override
    public void display() {
        displayProductInfo();
    }

    @Override
    public String getDisplayInfo() {
        return String.format("Product: %s | ID: %s | Category: %s | Price: $%.2f",
                name, productId, categoryName, price);
    }

    
   
}
