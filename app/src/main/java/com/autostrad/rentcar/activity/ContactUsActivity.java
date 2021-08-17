package com.autostrad.rentcar.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;
import com.autostrad.rentcar.R;
import com.autostrad.rentcar.adapter.ContactAdapter;
import com.autostrad.rentcar.adapter.LocationFilterAdapter;
import com.autostrad.rentcar.databinding.ActivityContactUsBinding;
import com.autostrad.rentcar.fragment.HomeFragment;
import com.autostrad.rentcar.model.LocationModel;
import com.autostrad.rentcar.util.Click;
import com.autostrad.rentcar.util.Config;
import com.autostrad.rentcar.util.P;
import com.autostrad.rentcar.util.ProgressView;
import com.autostrad.rentcar.util.WindowView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactUsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback,LocationFilterAdapter.onClick {

    private ContactUsActivity activity = this;
    private ActivityContactUsBinding binding;
    private LoadingDialog loadingDialog;
    private Session session;
    private String flag;

    private List<LocationModel> locationModelList;
    private LocationFilterAdapter locationFilterAdapter;

    private List<LocationModel> contactModelList;
    private ContactAdapter contactAdapter;

    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private int REQUEST_LOCATION = 1;
    double currentLat = 0;
    double currentLong = 0;
    boolean isUpdateSearch = false;
    private String setLocation;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;
    private String emirateID;
    private boolean callTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        session = new Session(activity);
        loadingDialog = new LoadingDialog(activity);
        flag = session.getString(P.languageFlag);
        updateIcons();
        initView();
    }

    private void initView() {
        binding.toolbar.setTitle(getResources().getString(R.string.contactUs));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        callTime = false;

        locationModelList = new ArrayList<>();
        locationFilterAdapter = new LocationFilterAdapter(activity,locationModelList,1);
        binding.recyclerLocation.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerLocation.setAdapter(locationFilterAdapter);

        contactModelList = new ArrayList<>();
        contactAdapter = new ContactAdapter(activity,contactModelList);
        binding.recyclerContacts.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerContacts.setAdapter(contactAdapter);

        setData();
        setLocationData();
        onClick();
        setMapData();
    }

    private void setMapData(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrame);
        mapFragment.getMapAsync(activity);
        checkGPS();
    }

    private void setData(){

        binding.txtLocation.setText("Corner Building Musafah Industrial 2(M2) Abu Dhabi, UAE");
        binding.txtEmail.setText("assist@fastuae.com");
        binding.txtTelephoneNo.setText("600-500-101");
        binding.txtMobileNo.setText("971 2815 2700");

    }

    private void setLocationData(){

        JsonList emirate_list = HomeFragment.emirate_list;
        if (emirate_list != null && emirate_list.size() != 0) {
            for (Json json : emirate_list) {
                String id = json.getString(P.id);
                String emirate_name = json.getString(P.emirate_name);
                String status = json.getString(P.status);
                LocationModel model = new LocationModel();
                model.setId(id);
                model.setEmirate_name(emirate_name);
                model.setStatus(status);
                locationModelList.add(model);
            }
            locationFilterAdapter.notifyDataSetChanged();
            binding.txtArea.setText(locationModelList.get(0).getEmirate_name());
            emirateID = locationModelList.get(0).getEmirate_id();
            hitLocationData(emirateID);
        }

        if (locationModelList.isEmpty()){
            binding.lnrLocationView.setVisibility(View.GONE);
        }else {
            binding.lnrLocationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFilterClick(LocationModel model) {
        checkLocationViewView();
        binding.txtArea.setText(model.getEmirate_name());
        emirateID = model.getEmirate_id();
        hitLocationData(emirateID);
    }

    private void onClick(){

        binding.cardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkLocationViewView();
            }
        });

        binding.txtViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                binding.txtViewMore.setVisibility(View.GONE);
                hitLocationData(emirateID);
            }
        });

    }

    private void checkLocationViewView() {
        if (binding.lnrLocationListView.getVisibility() == View.VISIBLE) {
            binding.lnrLocationListView.setVisibility(View.GONE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgLocationLeft.setImageResource(R.drawable.ic_down_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgLocationRight.setImageResource(R.drawable.ic_down_arrow);
            }
        } else if (binding.lnrLocationListView.getVisibility() == View.GONE) {
            binding.lnrLocationListView.setVisibility(View.VISIBLE);
            if (flag.equals(Config.ARABIC)) {
                binding.imgLocationLeft.setImageResource(R.drawable.ic_up_arrow);
            } else if (flag.equals(Config.ENGLISH)) {
                binding.imgLocationRight.setImageResource(R.drawable.ic_up_arrow);
            }
        }
    }

    private void updateIcons() {

        if (flag.equals(Config.ARABIC)) {

            binding.imgLocationRight.setVisibility(View.GONE);
            binding.imgLocationLeft.setVisibility(View.VISIBLE);

        } else if (flag.equals(Config.ENGLISH)) {

            binding.imgLocationRight.setVisibility(View.VISIBLE);
            binding.imgLocationLeft.setVisibility(View.GONE);

        }
    }

    private void hitLocationData(String emirateID) {
        contactModelList.clear();
        ProgressView.show(activity,loadingDialog);
        Api.newApi(activity, P.BaseUrl + "location?"+"emirate_id=" + emirateID)
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
                        JsonList location_list = json.getJsonList(P.location_list);

                        if (location_list!=null && location_list.size()!=0){

                            for (Json jsonData : location_list){
                                LocationModel model = new LocationModel();
                                model.setId(jsonData.getString(P.id));
                                model.setEmirate_id(jsonData.getString(P.emirate_id));
                                model.setEmirate_name(jsonData.getString(P.emirate_name));
                                model.setLocation_name(jsonData.getString(P.location_name));
                                model.setLocation_timing(jsonData.getString(P.location_timing));
                                model.setAddress(jsonData.getString(P.address));
                                model.setStatus(jsonData.getString(P.status));
                                model.setContact_number(jsonData.getString(P.contact_number));
                                model.setContact_email(jsonData.getString(P.contact_email));
                                model.setLocation_time_data(jsonData.getJsonArray(P.location_time_data));

                                if (!callTime){
                                    if (contactModelList.size()<3){
                                        contactModelList.add(model);
                                    }
                                }else {
                                    contactModelList.add(model);
                                }

                            }

                            callTime = true;
                            contactAdapter.notifyDataSetChanged();

                        }
                        checkData();
                    }else {
                        checkData();
                        H.showMessage(activity,json.getString(P.error));
                    }
                    ProgressView.dismiss(loadingDialog);

                })
                .run("hitLocationData");

    }

    private void checkData(){
        if (contactModelList.isEmpty()){
            binding.txtError.setVisibility(View.VISIBLE);
            binding.txtViewMore.setVisibility(View.GONE);
        }else {
            binding.txtError.setVisibility(View.GONE);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateLocation(currentLat, currentLong);
    }

    private void checkGPS() {
        if (statusCheck()) {
            checkProvider();
        }
    }

    private boolean statusCheck() {
        boolean value = false;
        final LocationManager manager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            value = false;
            buildAlertMessageNoGps();
        } else {
            value = true;
        }

        return value;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                getResources().getString(R.string.gpsDisable))
                .setCancelable(false).setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        startActivityForResult(new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 2);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,
                                        final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface s) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
        alert.show();
    }

    private void checkProvider() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getPermission();
        } else {
            checkForLocationManager();
        }
    }

    private void checkForLocationManager() {
        locationManager = (LocationManager) getSystemService(
                Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {
            if (!provider.contains("gps")) { // if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                sendBroadcast(poke);
            }
            // Get the location from the given provider
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 500, 0, (LocationListener) this);

            if (location != null) {
                onLocationChanged(location);
            } else {
                location = locationManager.getLastKnownLocation(provider);
            }

            if (location != null) {
                onLocationChanged(location);
            } else {
//                Toast.makeText(getBaseContext(), "Location can't be retrieved",
//                        Toast.LENGTH_SHORT).show();
            }

        } else {
//            Toast.makeText(getBaseContext(), "No Provider Found",
//                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (!isUpdateSearch){
            isUpdateSearch = true;
            currentLat = location.getLatitude();
            currentLong = location.getLongitude();
            updateLocation(currentLat, currentLong);
        }
    }

    private String getAddress(double currentLat, double currentLong) {
        String addressData = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(currentLat, currentLong, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();

            if (!TextUtils.isEmpty(address)) {
                addressData = address + "";
                binding.txtLocation.setText(addressData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressData;
    }

    private void updateLocation(double currentLat, double currentLong) {
        if (currentLat != 0 && currentLong != 0 && mMap != null) {
            mMap.clear();
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            LatLng latLong = new LatLng(currentLat, currentLong);
            mMap.addMarker(new MarkerOptions().position(latLong).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
            getAddress(currentLat,currentLong);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private void jumpToSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent,4);
    }

    private void permissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(ContextCompat.getDrawable(activity, R.mipmap.ic_launcher));
        builder.setTitle(getResources().getString(R.string.permissionAccess));
        builder.setMessage(getResources().getString(R.string.permissionAllow));
        builder.setCancelable(false);
        builder.setPositiveButton(
                getResources().getString(R.string.allow),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        jumpToSetting();
                    }
                });
        builder.setNegativeButton(
                getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface s) {
                alert11.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
        alert11.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            getPermission();
        }else if (requestCode == 4) {
            getPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkProvider();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    permissionDialog();
                } else {
                    getPermission();
                }
                return;
            }
        }
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION);
    }
}