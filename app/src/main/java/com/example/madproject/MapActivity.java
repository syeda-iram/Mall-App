package com.example.madproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    private ImageButton btnZoomIn, btnZoomOut;
    private AppCompatButton btnNavigate, btnLocateStore;
    private TextView txtCurrentFloor, txtNearestStore;
    private LinearLayout floorSelector;
    private TextView floor1, floor2, floor3;
    private SearchView searchView;
    private int currentFloor = 1;

    // Store markers
    private Marker userMarker;
    private Marker destinationMarker;

    // For mock navigation
    private boolean isNavigating = false;
    private Handler navigationHandler = new Handler();
    private Runnable navigationRunnable;

    // Default location (Centaurus Mall, Islamabad)
    private final LatLng DEFAULT_LOCATION = new LatLng(33.7294, 73.0931);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Test: Check if layout is loaded
        Toast.makeText(this, "Activity created", Toast.LENGTH_SHORT).show();

        // Test: Check if map fragment exists
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            Toast.makeText(this, "Map fragment is NULL!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Map fragment found", Toast.LENGTH_SHORT).show();
        }

        // Initialize views
        initializeViews();
        // Customize SearchView colors
        customizeSearchViewColors();
        // Set up click listeners
        setupClickListeners();
        // Initialize bottom panel with default values
        updateBottomPanel(currentFloor);

        // Initialize map immediately with default location
        initializeMap();
    }

    private void initializeMap() {
        // Try to get user location, but use default if permissions not granted
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            // Use default location and request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_PERMISSION_CODE);

            // Use default location for now
            currentLocation = new Location("");
            currentLocation.setLatitude(DEFAULT_LOCATION.latitude);
            currentLocation.setLongitude(DEFAULT_LOCATION.longitude);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                } else {
                    // Use default location
                    currentLocation = new Location("");
                    currentLocation.setLatitude(DEFAULT_LOCATION.latitude);
                    currentLocation.setLongitude(DEFAULT_LOCATION.longitude);
                }

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(MapActivity.this);
                } else {
                    Toast.makeText(MapActivity.this, "Map fragment not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Enable zoom controls
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);

        LatLng myPos;
        if (currentLocation != null) {
            myPos = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        } else {
            myPos = DEFAULT_LOCATION;
        }

        // Add user marker
        userMarker = googleMap.addMarker(new MarkerOptions()
                .position(myPos)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // Add some mock store locations around the user
        addMockStoreLocations(myPos);

        // Move camera to user location with zoom
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 17f));

        Toast.makeText(this, "Map loaded successfully!", Toast.LENGTH_SHORT).show();
    }

    private void addMockStoreLocations(LatLng center) {
        // Add mock stores around the user location
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(center.latitude + 0.0005, center.longitude + 0.0005))
                .title("Apple Store")
                .snippet("Floor 1 • Electronics"));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(center.latitude - 0.0003, center.longitude + 0.0007))
                .title("Food Court")
                .snippet("Floor 3 • Restaurants"));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(center.latitude + 0.0008, center.longitude - 0.0004))
                .title("Parking A3")
                .snippet("Floor 1 • Parking"));

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(center.latitude - 0.0006, center.longitude - 0.0003))
                .title("Nike Store")
                .snippet("Floor 2 • Clothing"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission Denied. Using default location.", Toast.LENGTH_SHORT).show();
                // Use default location
                currentLocation = new Location("");
                currentLocation.setLatitude(DEFAULT_LOCATION.latitude);
                currentLocation.setLongitude(DEFAULT_LOCATION.longitude);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                if (mapFragment != null) {
                    mapFragment.getMapAsync(this);
                }
            }
        }
    }

    private void initializeViews() {
        // Map control buttons
        btnZoomIn = findViewById(R.id.btnZoomIn);
        btnZoomOut = findViewById(R.id.btnZoomOut);

        // Action buttons
        btnNavigate = findViewById(R.id.btnNavigate);
        btnLocateStore = findViewById(R.id.btnBookParking);

        // Text views
        txtCurrentFloor = findViewById(R.id.txtCurrentFloor);
        txtNearestStore = findViewById(R.id.txtNearestStore);

        // Floor selector
        floorSelector = findViewById(R.id.floorSelector);
        floor1 = findViewById(R.id.floor1);
        floor2 = findViewById(R.id.floor2);
        floor3 = findViewById(R.id.floor3);

        // SearchView - Important: Change this to match your XML ID
        // If you don't have @+id/searchView, use the correct ID
        searchView = findViewById(R.id.searchView); // Check if this ID exists in your XML

        // Highlight current floor (F1 by default)
        updateFloorUI(currentFloor);
    }

    private void customizeSearchViewColors() {
        if (searchView != null) {
            View searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

            if (searchEditText instanceof AutoCompleteTextView) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) searchEditText;
                autoCompleteTextView.setTextColor(Color.parseColor("#000000"));
                autoCompleteTextView.setHintTextColor(Color.parseColor("#818181"));
                autoCompleteTextView.setHint("Search stores, parking, restaurants");
            } else if (searchEditText != null) {
                TextView textView = (TextView) searchEditText;
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setHintTextColor(Color.parseColor("#818181"));
            }

            // Change icon colors to orange
            View searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
            if (searchIcon instanceof android.widget.ImageView) {
                ((android.widget.ImageView) searchIcon).setColorFilter(
                        Color.parseColor("#F57C00"),
                        android.graphics.PorterDuff.Mode.SRC_IN
                );
            }

            View closeIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
            if (closeIcon instanceof android.widget.ImageView) {
                ((android.widget.ImageView) closeIcon).setColorFilter(
                        Color.parseColor("#F57C00"),
                        android.graphics.PorterDuff.Mode.SRC_IN
                );
            }

            // Set up search functionality
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    performSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        } else {
            Toast.makeText(this, "SearchView not found! Check ID in XML", Toast.LENGTH_SHORT).show();
        }
    }

    private void performSearch(String query) {
        Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_SHORT).show();

        // Mock search - find and zoom to Apple Store
        if (query.toLowerCase().contains("apple") && googleMap != null) {
            LatLng myPos = userMarker != null ? userMarker.getPosition() : DEFAULT_LOCATION;
            LatLng appleStore = new LatLng(myPos.latitude + 0.0005, myPos.longitude + 0.0005);

            if (destinationMarker != null) {
                destinationMarker.remove();
            }

            destinationMarker = googleMap.addMarker(new MarkerOptions()
                    .position(appleStore)
                    .title("Apple Store (Found)")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(appleStore, 18f));
            txtNearestStore.setText("Apple Store • 50m away\nTap 'Navigate' for directions");
        }
    }

    private void setupClickListeners() {
        // Zoom In button
        btnZoomIn.setOnClickListener(v -> {
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                Toast.makeText(this, "Zooming In", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Map not ready yet", Toast.LENGTH_SHORT).show();
            }
        });

        // Zoom Out button
        btnZoomOut.setOnClickListener(v -> {
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.zoomOut());
                Toast.makeText(this, "Zooming Out", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Map not ready yet", Toast.LENGTH_SHORT).show();
            }
        });

        // Navigate button
        btnNavigate.setOnClickListener(v -> {
            if (googleMap != null) {
                startMockNavigation();
            } else {
                Toast.makeText(this, "Map not ready yet", Toast.LENGTH_SHORT).show();
            }
        });

        // Locate Store button
        btnLocateStore.setOnClickListener(v -> {
            if (googleMap != null) {
                locateNearestStore();
            } else {
                Toast.makeText(this, "Map not ready yet", Toast.LENGTH_SHORT).show();
            }
        });

        // Floor selector clicks
        if (floor1 != null) {
            floor1.setOnClickListener(v -> changeFloor(1));
        }
        if (floor2 != null) {
            floor2.setOnClickListener(v -> changeFloor(2));
        }
        if (floor3 != null) {
            floor3.setOnClickListener(v -> changeFloor(3));
        }
    }

    private void startMockNavigation() {
        if (isNavigating) {
            stopNavigation();
            return;
        }

        isNavigating = true;
        btnNavigate.setText("Stop Navigation");

        Toast.makeText(this, "Starting mock navigation to Apple Store...", Toast.LENGTH_SHORT).show();

        // Create a mock destination (Apple Store)
        LatLng userPos = userMarker != null ? userMarker.getPosition() : DEFAULT_LOCATION;
        LatLng destination = new LatLng(userPos.latitude + 0.0005, userPos.longitude + 0.0005);

        if (destinationMarker != null) {
            destinationMarker.remove();
        }

        destinationMarker = googleMap.addMarker(new MarkerOptions()
                .position(destination)
                .title("Destination: Apple Store")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Animate camera to show both markers
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng((userPos.latitude + destination.latitude) / 2,
                        (userPos.longitude + destination.longitude) / 2),
                16f));

        // Start mock navigation animation
        navigationRunnable = new Runnable() {
            int steps = 0;

            @Override
            public void run() {
                if (steps < 10 && googleMap != null && userMarker != null) {
                    // Mock movement
                    double lat = userMarker.getPosition().latitude +
                            (destination.latitude - userMarker.getPosition().latitude) * 0.1;
                    double lng = userMarker.getPosition().longitude +
                            (destination.longitude - userMarker.getPosition().longitude) * 0.1;

                    userMarker.setPosition(new LatLng(lat, lng));

                    // Update distance display
                    float distance = calculateDistance(userMarker.getPosition(), destination);
                    txtNearestStore.setText(String.format("Navigating... %.0fm remaining", distance));

                    steps++;
                    navigationHandler.postDelayed(this, 1000);
                } else {
                    Toast.makeText(MapActivity.this, "You have reached your destination!", Toast.LENGTH_SHORT).show();
                    stopNavigation();
                }
            }
        };

        navigationHandler.postDelayed(navigationRunnable, 1000);
    }

    private void stopNavigation() {
        isNavigating = false;
        btnNavigate.setText("Navigate");

        if (navigationHandler != null && navigationRunnable != null) {
            navigationHandler.removeCallbacks(navigationRunnable);
        }

        if (destinationMarker != null) {
            destinationMarker.remove();
            destinationMarker = null;
        }

        txtNearestStore.setText("Nearest Store: Apple • 50m away");
    }

    private float calculateDistance(LatLng start, LatLng end) {
        float[] results = new float[1];
        Location.distanceBetween(start.latitude, start.longitude,
                end.latitude, end.longitude, results);
        return results[0];
    }

    private void locateNearestStore() {
        Toast.makeText(this, "Finding nearest store...", Toast.LENGTH_SHORT).show();

        // Find and zoom to Apple Store (mock)
        LatLng userPos = userMarker != null ? userMarker.getPosition() : DEFAULT_LOCATION;
        LatLng appleStore = new LatLng(userPos.latitude + 0.0005, userPos.longitude + 0.0005);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(appleStore, 18f));

        if (destinationMarker != null) {
            destinationMarker.remove();
        }

        destinationMarker = googleMap.addMarker(new MarkerOptions()
                .position(appleStore)
                .title("Nearest: Apple Store")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        txtNearestStore.setText("Apple Store located!\nFloor 1 • 50m away");
    }

    private void changeFloor(int floorNumber) {
        currentFloor = floorNumber;
        updateFloorUI(floorNumber);
        switchToFloor(floorNumber);
        updateBottomPanel(floorNumber);
    }

    private void updateFloorUI(int floorNumber) {
        // Reset all floors to default
        if (floor3 != null) {
            floor3.setBackgroundColor(Color.parseColor("#FFF6E5"));
            floor3.setTextColor(Color.parseColor("#F57C00"));
        }
        if (floor2 != null) {
            floor2.setBackgroundColor(Color.parseColor("#FFF6E5"));
            floor2.setTextColor(Color.parseColor("#F57C00"));
        }
        if (floor1 != null) {
            floor1.setBackgroundColor(Color.parseColor("#FFF6E5"));
            floor1.setTextColor(Color.parseColor("#F57C00"));
        }

        // Highlight selected floor
        switch (floorNumber) {
            case 1:
                if (floor1 != null) {
                    floor1.setBackgroundColor(Color.parseColor("#F57C00"));
                    floor1.setTextColor(Color.WHITE);
                }
                break;
            case 2:
                if (floor2 != null) {
                    floor2.setBackgroundColor(Color.parseColor("#F57C00"));
                    floor2.setTextColor(Color.WHITE);
                }
                break;
            case 3:
                if (floor3 != null) {
                    floor3.setBackgroundColor(Color.parseColor("#F57C00"));
                    floor3.setTextColor(Color.WHITE);
                }
                break;
        }
    }

    private void switchToFloor(int floorNumber) {
        String[] floorDescriptions = {
                "Switched to Floor 1: Ground level with main entrance and parking",
                "Switched to Floor 2: Electronics, clothing, and accessory stores",
                "Switched to Floor 3: Food court, restaurants, and entertainment zone"
        };

        int index = floorNumber - 1;
        if (index >= 0 && index < floorDescriptions.length) {
            Toast.makeText(this, floorDescriptions[index], Toast.LENGTH_LONG).show();
        }
    }

    private void updateBottomPanel(int floorNumber) {
        String[] floorNames = {"Ground Floor", "First Floor", "Second Floor"};
        String[] nearestStores = {
                "Apple Store • 50m away\nNike Store • 75m away\nParking: A3 • 100m",
                "Samsung Store • 30m away\nZara • 45m away\nParking: B2 • 120m",
                "Food Court • 20m away\nCinema • 60m away\nParking: C1 • 150m"
        };

        if (floorNumber >= 1 && floorNumber <= 3) {
            int index = floorNumber - 1;

            if (txtCurrentFloor != null) {
                txtCurrentFloor.setText("You are on " + floorNames[index]);
            }

            if (txtNearestStore != null) {
                txtNearestStore.setText("Nearest: " + nearestStores[index]);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (navigationHandler != null) {
            navigationHandler.removeCallbacksAndMessages(null);
        }
    }
}