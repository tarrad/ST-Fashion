package com.example.st_fashion.ui.create;

public class UpdateItems {

    private static byte[] photoTop = null;
    private static byte[] photoBottom = null;
    private static byte[] photoShoes = null;

    public static byte[] getPhotoTop() {
        return photoTop;
    }

    public static byte[] getPhotoBottom() {
        return photoBottom;
    }

    public static byte[] getPhotoShoes() {
        return photoShoes;
    }

    public static void setPhotoTop(byte[] photoTop) {
        UpdateItems.photoTop = photoTop;
    }

    public static void setPhotoBottom(byte[] photoBottom) {
        UpdateItems.photoBottom = photoBottom;
    }

    public static void setPhotoShoes(byte[] photoShoes) {
        UpdateItems.photoShoes = photoShoes;
    }

}
