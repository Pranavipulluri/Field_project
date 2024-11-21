package org.meicode.fieldproject;
public class Product {

    private String productName;
    private String category;
    private String imageUrl;
    private String price;

    public Product(String productName, String category, String imageUrl, String price) {
        this.productName = productName;
        this.category = category;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }
}
