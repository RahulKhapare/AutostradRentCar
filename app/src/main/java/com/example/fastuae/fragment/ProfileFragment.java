package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastuae.R;
import com.example.fastuae.adapter.CategorySelectionAdapter;
import com.example.fastuae.databinding.FragmentProfileBinding;
import com.example.fastuae.model.CategoryModel;
import com.example.fastuae.util.Config;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements CategorySelectionAdapter.onClick {

    private Context context;
    private FragmentProfileBinding binding;
    private List<CategoryModel> categoryModelList;
    private CategorySelectionAdapter adapter;

    private MyAccountFragment myAccountFragment;
    private DocumentFragment documentFragment;
    private AdditionalDriverFragment additionalDriverFragment;
    private BookingFragment bookingFragment;
    private ManagePaymentFragment managePaymentFragment;
    private InvoiceFragment invoiceFragment;
    private SalikChargesFragment salikChargesFragment;
    private TrafficLinesFragment trafficLinesFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
            context = inflater.getContext();

            initView();
        }

        return binding.getRoot();
    }

    private void initView() {

        categoryModelList = new ArrayList<>();
        adapter = new CategorySelectionAdapter(context, categoryModelList, ProfileFragment.this);
        binding.recyclerProfileCategory.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        binding.recyclerProfileCategory.setNestedScrollingEnabled(false);
        binding.recyclerProfileCategory.setAdapter(adapter);
        setData();

        myAccountFragment = MyAccountFragment.newInstance();
        fragmentLoader(myAccountFragment, Config.My_Account);

    }

    public void fragmentLoader(Fragment fragment, String tag) {
        Config.currentProfileFlag = tag;
        getChildFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit)
                .replace(R.id.fragmentFrame, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }


    private void setData() {

        CategoryModel categoryModel1 = new CategoryModel();
        categoryModel1.setCategoryFlag(Config.My_Account);
        categoryModel1.setCategoryName(getResources().getString(R.string.myAccount));
        categoryModelList.add(categoryModel1);
        CategoryModel categoryModel2 = new CategoryModel();
        categoryModel2.setCategoryFlag(Config.Documents);
        categoryModel2.setCategoryName(getResources().getString(R.string.document));
        categoryModelList.add(categoryModel2);
        CategoryModel categoryModel3 = new CategoryModel();
        categoryModel3.setCategoryFlag(Config.Additional_Driver);
        categoryModel3.setCategoryName(getResources().getString(R.string.additionalDriver));
        categoryModelList.add(categoryModel3);
        CategoryModel categoryModel4 = new CategoryModel();
        categoryModel4.setCategoryFlag(Config.Booking);
        categoryModel4.setCategoryName(getResources().getString(R.string.booking));
        categoryModelList.add(categoryModel4);
        CategoryModel categoryModel5 = new CategoryModel();
        categoryModel5.setCategoryFlag(Config.Manage_Payments);
        categoryModel5.setCategoryName(getResources().getString(R.string.managePayment));
        categoryModelList.add(categoryModel5);
        CategoryModel categoryModel6 = new CategoryModel();
        categoryModel6.setCategoryFlag(Config.Invoices);
        categoryModel6.setCategoryName(getResources().getString(R.string.invoices));
        categoryModelList.add(categoryModel6);
        CategoryModel categoryModel7 = new CategoryModel();
        categoryModel7.setCategoryFlag(Config.Salik_Charges);
        categoryModel7.setCategoryName(getResources().getString(R.string.salik_Charges));
        categoryModelList.add(categoryModel7);
        CategoryModel categoryModel8 = new CategoryModel();
        categoryModel8.setCategoryFlag(Config.Traffic_Lines);
        categoryModel8.setCategoryName(getResources().getString(R.string.traffic_Lines));
        categoryModelList.add(categoryModel8);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onCategoryClick(String categoryFlag) {
        String currentFlag = Config.currentProfileFlag;
        if (categoryFlag.equals(Config.My_Account)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.My_Account)) {
                myAccountFragment = MyAccountFragment.newInstance();
                fragmentLoader(myAccountFragment, Config.My_Account);
            }
        } else if (categoryFlag.equals(Config.Documents)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.Documents)) {
                documentFragment = DocumentFragment.newInstance();
                fragmentLoader(documentFragment, Config.Documents);
            }
        } else if (categoryFlag.equals(Config.Additional_Driver)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.Additional_Driver)) {
                additionalDriverFragment = AdditionalDriverFragment.newInstance();
                fragmentLoader(additionalDriverFragment, Config.Additional_Driver);
            }
        } else if (categoryFlag.equals(Config.Booking)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.Booking)) {
                bookingFragment = BookingFragment.newInstance();
                fragmentLoader(bookingFragment, Config.Booking);
            }
        } else if (categoryFlag.equals(Config.Manage_Payments)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.Manage_Payments)) {
                managePaymentFragment = ManagePaymentFragment.newInstance();
                fragmentLoader(managePaymentFragment, Config.Manage_Payments);
            }
        } else if (categoryFlag.equals(Config.Invoices)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.Invoices)) {
                invoiceFragment = InvoiceFragment.newInstance();
                fragmentLoader(invoiceFragment, Config.Invoices);
            }
        } else if (categoryFlag.equals(Config.Salik_Charges)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.Salik_Charges)) {
                salikChargesFragment = SalikChargesFragment.newInstance();
                fragmentLoader(salikChargesFragment, Config.Salik_Charges);
            }
        } else if (categoryFlag.equals(Config.Traffic_Lines)) {
            if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.Traffic_Lines)) {
                trafficLinesFragment = TrafficLinesFragment.newInstance();
                fragmentLoader(trafficLinesFragment, Config.Traffic_Lines);
            }
        }

    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

}
