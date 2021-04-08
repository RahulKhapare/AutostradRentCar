package com.example.fastuae.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.databinding.ActivityCarFleetDetailsBinding;
import com.example.fastuae.model.CarFleetModel;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.WindowView;
import com.squareup.picasso.Picasso;

public class CarFleetDetailsActivity extends AppCompatActivity{

    private CarFleetDetailsActivity activity = this;
    private ActivityCarFleetDetailsBinding binding;

    private Session session;
    private LoadingDialog loadingDialog;
    private String flag;
    private CarFleetModel carFleetModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_fleet_details);
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        flag = session.getString(P.languageFlag);
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle("");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        carFleetModel = Config.carFleetModel;

        setData();
        updateIcons();

    }

    private void setData(){
        Picasso.get().load(R.drawable.ic_car_horizontal).placeholder(R.drawable.ic_no_car).error(R.drawable.ic_no_car).into(binding.imgCar);
//        Picasso.get().load(carFleetModel.getImage()).placeholder(R.drawable.ic_no_car).error(R.drawable.ic_no_car).into(binding.imgCar);
        Picasso.get().load(carFleetModel.getImage1()).placeholder(R.drawable.ic_no_car).error(R.drawable.ic_no_car).into(binding.img1);
        Picasso.get().load(carFleetModel.getImage2()).placeholder(R.drawable.ic_no_car).error(R.drawable.ic_no_car).into(binding.img2);
        Picasso.get().load(carFleetModel.getImage3()).placeholder(R.drawable.ic_no_car).error(R.drawable.ic_no_car).into(binding.img3);

        binding.txtCarName.setText(carFleetModel.getCarName());
        binding.txtCarGroup.setText(carFleetModel.getGroupName());
        binding.txtCarType.setText(carFleetModel.getCarType());
        binding.txtDescription.setText(carFleetModel.getDescription());

        binding.txtSeat.setText(carFleetModel.getSeat());
        binding.txtAutomatic.setText(carFleetModel.getAutomatic());
        binding.txtPassengers.setText(carFleetModel.getPassenger());
        binding.txtDoors.setText(carFleetModel.getDoor());
        binding.txtPetrol.setText(carFleetModel.getPetrol());
        binding.txtSuitcase.setText(carFleetModel.getSuitcase());
        binding.txtEngine.setText(carFleetModel.getEngine());
        binding.txtSuitcaseTwo.setText(carFleetModel.getSuitcase());

    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {


//            binding.imgPassengerRight.setVisibility(View.GONE);
//            binding.imgPassengerLeft.setVisibility(View.VISIBLE);


        } else if (flag.equals(Config.ENGLISH)) {

//            binding.imgPassengerRight.setVisibility(View.VISIBLE);
//            binding.imgPassengerLeft.setVisibility(View.GONE);

        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}