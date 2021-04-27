package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.activity.CarBookingDetailsActivity;
import com.example.fastuae.databinding.ActivityCarBookingListBinding;
import com.example.fastuae.databinding.ActivityPaymentCardListBinding;
import com.example.fastuae.model.PaymentCardModel;
import com.example.fastuae.util.Click;

import java.util.List;

public class PaymentCardAdapter extends RecyclerView.Adapter<PaymentCardAdapter.viewHolder> {

    private Context context;
    private List<PaymentCardModel> paymentCardModelList;
    private Session session;
    private int valueFlag;

    public PaymentCardAdapter(Context context, List<PaymentCardModel> paymentCardModelList,int value) {
        this.context = context;
        this.paymentCardModelList = paymentCardModelList;
        session = new Session(context);
        valueFlag = value;
    }

    public interface onClick{
        void onEditPayment(PaymentCardModel model);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityPaymentCardListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_payment_card_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PaymentCardModel model = paymentCardModelList.get(position);

        String hideNumber = "";
        String number = model.getCard_number();
        String firstFour = number.substring(0, Math.min(number.length(), 4));
        String lastFour = number.substring(number.length() - 4);

        int count = number.length();
        for (int i = 0; i < count; i++) {

            if (i==4 || i==5 || i==6 || i==7 || i==8 || i==9 || i==10 || i==11 ){
                hideNumber = hideNumber + "x";
            }
        }

        String outputNo = firstFour + "-" + hideNumber + "-" + lastFour;
        holder.binding.txtCardNumber.setText(outputNo);

        if (valueFlag==1){
            holder.binding.imgEdit.setVisibility(View.GONE);
        }else if (valueFlag==2){
            holder.binding.imgEdit.setVisibility(View.VISIBLE);
        }

        holder.binding.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((CarBookingDetailsActivity)context).onEditPayment(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return paymentCardModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityPaymentCardListBinding binding;
        public viewHolder(@NonNull ActivityPaymentCardListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
