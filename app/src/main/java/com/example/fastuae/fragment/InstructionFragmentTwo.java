package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.fastuae.R;
import com.example.fastuae.databinding.FragmentInstructionBinding;

public class InstructionFragmentTwo extends Fragment {

    private Context context;
    private FragmentInstructionBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_instruction, container, false);
            context = inflater.getContext();
        }

        return binding.getRoot();
    }

    public static InstructionFragmentTwo newInstance() {
        InstructionFragmentTwo fragment = new InstructionFragmentTwo();
        return fragment;
    }

}