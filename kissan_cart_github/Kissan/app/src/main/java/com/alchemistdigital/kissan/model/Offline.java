package com.alchemistdigital.kissan.model;

/**
 * Created by user on 3/22/2016.
 */
public class Offline {
    private int offline_id;
    private String offline_table_name;
    private int offline_row_id;
    private String offline_row_action;


    public Offline() {
    }

    public Offline(String offline_table_name, int offline_row_id, String offline_row_action, String offline_row_creationTime) {
        this.offline_table_name = offline_table_name;
        this.offline_row_id = offline_row_id;
        this.offline_row_action = offline_row_action;
        this.offline_row_creationTime = offline_row_creationTime;
    }

    public String getOffline_row_creationTime() {
        return offline_row_creationTime;
    }

    public void setOffline_row_creationTime(String offline_row_creationTime) {
        this.offline_row_creationTime = offline_row_creationTime;
    }

    public int getOffline_id() {
        return offline_id;
    }

    public void setOffline_id(int offline_id) {
        this.offline_id = offline_id;
    }

    public String getOffline_table_name() {
        return offline_table_name;
    }

    public void setOffline_table_name(String offline_table_name) {
        this.offline_table_name = offline_table_name;
    }

    public int getOffline_row_id() {
        return offline_row_id;
    }

    public void setOffline_row_id(int offline_row_id) {
        this.offline_row_id = offline_row_id;
    }

    public String getOffline_row_action() {
        return offline_row_action;
    }

    public void setOffline_row_action(String offline_row_action) {
        this.offline_row_action = offline_row_action;
    }

    private String offline_row_creationTime;
}
