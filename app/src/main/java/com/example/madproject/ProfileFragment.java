package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private CardView parkingCard;
    private CardView mapCard;
    private CardView offersCard;
    private CardView restaurantsCard;
    private CardView recentPurchaseCard;
    private Button logoutButton;
    private TextView userName;
    private TextView userEmail;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(); // Realtime Database
        currentUser = mAuth.getCurrentUser();

        initViews(view);
        setupClickListeners();
        loadUserData();
    }

    private void initViews(View view) {
        parkingCard = view.findViewById(R.id.parking_card);
        mapCard = view.findViewById(R.id.map_card);
        offersCard = view.findViewById(R.id.offers_card);
        restaurantsCard = view.findViewById(R.id.restaurants_card);
        recentPurchaseCard = view.findViewById(R.id.recent_purchase_card);
        logoutButton = view.findViewById(R.id.logout_button);
        userName = view.findViewById(R.id.tvUserName);
        userEmail = view.findViewById(R.id.tvUserEmail);
    }

    private void setupClickListeners() {
        parkingCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ParkingActivity.class);
            startActivity(intent);
        });

        mapCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            startActivity(intent);
        });

        offersCard.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.bottomNavigationView.setSelectedItemId(R.id.offers);
            }
        });

        restaurantsCard.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.bottomNavigationView.setSelectedItemId(R.id.restaurants);
            }
        });

        recentPurchaseCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Opening purchase history", Toast.LENGTH_SHORT).show();
        });

        logoutButton.setOnClickListener(v -> {
            performLogout();
        });
    }

    private void loadUserData() {
        if (currentUser != null) {
            String email = currentUser.getEmail();
            if (userEmail != null && email != null) {
                userEmail.setText(email);
            }

            if (currentUser.getUid() != null) {
                // Get user data from Realtime Database
                databaseReference.child("users").child(currentUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String name = dataSnapshot.child("name").getValue(String.class);
                                    if (userName != null && name != null && !name.isEmpty()) {
                                        userName.setText(name);
                                    } else {
                                        setDefaultUserName(email);
                                    }
                                } else {
                                    setDefaultUserName(email);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                setDefaultUserName(email);
                                Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                setDefaultUserName(email);
            }
        } else {
            if (userName != null) {
                userName.setText("Guest User");
            }
            if (userEmail != null) {
                userEmail.setText("Not logged in");
            }
        }
    }

    private void setDefaultUserName(String email) {
        if (email != null && userName != null) {
            String name = email.split("@")[0];
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            userName.setText(name);
        }
    }

    private void performLogout() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            mAuth.signOut();
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        currentUser = mAuth.getCurrentUser();
        loadUserData();
    }
}