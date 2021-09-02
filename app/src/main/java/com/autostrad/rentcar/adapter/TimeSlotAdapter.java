package com.autostrad.rentcar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.EditPickupDropofftActivity;
import com.autostrad.rentcar.databinding.ActivityTimeSlotListBinding;
import com.autostrad.rentcar.fragment.HomeFragment;
import com.autostrad.rentcar.model.TimeSlotModel;
import com.autostrad.rentcar.util.Click;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.viewHolder> {

    private Context context;
    private List<TimeSlotModel> timeSlotModelList;
    private HomeFragment fragment;
    private int value;
    private int positionValue;
    private Dialog dialog;

    public interface onClick{
        void onTimeClick(TimeSlotModel model,int position);
    }

    public TimeSlotAdapter(Context context, List<TimeSlotModel> timeSlotModelList, HomeFragment fragment, int value, int positionValue, Dialog dialog) {
        this.context = context;
        this.timeSlotModelList = timeSlotModelList;
        this.fragment = fragment;
        this.value = value;
        this.positionValue = positionValue;
        this.dialog = dialog;
    }

    public TimeSlotAdapter(Context context, List<TimeSlotModel> timeSlotModelList,int value, int positionValue, Dialog dialog) {
        this.context = context;
        this.timeSlotModelList = timeSlotModelList;
        this.value = value;
        this.positionValue = positionValue;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityTimeSlotListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_time_slot_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        TimeSlotModel model = timeSlotModelList.get(position);
        holder.binding.txtTime.setText(checkString(model.getValue(),holder.binding.txtTime));
        if (positionValue == position){
            holder.binding.txtTime.setPaintFlags(holder.binding.txtTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        holder.binding.txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
                if (value==1){
                    ((HomeFragment)fragment).onTimeClick(model,position);
                }else if (value==2){
                    ((EditPickupDropofftActivity)context).onTimeClick(model,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return timeSlotModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityTimeSlotListBinding binding;

        public viewHolder(@NonNull ActivityTimeSlotListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String checkString(String string,TextView textView){
        String value = "";
        if (string==null || string.equals("") || string.equals("null")){
            textView.setVisibility(View.GONE);
        }else {
            value = string;
        }
        return value;
    }

}
