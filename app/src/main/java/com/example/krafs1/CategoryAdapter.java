package com.example.krafs1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<MerchantPage.Category> categoryList;
    private Context context;
    private MerchantPage merchantPage;

    public CategoryAdapter(List<MerchantPage.Category> categoryList, MerchantPage merchantPage) {
        this.categoryList = categoryList;
        this.merchantPage = merchantPage;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

         // Inflate the custom layout
        View categoryView = inflater.inflate(R.layout.item_category, parent, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMarginEnd(10);
        categoryView.setLayoutParams(layoutParams);

        // Return a new holder instance
        return new CategoryAdapter.ViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        MerchantPage.Category category = categoryList.get(position);

        // Set item views based on your views and data model
        holder.tvCategoryName.setText(category.getName());

        holder.tvCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryId = category.getIdc();
                merchantPage.getProductByCategory(categoryId);
            }
        });
    }
    public int getItemCount() {
        return categoryList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCategoryName;
        // Jika ada atribut lain yang ingin ditampilkan, tambahkan di sini
        // public ImageView ivCategoryImage;
        // public ProductAdapter productAdapter; // Contoh jika ingin menampilkan produk di dalam kategori

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            // ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            // Jika ingin menampilkan produk di dalam kategori, Anda bisa menambahkannya di sini
            // productAdapter = new ProductAdapter(); // Sesuaikan dengan nama adapter produk Anda
            // RecyclerView rvProducts = itemView.findViewById(R.id.rvProducts);
            // rvProducts.setAdapter(productAdapter);
        }
    }
}
