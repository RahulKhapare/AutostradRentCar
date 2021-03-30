package com.example.fastuae.adapter;

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
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCarBookingListBinding;
import com.example.fastuae.fragment.PastRentalFragment;
import com.example.fastuae.fragment.UpcomingReservationFragment;
import com.example.fastuae.model.BookingModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookingCarAdapter extends RecyclerView.Adapter<BookingCarAdapter.viewHolder> {

    private Context context;
    private List<BookingModel> bookingModelList;
    private Session session;
    private Fragment fragment;
    private int flag;

    public interface onClick{
        void cancelBooking(BookingModel model);
    }

    public BookingCarAdapter(Context context, List<BookingModel> bookingModelList,Fragment fragment,int flag) {
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

        if (!TextUtils.isEmpty(model.getCar_image())){
            Picasso.get().load(model.getCar_image()).placeholder(R.drawable.ic_no_car).error(R.drawable.ic_no_car).into(holder.binding.imgCar);
        }else {
            Picasso.get().load(R.drawable.ic_no_car).into(holder.binding.imgCar);
        }
        holder.binding.txtCarName.setText(model.getCar_name());
        holder.binding.txtRegisterNo.setText(context.getResources().getString(R.string.reservationNo)+"n"+model.getBooking_id());
        holder.binding.txtFrom.setText(model.getPickup_address());
        holder.binding.txtTo.setText(model.getDropoff_address());

        if (session.getString(P.languageFlag).equals(Config.ARABIC)) {
            holder.binding.txtRegisterNo.setGravity(Gravity.RIGHT);
        }else if (session.getString(P.languageFlag).equals(Config.ENGLISH)) {
            holder.binding.txtRegisterNo.setGravity(Gravity.LEFT);
        }

        holder.binding.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (flag==1){
                    ((UpcomingReservationFragment)fragment).cancelBooking(model);
                }else if (flag==2){
                    ((PastRentalFragment)fragment).cancelBooking(model);
                }
            }
        });

        holder.binding.txtViewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

            }
        });

        holder.binding.txtHelpSupport.setOnClickListener(new View.OnClickListener() {
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
}
