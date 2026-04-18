package models;

import interfaces.Displayable;

public class FAQ implements Displayable {
    private String faqId;
    private String question;
    private String answer;
    private String category;
    private String adminId;
    

    public FAQ(){
        
    }

    
      public FAQ(String question, String answer) {
    this.faqId = "FAQ-" + System.currentTimeMillis(); // Generate a random ID
    this.question = question;
    this.answer = answer;
    this.category = "General";
    this.adminId = "ADMIN-001";
}
    
    
    public String getFaqId() { return faqId; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getCategory() { return category; }
    public String getAdminId() { return adminId; }

    public void setQuestion(String question) { this.question = question; }
    public void setAnswer(String answer) { this.answer = answer; }
    public void setCategory(String category) { this.category = category; }

   
    public String getFaqDetailsString() {
        return "\nQ: " + question + "\n" +
               "A: " + answer + "\n" +
               "Category: " + category;
    }
    
    public String toTXT() {
        return question + "|" + answer;
    }

    

    @Override
    public String getDisplayInfo() {
        return String.format("FAQ: %s | Category: %s", question, category);
    }
}