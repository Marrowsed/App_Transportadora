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
            Intent i = new Intent(MainActivity.this, TAcesso.class);
            startActivity(i);
        });

        //Link for your website - Link para o site da empresa
        b2.setOnClickListener(v -> {
            String url = "https://www.google.com/search?q=cadastre+sua+empresa&client=firefox-b-d&ei=dE0xYcmaGpq85OUPyr-nwAs&oq=cadastre+sua+empresa&gs_lcp=Cgdnd3Mtd2l6EAMyBQgAEIAEMgUIABCABDIFCAAQgAQyBQgAEIAEMgYIABAWEB4yBggAEBYQHjIGCAAQFhAeMgYIABAWEB4yBggAEBYQHjIGCAAQFhAeOgcIABBHELADOgcIABCwAxBDOhMILhCxAxCDARDHARCjAhBDEJMCOgsIABCABBCxAxCDAToKCAAQsQMQgwEQQzoLCC4QgAQQsQMQgwE6EQguEIAEELEDEIMBEMcBEKMCOg4ILhCABBCxAxDHARCjAjoFCC4QgAQ6BAgAEEM6CAgAEIAEELEDOhEILhCABBCxAxCDARDHARCvAToICC4QsQMQgwE6DgguEIAEELEDEMcBEK8BOg4ILhCxAxCDARDHARCvAToLCC4QgAQQxwEQrwE6DgguEIAEELEDEMcBENEDOg0ILhCxAxDHARCjAhBDOgcIABCxAxBDOgQILhBDOgsIABCxAxCDARDJAzoHCAAQgAQQCkoECEEYAFCaIljaP2DwQGgDcAJ4AIABgAGIAaESkgEENy4xNZgBAKABAcgBCcABAQ&sclient=gws-wiz&ved=0ahUKEwjJpPfgqOHyAhUaHrkGHcrfCbgQ4dUDCA0&uact=5";
            Intent j = new Intent(Intent.ACTION_VIEW);
            j.setData(Uri.parse(url));
            startActivity(j);
        });
    }

}