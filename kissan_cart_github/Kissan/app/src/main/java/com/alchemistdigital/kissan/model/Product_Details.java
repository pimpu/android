package com.alchemistdigital.kissan.model;

/**
 * Created by user on 5/17/2016.
 */
public class Product_Details {
    private int id;
    private int serverId;
    private int subcategory_id_InProductDetails;
    private int category_id_InProductDetails;
    private int product_group_id_InProductDetails;
    private String product_name;
    private String prod_path;
    private String prod_path1;
    private String prod_path2;
    private String prod_path3;
    private String prod_path4;
    private String prod_description;
    private String discount_type;
    private int product_status;
    private String creted_at;

    public Product_Details() {
    }

    public Product_Details(int serverId, int subcategory_id_InProductDetails, int category_id_InProductDetails, int product_group_id_InProductDetails, String product_name, String prod_path, String prod_path1, String prod_path2, String prod_path3, String prod_path4, String prod_description, String discount_type, int product_status) {
        this.serverId = serverId;
        this.subcategory_id_InProductDetails = subcategory_id_InProductDetails;
        this.category_id_InProductDetails = category_id_InProductDetails;
        this.product_group_id_InProductDetails = product_group_id_InProductDetails;
        this.product_name = product_name;
        this.prod_path = prod_path;
        this.prod_path1 = prod_path1;
        this.prod_path2 = prod_path2;
        this.prod_path3 = prod_path3;
        this.prod_path4 = prod_path4;
        this.prod_description = prod_description;
        this.discount_type = discount_type;
        this.product_status = product_status;
    }

    @Override
    public String toString() {
        return this.product_name;
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

    public int getSubcategory_id_InProductDetails() {
        return subcategory_id_InProductDetails;
    }

    public void setSubcategory_id_InProductDetails(int subcategory_id_InProductDetails) {
        this.subcategory_id_InProductDetails = subcategory_id_InProductDetails;
    }

    public int getCategory_id_InProductDetails() {
        return category_id_InProductDetails;
    }

    public void setCategory_id_InProductDetails(int category_id_InProductDetails) {
        this.category_id_InProductDetails = category_id_InProductDetails;
    }

    public int getProduct_group_id_InProductDetails() {
        return product_group_id_InProductDetails;
    }

    public void setProduct_group_id_InProductDetails(int product_group_id_InProductDetails) {
        this.product_group_id_InProductDetails = product_group_id_InProductDetails;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProd_path() {
        return prod_path;
    }

    public void setProd_path(String prod_path) {
        this.prod_path = prod_path;
    }

    public String getProd_path1() {
        return prod_path1;
    }

    public void setProd_path1(String prod_path1) {
        this.prod_path1 = prod_path1;
    }

    public String getProd_path2() {
        return prod_path2;
    }

    public void setProd_path2(String prod_path2) {
        this.prod_path2 = prod_path2;
    }

    public String getProd_path3() {
        return prod_path3;
    }

    public void setProd_path3(String prod_path3) {
        this.prod_path3 = prod_path3;
    }

    public String getProd_path4() {
        return prod_path4;
    }

    public void setProd_path4(String prod_path4) {
        this.prod_path4 = prod_path4;
    }

    public String getProd_description() {
        return prod_description;
    }

    public void setProd_description(String prod_description) {
        this.prod_description = prod_description;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public int getProduct_status() {
        return product_status;
    }

    public void setProduct_status(int product_status) {
        this.product_status = product_status;
    }

    public String getCreted_at() {
        return creted_at;
    }

    public void setCreted_at(String creted_at) {
        this.creted_at = creted_at;
    }
}
