package com.autostrad.rentcar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.autostrad.rentcar.R;
import com.autostrad.rentcar.activity.AboutUAEDetailsActivity;
import com.autostrad.rentcar.activity.BlogDetailsActivity;
import com.autostrad.rentcar.databinding.ActivityBlogListBinding;
import com.autostrad.rentcar.model.BlogModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.LoadImage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.viewHolder> {

    private Context context;
    private List<BlogModel> blogModelList;

    public BlogAdapter(Context context, List<BlogModel> blogModelList) {
        this.context = context;
        this.blogModelList = blogModelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActivityBlogListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.activity_blog_list, parent, false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        BlogModel model = blogModelList.get(position);

        LoadImage.glideString(context,holder.binding.imgBlog,model.getThumbnail_image());
        holder.binding.txtDate.setText(checkString(getFormatDate(model.getAdd_date()),holder.binding.txtDate));
        holder.binding.txtTitle.setText(checkString(model.getTitle(),holder.binding.txtDate));

        holder.binding.lnrBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(context, AboutUAEDetailsActivity.class);
                intent.putExtra("blogSlug",model.getSlug());
                intent.putExtra("blogId",model.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ActivityBlogListBinding binding;

        public viewHolder(@NonNull ActivityBlogListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String checkString(String string,TextView textView){
        String value = "";
        if (string==null || string.equals("") || string.equals("null")){
            textView.setVisibility(View.GONE);
        }else {
            value = string;
        }
        return value;
    }

    private String getFormatDate(String actualDate){
        String app_date = "";
        try {
            app_date = actualDate;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = (Date)formatter.parse(app_date);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd.MM.yyyy");
            String finalString = newFormat.format(date);
            app_date = finalString;
        }catch (Exception e){
            app_date = actualDate;
        }

        return app_date;
    }

}
