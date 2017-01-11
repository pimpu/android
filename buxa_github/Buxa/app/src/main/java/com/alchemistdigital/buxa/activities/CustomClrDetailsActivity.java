package com.alchemistdigital.buxa.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.CustomClearanceModel;
import com.alchemistdigital.buxa.model.TransportationModel;
import com.alchemistdigital.buxa.utilities.DateHelper;

public class CustomClrDetailsActivity extends AppCompatActivity {
    TextView tv_bookingId, tv_date, tv_CCType, tv_commodity, tv_gross, tv_hsc, tv_shipmentType,
            tv_stuffingType, tv_stuffingAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_clr_details);

        toolbarSetup();

        init();

        setValue(getIntent().getExtras().getString("bookingId"));
    }

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_customClrtDetails);
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
        getSupportActionBar().setTitle("Custom Clr Details");
        getSupportActionBar().setSubtitle(getIntent().getExtras().getString("shipmentArea"));
    }

    private void init() {
        tv_bookingId = (TextView) findViewById(R.id.CCDetails_book);
        tv_date = (TextView) findViewById(R.id.CCDetails_book_date);
        tv_CCType = (TextView) findViewById(R.id.CCDetails_type);
        tv_commodity = (TextView) findViewById(R.id.CCDetails_commodity);
        tv_gross = (TextView) findViewById(R.id.CCDetails_gross);
        tv_hsc = (TextView) findViewById(R.id.CCDetails_HSC);
        tv_shipmentType = (TextView) findViewById(R.id.CCDetails_shipmentType);
        tv_stuffingType = (TextView) findViewById(R.id.CCDetails_stuffingType);
        tv_stuffingAddress = (TextView) findViewById(R.id.CCDetails_stuffingAddress);
    }

    private void setValue(String bookingId) {
        DatabaseClass dbClass = new DatabaseClass(this);
        CustomClearanceModel customClearanceModel = dbClass.getCustomClrData(bookingId);

        tv_bookingId.setText(customClearanceModel.getBookingId());
        tv_date.setText(DateHelper.convertToString(Long.parseLong(customClearanceModel.getCreatedAt())));
        tv_CCType.setText(customClearanceModel.getCCType());
        tv_commodity.setText( dbClass.getCommodityName(customClearanceModel.getCommodityServerId()));
        tv_gross.setText(""+customClearanceModel.getGrossWeight());
        tv_hsc.setText(""+customClearanceModel.getHSCode());
        tv_shipmentType.setText(dbClass.getShipmentNameByServerId(customClearanceModel.getiShipmentType()));
        tv_stuffingType.setText(customClearanceModel.getStuffingType());
        tv_stuffingAddress.setText(customClearanceModel.getStuffingAddress());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
