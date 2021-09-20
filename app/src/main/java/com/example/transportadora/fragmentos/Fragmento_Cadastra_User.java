package com.example.transportadora.fragmentos;

import android.content.Intent;
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

import com.example.transportadora.ManipulaDB;
import com.example.transportadora.R;
import com.example.transportadora.TLogin;
import com.example.transportadora.mascara.CodeMask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fragmento_Cadastra_User extends Fragment {

    protected EditText nome, sobrenome, email, cnpj, razao;
    protected Button continua;
    Fragmento_Cadastra_Regiao fr;
    ManipulaDB bd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_cadastra_user, container, false);
    }
    
    

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        nome = getView().findViewById(R.id.edt_nome);
        sobrenome = getView().findViewById(R.id.edt_sobrenome);
        email = getView().findViewById(R.id.edt_email);
        cnpj = getView().findViewById(R.id.edt_cnpj);
        cnpj.addTextChangedListener(CodeMask.mask(cnpj, CodeMask.FORMAT_CNPJ));
        razao = getView().findViewById(R.id.edt_razao);
        continua = getView().findViewById(R.id.btn_continua);
        bd = new ManipulaDB(getActivity());

        continua.setOnClickListener(v -> {
            String name = nome.getText().toString();
            String surname = sobrenome.getText().toString();
            String mail = email.getText().toString();
            String CNPJ = cnpj.getText().toString();
            String social = razao.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("CNPJ", CNPJ);

            if (name.equals("") || surname.equals("") || mail.equals("") || CNPJ.equals("") || social.equals("")) {
                Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
            } else if(!validateEmail(mail)) {
                Toast.makeText(getActivity(), "E-mail inválido !", Toast.LENGTH_SHORT).show();
            }else if (!bd.isDataPJ(CNPJ)) {
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

        });

    }

    public void cadastraUser(String nome, String sobrenome, String email, String CNPJ, String razao, String volume, String regiao, String categoria) {
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
        CharSequence emailc  = email;
        Matcher emailm = emailp.matcher(emailc);
        Boolean isEmail = false;
        if (emailm.matches()){
            isEmail = true;
        } else {
            return false;
        }
        return isEmail;
    }


}

