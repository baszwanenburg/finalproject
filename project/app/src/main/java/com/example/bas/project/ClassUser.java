package com.example.bas.project;

import java.io.Serializable;

/**
 * This class contains the user's information, and is used for Firebase authentication.
 */
public class ClassUser implements Serializable {
    public String id;
    public String username;
    public String email;

    // Default constructor for Firebase
    public ClassUser() {}

    public ClassUser(String id, String username, String email) {
        this.id        = id;
        this.username  = username;
        this.email     = email;
    }
}