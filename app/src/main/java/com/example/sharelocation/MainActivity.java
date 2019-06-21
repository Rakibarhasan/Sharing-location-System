package com.example.sharelocation;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

public class MainActivity extends AppCompatActivity {
    ImageView bgapp, clover;
    LinearLayout textsplash, textsplash1, texthome, texthome1, btc, menus;
    Animation frombottom;

//background

    Button btnShowNotification;

    //slide leftright

    float x1, x2, y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);


        bgapp = (ImageView) findViewById(R.id.bgapp);
        clover = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome);
        menus = (LinearLayout) findViewById(R.id.menus);
        textsplash1 = (LinearLayout) findViewById(R.id.textsplash1);
        btc = (LinearLayout) findViewById(R.id.btc);
        texthome1 = (LinearLayout) findViewById(R.id.texthome1);


        bgapp.animate().translationY(-3000).setDuration(900).setStartDelay(900);
        clover.animate().alpha(0).setDuration(800).setStartDelay(1100);
        textsplash.animate().translationY(30).alpha(0).setDuration(1200).setStartDelay(1200);
        textsplash1.animate().translationY(30).alpha(0).setDuration(1200).setStartDelay(1200);


        texthome.startAnimation(frombottom);
        texthome1.startAnimation(frombottom);
        menus.startAnimation(frombottom);
        btc.startAnimation(frombottom);


        ImageView imageView = (ImageView) findViewById(R.id.imagesms);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SMSS.class);
                startActivity(intent);
            }
        });

        ImageView extrapage = (ImageView) findViewById(R.id.extrapage);
        extrapage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExtraPage.class);
                startActivity(intent);
            }
        });

        Button GPS = (Button) findViewById(R.id.GPSbtn);
        GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GPS_on_Off.class);
                startActivity(intent);
            }
        });



        ImageView image1 = (ImageView) findViewById(R.id.image1);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Location.class);
                startActivity(intent);
            }
        });
        ImageView next = (ImageView) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });

        ImageView contact = (ImageView) findViewById(R.id.contactX);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Savecontact.class);
                startActivity(intent);
            }
        });

        //background


        btnShowNotification = (Button)findViewById(R.id.btndemo);
        btnShowNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assign big picture notification
                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.notific)).build();

                // Gets an instance of the NotificationManager service
                NotificationManager notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                //set intents and pending intents to call activity on click of "show activity" action button of notification
                Intent resultIntent = new Intent(MainActivity.this,Location.class);
                Intent resultIntent2 = new Intent(MainActivity.this,Main2Activity.class);
                Intent resultIntent3 = new Intent(MainActivity.this,MainActivity.class);



                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                resultIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                resultIntent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);



                PendingIntent piResult2 = PendingIntent.getActivity(MainActivity.this, (int) Calendar.getInstance().getTimeInMillis(), resultIntent2, 0);

                PendingIntent piResult = PendingIntent.getActivity(MainActivity.this, (int) Calendar.getInstance().getTimeInMillis(), resultIntent, 0);

                PendingIntent piResult3 = PendingIntent.getActivity(MainActivity.this, (int) Calendar.getInstance().getTimeInMillis(), resultIntent3, 0);

                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(MainActivity.this)
                                .setSmallIcon(R.drawable.ic_android_black_24dp)
                                .setContentTitle("Share Location")
                                .setContentText("Choose your Option Selected")
                                .setStyle(bigPictureStyle)
                                .addAction(R.drawable.ic_android_black_24dp, "Location", piResult)
                                .addAction(R.drawable.ic_android_black_24dp, "Sharing", piResult2)
                                .addAction(R.drawable.ic_android_black_24dp, "Home", piResult3)

                                .addAction(R.drawable.ic_android_black_24dp, "", PendingIntent.getActivity(getApplicationContext(), 0,getIntent(), 0, null));

                //to post your notification to the notification bar
                notificationManager.notify(0, builder.build());
            }
        });
    }
//slide leftright

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 < x2){
                    Intent i = new Intent(MainActivity.this, about.class);
                    startActivity(i);

                }else if(x1 > x2){
                    Intent i = new Intent(MainActivity.this, ExtraPage.class);
                    startActivity(i);
                }
                else if(x1 > x2){
                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                }


                break;
        }
        return false;
    }



}



