package com.example.transportadora.fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.transportadora.ManipulaDB;
import com.example.transportadora.R;
import com.example.transportadora.mascara.CodeMask;

import org.json.JSONException;
import org.json.JSONObject;

public class Fragmento_Cadastra_Regiao extends Fragment implements View.OnClickListener {

    public EditText cnpj, razao, volume, regiao, categoria, rua, compl, neigh, city;
    protected Button continua2, valida;
    ManipulaDB bd;
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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String empresa = bundle.getString("CNPJ");
            setEmpresa(empresa);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        cnpj = getView().findViewById(R.id.edt_empresa);
        cnpj.addTextChangedListener(CodeMask.mask(cnpj, CodeMask.FORMAT_CNPJ));
        cnpj.setText(getEmpresa());
        razao = getView().findViewById(R.id.edt_razao);
        volume = getView().findViewById(R.id.edt_volume);
        regiao = getView().findViewById(R.id.edt_cep);
     /*   regiao.addTextChangedListener(CodeMask.mask(regiao
                , CodeMask.FORMAT_CEP));*/
        rua = getView().findViewById(R.id.etd_logradouro);
        compl = getView().findViewById(R.id.edt_complemento);
        neigh = getView().findViewById(R.id.etd_bairro);
        city = getView().findViewById(R.id.edt_cidade);
        categoria = getView().findViewById(R.id.edt_categoria);
        continua2 = getView().findViewById(R.id.btn_regiao);
        valida = getView().findViewById(R.id.btn_cep);
        bd = new ManipulaDB(getActivity());

        Spinner estados = getView().findViewById(R.id.sp_state);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(getActivity(),
                        R.array.states,
                        android.R.layout.simple_spinner_item);
        estados.setAdapter(adapter);

        valida.setOnClickListener(this);

        continua2.setOnClickListener(this);

    }

    private void jsonCEP(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            String jsoncep; // json cep
            String jsonrua; // json logradouro
            String jsonbairro; // json bairro
            String jsoncidade; // json cidade


            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsoncep = response.getString("cep");
                    jsonrua = response.getString("logradouro");
                    jsonbairro = response.getString("bairro");
                    jsoncidade = response.getString("localidade");

                    regiao.setText(jsoncep);
                    rua.setText(jsonrua);
                    neigh.setText(jsonbairro);
                    city.setText(jsoncidade);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "CEP Inválido ! ", Toast.LENGTH_SHORT).show();
                    regiao.setText("");
                    rua.setText("");
                    neigh.setText("");
                    city.setText("");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError erro) {
                erro.printStackTrace();
                Toast.makeText(getActivity(), "CEP Inválido ! ", Toast.LENGTH_SHORT).show();
                regiao.setText("");
                rua.setText("");
                neigh.setText("");
                city.setText("");
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
                String url = "https://viacep.com.br/ws/" + regiao.getText().toString() + "/json/";
                jsonCEP(url);
                break;
            case R.id.btn_regiao:
                String CNPJ = cnpj.getText().toString();
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
        }
    }
}


