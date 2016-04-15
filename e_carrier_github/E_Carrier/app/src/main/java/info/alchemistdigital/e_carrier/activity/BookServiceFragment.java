package info.alchemistdigital.e_carrier.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.andexert.library.RippleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.services.GetLatLongService;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;
import info.alchemistdigital.e_carrier.utilities.GetAddressFromLatsLong;
import info.alchemistdigital.e_carrier.utilities.Validation;


/**
 * Created by Ravi on 29/07/15.
 */
public class BookServiceFragment extends Fragment implements AdapterView.OnItemSelectedListener,AdapterView.OnItemClickListener {
    String fromAddress,toAddress,deliveryDate,deliveryTime,unit,Type,fromAddressType,vehicleName;
    String weight;
    Button dateButton,timeButton,btnContinue;
    Spinner unitSpinner,vehicleNameSpinner,luggageWeightSpinner;
    RadioGroup typeVehicle,rdfromSelectType;
    EditText txtGpsAddressText,txtVehicleHeight,txtVehicleLength,txtVehicleWidth;
    AutoCompleteTextView fromArea,toArea;
    private ArrayAdapter<String> vehicleNameAdapter;
    private ArrayAdapter<String> luggageWeightAdapter;

//    -------------- place api -------------------
    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBSj_urCzdNexxwEHRhPsmHk-kRKRTrHJg";
//    -------------- place api -------------------


    public BookServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show a datepicker when the dateButton is clicked

