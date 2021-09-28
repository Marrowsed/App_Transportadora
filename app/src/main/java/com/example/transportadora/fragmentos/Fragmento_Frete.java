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

import com.example.transportadora.R;
import com.example.transportadora.mascara.CodeMask;

public class Fragmento_Frete extends Fragment implements View.OnClickListener {

    EditText d1, d2, d3, peso, valor, pesoc, cepo, cepd;
    Button calcula;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        return inflater.inflate(R.layout.fragmento_frete, container, false);
        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        d1 = getView().findViewById(R.id.dim1);
        d2 = getView().findViewById(R.id.dim2);
        d3 = getView().findViewById(R.id.dim3);
        peso = getView().findViewById(R.id.peso);
        pesoc = getView().findViewById(R.id.pesoc);
        valor = getView().findViewById(R.id.valor);
        calcula = getView().findViewById(R.id.btn_calc);
        cepo = getView().findViewById(R.id.cepo);
        cepd = getView().findViewById(R.id.cepd);
        cepo.addTextChangedListener(CodeMask.mask(cepo, CodeMask.FORMAT_CEP));
        cepd.addTextChangedListener(CodeMask.mask(cepd, CodeMask.FORMAT_CEP));

        calcula.setOnClickListener(this);
        }


    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_calc:
                Float dim1 = Float.valueOf(d1.getText().toString());
                Float dim2 = Float.valueOf(d2.getText().toString());
                Float dim3 = Float.valueOf(d3.getText().toString());
                Float pesar = Float.valueOf(peso.getText().toString());
                Float cubo = ((dim1 * dim2 * dim3)/6000);
                if (dim1.equals("") || dim2.equals("") || dim3.equals("")){
                    Toast.makeText(getActivity(), "Valores Incompletos", Toast.LENGTH_SHORT).show();
                } else
                if (cubo <= 0){
                    Toast.makeText(getActivity(), "Valores Errados", Toast.LENGTH_SHORT).show();
                }else
                if (cubo > pesar){
                    valor.setText(("R$10,00"));
                    pesoc.setText(Float.toString(cubo));
                } else {
                    valor.setText(("R$15,00"));
                    pesoc.setText(Float.toString(cubo));
                }
        }
        }
    }


