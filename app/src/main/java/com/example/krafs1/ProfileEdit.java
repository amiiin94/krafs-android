package com.example.krafs1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileEdit extends AppCompatActivity {

    private TextView edit_nama, edit_notelp;
    private Button save;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_page);

        //get data from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "");
        String notelp = sharedPreferences.getString("notelp", "");

        //Cari ID
        edit_nama = findViewById(R.id.edit_nama);
        edit_notelp = findViewById(R.id.edit_notelp);
        save = findViewById(R.id.save);

        //Masukkan data ke EditText
        edit_nama.setText(username);
        edit_notelp.setText(notelp);

        save.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfile();
            }
        }));
    }

    public void EditProfile() {
        // Get updated data from EditText
        String updatedUsername = edit_nama.getText().toString().trim();
        String updatedNotelp = edit_notelp.getText().toString().trim();

        // Get user email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");

        // Construct the URL
        String urlEndPoints = "https://ap-southeast-1.aws.data.mongodb-api.com/app/application-0-iyoxv/endpoint/editProfileByEmail?email=" + userEmail + "&username=" +updatedUsername + "&notelp=" + updatedNotelp;

        // Create a JSON object with the updated data
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("username", updatedUsername);
            requestData.put("notelp", updatedNotelp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create a JsonObjectRequest with PUT method
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                urlEndPoints,
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Update data in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", updatedUsername);
                        editor.putString("notelp", updatedNotelp);
                        editor.apply();

                        Toast.makeText(ProfileEdit.this, "Update Profile successful!", Toast.LENGTH_SHORT).show();

                        //Update chatadapter


                        // Redirect to the profile page
                        Intent profileIntent = new Intent(ProfileEdit.this, ProfilePage.class);
                        startActivity(profileIntent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileEdit.this, "Error updating profile: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

}