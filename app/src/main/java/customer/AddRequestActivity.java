package customer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bit.bookcab.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import driver.AddVehicleActivity;
import gettersetter.RideRequest;
import gettersetter.VehicleGtSt;

public class AddRequestActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Declare all widgets
    private EditText edtFrom, edtTo, edtSeatNumber, edtTime,edt_req_name;
    private RadioGroup radioGroupAC;
    private TextView tvDate;
    private RadioButton radioAC, radioNonAC;
    private Button btnSubmitRide;
    private LatLng startLocation;
    private LatLng endLocation;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrequest);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapstart);
        mapFragment.getMapAsync(this);

        // Initialize widgets
//        edtFrom = findViewById(R.id.edt_from);
//        edtTo = findViewById(R.id.edt_to);
        edt_req_name=findViewById(R.id.edt_req_name);
        edtSeatNumber = findViewById(R.id.edt_seat_number);
        tvDate = findViewById(R.id.tv_date);
        edtTime = findViewById(R.id.edt_time);
        radioGroupAC = findViewById(R.id.radio_group_ac);
        radioAC = findViewById(R.id.radio_ac);
        radioNonAC = findViewById(R.id.radio_non_ac);
        btnSubmitRide = findViewById(R.id.btn_submit_ride);
        // Set click listener for the Date TextView
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        // Set click listener for the Submit Ride button
        btnSubmitRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if start and end locations are selected
                if (startLocation == null || endLocation == null) {
                    Toast.makeText(AddRequestActivity.this, "Please select start and end locations", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                RideRequest rideRequest = new RideRequest();
                rideRequest.setSeatNumber(Integer.parseInt(edtSeatNumber.getText().toString()));
                rideRequest.setDate(tvDate.getText().toString());
                rideRequest.setTime(edtTime.getText().toString());
                rideRequest.setFromlatitude(startLocation.latitude);
                rideRequest.setFromlongitude(startLocation.longitude);
                rideRequest.setReqestername(edt_req_name.getText().toString());
                rideRequest.setIsaccepted(false);
                rideRequest.setReqid(user.getUid());
                rideRequest.setCabproviderid("");
                rideRequest.setTolatitude(endLocation.latitude);
                rideRequest.setTolongitude(endLocation.longitude);
                rideRequest.setAc(radioAC.isChecked()); // Assuming AC is checked by default
                rideRequest.setIspaid(false);
                saveDataToFirebase(rideRequest);
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set long click listener for the map to select start and end locations
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (startLocation == null) {
                    startLocation = latLng;
                    mMap.addMarker(new MarkerOptions()
                            .position(startLocation)
                            .title("Start Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); // Green color for start point
                    Toast.makeText(AddRequestActivity.this, "Start Location selected ", Toast.LENGTH_SHORT).show();
                } else if (endLocation == null) {
                    endLocation = latLng;
                    mMap.addMarker(new MarkerOptions()
                            .position(endLocation)
                            .title("End Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); // Red color for end point
                    Toast.makeText(AddRequestActivity.this, "End Location selected ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Method to show DatePickerDialog
    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog and set the listener
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Set selected date to the TextView
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvDate.setText(selectedDate);
                    }
                },
                year, month, dayOfMonth
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
    private void saveDataToFirebase(RideRequest vehicle) {
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        String vid = mdatabase.push().getKey();
        vehicle.setUid(vid);
        mdatabase.child("RideReq").child(vid).setValue(vehicle)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data successfully added to Firebase, close the activity
                            Toast.makeText(AddRequestActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity
                        } else {
                            // Failed to add data to Firebase
                            Toast.makeText(AddRequestActivity.this, "Failed to add data to database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // Method to create a RideRequest object based on user input
//    private RideRequest createRideRequestObject() {
//        RideRequest rideRequest = new RideRequest();
//        rideRequest.setFrom(edtFrom.getText().toString());
//        rideRequest.setTo(edtTo.getText().toString());
//        rideRequest.setSeatNumber(Integer.parseInt(edtSeatNumber.getText().toString()));
//        rideRequest.setDate(tvDate.getText().toString());
//        rideRequest.setTime(edtTime.getText().toString());
//        rideRequest.setAc(radioAC.isChecked()); // Assuming AC is checked by default
//        return rideRequest;
//    }
}