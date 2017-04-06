package com.cleanslatetech.floc.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.asynctask.GetInterestCategoryAsyncTask;
import com.cleanslatetech.floc.asynctask.SetInterestAsyncTask;

public class SelectInterestActivity extends AppCompatActivity {

    private AppCompatTextView tvBtnSaveInterest;
    private LinearLayout layoutPostSelectionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interest);

        GridView gridLinearLayout = (GridView) findViewById(R.id.gridviewInterest);
        tvBtnSaveInterest = (AppCompatTextView) findViewById(R.id.tvBtnSaveInterest);
        layoutPostSelectionText = (LinearLayout) findViewById(R.id.id_post_selection_text);

        final GetInterestCategoryAsyncTask getInterestCategoryAsyncTask = new GetInterestCategoryAsyncTask(SelectInterestActivity.this, gridLinearLayout);

        tvBtnSaveInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SetInterestAsyncTask(
                        SelectInterestActivity.this,
                        getInterestCategoryAsyncTask.getSelectedCategoryArray()).postData();
            }
        });
    }

    public void changeState_saveInterest(int counter) {
        if(counter >= 5) {
            layoutPostSelectionText.setVisibility(View.VISIBLE);
        }
        else {
            layoutPostSelectionText.setVisibility(View.GONE);
        }
    }
}
