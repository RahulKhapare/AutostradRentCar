package com.autostrad.rentcar.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.CareerAdapter;
import com.autostrad.rentcar.databinding.ActivityCareerBinding;
import com.autostrad.rentcar.model.CareerModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.Validation;
import com.autostrad.rentcar.util.WindowView;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CareerActivity extends AppCompatActivity implements CareerAdapter.onClick{

    private CareerActivity activity = this;
    private ActivityCareerBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;
    private List<CareerModel> careerModelList;
    private CareerAdapter careerAdapter;
    private String resumeDoc;

    private static final int REQUEST_PDF = 19;
    private String base64PDF = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_career);
        getAccess();
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.careers));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);

        careerModelList = new ArrayList<>();
        careerAdapter = new CareerAdapter(activity,careerModelList);
        binding.recyclerCareer.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerCareer.setHasFixedSize(true);
        binding.recyclerCareer.setNestedScrollingEnabled(false);
        binding.recyclerCareer.setAdapter(careerAdapter);

        hitCareerData();
    }

    @Override
    public void onApply(CareerModel model) {
//        onApplyDialog(model);
    }

    @Override
    public void onShare(CareerModel model) {

    }

    private void hitCareerData() {

        ProgressView.show(activity,loadingDialog);
        Api.newApi(activity, P.BaseUrl + "careers")
                .setMethod(Api.GET)
//                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                    checkData();
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        Json career = json.getJson(P.career);

                        String seo_title = career.getString("seo_title");
                        String seo_keywords = career.getString("seo_keywords");
                        String seo_description = career.getString("seo_description");
                        String page_title = career.getString("page_title");
                        String career_text_1 = career.getString("career_text_1");
                        String career_text_2 = career.getString("career_text_2");
                        String page_banner = career.getString("page_banner");

                        LoadImage.glideString(activity,binding.imgBanner,page_banner);
                        binding.txtPageTitle.setText(career_text_1);
                        binding.txtPageDesc.setText(career_text_2);

                        binding.imgBanner.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Click.preventTwoClick(v);
                                viewDialog(page_banner);
                            }
                        });

                        if (career_text_2.equals("") || career_text_2.equals("null")){
                            binding.txtPageDesc.setVisibility(View.GONE);
                        }

                        JsonList job_list = career.getJsonList(P.job_list);

                        if (job_list!=null && job_list.size()!=0){
                            for (Json jsonData : job_list){
                                CareerModel model = new CareerModel();
                                model.setId(jsonData.getString("id"));
                                model.setSlug(jsonData.getString("slug"));
                                model.setDesignation(jsonData.getString("designation"));
                                model.setLocation(jsonData.getString("location"));
                                model.setExperience(jsonData.getString("experience"));
                                model.setDescription(jsonData.getString("description"));
                                careerModelList.add(model);
                            }
                        }
                        careerAdapter.notifyDataSetChanged();
                        checkData();
                    }else {
                        checkData();
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitCareerData");

    }


    private void checkData(){
        if (careerModelList.isEmpty()){
            binding.txtError.setVisibility(View.VISIBLE);
        }else {
            binding.txtError.setVisibility(View.GONE);
        }
    }


    private void onApplyDialog(CareerModel model){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_job_apply);

        EditText etxFirstName = dialog.findViewById(R.id.etxFirstName);
        EditText etxLastName = dialog.findViewById(R.id.etxLastName);
        EditText etxEmail = dialog.findViewById(R.id.etxEmail);
        EditText etxNumber = dialog.findViewById(R.id.etxNumber);
        LinearLayout lnrDocument = dialog.findViewById(R.id.lnrDocument);
        ImageView imgClose = dialog.findViewById(R.id.imgClose);
        TextView txtSubmitResume = dialog.findViewById(R.id.txtSubmitResume);

        lnrDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                getPermission();
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });

        txtSubmitResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation(etxFirstName,etxLastName,etxEmail,etxNumber)){

                    try {
                        Json json = new Json();
                        json.put("job_id",model.getId());
                        json.put("name",etxFirstName.getText().toString().trim() + " " + etxLastName.getText().toString().trim());
                        json.put("email",etxEmail.getText().toString().trim());
                        json.put("mobile",etxNumber.getText().toString().trim());
                        json.put("upload_resume_file",resumeDoc);
                        json.put("description",model.getDescription());
                        hitApplyForJon(dialog,json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        H.showMessage(activity,getResources().getString(R.string.somethingWrong));
                    }

                }
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private boolean checkValidation(EditText etxFirstName,EditText etxLastName,EditText enterEmailId, EditText enterMobile) {
        boolean value = true;

        if (TextUtils.isEmpty(etxFirstName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterFirstName));
        } else if (TextUtils.isEmpty(etxLastName.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.pleaseEnterLastName));
        } else if (TextUtils.isEmpty(enterEmailId.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterEmailId));
        } else if (!Validation.validEmail(enterEmailId.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterEmailValid));
        } else if (TextUtils.isEmpty(enterMobile.getText().toString().trim())) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.enterMobile));
        } else if (TextUtils.isEmpty(resumeDoc)) {
            value = false;
            H.showMessage(activity, getResources().getString(R.string.checkDocument));
        }

        return value;
    }


    private void hitApplyForJon(Dialog dialog , Json jsonData) {

        ProgressView.show(activity,loadingDialog);
        Api.newApi(activity, P.BaseUrl + "apply_for_job").addJson(jsonData)
                .setMethod(Api.POST)
//                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                    checkData();
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        dialog.dismiss();
                    }else {

                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitApplyForJon");

    }


    private void getAccess() {
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        } catch (Exception e) {
        }
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PDF);
    }

    public void jumpToSetting() {
        H.showMessage(this, getResources().getString(R.string.allowPermission));
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PDF: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPDFIntent();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    jumpToSetting();
                } else {
                    getPermission();
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_PDF :
                if (resultCode == Activity.RESULT_OK && data != null) {
                    try {
                        Uri pdfUri = data.getData();
                        File file = new File(getPath(pdfUri));
                        String base64PDF = encodeFileToBase64Binary(file);
                        H.showMessage(activity,base64PDF);
                        hitUploadImage(base64PDF);
                    } catch (Exception e) {
                        Log.e("TAG", "onActivityResult: "+ e.getMessage() );
                        H.showMessage(this, getResources().getString(R.string.somethingWrong));
                    }
                }
                break;

        }
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private String encodeFileToBase64Binary(File yourFile) {
        int size = (int) yourFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes,Base64.NO_WRAP);
        return encoded;
    }

    private void openPDFIntent() {
        try {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_PDF);
        } catch (Exception e) {
            H.showMessage(this, getResources().getString(R.string.somethingWrong));
        }
    }


    private void hitUploadImage(String base64Image) {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.image,base64Image);
        j.addString(P.extension,"pdf");
        Api.newApi(activity, P.BaseUrl + "upload_image").addJson(j)
                .setMethod(Api.POST)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    ProgressView.dismiss(loadingDialog);
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        H.showMessage(activity,json.getString(P.msg));
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitUploadImage",session.getString(P.token));

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