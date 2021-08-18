package com.autostrad.rentcar.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityAboutBinding;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;
import com.github.chrisbanes.photoview.PhotoView;

public class AboutActivity extends AppCompatActivity {

    private AboutActivity activity = this;
    private ActivityAboutBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.about));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        hitAboutUsDetails();

    }

    public void hitAboutUsDetails() {

        ProgressView.show(activity, loadingDialog);

        Api.newApi(activity, P.BaseUrl + "about_us")
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        Json about_us = json.getJson("about_us");

                        String seo_title = about_us.getString("seo_title");
                        String seo_keywords = about_us.getString("seo_keywords");
                        String page_heading = about_us.getString("page_heading");
                        String quote = about_us.getString("quote");
                        String description = about_us.getString("description");
                        String our_heritage = about_us.getString("our_heritage");
                        String our_heritage_description = about_us.getString("our_heritage_description");
                        String our_mission = about_us.getString("our_mission");
                        String our_mission_description = about_us.getString("our_mission_description");
                        String our_vision = about_us.getString("our_vision");
                        String our_vision_description = about_us.getString("our_vision_description");
                        String our_values = about_us.getString("our_values");
                        String our_values_title_1 = about_us.getString("our_values_title_1");
                        String our_values_description_1 = about_us.getString("our_values_description_1");
                        String our_values_title_2 = about_us.getString("our_values_title_2");
                        String our_values_description_2 = about_us.getString("our_values_description_2");
                        String our_values_title_3 = about_us.getString("our_values_title_3");
                        String our_values_description_3 = about_us.getString("our_values_description_3");
                        String our_values_title_4 = about_us.getString("our_values_title_4");
                        String our_values_description_4 = about_us.getString("our_values_description_4");
                        String our_values_title_5 = about_us.getString("our_values_title_5");
                        String our_values_description_5 = about_us.getString("our_values_description_5");
                        String our_fleet = about_us.getString("our_fleet");
                        String our_fleet_description = about_us.getString("our_fleet_description");
                        String miles_to_go = about_us.getString("miles_to_go");
                        String miles_to_go_description = about_us.getString("miles_to_go_description");

                        String page_banner = about_us.getString("page_banner");
                        String our_heritage_image = about_us.getString("our_heritage_image");
                        String our_mission_image = about_us.getString("our_mission_image");
                        String our_vision_image = about_us.getString("our_vision_image");
                        String our_fleet_image = about_us.getString("our_fleet_image");
                        String miles_to_go_image = about_us.getString("miles_to_go_image");
                        String our_values_image_1 = about_us.getString("our_values_image_1");
                        String our_values_image_2 = about_us.getString("our_values_image_2");
                        String our_values_image_3 = about_us.getString("our_values_image_3");
                        String our_values_image_4 = about_us.getString("our_values_image_4");
                        String our_values_image_5 = about_us.getString("our_values_image_5");

                        LoadImage.glideString(activity,binding.imgBanner,page_banner);

                        binding.txtPageHeading.setText(checkString(page_heading,binding.txtPageHeading));
                        binding.txtQuotes.setText(checkString(quote,binding.txtQuotes));
                        binding.txtDesc.setText(checkString(description,binding.txtDesc));

                        binding.imgBanner.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Click.preventTwoClick(v);
                                viewDialog(page_banner);
                            }
                        });


                        LoadImage.glideString(activity,binding.imgOurHeritage,our_heritage_image);
                        binding.txtOurHeritageTitle.setText(checkString(our_heritage));
                        if (our_heritage_description.length()>300){
                            binding.txtOurHeritageDesc.setMaxLines(5);
                            binding.txtOurHeritageDesc.setText(checkString(our_heritage_description));
                            binding.txtOurHeritageMore.setVisibility(View.VISIBLE);
                        }else {
                            binding.txtOurHeritageDesc.setText(checkString(our_heritage_description));
                            binding.txtOurHeritageMore.setVisibility(View.GONE);
                        }
                        binding.imgOurHeritage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Click.preventTwoClick(v);
                                viewDialog(our_heritage_image);
                            }
                        });
                        binding.txtOurHeritageMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Click.preventTwoClick(v);
                                String text = binding.txtOurHeritageMore.getText().toString().trim();
                                if (text.equals(getResources().getString(R.string.moreView1))){
                                    binding.txtOurHeritageDesc.setText(checkString(our_heritage_description));
                                    binding.txtOurHeritageDesc.setMaxLines(1000);
                                    binding.txtOurHeritageMore.setText(getResources().getString(R.string.lessView1));
                                }else if (text.equals(getResources().getString(R.string.lessView1))){
                                    binding.txtOurHeritageDesc.setMaxLines(5);
                                    binding.txtOurHeritageDesc.setText(checkString(our_heritage_description));
                                    binding.txtOurHeritageMore.setText(getResources().getString(R.string.moreView1));
                                }
                            }
                        });

                        LoadImage.glideString(activity,binding.imgOurMission,our_mission_image);
                        binding.txtOurMissionTitle.setText(checkString(our_mission));
                        binding.txtOurMissionDesc.setText(checkString(our_mission_description));

                        LoadImage.glideString(activity,binding.imgOurVission,our_vision_image);
                        binding.txtOurVissionTitle.setText(checkString(our_vision));
                        binding.txtOurVissionDesc.setText(checkString(our_vision_description));

                        binding.txtOurValueTitle.setText(checkString(our_values));

                        LoadImage.glideString(activity,binding.imgOurValue1,our_values_image_1);
                        binding.txtOurValueTitle1.setText(checkString(our_values_title_1));
                        binding.txtOurValueDesc1.setText(checkString(our_values_description_1));

                        LoadImage.glideString(activity,binding.imgOurValue2,our_values_image_2);
                        binding.txtOurValueTitle2.setText(checkString(our_values_title_2));
                        binding.txtOurValueDesc2.setText(checkString(our_values_description_2));

                        LoadImage.glideString(activity,binding.imgOurValue3,our_values_image_3);
                        binding.txtOurValueTitle3.setText(checkString(our_values_title_3));
                        binding.txtOurValueDesc3.setText(checkString(our_values_description_3));

                        LoadImage.glideString(activity,binding.imgOurValue4,our_values_image_4);
                        binding.txtOurValueTitle4.setText(checkString(our_values_title_4));
                        binding.txtOurValueDesc4.setText(checkString(our_values_description_4));

                        LoadImage.glideString(activity,binding.imgOurValue5,our_values_image_5);
                        binding.txtOurValueTitle5.setText(checkString(our_values_title_5));
                        binding.txtOurValueDesc5.setText(checkString(our_values_description_5));

                        LoadImage.glideString(activity,binding.imgOurFleet,our_fleet_image);
                        binding.txtOurFleetTitle.setText(checkString(our_fleet));
                        if (our_fleet_description.length()>300){
                            binding.txtOurFleetDesc.setMaxLines(5);
                            binding.txtOurFleetDesc.setText(checkString(our_fleet_description));
                            binding.txtOurFleetMore.setVisibility(View.VISIBLE);
                        }else {
                            binding.txtOurFleetDesc.setText(checkString(our_fleet_description));
                            binding.txtOurFleetMore.setVisibility(View.GONE);
                        }
                        binding.imgOurFleet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Click.preventTwoClick(v);
                                viewDialog(our_fleet_image);
                            }
                        });
                        binding.txtOurFleetMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Click.preventTwoClick(v);
                                String text = binding.txtOurFleetMore.getText().toString().trim();
                                if (text.equals(getResources().getString(R.string.moreView1))){
                                    binding.txtOurFleetDesc.setText(checkString(our_fleet_description));
                                    binding.txtOurFleetDesc.setMaxLines(1000);
                                    binding.txtOurFleetMore.setText(getResources().getString(R.string.lessView1));
                                }else if (text.equals(getResources().getString(R.string.lessView1))){
                                    binding.txtOurFleetDesc.setMaxLines(5);
                                    binding.txtOurFleetDesc.setText(checkString(our_fleet_description));
                                    binding.txtOurFleetMore.setText(getResources().getString(R.string.moreView1));
                                }
                            }
                        });

                        LoadImage.glideString(activity,binding.imgMile,miles_to_go_image);
                        binding.txtMileTitle.setText(checkString(miles_to_go));
                        if (miles_to_go_description.length()>300){
                            binding.txtMileDesc.setMaxLines(5);
                            binding.txtMileDesc.setText(checkString(miles_to_go_description));
                            binding.txtMileMore.setVisibility(View.VISIBLE);
                        }else {
                            binding.txtMileDesc.setText(checkString(miles_to_go_description));
                            binding.txtMileMore.setVisibility(View.GONE);
                        }
                        binding.imgMile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Click.preventTwoClick(v);
                                viewDialog(miles_to_go_image);
                            }
                        });
                        binding.txtMileMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Click.preventTwoClick(v);
                                String text = binding.txtMileMore.getText().toString().trim();
                                if (text.equals(getResources().getString(R.string.moreView1))){
                                    binding.txtMileDesc.setText(checkString(miles_to_go_description));
                                    binding.txtMileDesc.setMaxLines(1000);
                                    binding.txtMileMore.setText(getResources().getString(R.string.lessView1));
                                }else if (text.equals(getResources().getString(R.string.lessView1))){
                                    binding.txtMileDesc.setMaxLines(5);
                                    binding.txtMileDesc.setText(checkString(miles_to_go_description));
                                    binding.txtMileMore.setText(getResources().getString(R.string.moreView1));
                                }
                            }
                        });

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);
                })
                .run("hitAboutUsDetails");
    }

    private String checkString(String string, TextView textView){
        String value = "";
        if (string==null || string.equals("") || string.equals("null")){
            textView.setVisibility(View.GONE);
        }else {
            value = string;
        }
        return value;
    }

    private String checkString(String string){
        String value = "";
        if (string!=null || !string.equals("") || !string.equals("null")){
            value = string;
        }
        return value;
    }

    private void viewDialog(String imagePath) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_image_view);

        PhotoView imageView = dialog.findViewById(R.id.imageView);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);

        LoadImage.glideString(activity,imageView,imagePath);

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