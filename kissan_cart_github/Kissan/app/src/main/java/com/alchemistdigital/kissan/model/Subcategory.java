package com.alchemistdigital.kissan.model;

/**
 * Created by user on 5/17/2016.
 */
public class Subcategory {
    private int id;
    private int serverId;
    private int category_id;
    private int product_group_id_InSubcategory;
    private String subcategory_name;
    private String subcategory_image;
    private int subcategory_status;
    private String creted_at;

    public Subcategory() {
    }

    public Subcategory(int serverId, int category_id, int product_group_id_InSubcategory, String subcategory_name, String subcategory_image, int subcategory_status) {
        this.serverId = serverId;
        this.category_id = category_id;
        this.product_group_id_InSubcategory = product_group_id_InSubcategory;
        this.subcategory_name = subcategory_name;
        this.subcategory_image = subcategory_image;
        this.subcategory_status = subcategory_status;
    }

    @Override
    public String toString() {
        return this.subcategory_name;
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getProduct_group_id_InSubcategory() {
        return product_group_id_InSubcategory;
    }

    public void setProduct_group_id_InSubcategory(int product_group_id_InSubcategory) {
        this.product_group_id_InSubcategory = product_group_id_InSubcategory;
    }

    public String getSubcategory_name() {
        return subcategory_name;
    }

    public void setSubcategory_name(String subcategory_name) {
        this.subcategory_name = subcategory_name;
    }

    public String getSubcategory_image() {
        return subcategory_image;
    }

    public void setSubcategory_image(String subcategory_image) {
        this.subcategory_image = subcategory_image;
    }

    public int getSubcategory_status() {
        return subcategory_status;
    }

    public void setSubcategory_status(int subcategory_status) {
        this.subcategory_status = subcategory_status;
    }

    public String getCreted_at() {
        return creted_at;
    }

    public void setCreted_at(String creted_at) {
        this.creted_at = creted_at;
    }
}
