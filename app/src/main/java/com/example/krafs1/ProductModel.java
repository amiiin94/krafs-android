package com.example.krafs1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class ProductModel {
    private String id;
    private String name;
    private int price;
    private String stock;
    private String description;
    private String imageUrl;

    public ProductModel(JSONObject productJson) throws JSONException {
        this.id = productJson.getString("_id");
        this.name = productJson.getString("name");
        this.price = productJson.getInt("price");
        this.stock = productJson.getString("stock");
        this.description = productJson.getString("description");

        // Mengambil URL gambar
        JSONArray images = productJson.getJSONArray("images");
        this.imageUrl = images.getString(0);
    }

    // Setter for 'id'
    public void setId(String id) {
        this.id = id;
    }

    // Getter for 'id'
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatRupiah.setCurrency(Currency.getInstance("IDR"));
        return formatRupiah.format(price).replace("Rp", "").trim();
    }

    public String getStock() {
        return stock;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
