package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.fastuae.R;
import com.example.fastuae.databinding.FragmentAdditionalDriveBinding;
import com.example.fastuae.databinding.FragmentMenuBinding;

public class AdditionalDriverFragment extends Fragment {

    private Context context;
    private FragmentAdditionalDriveBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_additional_drive, container, false);
            context = inflater.getContext();
        }

        return binding.getRoot();
    }

    public static AdditionalDriverFragment newInstance() {
        AdditionalDriverFragment fragment = new AdditionalDriverFragment();
        return fragment;
    }

}
