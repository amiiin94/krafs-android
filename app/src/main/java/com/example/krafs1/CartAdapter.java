package com.example.krafs1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<CartPage.Cart> cartList;
    private Context context;
    private CartPage cartPage;
    public CartAdapter(List<CartPage.Cart> cartList, CartPage cartPage) {
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

        if (holder.tvId != null) {
            holder.tvId.setText(cart.getIdcart());
        }
        if (holder.product_name != null) {
            holder.product_name.setText(cart.getProduct_name());
        }
        // Format and set the product price
        if (holder.product_price != null) {
            double productPrice = parseAndFormatPrice(cart.getProduct_price());
            holder.product_price.setText(formatCurrency(productPrice));
        }
        if (holder.quantity != null) {
            holder.quantity.setText(String.valueOf(cart.getQuantity()));
        }
        holder.plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idcart = holder.tvId.getText().toString();
                int Qbelum = Integer.parseInt(holder.quantity.getText().toString());
                int Qsudah = Qbelum + 1;

                // Update the quantity in the cartList
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    updateQuantity(position, Qsudah);
                }

                // Menyiapkan Intent untuk berpindah ke aktivitas detail
                holder.quantity.setText(String.valueOf(Qsudah));
            }
        });
        holder.minus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idcart = holder.tvId.getText().toString();
                int Qbelum = Integer.parseInt(holder.quantity.getText().toString());

                if (Qbelum >= 2) {
                    int Qsudah = Qbelum - 1;

                    if (cartPage != null) {
                        cartPage.editQuantityByIdproduct(idcart, Qsudah);
                    }
                    holder.quantity.setText(String.valueOf(Qsudah));
                } else {
                    int Qsudah = Qbelum - 0;

                    if (cartPage != null) {
                        cartPage.editQuantityByIdproduct(idcart, Qsudah);
                    }
                    // Update the quantity in the cartList
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        updateQuantity(position, Qsudah);
                    }

                    // Update the quantity text in the ViewHolder
                    holder.quantity.setText(String.valueOf(Qsudah));
                }
            }
        });
    }

    private double parseAndFormatPrice(String price) {
        try {
            // Parse the product price to double
            return Double.parseDouble(price.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private String formatCurrency(double amount) {
        // Format the amount as currency in Indonesian Rupiah (IDR)
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setCurrencySymbol("Rp");
        symbols.setMonetaryDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat format = new DecimalFormat("Rp #,###", symbols);

        return format.format(amount);
    }

    // Add this method to update the quantity in cartList
    public void updateQuantity(int position, int newQuantity) {
        if (position >= 0 && position < cartList.size()) {
            cartList.get(position).setQuantity(newQuantity);
            notifyItemChanged(position);
        }
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
        TextView plus_btn, minus_btn;

        ViewHolder(View itemView) {
            super(itemView);

            // Get references to the views defined in item_cart.xml
            tvId = itemView.findViewById(R.id.tvId); // Make sure to have this ID in your item_cart.xml
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.quantity);
            plus_btn = itemView.findViewById(R.id.plus_btn);
            minus_btn = itemView.findViewById(R.id.minus_btn);
        }
    }
}

