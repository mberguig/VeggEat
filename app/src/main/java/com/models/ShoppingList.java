package com.models;

import java.util.Date;

/**
 * Created by Patri on 11/11/2016.
 */
public class ShoppingList {

    private int id;
    private String name;
    private Date created_date;
    private boolean completed;

    public ShoppingList(int id, String name, Date created_date, boolean completed){
        this.id = id;
        this.name = name;
        this.created_date = created_date;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
