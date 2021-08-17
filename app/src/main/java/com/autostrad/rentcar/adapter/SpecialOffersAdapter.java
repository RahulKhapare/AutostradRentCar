package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivitySpecialOffersListBinding;
import com.autostrad.rentcar.model.SpecialOffersModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.RemoveHtml;

import java.util.List;

public class SpecialOffersAdapter extends RecyclerView.Adapter<SpecialOffersAdapter.viewHolder> {

    private Context context;
    private List<SpecialOffersModel> specialOffersModelList;
    private Session session;

    public SpecialOffersAdapter(Context context, List<SpecialOffersModel> specialOffersModelList) {
        this.context = context;
        this.specialOffersModelList = specialOffersModelList;
        session = new Session(context);
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivitySpecialOffersListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_special_offers_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SpecialOffersModel model = specialOffersModelList.get(position);

        String imagePath = model.getImage();
        if (TextUtils.isEmpty(imagePath)){
            imagePath = "empty";
        }
        LoadImage.glideString(context,holder.binding.imgCarBg,imagePath);
        holder.binding.txtTitle.setText(model.getTitle_name());
        holder.binding.txtDescription.setText(RemoveHtml.html2text(model.getDescription()));
        holder.binding.txtValid.setText(model.getOffer_validity());
        holder.binding.txtCode.setText(context.getResources().getString(R.string.useCode)+": "+model.getOffer_code());

        holder.binding.txtCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                H.showMessage(context, context.getResources().getString(R.string.codeCopied )+ " " + model.getOffer_code());
                setClipboard(context,model.getOffer_code());
            }
        });

        if (session.getString(P.languageFlag).equals(Config.ARABIC)){
            holder.binding.txtValid.setGravity(Gravity.RIGHT);
        }

    }

    @Override
    public int getItemCount() {
        return specialOffersModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivitySpecialOffersListBinding binding;
        public viewHolder(@NonNull ActivitySpecialOffersListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void setClipboard(Context context, String text) {
        try {
            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
                clipboard.setPrimaryClip(clip);
            }
        }catch (Exception e){
        }
    }
}
