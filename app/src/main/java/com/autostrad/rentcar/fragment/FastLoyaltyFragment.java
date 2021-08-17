package com.autostrad.rentcar.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.FragmentFastLoyaltyBinding;

public class FastLoyaltyFragment extends Fragment {

    private Context context;
    private FragmentFastLoyaltyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fast_loyalty, container, false);
            context = inflater.getContext();
            initView();
        }

        return binding.getRoot();
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

    private void initView(){

    }

    public static FastLoyaltyFragment newInstance() {
        FastLoyaltyFragment fragment = new FastLoyaltyFragment();
        return fragment;
    }

}
