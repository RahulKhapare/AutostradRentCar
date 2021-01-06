package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityMainBinding;
import com.example.fastuae.fragment.FleetFragment;
import com.example.fastuae.fragment.HomeFragment;
import com.example.fastuae.fragment.MenuFragment;
import com.example.fastuae.fragment.ProfileFragment;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.PdfDownloader;
import com.example.fastuae.util.WindowView;

import java.io.File;
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
    String pdf_url;
    String pdf_title;

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
            Intent intent = new Intent(activity,CarFilterActivity.class);
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

    public void checkPDF(String path) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_WRITE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkDirectory(activity, pdf_url, pdf_title);
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    jumpToSetting();
                } else {
                    getPermission();
                }
                return;
            }

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

    @Override
    public void onBackPressed() {
        onBackAction();
    }

}