package com.example.conasforapp.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;

public class ListasFragment extends Fragment {


    private View view;

    private CardView cvCargueDescargue, cvMotosierrista;

    public ListasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_listas, container, false);

        cvCargueDescargue = view.findViewById(R.id.cvCargueDescargue);
        cvMotosierrista = view.findViewById(R.id.cvMotosierrista);

        cvCargueDescargue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agregarListas = new Intent(getActivity(), AgregarMostrarListas.class);
                startActivity(agregarListas);
            }
        });


        return view;
    }
}