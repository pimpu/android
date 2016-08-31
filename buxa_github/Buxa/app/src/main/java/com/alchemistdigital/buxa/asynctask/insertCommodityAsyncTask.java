package com.alchemistdigital.buxa.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.model.CommodityModel;
import com.alchemistdigital.buxa.utilities.CommonVariables;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by user on 8/30/2016.
 */
public class insertCommodityAsyncTask extends AsyncTask<String, String, ArrayList<String>> {

    Context context;
    JSONArray commodities;
    DatabaseClass databaseClass;
    public insertCommodityAsyncTask(Context context, JSONArray commodities) {
        this.context = context;
        this.commodities = commodities;
        databaseClass = new DatabaseClass(context);
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> ids = new ArrayList<String>();
        try {

            for (int i = 0 ; i < commodities.length(); i++ ) {

                int commodityServerId = commodities.getJSONObject(i).getInt("id");
                String commodityName = commodities.getJSONObject(i).getString("comodity");
                int commodityStatus = commodities.getJSONObject(i).getInt("status");

                long l = databaseClass.insertCommodity(new CommodityModel(commodityServerId, commodityName, commodityStatus));
                ids.add(""+l);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return ids;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        System.out.println("Commodities: "+result);

        // close database in synchronized condition
        databaseClass.closeDB();

        // get all custom loaction from server.
//        GetAllCustomLoaction.getCL(context, CommonVariables.QUERY_CUSTOM_LOACTION_SERVER_URL);
        GetAllShipmentType.getShipmentTerm(context, CommonVariables.QUERY_SHIPMENT_TYPE_SERVER_URL);
    }
}
