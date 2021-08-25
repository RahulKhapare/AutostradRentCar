package com.autostrad.rentcar.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.BookingSucessfullActivity;
import com.autostrad.rentcar.activity.ProfileViewActivity;
import com.autostrad.rentcar.databinding.FragmentExtendBookingBinding;
import com.autostrad.rentcar.databinding.FragmentRefundBinding;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExtendBookingFragment extends Fragment {

    private Context context;
    private FragmentExtendBookingBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_extend_booking, container, false);
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

        session = new Session(context);
        loadingDialog = new LoadingDialog(context);

        String booking_id =  Config.driverIDFORDOC;
        String dropoff_datetime =  Config.driverDROP_DATE;

        binding.txtResevationNo.setText(booking_id);
        binding.etxTime.setText(getCurrentTime());

        //DATE
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog mDatePicker = new DatePickerDialog(context,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                binding.etxDate.setText(fdate);
                emptyData();

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());

        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date dateObj = curFormater.parse(dropoff_datetime);
            Calendar calendar = Calendar.getInstance();
            calendar .setTime(dateObj);

            Calendar c = Calendar.getInstance();
            c.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MARCH), calendar.get(Calendar.DATE)+1);

            mDatePicker.getDatePicker().setMinDate(c.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.etxDate.setFocusable(false);
        binding.etxDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_date_range_24, 0);
        binding.etxDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatePicker!=null){
                    mDatePicker.show();
                }
            }
        });


        //TIME
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String AM_PM = "";
                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                datetime.set(Calendar.MINUTE, selectedMinute);

                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                    AM_PM = "AM";
                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                    AM_PM = "PM";

                binding.etxTime.setText(selectedHour + ":" + selectedMinute + " " + AM_PM);
                emptyData();


            }
        }, hour, minute, true);//Yes 24 hour time
//        mTimePicker.setTitle("Select Time");

        binding.etxTime.setFocusable(false);
        binding.etxTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_watch_bg, 0);
        binding.etxTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimePicker!=null){
                    mTimePicker.show();
                }
            }
        });

        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

                if (binding.txtAEDMessage.getVisibility()==View.GONE){
                    if (TextUtils.isEmpty(binding.etxDate.getText().toString().trim())){
                        H.showMessage(context,getResources().getString(R.string.selectNewDropOffDate));
                    }else if (TextUtils.isEmpty(binding.etxTime.getText().toString().trim())){
                        H.showMessage(context,getResources().getString(R.string.selectNewDropOffTime));
                    }else {
                        Json json = new Json();
                        json.addString("dropoff_date",binding.etxDate.getText().toString().trim());
                        json.addString("dropoff_time",binding.etxTime.getText().toString().trim());
                        json.addString("booking_id",booking_id);
                        json.addString("booking_from",Config.MOBILE);
                        hitExtendData(json);
                    }
                }else if (binding.txtAEDMessage.getVisibility()==View.VISIBLE){
                    if (TextUtils.isEmpty(binding.etxDate.getText().toString().trim())){
                        H.showMessage(context,getResources().getString(R.string.selectNewDropOffDate));
                    }else if (TextUtils.isEmpty(binding.etxTime.getText().toString().trim())){
                        H.showMessage(context,getResources().getString(R.string.selectNewDropOffTime));
                    }else {
                        Json json = new Json();
                        json.addString("dropoff_date",binding.etxDate.getText().toString().trim());
                        json.addString("dropoff_time",binding.etxTime.getText().toString().trim());
                        json.addString("booking_id",booking_id);
                        json.addString("booking_from",Config.MOBILE);
                        json.addString("success_url","");
                        json.addString("failed_url","");
                        hitConformExtendData(json);
                    }
                }else {
                    H.showMessage(context,getResources().getString(R.string.somethingWrong));
                }

            }
        });
    }

    private String getCurrentTime() {
        String delegate = "kk:mm aaa"; //24 HOUR
//        String delegate = "hh:mm aaa"; //12 HOUR
        String oldstr = (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
        String str = oldstr.replace("am", "AM").replace("pm", "PM");
        return str;
    }

    private void emptyData(){
        binding.txtAEDMessage.setText("");
        binding.txtAEDMessage.setVisibility(View.GONE);
        binding.txtSubmit.setText(getResources().getString(R.string.submit));
    }

    public void hitExtendData(Json j) {

        ProgressView.show(context, loadingDialog);

        Api.newApi(context, P.BaseUrl + "extend_booking_data").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        Json data = json.getJson(P.data);
                        String booking_update_msg = data.getString("booking_update_msg");
                        binding.txtAEDMessage.setVisibility(View.VISIBLE);
                        binding.txtAEDMessage.setText(booking_update_msg);
                        binding.txtSubmit.setText(getResources().getString(R.string.confirm));
                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);
                })
                .run("hitExtendData",session.getString(P.token));
    }

    public void hitConformExtendData(Json j) {

        ProgressView.show(context, loadingDialog);

        Api.newApi(context, P.BaseUrl + "confirm_extend_booking").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        Json data = json.getJson(P.data);
                        String payment_link = data.getString(P.payment_link);
                        Intent intent = new Intent(context, BookingSucessfullActivity.class);
                        intent.putExtra(Config.WEB_URL,payment_link);
                        intent.putExtra(Config.PAY_TYPE,Config.pay_now);
                        startActivity(intent);
                    } else {
                        H.showMessage(context, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);
                })
                .run("hitConformExtendData",session.getString(P.token));
    }

    public static ExtendBookingFragment newInstance() {
        ExtendBookingFragment fragment = new ExtendBookingFragment();
        return fragment;
    }

}
