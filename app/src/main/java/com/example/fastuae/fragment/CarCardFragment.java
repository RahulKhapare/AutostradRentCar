package com.example.fastuae.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;
import com.example.fastuae.activity.LanguageSelectionActivity;
import com.example.fastuae.adapter.ViewPagerSwipeAdapter;
import com.example.fastuae.model.CarModel;
import com.example.fastuae.util.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class CarCardFragment extends Fragment {

    private Context context;
    private ViewPagerSwipeAdapter swipeAdapter;
    private ViewPager viewPagesSwipe;
    private List<CarModel> carModelList;
    private ScrollView scrollView;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_card, container, false);
        context = inflater.getContext();

        carModelList = new ArrayList<>();
        CarModel model = new CarModel();
        model.setName("Murcedes suv");
        model.setGroup("Group A");
        model.setType("Automatic");
        model.setModel("Suv");
        model.setSeat("5 Seat");
        model.setEngine("Engine");
        model.setDore("3 Door");
        model.setAedNow("1160 AED");
        model.setAedLater("1200 AED");
        model.setSuitcase("2 Suitcase");
        model.setPetrol("Petrol");

        carModelList.add(model);
        carModelList.add(model);
        carModelList.add(model);
        carModelList.add(model);
        carModelList.add(model);

        loadingDialog = new LoadingDialog(context);
        scrollView = view.findViewById(R.id.scrollview);
        viewPagesSwipe = view.findViewById(R.id.viewPagesSwipe);

        swipeAdapter = new ViewPagerSwipeAdapter(context, carModelList);
        viewPagesSwipe.setPageTransformer(true, new ViewPagerStack());
        viewPagesSwipe.setOffscreenPageLimit(3);
        viewPagesSwipe.setAdapter(swipeAdapter);


//        ProgressView.show(context,loadingDialog);
//        new Handler().postDelayed(() -> {
//            ProgressView.dismiss(loadingDialog);
//            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//        }, 1000);

        return view;
    }


    private class ViewPagerStack implements ViewPager.PageTransformer {
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position >= 0) {
                page.setScaleX(0.7f - 0.05f * position);
                page.setScaleY(0.7f);
                page.setTranslationX(-page.getWidth() * position);
                page.setTranslationY(-30 * position);
            }
        }
    }

    public static CarCardFragment newInstance() {
        CarCardFragment fragment = new CarCardFragment();
        return fragment;
    }
}
