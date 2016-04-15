package info.alchemistdigital.e_carrier;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import info.alchemistdigital.e_carrier.utilities.AlertDialogManager;
import info.alchemistdigital.e_carrier.utilities.ConnectionDetector;
import info.alchemistdigital.e_carrier.utilities.Queries;

public class AuthenticationScreen extends AppCompatActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    static AuthenticationScreen authenticationScreen_activity;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Connection detector
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Queries.db = openOrCreateDatabase("E_Carrier",MODE_PRIVATE,null);

        authenticationScreen_activity =this;

//        cd = new ConnectionDetector(getApplicationContext());
//
//        // Check if Internet present
//        if (!cd.isConnectingToInternet()) {
//            // Internet Connection is not present
//            alert.showAlertDialog(AuthenticationScreen.this,
//                    "Internet Connection Error",
//                    "Please connect to working Internet connection", false);
//            // stop executing code by return
//            return;
//        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    public static AuthenticationScreen getInstance(){
        return authenticationScreen_activity;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Login(), "LOGIN");
        adapter.addFragment(new Register(), "REGISTER");
        //adapter.addFragment(new TwoFragment(), "TWO");
        //adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
