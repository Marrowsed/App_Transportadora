package com.example.transportadora;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class TLogin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Button login, rastrear;
    EditText user, pass;
    private DrawerLayout drawer;
    ManipulaDB BD;
    Dados data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tlogin);
        //Font
        Typeface fonte = Typeface.createFromAsset(getAssets(), "fonts/IBMPlexSans-Bold.ttf");

        login = findViewById(R.id.btn_login);
        login.setText("FAÇA LOGIN");
        login.setTypeface(fonte);
        user = findViewById(R.id.edt_user);
        user.setTypeface(fonte);
        pass = findViewById(R.id.edt_pass);
        pass.setTypeface(fonte);
        rastrear = findViewById(R.id.btn_track);
        rastrear.setText("RASTREIE SEU PEDIDO");
        rastrear.setTypeface(fonte);
        BD = new ManipulaDB(this);
        data = new Dados();

        login.setOnClickListener(v -> {
            String usuario = user.getText().toString();
            String senha = pass.getText().toString();
            data.setLogin(usuario);

            if (usuario.equals("") || senha.equals("")) {
                Toast.makeText(TLogin.this, "Por favor, digite todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                isUser(usuario, senha);
            }
        });


        rastrear.setOnClickListener(view -> {
            String uri = "https://www.google.com/search?client=firefox-b-d&q=rastreio+"; //Track Link - Link para rastreio
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse(uri));
            startActivity(it);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_mais:
                String url = "https://www.google.com/search?client=firefox-b-d&q=quem+somos"; //Link temporário para sua empresa
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.nav_contato:
                isWhatsApp();
                break;
            case R.id.nav_forgot:
                Intent cadastra = new Intent(TLogin.this, TCadastra.class);
                startActivity(cadastra);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
        }

        public void isUser(String user, String pass) {
            Boolean checa = BD.isUserPass(user, pass);
            if (!checa) {
                Toast.makeText(TLogin.this, "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
            } else {
                Intent it = new Intent(TLogin.this, TAcesso.class);
                Toast.makeText(TLogin.this, "Acesso permitido", Toast.LENGTH_SHORT).show();
                startActivity(it);
            }
        }

        //Function to call Whatsapp - Função para chamar Whatsapp
    public void isWhatsApp() {
        PackageManager pm = getPackageManager();
        String celular = "00000000000"; //Number of your company - Número da empresa
        try {
            Intent wpIntent = new Intent (Intent.ACTION_SENDTO, Uri.parse("smsto:" + "" + celular + "?body=" + ""));
            wpIntent.setPackage("com.whatsapp");
            startActivity(wpIntent);
        } catch (Exception e) {
            Toast.makeText(TLogin.this, "Redirecionado ao Whatsapp Web !", Toast.LENGTH_SHORT).show();
            String wp = "https://api.whatsapp.com/send?phone=" + celular;
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse(wp));
            startActivity(it);
        }
    }

}