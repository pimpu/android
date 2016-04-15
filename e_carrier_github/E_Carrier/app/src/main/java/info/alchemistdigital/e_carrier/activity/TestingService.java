package info.alchemistdigital.e_carrier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.services.GetLatLongService;

public class TestingService extends AppCompatActivity implements View.OnClickListener {

    Button buttonStart, buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_service);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
//                new GetLatLongService();
                startService(new Intent(this, GetLatLongService.class));
                break;
            case R.id.buttonStop:
                stopService(new Intent(this, GetLatLongService.class));
                break;
        }
    }
}
