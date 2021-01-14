package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.fastuae.R;
import com.example.fastuae.adapter.LocationDetailAdapter;
import com.example.fastuae.databinding.ActivityLocationBinding;
import com.example.fastuae.model.LocationModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private LocationActivity activity = this;
    private ActivityLocationBinding binding;
    private List<LocationModel> locationModelList;
    private LocationDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.locations));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        locationModelList = new ArrayList<>();
        adapter = new LocationDetailAdapter(activity,locationModelList);
        binding.recyclerLocation.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerLocation.setAdapter(adapter);

        setData();
        onClick();
    }


    private void onClick(){
        binding.txtViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.txtViewMore.setVisibility(View.GONE);
                setData();
            }
        });
    }

    private void setData(){

        LocationModel model = new LocationModel();
        model.setBranchCode("A8");
        model.setBranchName("Mussafah");
        model.setDetails("Mussafah Industrial 2, Mussafah, Abu Dhabi,\nUnited Arab Emirates\nTel : 02 551 2916 Email : a8@fastuae.com");
        model.setOperation("Sunday to Wednesday: 08.00 to 17.00\nThursday: 08.00 to 17.00\nFriday: Closed\nSaturday: 08.00 to 17.00");
        locationModelList.add(model);
        locationModelList.add(model);
        locationModelList.add(model);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
}
