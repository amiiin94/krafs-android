package com.example.krafs1;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterPage extends AppCompatActivity {

    private MaterialButton register;
    private EditText username, email, notelp, password;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        register = findViewById(R.id.register);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        notelp = findViewById(R.id.notelp);
        password = findViewById(R.id.password);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = username.getText().toString();
                String enteredEmail = email.getText().toString();
                String enteredNotelp = notelp.getText().toString();
                String enteredPassword = password.getText().toString();

                registerUser(enteredUsername, enteredEmail, enteredNotelp, enteredPassword);
            }
        });



    }

    public void registerUser(String username, String email, String notelp, String password) {
        //String hashedPassword = BCrypt.hashpw(password.toString(), BCrypt.gensalt());
        //MD5 Hashed
        String encryptedInputPassword = MD5Encryption.encrypt(password.toString());

        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/insertUser" +
                "?username=" + username +
                "&email=" + email +
                "&notelp=" + notelp +
                "&password=" + encryptedInputPassword;

        StringRequest sr = new StringRequest(
                Request.Method.POST,
                urlEndPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            // Check if the response contains an error field
                            if (jsonResponse.has("error")) {
                                String errorMessage = jsonResponse.getString("error");
                                // Display toast with the error message
                                Toast.makeText(RegisterPage.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                // Registration successful
                                Toast.makeText(RegisterPage.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                Intent loginIntent = new Intent(RegisterPage.this, LoginPage.class);
                                startActivity(loginIntent);
                            }
                        } catch (JSONException e) {
                            // Handle JSON parsing error
                            e.printStackTrace();
                            Toast.makeText(RegisterPage.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterPage.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(sr);
    }

    public static class MD5Encryption {
        public static String encrypt(String password) {
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                digest.update(password.getBytes());
                byte messageDigest[] = digest.digest();

                // Create Hex String
                StringBuilder hexString = new StringBuilder();
                for (byte aMessageDigest : messageDigest) {
                    String h = Integer.toHexString(0xFF & aMessageDigest);
                    while (h.length() < 2)
                        h = "0" + h;
                    hexString.append(h);
                }
                return hexString.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }

        public static void main(String[] args) {
            String password = "myPassword";
            System.out.println("Original password: " + password);
            System.out.println("Encrypted password: " + encrypt(password));
        };
    };
}
