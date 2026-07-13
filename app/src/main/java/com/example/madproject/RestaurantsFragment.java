package com.example.madproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RestaurantsFragment extends Fragment {

    private LinearLayout restaurantCard1;
    private LinearLayout restaurantCard2;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        customizeSearchViewColors();
        setupClickListeners();
        setupSearch();
    }

    private void initViews(View view) {
        restaurantCard1 = view.findViewById(R.id.restaurant1);
        restaurantCard2 = view.findViewById(R.id.restaurant2);
        searchView = view.findViewById(R.id.search_restaurants);
    }

    private void customizeSearchViewColors() {
        // Method 1: Simple approach - find the AutoCompleteTextView inside SearchView
        View searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        if (searchEditText instanceof AutoCompleteTextView) {
            AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) searchEditText;

            // Set text color to BLACK (user input)
            autoCompleteTextView.setTextColor(Color.parseColor("#000000"));

            // Set hint text color to DARK GRAY
            autoCompleteTextView.setHintTextColor(Color.parseColor("#333333"));

            // Optional: Set hint text
            autoCompleteTextView.setHint("Search restaurants, cuisine...");
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

    // Alternative Method 2: Using resource IDs
    private void customizeSearchViewColorsAlternative() {
        try {
            // Find the AutoCompleteTextView using its resource ID
            int searchTextId = getResources().getIdentifier("search_src_text", "id",
                    getActivity().getPackageName());
            AutoCompleteTextView searchText = searchView.findViewById(searchTextId);

            if (searchText != null) {
                searchText.setTextColor(Color.parseColor("#000000"));
                searchText.setHintTextColor(Color.parseColor("#333333"));
            }

            // Change search icon color
            int searchIconId = getResources().getIdentifier("search_mag_icon", "id",
                    getActivity().getPackageName());
            android.widget.ImageView searchIcon = searchView.findViewById(searchIconId);
            if (searchIcon != null) {
                searchIcon.setColorFilter(Color.parseColor("#333333"),
                        android.graphics.PorterDuff.Mode.SRC_IN);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupClickListeners() {
        restaurantCard1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
            intent.putExtra("restaurant_name", "Olive Garden");
            intent.putExtra("cuisine", "Italian");
            intent.putExtra("discount", "20%");
            intent.putExtra("rating", "5.0");
            intent.putExtra("image_resource", R.drawable.olivegarden);
            intent.putExtra("specialty", "Pasta, Pizza");
            startActivity(intent);
        });

        restaurantCard2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RestaurantDetailActivity.class);
            intent.putExtra("restaurant_name", "Burger King");
            intent.putExtra("cuisine", "Fast Food");
            intent.putExtra("discount", "15%");
            intent.putExtra("rating", "4.8");
            intent.putExtra("image_resource", R.drawable.burgerking);
            intent.putExtra("specialty", "Burgers, Fries");
            startActivity(intent);
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterRestaurants(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRestaurants(newText);
                return true;
            }
        });
    }

    private void filterRestaurants(String searchText) {
        String searchLower = searchText.toLowerCase().trim();
        boolean showCard1 = "olive garden".contains(searchLower) ||
                "italian".contains(searchLower) ||
                "pasta".contains(searchLower) ||
                "pizza".contains(searchLower);
        boolean showCard2 = "burger king".contains(searchLower) ||
                "fast food".contains(searchLower) ||
                "burgers".contains(searchLower) ||
                "fries".contains(searchLower);

        restaurantCard1.setVisibility(showCard1 ? View.VISIBLE : View.GONE);
        restaurantCard2.setVisibility(showCard2 ? View.VISIBLE : View.GONE);
    }
}