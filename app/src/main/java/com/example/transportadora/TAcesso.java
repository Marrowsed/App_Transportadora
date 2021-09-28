package com.example.transportadora;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.transportadora.fragmentos.Fragmento_Conta;
import com.example.transportadora.fragmentos.Fragmento_Frete;
import com.example.transportadora.fragmentos.Fragmento_Rastreio;
import com.example.transportadora.fragmentos.Fragmento_Registra;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;



public class TAcesso extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    TextView home;
    String Login;
    ManipulaDB bd;
    Fragmento_Conta fc;

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

        bd = new ManipulaDB(this);

        Intent bundleit = getIntent();
        if (bundleit != null) {
            String Login = bundleit.getStringExtra("Login");
            setLogin(Login);
        }

        home = findViewById(R.id.txt_home);
        String usuario = getLogin();
        home.setText("Olá, " + bd.getNome(bd.getCNPJ(usuario)) + " !");

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
                String url = "https://www.google.com/search?client=firefox-b-d&q=quem+somos"; //Link temporário para sua empresa
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.nav_contato:
                isWhatsApp();
                break;
            case R.id.nav_conta:
                Bundle bundle = new Bundle();
                bundle.putString("Login", getLogin());
                fc = new Fragmento_Conta();
                fc.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_tacesso,
                        fc).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void isWhatsApp() {
        PackageManager pm = getPackageManager();
        String celular = "00000000000"; //Number of your company - Número da empresa
        try {
            Intent wpIntent = new Intent (Intent.ACTION_SENDTO, Uri.parse("smsto:" + "" + celular + "?body=" + ""));
            wpIntent.setPackage("com.whatsapp");
            startActivity(wpIntent);
        } catch (Exception e) {
            Toast.makeText(TAcesso.this, "Redirecionado ao Whatsapp Web !", Toast.LENGTH_SHORT).show();
            String wp = "https://api.whatsapp.com/send?phone=" + celular;
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse(wp));
            startActivity(it);
        }
    }

    public void setLogin(String Login){
        this.Login = Login;
    }

    public String getLogin(){
        return Login;
    }
}
