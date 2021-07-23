package com.example.fastuae.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityLeasingBinding;
import com.example.fastuae.util.WindowView;

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

    }

    private void  onClick(){

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

}