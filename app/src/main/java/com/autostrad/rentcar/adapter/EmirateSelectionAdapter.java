package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.model.EmirateModel;

import java.util.List;

public class EmirateSelectionAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflter;
    private List<EmirateModel> itemListModels;

    public EmirateSelectionAdapter(Context context, List<EmirateModel> itemListModelList) {
        this.context = context;
        this.itemListModels = itemListModelList;
        inflter = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return itemListModels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_address_white_bg, null);
        TextView txtName = view.findViewById(R.id.txtName);
        EmirateModel model = itemListModels.get(i);
        txtName.setText(model.getEmirate_name());
        if (i==0){
            txtName.setTextColor(context.getResources().getColor(R.color.iconicGray));
        }else {
            txtName.setTextColor(context.getResources().getColor(R.color.textDark));
        }
        return view;
    }

}