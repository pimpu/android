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
    int start, limit, commodityRowsCount;
    String url;
    public insertCommodityAsyncTask(Context context, JSONArray commodities, int start, int limit, int commodityRowsCount, String url) {
        this.context = context;
        this.commodities = commodities;
        this.start = start;
        this.limit = limit;
        this.commodityRowsCount = commodityRowsCount;
        this.url = url;
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
        // close database in synchronized condition
        databaseClass.closeDB();

        start = start + 100;
        if ((start % commodityRowsCount) >= limit) {
            GetAllCommodity.getData(context, url, start, limit);
        }
        // get all transport type from server.
        GetAllTransportType.getTransportType(context, CommonVariables.QUERY_TRANSPORT_TYPE_SERVER_URL);

    }
}
