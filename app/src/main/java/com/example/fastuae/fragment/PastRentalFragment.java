package com.example.fastuae.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fastuae.R;
import com.example.fastuae.adapter.BookingCarAdapter;
import com.example.fastuae.databinding.FragmentPastRentalBinding;
import com.example.fastuae.model.BookingModel;

import java.util.ArrayList;
import java.util.List;

public class PastRentalFragment extends Fragment {

    private Context context;
    private FragmentPastRentalBinding binding;
    private List<BookingModel> bookingModelList;
    private BookingCarAdapter bookingCarAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_past_rental, container, false);
            context = inflater.getContext();
            initView();
        }

        return binding.getRoot();
    }

    private void initView(){

        bookingModelList = new ArrayList<>();
        bookingCarAdapter = new BookingCarAdapter(context,bookingModelList);
        binding.recyclerPastRental.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerPastRental.setNestedScrollingEnabled(false);
        binding.recyclerPastRental.setAdapter(bookingCarAdapter);
        setData();

    }

    private void setData(){
        BookingModel model = new BookingModel();
        model.setCarName("Hyundai Accent 1.6");
        model.setReservationNumber("Reservation Number: 9906567753");
        model.setFrom("Dubai International Airport\nTuesday, January 12, 2021, 12:30 PM");
        model.setTo("Dubai International Airport\nTuesday, January 12, 2021, 12:30 PM");

//        bookingModelList.add(model);
//        bookingModelList.add(model);

        bookingCarAdapter.notifyDataSetChanged();
        checkError();
    }


    private void checkError(){

        if (bookingModelList.isEmpty()){
            binding.txtError.setVisibility(View.VISIBLE);
        }else {
            binding.txtError.setVisibility(View.GONE);
        }
    }

    public static PastRentalFragment newInstance() {
        PastRentalFragment fragment = new PastRentalFragment();
        return fragment;
    }

}
