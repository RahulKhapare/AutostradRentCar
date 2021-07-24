package com.example.fastuae.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityRentalListBinding;
import com.example.fastuae.model.RentalModel;
import com.example.fastuae.util.Click;

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
                onDetailClick(model.getTitle(),model.getDescription());
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

    private void onDetailClick(String title,String desc) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_rental_details_dialog);

        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        WebView webView = dialog.findViewById(R.id.webView);

        txtTitle.setText(title);
        webView.loadDataWithBaseURL(null, desc, "text/html", "utf-8", null);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

}
