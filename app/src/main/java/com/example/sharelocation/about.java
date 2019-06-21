package com.example.sharelocation;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;

public class about extends AppCompatActivity implements
        FetchAddressTask.OnTaskCompleted {

    // Constants
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_PICK_PLACE = 2;
    private static final String TRACKING_LOCATION_KEY = "tracking_location";

    // Views
    private Button mLocationButton;
    private Button mPlacePickerButton;
    private TextView mLocationTextView;
    private ImageView mAndroidImageView;
    private EditText text,phone;

    // Location classes
    private boolean mTrackingLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private LocationCallback mLocationCallback;

    // Animation
    private AnimatorSet mRotateAnim;
    Button sendBtn,messanger;

    //slide left right

    float x1, x2, y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mLocationButton = (Button) findViewById(R.id.button_location);
        mPlacePickerButton = (Button) findViewById(R.id.button_place_picker);
        mLocationTextView = (TextView) findViewById(R.id.textview_location);
        mAndroidImageView = (ImageView) findViewById(R.id.imageview_android);
        text = (EditText) findViewById(R.id.textview_location2);
        phone = (EditText) findViewById(R.id.phone);

        //send btn
        sendBtn = (Button) findViewById(R.id.sendbtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("smsto:"+phone.getText().toString()));
                intent.putExtra("sms_body", text.getText().toString());
                startActivity(intent);


            }
        });

        //Messanger sending
        messanger = (Button) findViewById(R.id.messanger);
        messanger.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (Intent.ACTION_SEND);
                intent.setData(Uri.parse("smsto:"+phone.getText().toString()));
                intent.putExtra("sms_body",text.getText().toString());
                startActivity (intent.createChooser (intent,"share"));

            }
        });


        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(
                this);

        // Initialize the PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Set up the animation.
        mRotateAnim = (AnimatorSet) AnimatorInflater.loadAnimator
                (this, R.animator.rotate);
        mRotateAnim.setTarget(mAndroidImageView);

        // Restore the state if the activity is recreated.
        if (savedInstanceState != null) {
            mTrackingLocation = savedInstanceState.getBoolean(
                    TRACKING_LOCATION_KEY);
        }


        // Set the listener for the location button.
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Toggle the tracking state.
             * @param v The track location button.
             */
            @Override
            public void onClick(View v) {
                if (!mTrackingLocation) {
                    startTrackingLocation();
                } else {
                    stopTrackingLocation();
                }
            }
        });

        // Set the listener for the Place Picker button.
        mPlacePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder =
                        new PlacePicker.IntentBuilder();
                try {
                    // Launch the PlacePicker.
                    startActivityForResult(builder.build(about.this)
                            , REQUEST_PICK_PLACE);
                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        // Initialize the location callbacks.
        mLocationCallback = new LocationCallback() {
            /**
             * This is the callback that is triggered when the
             * FusedLocationClient updates your location.
             * @param locationResult The result containing the device location.
             */
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // If tracking is turned on, reverse geocode into an address
                if (mTrackingLocation) {
                    new FetchAddressTask(about.this, about.this)
                            .execute(locationResult.getLastLocation());
                }
            }
        };
    }

    /**
     * Starts tracking the device. Checks for
     * permissions, and requests them if they aren't present. If they are,
     * requests periodic location updates, sets a loading text and starts the
     * animation.
     */
    private void startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mTrackingLocation = true;
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(),
                            mLocationCallback,
                            null /* Looper */);

            // Set a loading text while you wait for the address to be
            // returned
            mLocationTextView.setText(getString(R.string.address_text,
                    getString(R.string.loading), // Name
                    getString(R.string.loading), // Address
                    new Date())); // Timestamp
            mLocationButton.setText(R.string.stop_tracking_location);

            text.setText(getString(R.string.address_text,
                    getString(R.string.loading), // Name
                    getString(R.string.loading), // Address
                    new Date())); // Timestamp
            mLocationButton.setText(R.string.stop_tracking_location);
            mRotateAnim.start();
        }
    }


    /**
     * Stops tracking the device. Removes the location
     * updates, stops the animation, and resets the UI.
     */
    private void stopTrackingLocation() {
        if (mTrackingLocation) {
            mTrackingLocation = false;
            mLocationButton.setText(R.string.start_tracking_location);
            mLocationTextView.setText(R.string.textview_hint);
            text.setText(R.string.textview_hint);
            mRotateAnim.end();
        }
    }


    /**
     * Sets up the location request.
     *
     * @return The LocationRequest object containing the desired parameters.
     */
    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }


    /**
     * Saves the last location on configuration change
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(TRACKING_LOCATION_KEY, mTrackingLocation);
        super.onSaveInstanceState(outState);
    }

    /**
     * Callback that is invoked when the user responds to the permissions
     * dialog.
     *
     * @param requestCode  Request code representing the permission request
     *                     issued by the app.
     * @param permissions  An array that contains the permissions that were
     *                     requested.
     * @param grantResults An array with the results of the request for each
     *                     permission requested.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:

                // If the permission is granted, get the location, otherwise,
                // show a Toast
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    startTrackingLocation();
                } else {
                    Toast.makeText(this,
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(final String result) throws SecurityException {
        if (mTrackingLocation) {
            // Get the place name
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull
                                                       Task<PlaceLikelihoodBufferResponse> task) {

                            // If a you get a result, get the most likely place and
                            // update the place name.
                            if (task.isSuccessful()) {
                                PlaceLikelihoodBufferResponse likelyPlaces =
                                        task.getResult();
                                float maxLikelihood = 0;
                                Place currentPlace = null;
                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    if (maxLikelihood < placeLikelihood.getLikelihood())
                                    {
                                        maxLikelihood = placeLikelihood.getLikelihood();
                                        currentPlace = placeLikelihood.getPlace();
                                    }
                                }

                                // Update the UI.
                                if (currentPlace != null) {
                                    mLocationTextView.setText(
                                            getString(R.string.address_text,
                                                    currentPlace.getName(), result,
                                                    System.currentTimeMillis()));
                                    setAndroidType(currentPlace);



                                        //Edit Text show

                                    text.setText(
                                            getString(R.string.address_text,
                                                    currentPlace.getName(), result,
                                                    System.currentTimeMillis()));
                                    setAndroidType(currentPlace);
                                }

                                // Close the buffer
                                likelyPlaces.release();

                                // Otherwise, show an error
                            } else {
                                mLocationTextView.setText(
                                        getString(R.string.address_text,
                                                getString(R.string.no_place),
                                                result, System.currentTimeMillis()));


                                //Edit text show
                                text.setText(
                                        getString(R.string.address_text,
                                                getString(R.string.no_place),
                                                result, System.currentTimeMillis()));
                            }

                        }
                    });
        }
    }

    private void setAndroidType(Place currentPlace) {
        int drawableID = -1;
        for (Integer placeType : currentPlace.getPlaceTypes()) {
            switch (placeType) {
                case Place.TYPE_SCHOOL:
                    drawableID = R.drawable.android_school;
                    break;
                case Place.TYPE_GYM:
                    drawableID = R.drawable.android_gym;
                    break;
                case Place.TYPE_RESTAURANT:
                    drawableID = R.drawable.android_restaurant;
                    break;
                case Place.TYPE_LIBRARY:
                    drawableID = R.drawable.android_library;
                    break;
            }
        }

        if (drawableID < 0) {
            drawableID = R.drawable.android_plain;
        }
        mAndroidImageView.setImageResource(drawableID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            setAndroidType(place);
            mLocationTextView.setText(
                    getString(R.string.address_text, place.getName(),
                            place.getAddress(), System.currentTimeMillis()));

        } else {
            mLocationTextView.setText(R.string.no_place);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        if (mTrackingLocation) {
            stopTrackingLocation();
            mTrackingLocation = true;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mTrackingLocation) {
            startTrackingLocation();
        }
        super.onResume();
    }


    // slide left right

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 > x2) {
                    Intent i = new Intent(about.this, MainActivity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }

}
