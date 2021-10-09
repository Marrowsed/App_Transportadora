package com.example.transportadora.fragmentos;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class Fragmento_Dados_Cadastrais extends Fragment implements View.OnClickListener {

    EditText user, pass, confirmaPass, cep, rua, numero, complemento, bairro, cidade;
    Button editaUser, confirmaEdita, editaFatura, confirmaFatura, validaCEP, zerar;
    LinearLayout linha1, linha2, linha3;
    ManipulaDB bd;
    Dados data;
    String login;
    private RequestQueue mQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Dados Cadastrais");
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragmento_dados_cadastrais, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getActivity());
        data = new Dados();
        bd = new ManipulaDB(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Dados do Usuário
        user = getView().findViewById(R.id.conta_user);
        pass = getView().findViewById(R.id.conta_pass);
        confirmaPass = getView().findViewById(R.id.conta_pass2);
        editaUser = getView().findViewById(R.id.btn_edt_user);
        confirmaEdita = getView().findViewById(R.id.btn_cad_confirma);

        //Dados de Fatura
        editaFatura = getView().findViewById(R.id.btn_edt_fatura);
        confirmaFatura = getView().findViewById(R.id.btn_confirma_fat);
        cep = getView().findViewById(R.id.conta_cep);
        validaCEP = getView().findViewById(R.id.btn_conta_cep);

        linha1 = getView().findViewById(R.id.linha1);
        rua = getView().findViewById(R.id.conta_rua);
        numero = getView().findViewById(R.id.conta_numero);

        linha2 = getView().findViewById(R.id.linha2);
        complemento = getView().findViewById(R.id.conta_complemento);
        bairro = getView().findViewById(R.id.conta_bairro);

        linha3 = getView().findViewById(R.id.linha3);
        cidade = getView().findViewById(R.id.conta_cidade);

        Spinner estados = getView().findViewById(R.id.conta_estado);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(getActivity(),
                        R.array.states,
                        android.R.layout.simple_spinner_item);
        estados.setAdapter(adapter);

        //Botões voltar
        zerar = getView().findViewById(R.id.btn_zerar);



        editaUser.setOnClickListener(this);
        confirmaEdita.setOnClickListener(this);
        editaFatura.setOnClickListener(this);
        confirmaFatura.setOnClickListener(this);
        zerar.setOnClickListener(this);
        validaCEP.setOnClickListener(this);



    }

    @Override
    public void onClick (View view){
        switch (view.getId()) {
            case R.id.btn_edt_user:
                Button checa = getView().findViewById(R.id.btn_confirma_fat);
                if(checa.getVisibility() == View.GONE) {
                    Button btn = getView().findViewById(R.id.btn_edt_user);
                    btn.setVisibility(View.GONE);
                    EditText user_1 = getView().findViewById(R.id.conta_user);
                    user_1.setVisibility(View.VISIBLE);
                    user_1.setText(getLogin());
                    EditText pass_1 = getView().findViewById(R.id.conta_pass);
                    pass_1.setVisibility(View.VISIBLE);
                    EditText confirmaPass_1 = getView().findViewById(R.id.conta_pass2);
                    confirmaPass_1.setVisibility(View.VISIBLE);
                    Button btn2 = getView().findViewById(R.id.btn_cad_confirma);
                    btn2.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Termine a ação anterior ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_cad_confirma:
                Button btn_1 = getView().findViewById(R.id.btn_edt_user);
                Button btn2_1 = getView().findViewById(R.id.btn_cad_confirma);
                EditText user_2 = getView().findViewById(R.id.conta_user);
                EditText pass_2 = getView().findViewById(R.id.conta_pass);
                EditText confirmaPass_2 = getView().findViewById(R.id.conta_pass2);
                String usuario = user_2.getText().toString();
                String senha = pass_2.getText().toString();
                String confirma = confirmaPass_2.getText().toString();

                if (senha.equals("") || confirma.equals("")) {
                    Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
                } else if (confirmaPass(senha, confirma) == true) {
                    Log.i("Msg", "senha:" + senha + " e " + confirma);
                    if(!bd.isUserPass(usuario, senha)){
                        redefineSenha(usuario, senha);
                        pass_2.setText("");
                        confirmaPass_2.setText("");
                        btn_1.setVisibility(View.VISIBLE);
                        btn2_1.setVisibility(View.GONE);
                        user_2.setVisibility(View.GONE);
                        pass_2.setVisibility(View.GONE);
                        confirmaPass_2.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), "Senha em uso", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Senha inválida", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edt_fatura:
                Button checa2 = getView().findViewById(R.id.btn_cad_confirma);
                if(checa2.getVisibility() == View.GONE) {
                    Button btn3 = getView().findViewById(R.id.btn_edt_fatura);
                    Button btn4 = getView().findViewById(R.id.btn_confirma_fat);
                    Button valida = getView().findViewById(R.id.btn_conta_cep);
                    LinearLayout linha1 = getView().findViewById(R.id.linha1);
                    LinearLayout linha2 = getView().findViewById(R.id.linha2);
                    LinearLayout linha3 = getView().findViewById(R.id.linha3);
                    EditText CEP = getView().findViewById(R.id.conta_cep);
                    btn3.setVisibility(View.GONE);
                    btn4.setVisibility(View.VISIBLE);
                    valida.setVisibility(View.VISIBLE);
                    linha1.setVisibility(View.VISIBLE);
                    linha2.setVisibility(View.VISIBLE);
                    linha3.setVisibility(View.VISIBLE);
                    CEP.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Termine a ação anterior ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_confirma_fat:
                String CNPJ = bd.getCNPJ(user.getText().toString());
                String Cep = cep.getText().toString();
                if(bd.updateRegiao(CNPJ,Cep)){
                    Toast.makeText(getActivity(),"CEP Atualizado", Toast.LENGTH_SHORT).show();
                    Button btn3_1 = getView().findViewById(R.id.btn_edt_fatura);
                    Button btn4_1 = getView().findViewById(R.id.btn_confirma_fat);
                    Button valida_1 = getView().findViewById(R.id.btn_conta_cep);
                    LinearLayout linha1_1 = getView().findViewById(R.id.linha1);
                    LinearLayout linha2_1 = getView().findViewById(R.id.linha2);
                    LinearLayout linha3_1 = getView().findViewById(R.id.linha3);
                    EditText CEP_1 = getView().findViewById(R.id.conta_cep);
                    btn3_1.setVisibility(View.VISIBLE);
                    btn4_1.setVisibility(View.GONE);
                    valida_1.setVisibility(View.GONE);
                    linha1_1.setVisibility(View.GONE);
                    linha2_1.setVisibility(View.GONE);
                    linha3_1.setVisibility(View.GONE);
                    CEP_1.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(),"CEP Incorreto", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_zerar:
                Button btn_u = getView().findViewById(R.id.btn_edt_user);
                btn_u.setVisibility(View.VISIBLE);
                EditText user_u = getView().findViewById(R.id.conta_user);
                user_u.setVisibility(View.GONE);
                EditText pass_u = getView().findViewById(R.id.conta_pass);
                pass_u.setVisibility(View.GONE);
                EditText confirmaPass_u = getView().findViewById(R.id.conta_pass2);
                confirmaPass_u.setVisibility(View.GONE);
                Button btn2_u = getView().findViewById(R.id.btn_cad_confirma);
                btn2_u.setVisibility(View.GONE);
                Button btn3_f = getView().findViewById(R.id.btn_edt_fatura);
                btn3_f.setVisibility(View.VISIBLE);
                Button btn4_f = getView().findViewById(R.id.btn_confirma_fat);
                btn4_f.setVisibility(View.GONE);
                Button valida_f = getView().findViewById(R.id.btn_conta_cep);
                valida_f.setVisibility(View.GONE);
                LinearLayout linha1_f = getView().findViewById(R.id.linha1);
                linha1_f.setVisibility(View.GONE);
                LinearLayout linha2_f = getView().findViewById(R.id.linha2);
                linha2_f.setVisibility(View.GONE);
                LinearLayout linha3_f = getView().findViewById(R.id.linha3);
                linha3_f.setVisibility(View.GONE);
                EditText CEP_f = getView().findViewById(R.id.conta_cep);
                CEP_f.setVisibility(View.GONE);
                break;
            case R.id.btn_conta_cep: //Valida CEP
                String url = "https://viacep.com.br/ws/" + cep.getText().toString() + "/json/";
                jsonCEP(url);
                break;
        }

    }

    public String getLogin () {
        return login;
    }

    public void setLogin (String login){
        this.login = login;
    }

    public Boolean confirmaPass(String senha, String confirma) {
        boolean confirmar = senha.equals(confirma);
        return confirmar == true;
    }

    public void redefineSenha(String user, String pass) {
        if (bd.updatePass(user, pass)) {
            Toast.makeText(getActivity(), "Senha atualizada com sucesso !", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Credenciais inválidas !", Toast.LENGTH_SHORT).show();
        }
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

                    cep.setText(jsoncep);
                    rua.setText(jsonrua);
                    bairro.setText(jsonbairro);
                    cidade.setText(jsoncidade);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "CEP Inválido ! ", Toast.LENGTH_SHORT).show();
                    cep.setText("");
                    rua.setText("");
                    bairro.setText("");
                    cidade.setText("");
                }
            }
        }, erro -> {
            erro.printStackTrace();
            Toast.makeText(getActivity(), "CEP Inválido ! ", Toast.LENGTH_SHORT).show();
            cep.setText("");
            rua.setText("");
            bairro.setText("");
            cidade.setText("");
        });

        mQueue.add(request);

    }
}
