package com.cleanslatetech.floc.interfaces;

import org.json.JSONArray;

/**
 * Created by pimpu on 3/5/2017.
 */

public interface InterfaceAllRecent_Current_Archive_Event {
    void getAllEvents(JSONArray jsonArray);
    void getAllRecent(JSONArray jsonArray);
    void getAllArchive(JSONArray jsonArray);
    void getAllChannle(JSONArray jsonArray);
}
