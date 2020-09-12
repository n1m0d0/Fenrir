package com.example.fenrir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Officials extends AppCompatActivity {
    boolean connection;
    Intent letsGo;
    String token;
    LinearLayout llListOfficials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officials);

        llListOfficials = findViewById(R.id.llListOfficials);

        Bundle data = this.getIntent().getExtras();
        token = data.getString("token");

        Tools tools = new Tools(this);
        connection = tools.checkConnection();
        if (connection) {
            Log.w("connection", "yes");
            JSONObject jsonOfficials = new JSONObject();
            try {
                jsonOfficials.put("api_token", token);
                Log.w("data", jsonOfficials.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lListOfficials(jsonOfficials);
        } else {
            Log.w("connection", "no");
            Toast.makeText(Officials.this, getString(R.string.noConnection), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void lListOfficials(JSONObject jsonOfficials) {
        String url = getString(R.string.urlSite) + "funcionarios";
        Log.w("url", url);
        RequestQueue requestQueue = new Volley().newRequestQueue(Officials.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonOfficials, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.w("response", response.toString());

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