package com.example.st_fashion;

public class UniformsDb {

    String username;
    byte[] imageTop;
    byte[] imageBottom;
    byte[] imageShoes;
    String season;
    String color;

    public UniformsDb(String username, byte[] imageTop, byte[] imageBottom, byte[] imageShoes, String season, String color) {
        this.username = username;
        this.imageTop = imageTop;
        this.imageBottom = imageBottom;
        this.imageShoes = imageShoes;
        this.season = season;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getImageTop() {
        return imageTop;
    }

    public void setImageTop(byte[] imageTop) {
        this.imageTop = imageTop;
    }

    public byte[] getImageBottom() {
        return imageBottom;
    }

    public void setImageBottom(byte[] imageBottom) {
        this.imageBottom = imageBottom;
    }

    public byte[] getImageShoes() {
        return imageShoes;
    }

    public void setImageShoes(byte[] imageShoes) {
        this.imageShoes = imageShoes;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
