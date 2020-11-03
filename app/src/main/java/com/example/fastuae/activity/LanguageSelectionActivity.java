package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.adoisstudio.helper.H;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityLanguageSelectionBinding;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.WindowView;

public class LanguageSelectionActivity extends AppCompatActivity {

    private LanguageSelectionActivity activity = this;
    private ActivityLanguageSelectionBinding binding;
    private int languageFlag = 0;
    private int englishFlag = 1;
    private int urduFlag = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language_selection);
        initView();
    }

    private void initView(){

        binding.lnrEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                languageFlag = englishFlag;
                binding.lnrEnglish.setBackground(getResources().getDrawable(R.drawable.layout_color_bg));
                binding.lnrUrdu.setBackground(getResources().getDrawable(R.drawable.layout_bg));
            }
        });

        binding.lnrUrdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                languageFlag = urduFlag;
                binding.lnrEnglish.setBackground(getResources().getDrawable(R.drawable.layout_bg));
                binding.lnrUrdu.setBackground(getResources().getDrawable(R.drawable.layout_color_bg));
            }
        });

        binding.txtContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.preventTwoClick(view);
                if (languageFlag==englishFlag || languageFlag==urduFlag){
                    Intent continueIntent =  new Intent(activity,LoginDashboardActivity.class);
                    startActivity(continueIntent);
                    finish();
                }else {
                    H.showMessage(activity,getResources().getString(R.string.select_language));
                }
            }
        });
    }
}