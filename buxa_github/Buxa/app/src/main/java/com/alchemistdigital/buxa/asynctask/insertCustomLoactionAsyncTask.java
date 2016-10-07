package com.alchemistdigital.buxa.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.model.CustomClearanceLocation;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by user on 8/30/2016.
 */
public class insertCustomLoactionAsyncTask extends AsyncTask<String, String, ArrayList<String>> {
    Context context;
    JSONArray customLocation;
    DatabaseClass databaseClass;

    public insertCustomLoactionAsyncTask(Context context, JSONArray customLocation) {
        this.context = context;
        this.customLocation = customLocation;
        databaseClass = new DatabaseClass(context);
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> ids = new ArrayList<String>();

        try {

            for (int i = 0 ; i < customLocation.length(); i++ ) {
                int clServerId = customLocation.getJSONObject(i).getInt("id");
                int clCategoryId = customLocation.getJSONObject(i).getInt("CLCid");
                String name = customLocation.getJSONObject(i).getString("name");
                String location = customLocation.getJSONObject(i).getString("location");
                String state = customLocation.getJSONObject(i).getString("state");
                int status = customLocation.getJSONObject(i).getInt("status");

                long l = databaseClass.insertCustomLoaction(new CustomClearanceLocation(clServerId, clCategoryId, name, location, state, status));
                ids.add(""+l);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ids;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
//        System.out.println("custom loaction id: "+result);
        // close database in synchronized condition
        databaseClass.closeDB();

        /*SetSharedPreference setSharedPreference = new SetSharedPreference(context);
        // it store the Register true value of user for purpose of user is registered with this app.
        setSharedPreference.setBooleanLogin(context.getString(R.string.boolean_login_sharedPref), "true");

        // sent notification to activities that server calling finished
        CommonUtilities.displayMessage(context, "allDefaultDataFetched");*/
    }
}
