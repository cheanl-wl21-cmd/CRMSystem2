
package models;

import interfaces.Displayable;

public class Attachment implements Displayable {
    private String attachmentId;
    private String fileName;
    private double fileSizeMB;
    private String filePath;
    
    public Attachment(){
        
    }
    public Attachment(String attachmentId, String fileName, double fileSizeMB) {
        this.attachmentId = attachmentId;
        this.fileName = fileName;
        this.fileSizeMB = fileSizeMB;
        this.filePath = "/uploads/" + fileName;
    }
    
    
    public String getAttachmentId() { return attachmentId; }
    public String getFileName() { return fileName; }
    public double getFileSizeMB() { return fileSizeMB; }
    public String getFilePath() { return filePath; }

    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileSizeMB(double fileSizeMB) { this.fileSizeMB = fileSizeMB; }
   
  public boolean uploadFile() {
        return true; 
    }

  
    public String downloadFile() {
        return "Downloading file: " + fileName + " from " + filePath + "...\nDownload complete!";
    }

    @Override
    public void display() {
        System.out.println(getDisplayInfo());
    }

    @Override
    public String getDisplayInfo() {
        return String.format("Attachment: %s | Size: %.2f MB | Path: %s", fileName, fileSizeMB, filePath);
    }

    
}
