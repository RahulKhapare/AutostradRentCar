package com.example.fastuae.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCarBookingListBinding;
import com.example.fastuae.model.BookingModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;

import java.util.List;

public class BookingCarAdapter extends RecyclerView.Adapter<BookingCarAdapter.viewHolder> {

    private Context context;
    private List<BookingModel> bookingModelList;
    private Session session;

    public BookingCarAdapter(Context context, List<BookingModel> bookingModelList) {
        this.context = context;
        this.bookingModelList = bookingModelList;
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

        holder.binding.txtCarName.setText(model.getCarName());
        holder.binding.txtRegisterNo.setText(model.getReservationNumber());
        holder.binding.txtFrom.setText(model.getFrom());
        holder.binding.txtTo.setText(model.getTo());

        if (session.getString(P.languageFlag).equals(Config.ARABIC)) {
            holder.binding.txtRegisterNo.setGravity(Gravity.RIGHT);
        }else if (session.getString(P.languageFlag).equals(Config.ENGLISH)) {
            holder.binding.txtRegisterNo.setGravity(Gravity.LEFT);
        }

        holder.binding.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
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
