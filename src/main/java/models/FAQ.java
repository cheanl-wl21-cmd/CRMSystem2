/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import interfaces.Displayable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FAQ implements Displayable {
    private String faqId;
    private String question;
    private String answer;
    private String category;
    private String adminId;

    private static List<FAQ> faqDatabase = new ArrayList<>();

    public FAQ(String faqId, String question, String answer, String category, String adminId) {
        this.faqId = faqId;
        this.question = question;
        this.answer = answer;
        this.category = category;
        this.adminId = adminId;
    }

    public void displayFAQ() {
        System.out.println("\nQ: " + question);
        System.out.println("A: " + answer);
        System.out.println("Category: " + category);
    }

    public static List<FAQ> searchByCategory(String cat) {
        return faqDatabase.stream()
            .filter(faq -> faq.category.equalsIgnoreCase(cat))
            .collect(Collectors.toList());
    }

    public static void addToDatabase(FAQ faq) {
        faqDatabase.add(faq);
    }

    public static List<FAQ> getAllFAQs() {
        return new ArrayList<>(faqDatabase);
    }

    @Override
    public void display() {
        displayFAQ();
    }

    @Override
    public String getDisplayInfo() {
        return String.format("FAQ: %s | Category: %s", question, category);
    }

    // Getters and Setters
    public String getFaqId() { return faqId; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getCategory() { return category; }
    public String getAdminId() { return adminId; }

    public void setQuestion(String question) { this.question = question; }
    public void setAnswer(String answer) { this.answer = answer; }
    public void setCategory(String category) { this.category = category; }
}
