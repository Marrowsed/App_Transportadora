package com.example.transportadora;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button b1,b2;
    ImageView logo;
    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Font - Fonte
        Typeface fonte = Typeface.createFromAsset(getAssets(), "fonts/IBMPlexSans-Bold.ttf");


        //View
        texto = findViewById(R.id.txt_logo);
        //texto.setText("SUA EMPRESA"); Name of your shipping - Nome de sua transportadora
        texto.setTypeface(fonte);
        logo = findViewById(R.id.img_logo);
        //Logo of your APP - Logo do seu aplicativo

        //Buttons - Botões
        //Registered users button - Botão para usuários cadastrados
        b1 = findViewById(R.id.btn_1);
        b1.setText("Entre Aqui"); //Adjust the name of the button - Ajuste o nome do botão
        b1.setTypeface(fonte);

        //Register button - Botão para cadastrar
        b2 = findViewById(R.id.btn_2);
        b2.setText("Cadastre-se"); //Adjust the name of the button - Ajuste o nome do botão
        b2.setTypeface(fonte);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_1:
                Intent i = new Intent(MainActivity.this, TLogin.class);
                startActivity(i);
                break;
            case R.id.btn_2:
                Intent it = new Intent(MainActivity.this, TCadastra.class);
                startActivity(it);
                break;

        }
    }
}
