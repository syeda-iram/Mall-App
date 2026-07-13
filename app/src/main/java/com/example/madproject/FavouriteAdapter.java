package com.example.madproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    private List<FavouriteItem> favouriteItems;
    private OnItemRemoveListener removeListener;

    public interface OnItemRemoveListener {
        void onItemRemoved(String itemId);
    }

    public FavouriteAdapter(List<FavouriteItem> favouriteItems, OnItemRemoveListener removeListener) {
        this.favouriteItems = favouriteItems;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favourites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavouriteItem item = favouriteItems.get(position);

        holder.productName.setText(item.getName());
        holder.productPrice.setText("Price: " + item.getPrice());
        holder.productDiscount.setText("Discount: " + item.getDiscount());
        holder.productImage.setImageResource(item.getImageResource());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Opening " + item.getName(), Toast.LENGTH_SHORT).show();
            // Add your navigation logic here
        });

        holder.removeIcon.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onItemRemoved(item.getId());
            }
            Toast.makeText(v.getContext(), item.getName() + " removed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return favouriteItems.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < favouriteItems.size()) {
            FavouriteItem removedItem = favouriteItems.get(position);
            favouriteItems.remove(position);
            notifyItemRemoved(position);

            if (removeListener != null) {
                removeListener.onItemRemoved(removedItem.getId());
            }
        }
    }

    public void addItem(FavouriteItem item) {
        favouriteItems.add(item);
        notifyItemInserted(favouriteItems.size() - 1);
    }

    public void updateItems(List<FavouriteItem> newItems) {
        favouriteItems.clear();
        favouriteItems.addAll(newItems);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productDiscount;
        ImageView removeIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDiscount = itemView.findViewById(R.id.product_discount);
            removeIcon = itemView.findViewById(R.id.remove_icon);
        }
    }
}