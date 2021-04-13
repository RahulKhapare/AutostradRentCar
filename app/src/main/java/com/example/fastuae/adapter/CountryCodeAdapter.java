package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.fastuae.R;
import com.example.fastuae.model.CountryCodeModel;

import java.util.List;

public class CountryCodeAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflter;
    private List<CountryCodeModel> itemListModels;
    int flag = 0;

    public CountryCodeAdapter(Context context, List<CountryCodeModel> itemListModelList,int flag) {
        this.context = context;
        this.itemListModels = itemListModelList;
        inflter = (LayoutInflater.from(context));
        this.flag = flag;
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
        if (flag==1){
            view = inflter.inflate(R.layout.activity_countr_code_bg, null);
        }else if (flag==3){
            view = inflter.inflate(R.layout.activity_countr_code_white_new_bg, null);
        }else {
            view = inflter.inflate(R.layout.activity_countr_code_white_bg, null);
        }

        TextView txtName = view.findViewById(R.id.txtCode);
        CountryCodeModel model = itemListModels.get(i);

        if (flag==3){
            txtName.setText(model.getPhone_code());
        }else {
            txtName.setText("+"+model.getPhone_code());
        }

        return view;
    }

}