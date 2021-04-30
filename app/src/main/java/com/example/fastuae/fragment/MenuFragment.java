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
import com.example.fastuae.activity.EnquireyNowActivity;
import com.example.fastuae.activity.FAQActivity;
import com.example.fastuae.activity.LoastAndFoundActivity;
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

        binding.lnrEnquireyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(context, EnquireyNowActivity.class);
                startActivity(intent);
            }
        });

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

        binding.lnrLeasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.lnrLostFound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(context, LoastAndFoundActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgEnquireyNowRight.setVisibility(View.GONE);
            binding.imgEnquireyNowLeft.setVisibility(View.VISIBLE);

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

            binding.imgLeasingRight.setVisibility(View.GONE);
            binding.imgLeasingLeft.setVisibility(View.VISIBLE);

            binding.imgLoastFoundRight.setVisibility(View.GONE);
            binding.imgLoastFoundLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgEnquireyNowRight.setVisibility(View.VISIBLE);
            binding.imgEnquireyNowLeft.setVisibility(View.GONE);

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

            binding.imgLeasingRight.setVisibility(View.VISIBLE);
            binding.imgLeasingLeft.setVisibility(View.GONE);

            binding.imgLoastFoundRight.setVisibility(View.VISIBLE);
            binding.imgLoastFoundLeft.setVisibility(View.GONE);

        }
    }


    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onDestroyView()
    {
        if (binding.getRoot() != null)
        {
            ViewGroup parentViewGroup = (ViewGroup) binding.getRoot().getParent();

            if (parentViewGroup != null)
            {
                parentViewGroup.removeAllViews();
            }
        }

        super.onDestroyView();
    }
}
