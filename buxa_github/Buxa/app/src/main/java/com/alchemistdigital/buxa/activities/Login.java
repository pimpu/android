package com.alchemistdigital.buxa.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.sharedprefrencehelper.SetSharedPreference;
import com.alchemistdigital.buxa.utilities.CommonVariables;
import com.alchemistdigital.buxa.utilities.RestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import static com.alchemistdigital.buxa.utilities.CommonUtilities.isConnectingToInternet;
import static com.alchemistdigital.buxa.utilities.Validations.emailValidate;
import static com.alchemistdigital.buxa.utilities.Validations.isEmptyString;


public class Login extends Fragment implements View.OnClickListener {
    LinearLayout layout_noConnection;
    RelativeLayout relativeLayout_loginPanel;
    EditText txtLogin, txtPassword;
    TextInputLayout loginEmail_InputLayout, loginPwd_InputLayout;
    ProgressDialog prgDialog;
    Button btnLogin, btnGoToRegister;
    TextView errorMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        loginMethod(rootView);

        return rootView;
    }

    private void loginMethod(View rootView) {
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(getActivity());
        // Set Progress Dialog Text
        prgDialog.setMessage("Logging ...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        layout_noConnection = (LinearLayout) rootView.findViewById(R.id.id_noInternet_login);
        errorMessage = (TextView) rootView.findViewById(R.id.login_error_msg);
        relativeLayout_loginPanel = (RelativeLayout) rootView.findViewById(R.id.layout_loginPanel);
        txtLogin = (EditText) rootView.findViewById(R.id.login_email);
        txtPassword = (EditText) rootView.findViewById(R.id.login_password);
        loginEmail_InputLayout = (TextInputLayout) rootView.findViewById(R.id.input_layout_login_email);
        loginPwd_InputLayout = (TextInputLayout) rootView.findViewById(R.id.input_layout_login_password);
        btnLogin = (Button) rootView.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnGoToRegister  = (Button) rootView.findViewById(R.id.btn_Register);
        btnGoToRegister.setOnClickListener(this);

        Animation translateAnim = AnimationUtils.loadAnimation(getActivity(),
                R.anim.anim_logo_login);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.id_buxaLogo_splashscreen);
        imageView.startAnimation(translateAnim);
        translateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                relativeLayout_loginPanel.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Boolean boolEmail = emailValidate(txtLogin.getText().toString());
                Boolean boolPwd = isEmptyString(txtPassword.getText().toString());

                if (boolEmail) {
                    loginEmail_InputLayout.setErrorEnabled(false);
                } else {
                    loginEmail_InputLayout.setErrorEnabled(true);
                    loginEmail_InputLayout.setError("Email field is wrong.");
                }

                if (boolPwd) {
                    loginPwd_InputLayout.setErrorEnabled(false);
                } else {
                    loginPwd_InputLayout.setErrorEnabled(true);
                    loginPwd_InputLayout.setError("Password field is empty.");
                }

                if ( boolEmail && boolPwd ) {
                    // Check if Internet present
                    if (!isConnectingToInternet(getActivity())) {
                        layout_noConnection.setVisibility(View.VISIBLE);
                        errorMessage.setText(getResources().getString(R.string.strNoConnection));
                        // stop executing code by return
                        return;
                    } else {
                        layout_noConnection.setVisibility(View.GONE);
                        LoginCompanyForBuxa();
                    }
                }
                break;

            case R.id.btn_Register:
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                getActivity().finish();
                break;
        }
    }

    private void LoginCompanyForBuxa() {
        RequestParams params;
        params = new RequestParams();

        params.put("email", txtLogin.getText().toString());
        params.put("password", txtPassword.getText().toString());

        invokeWS(params);
    }

    private void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();

        // Make RESTful webservice call using AsyncHttpClient object
        RestClient.post(CommonVariables.COMPANY_LOGIN_SERVER_URL, params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'

            @Override
            public void onSuccess(String response) {
                prgDialog.cancel();
                try {
                    JSONObject json = new JSONObject(response);

                    Boolean error = json.getBoolean(CommonVariables.TAG_ERROR);
                    if (error) {
                        layout_noConnection.setVisibility(View.VISIBLE);
                        errorMessage.setText(json.getString(CommonVariables.TAG_MESSAGE));
                    } else {
                        layout_noConnection.setVisibility(View.GONE);
                        SetSharedPreference setSharedPreference = new SetSharedPreference(getActivity());

                        // it store the Register true value of user for purpose of user is registered with this app.
                        setSharedPreference.setBooleanLogin(getResources().getString(R.string.boolean_login_sharedPref), "true");
                        setSharedPreference.setLoginId(getResources().getString(R.string.loginId), json.getInt("id"));
                        setSharedPreference.setApiKey(getResources().getString(R.string.apikey), json.getString("api_key"));
                        setSharedPreference.setLoginEmail(getResources().getString(R.string.loginEmail), json.getString("email"));
                        setSharedPreference.setLoginName(getResources().getString(R.string.loginName), json.getString("loginName"));
                        setSharedPreference.setCompanyName(getResources().getString(R.string.companyName), json.getString("companyName"));

                        Intent intent = new Intent(getActivity(), SelectServiceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        getActivity().finish();
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    System.out.println("Requested resource not found");
                    Toast.makeText(getActivity(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    System.out.println("Something went wrong at server end");
                    Toast.makeText(getActivity(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    System.out.println("Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]");
                    Toast.makeText(getActivity(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
