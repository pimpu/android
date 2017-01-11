package com.alchemistdigital.buxa.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.TransportationModel;
import com.alchemistdigital.buxa.utilities.DateHelper;

public class TransportationDetailsActivity extends AppCompatActivity {
    TextView tv_bookingId, tv_pick, tv_drop, tv_shipmentType, tv_gross, tv_packType,
            tv_noOfPack, tv_commodity, tv_length, tv_height, tv_width, tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_details);

        toolbarSetup();

        init();

        setValue(getIntent().getExtras().getString("bookingId"));
    }

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_transportDetails);
        setSupportActionBar(toolbar);

        // set back button on toolbar
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        // set click listener on back button of toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Transportation Details");
        getSupportActionBar().setSubtitle(getIntent().getExtras().getString("shipmentArea"));
    }

    private void init() {
        tv_bookingId = (TextView) findViewById(R.id.transDetails_book);
        tv_date = (TextView) findViewById(R.id.transDetails_book_date);
        tv_pick = (TextView) findViewById(R.id.transDetails_pick);
        tv_drop = (TextView) findViewById(R.id.transDetails_drop);
        tv_shipmentType = (TextView) findViewById(R.id.transDetails_shipment);
        tv_gross = (TextView) findViewById(R.id.transDetails_gross);
        tv_packType = (TextView) findViewById(R.id.transDetails_packType);
        tv_noOfPack = (TextView) findViewById(R.id.transDetails_noOfPack);
        tv_commodity = (TextView) findViewById(R.id.transDetails_commodity);
        tv_length = (TextView) findViewById(R.id.transDetails_length);
        tv_height = (TextView) findViewById(R.id.transDetails_height);
        tv_width = (TextView) findViewById(R.id.transDetails_width);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setValue(String value) {
        DatabaseClass dbClass = new DatabaseClass(this);
        TransportationModel transportstionData = dbClass.getTransportstionData(value);

        String shipmentName = dbClass.getShipmentNameByServerId(transportstionData.getShipmentType());
        String measurement = transportstionData.getMeasurement();
        String text = shipmentName + " - ";
        if( shipmentName.equals("LCL")) {
            text = text + measurement +" CBM";
        }
        else {
            switch (measurement){
                case "20":
                    text = text + measurement +"\'";
                    break;
                case "40":
                    text = text + measurement +"\'";
                    break;
                case "40_HQ":
                    text = text + "40\' HQ";
                    break;

            }
        }

        tv_bookingId.setText(transportstionData.getBookingId());
        tv_pick.setText(transportstionData.getPickUp());
        tv_drop.setText(transportstionData.getDrop());
        tv_shipmentType.setText(text);
        tv_gross.setText( ""+transportstionData.getGrossWeight());
        tv_packType.setText(  dbClass.getPackagingTypeByServerId(transportstionData.getPackType())  );
        tv_noOfPack.setText(""+transportstionData.getNoOfPack());
        tv_commodity.setText(  dbClass.getCommodityName(transportstionData.getCommodityServerId())  );
        tv_length.setText(""+transportstionData.getDimenLength());
        tv_height.setText(""+transportstionData.getDimenHeight());
        tv_width.setText(""+transportstionData.getDimenWidth());
        tv_date.setText(DateHelper.convertToString(Long.parseLong(transportstionData.getCreatedAt())));

    }
}
