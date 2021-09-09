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

public class Fragmento_Frete extends Fragment {

    EditText d1, d2, d3, peso, valor, pesoc;
    Button calcula;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        calcula.setOnClickListener(v -> {
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

        });
    }
}

