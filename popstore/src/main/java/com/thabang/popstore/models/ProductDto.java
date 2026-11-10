package com.thabang.popstore.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ProductDto {

    @NotEmpty(message = "The name of the product is required")
    private String name;

    @NotEmpty(message = "The name of the brand is required")
    private String brand;

    @NotEmpty(message = "Category name required")
    private String category;

    @NotNull(message = "The price is required")
    @Min(value = 0, message = "The price cannot be negative")
    private Double price;


    @Size(min = 8, max = 2000, message = "The description must be between 8 and 2000 characters")
    private String description;

    private MultipartFile imageFile;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public MultipartFile getImageFile() { return imageFile; }
    public void setImageFile(MultipartFile imageFile) { this.imageFile = imageFile; }
}