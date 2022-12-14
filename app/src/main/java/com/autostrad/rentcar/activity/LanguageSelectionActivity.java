package com.autostrad.rentcar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.databinding.ActivityLanguageSelectionBinding;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.LoadImage;
import com.autostrad.rentcar.util.Localization;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.WindowView;

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
        Localization.loadLocal(activity,session.getString(P.languageFlag));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language_selection);
        initView();
    }

    private void initView() {

        LoadImage.glide(activity,binding.imgName,getResources().getDrawable(R.drawable.ic_name));
        LoadImage.glide(activity,binding.imgCar,getResources().getDrawable(R.drawable.ic_car_black));

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
                updateLocalization(Config.ENGLISH);
            }
        });

        binding.lnrArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                updateArabicLanguage(Config.ARABIC);
                updateLocalization(Config.ARABIC);
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
        binding.lnrEnglish.setBackground(getResources().getDrawable(R.drawable.layout_language_color_bg));
        binding.lnrArabic.setBackground(getResources().getDrawable(R.drawable.layout_bg));

        binding.imgEnglish.setColorFilter(ContextCompat.getColor(activity, R.color.lightBlue), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.imgUrdu.setColorFilter(ContextCompat.getColor(activity, R.color.colorWhite), android.graphics.PorterDuff.Mode.MULTIPLY);

        binding.txtEnglish.setTextColor(getResources().getColor(R.color.lightBlue));
        binding.txtUrdu.setTextColor(getResources().getColor(R.color.colorWhite));

        session.addString(P.languageFlag, language);
    }

    private void updateArabicLanguage(String language) {
        languageFlag = urduFlag;
        binding.lnrEnglish.setBackground(getResources().getDrawable(R.drawable.layout_bg));
        binding.lnrArabic.setBackground(getResources().getDrawable(R.drawable.layout_language_color_bg));

        binding.imgEnglish.setColorFilter(ContextCompat.getColor(activity, R.color.colorWhite), android.graphics.PorterDuff.Mode.MULTIPLY);
        binding.imgUrdu.setColorFilter(ContextCompat.getColor(activity, R.color.lightBlue), android.graphics.PorterDuff.Mode.MULTIPLY);

        binding.txtUrdu.setTextColor(getResources().getColor(R.color.lightBlue));
        binding.txtEnglish.setTextColor(getResources().getColor(R.color.colorWhite));

        session.addString(P.languageFlag, language);
    }

    private void updateLocalization(String language) {
        Localization.setLocal(activity, language);
    }

}