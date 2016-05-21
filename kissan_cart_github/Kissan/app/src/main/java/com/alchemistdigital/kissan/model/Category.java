package com.alchemistdigital.kissan.model;

/**
 * Created by user on 5/17/2016.
 */
public class Category {
    private int id;
    private int serverId;
    private int category_status;
    private String creted_at;
    private int product_group_id_InCategory;
    private String category_name;
    private String category_image;

    public Category() {
    }

    public Category(int serverId, int category_status, int product_group_id_InCategory, String category_name, String category_image) {
        this.serverId = serverId;
        this.category_status = category_status;
        this.product_group_id_InCategory = product_group_id_InCategory;
        this.category_name = category_name;
        this.category_image = category_image;
    }

    @Override
    public String toString() {
        return this.category_name;
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

    public int getCategory_status() {
        return category_status;
    }

    public void setCategory_status(int category_status) {
        this.category_status = category_status;
    }

    public String getCreted_at() {
        return creted_at;
    }

    public void setCreted_at(String creted_at) {
        this.creted_at = creted_at;
    }

    public int getProduct_group_id_InCategory() {
        return product_group_id_InCategory;
    }

    public void setProduct_group_id_InCategory(int product_group_id_InCategory) {
        this.product_group_id_InCategory = product_group_id_InCategory;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }
}
