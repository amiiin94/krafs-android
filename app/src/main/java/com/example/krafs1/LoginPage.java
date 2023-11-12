package com.example.krafs1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class LoginPage extends AppCompatActivity {
    private Button loginBtn, signupBtn;
    private EditText email;
    private EditText password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        loginBtn = findViewById(R.id.loginBtn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signupBtn = findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        signupBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(signupIntent);
            }
        }));
    }


    public void login() {
        String hashedPassword = BCrypt.hashpw(password.getText().toString(), BCrypt.gensalt());

        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/getUserbyemailandPassword?email=" + email.getText().toString() + "&password=" + password.getText().toString();

        StringRequest sr = new StringRequest(
                Request.Method.GET,
                urlEndPoints,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("LoginPage", "Raw Server Response: " + response);

                        try {

                            JSONObject userJson = new JSONObject(response);

                            String username = userJson.getString("username");
                            String email = userJson.getString("email");
                            String notelp = userJson.getString("notelp");
                            String password = userJson.getString("password");

                            // Store user data in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", username);
                            editor.putString("email", email);
                            editor.putString("notelp", notelp);
                            editor.putString("password", password);
                            editor.apply();

                            Toast.makeText(LoginPage.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            Intent profileIntent = new Intent(LoginPage.this, ProfilePage.class);
                            startActivity(profileIntent);
                        } catch (JSONException e) {
                            Toast.makeText(LoginPage.this, "Login Unsuccessful!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginPage.this, error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }
}
