package com.unique_user.unique_username_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usernames")
public class Username {
    @Id
    private String username;

    public Username() {}

    public Username(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

