package com.alchemistdigital.buxa.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.TransportationModel;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by user on 9/19/2016.
 */
public class InsertTransportationAsyncTask {
    private static ProgressDialog prgDialog;
    private static GetSharedPreference getSharedPreference;

    public static void postTransportationData(Context context, TransportationModel transportationModel) {

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("inserting data ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        getSharedPreference = new GetSharedPreference(context);

        RequestParams params;
        params = new RequestParams();

//        System.out.println(transportationModel.toString());

        params.put("transportdata", transportationModel.toString());

        invokeWS(context, params, transportationModel);

    }

    private static void invokeWS(final Context context, RequestParams params, final TransportationModel transportationModel) {
        // Show Progress Dialog
        prgDialog.show();

        String apiKeyHeader = getSharedPreference.getApiKey(context.getResources().getString(R.string.apikey));

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.postWithHeader(CommonVariables.INSERT_TRANSPOERTATION_SERVER_URL, apiKeyHeader, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                prgDialog.cancel();
                try{
                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);

                    if (error) {
                        Toast.makeText(context, json.getString(CommonVariables.TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        System.out.println(json.getString(CommonVariables.TAG_MESSAGE));
                    } else {
                        DatabaseClass dbHelper = new DatabaseClass(context);

                        TransportationModel dbInsertTransportData = new TransportationModel(
                                json.getInt("id"),
                                transportationModel.getCommodityServerId(),
                                transportationModel.getDimenLength(),
                                transportationModel.getDimenHeight(),
                                transportationModel.getDimenWidth(),
                                transportationModel.getShipmentType(),
                                transportationModel.getNoOfPack(),
                                transportationModel.getPackType(),
                                transportationModel.getPickUp(),
                                transportationModel.getDrop(),
                                "",
                                transportationModel.getAvailOption(),
                                transportationModel.getStatus(),
                                transportationModel.getCreatedAt(),
                                transportationModel.getBookingId(),
                                transportationModel.getMeasurement(),
                                transportationModel.getGrossWeight()
                        );

                        int i = dbHelper.insertTransportation(dbInsertTransportData);

                        if(i != 0) {
                            Intent intent = new Intent(CommonVariables.DISPLAY_MESSAGE_ACTION);
                            intent.putExtra(CommonVariables.EXTRA_MESSAGE, "gotoNextActivity");
                            intent.putExtra("transportData",transportationModel);
                            context.sendBroadcast(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialog.cancel();
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                Toast.makeText(context, "Error "+statusCode+" : "+responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialog.cancel();
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