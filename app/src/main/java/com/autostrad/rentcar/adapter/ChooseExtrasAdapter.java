package com.autostrad.rentcar.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.AddOnsActivity;
import com.autostrad.rentcar.activity.SelectCarActivity;
import com.autostrad.rentcar.databinding.ActivityChooseExtraListBinding;
import com.autostrad.rentcar.model.AEDModel;
import com.autostrad.rentcar.model.BookingModel;
import com.autostrad.rentcar.model.CarModel;
import com.autostrad.rentcar.model.ChooseExtrasModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.RemoveHtml;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChooseExtrasAdapter extends RecyclerView.Adapter<ChooseExtrasAdapter.viewHolder> {

    private Context context;
    private List<ChooseExtrasModel> chooseExtrasModelList;
    private Session session;
    private LoadingDialog loadingDialog;
    private CarModel carModel;

    public interface onClick {
        void cancelBooking(String quantity,BookingModel model);
    }

    public ChooseExtrasAdapter(Context context, List<ChooseExtrasModel> chooseExtrasModelList) {
        this.context = context;
        this.chooseExtrasModelList = chooseExtrasModelList;
        session = new Session(context);
        loadingDialog = new LoadingDialog(context);
        carModel = Config.carModel;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityChooseExtraListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_choose_extra_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        ChooseExtrasModel model = chooseExtrasModelList.get(position);
        int target = 0;

        if (!TextUtils.isEmpty(model.getMax_quantity()) && !model.getMax_quantity().equals("null")) {
            target = Integer.parseInt(model.getMax_quantity());
        }

        int finalTarget = target;
        holder.binding.txtName.setText(model.getTitle());
        holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
        holder.binding.txtPrice.setText(context.getResources().getString(R.string.aed) + " " + model.getPrice());

        if (model.getDescription().length()>100){
            holder.binding.txtDesc.setMaxLines(2);
            holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
            holder.binding.txtMore.setVisibility(View.VISIBLE);
            holder.binding.txtLess.setVisibility(View.GONE);
        }else {
            holder.binding.txtMore.setVisibility(View.GONE);
            holder.binding.txtLess.setVisibility(View.GONE);
            holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
        }

        if (model.getTitle().contains("Baby Seater")){
            LoadImage.glide(context,holder.binding.imgExtra,context.getResources().getDrawable(R.drawable.ic_baby_seat_new));
        }else if (model.getTitle().contains("Navigation System") || model.getTitle().contains("GPS")){
            LoadImage.glide(context,holder.binding.imgExtra,context.getResources().getDrawable(R.drawable.ic_gps_new));
        }else if (model.getTitle().contains("Additional Driver")){
            LoadImage.glide(context,holder.binding.imgExtra,context.getResources().getDrawable(R.drawable.ic_drive_new));
        }else if (model.getTitle().contains("CDW")){
            LoadImage.glide(context,holder.binding.imgExtra,context.getResources().getDrawable(R.drawable.ic_cdw_new));
        }else if (model.getTitle().contains("Super Collision Damage Waiver")){
            LoadImage.glide(context,holder.binding.imgExtra,context.getResources().getDrawable(R.drawable.ic_scdw_new));
        }else if (model.getTitle().contains("PAI")){
            LoadImage.glide(context,holder.binding.imgExtra,context.getResources().getDrawable(R.drawable.ic_pai_new));
        }else {
            holder.binding.imgExtra.setVisibility(View.GONE);
        }

        holder.binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (finalTarget >1){
                        holder.binding.lnrQuantity.setVisibility(View.VISIBLE);
                    }else {
                        holder.binding.lnrQuantity.setVisibility(View.GONE);
                    }
                    AddOnsActivity.addOnsList.add(model);
                }else {
                    holder.binding.lnrQuantity.setVisibility(View.GONE);
                    for (int i=0; i<AddOnsActivity.addOnsList.size(); i++){
                        String title = AddOnsActivity.addOnsList.get(i).getTitle();
                        if (title.equals(model.getTitle())){
                            AddOnsActivity.addOnsList.remove(i);
                            holder.binding.txtCount.setText("1");
                        }
                    }
                }

            }
        });

        model.setQuantity("1");
        holder.binding.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                int countValue = H.getInt(holder.binding.txtCount.getText().toString());
                if(countValue< finalTarget){
                    int itemCount = countValue + 1;
                    holder.binding.txtCount.setText(itemCount + "");
                    model.setQuantity(itemCount + "");
                }else {
                    H.showMessage(context,context.getResources().getString(R.string.noMoreQty));
                }
            }
        });

        holder.binding.imhMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                int countValue = H.getInt(holder.binding.txtCount.getText().toString());
                if (countValue > 1) {
                    int itemCount = countValue - 1;
                    holder.binding.txtCount.setText(itemCount + "");
                    model.setQuantity(itemCount + "");
                }
            }
        });

        holder.binding.txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                holder.binding.txtMore.setVisibility(View.GONE);
                holder.binding.txtLess.setVisibility(View.VISIBLE);
                holder.binding.txtDesc.setMaxLines(5000);
                holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
            }
        });

        holder.binding.txtLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                holder.binding.txtMore.setVisibility(View.VISIBLE);
                holder.binding.txtLess.setVisibility(View.GONE);
                holder.binding.txtDesc.setMaxLines(2);
                holder.binding.txtDesc.setText(RemoveHtml.html2text(model.getDescription()));
            }
        });

        holder.binding.txtViewCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                hitAEDDetailsData(model.getKey_value());
            }
        });

    }

    @Override
    public int getItemCount() {
        return chooseExtrasModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityChooseExtraListBinding binding;

        public viewHolder(@NonNull ActivityChooseExtraListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void hitAEDDetailsData(String detail_type) {

        ProgressView.show(context, loadingDialog);
        Json j = new Json();

        String extraParams =
                "emirate_id=" + SelectCarActivity.pickUpEmirateID+
                        "&pickup_emirate_id=" + SelectCarActivity.pickUpEmirateID+
                        "&car_id=" + carModel.getId()+
                        "&pickup_date=" + SelectCarActivity.pickUpDate +
                        "&dropoff_date=" + SelectCarActivity.dropUpDate +
                        "&booking_type=" + SelectCarActivity.bookingTYpe +
                        "&month_time=" + SelectCarActivity.monthDuration +
                        "&detail_type=" + detail_type +
                        "&coupon_code=" + "" ;

        Api.newApi(context, P.BaseUrl + "car_rate_details?" + extraParams).addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(context, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {

                        json = json.getJson(P.data);
                        AEDDetailsDialog(json,detail_type);

                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitAEDDetailsData");
    }

    private void AEDDetailsDialog(Json json,String detail_type) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_aed_details);

        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        LinearLayout lnrTotal = dialog.findViewById(R.id.lnrTotal);
        LinearLayout lnrPayLater = dialog.findViewById(R.id.lnrPayLater);
        LinearLayout lnrDiscount = dialog.findViewById(R.id.lnrDiscount);
        LinearLayout lnrPayNow = dialog.findViewById(R.id.lnrPayNow);

        TextView txtTotalAmount = dialog.findViewById(R.id.txtTotalAmount);
        TextView txtPayLaterAmount = dialog.findViewById(R.id.txtPayLaterAmount);
        TextView txtPayDiscountAmount = dialog.findViewById(R.id.txtPayDiscountAmount);
        TextView txtPayNowAmount = dialog.findViewById(R.id.txtPayNowAmount);

        List<AEDModel> aedModelList = new ArrayList<>();

        for (Json jsonData : json.getJsonList("breakup")){
            AEDModel model = new AEDModel();
            model.setDate(jsonData.getString("date"));
            model.setPrice(jsonData.getString("price"));
            model.setSurge(jsonData.getString("surge"));
            model.setDiscount(jsonData.getString("discount"));
//            model.setType(jsonData.getString(detail_type));
            aedModelList.add(model);
        }

        RecyclerView recyclerAED = dialog.findViewById(R.id.recyclerAED);
        recyclerAED.setLayoutManager(new LinearLayoutManager(context));
        recyclerAED.setNestedScrollingEnabled(false);
        AEDAdapter aedAdapter = new AEDAdapter(context,aedModelList);
        recyclerAED.setAdapter(aedAdapter);

        String aed  = context.getResources().getString(R.string.aed);
        txtTotalAmount.setText(aed + " " + checkView(json.getString("breakup_total"),lnrTotal));
        txtPayLaterAmount.setText(aed + " " + checkView(json.getString("pay_later_price"),lnrPayLater));
        txtPayDiscountAmount.setText(aed + " " + checkView(json.getString("pay_now_discount"),lnrDiscount));
        txtPayNowAmount.setText(aed + " " + checkView(json.getString("pay_now_price"),lnrPayNow));

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private String checkView(String string, LinearLayout linearLayout){
        String value = "";
        if (TextUtils.isEmpty(string) || string.equals("null") || string.equals("0")){
            linearLayout.setVisibility(View.GONE);
        }else {
            try {
                double doubleValue = Double.parseDouble(string);
                value = new DecimalFormat("##.##").format(doubleValue);
            }catch (Exception e){
                value = string;
            }
        }
        return value;
    }

}
