package com.example.madproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {

    private SearchView searchView;
    private CardView cardParking;
    private CardView cardMap;
    private CardView cardOffers;
    private CardView cardRestaurants;
    private ImageView imgUniworth;
    private ImageView imgOutfitters;
    private ImageView imgMonark;
    private ImageView imgApple;
    private ImageView imgCougar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        customizeSearchViewColors(); // Add this line
        setupClickListeners();
        setupSearchView();
    }

    private void initViews(View view) {
        searchView = view.findViewById(R.id.cardSearch);
        cardParking = view.findViewById(R.id.parking);
        cardMap = view.findViewById(R.id.map);
        cardOffers = view.findViewById(R.id.offers);
        cardRestaurants = view.findViewById(R.id.restaurants);

        imgUniworth = view.findViewById(R.id.uniworth);
        imgOutfitters = view.findViewById(R.id.outfitters);
        imgMonark = view.findViewById(R.id.monark);
        imgApple = view.findViewById(R.id.apple);
        imgCougar = view.findViewById(R.id.cougar);
    }

    private void customizeSearchViewColors() {
        // Customize the SearchView text and hint colors

        // Get the internal EditText/AutoCompleteTextView inside SearchView
        View searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        if (searchEditText instanceof AutoCompleteTextView) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) searchEditText;

            // Set text color to BLACK (user input)
            autoCompleteTextView.setTextColor(Color.parseColor("#000000"));

            // Set hint text color to DARK GRAY (#818181 as in your XML)
            autoCompleteTextView.setHintTextColor(Color.parseColor("#818181"));

            // Optional: Set hint text
            autoCompleteTextView.setHint("Search stores & products");
        } else if (searchEditText != null) {
            // Fallback to TextView if AutoCompleteTextView cast fails
            android.widget.TextView textView = (android.widget.TextView) searchEditText;
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setHintTextColor(Color.parseColor("#818181"));
        }

        // Optional: Change search icon color
        View searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        if (searchIcon instanceof android.widget.ImageView) {
            ((android.widget.ImageView) searchIcon).setColorFilter(
                    Color.parseColor("#333333"),
                    android.graphics.PorterDuff.Mode.SRC_IN
            );
        }

        // Optional: Change close icon color
        View closeIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        if (closeIcon instanceof android.widget.ImageView) {
            ((android.widget.ImageView) closeIcon).setColorFilter(
                    Color.parseColor("#333333"),
                    android.graphics.PorterDuff.Mode.SRC_IN
            );
        }
    }

    private void setupSearchView() {
        // Set up search functionality
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When search is clicked, navigate to SearchFragment
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.bottomNavigationView.setSelectedItemId(R.id.search);
                }
            }
        });

        // Optional: Set up query text listener for actual search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text changes (for real-time search if needed)
                return false;
            }
        });
    }

    private void setupClickListeners() {
        // Removed cardSearch click listener since it's now handled in setupSearchView()

        cardParking.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ParkingActivity.class);
            startActivity(intent);
        });

        cardMap.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapActivity.class);
            startActivity(intent);
        });

        cardOffers.setOnClickListener(v -> {
            navigateToOffersFragment();
        });

        cardRestaurants.setOnClickListener(v -> {
            navigateToRestaurantsFragment();
        });

        imgUniworth.setOnClickListener(v -> {
            navigateToStore("Uniworth", R.drawable.uniworth);
        });

        imgOutfitters.setOnClickListener(v -> {
            navigateToStore("Outfitters", R.drawable.outfitter);
        });

        imgMonark.setOnClickListener(v -> {
            navigateToStore("Monark", R.drawable.monark);
        });

        imgApple.setOnClickListener(v -> {
            navigateToStore("Apple", R.drawable.apple);
        });

        imgCougar.setOnClickListener(v -> {
            navigateToStore("Cougar", R.drawable.cou);
        });
    }

    private void navigateToStore(String storeName, int logoResId) {
        Intent intent = new Intent(getActivity(), StoreOutletActivity.class);
        intent.putExtra("store_name", storeName);
        intent.putExtra("store_logo", logoResId);

        // You can add store-specific details here
        switch (storeName) {
            case "Uniworth":
                intent.putExtra("store_details", "• Premium clothing store\n• Formal & Casual wear\n• Located on Ground Floor\n• Timing: 10 AM - 9 PM");
                intent.putExtra("contact_info", "Phone: +92 123 4567890\nEmail: info@uniworth.com\nWebsite: www.uniworth.com");
                break;
            case "Outfitters":
                intent.putExtra("store_details", "• Fashion clothing store\n• Casual & Trendy wear\n• Located on Ground Floor\n• Timing: 10 AM - 9 PM");
                intent.putExtra("contact_info", "Phone: +92 123 4567894\nEmail: info@outfitters.com\nWebsite: www.outfitters.com");
                break;
            case "Monark":
                intent.putExtra("store_details", "• Electronics & Gadgets store\n• Mobile Phones, Laptops, TVs\n• Located on Second Floor\n• Timing: 10 AM - 9 PM");
                intent.putExtra("contact_info", "Phone: +92 123 4567892\nEmail: info@monark.com\nWebsite: www.monark.com");
                break;
            case "Apple":
                intent.putExtra("store_details", "• Official Apple products\n• iPhones, iPads, MacBooks\n• Located on Third Floor\n• Timing: 10 AM - 9 PM");
                intent.putExtra("contact_info", "Phone: +92 123 4567893\nEmail: info@applestore.com\nWebsite: www.apple.com");
                break;
            case "Cougar":
                intent.putExtra("store_details", "• Premium footwear store\n• Shoes, Boots & Sandals\n• Located on First Floor\n• Timing: 10 AM - 9 PM");
                intent.putExtra("contact_info", "Phone: +92 123 4567891\nEmail: info@cougar.com\nWebsite: www.cougar.com");
                break;
        }

        startActivity(intent);
    }

    private void performSearch(String query) {
        // Implement search functionality here
        // For example, you could navigate to SearchFragment with the query
        SearchFragment searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("search_query", query);
        searchFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.container, searchFragment);
        transaction.addToBackStack("search_fragment");
        transaction.commit();
    }

    private void navigateToOffersFragment() {
        OffersFragment offersFragment = new OffersFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.container, offersFragment);
        transaction.addToBackStack("offers_fragment");
        transaction.commit();
    }

    private void navigateToRestaurantsFragment() {
        RestaurantsFragment restaurantsFragment = new RestaurantsFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.container, restaurantsFragment);
        transaction.addToBackStack("restaurants_fragment");
        transaction.commit();
    }
}