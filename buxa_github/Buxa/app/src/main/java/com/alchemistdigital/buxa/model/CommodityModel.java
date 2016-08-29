package com.alchemistdigital.buxa.model;

/**
 * Created by Pimpu on 8/29/2016.
 */
public class CommodityModel {
    private int commodityId ;
    private int serverId ;
    private String name;
    private int  status ;
    private String createdAt ;

    public CommodityModel() {
    }

    public CommodityModel(int serverId, String name, int status) {
        this.serverId = serverId;
        this.name = name;
        this.status = status;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
