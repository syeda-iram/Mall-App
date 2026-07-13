package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OffersFragment extends Fragment {

    private LinearLayout offerCard1;
    private LinearLayout offerCard2;
    private LinearLayout offerCard3;
    private LinearLayout offerCard4;
    private LinearLayout offerCard5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupClickListeners();
    }

    private void initViews(View view) {
        offerCard1 = view.findViewById(R.id.offer1);
        offerCard2 = view.findViewById(R.id.offer2);
        offerCard3 = view.findViewById(R.id.offer3);
        offerCard4 = view.findViewById(R.id.offer4);
        offerCard5 = view.findViewById(R.id.offer5);
    }

    private void setupClickListeners() {
        offerCard1.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StoreOutletActivity.class);
            intent.putExtra("store_name", "Uniworth");
            intent.putExtra("offer_type", "summer_sale");
            intent.putExtra("discount", "50%");
            intent.putExtra("store_logo", R.drawable.uniworth);
            intent.putExtra("offer_title", "Summer Sale");
            intent.putExtra("offer_description", "Up to 50% off on selected clothing items. Grab your favorites now!");
            intent.putExtra("store_details", "• Premium clothing store\n• Formal & Casual wear\n• Located on Ground Floor\n• Timing: 10 AM - 9 PM");
            intent.putExtra("contact_info", "Phone: +92 123 4567890\nEmail: info@uniworth.com\nWebsite: www.uniworth.com");
            startActivity(intent);
        });

        offerCard2.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StoreOutletActivity.class);
            intent.putExtra("store_name", "Cougar");
            intent.putExtra("offer_type", "buy_one_get_one");
            intent.putExtra("discount", "BOGO");
            intent.putExtra("store_logo", R.drawable.cou);
            intent.putExtra("offer_title", "Buy 1 Get 1 Free");
            intent.putExtra("offer_description", "Exclusive offer on footwear. Buy one pair and get the second free!");
            intent.putExtra("store_details", "• Premium footwear store\n• Shoes, Boots & Sandals\n• Located on First Floor\n• Timing: 10 AM - 9 PM");
            intent.putExtra("contact_info", "Phone: +92 123 4567891\nEmail: info@cougar.com\nWebsite: www.cougar.com");
            startActivity(intent);
        });

        offerCard3.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StoreOutletActivity.class);
            intent.putExtra("store_name", "Monark");
            intent.putExtra("offer_type", "electronics_fest");
            intent.putExtra("discount", "30%");
            intent.putExtra("store_logo", R.drawable.monark);
            intent.putExtra("offer_title", "Electronics Fest");
            intent.putExtra("offer_description", "Get up to 30% off on gadgets and electronics. Limited time offer!");
            intent.putExtra("store_details", "• Electronics & Gadgets store\n• Mobile Phones, Laptops, TVs\n• Located on Second Floor\n• Timing: 10 AM - 9 PM");
            intent.putExtra("contact_info", "Phone: +92 123 4567892\nEmail: info@monark.com\nWebsite: www.monark.com");
            startActivity(intent);
        });

        offerCard4.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StoreOutletActivity.class);
            intent.putExtra("store_name", "Apple");
            intent.putExtra("offer_type", "holiday_discounts");
            intent.putExtra("discount", "25%");
            intent.putExtra("store_logo", R.drawable.apple);
            intent.putExtra("offer_title", "Holiday Discounts");
            intent.putExtra("offer_description", "Save up to 25% on selected accessories and devices.");
            intent.putExtra("store_details", "• Official Apple products\n• iPhones, iPads, MacBooks\n• Located on Third Floor\n• Timing: 10 AM - 9 PM");
            intent.putExtra("contact_info", "Phone: +92 123 4567893\nEmail: info@applestore.com\nWebsite: www.apple.com");
            startActivity(intent);
        });

        offerCard5.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StoreOutletActivity.class);
            intent.putExtra("store_name", "Outfitters");
            intent.putExtra("offer_type", "weekend_sale");
            intent.putExtra("discount", "40%");
            intent.putExtra("store_logo", R.drawable.outfitter);
            intent.putExtra("offer_title", "Weekend Sale");
            intent.putExtra("offer_description", "Flat 40% off on jackets and winter clothing.");
            intent.putExtra("store_details", "• Fashion clothing store\n• Casual & Trendy wear\n• Located on Ground Floor\n• Timing: 10 AM - 9 PM");
            intent.putExtra("contact_info", "Phone: +92 123 4567894\nEmail: info@outfitters.com\nWebsite: www.outfitters.com");
            startActivity(intent);
        });
    }
}