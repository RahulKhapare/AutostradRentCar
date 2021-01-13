package com.example.fastuae.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityBookingSucessfullBinding;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.WindowView;

public class BookingSucessfullActivity extends AppCompatActivity {

    private BookingSucessfullActivity activity = this;
    private ActivityBookingSucessfullBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_sucessfull);
        initView();
    }

   private void initView(){

        onClick();
   }

   private void onClick(){

        binding.txtBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                goToHome();
            }
        });

       binding.txtViewBooking.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Click.preventTwoClick(v);
               goToHome();
           }
       });
   }

   private void goToHome(){
       Intent mainIntent = new Intent(activity,MainActivity.class);
       mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(mainIntent);
       finish();
   }
    @Override
    public void onBackPressed() {
        goToHome();
    }
}