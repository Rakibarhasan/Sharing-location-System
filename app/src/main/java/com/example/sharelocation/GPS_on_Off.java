package com.example.sharelocation;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GPS_on_Off extends AppCompatActivity {
    Button button;
    Context context;
    Intent intent1;
    TextView textview;
    LocationManager locationManager ;
    boolean GpsStatus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_on__off);

        button = (Button)findViewById(R.id.button1);
        textview = (TextView)findViewById(R.id.textView1);
        context = getApplicationContext();


        Button clear2=(Button) findViewById(R.id.cancel2);
        clear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GPS_on_Off.this,MainActivity.class);
                startActivity(intent);
            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSStatus();

                if(GpsStatus == true)
                {
                    textview.setText("Location Services Is Enabled   On Device");
                }else
                {textview.setText("Location Services Is Disabled  Off Device");}

                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent1);
            }
        });
    }

    public void GPSStatus(){
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}