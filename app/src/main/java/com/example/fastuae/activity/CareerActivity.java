package com.example.fastuae.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CareerAdapter;
import com.example.fastuae.databinding.ActivityCareerBinding;
import com.example.fastuae.model.CareerModel;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class CareerActivity extends AppCompatActivity {

    private CareerActivity activity = this;
    private ActivityCareerBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;
    private List<CareerModel> careerModelList;
    private CareerAdapter careerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_career);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.careers));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        careerModelList = new ArrayList<>();
        careerAdapter = new CareerAdapter(activity,careerModelList);
        binding.recyclerCareer.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerCareer.setHasFixedSize(true);
        binding.recyclerCareer.setNestedScrollingEnabled(false);
        binding.recyclerCareer.setAdapter(careerAdapter);

        setData();
    }

    private void setData(){
        CareerModel model = new CareerModel();
        model.setLocation("Mumbai, Maharashtra");
        model.setExperience("2 Years");
        model.setDetails("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et d olore magna aliqua.");

        careerModelList.add(model);
        careerModelList.add(model);
        careerModelList.add(model);
        careerModelList.add(model);
        careerModelList.add(model);
        careerModelList.add(model);

        careerAdapter.notifyDataSetChanged();
    }

    private void hitCareerData() {

        ProgressView.show(activity,loadingDialog);
        Api.newApi(activity, P.BaseUrl + "")
                .setMethod(Api.GET)
//                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                    checkData();
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        JsonList list = json.getJsonList(P.list);

                        if (list!=null && list.size()!=0){
                            for (Json jsonData : list){

                            }
                        }
                        checkData();
                    }else {
                        checkData();
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCareerData");

    }


    private void checkData(){
        if (careerModelList.isEmpty()){
            binding.txtError.setVisibility(View.VISIBLE);
        }else {
            binding.txtError.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

}