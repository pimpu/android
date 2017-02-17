package com.cleanslatetech.floc.asynctask;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.FlocDescriptionActivity;
import com.cleanslatetech.floc.adapter.AllFlocRecyclerAdapter;
import com.cleanslatetech.floc.utilities.CommonVariables;
import com.cleanslatetech.floc.utilities.RecyclerItemClickListener;
import com.cleanslatetech.floc.utilities.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by pimpu on 1/31/2017.
 */
public class GetFlocAsyncTask {
    private ProgressBar progressBar;
    private AppCompatButton btnRefresh;
    private Context context;
    private RecyclerView recyclerFloc;

    public GetFlocAsyncTask(final Context context, RecyclerView recyclerFloc, ProgressBar progressBar, final AppCompatButton btnRefresh) {
        this.context = context;
        this.recyclerFloc = recyclerFloc;

        // Instantiate Progress Dialog object
        this.progressBar = progressBar;
        this.progressBar.getIndeterminateDrawable().setColorFilter(
                context.getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        this.btnRefresh = btnRefresh;
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh.setVisibility(View.GONE);
                getData();
            }
        });

    }

    public void getData() {
        progressBar.setVisibility(View.VISIBLE);
        invokeWS(context);
    }

    private void invokeWS(final Context context) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.get(CommonVariables.GET_ALL_EVENTS_SERVER_URL, null, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                progressBar.setVisibility(View.GONE);
                recyclerFloc.setVisibility(View.VISIBLE);
                try{
                    System.out.println(json);

                    // manipulate gride view
                    populateAllEventRecyclerview(json.getJSONArray("Event"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                System.out.println("status code: "+statusCode);
                System.out.println("responseString: "+responseString);
                Toast.makeText(context, "Error "+statusCode+" : "+responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressBar.setVisibility(View.GONE);
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

                            btnRefresh.setVisibility(View.VISIBLE);

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

    private void populateAllEventRecyclerview(final JSONArray getEvents) {
        // set adapter for interests grid view
        final RecyclerView.Adapter allFlocRecyclerAdapter = new AllFlocRecyclerAdapter(context, getEvents);
        recyclerFloc.setAdapter(allFlocRecyclerAdapter);
        recyclerFloc.setLayoutManager(new LinearLayoutManager(context));

        recyclerFloc.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerFloc ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        try {
                            Intent intentFlocDesc = new Intent(context, FlocDescriptionActivity.class);
                            intentFlocDesc.putExtra("floc_data", getEvents.getJSONObject(position).toString());
                            context.startActivity(intentFlocDesc);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }
}
