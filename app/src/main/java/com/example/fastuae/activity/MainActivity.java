package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.fastuae.util.WindowView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();
    }

    private void initView(){
        session = new Session(activity);

        binding.toolbar.setTitle(getResources().getString(R.string.welcome) + " " + session.getString(P.user_name));
        setSupportActionBar(binding.toolbar);

        fragmentManager = getSupportFragmentManager();

        homeFragment = HomeFragment.newInstance();
        fragmentLoader(homeFragment, Config.HOME);
    }

    public void fragmentLoader(Fragment fragment, String tag) {
        Config.currentFlag = tag;
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit)
                .replace(R.id.fragmentFrame, fragment, tag)
                .addToBackStack(tag)
                .commit();
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

    @Override
    public void onBackPressed() {
        onBackAction();
    }
}