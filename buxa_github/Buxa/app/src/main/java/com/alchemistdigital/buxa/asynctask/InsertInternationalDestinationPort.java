package com.alchemistdigital.buxa.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.GetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.InternationalDestinationPorts;

import java.util.ArrayList;

/**
 * Created by user on 10/12/2016.
 */
public class InsertInternationalDestinationPort extends AsyncTask<String, String, ArrayList<String>> {
    Context context;
    private DatabaseClass dbHelper;

    public InsertInternationalDestinationPort(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> ids = new ArrayList<String>();

        dbHelper = new DatabaseClass(context);

        InternationalDestinationPorts interDestiPorts = new InternationalDestinationPorts();
        ArrayList portName = interDestiPorts.getPortName();
        ArrayList portCountry = interDestiPorts.getPortCountry();

        for (int p = 0 ; p < portName.size() ; p++ ) {
            // insert all international destination ports
            int i = dbHelper.insertInterDestiPorts(portName.get(p).toString(), portCountry.get(p).toString());
            ids.add(""+i);
        }

        return ids;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        // close database in synchronized condition
        dbHelper.closeDB();

        GetSharedPreference getPreference = new GetSharedPreference(context);
        int uId = getPreference.getLoginId(context.getResources().getString(R.string.loginId));
        String token = getPreference.getFCMRegId(context.getResources().getString(R.string.FCM_RegId));

        new UpdateGCMID(context, token, uId);

    }
}
