package customer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bit.bookcab.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import gettersetter.RideRequest;

public class RideDetailsActivity extends AppCompatActivity  implements OnMapReadyCallback, PaymentResultListener {
    TextView tvReqName, tvSeatNumber, tvDate, tvTime;
    TextView edtReqName, edtSeatNumber, edtTime;
    RadioGroup radioGroupAc;
    RadioButton radioAc, radioNonAc;
    private GoogleMap mMap;
    private RideRequest rideRequest;
    TextView txtname,email,phone;
    LinearLayout profile;
    Button btnpaymetn;
    EditText edtpayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        // Initialize widgets
        profile=findViewById(R.id.middle);
        edtpayment=findViewById(R.id.edtpayment);
        tvReqName = findViewById(R.id.edt_req_name);
        tvSeatNumber = findViewById(R.id.edt_seat_number);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.edt_time);
        edtReqName = findViewById(R.id.edt_req_name);
        edtSeatNumber = findViewById(R.id.edt_seat_number);
        edtTime = findViewById(R.id.edt_time);
        radioGroupAc = findViewById(R.id.radio_group_ac);
        radioAc = findViewById(R.id.radio_ac);
        radioNonAc = findViewById(R.id.radio_non_ac);
        txtname=findViewById ( R.id.txtname );
        email=findViewById ( R.id.email );
        phone=findViewById ( R.id.phone );
        btnpaymetn=findViewById(R.id.btnpaymetn);

        btnpaymetn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount= edtpayment.getText().toString();
                startPayment(amount);
            }
        });

        // Get the tag value from the intent extras
        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapdetails);
        if (mapFragment != null) {
            mapFragment.getMapAsync(RideDetailsActivity.this);
        }
        try {
             rideRequest = (RideRequest) getIntent().getSerializableExtra("rideRequest");

        }catch (Exception e){
            e.printStackTrace();
        }

//        String markerTag = getIntent().getStringExtra("markerTag");
//        getdata("RideReq/" + markerTag);
        // Now you can use markerTag as needed in this activity
//        btnSubmitRide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth auth=FirebaseAuth.getInstance();
//                rideRequest.setIsaccepted(true);
//                DatabaseReference rideRequestRef = FirebaseDatabase.getInstance().getReference("RideReq").child(rideRequest.getRid());
//                Map<String, Object> updates = new HashMap<>();
//                updates.put("isaccepted", true);
//                updates.put("cabproviderid",auth.getUid());
//
//                // Update the values in the database
//                rideRequestRef.updateChildren(updates)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                // Database update successful, show a toast message
//                                Toast.makeText(RideDetailsActivity.this, "Ride accepted successfully.", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Database update failed, show an error message
//                                Toast.makeText(RideDetailsActivity.this, "Failed to accept ride. Please try again.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//            }
//        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // updateUI();
        try {
            showData(rideRequest);
            if(rideRequest.getCabproviderid()!=null && !rideRequest.getCabproviderid().equals("")) {
                profile.setVisibility(View.VISIBLE);
                getdata("Users/"+rideRequest.getCabproviderid());
            }else{
                profile.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showData(RideRequest rideRequest){
        try {

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
    public void getdata(String url) {
        //System.out.println ( "-------------" + url );

        try {

            //
            FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance ();
            DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference ( url );
            mFirebaseDatabase.keepSynced ( true );
            mFirebaseDatabase.addListenerForSingleValueEvent ( new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> yourData = (HashMap<String, Object>) dataSnapshot.getValue ();

                    txtname.setText("Your CabProvider Name "+yourData.get ( "fullname").toString());
                    email.setText(yourData.get ( "email").toString());
                    phone.setText(yourData.get ( "mobile").toString());

                }

                @Override
                public void onCancelled(DatabaseError error) {


                }
            } );
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void startPayment(String amount) {

        Checkout checkout = new Checkout();
        int famount = Math.round(Float.parseFloat(amount) * 100);

        checkout.setImage(R.mipmap.ic_launcher);

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", R.string.app_name);
            options.put("description", "Payment for Anything");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", false);

            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");
            options.put("amount", famount);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "surajwaghmare978@gmail.com");
            preFill.put("contact", "00000000 ");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this,"Payment Success!"+s,Toast.LENGTH_SHORT).show();
               FirebaseAuth auth=FirebaseAuth.getInstance();
                rideRequest.setIspaid(true);
        rideRequest.setIscompleted(true);
                DatabaseReference rideRequestRef = FirebaseDatabase.getInstance().getReference("RideReq").child(rideRequest.getUid());
                Map<String, Object> updates = new HashMap<>();
                updates.put("ispaid", true);
                updates.put("iscompleted",true);

                // Update the values in the database
                rideRequestRef.updateChildren(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Database update successful, show a toast message
                                Toast.makeText(RideDetailsActivity.this, "Ride Completed successfully.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Database update failed, show an error message
                                Toast.makeText(RideDetailsActivity.this, "Failed to accept ride. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this,"Payment error!"+s,Toast.LENGTH_SHORT).show();

    }
}
