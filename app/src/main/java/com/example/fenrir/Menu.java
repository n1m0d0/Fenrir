package com.example.fenrir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Menu extends AppCompatActivity implements View.OnClickListener {
    Button btnOfficial;
    boolean connection;
    Intent letsGo;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnOfficial = findViewById(R.id.btnOfficial);
        btnOfficial.setOnClickListener(this);

        Bundle data = this.getIntent().getExtras();
        token = data.getString("token");

        Tools tools = new Tools(this);
        connection = tools.checkConnection();
        if (connection) {
            Log.w("connection", "yes");
        } else {
            Log.w("connection", "no");
            Toast.makeText(Menu.this, getString(R.string.noConnection), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOfficial:
                letsGo = new Intent(Menu.this, Officials.class);
                letsGo.putExtra("token", token);
                startActivity(letsGo);
                break;
        }
    }
}