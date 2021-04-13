package com.example.fastuae.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityChooseExtraListBinding;
import com.example.fastuae.databinding.ActivityRentalListBinding;
import com.example.fastuae.model.BookingModel;
import com.example.fastuae.model.ChooseExtrasModel;
import com.example.fastuae.model.CountryCodeModel;
import com.example.fastuae.model.RentalModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.RemoveHtml;

import java.util.ArrayList;
import java.util.List;

public class RentalAdapter extends RecyclerView.Adapter<RentalAdapter.viewHolder> {

    private Context context;
    private List<RentalModel> rentalModelList;
    private Session session;


    public RentalAdapter(Context context, List<RentalModel> rentalModelList) {
        this.context = context;
        this.rentalModelList = rentalModelList;
        session = new Session(context);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityRentalListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_rental_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        RentalModel model = rentalModelList.get(position);

        holder.binding.txtTitle.setText(model.getTitle());

        holder.binding.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                context.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return rentalModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityRentalListBinding binding;

        public viewHolder(@NonNull ActivityRentalListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
