package com.autostrad.rentcar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.CustomerFeedbackAdapter;
import com.autostrad.rentcar.adapter.LocationFilterAdapter;
import com.autostrad.rentcar.databinding.ActivityCustomerFeedbackBinding;
import com.autostrad.rentcar.model.CustomerFeedbackModel;
import com.autostrad.rentcar.model.LocationModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.Validation;
import com.autostrad.rentcar.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class CustomerFeedbackActivity extends AppCompatActivity implements LocationFilterAdapter.onFeedback, CustomerFeedbackAdapter.onCLick{

    private CustomerFeedbackActivity activity = this;
    private ActivityCustomerFeedbackBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;
    private String flag;

    private List<LocationModel> feedbackList;
    private LocationFilterAdapter feedbackAdapter;

    private List<LocationModel> serviceList;
    private LocationFilterAdapter serviceAdapter;

    private List<LocationModel> compareList;
    private LocationFilterAdapter compareAdapter;

    private List<LocationModel> rentList;
    private LocationFilterAdapter rentAdapter;

    private List<CustomerFeedbackModel> emirateList;
    private CustomerFeedbackAdapter emirateAdapter;

    private List<CustomerFeedbackModel> productKnowledgeList;
    private CustomerFeedbackAdapter productKnowledgeAdapter;

    private List<CustomerFeedbackModel> professionalismList;
    private CustomerFeedbackAdapter professionalismAdapter;

    private List<CustomerFeedbackModel> friednlinessList;
    private CustomerFeedbackAdapter friednlinessAdapter;

    private List<CustomerFeedbackModel> timelyResponseList;
    private CustomerFeedbackAdapter timelyResponseAdapter;

    private List<CustomerFeedbackModel> reliabilityList;
    private CustomerFeedbackAdapter reliabilityAdapter;

    private List<CustomerFeedbackModel> cleanlinessList;
    private CustomerFeedbackAdapter cleanlinessAdapter;

    String feedbackID;
    String serviceID;
    String compareID;
    String rentID;
    String emirateID;
    String timelyID;
    String friednlinessID;
    String professionalismID;
    String productID;
    String cleanlinessID;
    String releabilityID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_feedback);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.customerFeedback));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        flag = session.getString(P.languageFlag);

        feedbackList = new ArrayList<>();
        feedbackAdapter = new LocationFilterAdapter(activity,feedbackList,3);
        binding.recyclerFeedback.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerFeedback.setAdapter(feedbackAdapter);
        binding.recyclerFeedback.setNestedScrollingEnabled(false);

        serviceList = new ArrayList<>();
        serviceAdapter = new LocationFilterAdapter(activity,serviceList,4);
        binding.recyclerService.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerService.setAdapter(serviceAdapter);
        binding.recyclerService.setNestedScrollingEnabled(false);

        compareList = new ArrayList<>();
        compareAdapter = new LocationFilterAdapter(activity,compareList,5);
        binding.recyclerCompare.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerCompare.setAdapter(compareAdapter);
        binding.recyclerCompare.setNestedScrollingEnabled(false);

        rentList = new ArrayList<>();
        rentAdapter = new LocationFilterAdapter(activity,rentList,6);
        binding.recyclerRent.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerRent.setAdapter(rentAdapter);
        binding.recyclerRent.setNestedScrollingEnabled(false);

        emirateList = new ArrayList<>();
        emirateAdapter = new CustomerFeedbackAdapter(activity,emirateList,1);
        binding.recyclerEmirate.setLayoutManager(new GridLayoutManager(activity,3));
        binding.recyclerEmirate.setNestedScrollingEnabled(false);
        binding.recyclerEmirate.setAdapter(emirateAdapter);

        productKnowledgeList = new ArrayList<>();
        productKnowledgeAdapter = new CustomerFeedbackAdapter(activity,productKnowledgeList,2);
        binding.recyclerProductKnowledge.setLayoutManager(new GridLayoutManager(activity,3));
        binding.recyclerProductKnowledge.setNestedScrollingEnabled(false);
        binding.recyclerProductKnowledge.setAdapter(productKnowledgeAdapter);

        professionalismList = new ArrayList<>();
        professionalismAdapter = new CustomerFeedbackAdapter(activity,professionalismList,3);
        binding.recyclerProfessionalism.setLayoutManager(new GridLayoutManager(activity,3));
        binding.recyclerProfessionalism.setNestedScrollingEnabled(false);
        binding.recyclerProfessionalism.setAdapter(professionalismAdapter);

        friednlinessList = new ArrayList<>();
        friednlinessAdapter = new CustomerFeedbackAdapter(activity,friednlinessList,4);
        binding.recyclerFriednliness.setLayoutManager(new GridLayoutManager(activity,3));
        binding.recyclerFriednliness.setNestedScrollingEnabled(false);
        binding.recyclerFriednliness.setAdapter(friednlinessAdapter);

        timelyResponseList = new ArrayList<>();
        timelyResponseAdapter = new CustomerFeedbackAdapter(activity,timelyResponseList,5);
        binding.recyclerTimelyResponse.setLayoutManager(new GridLayoutManager(activity,3));
        binding.recyclerTimelyResponse.setNestedScrollingEnabled(false);
        binding.recyclerTimelyResponse.setAdapter(timelyResponseAdapter);

        reliabilityList = new ArrayList<>();
        reliabilityAdapter = new CustomerFeedbackAdapter(activity,reliabilityList,6);
        binding.recyclerReliability.setLayoutManager(new GridLayoutManager(activity,3));
        binding.recyclerReliability.setNestedScrollingEnabled(false);
        binding.recyclerReliability.setAdapter(reliabilityAdapter);

        cleanlinessList = new ArrayList<>();
        cleanlinessAdapter = new CustomerFeedbackAdapter(activity,cleanlinessList,7);
        binding.recyclerCleanliness.setLayoutManager(new GridLayoutManager(activity,3));
        binding.recyclerCleanliness.setNestedScrollingEnabled(false);
        binding.recyclerCleanliness.setAdapter(cleanlinessAdapter);


        updateIcons();
        onClick();
        hitCustomerFeedbackData();

    }

    private void onClick(){
        binding.cardFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkFeedbackView();
            }
        });

        binding.cardService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkServiceView();
            }
        });

        binding.cardCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkCompareView();
            }
        });

        binding.cardRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkRentView();
            }
        });

        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkValidateData();
            }
        });

    }

    private void checkValidateData(){
        if (TextUtils.isEmpty(feedbackID)){
            H.showMessage(activity,getResources().getString(R.string.selectFeedbackSoursce));
            return;
        }
        if (TextUtils.isEmpty(serviceID)){
            H.showMessage(activity,getResources().getString(R.string.selectFeedbackService));
            return;
        }
        if (TextUtils.isEmpty(compareID)){
            H.showMessage(activity,getResources().getString(R.string.selectFeedbackCompare));
            return;
        }
        if (TextUtils.isEmpty(rentID)){
            H.showMessage(activity,getResources().getString(R.string.selectFeedbackRentAgain));
            return;
        }
        if (TextUtils.isEmpty(binding.etxSuggetion.getText().toString())){
            H.showMessage(activity,getResources().getString(R.string.enterSuggetion));
            return;
        }
        if (binding.etxSuggetion.getText().toString().length()<5){
            H.showMessage(activity,getResources().getString(R.string.enterValidSuggetion));
            return;
        }
        if (TextUtils.isEmpty(binding.etxName.getText().toString())){
            H.showMessage(activity,getResources().getString(R.string.enterName));
            return;
        }
        if (TextUtils.isEmpty(binding.etxMobile.getText().toString())){
            H.showMessage(activity,getResources().getString(R.string.enterMobile));
            return;
        }
        if (TextUtils.isEmpty(binding.etxEmail.getText().toString())){
            H.showMessage(activity,getResources().getString(R.string.enterEmailId));
            return;
        }
        if (!Validation.validEmail(binding.etxEmail.getText().toString())){
            H.showMessage(activity,getResources().getString(R.string.enterEmailValid));
            return;
        }

        Json json = new Json();
        json.addString("feedback_source_id",feedbackID);
        json.addString("feedback_service_id",serviceID);
        json.addString("emirate_id",emirateID);
        json.addString("product_knowledge",productID);
        json.addString("professionalism",professionalismID);
        json.addString("friendliness",friednlinessID);
        json.addString("timely_response",timelyID);
        json.addString("reliability",releabilityID);
        json.addString("cleanliness",cleanlinessID);
        json.addString("compared_to_other_car",compareID);
        json.addString("rent_again",rentID);
        json.addString("suggestion",binding.etxSuggetion.getText().toString().trim());
        json.addString("user_name",binding.etxName.getText().toString().trim());
        json.addString("user_mobile",binding.etxMobile.getText().toString().trim());
        json.addString("user_email",binding.etxEmail.getText().toString().trim());

        hitFeedbackData(json);
    }


    public void hitFeedbackData(Json j) {

        ProgressView.show(activity, loadingDialog);

        Api.newApi(activity, P.BaseUrl + "customer_feedback_submit").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        H.showMessage(activity, json.getString(P.msg));
                        new Handler().postDelayed(() -> {
                            finish();
                        }, 1000);
                    } else {
                        H.showMessage(activity, json.getString(P.error_array));
                    }
                    ProgressView.dismiss(loadingDialog);
                })
                .run("hitFeedbackData");
    }

    private void checkFeedbackView(){
        if (binding.lnrFeedbackListView.getVisibility() == View.VISIBLE) {
            binding.lnrFeedbackListView.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgFeedbackLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgFeedbackRight.setImageResource(R.drawable.ic_down_arrow);
            }

        } else if (binding.lnrFeedbackListView.getVisibility() == View.GONE) {
            binding.lnrFeedbackListView.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgFeedbackLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgFeedbackRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void checkServiceView(){
        if (binding.lnrServiceListView.getVisibility() == View.VISIBLE) {
            binding.lnrServiceListView.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgServiceLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgServiceRight.setImageResource(R.drawable.ic_down_arrow);
            }

        } else if (binding.lnrServiceListView.getVisibility() == View.GONE) {
            binding.lnrServiceListView.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgServiceLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgServiceRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void checkCompareView(){
        if (binding.lnrCompareListView.getVisibility() == View.VISIBLE) {
            binding.lnrCompareListView.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgCompareLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgCompareRight.setImageResource(R.drawable.ic_down_arrow);
            }

        } else if (binding.lnrCompareListView.getVisibility() == View.GONE) {
            binding.lnrCompareListView.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgCompareLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgCompareRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void checkRentView(){
        if (binding.lnrRentListView.getVisibility() == View.VISIBLE) {
            binding.lnrRentListView.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgRentLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgRentRight.setImageResource(R.drawable.ic_down_arrow);
            }

        } else if (binding.lnrRentListView.getVisibility() == View.GONE) {
            binding.lnrRentListView.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgRentLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgRentRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }


    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgFeedbackRight.setVisibility(View.GONE);
            binding.imgFeedbackLeft.setVisibility(View.VISIBLE);

            binding.imgServiceRight.setVisibility(View.GONE);
            binding.imgServiceLeft.setVisibility(View.VISIBLE);

            binding.imgCompareRight.setVisibility(View.GONE);
            binding.imgCompareLeft.setVisibility(View.VISIBLE);

            binding.imgRentRight.setVisibility(View.GONE);
            binding.imgRentLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgFeedbackRight.setVisibility(View.VISIBLE);
            binding.imgFeedbackLeft.setVisibility(View.GONE);

            binding.imgServiceRight.setVisibility(View.VISIBLE);
            binding.imgServiceLeft.setVisibility(View.GONE);

            binding.imgCompareRight.setVisibility(View.VISIBLE);
            binding.imgCompareLeft.setVisibility(View.GONE);

            binding.imgRentRight.setVisibility(View.VISIBLE);
            binding.imgRentLeft.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFeedbackClick(LocationModel model) {
        checkFeedbackView();
        feedbackID = model.getId();
        binding.txtFeedback.setText(model.getEmirate_name());
    }

    @Override
    public void onServiceClick(LocationModel model) {
        checkServiceView();
        serviceID = model.getId();
        binding.txtService.setText(model.getEmirate_name());
    }

    @Override
    public void onCompareClick(LocationModel model) {
        checkCompareView();
        compareID = model.getId();
        binding.txtCompare.setText(model.getEmirate_name());
    }

    @Override
    public void onRentClick(LocationModel model) {
        checkRentView();
        rentID = model.getId();
        binding.txtRent.setText(model.getEmirate_name());
    }

    @Override
    public void emirateSelection(CustomerFeedbackModel model) {
        emirateID = model.getId();
    }

    @Override
    public void productSelection(CustomerFeedbackModel model) {
        productID = model.getId();
    }

    @Override
    public void professionalismSelection(CustomerFeedbackModel model) {
        professionalismID = model.getId();
    }

    @Override
    public void friednlinessSelection(CustomerFeedbackModel model) {
        friednlinessID = model.getId();
    }

    @Override
    public void timelySelection(CustomerFeedbackModel model) {
        timelyID = model.getId();
    }

    @Override
    public void releabilitySelection(CustomerFeedbackModel model) {
        releabilityID = model.getId();
    }

    @Override
    public void cleanlinessSelection(CustomerFeedbackModel model) {
        cleanlinessID = model.getId();
    }

    private void hitCustomerFeedbackData() {
        ProgressView.show(activity,loadingDialog);
        Api.newApi(activity, P.BaseUrl + "customer_feedback_data")
                .setMethod(Api.GET)
//                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        JsonList feedback_source = json.getJsonList("feedback_source");
                        JsonList feedback_service = json.getJsonList("feedback_service");
                        JsonList emirate = json.getJsonList("emirate");
                        JsonList feedback_rating = json.getJsonList("feedback_rating");
                        JsonList feedback_compared_to_other_car = json.getJsonList("feedback_compared_to_other_car");
                        JsonList feedback_rent_again = json.getJsonList("feedback_rent_again");

                        setFeedBackData(feedback_source);
                        setServiceData(feedback_service);
                        setEmirateData(emirate);
                        setRatingData(feedback_rating);
                        setCompareData(feedback_compared_to_other_car);
                        setRentData(feedback_rent_again);

                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCustomerFeedbackData");

    }

    private void setFeedBackData(JsonList jsonList){
        LocationModel model0 = new LocationModel();
        model0.setId("");
        model0.setEmirate_name(getResources().getString(R.string.select));
        feedbackList.add(model0);
        if (jsonList!=null && jsonList.size()!=0){
            for (Json json : jsonList){
                String id = json.getString("id");
                String source_name = json.getString("source_name");
                LocationModel model = new LocationModel();
                model.setId(id);
                model.setEmirate_name(source_name);
                feedbackList.add(model);
            }
            feedbackAdapter.notifyDataSetChanged();
            feedbackID = feedbackList.get(0).getId();
            binding.txtFeedback.setText(feedbackList.get(0).getEmirate_name());
        }
    }

    private void setServiceData(JsonList jsonList){
        LocationModel model0 = new LocationModel();
        model0.setId("");
        model0.setEmirate_name(getResources().getString(R.string.select));
        serviceList.add(model0);
        if (jsonList!=null && jsonList.size()!=0){
            for (Json json : jsonList){
                String id = json.getString("id");
                String source_name = json.getString("service_name");
                LocationModel model = new LocationModel();
                model.setId(id);
                model.setEmirate_name(source_name);
                serviceList.add(model);
            }
            serviceAdapter.notifyDataSetChanged();
            serviceID = serviceList.get(0).getId();
            binding.txtService.setText(serviceList.get(0).getEmirate_name());
        }
    }

    private void setCompareData(JsonList jsonList){
        LocationModel model0 = new LocationModel();
        model0.setId("");
        model0.setEmirate_name(getResources().getString(R.string.select));
        compareList.add(model0);
        if (jsonList!=null && jsonList.size()!=0){
            for (Json json : jsonList){
                String id = json.getString("id");
                String source_name = json.getString("compare_name");
                LocationModel model = new LocationModel();
                model.setId(id);
                model.setEmirate_name(source_name);
                compareList.add(model);
            }
            compareAdapter.notifyDataSetChanged();
            compareID = compareList.get(0).getId();
            binding.txtCompare.setText(compareList.get(0).getEmirate_name());
        }
    }

    private void setRentData(JsonList jsonList){
        LocationModel model0 = new LocationModel();
        model0.setId("");
        model0.setEmirate_name(getResources().getString(R.string.select));
        rentList.add(model0);
        if (jsonList!=null && jsonList.size()!=0){
            for (Json json : jsonList){
                String id = json.getString("id");
                String source_name = json.getString("rent_again_name");
                LocationModel model = new LocationModel();
                model.setId(id);
                model.setEmirate_name(source_name);
                rentList.add(model);
            }
            rentAdapter.notifyDataSetChanged();
            rentID = rentList.get(0).getId();
            binding.txtRent.setText(rentList.get(0).getEmirate_name());
        }
    }


    private void setEmirateData(JsonList jsonList){
        if (jsonList!=null && jsonList.size()!=0){
            for (Json json : jsonList){
                CustomerFeedbackModel model = new CustomerFeedbackModel();
                model.setId(json.getString("id"));
                model.setValue(json.getString("emirate_name"));
                emirateList.add(model);
            }
            emirateAdapter.notifyDataSetChanged();
        }
    }

    private void setRatingData(JsonList jsonList){
        if (jsonList!=null && jsonList.size()!=0){
            for (Json json : jsonList){
                CustomerFeedbackModel model = new CustomerFeedbackModel();
                model.setId(json.getString("id"));
                model.setValue(json.getString("rating_name"));
                productKnowledgeList.add(model);
                professionalismList.add(model);
                friednlinessList.add(model);
                timelyResponseList.add(model);
                reliabilityList.add(model);
                cleanlinessList.add(model);
            }
            productKnowledgeAdapter.notifyDataSetChanged();
            professionalismAdapter.notifyDataSetChanged();
            friednlinessAdapter.notifyDataSetChanged();
            timelyResponseAdapter.notifyDataSetChanged();
            reliabilityAdapter.notifyDataSetChanged();
            cleanlinessAdapter.notifyDataSetChanged();
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