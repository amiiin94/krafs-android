package com.example.krafs1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartPage.Cart> cartList;
    private Context context;

    public CartAdapter(List<CartPage.Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View cartView = inflater.inflate(R.layout.item_cart, parent, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cartView.setLayoutParams(layoutParams);

        // Return a new holder instance
        return new CartAdapter.ViewHolder(cartView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        // Get data from the dataset at a certain position
        CartPage.Cart cart = cartList.get(position);

        holder.tvId.setText(cart.getIdp());
        holder.product_name.setText(cart.getProduct_name());
        holder.product_price.setText(cart.getProduct_price());
        holder.quantity.setText(String.valueOf(cart.getQuantity()));
    }

    @Override
    public int getItemCount() {
        // Check if the cartList is null or empty
        return cartList == null ? 0 : cartList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        TextView product_name;
        TextView product_price;
        TextView quantity;

        ViewHolder(View itemView) {
            super(itemView);

            // Get references to the views defined in item_cart.xml
            tvId = itemView.findViewById(R.id.tvId); // Make sure to have this ID in your item_cart.xml
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }
}

