package com.example.eCommerceApp2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories") // Specify the collection name if desired
public class Category {

    @Id
    private String id; // MongoDB uses String for ObjectId
    private String name;
    private String imageName;
    private Boolean isActive; // Use 'isActive' to be consistent with Java naming conventions

    // Constructors
    public Category() {
        // Default constructor for MongoDB
    }

    public Category(String name, String imageName, Boolean isActive) {
        this.name = name;
        this.imageName = imageName;
        this.isActive = isActive;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Boolean getIsActive() { // Getter name reflects the field name
        return isActive;
    }

    public void setIsActive(Boolean isActive) { // Setter name reflects the field name
        this.isActive = isActive;
    }
}
