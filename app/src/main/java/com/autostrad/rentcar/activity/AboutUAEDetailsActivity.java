package com.autostrad.rentcar.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityBlogDetailsBinding;
import com.autostrad.rentcar.databinding.ActivityUaeDetailsBinding;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AboutUAEDetailsActivity extends AppCompatActivity {

    private AboutUAEDetailsActivity activity = this;
    private ActivityUaeDetailsBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_uae_details);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.aboutUAE));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        String blogSlug = getIntent().getStringExtra("blogSlug");
        String blogId = getIntent().getStringExtra("blogId");

        hitUAEDetailsData(blogSlug,blogId);
    }


    public void hitUAEDetailsData(String blogSlug,String blogId) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();
        j.addString("slug",blogSlug);
        j.addString("id",blogId);

        Api.newApi(activity, P.BaseUrl + "blog_detail").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        Json post_data = json.getJson("post_data");

                        String id = post_data.getString("id");
                        String blog_type = post_data.getString("blog_type");
                        String slug = post_data.getString("slug");
                        String title = post_data.getString("title");
                        String description = post_data.getString("description");
                        String image_url = post_data.getString("image_url");
                        String image = post_data.getString("image");
                        String add_date = post_data.getString("add_date");

                        LoadImage.glideString(activity,binding.imgBlogBanner,image_url);
                        binding.txtDate.setText(checkString(getFormatDate(add_date),binding.txtDate));
                        binding.txtTitle.setText(checkString(title,binding.txtDate));

                        binding.webView.loadDataWithBaseURL(null, description, "text/html", "utf-8", null);

                        binding.imgBlogBanner.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Click.preventTwoClick(v);
                                viewDialog(image_url);
                            }
                        });


                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);
                })
                .run("hitUAEDetailsData");
    }

    private String checkString(String string, TextView textView){
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

    private void viewDialog(String imagePath) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_image_view);

        PhotoView imageView = dialog.findViewById(R.id.imageView);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);

        LoadImage.glideString(activity,imageView,imagePath);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

}