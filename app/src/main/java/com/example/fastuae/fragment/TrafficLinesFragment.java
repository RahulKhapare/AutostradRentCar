package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.fastuae.R;
import com.example.fastuae.databinding.FragmentMenuBinding;
import com.example.fastuae.databinding.FragmentTrafficLinesBinding;

public class TrafficLinesFragment extends Fragment {

    private Context context;
    private FragmentTrafficLinesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_traffic_lines, container, false);
            context = inflater.getContext();
        }

        return binding.getRoot();
    }

    public static TrafficLinesFragment newInstance() {
        TrafficLinesFragment fragment = new TrafficLinesFragment();
        return fragment;
    }

}
