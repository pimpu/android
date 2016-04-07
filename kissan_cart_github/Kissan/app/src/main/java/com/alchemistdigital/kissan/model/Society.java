package com.alchemistdigital.kissan.model;

/**
 * Created by user on 3/2/2016.
 */
public class Society {
    private int id;
    private int serverId;
    private String creted_at;
    private int userId;
    private String soc_name;
    private String soc_contact;
    private String soc_email;
    private int soc_status;
    private String soc_adrs;

    private String soc_offline_action;

    public Society() {
    }

    public Society(int serverId, int userId, String soc_name, String soc_contact, String soc_email, String soc_adrs,int soc_status) {
        this.serverId = serverId;
        this.userId = userId;
        this.soc_name = soc_name;
        this.soc_contact = soc_contact;
        this.soc_email = soc_email;
        this.soc_adrs = soc_adrs;
        this.soc_status = soc_status;
    }

    @Override
    public String toString() {
        return "{" +
                " \"id\":\"" + id + '\"' +
                ", \"serverId\":\"" + serverId + '\"' +
                ", \"userId\":\"" + userId + '\"' +
                ", \"soc_name\":\"" + soc_name + '\"' +
                ", \"soc_contact\":\"" + soc_contact + '\"' +
                ", \"soc_email\":\"" + soc_email + '\"' +
                ", \"soc_adrs\":\"" + soc_adrs + '\"' +
                ", \"soc_offline_action\":\"" + soc_offline_action + '\"' +
                ", \"soc_status\":\"" + soc_status + '\"' +
                '}';
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

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public void setSoc_offline_action(String soc_offline_action) {
        this.soc_offline_action = soc_offline_action;
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

    public int getServerId() {
        return serverId;
    }

    public String getSoc_offline_action() {
        return soc_offline_action;
    }
}
