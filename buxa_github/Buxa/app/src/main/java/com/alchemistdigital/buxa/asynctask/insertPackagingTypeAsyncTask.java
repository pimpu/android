package com.alchemistdigital.buxa.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.CustomClearanceLocation;
import com.alchemistdigital.buxa.model.PackageTypeModel;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by user on 9/10/2016.
 */
public class insertPackagingTypeAsyncTask extends AsyncTask<String, String, ArrayList<String>> {
    Context context;
    JSONArray packageType;
    DatabaseClass databaseClass;

    public insertPackagingTypeAsyncTask(Context context, JSONArray packageType) {
        this.context = context;
        this.packageType = packageType;
        databaseClass = new DatabaseClass(context);
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> ids = new ArrayList<String>();

        try {

            for (int i = 0 ; i < packageType.length(); i++ ) {
                int serverId = packageType.getJSONObject(i).getInt("id");
                String name = packageType.getJSONObject(i).getString("name");
                int status = packageType.getJSONObject(i).getInt("status");

                long l = databaseClass.insertPackageType(new PackageTypeModel(serverId, name,  status));
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

        SetSharedPreference setSharedPreference = new SetSharedPreference(context);
        // it store the Register true value of user for purpose of user is registered with this app.
        setSharedPreference.setBooleanLogin(context.getString(R.string.boolean_login_sharedPref), "true");

        // sent notification to activities that server calling finished
        CommonUtilities.displayMessage(context, "allDefaultDataFetched");
    }
}
