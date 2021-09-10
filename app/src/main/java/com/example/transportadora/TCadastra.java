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
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class TCadastra extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    EditText user, pass;
    Button btn_register;
    ManipulaDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcadastra);

        db = new ManipulaDB(this);

        //Barra de cima
        Toolbar toolbar = findViewById(R.id.toolbar_cadastra);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Cadastra"); // Name of your Toolbar - Nome da sua Barra de cima

        //Menu lateral
        drawer = findViewById(R.id.drawer_layout_tcadastra);

        //Navegação pelo menu
        NavigationView navigationView = findViewById(R.id.nav_view_cadastra);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(TCadastra.this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cadastra);
        fab.setOnClickListener(v -> {
            Intent it = new Intent (TCadastra.this, TLogin.class);
            startActivity(it);
        });

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
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}