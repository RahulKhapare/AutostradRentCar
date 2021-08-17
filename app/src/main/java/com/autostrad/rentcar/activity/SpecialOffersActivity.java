package com.autostrad.rentcar.activity;

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
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.SpecialOffersAdapter;
import com.autostrad.rentcar.databinding.ActivitySpecialOffersBinding;
import com.autostrad.rentcar.model.SpecialOffersModel;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class SpecialOffersActivity extends AppCompatActivity {

    private SpecialOffersActivity activity = this;
    private ActivitySpecialOffersBinding binding;
    private List<SpecialOffersModel> specialOffersModelList;
    private SpecialOffersAdapter adapter;
    private LoadingDialog loadingDialog;
    private Session session;

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

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        specialOffersModelList = new ArrayList<>();
        adapter = new SpecialOffersAdapter(activity,specialOffersModelList);
        binding.recyclerOffers.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerOffers.setAdapter(adapter);

        hitSpecialOffersData();

    }


    private void hitSpecialOffersData() {

        ProgressView.show(activity,loadingDialog);
        Api.newApi(activity, P.BaseUrl + "offers_list")
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
                        JsonList offers = json.getJsonList(P.offers);

                        if (offers!=null && offers.size()!=0){
                            for (Json jsonData : offers){
                                SpecialOffersModel model = new SpecialOffersModel();
                                model.setId(jsonData.getString(P.id));
                                model.setTitle_name(jsonData.getString(P.title_name));
                                model.setDescription(jsonData.getString(P.description));
                                model.setOffer_validity(jsonData.getString(P.offer_validity));
                                model.setOffer_code(jsonData.getString(P.offer_code));
                                model.setImage_alt_text(jsonData.getString(P.image_alt_text));
                                model.setStatus(jsonData.getString(P.status));
                                model.setImage(jsonData.getString(P.image));
                                specialOffersModelList.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        checkData();
                    }else {
                        checkData();
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitSpecialOffersData");

    }


    private void checkData(){
        if (specialOffersModelList.isEmpty()){
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