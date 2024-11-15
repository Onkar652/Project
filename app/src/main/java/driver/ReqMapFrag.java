package driver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bit.bookcab.R;
import com.bit.bookcab.RequestDetail;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import gettersetter.VehicleGtSt;

public class ReqMapFrag extends Fragment implements OnMapReadyCallback {
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
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // Retrieve the tag value
                String tagValue = (String) marker.getTag();

                // Open a new activity when the marker title is clicked
                Intent intent = new Intent(getActivity(), RequestDetail.class);

                // Pass the tag value as an extra to the next activity
                intent.putExtra("markerTag", tagValue);

                startActivity(intent);
            }
        });

        getdata("RideReq");;
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

                    for (DataSnapshot req : dataSnapshot.getChildren ()) {
                        HashMap<String, Object> yourData = (HashMap<String, Object>) req.getValue ();

                        // Get request date and isAccepted status
                        String dateString = (String) yourData.get("date");
                        boolean isAccepted = (boolean) yourData.get("isaccepted");

                        // Parse the date string to Date object
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date requestDate;
                        try {
                            requestDate = sdf.parse(dateString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            continue; // Skip this request if date parsing fails
                        }

                        // Check if the request date is before the current date and if it's accepted
                        if (requestDate.before(new Date()) || isAccepted) {
                            // Skip adding marker
                            continue;
                        }



                        double lati= (double) yourData.get ( "fromlatitude");
                        double longitude= (double) yourData.get ( "fromlongitude");
                        latLng=new LatLng(lati,longitude);
                        String lname= (String) yourData.get ( "reqestername");
                        LatLng location = new LatLng(lati, longitude); // Replace with the desired coordinates
                        Drawable carDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.twotone_hail_24);
                        Random random = new Random();
                        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                        carDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        BitmapDescriptor carIcon = BitmapDescriptorFactory.fromBitmap(getBitmapFromDrawable(carDrawable));
                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(location) // Specify the position of the car
                                .title(lname)
                                .icon(carIcon); // Set the custom icon for the car

                        Marker carMarker = mMap.addMarker(markerOptions);
                        String tagValue = req.getKey (); // Replace with your desired tag value
                        carMarker.setTag(tagValue);


//                        for (DataSnapshot locationobj : user.getChildren ()) {
//
//
//                        }

                    }
                    if(latLng!=null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // Zoom to the last fetched location
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
