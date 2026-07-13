package com.example.madproject;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class StoreOutletActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_outlet);

        String storeName = getIntent().getStringExtra("store_name");
        String offerType = getIntent().getStringExtra("offer_type");
        String discount = getIntent().getStringExtra("discount");
        int storeLogo = getIntent().getIntExtra("store_logo", 0);
        String offerTitle = getIntent().getStringExtra("offer_title");
        String offerDescription = getIntent().getStringExtra("offer_description");
        String storeDetails = getIntent().getStringExtra("store_details");
        String contactInfo = getIntent().getStringExtra("contact_info");

        if (storeName == null) {
            storeName = "Store";
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(storeName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView logoView = findViewById(R.id.store_logo);
        TextView nameView = findViewById(R.id.store_name);
        TextView badgeView = findViewById(R.id.offer_badge);
        TextView titleView = findViewById(R.id.offer_title);
        TextView descView = findViewById(R.id.offer_description);
        TextView detailsView = findViewById(R.id.store_details);
        TextView contactView = findViewById(R.id.contact_info);

        if (logoView != null && storeLogo != 0) {
            logoView.setImageResource(storeLogo);
        }
        if (nameView != null) {
            nameView.setText(storeName);
        }
        if (badgeView != null && discount != null) {
            badgeView.setText(discount.equals("BOGO") ? "BOGO OFFER" : discount);
            setBadgeColor(badgeView, discount);
        }
        if (titleView != null && offerTitle != null) {
            titleView.setText(offerTitle);
        }
        if (descView != null && offerDescription != null) {
            descView.setText(offerDescription);
        }
        if (detailsView != null && storeDetails != null) {
            detailsView.setText(storeDetails);
        } else if (detailsView != null) {
            detailsView.setText(getDefaultStoreDetails(storeName));
        }
        if (contactView != null && contactInfo != null) {
            contactView.setText(contactInfo);
        } else if (contactView != null) {
            contactView.setText(getDefaultContactInfo(storeName));
        }
    }

    private void setBadgeColor(TextView badgeView, String discount) {
        GradientDrawable badgeBackground = new GradientDrawable();
        badgeBackground.setCornerRadius(30f); // Rounded corners

        int badgeColor;

        if (discount.contains("50")) {
            badgeColor = ContextCompat.getColor(this, android.R.color.holo_orange_dark);
        } else if (discount.contains("40")) {
            badgeColor = ContextCompat.getColor(this, android.R.color.holo_orange_light);
        } else if (discount.contains("30")) {
            badgeColor = ContextCompat.getColor(this, android.R.color.holo_green_dark);
        } else if (discount.contains("25")) {
            badgeColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
        } else if (discount.contains("BOGO")) {
            badgeColor = ContextCompat.getColor(this, android.R.color.holo_red_dark);
        } else {
            badgeColor = ContextCompat.getColor(this, android.R.color.holo_orange_dark);
        }

        badgeBackground.setColor(badgeColor);
        badgeView.setBackground(badgeBackground);
    }

    private String getDefaultStoreDetails(String storeName) {
        switch (storeName.toLowerCase()) {
            case "uniworth":
                return "• Premium clothing store\n• Formal & Casual wear\n• Located on Ground Floor\n• Timing: 10 AM - 9 PM";
            case "cougar":
                return "• Premium footwear store\n• Shoes, Boots & Sandals\n• Located on First Floor\n• Timing: 10 AM - 9 PM";
            case "monark":
                return "• Electronics & Gadgets store\n• Mobile Phones, Laptops, TVs\n• Located on Second Floor\n• Timing: 10 AM - 9 PM";
            case "apple":
                return "• Official Apple products\n• iPhones, iPads, MacBooks\n• Located on Third Floor\n• Timing: 10 AM - 9 PM";
            case "outfitters":
                return "• Fashion clothing store\n• Casual & Trendy wear\n• Located on Ground Floor\n• Timing: 10 AM - 9 PM";
            default:
                return "• Store details not available";
        }
    }

    private String getDefaultContactInfo(String storeName) {
        switch (storeName.toLowerCase()) {
            case "uniworth":
                return "Phone: +92 123 4567890\nEmail: info@uniworth.com\nWebsite: www.uniworth.com";
            case "cougar":
                return "Phone: +92 123 4567891\nEmail: info@cougar.com\nWebsite: www.cougar.com";
            case "monark":
                return "Phone: +92 123 4567892\nEmail: info@monark.com\nWebsite: www.monark.com";
            case "apple":
                return "Phone: +92 123 4567893\nEmail: info@applestore.com\nWebsite: www.apple.com";
            case "outfitters":
                return "Phone: +92 123 4567894\nEmail: info@outfitters.com\nWebsite: www.outfitters.com";
            default:
                return "Phone: Not available\nEmail: Not available\nWebsite: Not available";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}