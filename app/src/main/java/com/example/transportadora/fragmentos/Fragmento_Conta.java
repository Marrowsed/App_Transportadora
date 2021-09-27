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
import com.example.transportadora.TAcesso;

public class Fragmento_Conta extends Fragment implements View.OnClickListener {

    EditText user, pass, confirmpass;
    Button finaliza;
    ManipulaDB bd;
    String login, Usuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragmento_conta, container, false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            login = bundle.getString("Login");
            setLogin(login);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        user = getView().findViewById(R.id.cad_user);
        user.setText(getLogin());
        Usuario = user.getText().toString();
        pass = getView().findViewById(R.id.cad_pass);
        confirmpass = getView().findViewById(R.id.cad_confirma);
        finaliza = getView().findViewById(R.id.btn_final);
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

    public Boolean atualizaPass(String user, String pass) {
        isUser(user);
        Boolean checa = bd.isUserPass(user, pass);
        if (checa == false) {
            Boolean atualiza = bd.updatePass(user, pass);
            if (atualiza == true) {
                Toast.makeText(getActivity(), "Senha Atualizada !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Senha errada !", Toast.LENGTH_SHORT).show();
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

                if (usuario.equals("") || senha.equals("") || confirma.equals("")) {
                    Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
                } else if(!senha.equals(bd.getPass(usuario))) {
                    atualizaPass(usuario, senha);
                    Toast.makeText(getActivity(),"Senha Atualizada !", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("Login", usuario);
                    Intent it = new Intent(getActivity(), TAcesso.class);
                    it.putExtras(bundle);
                    startActivity(it);
                } else {
                    Toast.makeText(getActivity(), "Senha errada !", Toast.LENGTH_SHORT).show();
                }
                break;
                }

        }

    public String getLogin(){
        return login;
    }

    public void setLogin(String login){
        this.login = login;
    }
}

