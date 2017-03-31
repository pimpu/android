package com.cleanslatetech.floc.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.SocialLoginAsyncTask;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import static com.cleanslatetech.floc.utilities.CommonUtilities.handleIntentWhenSignOut;

/**
 * Created by pimpu on 1/20/2017.
 */
public class FacebookCallBackMethod implements FacebookCallback<LoginResult> {
    Context context;

    public FacebookCallBackMethod(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        getFacebookProfileData(loginResult.getAccessToken());
    }

    public void getFacebookProfileData(AccessToken accessToken) {
       ((AppCompatActivity)context).setContentView(R.layout.activity_splash_screen);

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                // Get facebook data from login
                Bundle bFacebookData = CommonUtilities.getFacebookData(object);
                System.out.println("profile_pic: "+bFacebookData.getString("profile_pic"));
                System.out.println("idFacebook: "+bFacebookData.getString("idFacebook"));
                System.out.println("first_name: "+bFacebookData.getString("first_name"));
                System.out.println("last_name: "+bFacebookData.getString("last_name"));
                System.out.println("email: "+bFacebookData.getString("email"));
                System.out.println("gender: "+bFacebookData.getString("gender"));

                new SocialLoginAsyncTask(
                        context,
                        bFacebookData.getString("email"),
                        "Facebook",
                        ""+bFacebookData.getString("idFacebook"),
                        bFacebookData.getString("profile_pic")).postData();

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender"); // Parámetros que pedimos a facebook
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {
        System.out.println("fb On cancel");
        // intet for next activity
        handleIntentWhenSignOut(context);
    }

    @Override
    public void onError(FacebookException error) {
        System.out.println("fb On error: "+ error.getMessage());
        // intet for next activity
        handleIntentWhenSignOut(context);
    }
}
