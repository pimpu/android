package com.alchemistdigital.buxa.asynctask;

import android.content.Context;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.model.ShipmentTypeModel;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Pimpu on 8/30/2016.
 */
public class GetAllShipmentType {

    public static void getShipmentType(final Context context, String url) {

        RestClient.get(url, null, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                try {
//                    JSONObject json = new JSONObject(response);

                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);
                    if (error) {
                        Toast.makeText(context,json.getString(CommonVariables.TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    } else {

                        DatabaseClass databaseClass = new DatabaseClass(context);

                        JSONArray arrayTerm = json.getJSONArray("shipmentType");
                        int commodityRows = json.getInt("commodityRowCount");
                        for (int i = 0 ; i < arrayTerm.length(); i++ ) {
                            int termServerId = arrayTerm.getJSONObject(i).getInt("id");
                            String termName = arrayTerm.getJSONObject(i).getString("name");
                            int termStatus = arrayTerm.getJSONObject(i).getInt("status");

                            databaseClass.insertShipmentType(new ShipmentTypeModel(termServerId, termName, termStatus));
                        }

                        // close database in synchronized condition
                        databaseClass.closeDB();

                        // get all commodities.
                        GetAllCommodity.getCommodities(context, CommonVariables.QUERY_COMMODITY_SERVER_URL, commodityRows);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                Toast.makeText(context, "Error "+statusCode+" : "+responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
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
                    try {
                        if( errorResponse.getBoolean("error") ) {
                            System.out.println(errorResponse.getString("message"));
                            Toast.makeText(context, errorResponse.getString("message"),Toast.LENGTH_LONG).show();
                        }
                        else {
                            System.out.println("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                            Toast.makeText(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
