package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Leer_Listas_Cargue;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.conasforapp.R;
import com.example.conasforapp.databinding.FragmentLeerInfoLugarBinding;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.example.conasforapp.modelos.UsuariosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeerInfoLugarFragment extends Fragment {
    private FragmentLeerInfoLugarBinding binding;
    private FirebaseFirestore db;
    TextInputEditText edtFecha, edtHora,edtTipoCargue, edtNombreZona, edtNombreNucleo, edtNombreFinca;
    Dialog dialog;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ListasCargueModel listasCargueModel = new ListasCargueModel();
    private ListasCargueDataBaseHelper dbLocal;
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String pathUsuarios = "Usuarios";
    UsuariosModel usuariosModel = new UsuariosModel();
    String cargo = "";
    private static final String ARG_LIST_ID = "list_id";

    private static int listIdLocal= 0;

    // Método estático para crear una instancia del fragmento con argumentos
    public static LeerInfoLugarFragment newInstance(int listId) {
        listIdLocal = listId;
        LeerInfoLugarFragment fragment = new LeerInfoLugarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    public LeerInfoLugarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //rootView = inflater.inflate(R.layout.fragment_leer_info_lugar, container, false);

        binding = FragmentLeerInfoLugarBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        db = FirebaseFirestore.getInstance();

        // Acceder a las vistas a través de binding
        edtHora = binding.edtHoraLlegadaLeer;
        edtFecha = binding.edtFechaLeer;
        edtTipoCargue = binding.edtTipoCargueLeer;
        edtNombreZona = binding.edtNombreZonaLeer;
        edtNombreNucleo = binding.edtNombreNucleoLeer;
        edtNombreFinca = binding.edtNombreFincaLeer;

        int colorNegro = ContextCompat.getColor(getContext(), R.color.black);

        edtHora.setTextColor(colorNegro);
        edtFecha.setTextColor(colorNegro);
        edtTipoCargue.setTextColor(colorNegro);
        edtNombreZona.setTextColor(colorNegro);
        edtNombreNucleo.setTextColor(colorNegro);
        edtNombreFinca.setTextColor(colorNegro);

        //txtEncabezado = rootView.findViewById(R.id.txtEncabezadoListaCargueDescargueLeer);
        dbLocal = new ListasCargueDataBaseHelper(requireContext());
        if (!isNetworkAvailable()){
            loadDataDBLocal(listIdLocal);

        }
        else {
            loadDataFirebase();
        }
        obtenerUsuario();
        return rootView;
    }

    private void obtenerUsuario() {
        if (currentUser != null) {
            // El usuario está autenticado
            String uid = currentUser.getUid();
            db.collection(pathUsuarios).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            usuariosModel = document.toObject(UsuariosModel.class);
                            cargo = usuariosModel.getCargo();
                            Log.d("CARGO EN LEER INFO LUGAR","CARGO EN LEER INFO LUGAR : " + cargo);
                            //loadDataFirebase(cargo);
                        }
                    }
                }
            });
        }
    }

    private void loadDataFirebase() {

        String hora = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getHoraEntrada();
        String fecha = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getFecha();
        String tipoCargue = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getTipoCargue();
        String zona = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreZona();
        String nucleo = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreNucleo();
        String finca = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();

        edtHora.setText(hora);
        edtFecha.setText(fecha);
        edtTipoCargue.setText(tipoCargue);
        edtNombreZona.setText(zona);
        edtNombreNucleo.setText(nucleo);
        edtNombreFinca.setText(finca);
    }

    private void loadDataDBLocal(Integer listId){

        Log.d("LIST ID LOAD DATA DB LOCAL","LIST ID LOAD DATA DB LOCAL : "+listId);

        listasCargueModel = dbLocal.getListById(listId);

        edtHora.setText(listasCargueModel.getItem_1_Informacion_lugar_cargue().getHoraEntrada().toString());
        edtFecha.setText(listasCargueModel.getItem_1_Informacion_lugar_cargue().getFecha());
        edtTipoCargue.setText(listasCargueModel.getItem_1_Informacion_lugar_cargue().getTipoCargue());
        edtNombreZona.setText(listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreZona());
        edtNombreNucleo.setText(listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreNucleo());
        edtNombreFinca.setText(listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca());

        edtHora.invalidate(); //invalidate se utiliza para solicitar que una vista se redespliegue o actualice su contenido visual en la pantalla
        edtFecha.invalidate();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private Date parseFecha(String fechaString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}