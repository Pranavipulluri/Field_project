package org.meicode.fieldproject;
public class Work {
    private String title;
    private String description;
    private double price;
    private String imageUri;

    public Work(String title, String description, double price, String imageUri) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUri = imageUri;
    }

    // Getter and Setter methods
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
