package com.example.courseproject.xmlcalendar;


public class Task {
    private String text;
    private String date;
    private Category category;


    public Task(String text, String date, Category category) {
        this.text = text;
        this.date = date;
        this.category = category;
    }

    public String toString() {
        return "(" + category + ") " + text;
    }

    public String getText() {
        return text;
    }

    public void setText(String name) {
        this.text = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
