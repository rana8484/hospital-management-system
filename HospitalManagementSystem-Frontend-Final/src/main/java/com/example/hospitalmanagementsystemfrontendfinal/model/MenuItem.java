package com.example.hospitalmanagementsystemfrontendfinal.model;

public class MenuItem {
    private final String text;
    private final String iconPath;

    public MenuItem(String text, String iconPath) {
        this.text = text;
        this.iconPath = iconPath;
    }

    public String getText() {
        return text;
    }

    public String getIconPath() {
        return iconPath;
    }
}
