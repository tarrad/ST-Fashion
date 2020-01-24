package com.example.st_fashion;

public class ItemsDb {

    String username;
    String name;
    String type;
    String color;
    String season;
    byte[] image;

    public ItemsDb(String username, String name, String type, String color, String season, byte[] image) {
        this.username = username;
        this.name = name;
        this.type = type;
        this.color = color;
        this.season = season;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
