package driver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.bookcab.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import gettersetter.VehicleGtSt;

public class VehiclesFragment extends Fragment {
    View view;
    FloatingActionButton addcar;
    RecyclerView vehiclelist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate ( R.layout.vehiclesfragmet ,container,false);
        addcar=view.findViewById(R.id.addvehicle);
        vehiclelist=view.findViewById(R.id.vehicleslist);
        System.out.println("---vehicle fragment---");
        addcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),AddVehicleActivity.class);
                startActivity(i);
            }
        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        getdata("Vehicles/"+auth.getUid());
        return view;
    }

    public void getdata(String url) {
        //System.out.println ( "-------------" + url );

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

                    for (DataSnapshot locationobj : dataSnapshot.getChildren ()) {
                        //System.out.println ( "is key key----------------" + locationobj.getKey () );

                        HashMap<String, Object> yourData = (HashMap<String, Object>) locationobj.getValue ();

                        // Create an instance of your custom object (VehicleGtSt)
                        VehicleGtSt vehicle = new VehicleGtSt();

// Set the values from your HashMap to the corresponding fields in your VehicleGtSt object
                        vehicle.setAc((Boolean) yourData.get("ac")); // Cast to Boolean
                        vehicle.setFuel(yourData.get("fuel").toString()); // Convert to String
                        vehicle.setMake(yourData.get("make").toString());
                        vehicle.setMilage(yourData.get("milage").toString());
                        vehicle.setModel(yourData.get("model").toString());
                        vehicle.setRate(yourData.get("rate").toString());
                        vehicle.setSeatno(yourData.get("seatno").toString());
                        vehicle.setYearmanufcture(yourData.get("yearmanufcture").toString());
                        vehicleGtStArrayList.add(vehicle);


                        VehicleRVAdapter adapter = new VehicleRVAdapter(vehicleGtStArrayList, getActivity());

                        // below line is for setting linear layout manager to our recycler view.
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

                        // below line is to set layout
                        // manager to our recycler view.
                        vehiclelist.setLayoutManager(linearLayoutManager);

                        // below line is to set adapter
                        // to our recycler view.
                        vehiclelist.setAdapter(adapter);

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

    public class VehicleRVAdapter extends RecyclerView.Adapter<VehicleRVAdapter.ViewHolder> {

        private ArrayList<VehicleGtSt> vehicleList;
        private Context context;

        public VehicleRVAdapter(ArrayList<VehicleGtSt> vehicleList, Context context) {
            this.vehicleList = vehicleList;
            this.context = context;
        }

        @NonNull
        @Override
        public VehicleRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_row, parent, false);
            return new VehicleRVAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VehicleRVAdapter.ViewHolder holder, int position) {
            VehicleGtSt vehicle = vehicleList.get(position);

            holder.textViewMake.setText("Make: " + vehicle.getMake());
            holder.textViewModel.setText("Model: " + vehicle.getModel());
            holder.textViewFuelType.setText("Fuel Type: " + vehicle.getFuel());
            holder.textViewSeats.setText("Seats: " + vehicle.getSeatno());
            holder.textViewAC.setText("AC: " + (vehicle.isAc() ? "Yes" : "No"));
            holder.textViewMileage.setText("Mileage: " + vehicle.getMilage());
            holder.textViewRate.setText("Rate per KM: " + vehicle.getRate());
            holder.textViewYearManufacture.setText("Year of Manufacture: " + vehicle.getYearmanufcture());
        }

        @Override
        public int getItemCount() {
            return vehicleList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView textViewMake, textViewModel, textViewFuelType, textViewSeats,
                    textViewAC, textViewMileage, textViewRate, textViewYearManufacture;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewMake = itemView.findViewById(R.id.textViewMake);
                textViewModel = itemView.findViewById(R.id.textViewModel);
                textViewFuelType = itemView.findViewById(R.id.textViewFuelType);
                textViewSeats = itemView.findViewById(R.id.textViewSeats);
                textViewAC = itemView.findViewById(R.id.textViewAC);
                textViewMileage = itemView.findViewById(R.id.textViewMileage);
                textViewRate = itemView.findViewById(R.id.textViewRate);
                textViewYearManufacture = itemView.findViewById(R.id.textViewYearManufacture);
            }
        }
    }

}
