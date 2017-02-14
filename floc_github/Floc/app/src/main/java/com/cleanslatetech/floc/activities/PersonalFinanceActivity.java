package com.cleanslatetech.floc.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cleanslatetech.floc.R;

public class PersonalFinanceActivity extends BaseAppCompactActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_finance);

        super.setToolBar(getResources().getString(R.string.my_profile));

        init();
    }

    private void init() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
