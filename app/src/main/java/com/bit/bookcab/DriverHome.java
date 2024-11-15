package com.bit.bookcab;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import driver.MyRides;
import driver.ReqMapFrag;
import driver.VehiclesFragment;
import driver.DriverProfileFragment;

public class DriverHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driverhome);
        bottomNavigationView = findViewById ( R.id.bottomNavigationView );
        bottomNavigationView.setOnNavigationItemSelectedListener ( this );
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.request:
                loadfragment ( new ReqMapFrag() );
                return true;


            case R.id.vehicles:
                loadfragment ( new VehiclesFragment() );
                return true;
            case R.id.myrides:
                loadfragment ( new MyRides() );
                return true;
            case R.id.profile:
                loadfragment(new DriverProfileFragment());
                return true;

        }

        return false;
    }

    public void loadfragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager ();
        fm.beginTransaction ().replace ( R.id.flFragment, fragment ).commit ();
    }

}
