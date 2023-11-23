package com.example.krafs1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class MerchantPage extends AppCompatActivity {
    private Button AllMerch;
    private LinearLayout homepage, navforum, navarticle, navprofile;
    private List<Product> productList;
    private List<Category> categoryList;
    private RecyclerView rv1;
    private RecyclerView rvButton;
    private EditText editTextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_page);

        rv1 = findViewById(R.id.rv1);
        rv1.setLayoutManager(new GridLayoutManager(this, 2));
        int horizontalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_horizontal);
        int verticalSpace = getResources().getDimensionPixelSize(R.dimen.space_between_cards_vertical);
        rv1.addItemDecoration(new SpaceItemDecoration(this, horizontalSpace, verticalSpace));

        editTextText = findViewById(R.id.editTextText);

        rvButton = findViewById(R.id.rvButton);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvButton.setLayoutManager(layoutManager);


        productList = new ArrayList<>();
        categoryList = new ArrayList<>();

        homepage = findViewById(R.id.homepage);
        navforum = findViewById(R.id.navforum);
        navarticle = findViewById(R.id.navarticle);
        navprofile = findViewById(R.id.navprofile);

        AllMerch = findViewById(R.id.AllMerch);

        getAllProducts();
        getAllCategory();

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(MerchantPage.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

        navforum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(MerchantPage.this, ForumPage.class);
                startActivity(homeIntent);
            }
        });

        navarticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent articleIntent = new Intent(MerchantPage.this, ArticlePage.class);
                startActivity(articleIntent);
            }
        });
        navprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

                if (sharedPreferences.getString("username", null) == null) {
                    Intent loginIntent = new Intent(MerchantPage.this, LoginPage.class);
                    startActivity(loginIntent);
                } else {
                    Intent profileIntent = new Intent(MerchantPage.this, ProfilePage.class);
                    startActivity(profileIntent);
                }
            }
        });
        AllMerch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                productList.clear();
                getAllProducts();
            }
        });

        editTextText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Panggil fungsi atau kode yang ingin Anda jalankan
                    String searchName = editTextText.getText().toString();
                    getProductsByName(searchName);
                    return true;

                }
                return false;
            }
        });
        // Add any code specific to this activity here
    }

    public void getAllProducts() {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getAllProducts";

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray products = new JSONArray(response);
                            for (int i = 0; i < products.length(); i++) {
                                JSONObject productJson = products.getJSONObject(i);

                                String idp = productJson.getString("_id");
                                String nama = productJson.getString("name");
                                int price = productJson.getInt("price");
                                String formattedHarga = formatToRupiah(price);

                                // Mengambil URL gambar
                                JSONArray images = productJson.getJSONArray("images");
                                String imgUrl = images.getString(0);

                                // Menambahkan produk ke dalam productList
//
                                Product product = new Product(idp, nama, formattedHarga, imgUrl);

//                                List<Product> productList = new ArrayList<>();
                                productList.add(product);
                            }
                            displayProducts();
                        }catch (JSONException e) {
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
                        Toast.makeText(MerchantPage.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }
    private void displayProducts() {
        ProductAdapter productAdapter = new ProductAdapter(productList);
        rv1.setAdapter(productAdapter);
    }

    public static class Product {
        private  String idp;
        private String name;
        private String price;
        private String imageUrl;

        public Product(String idp ,String name, String price, String imageUrl) {
            this.idp = idp;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
        }

        public  String getIdp() {return idp;}

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

//    CATEGORY

    public void getAllCategory () {
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getAllCategories";

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray categories = new JSONArray(response);
                            for (int i = 0; i < categories.length(); i++) {
                                JSONObject categoryJson = categories.getJSONObject(i);

                                String idc = categoryJson.getString("_id");
                                String nama = categoryJson.getString("name");

                                Category category = new Category(idc, nama);
                                categoryList.add(category);
                            }
                            displayCategory();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MerchantPage.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }

    private void displayCategory() {
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList, this);
        rvButton.setAdapter(categoryAdapter);
    }

    public static class Category {
        private String idc;
        private String name;

        public Category(String idc ,String name) {
            this.idc = idc;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getIdc() {
            return idc;
        }
    }
//    Sort Category

    public void getProductByCategory(String categoryId) {
        String url = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getProductByCategory?category=" + categoryId;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray products = new JSONArray(response);

                            productList.clear();

                            for (int i = 0; i < products.length(); i++) {
                                JSONObject productJson = products.getJSONObject(i);

                                String idp = productJson.getString("_id");
                                String nama = productJson.getString("name");
                                int price = productJson.getInt("price");
                                String formattedHarga = formatToRupiah(price);

                                // Mengambil URL gambar
                                JSONArray images = productJson.getJSONArray("images");
                                String imgUrl = images.getString(0);

                                Product product  = new Product (idp, nama, formattedHarga, imgUrl);
                                productList.add(product);
                            }
                            displayProducts();
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
                        Toast.makeText(MerchantPage.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
//    public void onCategoryItemClick(String categoryId) {
//        getProductByCategory(categoryId);
//    }

    public void getProductsByName(String searchName) {
        String url = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getProductByNama?name="+searchName;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray products = new JSONArray(response);

                            productList.clear();

                            for (int i = 0; i < products.length(); i++) {
                                JSONObject productJson = products.getJSONObject(i);

                                String idp = productJson.getString("_id");
                                String nama = productJson.getString("name");
                                int price = productJson.getInt("price");
                                String formattedHarga = formatToRupiah(price);

                                // Mengambil URL gambar
                                JSONArray images = productJson.getJSONArray("images");
                                String imgUrl = images.getString(0);

                                Product product  = new Product (idp, nama, formattedHarga, imgUrl);
                                productList.add(product);
                            }
                            displayProducts();
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
                        Toast.makeText(MerchantPage.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}