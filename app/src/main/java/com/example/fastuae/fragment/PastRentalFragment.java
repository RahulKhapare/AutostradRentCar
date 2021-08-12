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
import android.widget.EditText;
import android.widget.TextView;

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
import com.example.fastuae.activity.MainActivity;
import com.example.fastuae.activity.ProfileViewActivity;
import com.example.fastuae.adapter.PastRentalAdapter;
import com.example.fastuae.adapter.UpcomingReservationAdapter;
import com.example.fastuae.databinding.FragmentPastRentalBinding;
import com.example.fastuae.model.BookingModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class PastRentalFragment extends Fragment implements PastRentalAdapter.onClick{

    private Context context;
    private FragmentPastRentalBinding binding;
    private List<BookingModel> bookingModelList;
    private PastRentalAdapter pastRentalAdapter;
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
        pastRentalAdapter = new PastRentalAdapter(context, bookingModelList,PastRentalFragment.this,2);
        binding.recyclerPastRental.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerPastRental.setAdapter(pastRentalAdapter);
        hitPastBookingDetails();

    }
    private void hitPastBookingDetails() {
        bookingModelList.clear();
        pastRentalAdapter.notifyDataSetChanged();
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

                        if (past_list!=null && past_list.size()!=0){
                            for (Json jsonData : past_list){

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

                            pastRentalAdapter.notifyDataSetChanged();
                        }

                    }else {
                        H.showMessage(context,json.getString(P.error));
                    }
                    checkError();
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitPastBookingDetails",session.getString(P.token));

    }


    private void checkError() {

        if (bookingModelList.isEmpty()) {
            binding.txtError.setVisibility(View.VISIBLE);
        } else {
            binding.txtError.setVisibility(View.GONE);
        }
    }

    public static PastRentalFragment newInstance() {
        PastRentalFragment fragment = new PastRentalFragment();
        return fragment;
    }

    @Override
    public void downloadInvoice(BookingModel model) {
        String path = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
        ((ProfileViewActivity)getActivity()).checkPDF(path);
    }

}
