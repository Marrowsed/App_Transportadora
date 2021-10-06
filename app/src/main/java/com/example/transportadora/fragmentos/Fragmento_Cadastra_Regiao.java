package com.example.transportadora.fragmentos;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    ManipulaDB bd;
    Dados data;
    String empresa;
    private RequestQueue mQueue;
    Fragmento_Cadastra_Login fl;

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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Font
        Typeface fonte = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IBMPlexSans-Bold.ttf");

        //View
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

        //Buttons - Botões
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
                    Toast.makeText(getActivity(), "CEP Inválido", Toast.LENGTH_SHORT).show();
                    regiao.setText("");
                    rua.setText("");
                    neigh.setText("");
                    city.setText("");
                    uf.setText("");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError erro) {
                erro.printStackTrace();
                Toast.makeText(getActivity(), "CEP Inválido", Toast.LENGTH_SHORT).show();
                regiao.setText("");
                rua.setText("");
                neigh.setText("");
                city.setText("");
                uf.setText("");
            }
        });
        mQueue.add(request);

    }

    public void atualizaUser(String CNPJ, String volume, String regiao, String categoria){
            Boolean update = bd.atualizaPJ(CNPJ, volume, regiao, categoria);
            if(update == true){
                Toast.makeText(getActivity(), "CNPJ validado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "CNPJ Inválido", Toast.LENGTH_SHORT).show();
            }
            bd.close();
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEmpresa () {
            return empresa;
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
                String CNPJ = data.getCNPJ();
                String vol = volume.getText().toString();
                String reg = regiao.getText().toString();
                String cat = categoria.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("CNPJ", CNPJ);

                if (vol.equals("") || reg.equals("") || cat.equals("")) {
                    Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
                } else if (bd.isDataPJ(CNPJ)) {
                    atualizaUser(CNPJ, vol, reg, cat);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fl = new Fragmento_Cadastra_Login();
                    fl.setArguments(bundle);
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.fragmento_container_regiao, fl);
                    transaction.commit();
                }
                bd.close();
                break;
            case R.id.btn_finalize:

                break;
        }
    }
}


