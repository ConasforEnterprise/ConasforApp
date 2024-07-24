package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Editar_Listas_Cargue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditarFirmasCargue extends Fragment {
    private SignaturePad sPadFirmaSupervisor, sPadFirmaDespachador, sPadFirmaConductor, sPadFirmaOperadorCargue;
    private ImageView imgFirmaSupervisor, imgFirmaDespachador, imgFirmaConductor, imgFirmaOperador, imgCerrarDesplegableFirmas;
    private ImageButton btnLimpiarFirmaSupervisor, btnAceptarFirmaSupervisor, btnLimpiarFirmaDespachador, btnAceptarFirmaDespachador, btnLimpiarFirmaConductor, btnAceptarFirmaConductor, btnLimpiarFirmaOperador, btnAceptarFirmaOperador;
    private ImageButton btnEditarFirmaSupervisor, btnEditarFirmaConductor,btnEditarFirmaDespachador,btnEditarFirmaOperador;
    private ImageButton btnOcultarFirmaSupervisor, btnOcultarFirmaDespachador, btnOcultarFirmaConductor, btnOcultarFirmaOperador;
    private Button btnFirmarSupervisor, btnFirmarDespachador, btnFirmarConductor, btnFirmarOperador, btnAgregarFirmas;
    private RelativeLayout rlOpcFirmaSupervisor, rlOpcFirmaDespachador, rlOpcFirmaConductor, rlOpcFirmaOperador;
    private FrameLayout frameFirmaSupervisor, frameFirmaDespachador, frameFirmaConductor, frameFirmaOperador;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storage;
    private String pathLista = "Listas chequeo cargue descargue";
    private Bitmap firmaSupervisor = null, firmaDespachador = null, firmaConductor = null, firmaOperador = null;
    String id_posLista = AgregarMostrarListas.idPosicionLista;
    String urlFirmaSupervisor = AgregarMostrarListas.listasCargueModel.getFirma_Supervisor();
    String urlFirmaDespachador = AgregarMostrarListas.listasCargueModel.getFirma_Despachador();
    String urlFirmaConductor = AgregarMostrarListas.listasCargueModel.getFirma_Conductor();
    String urlFirmaOperador = AgregarMostrarListas.listasCargueModel.getFirma_Operador();
    private String nombreSupervisor = AgregarMostrarListas.listasCargueModel.getNombre_Supervisor();
    private String nombreDespachador = AgregarMostrarListas.listasCargueModel.getNombre_Despachador();
    private String nombreConductor = AgregarMostrarListas.listasCargueModel.getNombre_Conductor();
    private String nombreOperador = AgregarMostrarListas.listasCargueModel.getNombre_Operador();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uid = currentUser.getUid();
    private TextInputEditText edtNombreSupervisor, edtNombreDespachador,edtNombreConductor,edtNombreOperador;
    FrameLayout framePadreFirmas,frameHijoFirmas;
    String pathFirmasStorage = "Firmas_Cargue_Descargue/";
    private Map<String, Object> nombresFirmas = new HashMap<>();
    private int listId = -1;
    private ListasCargueDataBaseHelper dbLocal;
    private ListasCargueModel listasCargueModel = new ListasCargueModel();

    String idListaPos = null;

    View view;

    public EditarFirmasCargue() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_editar_firmas_cargue_descargue, container, false);

        edtNombreSupervisor = view.findViewById(R.id.edtNombreSupervisorFirmasEditar);
        edtNombreDespachador = view.findViewById(R.id.edtNombreDespachadorFirmasEditar);
        edtNombreConductor = view.findViewById(R.id.edtNombreConductorFirmasEditar);
        edtNombreOperador = view.findViewById(R.id.edtNombreOperadorFirmasEditar);

        framePadreFirmas = view.findViewById(R.id.frameFirmasEditar);
        frameHijoFirmas = view.findViewById(R.id.frameFirmasHijoEdit);

        btnLimpiarFirmaSupervisor = view.findViewById(R.id.btnLimpiarFirmaSupervisorEditar);
        btnAceptarFirmaSupervisor = view.findViewById(R.id.btnAceptarFirmaSupervisorEditar);
        btnEditarFirmaSupervisor = view.findViewById(R.id.btnEditarFirmaSupervisor);
        btnOcultarFirmaSupervisor = view.findViewById(R.id.btnOcultarFirmaSupervisor);

        btnLimpiarFirmaDespachador = view.findViewById(R.id.btnLimpiarFirmaDespachadorEditar);
        btnAceptarFirmaDespachador = view.findViewById(R.id.btnAceptarFirmaDespachadorEditar);
        btnEditarFirmaDespachador = view.findViewById(R.id.btnEditarFirmaDespachador);
        btnOcultarFirmaDespachador = view.findViewById(R.id.btnOcultarFirmaDespachador);

        btnLimpiarFirmaConductor =view.findViewById(R.id.btnLimpiarFirmaConductorEditar);
        btnAceptarFirmaConductor = view.findViewById(R.id.btnAceptarFirmaConductorEditar);
        btnEditarFirmaConductor = view.findViewById(R.id.btnEditarFirmaConductor);
        btnOcultarFirmaConductor = view.findViewById(R.id.btnOcultarFirmaConductor);

        btnLimpiarFirmaOperador =view.findViewById(R.id.btnLimpiarFirmaOperadorEditar);
        btnAceptarFirmaOperador = view.findViewById(R.id.btnAceptarFirmaOperadorEditar);
        btnEditarFirmaOperador = view.findViewById(R.id.btnEditarFirmaOperador);
        btnOcultarFirmaOperador = view.findViewById(R.id.btnOcultarFirmaOperador);

        rlOpcFirmaSupervisor = view.findViewById(R.id.rlOpcFirmaSupervisorEditar);
        rlOpcFirmaSupervisor.setVisibility(View.GONE);
        rlOpcFirmaDespachador = view.findViewById(R.id.rlOpcFirmaDespachadorEditar);
        rlOpcFirmaDespachador.setVisibility(View.GONE);
        rlOpcFirmaConductor = view.findViewById(R.id.rlOpcFirmaConductorEditar);
        rlOpcFirmaConductor.setVisibility(View.GONE);
        rlOpcFirmaOperador = view.findViewById(R.id.rlOpcFirmaOperadorEditar);
        rlOpcFirmaOperador.setVisibility(View.GONE);

        sPadFirmaSupervisor = view.findViewById(R.id.sPadFirmaSupervisorEditar);
        sPadFirmaSupervisor.setEnabled(false);
        sPadFirmaDespachador = view.findViewById(R.id.sPadFirmaDespachadorEditar);
        sPadFirmaDespachador.setEnabled(false);
        sPadFirmaConductor = view.findViewById(R.id.sPadFirmaConductorEditar);
        sPadFirmaConductor.setEnabled(false);
        sPadFirmaOperadorCargue = view.findViewById(R.id.sPadFirmaOperadorCargueEditar);
        sPadFirmaOperadorCargue.setEnabled(false);

        frameFirmaSupervisor = view.findViewById(R.id.frameFirmaSupervisorEditar);
        frameFirmaDespachador = view.findViewById(R.id.frameFirmaDespachadorEditar);
        frameFirmaConductor = view.findViewById(R.id.frameFirmaConductorEditar);
        frameFirmaOperador = view.findViewById(R.id.frameFirmaOperadorEditar);

        btnFirmarSupervisor = view.findViewById(R.id.btnFirmarSupervisorEditar);
        btnFirmarDespachador = view.findViewById(R.id.btnFirmarDespachadorEditar);
        btnFirmarConductor = view.findViewById(R.id.btnFirmarConductorEditar);
        btnFirmarOperador = view.findViewById(R.id.btnFirmarOperadorEditar);

        imgFirmaSupervisor = view.findViewById(R.id.imgFirmaSupervisor);
        imgFirmaDespachador = view.findViewById(R.id.imgFirmaDespachador);
        imgFirmaConductor = view.findViewById(R.id.imgFirmaConductor);
        imgFirmaOperador = view.findViewById(R.id.imgFirmaOperador);

        imgCerrarDesplegableFirmas = view.findViewById(R.id.imgCerrarFirmasEditar);
        btnAgregarFirmas = view.findViewById(R.id.btnAgregarFirmasEditar);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        btnAgregarFirmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarNombresFirmas();
                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        imgCerrarDesplegableFirmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    ocultarFrameWithConnection();
                }
                else{
                    ocultarFrameWithOutConnection();
                }
            }
        });

         */

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(isNetworkAvailable()){
                    ocultarFrameWithConnection();
                }
                else{
                    ocultarFrameWithOutConnection();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        if(!isNetworkAvailable()){
            int listId = getArguments().getInt("list_id", -1);
            Log.d("ID LISTA", "ID LISTA : " + listId);
            Toast.makeText(requireContext(), "ID: " + listId, Toast.LENGTH_SHORT).show();
            if (listId == -1) {
                requireActivity().finish();
                //return;
            }
            dbLocal = new ListasCargueDataBaseHelper(getContext());
            loadDataDBLocal(listId);
        }
        else {
            loadData();
            obtenerLista();

            // Obtener los argumentos del Bundle
            Bundle args = getArguments();
            if (args != null) {
                idListaPos = args.getString("idPos");
                Log.d("ID LISTA POS EN INFO CONDUCTOR","ID LISTA POS EN INFO CONDUCTOR : "+idListaPos);

            }
        }

        frameFirmaSupervisor.setVisibility(View.GONE);
        frameFirmaDespachador.setVisibility(View.GONE);
        frameFirmaConductor.setVisibility(View.GONE);
        frameFirmaOperador.setVisibility(View.GONE);

        //obtenerDatosUsuario();
        //obtenerFirmasFirebase();
        return view;
    }

    public static EditarFirmasCargue newInstance(String idLista) {
        Bundle args = new Bundle();
        args.putString("idPos", idLista);
        EditarFirmasCargue fragment = new EditarFirmasCargue();
        fragment.setArguments(args);
        return fragment;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void ocultarFrameWithOutConnection() {
        //getActivity().finish();
        Bundle arguments = requireArguments();
        listId = arguments.getInt("list_id", -1);
        Log.d("ID LISTA", "ID LISTA : " + listId);
        Toast.makeText(requireContext(), "ID: " + listId, Toast.LENGTH_SHORT).show();
        if (listId == -1) {
            requireActivity().finish();
        }
        Intent intent = new Intent(getContext(),EditarListasCargue.class);
        startActivity(intent);
    }

    private void ocultarFrameWithConnection(){
        Intent intent = new Intent(getContext(),EditarListasCargue.class);
        startActivity(intent);
        getActivity().finish();
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] firmaBytes = baos.toByteArray();
        return Base64.encodeToString(firmaBytes, Base64.DEFAULT);
    }

    public void editarNombresFirmas(){
        actualizarEstadoLista();
        nombresFirmas.put("nombre_Supervisor",edtNombreSupervisor.getText().toString());
        nombresFirmas.put("nombre_Despachador",edtNombreDespachador.getText().toString());
        nombresFirmas.put("nombre_Conductor",edtNombreConductor.getText().toString());
        nombresFirmas.put("nombre_Operador",edtNombreOperador.getText().toString());

        if(isNetworkAvailable()){
            db.collection(pathLista)
                    .document(idListaPos)
                    .update(nombresFirmas).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //loadData();

                            Toast.makeText(getContext(), "Datos almacenados correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "No se almacenaron los datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            int listId = getArguments().getInt("list_id", -1);
            Log.d("ID LISTA", "ID LISTA : " + listId);

            //Toast.makeText(requireContext(), "ID: " + listId, Toast.LENGTH_SHORT).show();
            if (listId == -1) {
                Toast.makeText(getContext(), "ID no válido", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            } else {
                dbLocal = new ListasCargueDataBaseHelper(requireContext());
                listasCargueModel = dbLocal.getListById(listId);
                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            }
            if (listasCargueModel != null) {
                listasCargueModel.setNombre_Supervisor(edtNombreSupervisor.getText().toString());
                listasCargueModel.setNombre_Despachador(edtNombreDespachador.getText().toString());
                listasCargueModel.setNombre_Conductor(edtNombreConductor.getText().toString());
                listasCargueModel.setNombre_Operador(edtNombreOperador.getText().toString());

                dbLocal.updateList(listasCargueModel);
            }
        }
    }

    //------------------------------------ CARGA DATOS BASE DE DATOS LOCAL --------------------------------------//
    private void loadDataDBLocal(Integer listId){
        //Hablita el menú de opciones para actualizar las firmas
        rlOpcFirmaSupervisor.setVisibility(View.VISIBLE);
        rlOpcFirmaDespachador.setVisibility(View.VISIBLE);
        rlOpcFirmaConductor.setVisibility(View.VISIBLE);
        rlOpcFirmaOperador.setVisibility(View.VISIBLE);

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
            editarFirmaSupervisor();
        }
        else {
            imgFirmaSupervisor.setImageResource(R.drawable.img_sin_firma);
            editarFirmaSupervisor();
        }
        // OBTENER FIRMA DESPACHADOR BD LOCAL
        if (!trimmed64StringFirma2.isEmpty()){
            byte[] decodedString = Base64.decode(trimmed64StringFirma2, Base64.DEFAULT);
            Bitmap decodedByteFirma2 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgFirmaDespachador.setImageBitmap(decodedByteFirma2);
            editarFirmaDespachador();
        }
        else {
            imgFirmaDespachador.setImageResource(R.drawable.img_sin_firma);
            editarFirmaDespachador();
        }
        // OBTENER FIRMA CONDUCTOR BD LOCAL
        if (!trimmed64StringFirma3.isEmpty()){
            byte[] decodedString = Base64.decode(trimmed64StringFirma3, Base64.DEFAULT);
            Bitmap decodedByteFirma3 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgFirmaConductor.setImageBitmap(decodedByteFirma3);
            editarFirmaConductor();
        }
        else {
            imgFirmaConductor.setImageResource(R.drawable.img_sin_firma);
            editarFirmaConductor();
        }
        // OBTENER FIRMA OPERADOR BD LOCAL
        if (!trimmed64StringFirma4.isEmpty()){
            byte[] decodedString = Base64.decode(trimmed64StringFirma4, Base64.DEFAULT);
            Bitmap decodedByteFirma4 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imgFirmaOperador.setImageBitmap(decodedByteFirma4);
            editarFirmaOperador();
        }
        else {
            imgFirmaOperador.setImageResource(R.drawable.img_sin_firma);
            editarFirmaOperador();
        }
    }

    //------------------------------------------ CARGA DATOS DE FIREBASE ----------------------------------------//
    private void loadData(){
        Log.d("EditarFirmas", "URL firma supervisor: " + urlFirmaSupervisor);
        Log.d("EditarFirmas", "URL firma despachador: " + urlFirmaDespachador);
        Log.d("EditarFirmas", "URL firma conductor: " + urlFirmaConductor);
        Log.d("EditarFirmas", "URL firma operador: " + urlFirmaOperador);

        rlOpcFirmaSupervisor.setVisibility(View.VISIBLE);
        rlOpcFirmaDespachador.setVisibility(View.VISIBLE);
        rlOpcFirmaConductor.setVisibility(View.VISIBLE);
        rlOpcFirmaOperador.setVisibility(View.VISIBLE);

        //String cargo = AgregarListaCargueDescargue.cargo;
        String cargo = AgregarMostrarListas.cargo;

        Date fechaActualDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_actual_date");
        Date fechaListaDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_lista_date");

        int colorVerdeOscuro = ContextCompat.getColor(getContext(), R.color.verde_oscuro);
        int colorBlanco = ContextCompat.getColor(getContext(), R.color.white);

        if("Administrador".equals(cargo) || (fechaActualDate != null && fechaListaDate != null && !fechaActualDate.equals(fechaListaDate)) && !"Administrador".equals(cargo)){ //
            rlOpcFirmaOperador.setVisibility(View.GONE);
            rlOpcFirmaSupervisor.setVisibility(View.GONE);
            rlOpcFirmaConductor.setVisibility(View.GONE);
            rlOpcFirmaDespachador.setVisibility(View.GONE);
            edtNombreSupervisor.setEnabled(false);
            edtNombreDespachador.setEnabled(false);
            edtNombreConductor.setEnabled(false);
            edtNombreOperador.setEnabled(false);
            btnAgregarFirmas.setVisibility(View.GONE);
            imgCerrarDesplegableFirmas.setVisibility(View.GONE);
            framePadreFirmas.setBackgroundColor(colorVerdeOscuro);
            frameHijoFirmas.setBackgroundColor(colorBlanco);
        }

        // OBTENER FIRMA SUPERVISOR FIRESTORE
        Log.d("URL FIRMA SUPERVISOR","URL FIRMA SUEPERVISOR EN EDITAR FIRMAS"+urlFirmaSupervisor);
        //edtNombreSupervisor.setText(nombreSupervisor);
        if (urlFirmaSupervisor != null && !urlFirmaSupervisor.isEmpty()){
            Picasso.with(view.getContext())
                    .load(urlFirmaSupervisor)
                    .resize(390, 390)
                    .into(imgFirmaSupervisor);
            editarFirmaSupervisor();
        }
        else {
            imgFirmaSupervisor.setImageResource(R.drawable.img_sin_firma);
            editarFirmaSupervisor();
        }
        // OBTENER FIRMA DESPACHADOR FIRESTORE
        //edtNombreDespachador.setText(nombreDespachador);
        if (urlFirmaDespachador != null &&!urlFirmaDespachador.isEmpty()){
            Picasso.with(view.getContext())
                    .load(urlFirmaDespachador)
                    .resize(390, 390)
                    .into(imgFirmaDespachador);
            editarFirmaDespachador();
        }
        else {
            imgFirmaDespachador.setImageResource(R.drawable.img_sin_firma);
            editarFirmaDespachador();
        }
        // OBTENER FIRMA CONDUCTOR FIRESTORE
        //edtNombreConductor.setText(nombreConductor);
        if (urlFirmaConductor!= null && !urlFirmaConductor.isEmpty()){
            Picasso.with(view.getContext())
                    .load(urlFirmaConductor)
                    .resize(390, 390)
                    .into(imgFirmaConductor);
            editarFirmaConductor();
        }
        else {
            imgFirmaConductor.setImageResource(R.drawable.img_sin_firma);
            editarFirmaConductor();
        }
        // OBTENER FIRMA OPERADOR FIRESTORE
        //edtNombreOperador.setText(nombreOperador);
        if (urlFirmaOperador != null && !urlFirmaOperador.isEmpty()){
            Picasso.with(view.getContext())
                    .load(urlFirmaOperador)
                    .resize(390, 390)
                    .into(imgFirmaOperador);
            editarFirmaOperador();
        }
        else {
            imgFirmaOperador.setImageResource(R.drawable.img_sin_firma);
            editarFirmaOperador();
        }
    }

    private void obtenerLista(){

        String idLista = AgregarMostrarListas.listasCargueModel.getId_lista().toString();
        db.collection(pathLista).document(idLista).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("EditarListasCargue", "Error fetching Firestore documents: ", error);
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    ListasCargueModel listasCargueModel = snapshot.toObject(ListasCargueModel.class);
                    if (listasCargueModel != null) {
                        listasCargueModel.setId_lista(snapshot.getId());

                        String urlFirmaSupervisor = listasCargueModel.getFirma_Supervisor();
                        String urlFirmaDespachador = listasCargueModel.getFirma_Despachador();
                        String urlFirmaConductor = listasCargueModel.getFirma_Conductor();
                        String urlFirmaOperador = listasCargueModel.getFirma_Operador();

                        // OBTENER FIRMA SUPERVISOR FIRESTORE
                        edtNombreSupervisor.setText(listasCargueModel.getNombre_Supervisor());
                        if (urlFirmaSupervisor != null && !urlFirmaSupervisor.isEmpty()){
                            Picasso.with(view.getContext())
                                    .load(urlFirmaSupervisor)
                                    .resize(390, 390)
                                    .into(imgFirmaSupervisor);
                            editarFirmaSupervisor();
                        }
                        else {
                            imgFirmaSupervisor.setImageResource(R.drawable.img_sin_firma);
                            editarFirmaSupervisor();
                        }
                        // OBTENER FIRMA DESPACHADOR FIRESTORE
                        edtNombreDespachador.setText(listasCargueModel.getNombre_Despachador());
                        if (urlFirmaDespachador != null &&!urlFirmaDespachador.isEmpty()){
                            Picasso.with(view.getContext())
                                    .load(urlFirmaDespachador)
                                    .resize(390, 390)
                                    .into(imgFirmaDespachador);
                            editarFirmaDespachador();
                        }
                        else {
                            imgFirmaDespachador.setImageResource(R.drawable.img_sin_firma);
                            editarFirmaDespachador();
                        }
                        // OBTENER FIRMA CONDUCTOR FIRESTORE
                        edtNombreConductor.setText(listasCargueModel.getNombre_Conductor());
                        if (urlFirmaConductor!= null && !urlFirmaConductor.isEmpty()){
                            Picasso.with(view.getContext())
                                    .load(urlFirmaConductor)
                                    .resize(390, 390)
                                    .into(imgFirmaConductor);
                            editarFirmaConductor();
                        }
                        else {
                            imgFirmaConductor.setImageResource(R.drawable.img_sin_firma);
                            editarFirmaConductor();
                        }
                        // OBTENER FIRMA OPERADOR FIRESTORE
                        edtNombreOperador.setText(listasCargueModel.getNombre_Operador());
                        if (urlFirmaOperador != null && !urlFirmaOperador.isEmpty()){
                            Picasso.with(view.getContext())
                                    .load(urlFirmaOperador)
                                    .resize(390, 390)
                                    .into(imgFirmaOperador);
                            editarFirmaOperador();
                        }
                        else {
                            imgFirmaOperador.setImageResource(R.drawable.img_sin_firma);
                            editarFirmaOperador();
                        }

                    }
                }
            }
        });
    }
    //----------------------------------------------- FIRMA SUPERVISOR -------------------------------------------//
    private void firmaSupervisor(){
        btnFirmarSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFirmarSupervisor.setVisibility(View.INVISIBLE);
                frameFirmaSupervisor.setVisibility(View.INVISIBLE);
                sPadFirmaSupervisor.setEnabled(true);//Habilita la firma
                sPadFirmaSupervisor.setMinWidth(3f); // Ancho mínimo del trazo
                sPadFirmaSupervisor.setMaxWidth(3f); // Ancho máximo del trazo
                rlOpcFirmaSupervisor.setVisibility(View.VISIBLE);
            }
        });
        sPadFirmaSupervisor.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() { // Se llama cuando se comienza a escribir
            }
            @Override
            public void onSigned() { // Se llama cuando el usuario ha terminado de escribir
                btnAceptarFirmaSupervisor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firmaSupervisor = sPadFirmaSupervisor.getSignatureBitmap();
                        frameFirmaSupervisor.setVisibility(View.VISIBLE);
                        btnFirmarSupervisor.setVisibility(View.VISIBLE);
                        sPadFirmaSupervisor.setEnabled(false);
                        rlOpcFirmaSupervisor.setVisibility(View.GONE);

                        if(isNetworkAvailable()){
                            enviarFirma1(firmaSupervisor);
                        }
                        else {
                            int listId = getArguments().getInt("list_id", -1);
                            if (listId == -1) {
                                Toast.makeText(getContext(), "ID no válido", Toast.LENGTH_SHORT).show();
                                requireActivity().finish();
                            } else {
                                dbLocal = new ListasCargueDataBaseHelper(requireContext());
                                listasCargueModel = dbLocal.getListById(listId);
                                Toast.makeText(getContext(), "Firma actualizada", Toast.LENGTH_SHORT).show();
                            }

                            if (listasCargueModel != null) {
                                String firma1Base64 = convertBitmapToBase64(firmaSupervisor);
                                listasCargueModel.setFirma_Supervisor(firma1Base64);
                                dbLocal.updateList(listasCargueModel);
                            }
                        }

                    }
                });
            }
            @Override
            public void onClear() {// Se llama cuando se borra la firma
            }
        });
        btnLimpiarFirmaSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPadFirmaSupervisor.clear(); //Limpia la firma
            }
        });
    }
    private void editarFirmaSupervisor(){
        firmaSupervisor();
        btnEditarFirmaSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPadFirmaSupervisor.setVisibility(View.VISIBLE);
                sPadFirmaSupervisor.setEnabled(true);
                rlOpcFirmaSupervisor.setVisibility(View.VISIBLE);
                imgFirmaSupervisor.setVisibility(View.GONE);
            }
        });
        btnLimpiarFirmaSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPadFirmaSupervisor.clear();
            }
        });
        btnOcultarFirmaSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameFirmaSupervisor.setVisibility(View.VISIBLE);
                btnFirmarSupervisor.setVisibility(View.VISIBLE);
                sPadFirmaSupervisor.setEnabled(false);
                rlOpcFirmaSupervisor.setVisibility(View.GONE);
                imgFirmaSupervisor.setVisibility(View.GONE);
            }
        });
    }
    public void enviarFirma1(Bitmap signature1) {
        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calHoy.getTime());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature1.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        //String id_lista = AgregarListaCargueDescargue.idPosicionLista;
        String finca = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();

        // Generar un nombre único para el archivo en el Storage
        String fileName = "firma_supervisor_" + "_" + formattedDate + "_ID_USUARIO:_"+  uid + "_ID_LISTA:_" + idListaPos + ".png";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(); // Referencia al Firebase Storage
        StorageReference firmaRef = storageRef.child(pathFirmasStorage + formattedDate + "/" + fileName); // Referencia al archivo en el Firebase Storage

        firmaRef.putBytes(data)  //--> Subir la firma al Firebase Storage
                .addOnSuccessListener(taskSnapshot -> {
                    // La firma se sube al Storage de Firebase
                    firmaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString(); // Obtener la URL de descarga del archivo
                        guardarFirma1EnFirestore(downloadUrl); // Guardar la URL de descarga en Firestore
                    });
                })
                .addOnFailureListener(exception -> {
                    // Manejar el error
                    Toast.makeText(getContext(), "Error al subir la firma", Toast.LENGTH_SHORT).show();
                });
    }
    private void guardarFirma1EnFirestore(String downloadUrl) {

        Log.d("Id POS LISTA","ID POS LISTA : " + id_posLista);
        // Crear un nuevo documento en la colección 'firmas' en Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> firmaData1 = new HashMap<>();

        firmaData1.put("firma_Supervisor", downloadUrl);
        firmaData1.put("nombre_Supervisor",edtNombreSupervisor.getText().toString());
        firmaData1.put("timestamp", FieldValue.serverTimestamp()); // Opcional, para registrar la fecha de la firma

        firestore.collection(pathLista).document(idListaPos).update(firmaData1)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al guardar la firma en Firestore

                    //loadData();
                    Toast.makeText(getContext(), "Firma cargada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error al guardar la firma en Firestore
                    Toast.makeText(getContext(), "Error al guardar la firma en Firestore", Toast.LENGTH_SHORT).show();
                });
    }

    //----------------------------------------------- FIRMA DESPACHADOR -------------------------------------------//
    private void firmaDespachador(){
        btnFirmarDespachador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFirmarDespachador.setVisibility(View.INVISIBLE);
                frameFirmaDespachador.setVisibility(View.INVISIBLE);
                sPadFirmaDespachador.setEnabled(true);//Habilita la firma
                sPadFirmaDespachador.setMinWidth(3f); // Ancho mínimo del trazo
                sPadFirmaDespachador.setMaxWidth(3f); // Ancho máximo del trazo
                rlOpcFirmaDespachador.setVisibility(View.VISIBLE);
            }
        });

        sPadFirmaDespachador.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }
            @Override
            public void onSigned() {
                btnAceptarFirmaDespachador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firmaDespachador = sPadFirmaDespachador.getSignatureBitmap();
                        frameFirmaDespachador.setVisibility(View.VISIBLE);
                        btnFirmarDespachador.setVisibility(View.VISIBLE);
                        sPadFirmaDespachador.setEnabled(false);
                        rlOpcFirmaDespachador.setVisibility(View.GONE);

                        if(isNetworkAvailable()){
                            enviarFirma2(firmaDespachador);
                        }
                        else {
                            int listId = getArguments().getInt("list_id", -1);
                            if (listId == -1) {
                                Toast.makeText(getContext(), "ID no válido", Toast.LENGTH_SHORT).show();
                                requireActivity().finish();
                            } else {
                                dbLocal = new ListasCargueDataBaseHelper(requireContext());
                                listasCargueModel = dbLocal.getListById(listId);
                                Toast.makeText(getContext(), "Firma actualizada", Toast.LENGTH_SHORT).show();
                            }
                            if (listasCargueModel != null) {
                                String firma2Base64 = convertBitmapToBase64(firmaDespachador);
                                listasCargueModel.setFirma_Despachador(firma2Base64);
                                dbLocal.updateList(listasCargueModel);
                            }
                        }
                    }
                });
            }

            @Override
            public void onClear() {
            }
        });
        btnLimpiarFirmaDespachador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Limpia la firma
                sPadFirmaDespachador.clear();
            }
        });
    }
    private void editarFirmaDespachador(){
        firmaDespachador();
        btnLimpiarFirmaDespachador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgFirmaDespachador.setVisibility(View.GONE);
                sPadFirmaDespachador.setEnabled(true);
                btnLimpiarFirmaDespachador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sPadFirmaDespachador.clear();
                    }
                });
            }
        });
        btnEditarFirmaDespachador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPadFirmaDespachador.setVisibility(View.VISIBLE);
                sPadFirmaDespachador.setEnabled(true);
                rlOpcFirmaDespachador.setVisibility(View.VISIBLE);
                imgFirmaDespachador.setVisibility(View.GONE);
            }
        });
        btnOcultarFirmaDespachador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameFirmaDespachador.setVisibility(View.VISIBLE);
                btnFirmarDespachador.setVisibility(View.VISIBLE);
                sPadFirmaDespachador.setEnabled(false);
                rlOpcFirmaDespachador.setVisibility(View.GONE);
                imgFirmaDespachador.setVisibility(View.GONE);
            }
        });
    }
    public void enviarFirma2(Bitmap signature2) {

        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calHoy.getTime());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature2.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        //String id_lista = AgregarListaCargueDescargue.idPosicionLista;
        String finca = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();

        // Generar un nombre único para el archivo en el Storage
        String fileName = "firma_despachador_" + "_" + formattedDate + "_ID_USUARIO:_"+  uid + "_ID_LISTA:_" + idListaPos + ".png";

        // Referencia al Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference firmaRef = storageRef.child(pathFirmasStorage + formattedDate + "/" + fileName);

        // Referencia al archivo en el Firebase Storage
        //StorageReference firmaRef = storageRef.child(pathFirmasStorage + fileName);

        // Subir la firma al Firebase Storage
        //firmaRef.putBytes(imageData)
        firmaRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    // La firma se ha subido correctamente al Storage
                    firmaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Obtener la URL de descarga del archivo
                        String downloadUrl = uri.toString();

                        // Guardar la URL de descarga en Firestore
                        guardarFirma2EnFirestore(downloadUrl);
                    });
                })
                .addOnFailureListener(exception -> {
                    // Manejar el error
                    Toast.makeText(getContext(), "Error al subir la firma", Toast.LENGTH_SHORT).show();
                });
    }
    private void guardarFirma2EnFirestore(String downloadUrl) {
        // Crear un nuevo documento en la colección 'firmas' en Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> firmaData2 = new HashMap<>();

        firmaData2.put("firma_Despachador", downloadUrl);
        firmaData2.put("nombre_Despachador",edtNombreDespachador.getText().toString());
        firmaData2.put("timestamp", FieldValue.serverTimestamp()); // Opcional, para registrar la fecha de la firma

        firestore.collection(pathLista).document(idListaPos).update(firmaData2)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al guardar la firma en Firestore
                    //loadData();
                    Toast.makeText(getContext(), "Firma cargada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error al guardar la firma en Firestore
                    Toast.makeText(getContext(), "Error al guardar la firma en Firestore", Toast.LENGTH_SHORT).show();
                });
    }

    //----------------------------------------------- FIRMA CONDUCTOR -------------------------------------------//
    private void firmaConductor(){
        btnFirmarConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFirmarConductor.setVisibility(View.INVISIBLE);
                frameFirmaConductor.setVisibility(View.INVISIBLE);
                sPadFirmaConductor.setEnabled(true);//Habilita la firma
                sPadFirmaConductor.setMinWidth(3f); // Ancho mínimo del trazo
                sPadFirmaConductor.setMaxWidth(3f); // Ancho máximo del trazo
                rlOpcFirmaConductor.setVisibility(View.VISIBLE);
            }
        });
        sPadFirmaConductor.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                imgFirmaConductor.setVisibility(View.GONE);
            }
            @Override
            public void onSigned() {
                btnAceptarFirmaConductor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firmaConductor = sPadFirmaConductor.getSignatureBitmap();
                        frameFirmaConductor.setVisibility(View.VISIBLE);
                        btnFirmarConductor.setVisibility(View.VISIBLE);
                        sPadFirmaConductor.setEnabled(false);
                        rlOpcFirmaConductor.setVisibility(View.GONE);

                        if(isNetworkAvailable()){
                            enviarFirma3(firmaConductor);
                        }
                        else {
                            int listId = getArguments().getInt("list_id", -1);
                            if (listId == -1) {
                                Toast.makeText(getContext(), "ID no válido", Toast.LENGTH_SHORT).show();
                                requireActivity().finish();
                            } else {
                                dbLocal = new ListasCargueDataBaseHelper(requireContext());
                                listasCargueModel = dbLocal.getListById(listId);
                                Toast.makeText(getContext(), "Firma actualizada", Toast.LENGTH_SHORT).show();
                            }
                            if (listasCargueModel != null) {
                                String firma3Base64 = convertBitmapToBase64(firmaConductor);
                                listasCargueModel.setFirma_Conductor(firma3Base64);
                                dbLocal.updateList(listasCargueModel);
                            }
                        }
                    }
                });
            }
            @Override
            public void onClear() {
            }
        });
        btnLimpiarFirmaConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPadFirmaConductor.clear(); //Limpia la firma
            }
        });
    }
    private void editarFirmaConductor(){
        firmaConductor();
        btnEditarFirmaConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPadFirmaConductor.setVisibility(View.VISIBLE);
                sPadFirmaConductor.setEnabled(true);
                rlOpcFirmaConductor.setVisibility(View.VISIBLE);
                imgFirmaConductor.setVisibility(View.GONE);
            }
        });
        btnLimpiarFirmaConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPadFirmaConductor.clear();
            }
        });

        btnOcultarFirmaConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameFirmaConductor.setVisibility(View.VISIBLE);
                btnFirmarConductor.setVisibility(View.VISIBLE);
                sPadFirmaConductor.setEnabled(false);
                rlOpcFirmaConductor.setVisibility(View.GONE);
                imgFirmaConductor.setVisibility(View.GONE);
            }
        });
    }
    public void enviarFirma3(Bitmap signature3) {

        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calHoy.getTime());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature3.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        //String id_lista = AgregarListaCargueDescargue.idPosicionLista;
        String finca = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();

        // Generar un nombre único para el archivo en el Storage
        String fileName = "firma_conductor_" + "_" + formattedDate + "_ID_USUARIO:_"+  uid + "_ID_LISTA:_" + idListaPos + ".png";

        // Referencia al Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference firmaRef = storageRef.child(pathFirmasStorage + formattedDate + "/" + fileName);
        // Referencia al archivo en el Firebase Storage
        //StorageReference firmaRef = storageRef.child(pathFirmasStorage + fileName);

        // Subir la firma al Firebase Storage
        //firmaRef.putBytes(imageData)
        firmaRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    // La firma se ha subido correctamente al Storage
                    firmaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Obtener la URL de descarga del archivo
                        String downloadUrl = uri.toString();

                        // Guardar la URL de descarga en Firestore
                        guardarFirma3EnFirestore(downloadUrl);
                    });
                })
                .addOnFailureListener(exception -> {
                    // Manejar el error
                    Toast.makeText(getContext(), "Error al subir la firma", Toast.LENGTH_SHORT).show();
                });
    }
    private void guardarFirma3EnFirestore(String downloadUrl) {
        // Crear un nuevo documento en la colección 'firmas' en Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> firmaData3 = new HashMap<>();

        firmaData3.put("firma_Conductor", downloadUrl);
        firmaData3.put("nombre_Conductor",edtNombreConductor.getText().toString());
        firmaData3.put("timestamp", FieldValue.serverTimestamp()); // Opcional, para registrar la fecha de la firma

        firestore.collection(pathLista).document(idListaPos).update(firmaData3)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al guardar la firma en Firestore
                    //loadData();
                    Toast.makeText(getContext(), "Firma cargada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error al guardar la firma en Firestore
                    Toast.makeText(getContext(), "Error al guardar firma", Toast.LENGTH_SHORT).show();
                });
    }

    //----------------------------------------------- FIRMA OPERADOR -------------------------------------------//
    private void firmaOperador(){
        btnFirmarOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFirmarOperador.setVisibility(View.INVISIBLE);
                frameFirmaOperador.setVisibility(View.INVISIBLE);
                sPadFirmaOperadorCargue.setEnabled(true); //Habilita la firma
                sPadFirmaOperadorCargue.setMinWidth(3f); // Ancho mínimo del trazo
                sPadFirmaOperadorCargue.setMaxWidth(3f); // Ancho máximo del trazo
                rlOpcFirmaOperador.setVisibility(View.VISIBLE);
            }
        });

        sPadFirmaOperadorCargue.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }
            @Override
            public void onSigned() {
                btnAceptarFirmaOperador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firmaOperador = sPadFirmaOperadorCargue.getSignatureBitmap();
                        frameFirmaOperador.setVisibility(View.VISIBLE);
                        btnFirmarOperador.setVisibility(View.VISIBLE);
                        sPadFirmaOperadorCargue.setEnabled(false);
                        rlOpcFirmaOperador.setVisibility(View.GONE);

                        if(isNetworkAvailable()){
                            enviarFirma4(firmaOperador);
                        }
                        else {
                            int listId = getArguments().getInt("list_id", -1);
                            if (listId == -1) {
                                Toast.makeText(getContext(), "ID no válido", Toast.LENGTH_SHORT).show();
                                requireActivity().finish();
                            } else {
                                dbLocal = new ListasCargueDataBaseHelper(requireContext());
                                listasCargueModel = dbLocal.getListById(listId);
                                Toast.makeText(getContext(), "Firma actualizada", Toast.LENGTH_SHORT).show();
                            }
                            if (listasCargueModel != null) {
                                String firma4Base64 = convertBitmapToBase64(firmaOperador);
                                listasCargueModel.setFirma_Operador(firma4Base64);
                                dbLocal.updateList(listasCargueModel);
                            }
                        }
                    }
                });
            }

            @Override
            public void onClear() {

            }
        });

        btnLimpiarFirmaOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Limpia la firma
                sPadFirmaOperadorCargue.clear();
            }
        });
    }
    private void editarFirmaOperador(){
        firmaOperador();
        btnEditarFirmaOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPadFirmaOperadorCargue.setVisibility(View.VISIBLE);
                sPadFirmaOperadorCargue.setEnabled(true);
                rlOpcFirmaOperador.setVisibility(View.VISIBLE);
                imgFirmaOperador.setVisibility(View.GONE);
            }
        });

        btnLimpiarFirmaOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPadFirmaOperadorCargue.clear();
            }
        });

        btnOcultarFirmaOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameFirmaOperador.setVisibility(View.VISIBLE);
                btnFirmarOperador.setVisibility(View.VISIBLE);
                sPadFirmaOperadorCargue.setEnabled(false);
                rlOpcFirmaOperador.setVisibility(View.GONE);
                imgFirmaOperador.setVisibility(View.GONE);
            }
        });

    }
    public void enviarFirma4(Bitmap signature4) {
        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calHoy.getTime());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature4.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        String finca = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();

        // Generar un nombre único para el archivo en el Storage
        String fileName = "firma_operador_" + "_" + formattedDate + "_ID_USUARIO:_"+  uid + "_ID_LISTA:_" + idListaPos + ".png";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();// Referencia al Firebase Storage
        StorageReference firmaRef = storageRef.child(pathFirmasStorage + formattedDate + "/" + fileName);
        // Referencia al archivo en el Firebase Storage
        //StorageReference firmaRef = storageRef.child(pathFirmasStorage + fileName);

        // Subir la firma al Firebase Storage
        //firmaRef.putBytes(imageData)
        firmaRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    // La firma se ha subido correctamente al Storage
                    firmaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Obtener la URL de descarga del archivo
                        String downloadUrl = uri.toString();

                        // Guardar la URL de descarga en Firestore
                        guardarFirma4EnFirestore(downloadUrl);
                    });
                })
                .addOnFailureListener(exception -> {
                    // Manejar el error
                    Toast.makeText(getContext(), "Error al guardar firma", Toast.LENGTH_SHORT).show();
                });
    }
    private void guardarFirma4EnFirestore(String downloadUrl) {
        // Crear un nuevo documento en la colección 'firmas' en Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> firmaData3 = new HashMap<>();

        firmaData3.put("firma_Operador", downloadUrl);
        firmaData3.put("nombre_Operador",edtNombreOperador.getText().toString());
        firmaData3.put("timestamp", FieldValue.serverTimestamp()); // Opcional, para registrar la fecha de la firma

        firestore.collection(pathLista).document(idListaPos).update(firmaData3)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al guardar la firma en Firestore

                    //loadData();
                    Toast.makeText(getContext(), "Firma cargada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error al guardar la firma en Firestore
                    Toast.makeText(getContext(), "Error al guardar firma", Toast.LENGTH_SHORT).show();
                });
    }

    private void showMessage(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }
    private boolean verificarCampos() {
        if(TextUtils.isEmpty(edtNombreConductor.getText())){
            showMessage("Debes ingresar el nombre del conductor");
            return false;
        }

        if (TextUtils.isEmpty(edtNombreDespachador.getText())) {
            showMessage("Debes ingresar el nombre del despachador");
            return false;
        }

        if (TextUtils.isEmpty(edtNombreConductor.getText())) {
            showMessage("Debes ingresar el nombre del conductor");
            return false;
        }

        if (TextUtils.isEmpty(edtNombreOperador.getText())) {
            showMessage("Debes ingresar el nombre del operador de cargue");
            return false;
        }

        if(sPadFirmaSupervisor == null){
            showMessage("Debe firmar el supervisor");
            return false;
        }

        if(sPadFirmaDespachador == null){
            showMessage("Debe firmar el despachador");
            return false;
        }

        if(sPadFirmaConductor == null){
            showMessage("Debe firmar el conductor");
            return false;
        }

        if(sPadFirmaOperadorCargue == null){
            showMessage("Debe firmar el operador de cargue");
            return false;
        }
        return true;
    }

    public interface OnEstadoCamposEditarFirmas {
        void onEstadoCamposEditarFirmas(boolean camposCompletos);
    }
    private OnEstadoCamposEditarFirmas listener5;

    private void actualizarEstadoLista() {
        boolean camposCompletos = verificarCampos();
        if (listener5 != null) {
            listener5.onEstadoCamposEditarFirmas(camposCompletos);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnEstadoCamposEditarFirmas) {
            listener5 = (OnEstadoCamposEditarFirmas) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEstadoCamposFirmasListener");
        }
    }


/*
    private void obtenerCoordenadasFirmas(){

        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection(pathLista)
                .document(id_posLista);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Obtener los datos de trazos
                String signatureSvg = documentSnapshot.getString("firma_Supervisor");

                // Volver a dibujar la firma en tu Signature Pad
                sPadFirmaSupervisor.setSignatureSvg(signatureSvg);
            } else {
                // El documento no existe
                Log.d("", "El documento no existe");
            }
        }).addOnFailureListener(e -> {
            // Manejar el error
            Log.d("", "Error al obtener las firmas", e);
        });
    }

 */
}