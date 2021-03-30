package com.example.fastuae.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.BookingCarAdapter;
import com.example.fastuae.databinding.FragmentUpcomingReservationBinding;
import com.example.fastuae.model.BookingModel;
import com.example.fastuae.util.CheckString;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class UpcomingReservationFragment extends Fragment implements BookingCarAdapter.onClick{

    private Context context;
    private FragmentUpcomingReservationBinding binding;
    private List<BookingModel> bookingModelList;
    private BookingCarAdapter bookingCarAdapter;
    private LoadingDialog loadingDialog;
    private Session session;

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
        session = new Session(context);
        loadingDialog = new LoadingDialog(context);
        bookingModelList = new ArrayList<>();
        bookingCarAdapter = new BookingCarAdapter(context,bookingModelList,UpcomingReservationFragment.this,1);
        binding.recyclerReservation.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerReservation.setAdapter(bookingCarAdapter);
        hitUpcomingBookingDetails();
    }

    public static UpcomingReservationFragment newInstance() {
        UpcomingReservationFragment fragment = new UpcomingReservationFragment();
        return fragment;
    }

    private void hitUpcomingBookingDetails() {
        bookingModelList.clear();
        bookingCarAdapter.notifyDataSetChanged();
        ProgressView.show(context,loadingDialog);
        Json j = new Json();
        j.addString(P.booking_number,"");

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
                        JsonList upcoming_list = json.getJsonList(P.upcoming_list);
                        JsonList current_list = json.getJsonList(P.current_list);
                        JsonList past_list = json.getJsonList(P.past_list);
                        JsonList cancel_list = json.getJsonList(P.cancel_list);

                        if (upcoming_list!=null && upcoming_list.size()!=0){
                            for (Json jsonData : upcoming_list){

                                String id = jsonData.getString(P.id);
                                String booking_id = jsonData.getString(P.booking_id);
                                String refund_status_msg = jsonData.getString(P.refund_status_msg);
                                String car_image = jsonData.getString(P.car_image);
                                String car_name = jsonData.getString(P.car_name);
                                String pickup_type = jsonData.getString(P.pickup_type);
                                String pickup_datetime = jsonData.getString(P.pickup_datetime);
                                String pickup_location_name = jsonData.getString(P.pickup_location_name);
                                String pickup_address = jsonData.getString(P.pickup_address);
                                String pickup_landmark = jsonData.getString(P.pickup_landmark);
                                String dropoff_type = jsonData.getString(P.dropoff_type);
                                String dropoff_datetime = jsonData.getString(P.dropoff_datetime);
                                String dropoff_location_name = jsonData.getString(P.dropoff_location_name);
                                String dropoff_address = jsonData.getString(P.dropoff_address);
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

                            bookingCarAdapter.notifyDataSetChanged();
                        }

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    checkError();
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUpcomingBookingDetails",session.getString(P.token));

    }

    private void checkError(){

        if (bookingModelList.isEmpty()){
            binding.txtError.setVisibility(View.VISIBLE);
        }else {
            binding.txtError.setVisibility(View.GONE);
        }
    }

    @Override
    public void cancelBooking(BookingModel model) {
        cancelDialog(model.getBooking_id());
    }

    private void cancelDialog(String booking_id) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_cancel_reason);

        EditText etxReason = dialog.findViewById(R.id.etxReason);
        Button txtSubmit = dialog.findViewById(R.id.txtSubmit);

        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                String reason = etxReason.getText().toString().trim();
                if (TextUtils.isEmpty(reason)){
                    H.showMessage(getActivity(),getResources().getString(R.string.enterCancelReason));
                }else if (reason.length()<5){
                    H.showMessage(getActivity(),getResources().getString(R.string.validCancelReason));
                }else {
                    hitCancelBooking(dialog,booking_id,etxReason.getText().toString().trim());
                }
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void hitCancelBooking(Dialog dialog,String booking_id,String cancellation_reason) {

        ProgressView.show(context,loadingDialog);
        Json j = new Json();
        j.addString(P.booking_id,booking_id);
        j.addString(P.cancellation_reason,cancellation_reason);

        Api.newApi(context, P.BaseUrl + "cancel_user_booking").addJson(j)
                .setMethod(Api.POST)
//                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        dialog.dismiss();
                        H.showMessage(getActivity(),getResources().getString(R.string.bookingCancelSuccessfully));
                        hitUpcomingBookingDetails();
                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCancelBooking",session.getString(P.token));
    }
}
