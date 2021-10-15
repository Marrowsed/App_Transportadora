package com.example.transportadora.fragmentos;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.transportadora.Dados;
import com.example.transportadora.ManipulaDB;
import com.example.transportadora.R;
import com.example.transportadora.TAcesso;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fragmento_Cadastra_Login extends Fragment implements View.OnClickListener, View.OnTouchListener {

    EditText user, pass, confirmpass;
    TextView txt_pass;
    Button finaliza;
    ManipulaDB bd;
    Dados data;
    String CNPJ;

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
        bd = new ManipulaDB(getActivity());
        data = new Dados();
        CNPJ = data.getCNPJ();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //Font
        Typeface fonte = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IBMPlexSans-Bold.ttf");

        //View
        user = getView().findViewById(R.id.cad_user);
        user.setTypeface(fonte);
        pass = getView().findViewById(R.id.cad_pass);
        pass.setTypeface(fonte);
        confirmpass = getView().findViewById(R.id.cad_confirma);
        confirmpass.setTypeface(fonte);
        txt_pass = getView().findViewById(R.id.txt_pass);
        txt_pass.setText(
                "Crie sua senha com 8 dígitos sendo:\n" +
                        "1 Número \n" +
                        "1 Letra Maiúscula \n" +
                        "1 Letra Minúscula \n" +
                        "1 Caractere Especial");
        txt_pass.setTypeface(fonte);
        finaliza = getView().findViewById(R.id.btn_final);


        finaliza.setOnClickListener(this);
        pass.setOnClickListener(this);
        pass.setOnTouchListener(this);
        confirmpass.setOnTouchListener(this);
        }


    public Boolean isUser(String user){
        Boolean checa = bd.isUser(user);
        if (!checa) {
                Toast.makeText(getActivity(), "Usuário permitido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Usuário já existente !", Toast.LENGTH_SHORT).show();
            }
        return true;
    }

    public Boolean confirmaPass(String senha, String confirma){
        boolean confirmar = senha.equals(confirma);
        if (confirmar){
            return true;
        } else {
            return false;
        }
    }

    public Boolean cadastraUser(String user, String pass, String CNPJ) {
        isUser(user);
        Boolean checa = bd.isUserPass(user, pass);
        if (!checa) {
            Boolean inserir = bd.inserirDados(user, pass, CNPJ);
            if (inserir) {
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

                if (usuario.equals("") || senha.equals("") || confirma.equals("")) {
                    Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
                } else if(!checa) {
                    validateUser(usuario);
                    if (validatePass(senha) && confirmaPass(senha, confirma)) {
                        cadastraUser(usuario, senha, CNPJ);
                        data.setLogin(usuario);
                        Intent it = new Intent(getActivity(), TAcesso.class);
                        startActivity(it);
                    } else
                        Toast.makeText(getActivity(), "Senha errada !", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getActivity(), "Usuário já existente !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cad_pass:
                Handler handler = new Handler(Looper.getMainLooper());
                txt_pass.setVisibility(View.VISIBLE);
                handler.postDelayed(() -> {
                    if(txt_pass.getVisibility() == View.VISIBLE){
                        txt_pass.setVisibility(View.GONE);
                    }
                }, 5000);
                break;
        }
    }

    public static String validateUser (String user){
        String regex = "\\s+";
        String regexu = user.replaceAll(regex, "");
        return Normalizer.normalize(regexu, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static Boolean validatePass (String senha){
        String regex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        //8 dígitos, sem espaços, Pelo menos: 1 Letra maíuscula, 1 Letra Miníscula,  1 Especial, 1 Número
        //Ex: 12345Aa@
        Pattern senhap = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher senham = senhap.matcher(senha);
        if(senham.matches()){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()){
            case R.id.cad_pass:
                final int DRAWABLE_RIGHT = 2;
                Typeface fonte_padrao = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IBMPlexSans-Bold.ttf");
                if (motionEvent.getRawX() >= (pass.getRight() - pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pass.setTypeface(fonte_padrao);
                    return true;
                } else {
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            case R.id.cad_confirma:
                final int RIGHT = 2;
                Typeface fonte = Typeface.createFromAsset(getActivity().getAssets(), "fonts/IBMPlexSans-Bold.ttf");
                if (motionEvent.getRawX() >= (confirmpass.getRight() - confirmpass.getCompoundDrawables()[RIGHT].getBounds().width())) {
                    confirmpass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confirmpass.setTypeface(fonte);
                    return true;
                } else {
                    confirmpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
        }
        return false;
    }
}

