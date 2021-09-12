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

import com.example.transportadora.ManipulaDB;
import com.example.transportadora.R;
import com.example.transportadora.TLogin;

public class Fragmento_Cadastra_Login extends Fragment {

    EditText user, pass;
    Button finaliza;
    ManipulaDB bd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragmento_cadastra_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        user = getView().findViewById(R.id.cad_user);
        pass = getView().findViewById(R.id.cad_pass);
        finaliza = getView().findViewById(R.id.btn_final);
        bd = new ManipulaDB(getActivity());

        finaliza.setOnClickListener(v -> {
           String usuario = user.getText().toString();
           String senha = pass.getText().toString();

            if (usuario.equals("") || senha.equals("")) {
                Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
            } else
                if(isUser(usuario) == false) {
                    cadastraUser(usuario, senha);
                    Intent it = new Intent(getActivity(), TLogin.class);
                    startActivity(it);
                } else
                    Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
            });
        }


    public Boolean isUser(String user){
        Boolean checa = bd.isUser(user);
        if (checa == false) {
                Toast.makeText(getActivity(), "Usuário permitido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Usuário já existente !", Toast.LENGTH_SHORT).show();
            }
        return true;
    }

    public Boolean cadastraUser(String user, String pass) {
        isUser(user);
        Boolean checa = bd.isUserPass(user, pass);
        if (checa == false) {
            Boolean inserir = bd.inserirDados(user, pass);
            if (inserir == true) {
                Toast.makeText(getActivity(), "Cadastro criado com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Cadastro já existente !", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
}

