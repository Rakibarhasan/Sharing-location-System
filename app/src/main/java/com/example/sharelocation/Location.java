package com.example.sharelocation;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tapadoo.alerter.Alerter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Location extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public static final int RequestPermissionCode = 1;
    protected GoogleApiClient googleApiClient;
    protected EditText longitudeText;
    protected EditText latitudeText;

    protected android.location.Location lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        longitudeText = (EditText) findViewById(R.id.lonText);
        latitudeText = (EditText) findViewById(R.id.latText);

        final EditText latText = (EditText) findViewById(R.id.latText);
        final EditText lonText = (EditText) findViewById(R.id.lonText);
        Button btnFind = (Button) findViewById(R.id.find);

        final ListView listResult = (ListView) findViewById(R.id.listResult);

        geocoder = new Geocoder(this);

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strLat = latText.getText().toString();
                String strLon = lonText.getText().toString();

                boolean parsable = true;
                Double lat = null, lon = null;

                try {
                    lat = Double.parseDouble(strLat);
                } catch (NumberFormatException ex) {
                    parsable = false;
                    View customView = Location.this.getLayoutInflater().inflate(R.layout.customtoast2,null);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setView(customView);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,0,200);
                    toast.show();
                }

                try {
                    lon = Double.parseDouble(strLon);
                } catch (NumberFormatException ex) {
                    parsable = false;
                    Alerter.create(Location.this)
                            .setTitle("GPS Connection for wait ")
                            .setText("Searching for Location")
                            .setIcon(R.drawable.ic_sad_24dp)
                            .setBackgroundColor(R.color.Update)
                            .show();
                }

                if (parsable) {
                    Alerter.create(Location.this)
                            .setTitle("New Update")
                            .setText("check the find lan and lon changing")
                            .setIcon(R.drawable.wifi)
                            .setBackgroundColor(R.color.new_update)
                            .show();
                    Toast.makeText(Location.this, "find " + lat + " : " + lon, Toast.LENGTH_LONG).show();


                    List <Address> geoResult = findGeocoder(lat, lon);
                    if (geoResult != null) {
                        List <String> geoStringResult = new ArrayList <String>();
                        for (int i = 0; i < geoResult.size(); i++) {
                            Address thisAddress = geoResult.get(i);
                            String stringThisAddress = "";
                            for (int a = 0; a < thisAddress.getMaxAddressLineIndex(); a++) {
                                stringThisAddress += thisAddress.getAddressLine(a) + "\n";
                            }

                            stringThisAddress +=
                                    "CountryName: " + thisAddress.getCountryName() + "\n"
                                            + "AdminArea: " + thisAddress.getAdminArea() + "\n"
                                            + "FeatureName: " + thisAddress.getFeatureName();
                            geoStringResult.add(stringThisAddress);
                        }

                        ArrayAdapter <String> adapter = new ArrayAdapter <String>(Location.this,
                                android.R.layout.simple_list_item_1, android.R.id.text1, geoStringResult);

                        listResult.setAdapter(adapter);
                    }

                }

            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                        @Override
                        public void onSuccess(android.location.Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitudeText.setText(String.valueOf(location.getLatitude()));
                                longitudeText.setText(String.valueOf(location.getLongitude()));

                            }
                        }
                    });
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Location.this, new
                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("MainActivity", "Connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("MainActivity", "Connection suspendedd");
    }

    private List <Address> findGeocoder(Double lat, Double lon) {
        final int maxResults = 5;
        List <Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, maxResults);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return addresses;

    }
}
