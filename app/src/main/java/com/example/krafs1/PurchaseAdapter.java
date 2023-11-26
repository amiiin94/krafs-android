package com.example.krafs1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {
    private List<CartPage.Cart> cartList;

    public PurchaseAdapter(List<CartPage.Cart> cartList) {
        this.cartList = cartList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        CartPage.Cart cart = cartList.get(position);
        holder.productNameTextView.setText(cart.getProduct_name());
        holder.productPriceTextView.setText(cart.getProduct_price());
        holder.quantityTextView.setText(cart.getQuantity() + "x");
    }

    @Override
    public int getItemCount() {
        return cartList != null ? cartList.size() : 0; // Add null check here
    }

    private String formatToRupiah(int value) {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatRupiah.setCurrency(Currency.getInstance("IDR"));

        String formattedValue = formatRupiah.format(value).replace("Rp", "").trim();

        return "Rp. " + formattedValue;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productNameTextView;
        public TextView productPriceTextView;
        public TextView quantityTextView;

        public ViewHolder(View view) {
            super(view);
            productNameTextView = view.findViewById(R.id.product_name);
            productPriceTextView = view.findViewById(R.id.product_price);
            quantityTextView = view.findViewById(R.id.quantity);
        }
    }
}
