package com.alchemistdigital.buxa.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.alchemistdigital.buxa.DBHelper.DatabaseClass;
import com.alchemistdigital.buxa.R;
import com.alchemistdigital.buxa.model.FreightForwardingModel;
import com.alchemistdigital.buxa.model.TransportationModel;
import com.alchemistdigital.buxa.utilities.DateHelper;

public class FreightFwdDetailsActivity extends AppCompatActivity {
    TextView tv_bookingId, tv_date, tv_pol, tv_poc, tv_pod, tv_incoterm, tv_dda, tv_shipmentType, tv_gross, tv_packType,
            tv_noOfPack, tv_commodity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freight_fwd_details);

        toolbarSetup();

        init();

        setValue(getIntent().getExtras().getString("bookingId"));
    }

    private void toolbarSetup() {
        // initialise toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_freightFwdtDetails);
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
        getSupportActionBar().setTitle("Freight Fwd Details");
    }

    private void init() {
        tv_bookingId = (TextView) findViewById(R.id.FFDetails_book);
        tv_date = (TextView) findViewById(R.id.FFDetails_book_date);
        tv_pol = (TextView) findViewById(R.id.FFDetails_pol);
        tv_poc = (TextView) findViewById(R.id.FFDetails_poc);
        tv_pod = (TextView) findViewById(R.id.FFDetails_pod);
        tv_incoterm = (TextView) findViewById(R.id.FFDetails_incoterm);
        tv_dda = (TextView) findViewById(R.id.FFDetails_DDA);
        tv_shipmentType = (TextView) findViewById(R.id.FFDetails_shipment);
        tv_gross = (TextView) findViewById(R.id.FFDetails_gross);
        tv_packType = (TextView) findViewById(R.id.FFDetails_packType);
        tv_noOfPack = (TextView) findViewById(R.id.FFDetails_noOfPack);
        tv_commodity = (TextView) findViewById(R.id.FFDetails_commodity);
    }

    private void setValue(String bookingId) {
        DatabaseClass dbClass = new DatabaseClass(this);
        FreightForwardingModel freightForwardingModel = dbClass.getFreightFwdData(bookingId);

        String shipmentName = dbClass.getShipmentNameByServerId(freightForwardingModel.getShipmentType());
        String measurement = freightForwardingModel.getMeasurement();
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

        tv_bookingId.setText(freightForwardingModel.getBookingId());
        tv_date.setText(DateHelper.convertToString(Long.parseLong(freightForwardingModel.getCreatedAt())));
        tv_pol.setText(freightForwardingModel.getPortOfLoading());
        tv_poc.setText(freightForwardingModel.getPortOfCountry());
        tv_pod.setText(""+freightForwardingModel.getPortOfDestination());
        tv_incoterm.setText(""+freightForwardingModel.getStrIncoterm());
        tv_dda.setText(""+freightForwardingModel.getStrDestinatioDeliveryAdr());
        tv_shipmentType.setText(text);
        tv_gross.setText( ""+freightForwardingModel.getGrossWeight());
        tv_packType.setText(  dbClass.getPackagingTypeByServerId(freightForwardingModel.getPackType())  );
        tv_noOfPack.setText(""+freightForwardingModel.getNoOfPack());
        tv_commodity.setText(  dbClass.getCommodityName(freightForwardingModel.getCommodityServerId())  );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
