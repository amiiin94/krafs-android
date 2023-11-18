package com.example.krafs1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<MerchantPage.Product> productList;
    private Context context;

    public ProductAdapter(List<MerchantPage.Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View productView = inflater.inflate(R.layout.item_product, parent, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        productView.setLayoutParams(layoutParams);

        // Return a new holder instance
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Mendapatkan data dari dataset pada posisi tertentu
        MerchantPage.Product product = productList.get(position);

        if (productList.isEmpty()) {
            // Menetapkan data ke tampilan holder
            holder.tvEmpty.setVisibility(View.VISIBLE);
            holder.tvId.setVisibility(View.GONE);
            holder.tvProductName.setVisibility(View.GONE);
            holder.tvProductPrice.setVisibility(View.GONE);
        }else {
            holder.tvEmpty.setVisibility(View.GONE);
            holder.tvId.setText(product.getIdp());
            holder.tvProductName.setText(product.getName());
            holder.tvProductPrice.setText(product.getPrice());
            Picasso.get().load(product.getImageUrl()).into(holder.ivProductImage);

            holder.ivDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Menyiapkan Intent untuk berpindah ke aktivitas detail
                    Intent intent = new Intent(view.getContext(), MerchantDetail.class);

                    // Menambahkan ID ke Intent
                    intent.putExtra("PRODUCT_ID", product.getIdp());

                    // Memulai aktivitas detail
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvId;
        TextView tvProductName;
        TextView tvProductPrice;
        ImageView ivDetail;
        TextView tvEmpty;

        ViewHolder(View itemView) {
            super(itemView);

            // Get references to the views defined in item_product.xml
            tvEmpty = itemView.findViewById(R.id.tvEmpty);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvId = itemView.findViewById(R.id.tvId);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivDetail = itemView.findViewById(R.id.ivDetail);
        }
    }
}
