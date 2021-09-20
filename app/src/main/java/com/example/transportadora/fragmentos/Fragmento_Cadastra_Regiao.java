package com.example.transportadora.fragmentos;

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
import com.example.transportadora.mascara.CodeMask;

public class Fragmento_Cadastra_Regiao extends Fragment {

    public EditText cnpj, razao, volume, regiao, categoria;
    protected Button continua2;
    ManipulaDB bd;
    String empresa;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragmento_cadastra_regiao, container, false);
        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String empresa = bundle.getString("CNPJ");
            setEmpresa(empresa);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        cnpj = getView().findViewById(R.id.edt_empresa);
        cnpj.addTextChangedListener(CodeMask.mask(cnpj, CodeMask.FORMAT_CNPJ));
        cnpj.setText(getEmpresa());
        razao = getView().findViewById(R.id.edt_razao);
        volume = getView().findViewById(R.id.edt_volume);
        regiao = getView().findViewById(R.id.edt_cep);
        regiao.addTextChangedListener(CodeMask.mask(regiao
                , CodeMask.FORMAT_CEP));
        categoria = getView().findViewById(R.id.edt_categoria);
        continua2 = getView().findViewById(R.id.btn_regiao);
        bd = new ManipulaDB(getActivity());

        continua2.setOnClickListener(view1 -> {
            String CNPJ = cnpj.getText().toString();
            String vol = volume.getText().toString();
            String reg = regiao.getText().toString();
            String cat = categoria.getText().toString();

            if(vol.equals("") || reg.equals("") || cat.equals("")){
                Toast.makeText(getActivity(), "Credenciais Inválidas", Toast.LENGTH_SHORT).show();
            } else if (bd.isDataPJ(CNPJ)){
                atualizaUser(CNPJ, vol, reg, cat);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.fragmento_container_regiao, new Fragmento_Cadastra_Login());
                transaction.commit();
            }

        });

    }

    public void atualizaUser(String CNPJ, String volume, String regiao, String categoria){
            Boolean update = bd.atualizaPJ(CNPJ, volume, regiao, categoria);
            if(update == true){
                Toast.makeText(getActivity(), "CNPJ validado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "CNPJ Inválido", Toast.LENGTH_SHORT).show();
            }
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEmpresa () {
            return empresa;
        }

}


