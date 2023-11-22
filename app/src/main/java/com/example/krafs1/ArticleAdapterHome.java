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

public class ArticleAdapterHome extends RecyclerView.Adapter<ArticleAdapterHome.ViewHolder> {

    private List<ArticlePage.Article> articleList;
    private Context context;

    public ArticleAdapterHome(List<ArticlePage.Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleAdapterHome.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article_home, parent, false);

        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMarginEnd(25);
        articleView.setLayoutParams(layoutParams);

        // Return a new holder instance
        return new ArticleAdapterHome.ViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapterHome.ViewHolder holder, int position) {
        // Mendapatkan data dari dataset pada posisi tertentu
        ArticlePage.Article article = articleList.get(position);

        holder.tvId.setText(article.getIdp());
        holder.title.setText(article.getTitle());
        holder.content.setText(article.getContent());

        holder.read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menyiapkan Intent untuk berpindah ke aktivitas detail
                Intent intent = new Intent(view.getContext(), ArticleDetail.class);

                // Menambahkan ID ke Intent
                intent.putExtra("ARTICLE_ID", article.getIdp());

                // Memulai aktivitas detail
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Check if the articleList is null or empty
        return articleList == null ? 0 : articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId;
        TextView title;
        TextView content;
        TextView tvEmpty;
        TextView read_more;

        ViewHolder(View itemView) {
            super(itemView);

            // Get references to the views defined in item_product.xml
            tvEmpty = itemView.findViewById(R.id.tvEmpty);
            tvId = itemView.findViewById(R.id.tvId);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            read_more = itemView.findViewById(R.id.read_more);
        }
    }
}