        getActivity().startService(new Intent(getActivity(), GetLatLongService.class));
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_book_service, container, false);

        final SharedPreferences sharedPreferenceLogin= getActivity().getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);

        fromAddressType = "manual";
        Type = "Closed";

        dateButton           = (Button) v.findViewById(R.id.btn_date);
        btnContinue          = (Button) v.findViewById(R.id.btn_continue);
        typeVehicle          = (RadioGroup) v.findViewById(R.id.vehicleType);
        rdfromSelectType     = (RadioGroup) v.findViewById(R.id.fromInsertType);
        unitSpinner          = (Spinner) v.findViewById(R.id.spinner3);
        txtVehicleHeight     = (EditText) v.findViewById(R.id.idVehicleHeight);
        txtVehicleLength     = (EditText) v.findViewById(R.id.idVehicleLength);
        txtVehicleWidth      = (EditText) v.findViewById(R.id.idVehicleWidth);
        txtGpsAddressText    = (EditText) v.findViewById(R.id.gpsAddressText);
        vehicleNameSpinner   = (Spinner) v.findViewById(R.id.idVehicleNameSpinner);
        luggageWeightSpinner = (Spinner) v.findViewById(R.id.idLuggageWeight);
        fromArea             = (AutoCompleteTextView) v.findViewById(R.id.fromArea);
        toArea               = (AutoCompleteTextView) v.findViewById(R.id.toArea);


        unitSpinner.setOnItemSelectedListener(this);
        vehicleNameSpinner.setOnItemSelectedListener(this);
        luggageWeightSpinner.setOnItemSelectedListener(this);

        // set adapter to fromArea spinner
        fromArea.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));
        fromArea.setOnItemClickListener(this);


        // set adapter to toArea spinner
        toArea.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.list_item));
        toArea.setOnItemClickListener(this);

        // formating date and set text on date button
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date date = new Date();
        String formatedDate=dateFormat.format(date);
        dateButton.setText(formatedDate);

        // open date dialog fragement when date button click
        final RippleView rippleViewForDate = (RippleView)v.findViewById(R.id.ripplebtn_date);
        rippleViewForDate.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                DialogFragment newDateFragment = new DatePickerFragment();
                newDateFragment.show(getFragmentManager(), "datePicker");

            }
        });

        // formating time and set text on time button
        timeButton = (Button) v.findViewById(R.id.btn_time);

        timeButton.setText(set12HrTime());

        // open time dialog fragement when date button click
        final RippleView rippleViewForTime = (RippleView)v.findViewById(R.id.ripplebtn_time);
        rippleViewForTime.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                DialogFragment newTimeFragment = new TimePickerFragment();
                newTimeFragment.show(getFragmentManager(), "TimePicker");

            }
        });

        // get value from radion button of type of vehicle
        typeVehicle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.openType:
                        Type = "Open";
                        break;

                    case R.id.closedType:
                        Type = "Closed";
                        break;

                }
            }
        });


        // select gps for getting current location address or manual enter from address using radio button
        rdfromSelectType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.idGPS:
                        fromAddressType = "gps";
                        fromArea.setVisibility(v.GONE);
                        txtGpsAddressText.setVisibility(v.VISIBLE);

                        String address = sharedPreferenceLogin.getString("fromAddress", "");
                        txtGpsAddressText.setText(address);
                        break;

                    case R.id.idManual:
                        fromAddressType = "manual";
                        fromArea.setVisibility(v.VISIBLE);
                        txtGpsAddressText.setVisibility(v.GONE);
                        txtGpsAddressText.setText("");

                        break;

                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(fromAddressType.equals("gps"))
            {
                fromAddress    = txtGpsAddressText.getText().toString();
            }
            else
            {
                fromAddress    = fromArea.getText().toString();

                if(fromAddress.isEmpty()){
                    Toast.makeText(getActivity(),"Select from area",Toast.LENGTH_LONG).show();
                    return;
                }
                GetAddressFromLatsLong.getGeoCodeFromLocationName(getActivity(), fromAddress);
            }
            toAddress      = toArea.getText().toString();
            deliveryDate   = dateButton.getText().toString();
            deliveryTime   = timeButton.getText().toString();

            System.out.println("to address: "+toAddress);
            if( toAddress.length() <=0 || toAddress.isEmpty()){
                Toast.makeText(getActivity(),"select correct destination",Toast.LENGTH_SHORT).show();
            }
            else if( fromAddress.equals(toAddress) ){
                Toast.makeText(getActivity(),"select correct destination",Toast.LENGTH_SHORT).show();
            }
            else if( !Validation.isEmptyString(Type) ){
                Toast.makeText(getActivity(),"select type",Toast.LENGTH_SHORT).show();
            }
            else {
                getActivity().stopService(new Intent(getActivity(), GetLatLongService.class));

                Bundle deliveryPrimaryData=new Bundle();
                deliveryPrimaryData.putString("FromAdd", fromAddress);
                deliveryPrimaryData.putString("ToAdd", toAddress);
                deliveryPrimaryData.putString("DeliveryDate", deliveryDate);
                deliveryPrimaryData.putString("DeliveryTime", deliveryTime);
                deliveryPrimaryData.putString("Weight", weight);
                deliveryPrimaryData.putString("Unit", unit);
                deliveryPrimaryData.putString("Type", Type);
                deliveryPrimaryData.putString("VehicleType", vehicleName);

                SecondBookServiceFragment fragment = new SecondBookServiceFragment();

                // send bundle from this fragment to payment fragment
                fragment.setArguments(deliveryPrimaryData);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
            }
        });

        return v;

    }

    private String set12HrTime() {
        //Use the current time as the default values for the time picker
        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        String aMpM = "AM";
        if(hourOfDay >11)
        {
            aMpM = "PM";
        }

        //Make the 24 hour time format to 12 hour time format
        int currentHour;
        if(hourOfDay>11)
        {
            currentHour = hourOfDay - 12;
        }
        else
        {
            currentHour = hourOfDay;
        }

        if(minute < 10){
            return String.valueOf(currentHour)+":0"+String.valueOf(minute)+" "+aMpM;
        }
        else{
            return String.valueOf(currentHour)+":"+String.valueOf(minute)+" "+aMpM;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        String item = parent.getItemAtPosition(position).toString();

        if(spinner.getId() == R.id.spinner3){
            unit = item;

            ArrayList<String> luggageArrayList = CommonUtilities.getWeight(unit);

            luggageWeightAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, luggageArrayList );
            luggageWeightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            luggageWeightSpinner.setAdapter(luggageWeightAdapter);
            luggageWeightAdapter.notifyDataSetChanged();
        }
        else if(spinner.getId() == R.id.idLuggageWeight){

            weight = item;
            ArrayList<String> vehicleName = CommonUtilities.getVehicleType(weight, unit, Type);

            vehicleNameAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, vehicleName );
            vehicleNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            vehicleNameSpinner.setAdapter(vehicleNameAdapter);
            vehicleNameAdapter.notifyDataSetChanged();
        }
        else if(spinner.getId() == R.id.idVehicleNameSpinner)
        {
            vehicleName = item;
            Map<String, String> getDimensionOfVehicle = CommonUtilities.getDimension(vehicleName);

            txtVehicleHeight.setText( getDimensionOfVehicle.get("height") );
            txtVehicleLength.setText( getDimensionOfVehicle.get("length") );
            txtVehicleWidth.setText( getDimensionOfVehicle.get("width") );
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//        switch (adapterView.getId()){
//            case R.id.fromArea:
//                String fromStr = (String) adapterView.getItemAtPosition(position);
//                System.out.println(fromStr);
//                GetAddressFromLatsLong.getGeoCodeFromLocationName(getActivity(), fromStr);
//                break;
//
//            case R.id.toArea:
//                String toStr = (String) adapterView.getItemAtPosition(position);
//                GetAddressFromLatsLong.getGeoCodeFromLocationName(getActivity(), toStr);
//                break;
//
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:IN");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

//            System.out.println("URL: "+url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            System.out.println("Error processing Places API URL: " + e);
            return resultList;
        } catch (IOException e) {
            System.out.println("Error connecting to Places API: "+e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            System.out.println("Cannot process JSON results: "+e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

}
