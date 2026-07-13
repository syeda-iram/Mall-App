package com.example.madproject;

public class FavouriteItem {
    private String id;
    private String name;
    private String brand;
    private String price;
    private String discount;
    private int imageResource;
    private String storeName;

    public FavouriteItem() {}

    public FavouriteItem(String id, String name, String brand, String price,
                         String discount, int imageResource, String storeName) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.discount = discount;
        this.imageResource = imageResource;
        this.storeName = storeName;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getDiscount() { return discount; }
    public void setDiscount(String discount) { this.discount = discount; }

    public int getImageResource() { return imageResource; }
    public void setImageResource(int imageResource) { this.imageResource = imageResource; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }
}