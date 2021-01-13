package com.example.fastuae.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.CarUpgradeAdapter;
import com.example.fastuae.databinding.ActivityCarDetailOneBinding;
import com.example.fastuae.model.CarUpgradeModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.WindowView;
import com.example.fastuae.util.ZoomFadeTransformer;

import java.util.ArrayList;
import java.util.List;

public class CarDetailOneActivity extends AppCompatActivity {

    private CarDetailOneActivity activity = this;
    private ActivityCarDetailOneBinding binding;
    private Session session;
    private String flag;
    private int currentPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_detail_one);
        session = new Session(activity);
        flag = session.getString(P.languageFlag);
        initView();
        updateIcons();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.carDetails));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setData();
        onClick();
    }

    private void setData() {

        binding.txtCarName.setText("Mercedes SUV");
        binding.txtPickUpLocation.setText("Mon, 10 Jan, 10:00 AM\n123, Building Name, Street Name, City Name,\nPincode 400000");
        binding.txtDropOffLocation.setText("Mon, 10 Jan, 10:00 AM\n123, Building Name, Street Name, City Name,\nPincode 400000");

        binding.txtSeat.setText("5 Seat");
        binding.txtEngineOne.setText("Engine");
        binding.txtEngineTwo.setText("Engine");
        binding.txtPetrol.setText("Petrol");
        binding.txtAutomaticOne.setText("Automatic");
        binding.txtAutomaticTwo.setText("Automatic");
        binding.txtDoor.setText("2 Door");
        binding.txtSuitcaseOne.setText("2 Suitcase");
        binding.txtSuitcaseTwo.setText("2 Suitcase");
        binding.txtMessageOne.setText("Free Booking");
        binding.txtMessageTwo.setText("Free Cancellation");
        binding.txtMessageThree.setText("Pay 100% at the counter or pay with online check-in-24hrs before your pickup time to secure your car and get 5% off");

        if (flag.equals(Config.ARABIC)) {
            binding.txtCarName.setGravity(Gravity.RIGHT);
        }
    }


    private void onClick() {


        binding.txtUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                updateCarDialog();
            }
        });

        binding.txtBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity, CarDetailTwoActivity.class);
                startActivity(intent);
            }
        });

        binding.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BitmapDrawable drawable = (BitmapDrawable) binding.img1.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                binding.imgCar.setImageBitmap(bitmap);

            }
        });
        binding.img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BitmapDrawable drawable = (BitmapDrawable) binding.img2.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                binding.imgCar.setImageBitmap(bitmap);
            }
        });
        binding.img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BitmapDrawable drawable = (BitmapDrawable) binding.img3.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                binding.imgCar.setImageBitmap(bitmap);
            }
        });

        binding.lnrPromotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkPromotionView();
            }
        });

        binding.lnrEstimated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkEstimationView();
            }
        });

        binding.lnrInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkInsuranceView();
            }
        });

        binding.lnrTermCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkTermView();
            }
        });
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgInsuranceRight.setVisibility(View.GONE);
            binding.imgInsuranceLeft.setVisibility(View.VISIBLE);

            binding.imgPromotionRight.setVisibility(View.GONE);
            binding.imgPromotionLeft.setVisibility(View.VISIBLE);

            binding.imgEstimatedRight.setVisibility(View.GONE);
            binding.imgEstimatedLeft.setVisibility(View.VISIBLE);

            binding.imgTermRight.setVisibility(View.GONE);
            binding.imgTemLeft.setVisibility(View.VISIBLE);


        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgInsuranceRight.setVisibility(View.VISIBLE);
            binding.imgInsuranceLeft.setVisibility(View.GONE);

            binding.imgPromotionRight.setVisibility(View.VISIBLE);
            binding.imgPromotionLeft.setVisibility(View.GONE);

            binding.imgEstimatedRight.setVisibility(View.VISIBLE);
            binding.imgEstimatedLeft.setVisibility(View.GONE);

            binding.imgTermRight.setVisibility(View.VISIBLE);
            binding.imgTemLeft.setVisibility(View.GONE);

        }
    }

    private void checkPromotionView() {
        if (binding.viewPromotion.getVisibility() == View.VISIBLE) {
            binding.viewPromotion.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPromotionLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPromotionRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewPromotion.getVisibility() == View.GONE) {
            binding.viewPromotion.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgPromotionLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgPromotionRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void checkInsuranceView() {
        if (binding.viewInsurance.getVisibility() == View.VISIBLE) {
            binding.viewInsurance.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgInsuranceLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgInsuranceRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewInsurance.getVisibility() == View.GONE) {
            binding.viewInsurance.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgInsuranceLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgInsuranceRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void checkEstimationView() {
        if (binding.viewEstimated.getVisibility() == View.VISIBLE) {
            binding.viewEstimated.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgEstimatedLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgEstimatedRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewEstimated.getVisibility() == View.GONE) {
            binding.viewEstimated.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgEstimatedLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgEstimatedRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void checkTermView() {
        if (binding.viewTerm.getVisibility() == View.VISIBLE) {
            binding.viewTerm.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgTemLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgTermRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.viewTerm.getVisibility() == View.GONE) {
            binding.viewTerm.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgTemLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgTermRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }


    private void updateCarDialog() {

        List<CarUpgradeModel> carUpgradeModelList = new ArrayList<>();
        carUpgradeModelList.add(new CarUpgradeModel(R.drawable.ic_car, "Hyundai Creta", "Reservation Fee: AED 100", "Estimated Total: AED 200"));
        carUpgradeModelList.add(new CarUpgradeModel(R.drawable.ic_car_black, "Maruti Suzuki", "Reservation Fee: AED 300", "Estimated Total: AED 400"));
        carUpgradeModelList.add(new CarUpgradeModel(R.drawable.ic_car_blue_new, "Maruti Alto", "Reservation Fee: AED 500", "Estimated Total: AED 600"));
        carUpgradeModelList.add(new CarUpgradeModel(R.drawable.ic_car_new, "Hyundai Creta", "Reservation Fee: AED 700", "Estimated Total: AED 800"));
        carUpgradeModelList.add(new CarUpgradeModel(R.drawable.ic_car_red_new, "Maruti Zen", "Reservation Fee: AED 900", "Estimated Total: AED 1000"));

        CarUpgradeAdapter adapter = new CarUpgradeAdapter(activity, carUpgradeModelList);

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_update_car);

        ViewPager viewPager = dialog.findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

//        viewPager.setPageMargin(40);
//        viewPager.setClipToPadding(false);
//        viewPager.setClipChildren(false);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setPageTransformer(false, new ZoomFadeTransformer(viewPager.getPaddingLeft(), 0.85f, 0));


//        viewPager.setPageMargin(100);
//        viewPager.setPageTransformer(false, new ViewPager.PageTransformer()
//        {
//            @Override
//            public void transformPage(View page, float position)
//            {
//                int pageWidth = viewPager.getMeasuredWidth() -
//                        viewPager.getPaddingLeft() - viewPager.getPaddingRight();
//                int pageHeight = viewPager.getHeight();
//                int paddingLeft = viewPager.getPaddingLeft();
//                float transformPos = (float) (page.getLeft() -
//                        (viewPager.getScrollX() + paddingLeft)) / pageWidth;
//                int max = pageHeight / 10;
//                if (transformPos < -1)
//                {
//                    // [-Infinity,-1)
//                    // This page is way off-screen to the left.
//                    page.setAlpha(0.5f);// to make left transparent
//                    page.setScaleY(0.7f);
//                }
//                else if (transformPos <= 1)
//                {
//                    // [-1,1]
//                    page.setScaleY(1f);
//                }
//                else
//                {
//                    // (1,+Infinity]
//                    // This page is way off-screen to the right.
//                    page.setAlpha(0.5f);// to make right transparent
//                    page.setScaleY(0.7f);
//                }
//            }
//        });

//        viewPager.setPageMargin(40);
//        viewPager.setClipToPadding(false);
//        viewPager.setClipChildren(false);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
//                    @Override
//                    public void transformPage(View page, float position) {
//                        float r = 1 - Math.abs(position);
//                        page.setScaleY(0.85f + r * 0.15f);
//
////                        int pageWidth = viewPager.getMeasuredWidth() -
////                                viewPager.getPaddingLeft() - viewPager.getPaddingRight();
////                        int paddingLeft = viewPager.getPaddingLeft();
////                        float transformPos = (float) (page.getLeft() -
////                                (viewPager.getScrollX() + paddingLeft)) / pageWidth;
////
////                        if (transformPos < -1){
////                            page.setScaleY(0.8f);
////                        } else if (transformPos <= 1) {
////                            page.setScaleY(1f);
////                        } else {
////                            page.setScaleY(0.8f);
////                        }
//
//                    }
//                }
//        );

//        viewPager.setClipToPadding(false);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int width = displayMetrics.widthPixels;
//        int paddingToSet = width/4; //set this ratio according to how much of the next and previos screen you want to show.
//        viewPager.setPadding(paddingToSet,0,paddingToSet,0);
//
//        viewPager.setClipToPadding(false);
//        viewPager.setClipChildren(false);
//        viewPager.setOffscreenPageLimit(3);
//        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                float r = 1 - Math.abs(position);
//                page.setScaleY(0.85f + r * 0.15f);
//            }
//        });
//        viewPager.setPageTransformer(compositePageTransformer);

        TextView txtCarName = dialog.findViewById(R.id.txtCarName);
        TextView txtReservationFee = dialog.findViewById(R.id.txtReservationFee);
        TextView txtEstimateTotal = dialog.findViewById(R.id.txtEstimateTotal);
        TextView txtCancel = dialog.findViewById(R.id.txtCancel);
        TextView txtUpgrade = dialog.findViewById(R.id.txtUpgrade);
        ImageView imgLeft = dialog.findViewById(R.id.imgLeft);
        ImageView imgRight = dialog.findViewById(R.id.imgRight);

        CarUpgradeModel model = carUpgradeModelList.get(0);
        txtCarName.setText(model.getCarName());
        txtReservationFee.setText(model.getReservationFee());
        txtEstimateTotal.setText(model.getEstimateTotal());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                currentPosition = position;
                CarUpgradeModel model = carUpgradeModelList.get(position);
                txtCarName.setText(model.getCarName());
                txtReservationFee.setText(model.getReservationFee());
                txtEstimateTotal.setText(model.getEstimateTotal());
            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (currentPosition > 0 && currentPosition <= carUpgradeModelList.size()) {
                    currentPosition--;
                    viewPager.setCurrentItem(currentPosition);
                }
            }
        });

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (currentPosition < carUpgradeModelList.size()) {
                    currentPosition++;
                    viewPager.setCurrentItem(currentPosition);
                }
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        txtUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
                binding.imgCar.setImageResource(carUpgradeModelList.get(currentPosition).getImage());
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }


    private int getCurrentItem(RecyclerView recyclerView) {
        return ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }
}