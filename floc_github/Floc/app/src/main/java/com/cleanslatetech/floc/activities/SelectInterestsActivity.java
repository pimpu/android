package com.cleanslatetech.floc.activities;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.SelectInterestAdapter;

import java.util.List;

public class SelectInterestsActivity extends AppCompatActivity {
//    private TextView tvSelectInterestText;
    private GridView selectInterestGridview;
    SelectInterestAdapter adapter;
    int iCounter=5;
    private Toolbar mActionBarToolbar;
    ImageView gotoNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interests);

        mActionBarToolbar = (Toolbar) findViewById(R.id.id_toolbar_selectinterest);
        setSupportActionBar(mActionBarToolbar);

//        tvSelectInterestText = (TextView) findViewById(R.id.id_tv_interest_text);
        gotoNext = (ImageView) findViewById(R.id.submitInterest);
        selectInterestGridview = (GridView) findViewById(R.id.id_gridview_selectInterest);

//        set pickup interest text to textview
        setPickupInterestText(iCounter);

        // set adapter for interests grid view
        adapter = new SelectInterestAdapter(this);
        selectInterestGridview.setAdapter(adapter);

        // video play when video thumbnail get click.
        final SelectInterestAdapter finalImageLoadAdapter = adapter;

        selectInterestGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // used for change background resources i.e. border to selected item
                List<Integer> iArraySelectedPositions = finalImageLoadAdapter.iArraySelectedPositions;

                if( iArraySelectedPositions.contains(position) && (iCounter >= 0 ) ) {
                    iArraySelectedPositions.remove(iArraySelectedPositions.indexOf(position));
                    iCounter++;
                    setPickupInterestText(iCounter);
                }
                else if(iCounter <= 5 && iCounter > 0 ){
                    iCounter--;
                    setPickupInterestText(iCounter);
                    iArraySelectedPositions.add(position);
                }
                finalImageLoadAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setPickupInterestText(int iCounter) {
        String strInterestText =  String.format(getResources().getString(R.string.strPick5Interests), iCounter);
        if( iCounter == 0 ) {
//            tvSelectInterestText.setText(R.string.strThanking);
            mActionBarToolbar.setTitle(R.string.strThanking);
            gotoNext.setVisibility(View.VISIBLE);

        }
        else {
            mActionBarToolbar.setTitle(strInterestText);
            gotoNext.setVisibility(View.GONE);
//            tvSelectInterestText.setText(strInterestText);
        }
        setSupportActionBar(mActionBarToolbar);
    }

    public void gotoNext(View view) {
        Toast.makeText(getApplicationContext(),"Hello", Toast.LENGTH_SHORT).show();
    }
}
