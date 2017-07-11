package com.models;

/**
 * Created by Patri on 11/11/2016.
 */
public class User {

    private String firstname;
    private String lastname;
    private String email;
    private String token;

    public User(String firstname, String lastname, String email, String token){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.token = token;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
