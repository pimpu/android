package com.alchemistdigital.buxa.utilities;

/**
 * Created by user on 9/14/2016.
 */
public enum  enumServices {
    TRANSPORTATION {
        @Override
        public String toString() {
            return "Transportation";
        }
    },
    CUSTOM_CLEARANCE{
        @Override
        public String toString() {
            return "Custom Clearance";
        }
    },
    FREIGHT_FORWARDING {
        @Override
        public String toString() {
            return "Freight Forwarding";
        }
    }
}
