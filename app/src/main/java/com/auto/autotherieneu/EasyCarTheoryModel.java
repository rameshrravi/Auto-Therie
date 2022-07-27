package com.auto.autotherieneu;

public class EasyCarTheoryModel {


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    String categoryID;
    String image;
    String description;
    String isOnlyDescription;
    String isOnlyImage;
    String isBoth;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsOnlyDescription() {
        return isOnlyDescription;
    }

    public void setIsOnlyDescription(String isOnlyDescription) {
        this.isOnlyDescription = isOnlyDescription;
    }

    public String getIsOnlyImage() {
        return isOnlyImage;
    }

    public void setIsOnlyImage(String isOnlyImage) {
        this.isOnlyImage = isOnlyImage;
    }

    public String getIsBoth() {
        return isBoth;
    }

    public void setIsBoth(String isBoth) {
        this.isBoth = isBoth;
    }
}
