package com.example.COSC341Task3.Task2;

public class Post {
    private String userName;
    private String text;
    private Integer imageResId; // null = no image

    public Post(String userName, String text) {
        this(userName, text, null);
    }

    public Post(String userName, String text, Integer imageResId) {
        this.userName = userName;
        this.text = text;
        this.imageResId = imageResId;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public Integer getImageResId() {
        return imageResId;
    }
}
