package com.example.sharelocation;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OutdorLocation extends AppCompatActivity {
    private int[] mImages = new int[]{

            R.drawable.ic_menu_black_24dp, R.drawable.ic_language_black_24dp
    };

    private String[] mImagesTitle = new String[]{
            "Rakib AR Hasan"
    };

    private static final int MY_PHONE_CALL_REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdor_location);

        Button facebook = (Button) findViewById(R.id.facebook);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = openFacebook(OutdorLocation.this);
                startActivity(facebookIntent);
            }
        });
    }
        public static Intent openFacebook(Context context) {

            try {
                context.getPackageManager()
                        .getPackageInfo("com.facebook.katana", 0);
                return new Intent(Intent.ACTION_VIEW,
                        Uri.parse("fb://page/100007858273468"));
            } catch (Exception e){

                return new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/rakibarhasan"));
            }


        }


        //email

    public void onEmailBtnClick(View view) {

        // Create the Intent
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);

        // Fill it with Data
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"rakibhasancity38@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Share Location user:");

        // Send it off to the Activity-Chooser
        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

    }







        }





