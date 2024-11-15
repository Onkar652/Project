package customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.bookcab.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import driver.AddVehicleActivity;
import driver.VehiclesFragment;
import gettersetter.VehicleGtSt;

public class ProvidersMap extends Fragment  implements OnMapReadyCallback {
    View view;
    private GoogleMap mMap;
    LatLng latLng;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate ( R.layout.providersmap ,container,false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager ()
                .findFragmentById ( R.id.providermap );
        if ( mapFragment == null ) {
            mapFragment = SupportMapFragment.newInstance ();
            getChildFragmentManager ().beginTransaction ().replace ( R.id.providermap, mapFragment ).commit ();
        }
        mapFragment.getMapAsync ( this );

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnInfoWindowClickListener ( new GoogleMap.OnInfoWindowClickListener () {
            @Override
            public void onInfoWindowClick(Marker marker) {


                // Retrieve the tag value
//                String tagValue = (String) marker.getTag();
//                Toast.makeText (getActivity (), tagValue, Toast.LENGTH_SHORT ).show ();
//                // Open a new activity when the marker title is clicked
//                Intent intent = new Intent(getActivity(), PlaceDetailsActivity.class);
//
//                // Pass the tag value as an extra to the next activity
//                intent.putExtra("markerTag", tagValue);
//
//                startActivity(intent);
            }
        } );

        FirebaseAuth auth = FirebaseAuth.getInstance();
        getdata("Vehicles");;
    }

    public void getdata(String url) {

        try {

            ArrayList<VehicleGtSt> vehicleGtStArrayList=new ArrayList<>();
            // pb.setVisibility ( View.VISIBLE );
            //

            FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance ();
            DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference ( url );
            mFirebaseDatabase.keepSynced ( true );
            mFirebaseDatabase.addListenerForSingleValueEvent ( new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //System.out.println ( "is req----------------" + dataSnapshot.toString () );

                    for (DataSnapshot user : dataSnapshot.getChildren ()) {

                        for (DataSnapshot locationobj : user.getChildren ()) {

                            HashMap<String, Object> yourData = (HashMap<String, Object>) locationobj.getValue ();


                            double lati= (double) yourData.get ( "vlat");
                            double longitude= (double) yourData.get ( "vlong");
                            String lname= (String) yourData.get ( "make")+" "+(String) yourData.get ( "model");
                            latLng=new LatLng(lati,longitude);

                            LatLng location = new LatLng(lati, longitude); // Replace with the desired coordinates


                            // Load the vector drawable for the car icon
                            Drawable carDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.twotone_directions_car_24);

// Apply a random color filter to the drawable
                            Random random = new Random();
                            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                            carDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);

// Convert the drawable to a BitmapDescriptor
                            BitmapDescriptor carIcon = BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(carDrawable));

// Add a marker for the car on the map
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(location) // Specify the position of the car
                                    .title(lname)
                                    .icon(carIcon); // Set the custom icon for the car

                            Marker carMarker = mMap.addMarker(markerOptions);
                            String tagValue = locationobj.getKey (); // Replace with your desired tag value
                            carMarker.setTag(tagValue);

                        }
                        if(latLng!=null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Zoom to the last fetched location
                        }

                    }



                }

                @Override
                public void onCancelled(DatabaseError error) {


                }
            } );
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
