package info.alchemistdigital.e_carrier.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import info.alchemistdigital.e_carrier.R;

import static info.alchemistdigital.e_carrier.utilities.CommonUtilities.distanceCalculatorFromLatLong;

/**
 * Created by user on 1/29/2016.
 */
public class DistanceCalculator extends Service{
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManagerForDistCalculate;
    public DistCalculateLocationListener listener;
    private Location previousBestLocation = null;
    public double previousDist = 0D;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 5000; // 5 seconds

    SharedPreferences sharedPreferenceLogin;
    SharedPreferences.Editor editor;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        sharedPreferenceLogin = getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
        editor = sharedPreferenceLogin.edit();

        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        locationManagerForDistCalculate = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new DistCalculateLocationListener();

        locationManagerForDistCalculate.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);

        locationManagerForDistCalculate.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
    }

    @Override
    public void onDestroy() {
        locationManagerForDistCalculate.removeUpdates(listener);

        super.onDestroy();
    }

    public class DistCalculateLocationListener implements LocationListener {

        public void onLocationChanged(final Location location){

            if(isBetterLocation(location, previousBestLocation)) {
                if (previousBestLocation != null) {
                    double distanceBtwnLatLong = distanceCalculatorFromLatLong(location.getLatitude(), location.getLongitude(), previousBestLocation.getLatitude(), previousBestLocation.getLongitude());

                    previousDist += distanceBtwnLatLong;

                    Toast.makeText(getApplicationContext(), "" + previousDist, Toast.LENGTH_LONG).show();

                    editor.putString(getResources().getString(R.string.distanceBtwnLatLong), "" + previousDist);
                    editor.commit();
                }
                previousBestLocation = location;
            }
        }

        public void onProviderDisabled(String provider){
            Toast.makeText(getApplicationContext(), "Gps Disabled,switch on gps ", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider){
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras){

        }

    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }



    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
