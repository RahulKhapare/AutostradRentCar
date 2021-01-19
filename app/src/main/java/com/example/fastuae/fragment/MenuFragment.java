package com.example.fastuae.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.activity.ContactUsActivity;
import com.example.fastuae.activity.FAQActivity;
import com.example.fastuae.activity.LocationActivity;
import com.example.fastuae.activity.SpecialOffersActivity;
import com.example.fastuae.databinding.FragmentMenuBinding;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;

public class MenuFragment extends Fragment {

    private Context context;
    private FragmentMenuBinding binding;
    private Session session;
    private String flag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
            context = inflater.getContext();
            session = new Session(context);
            flag = session.getString(P.languageFlag);
            updateIcons();
            initView();
        }

        return binding.getRoot();
    }

    private void initView(){

        onClick();
    }

    private void onClick(){

        binding.lnrFastRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.lnrSpecialOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(context, SpecialOffersActivity.class);
                startActivity(intent);
            }
        });

        binding.lnrFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(context, FAQActivity.class);
                startActivity(intent);
            }
        });

        binding.lnrLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(context, LocationActivity.class);
                startActivity(intent);
            }
        });

        binding.lnrAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.lnrContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(context, ContactUsActivity.class);
                startActivity(intent);
            }
        });

        binding.lnrGetReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.lnrCareers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.lnrRentalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgRewardRight.setVisibility(View.GONE);
            binding.imgRewardLeft.setVisibility(View.VISIBLE);

            binding.imgOffersRight.setVisibility(View.GONE);
            binding.imgOffersLeft.setVisibility(View.VISIBLE);

            binding.imgFaqRight.setVisibility(View.GONE);
            binding.imgFaqLeft.setVisibility(View.VISIBLE);

            binding.imgLocationRight.setVisibility(View.GONE);
            binding.imgLocationLeft.setVisibility(View.VISIBLE);

            binding.imgAboutRight.setVisibility(View.GONE);
            binding.imgAboutLeft.setVisibility(View.VISIBLE);

            binding.imgContactRight.setVisibility(View.GONE);
            binding.imgContactLeft.setVisibility(View.VISIBLE);

            binding.imgReceiptRight.setVisibility(View.GONE);
            binding.imgReceiptLeft.setVisibility(View.VISIBLE);

            binding.imgCareerRight.setVisibility(View.GONE);
            binding.imgCareerLeft.setVisibility(View.VISIBLE);

            binding.imgRentalInfoRight.setVisibility(View.GONE);
            binding.imgRentalInfoLeft.setVisibility(View.VISIBLE);



        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgRewardRight.setVisibility(View.VISIBLE);
            binding.imgRewardLeft.setVisibility(View.GONE);

            binding.imgOffersRight.setVisibility(View.VISIBLE);
            binding.imgOffersLeft.setVisibility(View.GONE);

            binding.imgFaqRight.setVisibility(View.VISIBLE);
            binding.imgFaqLeft.setVisibility(View.GONE);

            binding.imgLocationRight.setVisibility(View.VISIBLE);
            binding.imgLocationLeft.setVisibility(View.GONE);

            binding.imgAboutRight.setVisibility(View.VISIBLE);
            binding.imgAboutLeft.setVisibility(View.GONE);

            binding.imgContactRight.setVisibility(View.VISIBLE);
            binding.imgContactLeft.setVisibility(View.GONE);

            binding.imgReceiptRight.setVisibility(View.VISIBLE);
            binding.imgReceiptLeft.setVisibility(View.GONE);

            binding.imgCareerRight.setVisibility(View.VISIBLE);
            binding.imgCareerLeft.setVisibility(View.GONE);

            binding.imgRentalInfoRight.setVisibility(View.VISIBLE);
            binding.imgRentalInfoLeft.setVisibility(View.GONE);

        }
    }


    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

}
