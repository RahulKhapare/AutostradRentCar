package com.example.fastuae.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fastuae.R;
import com.example.fastuae.model.CarFilterModel;

import java.util.List;

public class FilterSpinnerAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflter;
    private List<CarFilterModel> itemListModels;

    public FilterSpinnerAdapter(Context context, List<CarFilterModel> itemListModelList) {
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
        view = inflter.inflate(R.layout.activity_filter_bg, null);
        TextView txtFilter = view.findViewById(R.id.txtFilter);
        CarFilterModel model = itemListModels.get(i);
        txtFilter.setText(model.getFilterName());
        return view;
    }

}