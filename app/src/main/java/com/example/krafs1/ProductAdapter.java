package com.example.krafs1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        productView.setLayoutParams(layoutParams);

        // Return a new holder instance
        return new ViewHolder(productView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (productList.isEmpty()) {
            holder.tvEmpty.setVisibility(View.VISIBLE);
            holder.tvEmpty.setText("Kosong");
            holder.ivDetail.setVisibility(View.GONE);
        }else {
            MerchantPage.Product product = productList.get(position);
            Log.d("onBindViewHolder: 321",productList.toString());
            holder.tvEmpty.setVisibility(View.VISIBLE);
            holder.tvEmpty.setText("kosong");
            holder.tvId.setText(product.getIdp());
            holder.tvProductName.setText(product.getName());
            holder.tvProductPrice.setText(product.getPrice());
            Picasso.get().load(product.getImageUrl()).into(holder.ivProductImage);

            holder.ivDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Menyiapkan Intent untuk berpindah ke aktivitas detail
                    Intent intent = new Intent(view.getContext(), ProductDetail.class);

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
        if (productList.isEmpty()) {
            return 1;
        } else {
            return productList.size();
        }
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
