package com.example.transportadora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button b1,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = findViewById(R.id.btn_1);
        b2 = findViewById(R.id.btn_2);

        //Registered users button - Botão para usuários cadastrados
        b1.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, TLogin.class);
            startActivity(i);
        });

        //Link for your website - Link para o site da empresa
        b2.setOnClickListener(v -> {
            String url = "https://www.google.com/search?client=firefox-b-d&q=seu+site";
            Intent j = new Intent(Intent.ACTION_VIEW);
            j.setData(Uri.parse(url));
            startActivity(j);
        });
    }

}
