package com.alchemistdigital.buxa.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.activities.SelectServiceActivity;
import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.model.CustomClearanceLocation;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 8/29/2016.
 */
public class GetAllCustomLoaction {
    private static ProgressDialog prgDialog;

    public static void getCL(final Context context, String url) {
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("Logging ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        // Show Progress Dialog
        prgDialog.show();

        RestClient.get(url, null, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                prgDialog.cancel();
                try {
                    JSONObject json = new JSONObject(response);

                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);
                    if (error) {
                        Toast.makeText(context,json.getString(CommonVariables.TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    } else {

                        DatabaseClass databaseClass = new DatabaseClass(context);

                        JSONArray arrayCustomLoc = json.getJSONArray("customLocation");

                        for (int i = 0 ; i < arrayCustomLoc.length(); i++ ) {
                            int clServerId = arrayCustomLoc.getJSONObject(i).getInt("id");
                            int clCategoryId = arrayCustomLoc.getJSONObject(i).getInt("CLCid");
                            String name = arrayCustomLoc.getJSONObject(i).getString("name");
                            String location = arrayCustomLoc.getJSONObject(i).getString("location");
                            String state = arrayCustomLoc.getJSONObject(i).getString("state");
                            int status = arrayCustomLoc.getJSONObject(i).getInt("status");

                            long l = databaseClass.insertCustomLoaction(new CustomClearanceLocation(clServerId, clCategoryId, name, location, state, status));
                            System.out.println("custom loaction id: "+l);
                        }

                        // close database in synchronized condition
                        databaseClass.closeDB();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    System.out.println("Requested resource not found");
                    Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    System.out.println("Something went wrong at server end");
                    Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    System.out.println("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                    Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
