package com.example.fastuae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityLanguageSelectionBinding;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.Localization;
import com.example.fastuae.util.P;
import com.example.fastuae.util.WindowView;

public class LanguageSelectionActivity extends AppCompatActivity {

    private LanguageSelectionActivity activity = this;
    private ActivityLanguageSelectionBinding binding;
    private int languageFlag = 0;
    private int englishFlag = 1;
    private int urduFlag = 2;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        session = new Session(activity);
//        Localization.loadLocal(activity,session.getString(P.languageFlag));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language_selection);
        initView();
    }

    private void initView() {

        if (session.getString(P.languageFlag).equals(Config.ENGLISH)){
            updateEnglishLanguage(Config.ENGLISH);
        }else if (session.getString(P.languageFlag).equals(Config.ARABIC)){
            updateArabicLanguage(Config.ARABIC);
        }

        binding.lnrEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                updateEnglishLanguage(Config.ENGLISH);
//                updateLocalization(Config.ENGLISH);
            }
        });

        binding.lnrArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                updateArabicLanguage(Config.ARABIC);
//                updateLocalization(Config.ARABIC);
            }
        });

        binding.txtContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                if (languageFlag == englishFlag || languageFlag == urduFlag) {
                    Intent continueIntent = new Intent(activity, LoginDashboardActivity.class);
                    startActivity(continueIntent);
                    finish();
                } else {
                    H.showMessage(activity, getResources().getString(R.string.select_language));
                }
            }
        });
    }

    private void updateEnglishLanguage(String language) {
        languageFlag = englishFlag;
        binding.lnrEnglish.setBackground(getResources().getDrawable(R.drawable.layout_color_bg));
        binding.lnrArabic.setBackground(getResources().getDrawable(R.drawable.layout_bg));
        session.addString(P.languageFlag, language);
    }

    private void updateArabicLanguage(String language) {
        languageFlag = urduFlag;
        binding.lnrEnglish.setBackground(getResources().getDrawable(R.drawable.layout_bg));
        binding.lnrArabic.setBackground(getResources().getDrawable(R.drawable.layout_color_bg));
        session.addString(P.languageFlag, language);
    }

    private void updateLocalization(String language) {
        Localization.setLocal(activity, language);
    }

}