package com.alchemistdigital.buxa.utilities;

import com.alchemistdigital.buxa.activities.ShipmentAreaActivity;

/**
 * Created by pimpu on 1/9/2017.
 */

public class ShipAreaVariableSingleton {
    private static ShipAreaVariableSingleton oInstance= null;

    public String shipAreaName;

    public ShipAreaVariableSingleton() {
    }

    public static synchronized ShipAreaVariableSingleton getInstance() {
        if(oInstance == null) {
            oInstance = new ShipAreaVariableSingleton();
        }
        return oInstance;
    }



}
