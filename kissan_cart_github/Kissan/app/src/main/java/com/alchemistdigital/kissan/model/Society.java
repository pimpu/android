package com.alchemistdigital.kissan.model;

/**
 * Created by user on 3/2/2016.
 */
public class Society {
    private int id;
    private String creted_at;
    private int userId;
    private String soc_name;
    private String soc_contact;
    private String soc_email;
    private int soc_status;
    private String soc_adrs;

    public Society() {
    }

    public Society(int id, int userId, String soc_name, String soc_contact, String soc_email, String soc_adrs,int soc_status) {
        this.id = id;
        this.userId = userId;
        this.soc_name = soc_name;
        this.soc_contact = soc_contact;
        this.soc_email = soc_email;
        this.soc_adrs = soc_adrs;
        this.soc_status = soc_status;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setSoc_status(int soc_status) {
        this.soc_status = soc_status;
    }

    public void setSoc_name(String soc_name) {
        this.soc_name = soc_name;
    }

    public void setSoc_contact(String soc_contact) {
        this.soc_contact = soc_contact;
    }

    public void setSoc_email(String soc_email) {
        this.soc_email = soc_email;
    }

    public void setSoc_adrs(String soc_adrs) {
        this.soc_adrs = soc_adrs;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCreted_at(String creted_at) {
        this.creted_at = creted_at;
    }

    // getters
    public int getId() {
        return id;
    }

    public int getSoc_status() {
        return soc_status;
    }

    public String getSoc_name() {
        return soc_name;
    }

    public String getSoc_contact() {
        return soc_contact;
    }

    public String getSoc_email() {
        return soc_email;
    }

    public String getSoc_adrs() {
        return soc_adrs;
    }

    public int getUserId() {
        return userId;
    }

    public String getCreted_at() {
        return creted_at;
    }
}
