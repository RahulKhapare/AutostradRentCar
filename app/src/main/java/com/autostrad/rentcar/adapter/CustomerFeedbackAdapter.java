package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.CustomerFeedbackActivity;
import com.autostrad.rentcar.databinding.ActivityCustomerFeedbackListBinding;
import com.autostrad.rentcar.model.CustomerFeedbackModel;

import java.util.List;

public class CustomerFeedbackAdapter extends RecyclerView.Adapter<CustomerFeedbackAdapter.viewHolder> {

    private Context context;
    private List<CustomerFeedbackModel> customerFeedbackModelList;
    private int lastCheckPosition;
    private int from;
    private boolean call;

    public interface onCLick{
        void emirateSelection(CustomerFeedbackModel model);
        void productSelection(CustomerFeedbackModel model);
        void professionalismSelection(CustomerFeedbackModel model);
        void friednlinessSelection(CustomerFeedbackModel model);
        void timelySelection(CustomerFeedbackModel model);
        void releabilitySelection(CustomerFeedbackModel model);
        void cleanlinessSelection(CustomerFeedbackModel model);
    }

    public CustomerFeedbackAdapter(Context context, List<CustomerFeedbackModel> customerFeedbackModelList,int from) {
        this.context = context;
        this.customerFeedbackModelList = customerFeedbackModelList;
        this.from = from;
        call = true;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCustomerFeedbackListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_customer_feedback_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        CustomerFeedbackModel model = customerFeedbackModelList.get(position);

        holder.binding.radioButton.setChecked(position == lastCheckPosition);
        holder.binding.radioButton.setText(model.getValue());
        holder.binding.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastCheckPosition = holder.getAdapterPosition();
                notifyItemRangeChanged(0,customerFeedbackModelList.size());
                callData(model);
            }
        });

        if (lastCheckPosition==position){
            blueTin(holder.binding.radioButton);
        }else {
            blackTin(holder.binding.radioButton);
        }

        if (call){
            call = false;
            callData(model);
        }
    }

    @Override
    public int getItemCount() {
        return customerFeedbackModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityCustomerFeedbackListBinding binding;

        public viewHolder(@NonNull ActivityCustomerFeedbackListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void callData(CustomerFeedbackModel model){
        if (from==1){
            ((CustomerFeedbackActivity)context).emirateSelection(model);
        }if (from==2){
            ((CustomerFeedbackActivity)context).productSelection(model);
        }if (from==3){
            ((CustomerFeedbackActivity)context).professionalismSelection(model);
        }if (from==4){
            ((CustomerFeedbackActivity)context).friednlinessSelection(model);
        }if (from==5){
            ((CustomerFeedbackActivity)context).timelySelection(model);
        }if (from==6){
            ((CustomerFeedbackActivity)context).releabilitySelection(model);
        }if (from==7){
            ((CustomerFeedbackActivity)context).cleanlinessSelection(model);
        }
    }
    private void blackTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{context.getResources().getColor(R.color.grayDark)}
                    },
                    new int[]{context.getResources().getColor(R.color.grayDark)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }

    private void blueTin(RadioButton radioButton) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList myColorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{context.getResources().getColor(R.color.lightBlue)}
                    },
                    new int[]{context.getResources().getColor(R.color.lightBlue)}
            );

            radioButton.setButtonTintList(myColorStateList);
        }
    }
}
