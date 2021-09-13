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

import com.example.transportadora.fragmentos.Fragmento_Frete;
import com.example.transportadora.fragmentos.Fragmento_Home;
import com.example.transportadora.fragmentos.Fragmento_Rastreio;
import com.example.transportadora.fragmentos.Fragmento_Registra;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;



public class TAcesso extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tacesso);

        //Barra de cima
        Toolbar toolbar = findViewById(R.id.toolbar_tacesso);
        setSupportActionBar(toolbar);

        //Help button - Botão de Ajuda
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent it = new Intent(TAcesso.this, Ajuda.class);
            startActivity(it);
        });

        //Menu lateral
        drawer = findViewById(R.id.drawer_layout_tacesso);

        //Navegação pelo menu
        NavigationView navigationView = findViewById(R.id.nav_view_tacesso);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tacesso,
                    new Fragmento_Home()).commit();
        }

    }


    //Chamada da função para abertura/fechamento do menu
    public void onBackPressed() {
        moveTaskToBack(true);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    //Navigation implements - Implementação da navegação pelo menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_rastreio:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tacesso,
                        new Fragmento_Rastreio()).commit();
                break;
            case R.id.nav_registre:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tacesso,
                        new Fragmento_Registra()).commit();
                break;
            case R.id.nav_frete:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tacesso,
                        new Fragmento_Frete()).commit();
                break;
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
