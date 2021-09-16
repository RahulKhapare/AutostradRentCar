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
import com.autostrad.rentcar.adapter.BlogAdapter;
import com.autostrad.rentcar.databinding.ActivityBlogBinding;
import com.autostrad.rentcar.model.BlogModel;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;

import java.util.ArrayList;
import java.util.List;

public class BlogActivity extends AppCompatActivity {

    private BlogActivity activity = this;
    private ActivityBlogBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;

    private List<BlogModel> blogModelList;
    private BlogAdapter blogAdapter;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager linearLayoutManager;
    int count;
    int pageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blog);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.myBlog));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        blogModelList = new ArrayList<>();
        blogAdapter = new BlogAdapter(activity, blogModelList);
        linearLayoutManager = new LinearLayoutManager(activity);
        binding.recyclerBlog.setLayoutManager(linearLayoutManager);
        binding.recyclerBlog.setHasFixedSize(true);
        binding.recyclerBlog.setAdapter(blogAdapter);

        hitBlogData(pageCount);
        setPagination();
    }

    private void setPagination() {
        binding.recyclerBlog.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (blogModelList != null && !blogModelList.isEmpty()) {
                        if (blogModelList.size() < count) {
                            pageCount++;
                            hitBlogData(pageCount);
                        }
                    }
                }
            }
        });
    }

    public void hitBlogData(int pageCount) {

        ProgressView.show(activity, loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "blog?" + "page=" + pageCount + "&per_page=10&type=1").addJson(j)
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
                                BlogModel model = new BlogModel();
                                model.setId(jasonValue.getString("id"));
                                model.setBlog_type(jasonValue.getString("blog_type"));
                                model.setSlug(jasonValue.getString("slug"));
                                model.setTitle(jasonValue.getString("title"));
                                model.setDescription(jasonValue.getString("description"));
                                model.setThumbnail_image(jasonValue.getString("thumbnail_image"));
                                model.setAdd_date(jasonValue.getString("add_date"));
                                blogModelList.add(model);
                            }
                            blogAdapter.notifyDataSetChanged();
                        }

                    } else {
                        H.showMessage(activity, json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);
                    checkError();
                })
                .run("hitBlogData");
    }

    private void checkError() {
        if (blogModelList.isEmpty()) {
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