package com.cleanslatetech.floc.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.activities.CreateFlocActivity;
import com.cleanslatetech.floc.activities.CreateFlocActivitySecond;
import com.cleanslatetech.floc.activities.HomeActivity;
import com.cleanslatetech.floc.models.EventsModel;
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

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignIn;

/**
 * Created by pimpu on 2/26/2017.
 */
public class CreateFlocAsyncTask {
    private ProgressDialog prgDialog;
    private Context context;
    private EventsModel eventsModel;

    public CreateFlocAsyncTask(Context context, EventsModel eventsModel, ProgressDialog prgDialog) {
        this.context = context;
        this.eventsModel = eventsModel;
        this.prgDialog = prgDialog;
    }

    public void postData() {

        RequestParams params;
        params = new RequestParams();

        params.put("EventId", eventsModel.getEventId());
        params.put("EventCreatorId",eventsModel.getEventCreatorId());
        params.put("EventName",eventsModel.getEventName());
        params.put("EventCategory",eventsModel.getEventCategory());
        params.put("EventDescription",eventsModel.getEventDescription());
        params.put("EventPicture",eventsModel.getEventPicture());
        params.put("EventStartDate",eventsModel.getEventStartDate());
        params.put("EventStartHour",eventsModel.getEventStartHour());
        params.put("EventStartMin",eventsModel.getEventStartMin());
        params.put("EventEndDate",eventsModel.getEventEndDate());
        params.put("EventEndHour",eventsModel.getEventEndHour());
        params.put("EventEndMin",eventsModel.getEventEndMin());
        params.put("EventPriceType",eventsModel.getEventPriceType());
        params.put("EventPrice",eventsModel.getEventPrice());
        params.put("EventMembers",eventsModel.getEventMembers());
        params.put("EventCity",eventsModel.getEventCity());
        params.put("EventArea",eventsModel.getEventArea());
        params.put("EventAddress",eventsModel.getEventAddress());
        params.put("EventState",eventsModel.getEventState());
        params.put("EventCountry",eventsModel.getEventCountry());
        params.put("EventUrl",eventsModel.getEventUrl());
        params.put("EventReason",eventsModel.getEventReason());
        params.put("EventPublish",eventsModel.getEventPublish());
        params.put("ManagementService",eventsModel.getManagementService());
        params.put("ConciergeServices",eventsModel.getConciergeServices());
        params.put("EventStatus",eventsModel.getEventStatus());
        params.put("IsExclusive",eventsModel.getIsExclusive());
                
        invokeWS(context, params);
    }

    private void invokeWS(final Context context, RequestParams params) {
        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.CREATE_FLOC_SERVER_URL, params, new JsonHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                try{
                    System.out.println(json);

                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);
                    JSONArray jsonArray = json.getJSONArray(CommonVariables.TAG_MESSAGE);

                    if (error) {
                        for( int i = 0 ; i < jsonArray.length(); i++) {
                            String msg = jsonArray.getJSONObject(i).getString(CommonVariables.TAG_MESSAGE_OBJ);
                            System.out.println(msg);
                            CommonUtilities.customToast(context, msg);
                        }
                    } else {
                        new GetAllEventsAsyncTask(context, prgDialog).getData();
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
                CommonUtilities.customToast(context, "Error "+statusCode+" : "+responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                prgDialog.cancel();
                // When Http response code is '404'
                if (statusCode == 404) {
                    System.out.println("Requested resource not found");
                    CommonUtilities.customToast(context, "Requested resource not found");
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    System.out.println("Something went wrong at server end");
                    CommonUtilities.customToast(context, "Something went wrong at server end");
                }
                // When Http response code other than 404, 500
                else {
                    try {
                        System.out.println(errorResponse);
                        if (errorResponse == null) {
                            CommonUtilities.customToast(context,"Sorry for inconvenience. Please, Try again.");
                            return;
                        }

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