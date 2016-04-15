package info.alchemistdigital.e_carrier.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.asynctask.DriverTrackAsyncTask;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;

public class MapsTrackingActivity extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    int enquiryId;

    public MapsTrackingActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_maps_tracking, container, false);
        enquiryId = getArguments().getInt("enquiryId");

        new extendDriverTrack(getActivity(),enquiryId).execute();

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.driver_track_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menuRefresh){
            new extendDriverTrack(getActivity(),enquiryId).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setMap(Double lats,Double longs){

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(lats, longs));

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lats, longs)).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private class extendDriverTrack extends DriverTrackAsyncTask {
        public extendDriverTrack(FragmentActivity activity, int enquiryId) {
            super(activity,enquiryId);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // check log cat fro response

            try {
                Log.d("Driver Tracking Data", result.toString());

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                if(result.contains("Error occurred!")){
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = new JSONObject(result);

                // check for success tag
                int success = json.getInt(CommonUtilities.TAG_SUCCESS);
                if(success == 0) {
                    String message = json.getString(CommonUtilities.TAG_MESSAGE);
                    HashMap<String ,String> keyValue=CommonUtilities.convertToKeyValuePair(message);

                    Double lats=Double.valueOf(keyValue.get("latitude"));
                    Double longs=Double.valueOf(keyValue.get("longitude"));
                    setMap(lats,longs);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
