package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Leer_Listas_Cargue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.conasforapp.R;
import com.example.conasforapp.databinding.FragmentLeerFirmasBinding;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class LeerFirmasFragment extends Fragment {
    View rootView;
    private FragmentLeerFirmasBinding binding;
    private ImageView imgFirmaSupervisor, imgFirmaDespachador, imgFirmaConductor, imgFirmaOperador, imgCerrarDesplegableFirmas;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storage;
    private String pathLista = "Listas chequeo cargue descargue";
    private Bitmap firmaSupervisor = null, firmaDespachador = null, firmaConductor = null, firmaOperador = null;
    String id_posLista = AgregarMostrarListas.idPosicionLista;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uid = currentUser.getUid();
    private TextInputEditText edtNombreSupervisor, edtNombreDespachador,edtNombreConductor,edtNombreOperador;
    String pathFirmasStorage = "Firmas_Cargue_Descargue/";
    private Map<String, Object> nombresFirmas = new HashMap<>();
    private int listId = -1;
    private ListasCargueDataBaseHelper dbLocal;
    private ListasCargueModel listasCargueModel = new ListasCargueModel();

    String idListaPos = null;

    private static int listIdLocal= 0;
    private static final String ARG_LIST_ID = "list_id";

    // Método estático para crear una instancia del fragmento con argumentos
    public static LeerFirmasFragment newInstance(int listId) {
        Log.d("LIST ID LEER FIRMAS NEW INSTANCE","LIST ID LEER FIRMAS NEW INSTANCE : " + listId);
        listIdLocal = listId;
        LeerFirmasFragment fragment = new LeerFirmasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //rootView =  inflater.inflate(R.layout.fragment_leer_firmas, container, false);

        binding = FragmentLeerFirmasBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        dbLocal = new ListasCargueDataBaseHelper(requireContext());

        edtNombreSupervisor = binding.edtNombreSupervisorFirmasLeer;
        edtNombreDespachador = binding.edtNombreDespachadorFirmasLeer;
        edtNombreConductor = binding.edtNombreConductorFirmasLeer;
        edtNombreOperador = binding.edtNombreOperadorFirmasLeer;

        imgFirmaSupervisor = binding.imgFirmaSupervisorLeer;
        imgFirmaDespachador = binding.imgFirmaDespachadorLeer;
        imgFirmaConductor = binding.imgFirmaConductorLeer;
        imgFirmaOperador = binding.imgFirmaOperadorLeer;

        int colorNegro = ContextCompat.getColor(getContext(), R.color.black);
        edtNombreSupervisor.setTextColor(colorNegro);
        edtNombreDespachador.setTextColor(colorNegro);
        edtNombreConductor.setTextColor(colorNegro);
        edtNombreOperador.setTextColor(colorNegro);

        if(isNetworkAvailable()){
            loadDataFirestore();
        }
        else {
            loadDataDBLocal(listIdLocal);
            Log.d("ID LEER FIRMAS ","ID LEER FIRMAS : " + listIdLocal);
        }
        return rootView;
    }

    private void loadDataFirestore(){
        String nombreSupervisor = AgregarMostrarListas.listasCargueModel.getNombre_Supervisor();
        String nombreDespachador = AgregarMostrarListas.listasCargueModel.getNombre_Despachador();
        String nombreConductor = AgregarMostrarListas.listasCargueModel.getNombre_Conductor();
        String nombreOperador = AgregarMostrarListas.listasCargueModel.getNombre_Operador();

        String urlFirmaSupervisor = AgregarMostrarListas.listasCargueModel.getFirma_Supervisor();
        String urlFirmaDespachador = AgregarMostrarListas.listasCargueModel.getFirma_Despachador();
        String urlFirmaConductor = AgregarMostrarListas.listasCargueModel.getFirma_Conductor();
        String urlFirmaOperador = AgregarMostrarListas.listasCargueModel.getFirma_Operador();

        // OBTENER FIRMA SUPERVISOR FIRESTORE
        Log.d("URL FIRMA SUPERVISOR","URL FIRMA SUEPERVISOR EN EDITAR FIRMAS"+urlFirmaSupervisor);
        edtNombreSupervisor.setText(nombreSupervisor);
        if (urlFirmaSupervisor != null && !urlFirmaSupervisor.isEmpty()){
            Picasso.with(getContext())
                    .load(urlFirmaSupervisor)
                    .resize(390, 390)
                    .into(imgFirmaSupervisor);
        }
        else {
            imgFirmaSupervisor.setImageResource(R.drawable.img_sin_firma);
        }
        // OBTENER FIRMA DESPACHADOR FIRESTORE
        edtNombreDespachador.setText(nombreDespachador);
        if (urlFirmaDespachador != null &&!urlFirmaDespachador.isEmpty()){
            Picasso.with(getContext())
                    .load(urlFirmaDespachador)
                    .resize(390, 390)
                    .into(imgFirmaDespachador);
        }
        else {
            imgFirmaDespachador.setImageResource(R.drawable.img_sin_firma);

        }
        // OBTENER FIRMA CONDUCTOR FIRESTORE
        edtNombreConductor.setText(nombreConductor);
        if (urlFirmaConductor!= null && !urlFirmaConductor.isEmpty()){
            Picasso.with(getContext())
                    .load(urlFirmaConductor)
                    .resize(390, 390)
                    .into(imgFirmaConductor);
        }
        else {
            imgFirmaConductor.setImageResource(R.drawable.img_sin_firma);

        }
        // OBTENER FIRMA OPERADOR FIRESTORE
        edtNombreOperador.setText(nombreOperador);
        if (urlFirmaOperador != null && !urlFirmaOperador.isEmpty()){
            Picasso.with(getContext())
                    .load(urlFirmaOperador)
                    .resize(390, 390)
                    .into(imgFirmaOperador);
        }
        else {
            imgFirmaOperador.setImageResource(R.drawable.img_sin_firma);
        }
    }

    private void loadDataDBLocal(Integer listId){
        listasCargueModel = dbLocal.getListById(listId);

        edtNombreSupervisor.setText(listasCargueModel.getNombre_Supervisor());
        edtNombreDespachador.setText(listasCargueModel.getNombre_Despachador());
        edtNombreConductor.setText(listasCargueModel.getNombre_Conductor());
        edtNombreOperador.setText(listasCargueModel.getNombre_Operador());
        String firmaSupervisor = listasCargueModel.getFirma_Supervisor();
        String firmaDespachador = listasCargueModel.getFirma_Despachador();
        String firmaConductor = listasCargueModel.getFirma_Conductor();
        String firmaOperador = listasCargueModel.getFirma_Operador();

        //El método replace("\\s", ""), se utiliza para quitar espacios que estén presentes en la imagen codificada que se recupera de la base de datos local
        String trimmed64StringFirma1 = firmaSupervisor.replaceAll("\\s", "");
        String trimmed64StringFirma2 = firmaDespachador.replaceAll("\\s", "");
        String trimmed64StringFirma3 = firmaConductor.replaceAll("\\s", "");
        String trimmed64StringFirma4 = firmaOperador.replaceAll("\\s", "");

        Log.d("trimmed64StringFirma1","trimmed64StringFirma1 : " + trimmed64StringFirma1);
        Log.d("trimmed64StringFirma2","trimmed64StringFirma2 : " + trimmed64StringFirma2);
        Log.d("trimmed64StringFirma3","trimmed64StringFirma3 : " + trimmed64StringFirma3);
        Log.d("trimmed64StringFirma4","trimmed64StringFirma4 : " + trimmed64StringFirma4);

        // OBTENER FIRMA SUPERVISOR BD LOCAL
        if (!trimmed64StringFirma1.isEmpty()){
            byte[] decodedString = Base64.decode(trimmed64StringFirma1, Base64.DEFAULT);
            Bitmap decodedByteFirma1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgFirmaSupervisor.setImageBitmap(decodedByteFirma1);

        }
        else {
            imgFirmaSupervisor.setImageResource(R.drawable.img_sin_firma);

        }
        // OBTENER FIRMA DESPACHADOR BD LOCAL
        if (!trimmed64StringFirma2.isEmpty()){
            byte[] decodedString = Base64.decode(trimmed64StringFirma2, Base64.DEFAULT);
            Bitmap decodedByteFirma2 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgFirmaDespachador.setImageBitmap(decodedByteFirma2);

        }
        else {
            imgFirmaDespachador.setImageResource(R.drawable.img_sin_firma);
        }
        // OBTENER FIRMA CONDUCTOR BD LOCAL
        if (!trimmed64StringFirma3.isEmpty()){
            byte[] decodedString = Base64.decode(trimmed64StringFirma3, Base64.DEFAULT);
            Bitmap decodedByteFirma3 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgFirmaConductor.setImageBitmap(decodedByteFirma3);
        }
        else {
            imgFirmaConductor.setImageResource(R.drawable.img_sin_firma);
        }
        // OBTENER FIRMA OPERADOR BD LOCAL
        if (!trimmed64StringFirma4.isEmpty()){
            byte[] decodedString = Base64.decode(trimmed64StringFirma4, Base64.DEFAULT);
            Bitmap decodedByteFirma4 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgFirmaOperador.setImageBitmap(decodedByteFirma4);
        }
        else {
            imgFirmaOperador.setImageResource(R.drawable.img_sin_firma);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}