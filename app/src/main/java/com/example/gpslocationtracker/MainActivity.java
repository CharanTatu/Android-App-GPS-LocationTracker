package com.example.gpslocationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final Object PRIORITY_HIGH_ACCURACY = 1;
    TextView village, textcity, pincode,textnames;
    ProgressBar progressBar;
    Button button;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        village = findViewById(R.id.village);
        textcity = findViewById(R.id.city);
        pincode = findViewById(R.id.textPincode);
        progressBar = findViewById(R.id.probar);
        button = findViewById(R.id.buton);
        textnames = findViewById(R.id.textname);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getlocation();
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }

            }
        });







        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                     ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                             Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_LOCATION_PERMISSION);
                 }else{
                     getcurruntlocation();
                 }
            }
        });*/
    }

    /*
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getcurruntlocation();
                }else {
                    Toast.makeText(MainActivity.this, "Permission required", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private  void getcurruntlocation(){
            progressBar.setVisibility(View.VISIBLE);
            Log.e("Tag","done");
           *//* LocationRequest locationRequest = new LocationRequest()
                .setInterval(1000)
                .setFastestInterval(3000)
                .setPriority(PRIORITY_HIGH_ACCURACY);*//*

    }*/
    private void getlocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                      Location location = task.getResult();
                      if(location != null)
                      {
                          Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                          try {
                              List<Address> addresses = geocoder.getFromLocation(
                                      location.getLatitude(),location.getLongitude(),1
                              );
                              village.setText(Html.fromHtml("<front color = '#6200EE'><b>Latitude:</b><br></front>"
                                      + addresses.get(0).getLatitude()
                              ));
                              textcity.setText(Html.fromHtml("<front color = '#6200EE'><b>Longitude:</b><br></front>"
                                      + addresses.get(0).getLongitude()
                              ));
                              pincode.setText(Html.fromHtml("<front color = '#6200EE'><b>Address:</b><br></front>"
                                      + addresses.get(0).getAddressLine(0)
                              ));
                              textnames.setText(Html.fromHtml("<front color = '#6200EE'><b>City:</b><br></front>"+addresses.get(0).getLocality()));
                              Log.d("tag",village+" "+textcity+" " +pincode);
                              progressBar.setVisibility(View.GONE);
                              System.out.println("User Location: "+ addresses);
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      }
            }
        });
    }


}