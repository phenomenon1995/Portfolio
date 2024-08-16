package com.techelevator.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Product {
    
    private int productId;
    @JsonProperty("product_category_id")
    private int productCategoryId;
    @JsonProperty("product_category_description")
    private String productCategoryDescription;
    private BigDecimal price;
    private String description;
    private int quantity;
    public Product() {}
    public Product(int productId, int productCategoryId, String productCategoryDescription,
                   BigDecimal price, String description, int quantity) {
        this.productId = productId;
        this.productCategoryId = productCategoryId;
        this.productCategoryDescription = productCategoryDescription;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductCategoryDescription() {
        return productCategoryDescription;
    }

    public void setProductCategoryDescription(String productCategoryDescription) {
        this.productCategoryDescription = productCategoryDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
