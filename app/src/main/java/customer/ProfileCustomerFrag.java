package customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bit.bookcab.R;
import com.bit.bookcab.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileCustomerFrag extends Fragment {
    EditText info;
    TextView txtname,email,phone;
    View view;

    Button btnlogut;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate ( R.layout.profilecustomer ,container,false);
        txtname=view.findViewById ( R.id.txtname );
        email=view.findViewById ( R.id.email );
        phone=view.findViewById ( R.id.phone );
        btnlogut= view.findViewById(R.id.btnlogut);

//        aboutus=view.findViewById ( R.id.AboutUs );
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        getdata ( "Users/"+user.getUid() );
        btnlogut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                auth.signOut();
                Intent i =new Intent(getActivity(), SplashScreen.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear all previous activities
                startActivity(i);
            }
        });
        return view;
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

                    txtname.setText("Welcome "+yourData.get ( "fullname").toString());
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
}
