package com.example.fenrir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText etUser, etPassword;
    Button btnLogin;
    boolean connection;
    Intent letsGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        Tools tools = new Tools(this);
        connection = tools.checkConnection();
        if (connection) {
            Log.w("connection", "yes");
        } else {
            Log.w("connection", "no");
            Toast.makeText(Login.this, getString(R.string.noConnection), Toast.LENGTH_LONG).show();
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUser.getText().toString().trim().equals("") || etPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(Login.this, getString(R.string.invalidData), Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jsonUser = new JSONObject();
                    try {
                        jsonUser.put("email", etUser.getText().toString().trim() + "@minsalud.gob.bo");
                        jsonUser.put("password", etPassword.getText().toString().trim());
                        Log.w("jsonObject", jsonUser.toString());
                        loginUser(jsonUser);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void loginUser(JSONObject jsonUser) {
        String url = getString(R.string.urlSite) + "login";
        Log.w("url", url);
        RequestQueue requestQueue = new Volley().newRequestQueue(Login.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonUser, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w("response", response.toString());
                try {
                    if (response.getBoolean("res")) {
                        letsGo = new Intent(Login.this, Menu.class);
                        letsGo.putExtra("token", response.getString("token"));
                        Toast.makeText(Login.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        startActivity(letsGo);
                    } else {
                        Toast.makeText(Login.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("content-type", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}