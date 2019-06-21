package com.example.sharelocation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ExtraPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_page);




        Button language=(Button) findViewById(R.id.language);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraPage.this,Main3Activity.class);
                startActivity(intent);
            }
        });


        Button abuting=(Button) findViewById(R.id.abut);
        abuting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraPage.this,about.class);
                startActivity(intent);
            }
        });

        Button manupage=(Button) findViewById(R.id.menupage);
        manupage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraPage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Button inter=(Button)findViewById(R.id.internet);
        inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ExtraPage.this, Internetcheck.class);
                startActivity(intent);

            }
        });



        Button user=(Button) findViewById(R.id.user);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraPage.this,gallery.class);
                startActivity(intent);
            }
        });

        Button about=(Button) findViewById(R.id.about);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraPage.this,OutdorLocation.class);
                startActivity(intent);
            }
        });

        Button button15=(Button) findViewById(R.id.button15);

        button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraPage.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Button button5=(Button) findViewById(R.id.button5);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraPage.this,Check_netConnection.class);
                startActivity(intent);
            }
        });


    }
    //map
    public void onMapBtnClick(View view) {

        // Case-1
        // To open google map app
        // String uri = "http://maps.google.com/maps?q=";

        // Case-2
        // To open google map with specific location
        //  packageName + latitude + longitude + place title
        // String uri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + mTitle + ")";
        String geoUri = "http://maps.google.com/maps?q=loc:" + 23.17024 + "," + 90.831061 + " (" + ")";

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        this.startActivity(mapIntent);

    }

}
