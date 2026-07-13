package com.example.madproject;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    FavouritesFragment favouritesFragment = new FavouritesFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is logged in - THIS IS CRITICAL!
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User not logged in, redirect to LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity so user can't go back
            return; // Stop here, don't set up the app
        }

        // Only setup the app if user is logged in
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackgroundColor(Color.WHITE);
        bottomNavigationView.setItemIconTintList(createColorStateList());
        bottomNavigationView.setItemTextColor(createColorStateList());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, homeFragment)
                            .commit();
                    return true;
                } else if (id == R.id.search) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, searchFragment)
                            .commit();
                    return true;
                } else if (id == R.id.favourites) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, favouritesFragment)
                            .commit();
                    return true;
                } else if (id == R.id.profile) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, profileFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }

    private ColorStateList createColorStateList() {
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked }, // checked state
                new int[] { -android.R.attr.state_checked } // default state
        };

        int[] colors = new int[] {
                Color.parseColor("#F57C00"), // orange when selected
                Color.parseColor("#757575")  // grey when not selected
        };

        return new ColorStateList(states, colors);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Double-check authentication when activity starts
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}