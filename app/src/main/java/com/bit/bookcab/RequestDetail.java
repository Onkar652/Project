package com.bit.bookcab;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import gettersetter.RideRequest;
import gettersetter.VehicleGtSt;

public class RequestDetail extends AppCompatActivity  implements OnMapReadyCallback {
    TextView tvReqName, tvSeatNumber, tvDate, tvTime;
    TextView edtReqName, edtSeatNumber, edtTime;
    RadioGroup radioGroupAc;
    RadioButton radioAc, radioNonAc;
    Button btnSubmitRide,btnstartpoinnt;
    private GoogleMap mMap;
    private RideRequest rideRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_req_details);
        // Initialize widgets
        tvReqName = findViewById(R.id.edt_req_name);
        tvSeatNumber = findViewById(R.id.edt_seat_number);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.edt_time);
        edtReqName = findViewById(R.id.edt_req_name);
        edtSeatNumber = findViewById(R.id.edt_seat_number);
        edtTime = findViewById(R.id.edt_time);
        btnstartpoinnt=findViewById(R.id.btnstartpoinnt);
        radioGroupAc = findViewById(R.id.radio_group_ac);
        radioAc = findViewById(R.id.radio_ac);
        radioNonAc = findViewById(R.id.radio_non_ac);
        btnSubmitRide = findViewById(R.id.btn_accept_ride);
        // Get the tag value from the intent extras
        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapdetails);
        if (mapFragment != null) {
            mapFragment.getMapAsync(RequestDetail.this);
        }
        String markerTag = getIntent().getStringExtra("markerTag");
        getdata("RideReq/" + markerTag);
        // Now you can use markerTag as needed in this activity
        btnSubmitRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                rideRequest.setIsaccepted(true);
                DatabaseReference rideRequestRef = FirebaseDatabase.getInstance().getReference("RideReq").child(rideRequest.getRid());
                Map<String, Object> updates = new HashMap<>();
                updates.put("isaccepted", true);
                updates.put("cabproviderid",auth.getUid());

                // Update the values in the database
                rideRequestRef.updateChildren(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Database update successful, show a toast message
                                Toast.makeText(RequestDetail.this, "Ride accepted successfully.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Database update failed, show an error message
                                Toast.makeText(RequestDetail.this, "Failed to accept ride. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        btnstartpoinnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a Uri with the start point coordinates
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + rideRequest.getFromlatitude() + "," + rideRequest.getFromlongitude());

                // Create an intent with the ACTION_VIEW action and the Uri
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Set the package to Google Maps, so it opens in Google Maps app
                mapIntent.setPackage("com.google.android.apps.maps");

                // Verify that the intent will resolve to an activity
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    // Start the intent
                    startActivity(mapIntent);
                } else {
                    // Handle case where Google Maps is not installed
                    Toast.makeText(RequestDetail.this, "Google Maps app is not installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // updateUI();

    }
    public void getdata(String url) {

        try {

            FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
            DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference(url);
            mFirebaseDatabase.keepSynced(true);
            mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                         rideRequest = new RideRequest();
                        HashMap<String, Object> yourData = (HashMap<String, Object>) dataSnapshot.getValue();
                        rideRequest.setFromlatitude((double) yourData.get("fromlatitude"));
                        rideRequest.setFromlongitude((double) yourData.get("fromlongitude"));
                        rideRequest.setTolatitude((double) yourData.get("tolatitude"));
                        rideRequest.setTolongitude((double) yourData.get("tolongitude"));
                        rideRequest.setSeatNumber(Integer.parseInt(yourData.get("seatNumber").toString()));
                        rideRequest.setDate(yourData.get("date").toString());
                        rideRequest.setTime(yourData.get("time").toString());
                        rideRequest.setAc((boolean) yourData.get("ac"));
                        rideRequest.setIsaccepted((boolean) yourData.get("isaccepted"));
                        rideRequest.setCabproviderid(yourData.get("cabproviderid").toString());
                        rideRequest.setReqestername(yourData.get("reqestername").toString());
                        rideRequest.setReqid(yourData.get("reqid").toString());
rideRequest.setRid(dataSnapshot.getKey());
                        tvReqName.setText(rideRequest.getReqestername());
                        tvSeatNumber.setText(String.valueOf(rideRequest.getSeatNumber()));
                        tvDate.setText(rideRequest.getDate());
                        tvTime.setText(rideRequest.getTime());
                        edtReqName.setText(rideRequest.getReqestername());
                        edtSeatNumber.setText(String.valueOf(rideRequest.getSeatNumber()));
                        edtTime.setText(rideRequest.getTime());
                        radioAc.setChecked(rideRequest.isAc());
                        radioNonAc.setChecked(!rideRequest.isAc());

                        try{
                            // Update map with start and end points
                            if (mMap != null) {
                                LatLng startPoint = new LatLng(rideRequest.getFromlatitude(), rideRequest.getFromlongitude());
                                LatLng endPoint = new LatLng(rideRequest.getTolatitude(), rideRequest.getTolongitude());

                                // Add marker for start point with green color
                                mMap.addMarker(new MarkerOptions()
                                        .position(startPoint)
                                        .title("Start Point")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                // Add marker for end point with red color
                                mMap.addMarker(new MarkerOptions()
                                        .position(endPoint)
                                        .title("End Point")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                // Draw polyline between start and end points
                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .add(startPoint, endPoint)
                                        .width(5)
                                        .color(Color.BLUE);
                                mMap.addPolyline(polylineOptions);

                                // Zoom camera to fit both markers and polyline
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(startPoint);
                                builder.include(endPoint);
                                LatLngBounds bounds = builder.build();
                                int padding = 100; // Padding around start and end points
                                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
