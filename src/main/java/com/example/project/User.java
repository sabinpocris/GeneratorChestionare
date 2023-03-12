package com.example.project;

import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private static final long serialVersionUID = 333L;

    public User(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User " + this.username;
    }
}
