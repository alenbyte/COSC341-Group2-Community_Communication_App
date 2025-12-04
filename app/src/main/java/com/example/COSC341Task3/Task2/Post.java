package com.example.COSC341Task3.Task2;

public class Post {
    private String userName;
    private String text;
    private Integer imageResId; // null = no image

    // who owns it / bell state
    private boolean ownedByCurrentUser;
    private boolean notificationsOn;

    // new: like + bookmark state
    private boolean liked;
    private boolean saved;

    // simplest constructor (for old seed posts: not owned, no bell)
    public Post(String userName, String text) {
        this(userName, text, null, false, false, false, false);
    }

    // old constructor with image (seed posts with image)
    public Post(String userName, String text, Integer imageResId) {
        this(userName, text, imageResId, false, false, false, false);
    }

    // full constructor (for your new posts from Homepage)
    public Post(String userName,
                String text,
                Integer imageResId,
                boolean ownedByCurrentUser,
                boolean notificationsOn) {
        this(userName, text, imageResId, ownedByCurrentUser, notificationsOn, false, false);
    }

    // internal “master” constructor
    public Post(String userName,
                String text,
                Integer imageResId,
                boolean ownedByCurrentUser,
                boolean notificationsOn,
                boolean liked,
                boolean saved) {
        this.userName = userName;
        this.text = text;
        this.imageResId = imageResId;
        this.ownedByCurrentUser = ownedByCurrentUser;
        this.notificationsOn = notificationsOn;
        this.liked = liked;
        this.saved = saved;
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

    public boolean isOwnedByCurrentUser() {
        return ownedByCurrentUser;
    }

    public boolean isNotificationsOn() {
        return notificationsOn;
    }

    public void setNotificationsOn(boolean notificationsOn) {
        this.notificationsOn = notificationsOn;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
