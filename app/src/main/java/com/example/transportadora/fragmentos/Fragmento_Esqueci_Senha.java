package com.example.transportadora.fragmentos;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.transportadora.ManipulaDB;
import com.example.transportadora.R;
import com.example.transportadora.TLogin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fragmento_Esqueci_Senha extends Fragment implements View.OnClickListener {

    EditText user, pass;
    TextView texto;
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

        //Font
        Typeface fonte = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IBMPlexSans-Bold.ttf");

        user = getView().findViewById(R.id.fgt_user);
        user.setTypeface(fonte);
        pass = getView().findViewById(R.id.fgt_pass);
        pass.setTypeface(fonte);
        redefinir = getView().findViewById(R.id.fgt_btn);
        redefinir.setText("ESQUECI MINHA SENHA");
        redefinir.setTypeface(fonte);
        texto = getView().findViewById(R.id.fgt_txt);
        texto.setTypeface(fonte);
        bd = new ManipulaDB(getActivity());

        pass.setOnClickListener(this);
        redefinir.setOnClickListener(this);
    }

    public void redefineSenha(String user, String pass) {
        Boolean checa = bd.isUser(user);
        if (checa) {
            Boolean atualiza = bd.updatePass(user, pass);
            if (atualiza) {
                Toast.makeText(getActivity(), "Senha atualizada com sucesso !", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Senha incorreta !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Boolean validatePass (String senha){
        String regex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        //8 d??gitos, sem espa??os, Pelo menos: 1 Letra ma??uscula, 1 Letra Min??scula,  1 Especial, 1 N??mero
        Pattern senhap = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher senham = senhap.matcher(senha);
        if(senham.matches()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fgt_pass:
                texto.setVisibility(View.VISIBLE);
                texto.setText(
                        "Crie sua senha com 8 d??gitos sendo:\n" +
                                "1 N??mero \n" +
                                "1 Letra Mai??scula \n" +
                                "1 Letra Min??scula \n" +
                                "1 Caractere Especial");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    if (texto.getVisibility() == View.VISIBLE){
                        texto.setVisibility(View.GONE);
                    }
                }, 10000);
                break;
            case R.id.fgt_btn:
                String usuario = user.getText().toString();
                String senha = pass.getText().toString();
                Boolean checa = bd.isUser(usuario);

                if (usuario.equals("") || senha.equals("")) {
                    Toast.makeText(getActivity(), "Credenciais Inv??lidas", Toast.LENGTH_SHORT).show();
                } else if(checa && validatePass(senha)){
                    redefineSenha(usuario, senha);
                    Intent it = new Intent(getActivity(), TLogin.class);
                    startActivity(it);
                } else
                    Toast.makeText(getActivity(), "Usu??rio ou Senha incorreto !", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

