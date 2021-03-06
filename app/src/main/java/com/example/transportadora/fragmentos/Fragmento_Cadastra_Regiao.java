package com.example.transportadora.fragmentos;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.transportadora.Dados;
import com.example.transportadora.ManipulaDB;
import com.example.transportadora.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Fragmento_Cadastra_Regiao extends Fragment implements View.OnClickListener {

    public EditText volume, regiao, categoria, rua, compl, neigh, city, uf;
    protected Button continua2, valida, finalize;
    TextView etapa1, etapa2;
    ProgressBar barra1, barra2;
    LinearLayout linha1, linha2;
    ManipulaDB bd;
    Dados data;
    String CNPJ;
    private RequestQueue mQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragmento_cadastra_regiao, container, false);
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getActivity());
        data = new Dados();
        bd = new ManipulaDB(getActivity());
        CNPJ = data.getCNPJ();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Font
        Typeface fonte = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IBMPlexSans-Bold.ttf");

        //View
        linha1 = getView().findViewById(R.id.linha1_r);
        linha2 = getView().findViewById(R.id.linha2_r);
        barra1 = getView().findViewById(R.id.barra_progresso);
        barra2 = getView().findViewById(R.id.barra_progresso_1);
        etapa1 = getView().findViewById(R.id.txt_etapa1);
        etapa2 = getView().findViewById(R.id.txt_etapa1_1);
        regiao = getView().findViewById(R.id.edt_cep);
        regiao.setTypeface(fonte);
     /*   regiao.addTextChangedListener(CodeMask.mask(regiao
                , CodeMask.FORMAT_CEP));*/
        rua = getView().findViewById(R.id.etd_logradouro);
        rua.setTypeface(fonte);
        compl = getView().findViewById(R.id.edt_complemento);
        compl.setTypeface(fonte);
        neigh = getView().findViewById(R.id.etd_bairro);
        neigh.setTypeface(fonte);
        city = getView().findViewById(R.id.edt_cidade);
        city.setTypeface(fonte);
        uf = getView().findViewById(R.id.edt_uf);
        uf.setTypeface(fonte);
        categoria = getView().findViewById(R.id.edt_categoria);
        categoria.setTypeface(fonte);
        volume = getView().findViewById(R.id.edt_volume);
        volume.setTypeface(fonte);

        //Buttons - Bot??es
        continua2 = getView().findViewById(R.id.btn_regiao);
        valida = getView().findViewById(R.id.btn_cep);
        finalize = getView().findViewById(R.id.btn_finalize);


        valida.setOnClickListener(this);
        continua2.setOnClickListener(this);
        finalize.setOnClickListener(this);

    }

    private void jsonCEP(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            String jsoncep; // json cep
            String jsonrua; // json logradouro
            String jsonbairro; // json bairro
            String jsoncidade; // json cidade
            String jsonuf; // json uf;


            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsoncep = response.getString("cep");
                    jsonrua = response.getString("logradouro");
                    jsonbairro = response.getString("bairro");
                    jsoncidade = response.getString("localidade");
                    jsonuf = response.getString("uf");

                    regiao.setText(jsoncep);
                    rua.setText(jsonrua);
                    neigh.setText(jsonbairro);
                    city.setText(jsoncidade);
                    uf.setText(jsonuf);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "CEP Inv??lido", Toast.LENGTH_SHORT).show();
                    regiao.setText("");
                    rua.setText("");
                    neigh.setText("");
                    city.setText("");
                    uf.setText("");
                }
            }
        }, erro -> {
            erro.printStackTrace();
            Toast.makeText(getActivity(), "CEP Inv??lido", Toast.LENGTH_SHORT).show();
            regiao.setText("");
            rua.setText("");
            neigh.setText("");
            city.setText("");
            uf.setText("");
        });
        mQueue.add(request);

    }

    public void atualizaUser(String CNPJ, String volume, String regiao, String categoria){
            Boolean update = bd.atualizaPJ(CNPJ, volume, regiao, categoria);
            if(update){
                Toast.makeText(getActivity(), "CNPJ validado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "CNPJ Inv??lido", Toast.LENGTH_SHORT).show();
            }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cep:
                String cep = regiao.getText().toString();
                String regex = cep.replaceAll("[-]","");
                String url = "https://viacep.com.br/ws/" + regex + "/json/";
                jsonCEP(url);
                break;
            case R.id.btn_regiao:
                String reg = regiao.getText().toString();

                if (reg.equals("")) {
                    Toast.makeText(getActivity(), "CEP Inv??liddo", Toast.LENGTH_SHORT).show();
                } else if (bd.isDataPJ(CNPJ)) {
                    etapa1.setVisibility(View.GONE);
                    barra1.setVisibility(View.GONE);
                    etapa2.setVisibility(View.VISIBLE);
                    barra2.setVisibility(View.VISIBLE);
                    continua2.setVisibility(View.GONE);
                    finalize.setVisibility(View.VISIBLE);
                    linha1.setVisibility(View.GONE);
                    linha2.setVisibility(View.GONE);
                    categoria.setVisibility(View.VISIBLE);
                    volume.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_finalize:
                String vol = volume.getText().toString();
                String reg2 = regiao.getText().toString();
                String cat = categoria.getText().toString();
                if (vol.equals("") || cat.equals("")) {
                    Toast.makeText(getActivity(), "Dados inv??lidos", Toast.LENGTH_SHORT).show();
                } else {
                    atualizaUser(CNPJ, vol, reg2, cat);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fragmento_container_regiao, new Fragmento_Cadastra_Login());
                    transaction.commit();
                }
                break;
        }
    }
}


