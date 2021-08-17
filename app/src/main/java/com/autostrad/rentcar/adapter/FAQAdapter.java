package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityFaqListBinding;
import com.autostrad.rentcar.model.FAQModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.RemoveHtml;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.viewHolder> {

    private Context context;
    private List<FAQModel> faqModelList;
    int lastCheckPosition = -1;

    public FAQAdapter(Context context, List<FAQModel> faqModelList) {
        this.context = context;
        this.faqModelList = faqModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityFaqListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_faq_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        FAQModel model = faqModelList.get(position);

        holder.binding.txtTitle.setText(model.getQuestion());
        holder.binding.txtDescription.setText(RemoveHtml.html2text(model.getAnswer()).trim());

        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                lastCheckPosition = holder.getAdapterPosition();
                notifyItemRangeChanged(0,faqModelList.size());
            }
        });

        if (lastCheckPosition==position){
            if (model.isClickFlag()){
                model.setClickFlag(false);
                holder.binding.imgTitle.setImageResource(R.drawable.ic_plus);
                holder.binding.txtDescription.setVisibility(View.GONE);
            }else {
                model.setClickFlag(true);
                holder.binding.imgTitle.setImageResource(R.drawable.ic_minus);
                holder.binding.txtDescription.setVisibility(View.VISIBLE);
            }
        }else {
            model.setClickFlag(false);
            holder.binding.imgTitle.setImageResource(R.drawable.ic_plus);
            holder.binding.txtDescription.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return faqModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityFaqListBinding binding;
        public viewHolder(@NonNull ActivityFaqListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
