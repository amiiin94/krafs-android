package com.example.krafs1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class ProductDetail extends AppCompatActivity {
    TextView nama_produk, harga_produk, stok_produk, deskripsi_produk;
    ImageView foto_produk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        // Menerima ID dari Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_ID")) {
            String productId = intent.getStringExtra("PRODUCT_ID");

            // Sekarang, Anda dapat menggunakan cardId untuk menampilkan detail kartu yang sesuai
            // ...
            foto_produk = findViewById(R.id.foto_produk);
            nama_produk = findViewById(R.id.nama_produk);
            harga_produk = findViewById(R.id.harga_produk);
            stok_produk = findViewById(R.id.stok_produk);
            deskripsi_produk = findViewById(R.id.deskripsi_produk);
//            tvcontoh.setText("Product ID: " + productId);
            getProductById(productId);
        } else {
            // Penanganan jika ID tidak diterima dengan benar
            Toast.makeText(this, "ID not found", Toast.LENGTH_SHORT).show();
            finish(); // Anda mungkin ingin menutup aktivitas ini jika ID tidak ditemukan
        }
    }
    public void getProductById(String productId) {
        String url = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getProductById?id=" + productId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray products = new JSONArray(response);
                            JSONObject productJson = products.getJSONObject(0);

                            String idp = productJson.getString("_id");
                            String name = productJson.getString("name");
                            int price = productJson.getInt("price");
                            String stock = productJson.getString("stock");
                            String description = productJson.getString("description");
                            String formattedHarga = formatToRupiah(price);

                            // Mengambil URL gambar
                            JSONArray images = productJson.getJSONArray("images");
                            String imgUrl = images.getString(0);

                            //taro data di id textview
                            Picasso.get().load(imgUrl).into(foto_produk);
                            nama_produk.setText(name);  // Use 'name' instead of 'formattedHarga'
                            harga_produk.setText(formattedHarga);
                            stok_produk.setText("Stock: " + stock + " Pcs");
                            deskripsi_produk.setText(description);



                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    private String formatToRupiah(int value) {
                        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                        formatRupiah.setCurrency(Currency.getInstance("IDR"));

                        String formattedValue = formatRupiah.format(value).replace("Rp", "").trim();

                        return "Rp. " + formattedValue;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDetail.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
