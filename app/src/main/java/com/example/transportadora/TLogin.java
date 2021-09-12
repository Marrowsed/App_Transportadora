package com.example.transportadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class TLogin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Button login, cadastrar, rastrear;
    EditText user, pass;
    private DrawerLayout drawer;
    ManipulaDB BD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlogin);
        login = findViewById(R.id.btn_login);
        user = findViewById(R.id.edt_user);
        pass = findViewById(R.id.edt_pass);
        cadastrar = findViewById(R.id.btn_forgot);
        rastrear = findViewById(R.id.btn_track);
        BD = new ManipulaDB(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String usuario = user.getText().toString();
                String senha = pass.getText().toString();

                if(usuario.equals("") || senha.equals("")){
                    Toast.makeText(TLogin.this, "Por favor, digite todos os campos", Toast.LENGTH_SHORT).show();
                } else{
                        isUser(usuario, senha);
                }
            }
        });

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TLogin.this, TCadastra.class);
                startActivity(it);
            }
        });

        //Barra de cima
        Toolbar toolbar = findViewById(R.id.toolbar_tlogin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login"); // Name of your Toolbar - Nome da sua Barra de cima

        //Menu lateral
        drawer = findViewById(R.id.drawer_layout_login);

        //Navegação pelo menu
        NavigationView navigationView = findViewById(R.id.nav_view_tlogin);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    //Chamada da função para abertura/fechamento do menu
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_mais:
                String url = "https://www.google.com/search?client=firefox-b-d&q=saiba+mais"; //Link temporário para sua empresa
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.nav_contato:
                String url2 = "https://www.google.com/search?client=firefox-b-d&q=contato"; //Link temporário de contato
                Intent j = new Intent(Intent.ACTION_VIEW);
                j.setData(Uri.parse(url2));
                startActivity(j);
                break;
            case R.id.nav_forgot:
                Intent it = new Intent(TLogin.this, TCadastra.class);
                startActivity(it);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
        }


        public void isUser(String user, String pass) {
            Boolean checa = BD.isUserPass(user, pass);
            if (checa == false) {
                Toast.makeText(TLogin.this, "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
            } else {
                Intent it = new Intent(TLogin.this, TAcesso.class);
                Toast.makeText(TLogin.this, "Acesso permitido", Toast.LENGTH_SHORT).show();
                startActivity(it);
            }
        }
}