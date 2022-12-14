package com.autostrad.rentcar.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Json;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityFiledListBinding;
import com.autostrad.rentcar.model.FieldModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentFieldAdapter extends RecyclerView.Adapter<DocumentFieldAdapter.viewHolder> {

    private Context context;
    private List<FieldModel> filedList;

    String license_number = "license_number";
    String issue_date = "issue_date";
    String expiry = "expiry";
    String id_number = "id_number";
    String passport_number = "passport_number";
    String visa_issue_date = "visa_issue_date";
    String credit_card_number = "credit_card_number";
    String name_on_card = "name_on_card";
    String entry_stamp = "entry_stamp";

    Json jsonItem ;
    Json saveJsonData ;

    int value;

    public DocumentFieldAdapter(Context context, List<FieldModel> filedList,Json json,Json saveJson,int value) {
        this.context = context;
        this.filedList = filedList;
        this.jsonItem = json;
        this.saveJsonData = saveJson;
        this.value = value;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityFiledListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_filed_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        FieldModel model = filedList.get(position);

        String hint = model.getFiled().replace("_"," ");
//        String upperString = hint.substring(0, 1).toUpperCase() + hint.substring(1).toLowerCase();

        holder.binding.editText.setHint(capitalize(hint));

        if (model.getFiled().equals(issue_date) || model.getFiled().equals(expiry) || model.getFiled().equals(visa_issue_date)){

            Calendar newCalendar = Calendar.getInstance();
            DatePickerDialog mDatePicker = new DatePickerDialog(context,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                    final Date startDate = newDate.getTime();
                    String fdate = sd.format(startDate);

                    holder.binding.editText.setText(fdate);

                    checkJson(model,fdate,jsonItem);

                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            if (model.getFiled().equals(expiry)){
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            }

            holder.binding.editText.setFocusable(false);
            holder.binding.editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_date_range_24, 0);
            holder.binding.editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDatePicker!=null){
                        mDatePicker.show();
                    }
                }
            });

        }else {
            holder.binding.editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if(s.length() != 0){
                        checkJson(model,s.toString(),jsonItem);
                    }else if (s.length()==0){
                        checkRemoveJson(model,jsonItem);
                    }

                }
            });
        }

        try {
            if (saveJsonData!=null && saveJsonData.length()!=0){
                if (saveJsonData.has(model.getFiled())){
                    String inputValue = saveJsonData.getString(model.getFiled());
                    if (!TextUtils.isEmpty(inputValue) && !inputValue.equals("null")){
                        holder.binding.editText.setText(inputValue);
                        checkJson(model,inputValue,jsonItem);
                    }
//                    Log.e("TAG", "onBindViewHolderJSJJS: "+ saveJsonData.getString(model.getFiled()) );
                }
            }
        }catch (Exception e){
        }

        if (value==2){
            setMargins(holder.binding.lnrView,5,10,5,10);
        }
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
    @Override
    public int getItemCount() {
        return filedList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityFiledListBinding binding;

        public viewHolder(@NonNull ActivityFiledListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void checkJson(FieldModel model, String value,Json jsonItem){
        try {
            if (jsonItem.has(model.getFiled())){
                jsonItem.remove(model.getFiled());
                jsonItem.addString(model.getFiled(),value);
            }else {
                jsonItem.addString(model.getFiled(),value);
            }
        }catch (Exception e){

        }

        model.setJson(jsonItem);

        Log.e("TAG", "checkJson: "+ model.getFiled() + " " + value );
        Log.e("TAG", "checkJsonDAA: "+ model.getJson().toString() );
    }

    private void checkRemoveJson(FieldModel model,Json jsonItem){
        try {
            if (jsonItem.has(model.getFiled())){
                jsonItem.remove(model.getFiled());
            }
        }catch (Exception e){

        }

        model.setJson(jsonItem);
        Log.e("TAG", "checkJsonDAA: "+ model.getJson().toString() );
    }

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}
