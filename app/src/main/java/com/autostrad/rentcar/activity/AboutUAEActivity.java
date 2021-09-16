package com.autostrad.rentcar.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.AboutUAEAdapter;
import com.autostrad.rentcar.databinding.ActivityAboutUaeBinding;
import com.autostrad.rentcar.model.AboutUAEModel;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class AboutUAEActivity extends AppCompatActivity {

    private AboutUAEActivity activity = this;
    private ActivityAboutUaeBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;

    private List<AboutUAEModel> aboutUAEModelList;
    private AboutUAEAdapter uaeAdapter;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    int count;
    int pageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_uae);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.aboutUAE));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        aboutUAEModelList = new ArrayList<>();
        uaeAdapter = new AboutUAEAdapter(activity, aboutUAEModelList);
        linearLayoutManager = new LinearLayoutManager(activity);
        binding.recyclerUAE.setLayoutManager(linearLayoutManager);
        binding.recyclerUAE.setHasFixedSize(true);
        binding.recyclerUAE.setAdapter(uaeAdapter);

        hitUAEData(pageCount);
        setPagination();

    }

    private void setPagination() {
        binding.recyclerUAE.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    loading = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (loading && (visibleItemCount + pastVisiblesItems == totalItemCount)) {
                    loading = false;
                    if (aboutUAEModelList != null && !aboutUAEModelList.isEmpty()) {
                        if (aboutUAEModelList.size() < count) {
                            pageCount++;
                            hitUAEData(pageCount);
                        }
                    }
                }
            }
        });
    }

    public void hitUAEData(int pageCount) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "blog?" + "page=" + pageCount + "&per_page=10&type=2").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                    checkError();
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        count = json.getInt("num_rows");
                        JsonList post_list = json.getJsonList(P.post_list);

                        if (post_list != null && post_list.size() != 0) {
                            for (Json jasonValue : post_list) {
                                AboutUAEModel model = new AboutUAEModel();
                                model.setId(jasonValue.getString("id"));
                                model.setBlog_type(jasonValue.getString("blog_type"));
                                model.setSlug(jasonValue.getString("slug"));
                                model.setTitle(jasonValue.getString("title"));
                                model.setDescription(jasonValue.getString("description"));
                                model.setThumbnail_image(jasonValue.getString("thumbnail_image"));
                                model.setAdd_date(jasonValue.getString("add_date"));
                                aboutUAEModelList.add(model);
                            }
                            uaeAdapter.notifyDataSetChanged();
                        }

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);
                    checkError();
                })
                .run("hitUAEData");
    }

    private void checkError() {
        if (aboutUAEModelList.isEmpty()) {
            binding.txtError.setVisibility(View.VISIBLE);
        } else {
            binding.txtError.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

}