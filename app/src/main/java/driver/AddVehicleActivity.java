package driver;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bit.bookcab.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gettersetter.VehicleGtSt;

public class AddVehicleActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener{
    // Declare EditText fields
    private EditText edtMake, edtModel, edtSeats, edtMileage, edtRatePerKm, edtYearManufacture;
    private GoogleMap mMap;
    LatLng finallatLng;


    // Declare RadioButtons and RadioGroups
    private RadioButton radioPetrol, radioDiesel, radioElectric, radioAC, radioNonAC;

    // Declare Submit Button
    private Button btnSave;
    private RadioGroup radioGroupFuelType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addvehicle);


        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.providervehicle);
        mapFragment.getMapAsync(this);
        // Initialize EditText fields
        edtMake = findViewById(R.id.edt_make);
        edtModel = findViewById(R.id.edt_model);
        edtSeats = findViewById(R.id.edt_seats);
        edtMileage = findViewById(R.id.edt_mileage);
        edtRatePerKm = findViewById(R.id.edt_rate_per_km);
        edtYearManufacture = findViewById(R.id.edt_year_manufacture);

        // Initialize RadioButtons and RadioGroups for fuel type
        radioPetrol = findViewById(R.id.radio_petrol);
        radioDiesel = findViewById(R.id.radio_diesel);
        radioElectric = findViewById(R.id.radio_electric);

        // Initialize RadioButtons and RadioGroups for AC type
        radioAC = findViewById(R.id.radio_ac);
        radioNonAC = findViewById(R.id.radio_non_ac);
        radioGroupFuelType = findViewById(R.id.radio_group_fuel_type);
        // Initialize Submit Button
        btnSave = findViewById(R.id.btn_submit);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set values from EditText fields to the object
                VehicleGtSt vehicle = new VehicleGtSt();
                vehicle.setMake(edtMake.getText().toString());
                vehicle.setModel(edtModel.getText().toString());
                vehicle.setSeatno(edtSeats.getText().toString());
                vehicle.setMilage(edtMileage.getText().toString());
                vehicle.setRate(edtRatePerKm.getText().toString());
                vehicle.setYearmanufcture(edtYearManufacture.getText().toString());
vehicle.setVlat(finallatLng.latitude);
                vehicle.setVlong(finallatLng.longitude);
                // Determine selected fuel type and set it to the object
                int selectedFuelId = radioGroupFuelType.getCheckedRadioButtonId();
                RadioButton selectedFuel = findViewById(selectedFuelId);
                vehicle.setFuel(selectedFuel.getText().toString());

                // Determine if AC is selected and set it to the object
                boolean isAC = radioAC.isChecked();
                vehicle.setAc(isAC);
                saveDataToFirebase(vehicle);

//                FirebaseAuth auth=FirebaseAuth.getInstance();
//                DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
//                String vid=mdatabase.push().getKey();
//                mdatabase.child( "Vehicles" ).child( auth.getUid() ).child(vid).setValue( vehicle );
            }
        });
    }

    // Method to save data to Firebase
    private void saveDataToFirebase(VehicleGtSt vehicle) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        String vid = mdatabase.push().getKey();
        mdatabase.child("Vehicles").child(auth.getUid()).child(vid).setValue(vehicle)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data successfully added to Firebase, close the activity
                            Toast.makeText(AddVehicleActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity
                        } else {
                            // Failed to add data to Firebase
                            Toast.makeText(AddVehicleActivity.this, "Failed to add data to database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener( AddVehicleActivity.this);
// Add a marker in Sydney and move the camera

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        finallatLng=latLng;
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));

        // Show a Toast message with the latitude and longitude
        String message = "Latitude: " + latLng.latitude + ", Longitude: " + latLng.longitude;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
