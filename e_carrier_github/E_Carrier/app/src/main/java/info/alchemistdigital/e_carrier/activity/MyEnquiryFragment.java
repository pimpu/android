package info.alchemistdigital.e_carrier.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.adapter.Enquired_Details_Adapter;
import info.alchemistdigital.e_carrier.model.Enquired_Details_Item;
import info.alchemistdigital.e_carrier.utilities.DateHelper;
import info.alchemistdigital.e_carrier.utilities.Queries;
import info.alchemistdigital.e_carrier.utilities.RecyclerViewListener;


public class MyEnquiryFragment extends Fragment {

    private RecyclerView enquired_details_RecyclerView;
    private RecyclerView.Adapter enquired_details_Adapter;
    public static List<Enquired_Details_Item> enquired_details_data;

    public MyEnquiryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_my_enquiry, container, false);

        enquired_details_RecyclerView = (RecyclerView) v.findViewById(R.id.enquired_details_recycler);
        View viewById = v.findViewById(R.id.empty_enqyuiry_data);

        List<Enquired_Details_Item> bookingEnquiryData = enquired_details_data();

        if(bookingEnquiryData.size() <= 0){
            viewById.setVisibility(View.VISIBLE);
            enquired_details_RecyclerView.setVisibility(View.GONE);
        }
        else {
            viewById.setVisibility(View.GONE);
            enquired_details_RecyclerView.setVisibility(View.VISIBLE);
            enquiryDetailsRecycler(bookingEnquiryData);
        }


        return v;
    }

    private List<Enquired_Details_Item> enquired_details_data() {
        enquired_details_data = new ArrayList<>();

        SharedPreferences sharedPreferenceLogin = getActivity().getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
        int loginID = sharedPreferenceLogin.getInt(getResources().getString(R.string.loginId), 0);

        Cursor c= Queries.db.rawQuery("SELECT * FROM booking_service_info WHERE userId="+loginID+";", null);

        if (c.moveToFirst()) {
            do{
                Enquired_Details_Item enquiryItem = new Enquired_Details_Item();
                String date = DateHelper.convertToString(Long.parseLong(c.getString(c.getColumnIndex("deliveryDateTime"))));
                enquiryItem.setBeginning(c.getString(c.getColumnIndex("beginningArea")));
                enquiryItem.setEnquiryDate(date);

                int enquiryId = c.getInt(c.getColumnIndex("enquiryId"));
                enquiryItem.setEnquiryId(enquiryId);

                Cursor bookedEnquiryCursor= Queries.db.rawQuery("SELECT * FROM booked_enquiry WHERE enquiryId="+enquiryId+";", null);
                if(bookedEnquiryCursor!=null && bookedEnquiryCursor.getCount()>0)
                {
                    bookedEnquiryCursor.moveToFirst();
                    do {
                        enquiryItem.setDriverData( bookedEnquiryCursor.getString( bookedEnquiryCursor.getColumnIndex("driverData")) );
                    } while (bookedEnquiryCursor.moveToNext());
                }
                else {
                    enquiryItem.setDriverData("not assign any driver till.");
                }


                enquired_details_data.add(enquiryItem);
            }while(c.moveToNext());
        }
        return enquired_details_data;
    }

    private void enquiryDetailsRecycler(List<Enquired_Details_Item> bookingEnquiryData) {
        enquired_details_Adapter = new Enquired_Details_Adapter(MainActivity.getInstance(),bookingEnquiryData);
        enquired_details_RecyclerView.setAdapter(enquired_details_Adapter);
        enquired_details_RecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.getInstance()));
        enquired_details_RecyclerView.addOnItemTouchListener(
                new RecyclerViewListener.RecyclerItemClickListener(getActivity(),new RecyclerViewListener.RecyclerItemClickListener.OnItemClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {
                        final int enquiryId = enquired_details_data.get(position).getEnquiryId();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setMessage("Track your service");

                        alertDialogBuilder.setPositiveButton("Track", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                Cursor bookedEnquiryCursor= Queries.db.rawQuery("SELECT * FROM booked_enquiry WHERE enquiryId="+enquiryId+";", null);
                                if(bookedEnquiryCursor!=null && bookedEnquiryCursor.getCount()>0){
                                    bookedEnquiryCursor.moveToFirst();
                                    do {
                                        if( !bookedEnquiryCursor.getString( bookedEnquiryCursor.getColumnIndex("driverData")).equals("not assign any driver till.") ){
                                            Bundle enquiryPassBundle=new Bundle();
                                            enquiryPassBundle.putInt("enquiryId",enquiryId);

                                            MapsTrackingActivity mapTrack = new MapsTrackingActivity();
                                            mapTrack.setArguments(enquiryPassBundle);

                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.container_body, mapTrack);
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                        }

                                    } while (bookedEnquiryCursor.moveToNext());
                                }
                                else {
                                    Toast.makeText(getActivity(), "Sorry,this service completed or not started yet.", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }
                })
        );
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
