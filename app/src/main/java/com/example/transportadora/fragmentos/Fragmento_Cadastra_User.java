package com.example.transportadora.fragmentos;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fragmento_Cadastra_User extends Fragment implements View.OnClickListener {

    protected EditText nome, sobrenome, email, cnpj;
    TextView razao;
    protected Button continua, valide;
    Fragmento_Cadastra_Regiao fr;
    ManipulaDB bd;
    private RequestQueue mQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_cadastra_user, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nome = getView().findViewById(R.id.edt_nome);
        sobrenome = getView().findViewById(R.id.edt_sobrenome);
        email = getView().findViewById(R.id.edt_email);
        cnpj = getView().findViewById(R.id.edt_cnpj);
        razao = getView().findViewById(R.id.edt_razao);
        continua = getView().findViewById(R.id.btn_continua);
        valide = getView().findViewById(R.id.btn_valida);
        bd = new ManipulaDB(getActivity());

        continua.setOnClickListener(this);
        valide.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_continua:
                String name = nome.getText().toString().toLowerCase();
                String surname = sobrenome.getText().toString().toLowerCase();
                String mail = email.getText().toString();
                String CNPJ = cnpj.getText().toString();
                String social = razao.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("CNPJ", CNPJ);

                if (name.equals("") || surname.equals("") || mail.equals("") || CNPJ.equals("") || social.equals("")) {
                    Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
                } else if (!validateEmail(mail)) {
                    Toast.makeText(getActivity(), "E-mail inválido !", Toast.LENGTH_SHORT).show();
                } else if (!bd.isDataPJ(CNPJ)) {
                    cadastraUser(name, surname, mail, CNPJ, social, "null", "null", "null");
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    fr = new Fragmento_Cadastra_Regiao();
                    fr.setArguments(bundle);
                    transaction.replace(R.id.fragmento_container_user, fr);
                    transaction.commit();
                } else {
                    Toast.makeText(getActivity(), "CNPJ Inválido", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_valida:
                String PJ = cnpj.getText().toString();
                String regex = PJ.replaceAll("[-./]","");
                String url = "https://www.receitaws.com.br/v1/cnpj/" + regex;
                jsonParse(url);
                break;
        }

    }

    private void jsonParse(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            String jsonr; //json razao
            String jsonc; // json cnpj
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsonr = response.getString("nome");
                    jsonc = response.getString("cnpj");

                    razao.setText(jsonr);
                    cnpj.setText(jsonc);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "CNPJ Inválido ! ", Toast.LENGTH_SHORT).show();
                    razao.setText("");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError erro) {
                erro.printStackTrace();
                Toast.makeText(getActivity(), "CNPJ Inválido ! ", Toast.LENGTH_SHORT).show();
                razao.setText("");
            }
        });

        mQueue.add(request);
    }

    public void cadastraUser(String nome, String sobrenome, String email, String CNPJ, String
            razao, String volume, String regiao, String categoria) {
        validateNome(nome);
        validateSobrenome(sobrenome);
        Boolean checa = bd.isDataPJ(CNPJ);
        if (checa == false) {
            Boolean inserir = bd.inserirPJ(nome, sobrenome, email, CNPJ, razao, volume, regiao, categoria);
            if (inserir == true) {
                Toast.makeText(getActivity(), "CNPJ em Validação", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "CNPJ Inválido", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public Boolean validateEmail (String email){
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern emailp = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        CharSequence emailc = email;
        Matcher emailm = emailp.matcher(emailc);
        Boolean isEmail = false;
        if (emailm.matches()) {
            isEmail = true;
        } else {
            return false;
        }
        return isEmail;
    }

    public static String validateNome(String nome){
        String regex = "\\s+";
        nome.toLowerCase();
        String nomef = nome.replaceAll(regex, "");
        String Nome = nomef.substring(0, 1).toUpperCase() + nomef.substring(1);
        String min = Nome.toLowerCase();
        String last = min.substring(0, 1).toUpperCase() + min.substring(1);
        return last;
    }

    public static String validateSobrenome(String sobrenome){
        String regex = "\\s+";
        sobrenome.toLowerCase();
        String snomef = sobrenome.replaceAll(regex, "");
        String Snome = snomef.substring(0, 1).toUpperCase() + snomef.substring(1);
        String min = Snome.toLowerCase();
        String last = min.substring(0, 1).toUpperCase() + min.substring(1);
        return last;
    }
}


