package com.autostrad.rentcar.activity;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityLeasingBinding;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;

import java.util.regex.Matcher;

public class LeasingActivity extends AppCompatActivity {

    private LeasingActivity activity = this;
    private ActivityLeasingBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_leasing);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.corporateLEasing));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        onClick();
        hitLeasingData();

    }

    private void hitLeasingData() {

        ProgressView.show(activity, loadingDialog);
        Api.newApi(activity, P.BaseUrl + "corporate_leasing")
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
                        json = json.getJson("corporate_leasing");

                        String who_is_it_right_for = json.getString("who_is_it_right_for");
                        String who_is_it_right_for_tagline = json.getString("who_is_it_right_for_tagline");
                        JSONArray who_is_it_right_for_points = json.getJsonArray("who_is_it_right_for_points");
                        binding.txtWhoIsTitle.setText(who_is_it_right_for);
                        binding.txtTaglineWhoIs.setText(who_is_it_right_for_tagline);
                        setData(binding.lnrWhoIs,who_is_it_right_for_points);

                        String why_choose_fast_rent_a_car = json.getString("why_choose_fast_rent_a_car");
                        String why_choose_fast_rent_a_car_description = json.getString("why_choose_fast_rent_a_car_description");
                        binding.txtChooseRentCar.setText(why_choose_fast_rent_a_car);
                        binding.txtRentCarDesc.setText(why_choose_fast_rent_a_car_description);

                        String sectors_we_cater_to = json.getString("sectors_we_cater_to");
                        JSONArray sectors_we_cater_to_points = json.getJsonArray("sectors_we_cater_to_points");
                        binding.txtSectorCareTitle.setText(sectors_we_cater_to);
                        setData(binding.lnrSector,sectors_we_cater_to_points);

                        String end_to_end_solutions_we_provide = json.getString("end_to_end_solutions_we_provide");
                        JSONArray end_to_end_solutions_we_provide_points = json.getJsonArray("end_to_end_solutions_we_provide_points");
                        binding.txtEndToEndTitle.setText(end_to_end_solutions_we_provide);
                        setData(binding.lnrEndToEnd,end_to_end_solutions_we_provide_points);

                        String advantages = json.getString("advantages");
                        JSONArray criteria_points = json.getJsonArray("criteria_points");
                        JSONArray company_owned_fleet_points = json.getJsonArray("company_owned_fleet_points");
                        JSONArray leasing_from_fast_rent_a_car_points = json.getJsonArray("leasing_from_fast_rent_a_car_points");
                        binding.txtAdvantagesTitle.setText(advantages);
                        setAdvantagesData(binding.lnrListCriteria,criteria_points);
                        setAdvantagesData(binding.lnrCompanyOwned,company_owned_fleet_points);
                        setAdvantagesData(binding.lnrLeasingFrom,leasing_from_fast_rent_a_car_points);

                        String the_fast_rent_a_car_leasing_process = json.getString("the_fast_rent_a_car_leasing_process");
                        String the_fast_rent_a_car_leasing_process_description = json.getString("the_fast_rent_a_car_leasing_process_description");
                        String the_fast_rent_a_car_leasing_process_line1 = json.getString("the_fast_rent_a_car_leasing_process_line1");
                        String the_fast_rent_a_car_leasing_process_line2 = json.getString("the_fast_rent_a_car_leasing_process_line2");
                        JSONArray the_fast_rent_a_car_leasing_process_points = json.getJsonArray("the_fast_rent_a_car_leasing_process_points");
                        binding.txtLeasingProcess.setText(the_fast_rent_a_car_leasing_process);
                        binding.txtLeasingProcessDesc.setText(the_fast_rent_a_car_leasing_process_description);
                        binding.txtLeasingProcess1.setText(the_fast_rent_a_car_leasing_process_line1);
                        binding.txtLeasingProcess2.setText(the_fast_rent_a_car_leasing_process_line2);
                        setLeasingPointsData(binding.lnrLeasingProcessPoints,the_fast_rent_a_car_leasing_process_points);

                        String school_buses_services = json.getString("school_buses_services");
                        JSONArray school_buses_services_points = json.getJsonArray("school_buses_services_points");
                        binding.txtSchoolBusTitle.setText(school_buses_services);
                        setAdvantagesData(binding.lnrSchoolBusService,school_buses_services_points);

                        String special_features = json.getString("special_features");
                        JSONArray special_features_points = json.getJsonArray("special_features_points");
                        binding.txtSecialFeature.setText(special_features);
                        setFeaturePointsData(binding.lnrSpecialView,special_features_points);

                        String end_to_end_solutions = json.getString("end_to_end_solutions");
                        JSONArray end_to_end_solutions_points = json.getJsonArray("end_to_end_solutions_points");
                        binding.txtEndSolution.setText(end_to_end_solutions);
                        setEndSolutionData(binding.lnrEndSolution,end_to_end_solutions_points);

                        String private_and_airport_bus_rentals = json.getString("private_and_airport_bus_rentals");
                        String private_and_airport_bus_rentals_description = json.getString("private_and_airport_bus_rentals_description");
                        binding.txtPrivateTitle.setText(private_and_airport_bus_rentals);
                        binding.txtPrivateDesc.setText(private_and_airport_bus_rentals_description);

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitLeasingData");

    }

    public void setData(LinearLayout linearLayout,JSONArray jsonArray) {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                String value = jsonArray.getString(i);
                View view = getLayoutInflater().inflate(R.layout.leasing_item_view, null, false);
                TextView txtView = view.findViewById(R.id.txtView);
                txtView.setText(value);
                linearLayout.addView(view);
            }
        } catch (Exception e) {
            Log.e("TAG", "setDataASASAs: "+  e.getMessage() );
        }

    }


    public void setAdvantagesData(LinearLayout linearLayout,JSONArray jsonArray) {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                String value = jsonArray.getString(i);
                View view = getLayoutInflater().inflate(R.layout.leasing_item_text_view, null, false);
                TextView txtView = view.findViewById(R.id.txtView);
                txtView.setText(value);
                linearLayout.addView(view);
            }
        } catch (Exception e) {
            Log.e("TAG", "setDataASASAs: "+  e.getMessage() );
        }

    }

    public void setLeasingPointsData(LinearLayout linearLayout,JSONArray jsonArray) {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                String value = jsonArray.getString(i);
                View view = getLayoutInflater().inflate(R.layout.leasing_points_view, null, false);
                TextView txtView = view.findViewById(R.id.txtView);
                ImageView imgView = view.findViewById(R.id.imgView);
                txtView.setText(value);
                if (i==0){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_licence));
                }else if (i==1){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_certificute));
                }else if (i==2){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
                }else {
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));
                }
                linearLayout.addView(view);
            }
        } catch (Exception e) {
            Log.e("TAG", "setDataASASAs: "+  e.getMessage() );
        }

    }

    public void setFeaturePointsData(LinearLayout linearLayout,JSONArray jsonArray) {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                String value = jsonArray.getString(i);
                View view = getLayoutInflater().inflate(R.layout.special_feature_view, null, false);
                TextView txtView = view.findViewById(R.id.txtView);
                ImageView imgView = view.findViewById(R.id.imgView);
                txtView.setText(value);
                if (i==0){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_bus_mini));
                }else if (i==1){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_cctv_mini));
                }else if (i==2){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_seat_mini));
                }else if (i==3){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_exit_mini));
                }else if (i==4){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_door_mini));
                }else if (i==5){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_fire_mini));
                }else {
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));
                }
                linearLayout.addView(view);
            }
        } catch (Exception e) {
            Log.e("TAG", "setDataASASAs: "+  e.getMessage() );
        }

    }

    public void setEndSolutionData(LinearLayout linearLayout,JSONArray jsonArray) {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                String value = jsonArray.getString(i);
                View view = getLayoutInflater().inflate(R.layout.special_feature_view, null, false);
                TextView txtView = view.findViewById(R.id.txtView);
                ImageView imgView = view.findViewById(R.id.imgView);
                txtView.setText(value);
                if (i==0){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_truck_mini));
                }else if (i==1){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_setting_mini));
                }else if (i==2){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_page_mini));
                }else if (i==3){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_sanitizer));
                }else if (i==4){
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffel));
                }else {
                    imgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));
                }
                linearLayout.addView(view);
            }
        } catch (Exception e) {
            Log.e("TAG", "setDataASASAs: "+  e.getMessage() );
        }

    }


    private void onClick() {


        binding.imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                viewDialog(getResources().getDrawable(R.drawable.ic_carrer));
            }
        });

        binding.lnrClickOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownOne.getVisibility() == View.VISIBLE) {
                    binding.imgDownOne.setVisibility(View.GONE);
                    binding.imgUpOne.setVisibility(View.VISIBLE);
                    binding.lnrViewOne.setVisibility(View.VISIBLE);
                } else if (binding.imgUpOne.getVisibility() == View.VISIBLE) {
                    binding.imgDownOne.setVisibility(View.VISIBLE);
                    binding.imgUpOne.setVisibility(View.GONE);
                    binding.lnrViewOne.setVisibility(View.GONE);
                }
            }
        });


        binding.lnrClickTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownTwo.getVisibility() == View.VISIBLE) {
                    binding.imgDownTwo.setVisibility(View.GONE);
                    binding.imgUpTwo.setVisibility(View.VISIBLE);
                    binding.lnrViewTwo.setVisibility(View.VISIBLE);
                } else if (binding.imgUpTwo.getVisibility() == View.VISIBLE) {
                    binding.imgDownTwo.setVisibility(View.VISIBLE);
                    binding.imgUpTwo.setVisibility(View.GONE);
                    binding.lnrViewTwo.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownThree.getVisibility() == View.VISIBLE) {
                    binding.imgDownThree.setVisibility(View.GONE);
                    binding.imgUpThree.setVisibility(View.VISIBLE);
                    binding.lnrViewThree.setVisibility(View.VISIBLE);
                } else if (binding.imgUpThree.getVisibility() == View.VISIBLE) {
                    binding.imgDownThree.setVisibility(View.VISIBLE);
                    binding.imgUpThree.setVisibility(View.GONE);
                    binding.lnrViewThree.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownFour.getVisibility() == View.VISIBLE) {
                    binding.imgDownFour.setVisibility(View.GONE);
                    binding.imgUpFour.setVisibility(View.VISIBLE);
                    binding.lnrViewFour.setVisibility(View.VISIBLE);
                } else if (binding.imgUpFour.getVisibility() == View.VISIBLE) {
                    binding.imgDownFour.setVisibility(View.VISIBLE);
                    binding.imgUpFour.setVisibility(View.GONE);
                    binding.lnrViewFour.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownFive.getVisibility() == View.VISIBLE) {
                    binding.imgDownFive.setVisibility(View.GONE);
                    binding.imgUpFive.setVisibility(View.VISIBLE);
                    binding.lnrViewFive.setVisibility(View.VISIBLE);
                } else if (binding.imgUpFive.getVisibility() == View.VISIBLE) {
                    binding.imgDownFive.setVisibility(View.VISIBLE);
                    binding.imgUpFive.setVisibility(View.GONE);
                    binding.lnrViewFive.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownSix.getVisibility() == View.VISIBLE) {
                    binding.imgDownSix.setVisibility(View.GONE);
                    binding.imgUpSix.setVisibility(View.VISIBLE);
                    binding.lnrViewSix.setVisibility(View.VISIBLE);
                } else if (binding.imgUpSix.getVisibility() == View.VISIBLE) {
                    binding.imgDownSix.setVisibility(View.VISIBLE);
                    binding.imgUpSix.setVisibility(View.GONE);
                    binding.lnrViewSix.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownSeven.getVisibility() == View.VISIBLE) {
                    binding.imgDownSeven.setVisibility(View.GONE);
                    binding.imgUpSeven.setVisibility(View.VISIBLE);
                    binding.lnrViewSeven.setVisibility(View.VISIBLE);
                } else if (binding.imgUpSeven.getVisibility() == View.VISIBLE) {
                    binding.imgDownSeven.setVisibility(View.VISIBLE);
                    binding.imgUpSeven.setVisibility(View.GONE);
                    binding.lnrViewSeven.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownEight.getVisibility() == View.VISIBLE) {
                    binding.imgDownEight.setVisibility(View.GONE);
                    binding.imgUpEight.setVisibility(View.VISIBLE);
                    binding.lnrViewEight.setVisibility(View.VISIBLE);
                } else if (binding.imgUpEight.getVisibility() == View.VISIBLE) {
                    binding.imgDownEight.setVisibility(View.VISIBLE);
                    binding.imgUpEight.setVisibility(View.GONE);
                    binding.lnrViewEight.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownNine.getVisibility() == View.VISIBLE) {
                    binding.imgDownNine.setVisibility(View.GONE);
                    binding.imgUpNine.setVisibility(View.VISIBLE);
                    binding.lnrViewNine.setVisibility(View.VISIBLE);
                } else if (binding.imgUpNine.getVisibility() == View.VISIBLE) {
                    binding.imgDownNine.setVisibility(View.VISIBLE);
                    binding.imgUpNine.setVisibility(View.GONE);
                    binding.lnrViewNine.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownTen.getVisibility() == View.VISIBLE) {
                    binding.imgDownTen.setVisibility(View.GONE);
                    binding.imgUpTen.setVisibility(View.VISIBLE);
                    binding.lnrViewTen.setVisibility(View.VISIBLE);
                } else if (binding.imgUpTen.getVisibility() == View.VISIBLE) {
                    binding.imgDownTen.setVisibility(View.VISIBLE);
                    binding.imgUpTen.setVisibility(View.GONE);
                    binding.lnrViewTen.setVisibility(View.GONE);
                }
            }
        });


    }


    private void viewDialog(Drawable imagePath) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_image_view);

        PhotoView imageView = dialog.findViewById(R.id.imageView);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);

        LoadImage.glide(activity, imageView, imagePath);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

}