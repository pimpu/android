package info.alchemistdigital.e_carrier.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.utilities.CommonUtilities;
import info.alchemistdigital.e_carrier.utilities.Validation;

/**
 * Created by user on 12/19/2015.
 */
public class SecondBookServiceFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    CheckBox chkBoxTermCondition;
    boolean checkboxFlag=false;
    Button btnContinue;
    FloatingActionButton btnAddVAS;
    ImageView btnCloseVAs;
    EditText pickUpAdd,deliveryAdd,extraManPwr;
    String txtPickUpAdd,txtDeliverAdd;
    TextInputLayout pickup_input_layout,deliver_input_layout;
    CheckBox chkManpower, chkInsurance, chkRisk, chkPacking, chkHandling, chkHoliday, chkShipment, chkLabour;
    String strManPower,strExtraManPwrKg="",strInsurance,strRisk,strPacking,strHandling,strHoliday,strShipment,strLabour;
    ArrayList<String> arrayVAS;
    AlertDialog alertDialog;

    public SecondBookServiceFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show a datepicker when the dateButton is clicked

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second_book_service, container, false);

        arrayVAS = new ArrayList<String>();

        btnContinue          = (Button) v.findViewById(R.id.btn_value_added_continue);
        btnAddVAS            = (FloatingActionButton) v.findViewById(R.id.idAddVAS);
        chkBoxTermCondition  = (CheckBox) v.findViewById(R.id.chkBoxTermCondition);
        pickUpAdd            = (EditText) v.findViewById(R.id.pickAddress);
        deliveryAdd          = (EditText) v.findViewById(R.id.deliveryAddress);
        pickup_input_layout  = (TextInputLayout) v.findViewById(R.id.input_layout_pickupAddress);
        deliver_input_layout = (TextInputLayout) v.findViewById(R.id.input_layout_deliveryAddress);


        chkBoxTermCondition.setOnCheckedChangeListener(new myCheckBoxChnageClicker());

        btnAddVAS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // get value added service cost w.r.t vehicle
                Map<String, String> manPowerPrice = CommonUtilities.getManPowerPriceByVehicle(getArguments().getString("VehicleType"));

                // set user defined value in string w.r.t. vehicle
                String manPowertext = getResources().getString(R.string.chkTextExtraManPower, manPowerPrice.get("manpower"), getResources().getString(R.string.Rs));
                String deliveryText = getResources().getString(R.string.chkTextHolidayDelivery, manPowerPrice.get("delivery"));
                String labourText = getResources().getString(R.string.chkTextLabourCharges, manPowerPrice.get("labour"));

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View VADView = inflater.inflate(R.layout.value_added_services_chk, null);

                extraManPwr = (EditText) VADView.findViewById(R.id.extraManpowerKg);
                chkManpower = (CheckBox) VADView.findViewById(R.id.chkManpower);
                chkInsurance = (CheckBox) VADView.findViewById(R.id.chkInsurance);
                chkRisk = (CheckBox) VADView.findViewById(R.id.chkRisk);
                chkPacking = (CheckBox) VADView.findViewById(R.id.chkPackaging);
                chkHandling = (CheckBox) VADView.findViewById(R.id.chkHandling);
                chkHoliday = (CheckBox) VADView.findViewById(R.id.chkHoliday);
                chkShipment = (CheckBox) VADView.findViewById(R.id.chkShipment);
                chkLabour = (CheckBox) VADView.findViewById(R.id.chkLabour);
                btnCloseVAs = (ImageView) VADView.findViewById(R.id.imageView_closeVAS);

                // textbox show or not according to chkManpower is checked
                chkManpower.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //is chkManpower checked?
                        if (((CheckBox) v).isChecked()) {
                            extraManPwr.setVisibility(VADView.VISIBLE);
                        } else {
                            extraManPwr.setVisibility(VADView.GONE);
                        }

                    }
                });

                chkManpower.setText(manPowertext);
                chkHoliday.setText(deliveryText + getResources().getString(R.string.Rs));
                chkLabour.setText(labourText + getResources().getString(R.string.Rs));

                if(strManPower != null){
                    chkManpower.setChecked(true);
                    extraManPwr.setVisibility(VADView.VISIBLE);
                    extraManPwr.setText(strExtraManPwrKg);
                }
                if(strInsurance != null){
                    chkInsurance.setChecked(true);
                }
                if(strRisk != null){
                    chkRisk.setChecked(true);
                }
                if(strPacking != null){
                    chkPacking.setChecked(true);
                }
                if(strHandling != null){
                    chkHandling.setChecked(true);
                }
                if(strHoliday != null){
                    chkHoliday.setChecked(true);
                }
                if(strShipment != null){
                    chkShipment.setChecked(true);
                }
                if(strLabour != null){
                    chkLabour.setChecked(true);
                }

                builder.setCancelable(false);
                builder.setView(VADView);

                // create alert dialog
                alertDialog = builder.create();

                // show it
                alertDialog.show();
                btnCloseVAs.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        arrayVAS.clear();

                        strManPower = chkManpower.isChecked() ? "manpower" : null;
                        strInsurance = chkInsurance.isChecked() ? "insurance" : null;
                        strRisk = chkRisk.isChecked() ? "risk" : null;
                        strPacking = chkPacking.isChecked() ? "packing" : null;
                        strHandling = chkHandling.isChecked() ? "handling" : null;
                        strHoliday= chkHoliday.isChecked() ? "holiday_delivery" : null;
                        strShipment= chkShipment.isChecked() ? "shipment" : null;
                        strLabour= chkLabour.isChecked() ? "labour_charges" : null;

                        arrayVAS.add(strManPower);
                        arrayVAS.add(strInsurance);
                        arrayVAS.add(strRisk);
                        arrayVAS.add(strPacking);
                        arrayVAS.add(strHandling);
                        arrayVAS.add(strHoliday);
                        arrayVAS.add(strShipment);
                        arrayVAS.add(strLabour);
                        arrayVAS.removeAll(Collections.singleton(null));

                        if( strManPower != null ){
                            strExtraManPwrKg = extraManPwr.getText().toString();
                        }
                        else {
                            strExtraManPwrKg = "";
                        }

                        if( strManPower != null ){
                            if( strExtraManPwrKg.length() > 0  ){
                                alertDialog.dismiss();
                            }else {
                                Toast.makeText(getActivity(),"Weight text field is empty.",Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            alertDialog.dismiss();
                        }
                    }
                });


            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPickUpAdd         = pickUpAdd.getText().toString();
                txtDeliverAdd        = deliveryAdd.getText().toString();

                if(!Validation.isEmptyString(txtPickUpAdd)){
                    pickup_input_layout.setErrorEnabled(true);
                    pickup_input_layout.setError("You need to enter Pickup address");
                }
                else {
                    pickup_input_layout.setErrorEnabled(false);
                }

                if(!Validation.isEmptyString(txtDeliverAdd)){
                    deliver_input_layout.setErrorEnabled(true);
                    deliver_input_layout.setError("You need to enter Delivery address");
                }
                else {
                    deliver_input_layout.setErrorEnabled(false);
                }

                if(checkboxFlag && Validation.isEmptyString(txtPickUpAdd) &&  Validation.isEmptyString(txtDeliverAdd) ) {

                    Bundle secondaBookServiceBundle = new Bundle();

                    if(arrayVAS.size() <= 0)
                    {
                        arrayVAS.add("noVAS");
                    }
                    secondaBookServiceBundle.putString("FromAdd", getArguments().getString("FromAdd"));
                    secondaBookServiceBundle.putString("ToAdd", getArguments().getString("ToAdd"));
                    secondaBookServiceBundle.putString("DeliveryDate", getArguments().getString("DeliveryDate"));
                    secondaBookServiceBundle.putString("DeliveryTime", getArguments().getString("DeliveryTime"));
                    secondaBookServiceBundle.putString("Weight", getArguments().getString("Weight"));
                    secondaBookServiceBundle.putString("Unit", getArguments().getString("Unit"));
                    secondaBookServiceBundle.putString("Type", getArguments().getString("Type"));
                    secondaBookServiceBundle.putString("VehicleType", getArguments().getString("VehicleType"));

                    secondaBookServiceBundle.putStringArrayList("ValueAddedService", arrayVAS);
                    secondaBookServiceBundle.putString("extraManPwr", strExtraManPwrKg);
                    secondaBookServiceBundle.putString("PickUpAddress",txtPickUpAdd);
                    secondaBookServiceBundle.putString("DeliveryAddress",txtDeliverAdd);

                    PaymentBookServiceFragment payment_fragment = new PaymentBookServiceFragment();

                    // send bundle from this fragment to payment fragment
                    payment_fragment.setArguments(secondaBookServiceBundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, payment_fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {
                    Toast.makeText(getActivity(),"Accept Term and Condition",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return  v;
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class myCheckBoxChnageClicker implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView == chkBoxTermCondition){
                checkboxFlag=true;
            }
            else {
                checkboxFlag=false;
            }

        }
    }
}