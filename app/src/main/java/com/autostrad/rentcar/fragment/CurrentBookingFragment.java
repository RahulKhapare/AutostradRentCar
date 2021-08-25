package com.autostrad.rentcar.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.MainActivity;
import com.autostrad.rentcar.activity.ProfileViewActivity;
import com.autostrad.rentcar.adapter.CancelBookingAdapter;
import com.autostrad.rentcar.adapter.CurrentReservationAdapter;
import com.autostrad.rentcar.adapter.PastRentalAdapter;
import com.autostrad.rentcar.databinding.FragmentPastRentalBinding;
import com.autostrad.rentcar.model.BookingModel;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class CurrentBookingFragment extends Fragment implements CurrentReservationAdapter.onClick {

    private Context context;
    private FragmentPastRentalBinding binding;
    private List<BookingModel> bookingModelList;
    private CurrentReservationAdapter currentReservationAdapter;
    private LoadingDialog loadingDialog;
    private Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_past_rental, container, false);
            context = inflater.getContext();
            initView();
        }

        return binding.getRoot();
    }

    private void initView() {
        session = new Session(context);
        loadingDialog = new LoadingDialog(context);
        bookingModelList = new ArrayList<>();
        currentReservationAdapter = new CurrentReservationAdapter(context, bookingModelList, CurrentBookingFragment.this, 2);
        binding.recyclerPastRental.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerPastRental.setAdapter(currentReservationAdapter);
        hitCurrentBookingDetails();
    }

    @Override
    public void extendBooking(BookingModel model) {
        Config.currentProfileFlag = Config.Extend_Booking;
        Config.driverIDFORDOC = model.getBooking_id();
        Config.driverDROP_DATE = model.getDropoff_datetime();
        Intent intent = new Intent(context, ProfileViewActivity.class);
        startActivity(intent);
    }

    private void hitCurrentBookingDetails() {
        bookingModelList.clear();
        currentReservationAdapter.notifyDataSetChanged();
        ProgressView.show(context, loadingDialog);
        Json j = new Json();
        j.addString(P.booking_number, "");

        Api.newApi(context, P.BaseUrl + "user_bookings").addJson(j)
                .setMethod(Api.POST)
//                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    checkError();
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);

                        JsonList current_list = json.getJsonList(P.current_list);
                        if (current_list != null && current_list.size() != 0) {
                            for (Json jsonData : current_list) {
                                String id = jsonData.getString(P.id);
                                String booking_id = jsonData.getString(P.booking_id);
                                String refund_status_msg = jsonData.getString(P.refund_status_msg);
                                String car_image = jsonData.getString(P.car_image);
                                String car_name = jsonData.getString(P.car_name);
                                String pickup_type = jsonData.getString(P.pickup_type);
                                String pickup_datetime = jsonData.getString(P.pickup_datetime);
                                String pickup_location_name = jsonData.getString(P.pickup_location_name);
//                                String pickup_address = jsonData.getString(P.pickup_address);
                                String pickup_address = jsonData.getString(P.pickup_details);
                                String pickup_landmark = jsonData.getString(P.pickup_landmark);
                                String dropoff_type = jsonData.getString(P.dropoff_type);
                                String dropoff_datetime = jsonData.getString(P.dropoff_datetime);
                                String dropoff_location_name = jsonData.getString(P.dropoff_location_name);
//                                String dropoff_address = jsonData.getString(P.dropoff_address);
                                String dropoff_address = jsonData.getString(P.dropoff_details);
                                String dropoff_landmark = jsonData.getString(P.dropoff_landmark);

                                BookingModel model = new BookingModel();
                                model.setId(id);
                                model.setBooking_id(booking_id);
                                model.setRefund_status_msg(refund_status_msg);
                                model.setCar_image(car_image);
                                model.setCar_name(car_name);
                                model.setPickup_type(pickup_type);
                                model.setPickup_datetime(pickup_datetime);
                                model.setPickup_location_name(pickup_location_name);
                                model.setPickup_address(pickup_address);
                                model.setPickup_landmark(pickup_landmark);
                                model.setDropoff_type(dropoff_type);
                                model.setDropoff_datetime(dropoff_datetime);
                                model.setDropoff_location_name(dropoff_location_name);
                                model.setDropoff_address(dropoff_address);
                                model.setDropoff_landmark(dropoff_landmark);

                                bookingModelList.add(model);
                            }

                            currentReservationAdapter.notifyDataSetChanged();
                        }

                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }
                    checkError();
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCurrentBookingDetails", session.getString(P.token));

    }


    private void checkError() {

        if (bookingModelList.isEmpty()) {
            binding.txtError.setVisibility(View.VISIBLE);
        } else {
            binding.txtError.setVisibility(View.GONE);
        }
    }

    public static CurrentBookingFragment newInstance() {
        CurrentBookingFragment fragment = new CurrentBookingFragment();
        return fragment;
    }
}
