package com.example.sharelocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMSS extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_LOCATION = 1;
    Button button;

    LocationManager locationManager;
    String lattitude,longitude;

    EditText text,phone;
    Button sendBtn;
    Button btnXM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smss);


        text = (EditText) findViewById(R.id.text_location);
        sendBtn = (Button) findViewById(R.id.main_smsbtn);
        phone = (EditText) findViewById(R.id.Phone);
        button = (Button)findViewById(R.id.btns);

        Button button11=(Button) findViewById(R.id.button11);
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SMSS.this,MainActivity.class);
                startActivity(intent);
            }
        });





        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        button = (Button)findViewById(R.id.btns);
        button.setOnClickListener(this);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("smsto:"+phone.getText().toString()));
                intent.putExtra("sms_body",text.getText().toString());
                startActivity(intent);

            }
        });
    }

    @Override
    public void onClick(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(SMSS.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (SMSS.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SMSS.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                text.setText("Here is my current location save it ==>>http://maps.google.com/maps?q="+ lattitude+","
                        + longitude+"&iwloc=A");



            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);



                text.setText("Here is my current location save it ==>>http://maps.google.com/maps?q="+  lattitude+","
                        + longitude+"&iwloc=A" );

            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);


                text.setText("Here is my current location save it ==>>http://maps.google.com/maps?q="+ lattitude+","
                        + longitude+"&iwloc=A");




            }else{

                Toast.makeText(this,"Unble to Trace your location",Toast.LENGTH_SHORT).show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setMessage ("Please Turn ON your GPS Connection")
                .setCancelable (false)
                .setPositiveButton ("Yes", new DialogInterface.OnClickListener () {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity (new Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton ("No", new DialogInterface.OnClickListener () {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel ();
                    }
                });
        final AlertDialog alert = builder.create ();
        alert.show ();
    }


}
