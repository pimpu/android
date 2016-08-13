package com.alchemistdigital.buxa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alchemistdigital.buxa.R;

public class Login extends AppCompatActivity {
    RelativeLayout relativeLayout_loginPanel;
    EditText txtLogin, txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        relativeLayout_loginPanel = (RelativeLayout) findViewById(R.id.layout_loginPanel);
        txtLogin = (EditText) findViewById(R.id.login_email);
        txtPassword = (EditText) findViewById(R.id.login_password);

        Animation translateAnim = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_logo_login);
        ImageView imageView = (ImageView) findViewById(R.id.id_buxaLogo_splashscreen);
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

    public void goToRegisterPage(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }
}
