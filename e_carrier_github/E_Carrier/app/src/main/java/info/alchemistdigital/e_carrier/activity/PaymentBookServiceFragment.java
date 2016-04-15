package info.alchemistdigital.e_carrier.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.asynctask.CustomerEnquiryAsyncTask;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;
import info.alchemistdigital.e_carrier.utilities.ConnectionDetector;
import info.alchemistdigital.e_carrier.utilities.DateHelper;
import info.alchemistdigital.e_carrier.utilities.Queries;

/**
 * Created by user on 12/19/2015.
 */
public class PaymentBookServiceFragment extends Fragment{
    protected RadioGroup rgPaymentType;
    String fromArea,toArea,date,time,weight,unit,type,vehicle,pickup,delivery,lats,longs,extraManPwr;
    ArrayList<String> VAS = new ArrayList<String>();
    int userId;
    View payMentView;

    public PaymentBookServiceFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show a datepicker when the dateButton is clicked

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment_book_service, container, false);

        payMentView = v.findViewById(R.id.payment_fragment);

        rgPaymentType = (RadioGroup) v.findViewById(R.id.paymentType);

        SharedPreferences sharedPreferenceLogin=MainActivity.getInstance().getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
        userId = sharedPreferenceLogin.getInt(getResources().getString(R.string.loginId), 0);
        lats = sharedPreferenceLogin.getString(getResources().getString(R.string.lats), null);
        longs = sharedPreferenceLogin.getString(getResources().getString(R.string.longs), null);

        fromArea = getArguments().getString("FromAdd");
        toArea   = getArguments().getString("ToAdd");
        date     = getArguments().getString("DeliveryDate");
        time     = getArguments().getString("DeliveryTime");
        weight   = getArguments().getString("Weight");
        unit     = getArguments().getString("Unit");
        type     = getArguments().getString("Type");
        vehicle  = getArguments().getString("VehicleType");
        VAS      = getArguments().getStringArrayList("ValueAddedService");
        pickup   = getArguments().getString("PickUpAddress");
        delivery = getArguments().getString("DeliveryAddress");
        extraManPwr = getArguments().getString("extraManPwr");

        System.out.println("From: "+fromArea);
        System.out.println("to: "+toArea);
        System.out.println("Date: "+date);
        System.out.println("Time: "+time);
        System.out.println("Weight: "+weight);
        System.out.println("Unit: "+unit);
        System.out.println("Type: "+type);
        System.out.println("Vehicle Type: "+vehicle);

        System.out.println("Value Added: "+VAS);
        System.out.println("PickUp: "+pickup);
        System.out.println("Deliver: "+delivery);
        System.out.println("extraManPwr: "+extraManPwr);
        System.out.println("lats: "+lats);
        System.out.println("longs: "+longs);


        // get value from radion button of type of vehicle
        rgPaymentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.cashPayment:
                        ConnectionDetector cd=new ConnectionDetector(getActivity());

                        if (!cd.isConnectingToInternet()) {
                            // Internet Connection is not present
                            Snackbar.make(payMentView, "No internet connection !", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            // stop executing code by return
                            return;
                        }
                        else{
                            new extendCustomerEnquiryAsyncTask(MainActivity.getInstance(),userId,
                                    fromArea,toArea,date,time,weight,
                                    unit,type,vehicle,VAS,pickup,delivery,lats,longs,extraManPwr).execute();
                        }
                        break;

                    case R.id.cashOnDelivery:

                        break;

                    case R.id.netBanking:

                        break;

                    case R.id.creditDebit:

                        break;

                }
            }
        });


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class extendCustomerEnquiryAsyncTask extends CustomerEnquiryAsyncTask {

        public extendCustomerEnquiryAsyncTask(MainActivity instance, int userId, String fromArea,
                                              String toArea, String date, String time, String weight,
                                              String unit, String type, String vehicle,
                                              ArrayList<String> vas,String pickup, String delivery,String lats,String longs,String extraManPwr) {
            super(instance,userId,fromArea,toArea,date,time,weight,unit,type,vehicle,vas,pickup,delivery,lats,longs,extraManPwr);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // check log cat fro response

            try {
                Log.d("Customer request: ", result.toString());

                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

                if(result.contains("Error occurred!")) {
                    Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                    return;
                }

                JSONObject json = new JSONObject(result);

                int success = json.getInt(CommonUtilities.TAG_SUCCESS);
                if (success == 1) {
                    int enquiryId = json.getInt(CommonUtilities.TAG_MESSAGE);
                    SharedPreferences sharedPreferenceLogin=MainActivity.getInstance().getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                    int loginID = sharedPreferenceLogin.getInt(getResources().getString(R.string.loginId), 0);

                    String str = date+" "+time; //Your String containing a date
                    long milisDate = 0;
                    try {
                        milisDate = DateHelper.convertToMillis(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String strDateTime    = String.valueOf(milisDate);

                    Queries.db.execSQL("INSERT INTO booking_service_info" +
                            "(enquiryId,userId,creationTime," +
                            "beginningArea,destinationArea,deliveryDateTime," +
                            "weight,unit," +
                            "pickupAddress,deliveryAddress" +
                            ")VALUES" +
                            "(" + enquiryId + ", " +
                            loginID + ", " +
                            System.currentTimeMillis() + ", " +
                            "'" + fromArea + "', " +
                            "'" + toArea + "', " +
                            "'" + strDateTime + "', " +
                            Double.parseDouble(weight) + ", " +
                            "'" + unit + "', " +
                            "'" + pickup + "', " +
                            "'" + delivery + "'" +
                            ");");

                    Toast.makeText(getActivity(), "You will get driver within 30 minutes.Thanking you.", Toast.LENGTH_LONG).show();

                    MyEnquiryFragment my_enquiry_fragment = new MyEnquiryFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, my_enquiry_fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
                else if( success == 2 || success == 0){
                    Toast.makeText(getActivity(), json.getString(CommonUtilities.TAG_MESSAGE) ,Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
