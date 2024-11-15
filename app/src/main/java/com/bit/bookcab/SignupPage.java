package com.bit.bookcab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gettersetter.User;
import utils.PreferenceHelper;


public class SignupPage extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText edtusername,edtupassword,edtfullname,edtother,edtphonenumber;
    Button btnsignup;
    RadioButton yes,no,male,female,radioButtonCustomer,radioButtonDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup );
        edtusername= findViewById(R.id.edtusername);
        edtupassword= findViewById(R.id.edtpin);
        btnsignup= findViewById(R.id.signUpBtn);
        edtfullname =findViewById( R.id.edtfullname );
      edtother=findViewById(R.id.edtsporthistory);
//        edtaddress=findViewById( R.id.address );
        edtphonenumber=findViewById( R.id.edtphoneno );
        male=findViewById(R.id.radiomale);
        female=findViewById(R.id.radiofemale);
        radioButtonCustomer=findViewById(R.id.radioButtonCustomer);
        radioButtonDriver=findViewById(R.id.radioButtonDriver);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username= edtusername.getText().toString().trim();
                String password = edtupassword.getText().toString().trim();
                Signup(username,password);
            }
        });
    }

    public void Signup(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Signup successful, get user ID
                            String userId = mAuth.getCurrentUser().getUid();
                            Toast.makeText(SignupPage.this, "UserCreated-" + userId, Toast.LENGTH_LONG).show();
                            User user=new User();
                            user.setFullname(edtfullname.getText().toString());
                            user.setEmail(edtusername.getText().toString());
                            user.setMobile(edtphonenumber.getText().toString());
                            if(male.isChecked()){
                                user.setGender("Male");
                            }else{
                                user.setGender("Female");
                            }
                            if(radioButtonCustomer.isChecked()){
                                user.setUsertype("C");
                            }else{
                                user.setUsertype("D");
                            }

                            // user.getUseraddress()




                            DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
                            mdatabase.child( "Users" ).child( userId ).setValue( user );
//

                            if(radioButtonCustomer.isChecked()){
                                PreferenceHelper.storeUsertype(SignupPage.this,"C");
                                Intent i = new Intent(getApplicationContext(), CustomerHome.class) ;
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear all previous activities
                                startActivity(i);
                                finish();
                            }else{
                                PreferenceHelper.storeUsertype(SignupPage.this,"D");
                                Intent i = new Intent(getApplicationContext(), DriverHome.class) ;
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear all previous activities
                                startActivity(i);
                                finish();
                            }

// Do something with the user ID
                        } else {
// Signup failed
                            Log.e("TAG", "Signup failed.", task.getException());
                            Toast.makeText(SignupPage.this, "Error-" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

