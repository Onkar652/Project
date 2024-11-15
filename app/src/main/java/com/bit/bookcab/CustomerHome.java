package com.bit.bookcab;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import customer.AddRequestActivity;
import customer.MyReqFragment;
import customer.ProfileCustomerFrag;
import customer.ProvidersMap;
import driver.VehiclesFragment;

public class CustomerHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    FloatingActionButton addreq;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerhome);
        bottomNavigationView = findViewById ( R.id.bottomNavigationView );
        bottomNavigationView.setOnNavigationItemSelectedListener ( this );
        addreq=findViewById(R.id.addreq);
        addreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CustomerHome.this, AddRequestActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.vprovider:
                 loadfragment ( new ProvidersMap() );
                return true;
            case R.id.myreq:
                loadfragment ( new MyReqFragment() );
                return true;

            case R.id.vehicles:
                loadfragment ( new VehiclesFragment() );
                return true;
            case R.id.profile:
                loadfragment(new ProfileCustomerFrag());
                return true;

        }

        return false;
    }

    public void loadfragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager ();
        fm.beginTransaction ().replace ( R.id.flFragment, fragment ).commit ();
    }
}
