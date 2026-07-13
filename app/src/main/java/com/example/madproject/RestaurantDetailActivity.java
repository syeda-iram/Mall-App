package com.example.madproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RestaurantDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        String restaurantName = getIntent().getStringExtra("restaurant_name");
        String cuisine = getIntent().getStringExtra("cuisine");
        String discount = getIntent().getStringExtra("discount");
        String rating = getIntent().getStringExtra("rating");
        String specialty = getIntent().getStringExtra("specialty");
        int imageResource = getIntent().getIntExtra("image_resource", 0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(restaurantName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImageView restaurantImage = findViewById(R.id.restaurant_image);
        TextView titleView = findViewById(R.id.restaurant_title);
        TextView cuisineView = findViewById(R.id.restaurant_cuisine);
        TextView discountView = findViewById(R.id.restaurant_discount);
        TextView ratingView = findViewById(R.id.restaurant_rating);
        TextView specialtyView = findViewById(R.id.restaurant_specialty);

        if (restaurantImage != null && imageResource != 0) {
            restaurantImage.setImageResource(imageResource);
        }

        if (titleView != null && restaurantName != null) {
            titleView.setText(restaurantName);
        }

        if (cuisineView != null && cuisine != null) {
            cuisineView.setText("Cuisine: " + cuisine);
        }

        if (discountView != null && discount != null) {
            discountView.setText("Special Offer: " + discount);
        }

        if (ratingView != null && rating != null) {
            ratingView.setText("Rating: " + rating + "/5");
        }

        if (specialtyView != null && specialty != null) {
            specialtyView.setText("Specialty: " + specialty);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}