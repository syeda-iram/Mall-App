package com.example.madproject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchInput;
    private ImageView filterIcon;
    private LinearLayout recentSearchesContainer;
    private LinearLayout suggestedStoresContainer;
    private GridLayout resultsGrid;
    private TextView resultsCount;
    private LinearLayout emptyState;
    private TextView emptyStateText;

    private List<String> recentSearches = new ArrayList<>(Arrays.asList(
            "UniWorth", "Cougar", "Apple", "Monark", "Outfitters"
    ));

    private List<Store> suggestedStores = new ArrayList<>();
    private List<Product> searchResults = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize all views
        searchInput = view.findViewById(R.id.search_input);
        filterIcon = view.findViewById(R.id.filter_icon);
        recentSearchesContainer = view.findViewById(R.id.recent_searches_container);
        suggestedStoresContainer = view.findViewById(R.id.suggested_stores_container);
        resultsGrid = view.findViewById(R.id.results_grid);
        resultsCount = view.findViewById(R.id.results_count);
        emptyState = view.findViewById(R.id.empty_state);
        emptyStateText = view.findViewById(R.id.empty_state_text);

        // Setup search functionality
        setupSearchListeners();
        loadRecentSearches();
        loadSuggestedStores();
        performSearch(""); // Show initial results
    }

    private void setupSearchListeners() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                performSearch(query);

                if (!query.isEmpty() && !recentSearches.contains(query)) {
                    addRecentSearch(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        filterIcon.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Filter options coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadRecentSearches() {
        if (recentSearchesContainer == null) return;

        recentSearchesContainer.removeAllViews();

        for (String search : recentSearches) {
            TextView chip = createSearchChip(search);
            recentSearchesContainer.addView(chip);
        }
    }

    private TextView createSearchChip(String text) {
        TextView chip = new TextView(getContext());
        chip.setText(text);
        chip.setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(8));
        chip.setTextColor(Color.WHITE);
        chip.setTextSize(14);

        // Create orange background with rounded corners
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#F57C00")); // Your orange color
        drawable.setCornerRadius(dpToPx(20));
        chip.setBackground(drawable);

        chip.setOnClickListener(v -> {
            searchInput.setText(text);
            searchInput.setSelection(text.length());
            performSearch(text);
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, dpToPx(8), 0);
        chip.setLayoutParams(params);

        return chip;
    }

    private void addRecentSearch(String query) {
        if (!recentSearches.contains(query)) {
            recentSearches.add(0, query);
            if (recentSearches.size() > 10) {
                recentSearches.remove(recentSearches.size() - 1);
            }
            loadRecentSearches();
        }
    }

    private void loadSuggestedStores() {
        if (suggestedStoresContainer == null) return;

        suggestedStores.clear();
        suggestedStores.add(new Store("Uniworth", R.drawable.uniworth));
        suggestedStores.add(new Store("Cougar", R.drawable.cou));
        suggestedStores.add(new Store("Apple", R.drawable.apple));
        suggestedStores.add(new Store("Monark", R.drawable.monark));
        suggestedStores.add(new Store("Outfitters", R.drawable.outfitter));

        suggestedStoresContainer.removeAllViews();

        for (Store store : suggestedStores) {
            CardView storeCard = createStoreCard(store);
            suggestedStoresContainer.addView(storeCard);
        }
    }

    private CardView createStoreCard(Store store) {
        CardView cardView = new CardView(getContext());
        cardView.setCardElevation(dpToPx(4));
        cardView.setRadius(dpToPx(12));
        cardView.setCardBackgroundColor(Color.WHITE);

        LinearLayout innerLayout = new LinearLayout(getContext());
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setGravity(android.view.Gravity.CENTER);
        innerLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(store.getImageResource());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dpToPx(80), dpToPx(80));
        imageView.setLayoutParams(imgParams);

        TextView storeName = new TextView(getContext());
        storeName.setText(store.getName());
        storeName.setTextColor(Color.parseColor("#1F2937"));
        storeName.setTextSize(14);
        storeName.setGravity(android.view.Gravity.CENTER);
        storeName.setPadding(0, dpToPx(8), 0, 0);

        innerLayout.addView(imageView);
        innerLayout.addView(storeName);
        cardView.addView(innerLayout);

        cardView.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Opening " + store.getName(), Toast.LENGTH_SHORT).show();
            // If you have StoreOutletActivity:
            // Intent intent = new Intent(getActivity(), StoreOutletActivity.class);
            // intent.putExtra("store_name", store.getName());
            // startActivity(intent);
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dpToPx(120),
                dpToPx(140)
        );
        params.setMargins(0, 0, dpToPx(12), 0);
        cardView.setLayoutParams(params);

        return cardView;
    }

    private void performSearch(String query) {
        if (resultsGrid != null) {
            resultsGrid.removeAllViews();
        }
        searchResults.clear();

        if (query.isEmpty()) {
            loadDefaultProducts();
        } else {
            filterProducts(query);
        }

        if (resultsCount != null) {
            resultsCount.setText(searchResults.size() + " products");
        }

        // Show/hide empty state
        if (emptyState != null && resultsGrid != null) {
            if (searchResults.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
                resultsGrid.setVisibility(View.GONE);
            } else {
                emptyState.setVisibility(View.GONE);
                resultsGrid.setVisibility(View.VISIBLE);
                displaySearchResults();
            }
        }
    }

    private void loadDefaultProducts() {
        searchResults.add(new Product("Uniworth Shirt", R.drawable.shirt, "$29.99", "Uniworth"));
        searchResults.add(new Product("Cougar Jeans", R.drawable.pantsitem, "$45.00", "Cougar"));
        searchResults.add(new Product("Apple iPhone", R.drawable.iphone, "$999.00", "Apple"));
        searchResults.add(new Product("Monark Laptop", R.drawable.laptop, "$799.00", "Monark"));
        searchResults.add(new Product("Outfitters Jacket", R.drawable.jacket, "$59.99", "Outfitters"));
        searchResults.add(new Product("Uniworth Pants", R.drawable.pants, "$39.99", "Uniworth"));
    }

    private void filterProducts(String query) {
        List<Product> allProducts = Arrays.asList(
                new Product("Uniworth Shirt", R.drawable.shirt, "$29.99", "Uniworth"),
                new Product("Cougar Jeans", R.drawable.pantsitem, "$45.00", "Cougar"),
                new Product("Apple iPhone", R.drawable.iphone, "$999.00", "Apple"),
                new Product("Monark Laptop", R.drawable.laptop, "$799.00", "Monark"),
                new Product("Outfitters Jacket", R.drawable.jacket, "$59.99", "Outfitters"),
                new Product("Uniworth Pants", R.drawable.pants, "$39.99", "Uniworth"),
                new Product("Cougar Shoes", R.drawable.shoes, "$89.99", "Cougar"),
                new Product("Apple Watch", R.drawable.watch, "$399.00", "Apple")
        );

        query = query.toLowerCase();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(query) ||
                    product.getStore().toLowerCase().contains(query)) {
                searchResults.add(product);
            }
        }
    }

    private void displaySearchResults() {
        if (resultsGrid == null) return;

        int columnCount = 2;

        for (int i = 0; i < searchResults.size(); i++) {
            Product product = searchResults.get(i);
            CardView productCard = createProductCard(product);

            GridLayout.Spec rowSpec = GridLayout.spec(i / columnCount);
            GridLayout.Spec colSpec = GridLayout.spec(i % columnCount, 1f);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
            params.width = 0;
            params.height = dpToPx(180);
            params.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));

            productCard.setLayoutParams(params);
            resultsGrid.addView(productCard);
        }
    }

    private CardView createProductCard(Product product) {
        CardView cardView = new CardView(getContext());
        cardView.setCardElevation(dpToPx(2));
        cardView.setRadius(dpToPx(8));
        cardView.setCardBackgroundColor(Color.WHITE);

        LinearLayout innerLayout = new LinearLayout(getContext());
        innerLayout.setOrientation(LinearLayout.VERTICAL);
        innerLayout.setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(12));

        // Product Image
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(product.getImageResource());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(100)
        );
        imageView.setLayoutParams(imgParams);
        imageView.setBackgroundColor(Color.parseColor("#F3F4F6"));

        // Product Name
        TextView nameText = new TextView(getContext());
        nameText.setText(product.getName());
        nameText.setTextColor(Color.parseColor("#1F2937"));
        nameText.setTextSize(12);
        nameText.setMaxLines(2);
        nameText.setPadding(0, dpToPx(8), 0, 0);

        // Product Store
        TextView storeText = new TextView(getContext());
        storeText.setText(product.getStore());
        storeText.setTextColor(Color.parseColor("#6B7280"));
        storeText.setTextSize(10);
        storeText.setPadding(0, dpToPx(2), 0, 0);

        // Product Price
        TextView priceText = new TextView(getContext());
        priceText.setText(product.getPrice());
        priceText.setTextColor(Color.parseColor("#F57C00"));
        priceText.setTextSize(14);
        priceText.setTypeface(null, Typeface.BOLD);
        priceText.setPadding(0, dpToPx(4), 0, 0);

        innerLayout.addView(imageView);
        innerLayout.addView(nameText);
        innerLayout.addView(storeText);
        innerLayout.addView(priceText);
        cardView.addView(innerLayout);

        cardView.setOnClickListener(v -> {
            Toast.makeText(getContext(), product.getName() + " - " + product.getPrice(),
                    Toast.LENGTH_SHORT).show();
        });

        return cardView;
    }

    private int dpToPx(int dp) {
        if (getContext() == null || getResources() == null) return dp;
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    // Inner classes
    private class Store {
        private String name;
        private int imageResource;

        public Store(String name, int imageResource) {
            this.name = name;
            this.imageResource = imageResource;
        }

        public String getName() { return name; }
        public int getImageResource() { return imageResource; }
    }

    private class Product {
        private String name;
        private int imageResource;
        private String price;
        private String store;

        public Product(String name, int imageResource, String price, String store) {
            this.name = name;
            this.imageResource = imageResource;
            this.price = price;
            this.store = store;
        }

        public String getName() { return name; }
        public int getImageResource() { return imageResource; }
        public String getPrice() { return price; }
        public String getStore() { return store; }
    }
}