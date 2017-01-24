package com.cleanslatetech.floc.asynctask;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.SelectInterestAdapter;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.cleanslatetech.floc.activities.SelectInterestsActivity.gotoNext;
import static com.cleanslatetech.floc.activities.SelectInterestsActivity.mActionBarToolbar;

/**
 * Created by pimpu on 1/24/2017.
 */
public class GetInterestCategoryAsyncTask {
    private Context context;
    private ProgressDialog prgDialog;
    private int iCounter=5;
    private GridView selectInterestGridview;

    public GetInterestCategoryAsyncTask(Context context, GridView selectInterestGridview) {
        this.context = context;
        this.selectInterestGridview = selectInterestGridview;

        // set pickup interest text to textview
        setPickupInterestText(iCounter);

        postData();
    }

    private void postData() {
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(context);
        // Set Progress Dialog Text
        prgDialog.setMessage("getting interest ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        invokeWS(context);
    }

    private void invokeWS(final Context context) {
        // Show Progress Dialog
        prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.GET_INTEREST_CATEGORY_SERVER_URL, null, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                prgDialog.cancel();
                try{
                    System.out.println(json);

                    populateGridview(json.getJSONArray("GetCategory"));

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
                        System.out.println(errorResponse);
                        if (errorResponse == null) {
                            Toast.makeText(context,"Sorry for inconvenience. Please, Try again.",Toast.LENGTH_LONG).show();
                            return;
                        }

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

    private void populateGridview(JSONArray getCategory) {
        // set adapter for interests grid view
        SelectInterestAdapter adapter = new SelectInterestAdapter(context, getCategory);
        selectInterestGridview.setAdapter(adapter);

        // video play when video thumbnail get click.
        final SelectInterestAdapter finalImageLoadAdapter = adapter;

        selectInterestGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // used for change background resources i.e. border to selected item
                List<Integer> iArraySelectedPositions = finalImageLoadAdapter.iArraySelectedPositions;

                if( iArraySelectedPositions.contains(position) && (iCounter >= 0 ) ) {
                    iArraySelectedPositions.remove(iArraySelectedPositions.indexOf(position));
                    iCounter++;
                    setPickupInterestText(iCounter);
                }
                else if(iCounter <= 5 && iCounter > 0 ) {
                    iCounter--;
                    setPickupInterestText(iCounter);
                    iArraySelectedPositions.add(position);
                }
                finalImageLoadAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setPickupInterestText(int iCounter) {
        String strInterestText =  String.format(context.getResources().getString(R.string.strPick5Interests), iCounter);
        if( iCounter == 0 ) {
//            tvSelectInterestText.setText(R.string.strThanking);
            mActionBarToolbar.setTitle(R.string.strThanking);
            gotoNext.setVisibility(View.VISIBLE);

        }
        else {
            mActionBarToolbar.setTitle(strInterestText);
            gotoNext.setVisibility(View.GONE);
//            tvSelectInterestText.setText(strInterestText);
        }
        ((AppCompatActivity)context).setSupportActionBar(mActionBarToolbar);
    }

}
