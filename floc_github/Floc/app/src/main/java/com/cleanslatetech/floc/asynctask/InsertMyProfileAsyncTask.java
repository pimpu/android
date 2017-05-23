package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.BaseAppCompactActivity;
import com.cleanslatetech.floc.activities.MyProfileActivity;
import com.cleanslatetech.floc.models.MyProfileModel;
import com.cleanslatetech.floc.sharedprefrencehelper.SetSharedPreference;
import com.cleanslatetech.floc.utilities.CommonUtilities;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 3/10/2017.
 */

public class InsertMyProfileAsyncTask {
    private ProgressDialog prgDialog;
    private Context context;
    private MyProfileModel myProfileModel;

    public InsertMyProfileAsyncTask(Context context, MyProfileModel myProfileModel, ProgressDialog prgDialog) {
        this.context = context;
        this.myProfileModel = myProfileModel;
        this.prgDialog = prgDialog;
    }

    public void postData() {
        RequestParams params;
        params = new RequestParams();

        params.put("Id", myProfileModel.getUserId());
        params.put("UserName",myProfileModel.getUserName());
        params.put("FirstName",myProfileModel.getFirstName());
        params.put("LastName",myProfileModel.getLastName());
        params.put("MiddleName",myProfileModel.getMiddleName());
        params.put("Gender",myProfileModel.getGender());
        params.put("Contact",myProfileModel.getContact());
        params.put("EmailId",myProfileModel.getEmailId());
        params.put("Profession",myProfileModel.getProfession());
        params.put("City",myProfileModel.getCity());
        params.put("State",myProfileModel.getState());
        params.put("Country",myProfileModel.getCountry());
        params.put("PinCode",myProfileModel.getPinCode());
        params.put("ProfilePic",myProfileModel.getProfilePic());
        params.put("BirthDate",myProfileModel.getBirthDate());
        params.put("BankName",myProfileModel.getBankName());
        params.put("Branch",myProfileModel.getBranch());
        params.put("IFSC",myProfileModel.getIFSC());
        params.put("Account",myProfileModel.getAccount());
        params.put("URL",myProfileModel.getURL());

        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.USER_PROFILE_SERVER_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                prgDialog.cancel();
                System.out.println(json);

                new SetSharedPreference(context).setString(context.getResources().getString(R.string.shrdMyProfile), json.toString());
//                ((BaseAppCompactActivity)context).createRightPopupMenu();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                prgDialog.cancel();
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                CommonUtilities.customToast(context, "Error "+statusCode+" : "+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // When Http response code is '404'
                if (statusCode == 404) {
                    prgDialog.cancel();

                    System.out.println("Requested resource not found");
                    CommonUtilities.customToast(context, "Requested resource not found");
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    prgDialog.cancel();
                    System.out.println("Something went wrong at server end");
                    CommonUtilities.customToast(context, "Something went wrong at server end");
                }
                // When Http response code other than 404, 500
                else {
                    try {
                        System.out.println(errorResponse);
                        if (errorResponse == null) {
//                            CommonUtilities.customToast(context,"Sorry for inconvenience. Please, Try again.");
                            postData();
                            return;
                        }
                        prgDialog.cancel();

                        if( errorResponse.getBoolean("error") ) {
                            System.out.println(errorResponse.getString("message"));
                            CommonUtilities.customToast(context, errorResponse.getString("message"));
                        }
                        else {
                            System.out.println("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                            CommonUtilities.customToast(context, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
