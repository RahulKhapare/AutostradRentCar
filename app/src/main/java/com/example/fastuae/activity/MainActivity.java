package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityMainBinding;
import com.example.fastuae.fragment.FleetFragment;
import com.example.fastuae.fragment.HomeFragment;
import com.example.fastuae.fragment.MenuFragment;
import com.example.fastuae.fragment.ProfileFragment;
import com.example.fastuae.model.DocumentModel;
import com.example.fastuae.model.ImagePathModel;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.PdfDownloader;
import com.example.fastuae.util.ProgressView;
import com.example.fastuae.util.WindowView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MainActivity activity = this;
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private FleetFragment fleetFragment;
    private MenuFragment menuFragment;
    private final int TIME_DELAY = 2000;
    private long back_pressed;
    private Session session;
    private MenuItem ic_filter;
    private static final int READ_WRITE = 20;
    private static final int PDF_DATA = 22;
    String pdf_url;
    String pdf_title;
    String documentName;
    String base64Image;
    public static List<ImagePathModel> imagePathModelList;

    public int download = 1;
    public int upload = 2;
    public int click = 0;

    private LoadingDialog loadingDialog;
    TextView textViewDocumnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        getAccess();
        initView();
    }

    private void initView() {
        session = new Session(activity);

        loadingDialog = new LoadingDialog(activity);
        imagePathModelList = new ArrayList<>();
        binding.toolbar.setTitle(getResources().getString(R.string.welcome) + " " + session.getString(P.user_name));
        setSupportActionBar(binding.toolbar);

        fragmentManager = getSupportFragmentManager();

        homeFragment = HomeFragment.newInstance();
        fragmentLoader(homeFragment, Config.HOME);
    }

    private void updateToolbarName(String tag) {
        if (tag.equals(Config.HOME)) {
            binding.toolbar.setTitle(getResources().getString(R.string.welcome) + " " + session.getString(P.user_name));
            hideBackArrow();
            if (ic_filter!=null){
                ic_filter.setVisible(false);
            }
        }else if (tag.equals(Config.PROFILE)){
            binding.toolbar.setTitle(getResources().getString(R.string.profile));
            showBackArrow();
            if (ic_filter!=null){
                ic_filter.setVisible(false);
            }
        }else if (tag.equals(Config.FLEET)){
            binding.toolbar.setTitle(getResources().getString(R.string.carFleet));
            showBackArrow();
            if (ic_filter!=null){
                ic_filter.setVisible(true);
            }
        }else if (tag.equals(Config.MENU)){
            binding.toolbar.setTitle(getResources().getString(R.string.menu));
            showBackArrow();
            if (ic_filter!=null){
                ic_filter.setVisible(false);
            }
        }
    }

    public void fragmentLoader(Fragment fragment, String tag) {
        Config.currentFlag = tag;
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit)
                .replace(R.id.fragmentFrame, fragment, tag)
                .addToBackStack(tag)
                .commit();

        updateToolbarName(tag);
    }

    public void onBottomBarClick(View view) {
        int i = view.getId();
        selectBottomNavigation(view);
        Click.preventTwoClick(view);
        String currentFlag = Config.currentFlag;
        switch (i) {
            case R.id.lnrHome: {
                if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.HOME)) {
                    homeFragment = HomeFragment.newInstance();
                    fragmentLoader(homeFragment, Config.HOME);
                }
                break;
            }
            case R.id.lnrProfile: {
                if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.PROFILE)) {
                    profileFragment = ProfileFragment.newInstance();
                    fragmentLoader(profileFragment, Config.PROFILE);
                }
                break;
            }
            case R.id.lnrFleet: {
                if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.FLEET)) {
                    fleetFragment = FleetFragment.newInstance();
                    fragmentLoader(fleetFragment, Config.FLEET);
                }
                break;
            }
            case R.id.lnrMenu: {
                if (!TextUtils.isEmpty(currentFlag) && !currentFlag.equals(Config.MENU)) {
                    menuFragment = MenuFragment.newInstance();
                    fragmentLoader(menuFragment, Config.MENU);
                }
                break;
            }
        }
    }

    private void selectBottomNavigation(View view) {
        LinearLayout parentLayout = findViewById(R.id.lnrBottom);
        int j = (int) H.convertDpToPixel(30, this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(j, j);
        //LinearLayout linearLayout = (LinearLayout) view.getParent();
        LinearLayout childLayout;
        View v;
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;

        for (int i = 0; i < parentLayout.getChildCount(); i++) {

            childLayout = (LinearLayout) parentLayout.getChildAt(i);
            imageView = childLayout.findViewWithTag("imageView");
            imageView.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));

            textView = childLayout.findViewWithTag("textView");
            textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            linearLayout = childLayout.findViewWithTag("lnrTag");
            linearLayout.setBackgroundColor(getResources().getColor(R.color.colorWhite));

        }

        j = (int) H.convertDpToPixel(32, this);
        layoutParams = new LinearLayout.LayoutParams(j, j);

        imageView = view.findViewWithTag("imageView");
        imageView.setColorFilter(getResources().getColor(R.color.lightBlue));

        textView = view.findViewWithTag("textView");
        textView.setTextColor(getResources().getColor(R.color.lightBlue));

        linearLayout = view.findViewWithTag("lnrTag");
        linearLayout.setBackgroundColor(getResources().getColor(R.color.lightBlue));

    }

    public void onBackAction() {
        try {
            HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(Config.HOME);
            if (homeFragment != null && homeFragment.isVisible()) {
                finishAction();
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    if (!(getSupportFragmentManager().getBackStackEntryCount() == 1)) {
                        getSupportFragmentManager().popBackStack();
                        String title = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 2).getName();
                        updateBottomIcon(title);
                    } else {
                        finishAction();
                    }
                } else {
                    finishAction();
                }
            }
        } catch (Exception e) {

        }

    }

    private void updateBottomIcon(String tag) {
        if (tag.equals(Config.HOME)) {
            selectBottomNavigation(binding.lnrHome);
        } else if (tag.equals(Config.PROFILE)) {
            selectBottomNavigation(binding.lnrProfile);
        } else if (tag.equals(Config.FLEET)) {
            selectBottomNavigation(binding.lnrFleet);
        } else if (tag.equals(Config.MENU)) {
            selectBottomNavigation(binding.lnrMenu);
        }
        updateToolbarName(tag);
    }

    private void finishAction() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            finishAffinity();
            finish();
        } else {
            H.showMessage(activity, getResources().getString(R.string.exitMessage));
        }
        back_pressed = System.currentTimeMillis();
    }

    private void showBackArrow(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void hideBackArrow(){
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        ic_filter = menu.findItem(R.id.ic_filter);
        if (ic_filter!=null){
            ic_filter.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        else if (item.getItemId() == R.id.ic_filter) {
            Intent intent = new Intent(activity,CarFleetFilterActivity.class);
            startActivity(intent);
        }
        return false;
    }

    private void getAccess() {
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        } catch (Exception e) {
        }
    }

    public void uploadDocument(String name,TextView textView) {
        textViewDocumnt = textView;
        documentName = name;
        click = upload;
        getPermission();
    }

    public void checkPDF(String path) {
        click = download;
        pdf_url = path;
        pdf_title = randomText();
        if (TextUtils.isEmpty(pdf_url) || pdf_url.equals("null")) {
            H.showMessage(activity, getResources().getString(R.string.noPdfFound));
        } else {
            getPermission();
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

    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_WRITE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (click==upload){
                        getDocument();
                    }else if (click==download){
                        checkDirectory(activity, pdf_url, pdf_title);
                    }
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


    private void getDocument(){
        try {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PDF_DATA);
        } catch (Exception e) {
        }
    }

    private void checkDirectory(Context context, String fileURL, String title) {
        try {
            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/FastUAE/Pdf/";
            String fileName = title + ".pdf";
            destination += fileName;
            File direct = new File(destination);
            if (direct.exists()) {
                openPdf(context, destination);
            } else {
                PdfDownloader.download(context, fileURL, title, Config.OPEN);
            }
        } catch (Exception e) {
            H.showMessage(context, getResources().getString(R.string.somethingWrong));
        }

    }

    private void openPdf(Context context, String filepath) {
        File pdfFile = new File(filepath);
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            context.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            H.showMessage(context, getResources().getString(R.string.noPdfAppAvailable));
        }
    }

    private String randomText() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
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
                        H.showMessage(activity,getResources().getString(R.string.imageUploaded));
                    }else {
                        H.showMessage(activity,json.getString(P.error));
                    }
                })
                .run("hitUploadImage",session.getString(P.token));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imagePathModelList!=null){
            imagePathModelList.clear();
        }
    }

    @Override
    public void onBackPressed() {
        onBackAction();
    }

}