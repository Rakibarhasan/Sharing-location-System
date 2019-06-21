package com.example.sharelocation;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tapadoo.alerter.Alerter;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_LOCATION = 1;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 2;
    Button button;
    Button sending;

    LocationManager locationManager;
    String lattitude,longitude;

    EditText text,phone;
    Button sendBtn,btns;
    Button button2;
    //savecontact
    EditText id,name;
    Button view;
    TextView texts;
    DBHandler db;


    private final String SENT = "SMS_SENT";
    private final String DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;

    //contact show
    public static final String TAG = "FakePanicButton";
    private static final int CONTACT_PICKER_RESULT = 0x00;
    private static final int CONNECT_RESULT = 0x01;
    private EditText Phone;
    private String displayName;
    private String phoneNumber;
    private final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        text = findViewById(R.id.text_location);
        btns = findViewById(R.id.btns);
        sendBtn = findViewById(R.id.sending);
        phone = findViewById(R.id.Phone);
        button2 = findViewById(R.id.button2);


        //save number frist part

        id=(EditText)findViewById(R.id.id);
        view=(Button)findViewById(R.id.view);
        texts=(TextView)findViewById(R.id.text);
        db=new DBHandler(getApplicationContext());

        //show the contact number first part

        Phone = (EditText) findViewById(R.id.Phone);
        Button chooseContactButton = (Button) findViewById(R.id.chooseContactButton);





        button2.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (Intent.ACTION_SEND);
                intent.setData(Uri.parse("smsto:"+phone.getText().toString()));
                intent.putExtra("sms_body",text.getText().toString());
                startActivity (intent.createChooser (intent,"share"));

            }
        });

        //save number show







        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        button = findViewById(R.id.btns);
        button.setOnClickListener(this);
        sentPI = PendingIntent.getBroadcast(Main2Activity.this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(Main2Activity.this, 0, new Intent(DELIVERED), 0);

        sentPI = PendingIntent.getBroadcast(Main2Activity.this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(Main2Activity.this, 0, new Intent(DELIVERED), 0);



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = text.getText().toString();
                String telNr = phone.getText().toString();

                if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Main2Activity.this, new String [] {Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
                else
                {
                    SmsManager sms = SmsManager.getDefault();

                    //phone - Recipient's phone number
                    //address - Service Center Address (null for default)
                    //message - SMS message to be sent
                    //piSent - Pending intent to be invoked when the message is sent
                    //piDelivered - Pending intent to be invoked when the message is delivered to the recipient
                    sms.sendTextMessage(telNr, null, message, sentPI, deliveredPI);
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //The deliveredPI PendingIntent does not fire in the Android emulator.
        //You have to test the application on a real device to view it.
        //However, the sentPI PendingIntent works on both, the emulator as well as on a real device.

        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        View customView = Main2Activity.this.getLayoutInflater().inflate(R.layout.customtoast,null);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setView(customView);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,0,200);
                        toast.show();
                        break;

                    //Something went wrong and there's no way to tell what, why or how.
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Alerter.create(Main2Activity.this)
                                .setTitle("Generic problem")
                                .setText("Check phone number and blance with one phone number use ")
                                .setBackgroundColor(R.color.colorAccent)
                                .show();
                        break;

                    //Your device simply has no cell reception. You're probably in the middle of
                    //nowhere, somewhere inside, underground, or up in space.
                    //Certainly away from any cell phone tower.
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service!", Toast.LENGTH_SHORT).show();
                        break;

                    //Something went wrong in the SMS stack, while doing something with a protocol
                    //description unit (PDU) (most likely putting it together for transmission).
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;

                    //You switched your device into airplane mode, which tells your device exactly
                    //"turn all radios off" (cell, wifi, Bluetooth, NFC, ...).
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off!", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };

        smsDeliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch(getResultCode())
                {
                    case Activity.RESULT_OK:
                        View customView = Main2Activity.this.getLayoutInflater().inflate(R.layout.customtoast1,null);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setView(customView);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,0,200);
                        toast.show();
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        //register the BroadCastReceivers to listen for a specific broadcast
        //if they "hear" that broadcast, it will activate their onReceive() method
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
    }















//location  signal

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
        if (ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (Main2Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                text.setText("Here is my current location save it ==>>http://maps.google.com/maps?q="+  lattitude+","
                        + longitude+"&iwloc=A");
                Alerter.create(Main2Activity.this)
                        .setTitle("Location successfully ")
                        .setText("Saved Location")
                        .setIcon(R.drawable.ic_my_location_black_24dp)
                        .setBackgroundColor(R.color.slide_2)
                        .show();


            } else  if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                text.setText("Here is my current location save it ==>>http://maps.google.com/maps?q="+  lattitude+","
                        + longitude+"&iwloc=A");
                Alerter.create(Main2Activity.this)
                        .setTitle("Location successfully ")
                        .setText("Saved Location")
                        .setIcon(R.drawable.ic_my_location_black_24dp)
                        .setBackgroundColor(R.color.slide_1)
                        .show();



            } else  if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

                text.setText("Here is my current location save it ==>>http://maps.google.com/maps?q="+  lattitude+","
                        + longitude+"&iwloc=A");
                Alerter.create(Main2Activity.this)
                        .setTitle("Location successfully ")
                        .setText("Saved Location")
                        .setIcon(R.drawable.ic_my_location_black_24dp)
                        .setBackgroundColor(R.color.Orangetext)
                        .show();


            }else{

                Alerter.create(Main2Activity.this)
                        .setTitle("Please wait ")
                        .setText("Searching Location and Saved")
                        .setIcon(R.drawable.ic_my_location_black_24dp)
                        .setBackgroundColor(R.color.colorAccent)
                        .show();

            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }







    //save number show Last part
    public void buttonAction(View view) {
        switch (view.getId()) {
            case R.id.view:
                Phone.setText(db.getRecords());
                break;

        }

        //contact show 2nd part
        Button chooseContactButton = (Button) findViewById(R.id.chooseContactButton);
        chooseContactButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pickContacts ();

            }
        });

    }

    private void pickContacts ()
    {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else
        {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case CONTACT_PICKER_RESULT:
                if (data == null)
                    return;
                Uri uri = data.getData();
                String id = uri.getLastPathSegment();
                Log.i("", uri + "");

                String[] projection = {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        projection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
                        new String[] {
                                id,
                        }, null);

                if (cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Phone.setText(phoneNumber);


                }}}
}




