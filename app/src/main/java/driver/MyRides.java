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
import androidx.core.content.ContextCompat;
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

import gettersetter.RideRequest;
import gettersetter.VehicleGtSt;

public class MyRides extends Fragment {
    View view;
    FloatingActionButton addcar;
    RecyclerView vehiclelist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.vehiclesfragmet, container, false);
        addcar = view.findViewById(R.id.addvehicle);
        vehiclelist = view.findViewById(R.id.vehicleslist);
        System.out.println("---myyrides fragment---");
        addcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddVehicleActivity.class);
                startActivity(i);
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        getRideRequests("RideReq");

        return view;
    }

    public void getRideRequests(String url) {
        try {
            ArrayList<RideRequest> rideRequestsList = new ArrayList<>();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String uid=auth.getUid();
            FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
            DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference(url);
            mFirebaseDatabase.keepSynced(true);

            mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                        RideRequest rideRequest = requestSnapshot.getValue(RideRequest.class);
                        if (rideRequest != null && rideRequest.getCabproviderid().equals(uid)) {
                            rideRequestsList.add(rideRequest);
                        }
                    }

                    RideRequestAdapter adapter = new RideRequestAdapter(rideRequestsList, getActivity());
                    vehiclelist.setLayoutManager(new LinearLayoutManager(getActivity()));
                    vehiclelist.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class RideRequestAdapter extends RecyclerView.Adapter<RideRequestAdapter.ViewHolder> {
        private ArrayList<RideRequest> rideRequestsList;
        private Context context;

        public RideRequestAdapter(ArrayList<RideRequest> rideRequestsList, Context context) {
            this.rideRequestsList = rideRequestsList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RideRequest rideRequest = rideRequestsList.get(position);

            holder.textViewReqName.setText("Requester Name: " + rideRequest.getReqestername());
            holder.textViewSeatNumber.setText("Seats: " + rideRequest.getSeatNumber());
            holder.textViewDate.setText("Date: " + rideRequest.getDate());
            holder.textViewTime.setText("Time: " + rideRequest.getTime());
            holder.textViewAC.setText("AC: " + (rideRequest.isAc() ? "Yes" : "No"));
            holder.textViewIsAccepted.setText("Accepted: " + (rideRequest.isIsaccepted() ? "Yes" : "No"));
            // Set text color for textViewIsCompleted based on the status
            if (rideRequest.isIscompleted()) {
                holder.textViewIsCompleted.setTextColor(ContextCompat.getColor(context, R.color.green)); // Green color for completed rides
            } else {
                holder.textViewIsCompleted.setTextColor(ContextCompat.getColor(context, R.color.md_red_A700)); // Red color for pending rides
            }
            holder.textViewIsCompleted.setText("Status: " + (rideRequest.isIscompleted() ? "Ride Completed" : "Pending"));
        }

        @Override
        public int getItemCount() {
            return rideRequestsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewReqName, textViewSeatNumber, textViewDate, textViewTime, textViewAC, textViewIsAccepted,textViewIsCompleted;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewReqName = itemView.findViewById(R.id.textViewReqName);
                textViewSeatNumber = itemView.findViewById(R.id.textViewSeatNumber);
                textViewDate = itemView.findViewById(R.id.textViewDate);
                textViewTime = itemView.findViewById(R.id.textViewTime);
                textViewAC = itemView.findViewById(R.id.textViewAC);
                textViewIsAccepted = itemView.findViewById(R.id.textViewIsAccepted);
                textViewIsCompleted = itemView.findViewById(R.id.textViewIsCompleted);
            }
        }
    }
}