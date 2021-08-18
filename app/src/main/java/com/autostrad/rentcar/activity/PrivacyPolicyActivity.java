package com.autostrad.rentcar.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityBlogBinding;
import com.autostrad.rentcar.databinding.ActivityPrivacyPolicyBinding;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;
import com.github.chrisbanes.photoview.PhotoView;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private PrivacyPolicyActivity activity = this;
    private ActivityPrivacyPolicyBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.privacyPolicy));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        hitPrivacyPolicyData();

    }

    public void hitPrivacyPolicyData() {

        ProgressView.show(activity, loadingDialog);
        Api.newApi(activity, P.BaseUrl + "pages/privacy_policy")
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        Json privacy_policy = json.getJson("privacy_policy");

                        String page_title = privacy_policy.getString("page_title");
                        String description = privacy_policy.getString("description");
                        String page_banner = privacy_policy.getString("page_banner");

                        LoadImage.glideString(activity,binding.imgBlogBanner,page_banner);

                        binding.webView.loadDataWithBaseURL(null, description, "text/html", "utf-8", null);

                        binding.imgBlogBanner.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Click.preventTwoClick(v);
                                viewDialog(page_banner);
                            }
                        });


                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);
                })
                .run("hitPrivacyPolicyData");
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