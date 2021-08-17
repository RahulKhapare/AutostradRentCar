package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityInvoiceListBinding;
import com.autostrad.rentcar.fragment.InvoiceFragment;
import com.autostrad.rentcar.fragment.SalikChargesFragment;
import com.autostrad.rentcar.fragment.TrafficLinesFragment;
import com.autostrad.rentcar.model.InvoiceModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.P;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.viewHolder> {

    private Context context;
    private List<InvoiceModel> invoiceModelList;
    private InvoiceFragment invoiceFragment;
    private SalikChargesFragment salikChargesFragment;
    private TrafficLinesFragment trafficLinesFragment;
    private int fragmentValue = 0;
    private Session session;

    public interface onClick{
        void onDownload(String path);
    }

    public InvoiceAdapter(Context context, List<InvoiceModel> invoiceModelList, InvoiceFragment invoiceFragment,int value) {
        this.context = context;
        this.invoiceModelList = invoiceModelList;
        this.invoiceFragment = invoiceFragment;
        fragmentValue = value;
        session = new Session(context);
    }

    public InvoiceAdapter(Context context, List<InvoiceModel> invoiceModelList, SalikChargesFragment salikChargesFragment,int value) {
        this.context = context;
        this.invoiceModelList = invoiceModelList;
        this.salikChargesFragment = salikChargesFragment;
        fragmentValue = value;
        session = new Session(context);
    }

    public InvoiceAdapter(Context context, List<InvoiceModel> invoiceModelList, TrafficLinesFragment trafficLinesFragment,int value) {
        this.context = context;
        this.invoiceModelList = invoiceModelList;
        this.trafficLinesFragment = trafficLinesFragment;
        fragmentValue = value;
        session = new Session(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityInvoiceListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_invoice_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        InvoiceModel model = invoiceModelList.get(position);

        holder.binding.txtDate.setText(model.getDate());
        holder.binding.txtType.setText(model.getType());
        holder.binding.txtNo.setText(model.getNo());
        holder.binding.txtCreditAmt.setText(model.getCreditAmount());
        holder.binding.txtCreditAmt.setText(model.getCreditAmount());
        holder.binding.txtDebitAmt.setText(model.getDebitAmount());
        holder.binding.txtPaymentType.setText(model.getPaymentType());

        holder.binding.txtDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (fragmentValue == 1){
                    ((InvoiceFragment)invoiceFragment).onDownload(model.getPath());
                }else if (fragmentValue == 2){
                    ((SalikChargesFragment)salikChargesFragment).onDownload(model.getPath());
                }else if (fragmentValue == 3){
                    ((TrafficLinesFragment)trafficLinesFragment).onDownload(model.getPath());
                }
            }
        });

        if (position==invoiceModelList.size()-1){
            holder.binding.lnrLine.setVisibility(View.GONE);
        }

        if (session.getString(P.languageFlag).equals(Config.ARABIC)){
            holder.binding.txtPaymentType.setGravity(Gravity.RIGHT);
            holder.binding.txtType.setGravity(Gravity.RIGHT);
        }else if (session.getString(P.languageFlag).equals(Config.ENGLISH)){
            holder.binding.txtPaymentType.setGravity(Gravity.LEFT);
            holder.binding.txtType.setGravity(Gravity.LEFT);
        }

    }

    @Override
    public int getItemCount() {
        return invoiceModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityInvoiceListBinding binding;
        public viewHolder(@NonNull ActivityInvoiceListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
