package info.alchemistdigital.e_carrier.activity;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andexert.library.RippleView;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.services.GetLatLongService;


public class HomeFragment extends Fragment  {
    private RippleView rippleViewMyEnquiry,rippleViewBookService,rippleViewContact;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        rippleViewMyEnquiry = (RippleView)rootView.findViewById(R.id.ripplebtn_MyEnquiry);
        rippleViewBookService = (RippleView)rootView.findViewById(R.id.ripplebtn_book);
//        rippleViewTrack       = (RippleView) rootView.findViewById(R.id.ripplebtn_track);
        rippleViewContact     = (RippleView) rootView.findViewById(R.id.ripplebtn_contact);

        rippleViewMyEnquiry.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            Fragment fragment = null;
            String title = getString(R.string.app_name);
            @Override
            public void onComplete(RippleView rippleView) {
                fragment = new MyEnquiryFragment();
                title = getString(R.string.title_my_enquiry);
                if(fragment != null) {
                    FragmentManager fragmentManager = ((MainActivity)getActivity()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle("");
                }
            }

        });


        rippleViewBookService.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            Fragment fragment = null;
            String title = getString(R.string.app_name);
            @Override
            public void onComplete(RippleView rippleView) {
                fragment = new BookServiceFragment();
                title = getString(R.string.title_book);
                if(fragment != null) {

                    getActivity().startService(new Intent(getActivity(), GetLatLongService.class));

                    FragmentManager fragmentManager = ((MainActivity)getActivity()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();

                    // set the toolbar title
                    ((MainActivity)getActivity()).getSupportActionBar().setTitle("");
                }
            }

        });

        /*rippleViewTrack.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            FragmentActivity fragmentActivity = null;
            String title = getString(R.string.app_name);
            @Override
            public void onComplete(RippleView rippleView) {
                fragmentActivity = new MapsTrackingActivity();
                title = getString(R.string.title_track);

                startActivity(new Intent(getContext(), MapsTrackingActivity.class));
                // set the toolbar title
                ((MainActivity)getActivity()).getSupportActionBar().setTitle("");
            }

        });*/

        rippleViewContact.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            String title = getString(R.string.app_name);
            @Override
            public void onComplete(RippleView rippleView) {
                title = getString(R.string.title_contact);

//                startActivity(new Intent(getContext(), TestingService.class));
                // set the toolbar title
//                ((MainActivity)getActivity()).getSupportActionBar().setTitle("");
            }

        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
