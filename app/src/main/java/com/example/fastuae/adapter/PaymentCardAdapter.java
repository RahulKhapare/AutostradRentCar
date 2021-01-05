package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCarBookingListBinding;
import com.example.fastuae.databinding.ActivityPaymentCardListBinding;
import com.example.fastuae.model.PaymentCardModel;

import java.util.List;

public class PaymentCardAdapter extends RecyclerView.Adapter<PaymentCardAdapter.viewHolder> {

    private Context context;
    private List<PaymentCardModel> paymentCardModelList;
    private Session session;

    public PaymentCardAdapter(Context context, List<PaymentCardModel> paymentCardModelList) {
        this.context = context;
        this.paymentCardModelList = paymentCardModelList;
        session = new Session(context);
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
        String number = model.getCardNumber();
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
