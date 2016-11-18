package com.alchemistdigital.buxa.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.CustomClearanceLocation;
import com.alchemistdigital.buxa.model.PackageTypeModel;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonUtilities;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by user on 9/10/2016.
 */
public class InsertPackagingTypeAsyncTask extends AsyncTask<String, String, ArrayList<String>> {
    Context context;
    JSONArray packageType;
    DatabaseClass databaseClass;

    public InsertPackagingTypeAsyncTask(Context context, JSONArray packageType) {
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

        GetSharedPreference getPreference = new GetSharedPreference(context);
        int uId = getPreference.getLoginId(context.getResources().getString(R.string.loginId));
        String token = getPreference.getFCMRegId(context.getResources().getString(R.string.FCM_RegId));

        new UpdateGCMID(context, token, uId);

    }
}
