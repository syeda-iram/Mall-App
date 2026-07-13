package com.example.madproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private RecyclerView favouritesRecyclerView;
    private FavouriteAdapter favouritesAdapter;
    private List<FavouriteItem> favouriteItems = new ArrayList<>();
    private SharedPreferences favouritesPrefs;
    private Gson gson = new Gson();
    private TextView emptyStateText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        // Initialize SharedPreferences
        favouritesPrefs = getActivity().getSharedPreferences("favourites_prefs", Context.MODE_PRIVATE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        loadFavouriteItemsFromStorage();
        checkEmptyState();
    }

    private void initViews(View view) {
        favouritesRecyclerView = view.findViewById(R.id.favourites_recycler_view);

        // Create empty state view
        emptyStateText = new TextView(getContext());
        emptyStateText.setText("No favourite items yet");
        emptyStateText.setTextSize(18);
        emptyStateText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        emptyStateText.setPadding(0, 100, 0, 0);
        emptyStateText.setGravity(View.TEXT_ALIGNMENT_CENTER);
    }

    private void setupRecyclerView() {
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favouritesAdapter = new FavouriteAdapter(favouriteItems, new FavouriteAdapter.OnItemRemoveListener() {
            @Override
            public void onItemRemoved(String itemId) {
                // Handle item removal from storage
                saveFavouriteItemsToStorage();
                checkEmptyState();
            }
        });
        favouritesRecyclerView.setAdapter(favouritesAdapter);
    }

    private void loadFavouriteItemsFromStorage() {
        // Load from SharedPreferences
        String favouritesJson = favouritesPrefs.getString("favourites_list", "[]");

        // Convert JSON to List
        Type type = new TypeToken<List<FavouriteItem>>() {}.getType();
        List<FavouriteItem> savedItems = gson.fromJson(favouritesJson, type);

        if (savedItems != null && !savedItems.isEmpty()) {
            favouriteItems.clear();
            favouriteItems.addAll(savedItems);

            if (favouritesAdapter != null) {
                favouritesAdapter.notifyDataSetChanged();
            }
        } else {
            // If no saved items, load default items
            loadDefaultFavouriteItems();
        }
    }

    private void loadDefaultFavouriteItems() {
        favouriteItems.clear();

        // Add default items
        favouriteItems.add(new FavouriteItem(
                "1",
                "Cougar Jeans",
                "Cougar",
                "$45",
                "50%",
                R.drawable.pantsitem,
                "Cougar Store"
        ));

        favouriteItems.add(new FavouriteItem(
                "2",
                "Uniworth Casual Shirt",
                "Uniworth",
                "$14.99",
                "75%",
                R.drawable.casualshirt,
                "Uniworth Store"
        ));

        favouriteItems.add(new FavouriteItem(
                "3",
                "Uniworth Full Sleeves Shirt",
                "Uniworth",
                "$14.9",
                "75%",
                R.drawable.fullsleevesshirt,
                "Uniworth Store"
        ));

        // Save defaults to storage
        saveFavouriteItemsToStorage();

        if (favouritesAdapter != null) {
            favouritesAdapter.notifyDataSetChanged();
        }
    }

    private void saveFavouriteItemsToStorage() {
        // Convert list to JSON
        String favouritesJson = gson.toJson(favouriteItems);

        // Save to SharedPreferences
        SharedPreferences.Editor editor = favouritesPrefs.edit();
        editor.putString("favourites_list", favouritesJson);
        editor.apply();
    }

    // Method to add a new favourite item dynamically
    public void addFavouriteItem(FavouriteItem item) {
        if (favouriteItems != null && favouritesAdapter != null) {
            // Check if item already exists
            boolean exists = false;
            for (FavouriteItem existingItem : favouriteItems) {
                if (existingItem.getId().equals(item.getId())) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                favouriteItems.add(item);
                favouritesAdapter.notifyItemInserted(favouriteItems.size() - 1);
                saveFavouriteItemsToStorage();
                checkEmptyState();
            }
        }
    }

    // Method to remove a favourite item
    public void removeFavouriteItem(String itemId) {
        if (favouriteItems != null && favouritesAdapter != null) {
            for (int i = 0; i < favouriteItems.size(); i++) {
                if (favouriteItems.get(i).getId().equals(itemId)) {
                    favouriteItems.remove(i);
                    favouritesAdapter.notifyItemRemoved(i);
                    saveFavouriteItemsToStorage();
                    checkEmptyState();
                    break;
                }
            }
        }
    }

    // Method to clear all favourites
    public void clearAllFavourites() {
        if (favouriteItems != null && favouritesAdapter != null) {
            int size = favouriteItems.size();
            favouriteItems.clear();
            favouritesAdapter.notifyItemRangeRemoved(0, size);
            saveFavouriteItemsToStorage();
            checkEmptyState();
        }
    }

    // Check if list is empty and show/hide empty state
    private void checkEmptyState() {
        if (favouriteItems.isEmpty()) {
            favouritesRecyclerView.setVisibility(View.GONE);

            // Remove empty state text if already added
            if (emptyStateText.getParent() != null) {
                ((ViewGroup) emptyStateText.getParent()).removeView(emptyStateText);
            }

            // Add empty state text to parent layout
            ViewGroup parent = (ViewGroup) getView();
            if (parent != null) {
                parent.addView(emptyStateText);
            }
        } else {
            favouritesRecyclerView.setVisibility(View.VISIBLE);

            // Remove empty state text if shown
            if (emptyStateText.getParent() != null) {
                ((ViewGroup) emptyStateText.getParent()).removeView(emptyStateText);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveFavouriteItemsToStorage();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveFavouriteItemsToStorage();
    }
}