package com.example.fastuae.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fastuae.R;
import com.example.fastuae.adapter.SpecialOffersAdapter;
import com.example.fastuae.databinding.ActivitySpecialOffersBinding;
import com.example.fastuae.model.SpecialOffersModel;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class SpecialOffersActivity extends AppCompatActivity {

    private SpecialOffersActivity activity = this;
    private ActivitySpecialOffersBinding binding;
    private List<SpecialOffersModel> specialOffersModelList;
    private SpecialOffersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_special_offers);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.specialOffers));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        specialOffersModelList = new ArrayList<>();
        adapter = new SpecialOffersAdapter(activity,specialOffersModelList);
        binding.recyclerOffers.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerOffers.setAdapter(adapter);

        setData();

    }

    private void setData(){

        SpecialOffersModel model = new SpecialOffersModel();
        model.setCode("Use Code: FAST100");
        model.setValidUpTo("Validity: 15 December 2020 to 15 January 2021");
        model.setDescription("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet.");

        specialOffersModelList.add(model);
        specialOffersModelList.add(model);

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