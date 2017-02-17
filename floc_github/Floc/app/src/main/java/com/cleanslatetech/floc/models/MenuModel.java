package com.cleanslatetech.floc.models;

import java.util.ArrayList;

/**
 * Created by pimpu on 2/14/2017.
 */

public class MenuModel {
    private String menuName;
    private ArrayList<SubMenuModels> arrayListSubMenuName;

    public MenuModel() {
    }

    public MenuModel(String menuName) {
        this.menuName = menuName;
    }

    public MenuModel(String menuName, ArrayList<SubMenuModels> arrayListSubMenuName) {
        this.menuName = menuName;
        this.arrayListSubMenuName = arrayListSubMenuName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public ArrayList<SubMenuModels> getArrayListSubMenuName() {
        return arrayListSubMenuName;
    }

    public void setArrayListSubMenuName(ArrayList<SubMenuModels> arrayListSubMenuName) {
        this.arrayListSubMenuName = arrayListSubMenuName;
    }
}
