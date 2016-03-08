package com.alchemistdigital.kissan;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alchemistdigital.kissan.asynctask.LoginAsyncTask;
import com.alchemistdigital.kissan.sharedPrefrenceHelper.GetSharedPreferenceHelper;
import com.andexert.library.RippleView;

import static com.alchemistdigital.kissan.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.kissan.utilities.Validations.emailValidate;
import static com.alchemistdigital.kissan.utilities.Validations.isEmptyString;

public class Login extends AppCompatActivity {
    EditText txtLoginEmail, txtLoginPwd;
    TextInputLayout emailInputLayout, pwdInputLayout;
    private String loginSharedPrefEmail;
    private TextView tvForgotPwd;
    private View idLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_login_toolbar);
        setSupportActionBar(toolbar);

        idLogin = findViewById(R.id.id_xmlLogin);

        txtLoginEmail = (EditText) findViewById(R.id.login_email);
        txtLoginPwd = (EditText) findViewById(R.id.login_password);
        emailInputLayout = (TextInputLayout) findViewById(R.id.input_layout_email);
        pwdInputLayout = (TextInputLayout) findViewById(R.id.input_layout_password);
//        tvForgotPwd      = (TextView) findViewById(R.id.idForgotPwd);

        // made sharedprefrence
        GetSharedPreferenceHelper getPrefrence = new GetSharedPreferenceHelper(Login.this);

        // take values for validation purpose.
        // loginSharedPrefEmail string validates with entered email id and pwd when Login button pressed
        loginSharedPrefEmail = getPrefrence.getEmailPreference(getResources().getString(R.string.loginEmail));
        txtLoginEmail.setText(loginSharedPrefEmail);

        final RippleView rippleView = (RippleView) findViewById(R.id.ripplebtn_login);
        rippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

            @Override
            public void onComplete(RippleView rippleView) {
                String email = txtLoginEmail.getText().toString();
                String pwd = txtLoginPwd.getText().toString();
                Boolean EmailValid = emailValidate(email);
                Boolean PWDValid = isEmptyString(pwd);

                if (!EmailValid) {
                    emailInputLayout.setErrorEnabled(true);
                    emailInputLayout.setError("You need to enter correct email-id");
                } else {
                    emailInputLayout.setErrorEnabled(false);
                }

                if (!PWDValid) {
                    pwdInputLayout.setErrorEnabled(true);
                    pwdInputLayout.setError("password field is empty");
                } else {
                    pwdInputLayout.setErrorEnabled(false);
                }

                if (EmailValid && PWDValid) {


                    // Check if Internet present
                    if (!isConnectingToInternet(Login.this)) {
                        // Internet Connection is not present
                        Snackbar.make(idLogin, "No internet connection !", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onCreate(null);
                                    }
                                }).show();
                        // stop executing code by return
                        return;
                    } else {
                        new LoginAsyncTask(Login.this, email, pwd).execute();
                    }
                }
            }
        });

        /*tvForgotPwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Forgot Password");
                builder.setMessage("password will be send on \"" + loginSharedPrefEmail + "\" email.");

                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        int loginId = sharedPreferenceLogin.getInt(getResources().getString(R.string.loginId), 0);
                        new extendsForgotPasswordAsync(loginSharedPrefEmail,String.valueOf(loginId)).execute();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });*/
    }

    /*private class extendsForgotPasswordAsync extends ForgotPassword {
        public extendsForgotPasswordAsync(String loginEmail, String loginId) {
            super(getActivity(), loginEmail, loginId);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            try {
                System.out.println("forgot password request: " + result.toString());

                String message = result.getString(CommonUtilities.TAG_MESSAGE);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}
