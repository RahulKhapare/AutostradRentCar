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
import com.example.fastuae.databinding.FragmentUpcomingReservationBinding;
import com.example.fastuae.model.BookingModel;

import java.util.ArrayList;
import java.util.List;

public class UpcomingReservationFragment extends Fragment {

    private Context context;
    private FragmentUpcomingReservationBinding binding;
    private List<BookingModel> bookingModelList;
    private BookingCarAdapter bookingCarAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming_reservation, container, false);
            context = inflater.getContext();
            initView();
        }

        return binding.getRoot();
    }

    private void initView(){

        bookingModelList = new ArrayList<>();
        bookingCarAdapter = new BookingCarAdapter(context,bookingModelList);
        binding.recyclerReservation.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerReservation.setAdapter(bookingCarAdapter);
        setData();

    }

    private void setData(){
        BookingModel model = new BookingModel();
        model.setCarName("Hyundai Accent 1.6");
        model.setReservationNumber("Reservation Number: 9906567753");
        model.setFrom("Dubai International Airport\nTuesday, January 12, 2021, 12:30 PM");
        model.setTo("Dubai International Airport\nTuesday, January 12, 2021, 12:30 PM");

        bookingModelList.add(model);
        bookingModelList.add(model);

        bookingCarAdapter.notifyDataSetChanged();
    }

    public static UpcomingReservationFragment newInstance() {
        UpcomingReservationFragment fragment = new UpcomingReservationFragment();
        return fragment;
    }

}
