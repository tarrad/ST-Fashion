package com.example.st_fashion.ui.params;

public class UpdateUniforms {

    private static byte[] photoTop = null;
    private static byte[] photoBottom = null;
    private static byte[] photoShoes = null;
    private static String season = null;
    private static String color = null;

    public static byte[] getPhotoTop() {
        return photoTop;
    }

    public static void setPhotoTop(byte[] photoTop) {
        UpdateUniforms.photoTop = photoTop;
    }

    public static byte[] getPhotoBottom() {
        return photoBottom;
    }

    public static void setPhotoBottom(byte[] photoBottom) {
        UpdateUniforms.photoBottom = photoBottom;
    }

    public static byte[] getPhotoShoes() {
        return photoShoes;
    }

    public static void setPhotoShoes(byte[] photoShoes) {
        UpdateUniforms.photoShoes = photoShoes;
    }

    public static String getSeason() {
        return season;
    }

    public static void setSeason(String season) {
        UpdateUniforms.season = season;
    }

    public static String getColor() {
        return color;
    }

    public static void setColor(String color) {
        UpdateUniforms.color = color;
    }

}
