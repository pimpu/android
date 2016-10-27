package com.alchemistdigital.buxa.model;

/**
 * Created by Pimpu on 10/27/2016.
 */
public class Email_Account_Item {
    private String type;
    private String name;

    public Email_Account_Item(String type, String name) {
        this.setType(type);
        this.setName(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
