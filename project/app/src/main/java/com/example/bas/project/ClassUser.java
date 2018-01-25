package com.example.bas.project;

/**
 * Created by Bas on 11-1-2018.
 */

import java.io.Serializable;
import java.util.HashMap;

/**
 * Contains the user information
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