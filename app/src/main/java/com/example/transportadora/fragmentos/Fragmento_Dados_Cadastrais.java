package com.example.transportadora.fragmentos;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    EditText user, senha, pass, confirmaPass, cep, rua, numero, complemento, bairro, cidade, estado;
    Button editaUser, editaPass, confirmaEdita, editaFatura, confirmaFatura, validaCEP, zerar;
    LinearLayout linha1;
    ManipulaDB bd;
    Dados data;
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

        //Font
        Typeface fonte = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IBMPlexSans-Bold.ttf");

        // Dados do Usuário
        user = getView().findViewById(R.id.conta_user);
        user.setTypeface(fonte);
        senha = getView().findViewById(R.id.conta_senha);
        senha.setTypeface(fonte);
        pass = getView().findViewById(R.id.conta_pass);
        pass.setTypeface(fonte);
        confirmaPass = getView().findViewById(R.id.conta_pass2);
        confirmaPass.setTypeface(fonte);
        editaPass = getView().findViewById(R.id.btn_edit_confirma);
        editaPass.setText("TROQUE A SENHA");
        editaPass.setTypeface(fonte);
        editaUser = getView().findViewById(R.id.btn_edt_user);
        editaUser.setText("DADOS DE LOGIN");
        editaUser.setTypeface(fonte);
        confirmaEdita = getView().findViewById(R.id.btn_cad_confirma);
        confirmaEdita.setText("CONFIRMAR");
        confirmaEdita.setTypeface(fonte);

        //Dados de Fatura
        editaFatura = getView().findViewById(R.id.btn_edt_fatura);
        editaFatura.setText("DADOS DE FATURAMENTO");
        editaFatura.setTypeface(fonte);
        confirmaFatura = getView().findViewById(R.id.btn_confirma_fat);
        confirmaFatura.setText("CONFIRMAR");
        confirmaFatura.setTypeface(fonte);
        cep = getView().findViewById(R.id.conta_cep);
        cep.setTypeface(fonte);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            if (!cep.getText().toString().equals(getCEP())){
                String CEP = getCEP().replaceAll("[-]","");
                cep.setText(CEP);
                String url = "https://viacep.com.br/ws/" + cep.getText().toString() + "/json/";
                jsonCEP(url);
            }
        });
        validaCEP = getView().findViewById(R.id.btn_conta_cep);
        validaCEP.setText("ALTERAR O CEP");
        validaCEP.setTypeface(fonte);

        //Dados de Região
        linha1 = getView().findViewById(R.id.linha1);
        rua = getView().findViewById(R.id.conta_rua);
        rua.setTypeface(fonte);
        numero = getView().findViewById(R.id.conta_numero);
        numero.setTypeface(fonte);
        complemento = getView().findViewById(R.id.conta_complemento);
        complemento.setTypeface(fonte);
        bairro = getView().findViewById(R.id.conta_bairro);
        bairro.setTypeface(fonte);
        cidade = getView().findViewById(R.id.conta_cidade);
        cidade.setTypeface(fonte);
        estado = getView().findViewById(R.id.conta_estado);
        estado.setTypeface(fonte);


        //Botões voltar
        zerar = getView().findViewById(R.id.btn_zerar);

        editaUser.setOnClickListener(this);
        editaPass.setOnClickListener(this);
        confirmaEdita.setOnClickListener(this);
        editaFatura.setOnClickListener(this);
        confirmaFatura.setOnClickListener(this);
        zerar.setOnClickListener(this);
        validaCEP.setOnClickListener(this);
        senha.setOnClickListener(this);
    }

    @Override
    public void onClick (View view){
        switch (view.getId()) {
            case R.id.conta_senha:
                if(senha.getText().toString().equals(bd.getPass(data.getLogin()))){
                    senha.setText("Clique para ver sua senha");
                } else {
                    senha.setText(bd.getPass(data.getLogin()));
                }
            break;
            case R.id.btn_edt_user:
                Button checa = getView().findViewById(R.id.btn_confirma_fat);
                if(checa.getVisibility() == View.GONE) {
                    editaUser.setVisibility(View.GONE);
                    user.setVisibility(View.VISIBLE);
                    user.setText(data.getLogin());
                    senha.setVisibility(View.VISIBLE);
                    senha.setText("Clique para ver sua senha");
                    editaPass.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Termine a ação anterior ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit_confirma:
                senha.setVisibility(View.GONE);
                editaPass.setVisibility(View.GONE);
                pass.setVisibility(View.VISIBLE);
                confirmaPass.setVisibility(View.VISIBLE);
                confirmaEdita.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_cad_confirma:
                String usuario = user.getText().toString();
                String senha = pass.getText().toString();
                String confirma = confirmaPass.getText().toString();

                if (senha.equals("") || confirma.equals("")) {
                    Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
                } else if (confirmaPass(senha, confirma)) {
                    if(!bd.isUserPass(usuario, senha)){
                        redefineSenha(usuario, senha);
                        pass.setText("");
                        confirmaPass.setText("");
                        editaUser.setVisibility(View.VISIBLE);
                        confirmaEdita.setVisibility(View.GONE);
                        confirmaPass.setVisibility(View.GONE);
                        user.setVisibility(View.GONE);
                        pass.setVisibility(View.GONE);
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
                    editaUser.setVisibility(View.GONE);
                    editaFatura.setVisibility(View.GONE);
                    confirmaFatura.setVisibility(View.VISIBLE);
                    linha1.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Termine a ação anterior ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_confirma_fat:
                String CNPJ = bd.getCNPJ(user.getText().toString());
                String Cep = cep.getText().toString();
                if(bd.updateRegiao(CNPJ,Cep)){
                    Toast.makeText(getActivity(),"CEP Atualizado", Toast.LENGTH_SHORT).show();
                    editaFatura.setVisibility(View.VISIBLE);
                    confirmaFatura.setVisibility(View.GONE);
                    linha1.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(),"CEP Incorreto", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_zerar:
                editaUser.setVisibility(View.VISIBLE);
                user.setVisibility(View.GONE);
                EditText senha_u = getView().findViewById(R.id.conta_senha);
                senha_u.setVisibility(View.GONE);
                pass.setVisibility(View.GONE);
                confirmaPass.setVisibility(View.GONE);
                editaPass.setVisibility(View.GONE);
                confirmaEdita.setVisibility(View.GONE);
                editaFatura.setVisibility(View.VISIBLE);
                confirmaFatura.setVisibility(View.GONE);
                linha1.setVisibility(View.GONE);
                break;
            case R.id.btn_conta_cep: //Valida CEP
                String url = "https://viacep.com.br/ws/" + cep.getText().toString() + "/json/";
                jsonCEP(url);
                break;
        }

    }

    public String getCEP(){
        return bd.getRegiao(bd.getCNPJ(data.getLogin()));
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
            String jsonuf; // jsonUF


            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsoncep = response.getString("cep");
                    jsonrua = response.getString("logradouro");
                    jsonbairro = response.getString("bairro");
                    jsoncidade = response.getString("localidade");
                    jsonuf = response.getString("uf");

                    cep.setText(jsoncep);
                    rua.setText(jsonrua);
                    bairro.setText(jsonbairro);
                    cidade.setText(jsoncidade);
                    estado.setText(jsonuf);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "CEP Inválido ! ", Toast.LENGTH_SHORT).show();
                    cep.setText("");
                    rua.setText("");
                    bairro.setText("");
                    cidade.setText("");
                    estado.setText("");
                }
            }
        }, erro -> {
            erro.printStackTrace();
            Toast.makeText(getActivity(), "CEP Inválido ! ", Toast.LENGTH_SHORT).show();
            cep.setText("");
            rua.setText("");
            bairro.setText("");
            cidade.setText("");
            estado.setText("");
        });

        mQueue.add(request);

    }
}
