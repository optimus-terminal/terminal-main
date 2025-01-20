package org.fyp24064.userData;

import org.springframework.beans.factory.annotation.Autowired;

public class User {
    public String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

}
