package com.cleanslatetech.floc.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cleanslatetech.floc.R;
import com.cleanslatetech.floc.adapter.AllFlocRecyclerAdapter;
import com.cleanslatetech.floc.adapter.FlocTopicRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;

public class FlocDescTopicsActivity extends AppCompatActivity {
    public RecyclerView.Adapter flocTopicRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floc_desc_topics);


        String flocTopics = getIntent().getStringExtra("flocTopics");
        String topicsType = getIntent().getStringExtra("what");

        JSONArray arrayData = null;
        try {
            arrayData = new JSONArray(flocTopics);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerFlocTopics = (RecyclerView) findViewById(R.id.recyclerview_floctopics);

        flocTopicRecyclerAdapter = new FlocTopicRecyclerAdapter(this, arrayData, topicsType);
        recyclerFlocTopics.setAdapter(flocTopicRecyclerAdapter);
        recyclerFlocTopics.setLayoutManager(new LinearLayoutManager(this));
    }
}
