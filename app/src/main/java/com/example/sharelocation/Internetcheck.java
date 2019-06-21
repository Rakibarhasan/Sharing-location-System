package com.example.sharelocation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Internetcheck extends AppCompatActivity {

    ImageView mConStatusIv;
    TextView mConStatusTv;
    Button mConStatusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internetcheck);

        mConStatusIv = findViewById(R.id.conStatusIv);
        mConStatusTv = findViewById(R.id.conStatusTv);
        mConStatusBtn = findViewById(R.id.conStatusBtn);

        Button cancel=(Button)findViewById(R.id.canx);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Internetcheck.this,ExtraPage.class);
                startActivity(intent);

            }
        });

        //button click to check network status
        mConStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //function call to check network connection status
                checkNetworkConnectionStatus();
            }
        });

    }

    private void checkNetworkConnectionStatus() {
        boolean wifiConnected;
        boolean mobileConnected;
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()){ //connected with either mobile or wifi
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            if (wifiConnected){ //wifi connected
                mConStatusIv.setImageResource(R.drawable.ic_signal_wifi_3_bar_black_24dp);
                mConStatusTv.setText("Connected Wifi");
            }
            else if (mobileConnected){ //mobile data connected
                mConStatusIv.setImageResource(R.drawable.ic_signal_cellular_3_bar_black_24dp);
                mConStatusTv.setText("Mobile Data");
            }
        }
        else { //no internet connection
            mConStatusIv.setImageResource(R.drawable.ic_do_not_disturb_alt_black_24dp);
            mConStatusTv.setText("No internet");
        }
    }
}




