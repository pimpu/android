package com.alchemistdigital.kissan.utilities;

/**
 * Created by user on 3/22/2016.
 */
public enum offlineActionModeEnum {
    INSERT{
        @Override
        public String toString() {
            return "Insert";
        }
    },
    UPDATE{
        @Override
        public String toString() {
            return "Update";
        }
    },
    DELETE{
        @Override
        public String toString() {
            return "Delete";
        }
    };


}
