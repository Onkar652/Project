package com.bit.bookcab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import utils.PreferenceHelper;

public class LoginActivity extends AppCompatActivity {
   FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText edtusername, edtpassword;
    Button loginbtn;
    TextView createnewaccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        edtusername = findViewById( R.id.username );
        edtpassword = findViewById( R.id.password );
        loginbtn = findViewById( R.id.loginbtn );
        createnewaccount=findViewById( R.id.createnewaccount );
        loginbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=edtusername.getText().toString();
                String password=edtpassword.getText().toString();
                signInWithEmail(username,password);
            }
        } );
createnewaccount.setOnClickListener( new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent(LoginActivity.this,SignupPage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear all previous activities
        startActivity( i );

    }
} );
    }
    public void signInWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's

                            String uid = mAuth.getCurrentUser().getUid();
                            Toast.makeText(LoginActivity.this, "Login Successfully ."+uid,
                                    Toast.LENGTH_SHORT).show();
                            getdata("Users/"+uid);
//                            Intent i=new Intent(LoginActivity.this,HomeActivity.class);
//                            startActivity( i );
//                            finish();
// Do something with the user
                        } else {
// If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void getdata(String url) {

        try {

            FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance ();
            DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference ( url );
            mFirebaseDatabase.keepSynced ( true );
            mFirebaseDatabase.addListenerForSingleValueEvent ( new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> yourData = (HashMap<String, Object>) dataSnapshot.getValue ();
                    String usertype= yourData.get("usertype").toString();

                    if(usertype.equals("C")){
                        PreferenceHelper.storeUsertype(LoginActivity.this,"C");
                        Intent i = new Intent(getApplicationContext(), CustomerHome.class) ;
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear all previous activities
                        startActivity(i);
                        finish();
                    }else{
                        PreferenceHelper.storeUsertype(LoginActivity.this,"D");
                        Intent i = new Intent(getApplicationContext(), DriverHome.class) ;
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear all previous activities
                        startActivity(i);
                        finish();
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
}
