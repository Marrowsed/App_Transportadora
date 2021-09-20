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

public class Fragmento_Esqueci_Senha extends Fragment {

    EditText user, pass;
    Button redefinir;
    ManipulaDB bd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragmento_esqueci_senha, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        user = getView().findViewById(R.id.fgt_user);
        pass = getView().findViewById(R.id.fgt_pass);
        redefinir = getView().findViewById(R.id.fgt_btn);
        bd = new ManipulaDB(getActivity());

        redefinir.setOnClickListener(v -> {
           String usuario = user.getText().toString();
           String senha = pass.getText().toString();
           Boolean checa = bd.isUser(usuario);

            if (usuario.equals("") || senha.equals("")) {
                Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
            } else if(checa == true){
               redefineSenha(usuario, senha);
               Intent it = new Intent(getActivity(), TLogin.class);
               startActivity(it);
            } else
               Toast.makeText(getActivity(), "Usuário não existente !", Toast.LENGTH_SHORT).show();
            });

        }

    public Boolean redefineSenha(String user, String pass) {
        Boolean checa = bd.isUser(user);
        if (checa == true) {
            Boolean atualiza = bd.updatePass(user, pass);
            if (atualiza == true) {
                Toast.makeText(getActivity(), "Senha atualizada com sucesso !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Usuário não existente !", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

}

