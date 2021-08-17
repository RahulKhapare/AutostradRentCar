package com.autostrad.rentcar.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.JsonList;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.CategorySelectionAdapter;
import com.autostrad.rentcar.databinding.ActivityFAQBinding;
import com.autostrad.rentcar.fragment.GeneralFragment;
import com.autostrad.rentcar.fragment.InsuranceRelatedFragment;
import com.autostrad.rentcar.fragment.PaymentRelatedFragment;
import com.autostrad.rentcar.fragment.VehicleRelatedFragment;
import com.autostrad.rentcar.model.CategoryModel;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity implements CategorySelectionAdapter.onClick{

    private FAQActivity activity = this;
    private ActivityFAQBinding binding;
    private FragmentManager fragmentManager;
    private List<CategoryModel> categoryModelList;
    private CategorySelectionAdapter adapter;
    private GeneralFragment generalFragment;
    private PaymentRelatedFragment paymentRelatedFragment;
    private VehicleRelatedFragment vehicleRelatedFragment;
    private InsuranceRelatedFragment insuranceRelatedFragment;

    public static JsonList generalJsonList = null;
    public static JsonList paymentJsonList = null;
    public static JsonList vehicleJsonList = null;
    public static JsonList insuranceJsonList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_f_a_q);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.faqs));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragmentManager = getSupportFragmentManager();
        categoryModelList = new ArrayList<>();
        adapter = new CategorySelectionAdapter(activity, categoryModelList,Config.FAQ_TAG);
        binding.recyclerFAQCategory.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recyclerFAQCategory.setNestedScrollingEnabled(false);
        binding.recyclerFAQCategory.setAdapter(adapter);

        setData();

        generalFragment = GeneralFragment.newInstance();
        fragmentLoader(generalFragment, Config.General);

    }

    private void setData() {

        CategoryModel categoryModel1 = new CategoryModel();
        categoryModel1.setCategory_name_slug(Config.General);
        categoryModel1.setCategoryName(getResources().getString(R.string.general));
        categoryModelList.add(categoryModel1);
        CategoryModel categoryModel2 = new CategoryModel();
        categoryModel2.setCategory_name_slug(Config.Payment_Related);
        categoryModel2.setCategoryName(getResources().getString(R.string.paymentRelated));
        categoryModelList.add(categoryModel2);
        CategoryModel categoryModel3 = new CategoryModel();
        categoryModel3.setCategory_name_slug(Config.Vehicle_Related);
        categoryModel3.setCategoryName(getResources().getString(R.string.vehicleRelated));
        categoryModelList.add(categoryModel3);
        CategoryModel categoryModel4 = new CategoryModel();
        categoryModel4.setCategory_name_slug(Config.Insurance_Related);
        categoryModel4.setCategoryName(getResources().getString(R.string.insuranceRelated));
        categoryModelList.add(categoryModel4);

        adapter.notifyDataSetChanged();

    }

    public void fragmentLoader(Fragment fragment, String tag) {
        Config.currentFAQFlag = tag;
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit)
                .replace(R.id.fragmentFrame, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onCategoryClick(String categoryFlag) {
        String currentFlag = Config.currentFAQFlag;
        if (categoryFlag.equals(Config.General)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.General)) {
                generalFragment = GeneralFragment.newInstance();
                fragmentLoader(generalFragment, Config.General);
            }
        } else if (categoryFlag.equals(Config.Payment_Related)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.Payment_Related)) {
                paymentRelatedFragment = PaymentRelatedFragment.newInstance();
                fragmentLoader(paymentRelatedFragment, Config.Payment_Related);
            }
        } else if (categoryFlag.equals(Config.Vehicle_Related)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.Vehicle_Related)) {
                vehicleRelatedFragment = VehicleRelatedFragment.newInstance();
                fragmentLoader(vehicleRelatedFragment, Config.Vehicle_Related);
            }
        } else if (categoryFlag.equals(Config.Insurance_Related)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.Insurance_Related)) {
                insuranceRelatedFragment = InsuranceRelatedFragment.newInstance();
                fragmentLoader(insuranceRelatedFragment, Config.Insurance_Related);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}