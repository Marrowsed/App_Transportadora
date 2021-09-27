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

public class Fragmento_Cadastra_Login extends Fragment implements View.OnClickListener {

    EditText user, pass, confirmpass;
    Button finaliza;
    ManipulaDB bd;
    String empresa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragmento_cadastra_login, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            empresa = bundle.getString("CNPJ");
            setEmpresa(empresa);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        user = getView().findViewById(R.id.cad_user);
        pass = getView().findViewById(R.id.cad_pass);
        finaliza = getView().findViewById(R.id.btn_final);
        confirmpass = getView().findViewById(R.id.cad_confirma);
        bd = new ManipulaDB(getActivity());

        finaliza.setOnClickListener(this);
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

    public Boolean confirmaPass(String senha, String confirma){
        Boolean confirmar = senha.equals(confirma);
        if (confirmar == true){
            return true;
        } else {
            return false;
        }
    }

    public Boolean cadastraUser(String user, String pass, String CNPJ) {
        isUser(user);
        Boolean checa = bd.isUserPass(user, pass);
        if (checa == false) {
            Boolean inserir = bd.inserirDados(user, pass, CNPJ);
            if (inserir == true) {
                Toast.makeText(getActivity(), "Cadastro criado com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Cadastro já existente !", Toast.LENGTH_SHORT).show();
            }
        }
        bd.close();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_final:
                String usuario = user.getText().toString();
                String senha = pass.getText().toString();
                String confirma = confirmpass.getText().toString();
                Boolean checa = bd.isUser(usuario);
                String CNPJ = getEmpresa();

                if (usuario.equals("") || senha.equals("") || confirma.equals("")) {
                    Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
                } else if(checa == false) {
                    if (confirmaPass(senha, confirma) == true) {
                        cadastraUser(usuario, senha, CNPJ);
                        Intent it = new Intent(getActivity(), TLogin.class);
                        startActivity(it);
                    } else
                        Toast.makeText(getActivity(), "Senha errada !", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(), "Usuário já existente !", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public String getEmpresa (){
        return empresa;
    }

    public void setEmpresa(String empresa){
        this.empresa = empresa;
    }
}

