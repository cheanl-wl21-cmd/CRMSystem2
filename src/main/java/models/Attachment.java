/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import interfaces.Displayable;

public class Attachment implements Displayable {
    private String attachmentId;
    private String fileName;
    private double fileSizeMB;
    private String filePath;

    public Attachment(String attachmentId, String fileName, double fileSizeMB) {
        this.attachmentId = attachmentId;
        this.fileName = fileName;
        this.fileSizeMB = fileSizeMB;
        this.filePath = "/uploads/" + fileName;
    }

    public boolean uploadFileWithGUI() {
        // Simulated file upload
        System.out.println("Uploading file: " + fileName + "...");
        System.out.println("File uploaded successfully!");
        return true;
    }

    public void downloadFile() {
        System.out.println("Downloading file: " + fileName + " from " + filePath);
        System.out.println("Download complete!");
    }

    @Override
    public void display() {
        System.out.println(getDisplayInfo());
    }

    @Override
    public String getDisplayInfo() {
        return String.format("Attachment: %s | Size: %.2f MB | Path: %s", fileName, fileSizeMB, filePath);
    }

    // Getters and Setters
    public String getAttachmentId() { return attachmentId; }
    public String getFileName() { return fileName; }
    public double getFileSizeMB() { return fileSizeMB; }
    public String getFilePath() { return filePath; }

    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setFileSizeMB(double fileSizeMB) { this.fileSizeMB = fileSizeMB; }
}
