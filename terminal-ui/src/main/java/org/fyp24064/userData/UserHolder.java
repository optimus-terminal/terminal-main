package org.fyp24064.userData;

public class UserHolder {
    private static UserHolder instance;
    private User user;

    private UserHolder() { }

    public static UserHolder getInstance() {
        if (instance == null) {
            instance = new UserHolder();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}