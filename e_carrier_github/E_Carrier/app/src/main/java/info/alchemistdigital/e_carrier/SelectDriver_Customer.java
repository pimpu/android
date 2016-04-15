package info.alchemistdigital.e_carrier;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SelectDriver_Customer extends AppCompatActivity {
    RadioGroup selectUser;
    String userType,name,email,pwd,phone;
    Button btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_driver__customer);

        selectUser = (RadioGroup) findViewById(R.id.userType);
        btn_continue = (Button) findViewById(R.id.btn_userTypeContinue);

        Bundle extras = getIntent().getExtras();

        name  = extras.getString("regName");
        email = extras.getString("regEmail");
        pwd   = extras.getString("regPwd");
        phone = extras.getString("regPhone");

        // get value from radion button of type of vehicle
        selectUser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rdCustomer:
                    userType = "customer";
                    break;

                case R.id.rdDriver:
                    userType = "driver";
                    break;

            }
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if( userType == null )
            {
                Toast.makeText(getApplicationContext(),"Please, Select user type",Toast.LENGTH_LONG).show();
            }
            else
            {
                SharedPreferences sharedPreferenceLogin= getSharedPreferences(getResources().getString(R.string.sharedPrefrence), 0);
                SharedPreferences.Editor editor = sharedPreferenceLogin.edit();
                editor.putString(getResources().getString(R.string.userType), userType );
                editor.commit();

//                new UserInfoInsertAsyncTask(SelectDriver_Customer.this,name,email,phone,pwd,userType).execute();
            }
            }
        });

    }
}
