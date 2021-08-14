package com.example.fastuae.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.example.fastuae.R;
import com.example.fastuae.adapter.DocumentAdapter;
import com.example.fastuae.databinding.ActivityEditDocumentBinding;
import com.example.fastuae.model.DocumentModel;
import com.example.fastuae.model.FieldModel;
import com.example.fastuae.model.ImagePathModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.LoadImage;
import com.example.fastuae.util.P;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DocumentEditActivity extends AppCompatActivity implements DocumentAdapter.onClick{

    private DocumentEditActivity activity = this;
    private ActivityEditDocumentBinding binding;

    private List<DocumentModel> documentModelList;
    private DocumentAdapter adapter;
    private Session session;
    private LoadingDialog loadingDialog;
    String country_id = "";
    String documentName;
    String base64Image;
    TextView textViewDocumnt;
    TextView txtImagePath;
    private static final int READ_WRITE = 20;
    private static final int PDF_DATA = 22;

    private List<ImagePathModel> imagePathModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_document);
        initView();


    }

    private void initView(){

        getAccess();

        binding.toolbar.setTitle(getResources().getString(R.string.document));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        loadingDialog = new LoadingDialog(activity);
        session = new Session(activity);

        imagePathModelList = new ArrayList<>();

        documentModelList = new ArrayList<>();
        adapter = new DocumentAdapter(activity,documentModelList,2);
        binding.recyclerDocument.setLayoutManager(new LinearLayoutManager(activity));
//        binding.recyclerDocument.setNestedScrollingEnabled(false);
        binding.recyclerDocument.setAdapter(adapter);

        binding.txtSaveDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Json jsonMain = new Json();
                Json json = new Json();

                try {
                    for (DocumentModel model : documentModelList){

                        if (model.getCheckValue().equals("1")){
//                            jsonMain.addString(model.getKey(),"1");
                            for (FieldModel fieldModel : model.getFieldList()){

                                for (ImagePathModel imageModel : imagePathModelList){
                                    if (imageModel.getTitle().equals(model.getTitle())){
                                        if (fieldModel.getJson().has(P.image)){
                                            fieldModel.getJson().remove(P.image);
                                            fieldModel.getJson().addString(P.image,imageModel.getPath());
                                        }else {
                                            fieldModel.getJson().addString(P.image,imageModel.getPath());
                                        }
                                    }
                                }

                                json.addJSON(model.getKey(),fieldModel.getJson());

                                if (fieldModel.getJson().has("key")){
                                    fieldModel.getJson().remove("key");
                                }else {
                                    fieldModel.getJson().addString("key","1");
                                }


                            }
                        }

                        jsonMain.addJSON(P.document,json);
                    }


                }catch (Exception e){
                }

                Log.e("TAG", "onClickJSJJS: "+ jsonMain.toString() );

//                hitSaveUserDocumentDetails(jsonMain);

            }
        });

        hitUserBookingDetails();

    }

    private void hitUserBookingDetails() {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();

        Api.newApi(activity, P.BaseUrl + "user_data_for_booking").addJson(j)
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
                        json = json.getJson(P.user);
                        json = json.getJson(P.user_info);
                        country_id = json.getString(P.country_id);
                        String user_country_code = json.getString(P.user_country_code);
                        hitDocumentData(country_id);

                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitUserBookingDetails",session.getString(P.token));

    }

    private void hitDocumentData(String country_id) {
        documentModelList.clear();
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        Api.newApi(activity, P.BaseUrl + "customer_profile_document_mobile").addJson(j)
                .setMethod(Api.GET)
                //.onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    ProgressView.dismiss(loadingDialog);
                    if (json.getInt(P.status) == 1) {

                        try {
                            JSONObject jsonObject = json.getJson(P.data);
                            JSONArray author = jsonObject.getJSONArray(P.document);
                            String arrayOne = author.getString(0);
                            String arrayTwo = author.getString(1);

                            JsonList jsonListOne = new JsonList(arrayOne);
                            JsonList jsonListTwo = new JsonList(arrayTwo);

                            if (!TextUtils.isEmpty(country_id) && !country_id.equals("null") && country_id.equals("229")){

                                for (Json jsonData : jsonListOne){
                                    DocumentModel documentModel = new DocumentModel();
                                    documentModel.setTitle(jsonData.getString(P.title));
                                    documentModel.setKey(jsonData.getString(P.key));
                                    documentModel.setField(jsonData.getJsonArray(P.field));
                                    documentModel.setCheckValue("0");
                                    documentModelList.add(documentModel);
                                }
                            }else {
                                for (Json jsonData : jsonListTwo){
                                    DocumentModel documentModel = new DocumentModel();
                                    documentModel.setTitle(jsonData.getString(P.title));
                                    documentModel.setKey(jsonData.getString(P.key));
                                    documentModel.setField(jsonData.getJsonArray(P.field));
                                    documentModel.setCheckValue("0");
                                    documentModelList.add(documentModel);
                                }
                            }

                            adapter.notifyDataSetChanged();

                        }catch (Exception e){

                        }
                        checkData();
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                        checkData();
                    }

                })
                .run("hitDocumentData",session.getString(P.token));

    }


    private void checkData(){
        if (documentModelList.isEmpty()){
            binding.txtSaveDocument.setVisibility(View.GONE);
        }else {
            binding.txtSaveDocument.setVisibility(View.VISIBLE);
        }
    }

    private void hitSaveUserDocumentDetails(Json j) {

        ProgressView.show(activity,loadingDialog);
        Api.newApi(activity, P.BaseUrl + "user_save_documents").addJson(j)
                .setMethod(Api.POST)
//                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    H.showMessage(activity, "On error is called");
                })
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 1) {
                        imagePathModelList.clear();
                        H.showMessage(activity,getResources().getString(R.string.dataUpdated));
                        finish();
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitSaveUserDocumentDetails",session.getString(P.token));

    }

    @Override
    public void downloadDocument(String name, TextView textView,TextView txtImage) {
        textViewDocumnt = textView;
        txtImagePath = txtImage;
        documentName = name;
        getPermission();
    }

    private void getAccess() {
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        } catch (Exception e) {
        }
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_WRITE);
    }

    public void jumpToSetting() {
        H.showMessage(activity, getResources().getString(R.string.allowPermission));
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void getDocument(){
        try {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PDF_DATA);
        } catch (Exception e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_WRITE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDocument();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    jumpToSetting();
                } else {
                    getPermission();
                }
                return;
            }

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PDF_DATA:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    try {
                        Uri selectedPDF = data.getData();

                        String path = getBase64Image(selectedPDF);
                        hitUploadImage(path);

                    } catch (Exception e) {
                        H.showMessage(activity,e.getMessage());
                    }
                }
                break;
        }
    }


    private String getBase64Image(Uri uri) {
        base64Image = "";
        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            base64Image = encodeImage(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "setImageDataEE: "+ e.getMessage() );
            H.showMessage(activity, "Unable to get image, try again.");
        }

        return base64Image;
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    private void hitUploadImage(String base64Image) {

        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.image,base64Image);
        j.addString(P.extension,"png");
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
                        String image = json.getString(P.image);
                        String image_url = json.getString(P.image_url);
                        ImagePathModel model = new ImagePathModel();
                        model.setPath(image);
                        model.setTitle(documentName);
                        imagePathModelList.add(model);
                        textViewDocumnt.setText(getResources().getString(R.string.uploaded) + " " +documentName);
                        txtImagePath.setText(getResources().getString(R.string.imagePath) + " " +image);
                        txtImagePath.setVisibility(View.VISIBLE);
                        txtImagePath.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewDialog(image_url);
                            }
                        });
                        H.showMessage(activity,getResources().getString(R.string.imageUploaded));
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitUploadImage",session.getString(P.token));

    }


    private void documentDialog(String imagePath) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_document_view);

        ImageView imgDocument = dialog.findViewById(R.id.imgDocument);
        LoadImage.glideString(activity, imgDocument, imagePath);

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

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
            onBackPressed();
        }
        return false;
    }


}