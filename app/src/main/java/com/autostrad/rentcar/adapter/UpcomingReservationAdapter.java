package com.autostrad.rentcar.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityCarBookingListBinding;
import com.autostrad.rentcar.fragment.UpcomingReservationFragment;
import com.autostrad.rentcar.model.BookingModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UpcomingReservationAdapter extends RecyclerView.Adapter<UpcomingReservationAdapter.viewHolder> {

    private Context context;
    private List<BookingModel> bookingModelList;
    private Session session;
    private Fragment fragment;
    private int flag;

    public interface onClick{
        void cancelBooking(BookingModel model);
    }

    public UpcomingReservationAdapter(Context context, List<BookingModel> bookingModelList, Fragment fragment, int flag) {
        this.context = context;
        this.bookingModelList = bookingModelList;
        this.fragment = fragment;
        this.flag = flag;
        session = new Session(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCarBookingListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_car_booking_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        BookingModel model = bookingModelList.get(position);

        LoadImage.glideString(context,holder.binding.imgCar,model.getCar_image());
        holder.binding.txtCarName.setText(model.getCar_name());
        holder.binding.txtRegisterNo.setText(context.getResources().getString(R.string.reservationNo)+"\n"+model.getBooking_id());

        if (model.getPickup_type().equals(Config.self_pickup)){
            holder.binding.txtFrom.setText(checkString(model.getPickup_location_name() + "\n" + getFormatDate(model.getPickup_datetime())));
        }else if (model.getPickup_type().equals(Config.deliver)){
            holder.binding.txtFrom.setText(checkString(model.getPickup_address() + "\n" + getFormatDate(model.getPickup_datetime())));
        }

        if (model.getDropoff_type().equals(Config.self_dropoff)){
            holder.binding.txtTo.setText(checkString(model.getDropoff_location_name() + "\n" + getFormatDate(model.getDropoff_datetime())));
        }else if (model.getDropoff_type().equals(Config.collect)){
            holder.binding.txtTo.setText(checkString(model.getDropoff_address() + "\n" + getFormatDate(model.getDropoff_datetime())));
        }


        if (session.getString(P.languageFlag).equals(Config.ARABIC)) {
            holder.binding.txtRegisterNo.setGravity(Gravity.RIGHT);
        }else if (session.getString(P.languageFlag).equals(Config.ENGLISH)) {
            holder.binding.txtRegisterNo.setGravity(Gravity.LEFT);
        }

        holder.binding.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
//                ((UpcomingReservationFragment)fragment).cancelBooking(model);
            }
        });

        holder.binding.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        try{
            if (position==bookingModelList.size()-1){
                ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 16);
                holder.binding.lnrMain.setLayoutParams(params);
            }
        }catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return bookingModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityCarBookingListBinding binding;
        public viewHolder(@NonNull ActivityCarBookingListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String checkString(String value){
        String data = "";
        if (!TextUtils.isEmpty(value) && !value.equals("null")){
            data = value;
        }
        return data;
    }

    private String getFormatDate(String actualDate){
        String app_date = "";
        try {
            app_date = actualDate;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = (Date)formatter.parse(app_date);
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM, dd, yyyy hh:mm:ss");
            String finalString = newFormat.format(date);
            app_date = finalString;
        }catch (Exception e){
            app_date = actualDate;
        }
        return app_date;
    }
}
