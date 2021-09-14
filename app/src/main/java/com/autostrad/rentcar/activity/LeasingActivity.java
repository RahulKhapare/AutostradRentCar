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

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityLeasingBinding;
import com.autostrad.rentcar.model.CareerModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;
import com.github.chrisbanes.photoview.PhotoView;

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

        ProgressView.show(activity,loadingDialog);
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
                        String who_is_it_right_for_points = json.getString("who_is_it_right_for_points");
                        who_is_it_right_for_points = who_is_it_right_for_points.replaceAll("\"", Matcher.quoteReplacement("\\\""));
                        Log.e("TAG", "hitLeasingData121212: "+ who_is_it_right_for_points );

                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitLeasingData");

    }


    private void  onClick(){


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
                if (binding.imgDownOne.getVisibility()==View.VISIBLE){
                    binding.imgDownOne.setVisibility(View.GONE);
                    binding.imgUpOne.setVisibility(View.VISIBLE);
                    binding.lnrViewOne.setVisibility(View.VISIBLE);
                }else if (binding.imgUpOne.getVisibility()==View.VISIBLE){
                    binding.imgDownOne.setVisibility(View.VISIBLE);
                    binding.imgUpOne.setVisibility(View.GONE);
                    binding.lnrViewOne.setVisibility(View.GONE);
                }
            }
        });


        binding.lnrClickTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownTwo.getVisibility()==View.VISIBLE){
                    binding.imgDownTwo.setVisibility(View.GONE);
                    binding.imgUpTwo.setVisibility(View.VISIBLE);
                    binding.lnrViewTwo.setVisibility(View.VISIBLE);
                }else if (binding.imgUpTwo.getVisibility()==View.VISIBLE){
                    binding.imgDownTwo.setVisibility(View.VISIBLE);
                    binding.imgUpTwo.setVisibility(View.GONE);
                    binding.lnrViewTwo.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownThree.getVisibility()==View.VISIBLE){
                    binding.imgDownThree.setVisibility(View.GONE);
                    binding.imgUpThree.setVisibility(View.VISIBLE);
                    binding.lnrViewThree.setVisibility(View.VISIBLE);
                }else if (binding.imgUpThree.getVisibility()==View.VISIBLE){
                    binding.imgDownThree.setVisibility(View.VISIBLE);
                    binding.imgUpThree.setVisibility(View.GONE);
                    binding.lnrViewThree.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownFour.getVisibility()==View.VISIBLE){
                    binding.imgDownFour.setVisibility(View.GONE);
                    binding.imgUpFour.setVisibility(View.VISIBLE);
                    binding.lnrViewFour.setVisibility(View.VISIBLE);
                }else if (binding.imgUpFour.getVisibility()==View.VISIBLE){
                    binding.imgDownFour.setVisibility(View.VISIBLE);
                    binding.imgUpFour.setVisibility(View.GONE);
                    binding.lnrViewFour.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownFive.getVisibility()==View.VISIBLE){
                    binding.imgDownFive.setVisibility(View.GONE);
                    binding.imgUpFive.setVisibility(View.VISIBLE);
                    binding.lnrViewFive.setVisibility(View.VISIBLE);
                }else if (binding.imgUpFive.getVisibility()==View.VISIBLE){
                    binding.imgDownFive.setVisibility(View.VISIBLE);
                    binding.imgUpFive.setVisibility(View.GONE);
                    binding.lnrViewFive.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownSix.getVisibility()==View.VISIBLE){
                    binding.imgDownSix.setVisibility(View.GONE);
                    binding.imgUpSix.setVisibility(View.VISIBLE);
                    binding.lnrViewSix.setVisibility(View.VISIBLE);
                }else if (binding.imgUpSix.getVisibility()==View.VISIBLE){
                    binding.imgDownSix.setVisibility(View.VISIBLE);
                    binding.imgUpSix.setVisibility(View.GONE);
                    binding.lnrViewSix.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownSeven.getVisibility()==View.VISIBLE){
                    binding.imgDownSeven.setVisibility(View.GONE);
                    binding.imgUpSeven.setVisibility(View.VISIBLE);
                    binding.lnrViewSeven.setVisibility(View.VISIBLE);
                }else if (binding.imgUpSeven.getVisibility()==View.VISIBLE){
                    binding.imgDownSeven.setVisibility(View.VISIBLE);
                    binding.imgUpSeven.setVisibility(View.GONE);
                    binding.lnrViewSeven.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownEight.getVisibility()==View.VISIBLE){
                    binding.imgDownEight.setVisibility(View.GONE);
                    binding.imgUpEight.setVisibility(View.VISIBLE);
                    binding.lnrViewEight.setVisibility(View.VISIBLE);
                }else if (binding.imgUpEight.getVisibility()==View.VISIBLE){
                    binding.imgDownEight.setVisibility(View.VISIBLE);
                    binding.imgUpEight.setVisibility(View.GONE);
                    binding.lnrViewEight.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownNine.getVisibility()==View.VISIBLE){
                    binding.imgDownNine.setVisibility(View.GONE);
                    binding.imgUpNine.setVisibility(View.VISIBLE);
                    binding.lnrViewNine.setVisibility(View.VISIBLE);
                }else if (binding.imgUpNine.getVisibility()==View.VISIBLE){
                    binding.imgDownNine.setVisibility(View.VISIBLE);
                    binding.imgUpNine.setVisibility(View.GONE);
                    binding.lnrViewNine.setVisibility(View.GONE);
                }
            }
        });

        binding.lnrClickTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.imgDownTen.getVisibility()==View.VISIBLE){
                    binding.imgDownTen.setVisibility(View.GONE);
                    binding.imgUpTen.setVisibility(View.VISIBLE);
                    binding.lnrViewTen.setVisibility(View.VISIBLE);
                }else if (binding.imgUpTen.getVisibility()==View.VISIBLE){
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

        LoadImage.glide(activity,imageView,imagePath);

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