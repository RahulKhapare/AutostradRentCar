package com.example.fastuae.activity;

import android.Manifest;
import android.app.Activity;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Session;
import com.example.fastuae.R;
import com.example.fastuae.adapter.LocationAdapter;
import com.example.fastuae.databinding.ActivitySelectLocationBinding;
import com.example.fastuae.util.Click;
import com.example.fastuae.util.Config;
import com.example.fastuae.util.P;
import com.example.fastuae.util.WindowView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectLocationActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback{

    private SelectLocationActivity activity = this;
    private ActivitySelectLocationBinding binding;

    private LocationManager locationManager;
    private String provider;
    private int REQUEST_LOCATION = 1;
    double currentLat = 0;
    double currentLong = 0;
    private GoogleMap mMap;
    private String checkingAddress;
    private String setLocation;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;
    private boolean isUpdateSearch;

    private LocationAdapter locationAdapter;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_location);

        isUpdateSearch = false;
        geocoder = new Geocoder(this, Locale.getDefault());
        checkingAddress = getResources().getString(R.string.getLOcation);
        setLocation = getResources().getString(R.string.conformLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrame);
        mapFragment.getMapAsync(this);
        checkGPS();

        binding.btnLocation.setText(checkingAddress);
        onClick();

    }

    private void onClick(){

        binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.btnLocation.getText().toString().equals(checkingAddress)) {
                    checkGPS();
                } else {
                    Config.FROM_MAP = true;
                    finish();
                }
            }
        });

        binding.imgCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                isUpdateSearch = false;
                checkGPS();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                finish();
            }
        });

        binding.cardPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.imgArrowDown.getVisibility()==View.VISIBLE){
                    binding.imgArrowDown.setVisibility(View.GONE);
                    binding.imgArrowUp.setVisibility(View.VISIBLE);
                    binding.lnrSearchLocation.setVisibility(View.VISIBLE);
                    binding.etxLocation.setText("");
                }else if (binding.imgArrowUp.getVisibility()==View.VISIBLE){
                    binding.imgArrowUp.setVisibility(View.GONE);
                    binding.imgArrowDown.setVisibility(View.VISIBLE);
                    binding.lnrSearchLocation.setVisibility(View.GONE);
                    binding.etxLocation.setText("");
                    hideKeyboard(activity);

                }
            }
        });

        binding.txtSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (!TextUtils.isEmpty(binding.etxLocation.getText().toString().trim())){
                    binding.imgArrowUp.setVisibility(View.GONE);
                    binding.imgArrowDown.setVisibility(View.VISIBLE);
                    binding.lnrSearchLocation.setVisibility(View.GONE);
                    getLocation(binding.etxLocation.getText().toString());
                hideKeyboard(activity);
                }else {
                    H.showMessage(activity,getResources().getString(R.string.enterSearchLocation));
                }

            }
        });

    }

    private void getLocation(String addressData){
        isUpdateSearch = true;
        binding.etxLocation.setText("");
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(addressData, 1);
            for (int i = 0; i < addressList.size(); i++) {
                Address address = addressList.get(0);
                currentLat = address.getLatitude();
                currentLong = address.getLongitude();
                updateLocation(currentLat, currentLong);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUserAddress(){
        Session session = new Session(activity);
        session.addString(P.googleAddress,address);
        session.addString(P.googleCity,city);
        session.addString(P.googleState,state);
        session.addString(P.googleCountry,country);
        session.addString(P.googleCode,postalCode);
        session.addString(P.googleKnownName,knownName);
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
            }
//            if (!TextUtils.isEmpty(city)){
//                addressData = addressData + city + ",";
//            }
//            if (!TextUtils.isEmpty(state)){
//                addressData = addressData + state + ",";
//            }
//            if (!TextUtils.isEmpty(country)){
//                addressData = addressData + country;
//            }

            setUserAddress();

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
            binding.txtAddress.setText(getAddress(currentLat,currentLong));
            binding.btnLocation.setText(setLocation);
            new Session(activity).addString(P.locationAddress, binding.txtAddress.getText().toString());
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
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

    private void hideKeyboard(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.etxLocation.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}