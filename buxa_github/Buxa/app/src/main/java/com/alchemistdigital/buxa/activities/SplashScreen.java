package com.alchemistdigital.buxa.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alchemistdigital.buxa.R;

public class SplashScreen extends Fragment {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_splash_screen, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                try {
                    startActivity(new Intent(getActivity(), WelcomeActivity.class));
                    System.out.println("SplashScreen(OnResume)");
                    getActivity().finish();
                }
                catch (Exception e){
                    System.err.println("SplashScreen(OnResume): "+e.getMessage());
                }
            }
        }, SPLASH_TIME_OUT);

        super.onResume();
    }
}
