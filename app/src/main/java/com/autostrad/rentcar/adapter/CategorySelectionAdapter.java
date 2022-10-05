package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.FAQActivity;
import com.autostrad.rentcar.databinding.ActivityCategorySetectionBgBinding;
import com.autostrad.rentcar.fragment.FleetFragment;
import com.autostrad.rentcar.fragment.ProfileFragment;
import com.autostrad.rentcar.model.CategoryModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;

import java.util.List;

public class CategorySelectionAdapter extends RecyclerView.Adapter<CategorySelectionAdapter.ViewHolder> {

    Context context;
    List<CategoryModel> categoryModelList;
    int lastCheckPosition = 0;
    ProfileFragment profileFragment;
    FleetFragment fleetFragment;
    String tag = "";
    boolean checkFlag;

    public interface onClick{
        void onCategoryClick(String category);
    }


    public CategorySelectionAdapter(Context context, List<CategoryModel> categoryModelList, FleetFragment fleetFragment,String tag,boolean checkFlag) {
        this.context = context;
        this.categoryModelList = categoryModelList;
        this.fleetFragment = fleetFragment;
        this.tag = tag;
        this.checkFlag = checkFlag;
    }

    public CategorySelectionAdapter(Context context, List<CategoryModel> categoryModelList,String tag) {
        this.context = context;
        this.categoryModelList = categoryModelList;
        this.tag = tag;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityCategorySetectionBgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_category_setection_bg, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CategoryModel model = categoryModelList.get(position);

        holder.binding.txtCategory.setText(model.getCategoryName());

        holder.binding.txtCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                lastCheckPosition = holder.getAdapterPosition();
                notifyItemRangeChanged(0,categoryModelList.size());
                if (tag.equals(Config.FAQ_TAG)){
                    ((FAQActivity)context).onCategoryClick(model.getCategory_name_slug());
                }else if (tag.equals(Config.FLEET_TAG)){
                    ((FleetFragment)fleetFragment).onCategoryClick(model.getCategoryName());
                }
            }
        });

        if (position==lastCheckPosition){
            Typeface typefaceBold = ResourcesCompat.getFont(context, R.font.nunit_sans_bold);
            holder.binding.txtCategory.setTypeface(typefaceBold);
            holder.binding.txtCategory.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }else {
            Typeface typefaceRegular = ResourcesCompat.getFont(context, R.font.nunito_sans_regular);
            holder.binding.txtCategory.setTypeface(typefaceRegular);
            holder.binding.txtCategory.setTextColor(context.getResources().getColor(R.color.textDark));
        }

        if (tag.equals(Config.FLEET_TAG) && position==0 && checkFlag){
            checkFlag = false;
            ((FleetFragment)fleetFragment).onCategoryClick(model.getCategoryName());
        }

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivityCategorySetectionBgBinding binding;
        public ViewHolder(@NonNull ActivityCategorySetectionBgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
