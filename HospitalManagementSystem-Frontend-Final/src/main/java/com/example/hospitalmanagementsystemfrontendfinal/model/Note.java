package com.example.hospitalmanagementsystemfrontendfinal.model;

public class Note {
    private String createdAt;
    private String text;

    public Note(String createdAt, String text) {
        this.createdAt = createdAt;
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }
}