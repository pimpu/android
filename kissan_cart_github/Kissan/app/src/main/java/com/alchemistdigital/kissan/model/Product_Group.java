package com.alchemistdigital.kissan.model;

/**
 * Created by user on 5/17/2016.
 */
public class Product_Group {
    private int id;
    private int serverId;
    private int group_status;
    private String creted_at;
    private String group_name;

    public Product_Group() {
    }

    public Product_Group(int serverId, String group_name, int group_status) {
        this.serverId = serverId;
        this.group_name = group_name;
        this.group_status = group_status;
    }

    @Override
    public String toString() {
        return  this.group_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getGroup_status() {
        return group_status;
    }

    public void setGroup_status(int group_status) {
        this.group_status = group_status;
    }

    public String getCreted_at() {
        return creted_at;
    }

    public void setCreted_at(String creted_at) {
        this.creted_at = creted_at;
    }
}
