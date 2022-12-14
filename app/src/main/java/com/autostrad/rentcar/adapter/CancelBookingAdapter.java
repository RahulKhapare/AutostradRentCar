package com.autostrad.rentcar.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityCancelBookingListBinding;
import com.autostrad.rentcar.model.BookingModel;
import com.autostrad.rentcar.util.LoadImage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CancelBookingAdapter extends RecyclerView.Adapter<CancelBookingAdapter.viewHolder> {

    private Context context;
    private List<BookingModel> bookingModelList;
    private Session session;
    private Fragment fragment;
    private int flag;

    public CancelBookingAdapter(Context context, List<BookingModel> bookingModelList, Fragment fragment, int flag) {
        this.context = context;
        this.bookingModelList = bookingModelList;
        this.fragment = fragment;
        this.flag = flag;
        session = new Session(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCancelBookingListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_cancel_booking_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        BookingModel model = bookingModelList.get(position);

        LoadImage.glideString(context, holder.binding.imgCar, model.getCar_image());
        holder.binding.txtCarName.setText(model.getCar_name());
        holder.binding.txtReservationNo.setText(model.getBooking_id());
        holder.binding.txtPickup.setText(getFormatDate(checkString(model.getPickup_datetime())));
        holder.binding.txtDropoff.setText(getFormatDate(checkString(model.getDropoff_datetime())));

        if (!TextUtils.isEmpty(checkString(model.getRefund_status_msg()))){
            holder.binding.txtMessage.setText(checkString(model.getRefund_status_msg()));
            holder.binding.lnrMessage.setVisibility(View.VISIBLE);
        }else {
            holder.binding.lnrMessage.setVisibility(View.GONE);
        }



        try {
            if (position == bookingModelList.size() - 1) {
                ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 0, 16);
                holder.binding.lnrMain.setLayoutParams(params);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return bookingModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityCancelBookingListBinding binding;

        public viewHolder(@NonNull ActivityCancelBookingListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String checkString(String value) {
        String data = "";
        if (!TextUtils.isEmpty(value) && !value.equals("null")) {
            data = value;
        }
        return data;
    }

    private String getFormatDate(String actualDate) {
        String app_date = "";
        try {
            app_date = actualDate;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = (Date) formatter.parse(app_date);
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM, dd, yyyy hh:mm:ss");
            String finalString = newFormat.format(date);
            app_date = finalString;
        } catch (Exception e) {
            app_date = actualDate;
        }
        return app_date;
    }
}
