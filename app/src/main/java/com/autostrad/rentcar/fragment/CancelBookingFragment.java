package com.autostrad.rentcar.fragment;

import android.content.Context;
import android.os.Bundle;
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
import com.autostrad.rentcar.adapter.CancelBookingAdapter;
import com.autostrad.rentcar.adapter.PastRentalAdapter;
import com.autostrad.rentcar.databinding.FragmentPastRentalBinding;
import com.autostrad.rentcar.model.BookingModel;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class CancelBookingFragment extends Fragment implements PastRentalAdapter.onClick {

    private Context context;
    private FragmentPastRentalBinding binding;
    private List<BookingModel> bookingModelList;
    private CancelBookingAdapter cancelBookingAdapter;
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
        cancelBookingAdapter = new CancelBookingAdapter(context, bookingModelList, CancelBookingFragment.this, 2);
        binding.recyclerPastRental.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerPastRental.setAdapter(cancelBookingAdapter);
        hitCancelBookingDetails();
//        setData();

    }

    private void setData(){
        BookingModel model = new BookingModel();
        model.setId("3");
        model.setBooking_id("16197831949");
        model.setRefund_status_msg("We have received the refund request and will get back to you in 4-5 working days");
        model.setCar_image("https://www.pivotmkg.com/fastcarmodule/uploads/car/thumbnail/2c193415961deb706c649056d7600363.png");
        model.setCar_name("Mitsubishi Attrage");
        model.setPickup_type("self_pickup");
        model.setPickup_datetime("2021-04-30 17:14:00");
        model.setPickup_location_name("Al Quoz - Service/sales branch");
        model.setPickup_address("Behind Ajmal Perfume factory, near Oasis Center, Sheikh Zayed Road, Dubai, United Arab Emirates");
        model.setPickup_landmark("Al Quoz - Service/sales branch");
        model.setDropoff_type("self_dropoff");
        model.setDropoff_datetime("2021-05-01 17:14:00");
        model.setDropoff_location_name("Ras Al Khaimah");
        model.setDropoff_address("Al Jazah Street, Opp RAK Chambers of Commerce - Ras Al-Khaimah, United Arab Emirates");
        model.setDropoff_landmark("Ras Al Khaimah");

        bookingModelList.add(model);
        cancelBookingAdapter.notifyDataSetChanged();
    }

    private void hitCancelBookingDetails() {
        bookingModelList.clear();
        cancelBookingAdapter.notifyDataSetChanged();
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
                        JsonList upcoming_list = json.getJsonList(P.upcoming_list);
                        JsonList current_list = json.getJsonList(P.current_list);
                        JsonList past_list = json.getJsonList(P.past_list);
                        JsonList cancel_list = json.getJsonList(P.cancel_list);

//                        Log.e("TAG", "hitCancelBookingDetailsAASAS: "+ cancel_list.toString() );

                        if (cancel_list != null && cancel_list.size() != 0) {
                            for (Json jsonData : cancel_list) {

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

                            cancelBookingAdapter.notifyDataSetChanged();
                        }

                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }
                    checkError();
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCancelBookingDetails", session.getString(P.token));

    }


    private void checkError() {

        if (bookingModelList.isEmpty()) {
            binding.txtError.setVisibility(View.VISIBLE);
        } else {
            binding.txtError.setVisibility(View.GONE);
        }
    }

    public static CancelBookingFragment newInstance() {
        CancelBookingFragment fragment = new CancelBookingFragment();
        return fragment;
    }

    @Override
    public void downloadInvoice(BookingModel model) {
        String path = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";
        ((MainActivity) getActivity()).checkPDF(path);
    }

}
