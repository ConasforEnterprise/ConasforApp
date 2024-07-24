package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Editar_Listas_Cargue;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditarInfoLugarCargue extends Fragment{
    private FirebaseFirestore db;
    TextInputEditText edtFecha, edtHora;
    AutoCompleteTextView edtTipoCargue, edtNombreZona, edtNombreNucleo, edtNombreFinca;
    private String pathLista = "Listas chequeo cargue descargue";
    Button btnActualizarDatos;
    FrameLayout framePadreInfoLugar;
    TextView txtEncabezado;
    ImageView imgInfoLugarCargue;
    private int listId = -1;
    private ListasCargueModel listasCargueModel = new ListasCargueModel();
    private ListasCargueDataBaseHelper dbLocal;
    String hora = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getHoraEntrada();
    String fecha = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getFecha();
    String tipoCargue = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getTipoCargue();
    String zona = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreZona();
    String nucleo = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreNucleo();
    String finca = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();
    String idListaPos = null;

    public EditarInfoLugarCargue() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_editar_info_lugar_cargue, container, false);

        btnActualizarDatos = rootView.findViewById(R.id.btnActualizarDatosCargueDescargueEditar);

        db = FirebaseFirestore.getInstance();

        edtHora = rootView.findViewById(R.id.edtHoraLlegadaEditar);
        edtFecha = rootView.findViewById(R.id.edtFechaEditar);
        edtTipoCargue = rootView.findViewById(R.id.edtTipoCargueEditar);
        edtNombreZona = rootView.findViewById(R.id.edtNombreZonaEditar);
        edtNombreNucleo = rootView.findViewById(R.id.edtNombreNucleoEditar);
        edtNombreFinca = rootView.findViewById(R.id.edtNombreFincaEditar);
        txtEncabezado = rootView.findViewById(R.id.txtEncabezadoListaCargueDescargueEdit);
        framePadreInfoLugar = rootView.findViewById(R.id.bSheetInfoLugarCarguePadre);
        imgInfoLugarCargue = rootView.findViewById(R.id.imgInfoLugar);

        edtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirHora(v);
            }
        });

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
        btnActualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatosInfoLugarCargue();
            }
        });

        int listId = 0;

        if (!isNetworkAvailable()){
            listId = getArguments().getInt("list_id", -1);

            Log.d("ID LISTA LOCAL EDITAR INFO LUGAR", "ID LISTA LOCAL EDITAR INFO LUGAR : " + listId);
            Toast.makeText(requireContext(), "ID: " + listId, Toast.LENGTH_SHORT).show();

            if (listId == -1) {
                Toast.makeText(getContext(), "ID no válido", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
        }
            dbLocal = new ListasCargueDataBaseHelper(requireContext());
            loadDataDBLocal(listId);
        }
        else {

            loadDataFirebase();

            // Obtener los argumentos del Bundle
            Bundle args = getArguments();
            if (args != null) {
                idListaPos = args.getString("idPos");
                Log.d("ID LISTA POS EN INFO LUGAR","ID LISTA POS EN INFO LUGAR : "+idListaPos);

            }
        }

        obtenerDesplegablesInfoLugarCargue();
        return rootView;
    }

    public static EditarInfoLugarCargue newInstance(String idLista) {
        Bundle args = new Bundle();
        args.putString("idPos", idLista);
        EditarInfoLugarCargue fragment = new EditarInfoLugarCargue();
        fragment.setArguments(args);
        return fragment;
    }


    private void ocultarFrameWithOutConnection() {

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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadDataDBLocal(Integer listId){

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

    @SuppressLint("ResourceAsColor")
    private void loadDataFirebase() {
        int colorNegro = ContextCompat.getColor(getContext(), R.color.black);
        edtHora.setText(hora);
        edtFecha.setText(fecha);
        edtFecha.setTextColor(colorNegro);
        edtTipoCargue.setText(tipoCargue);
        edtNombreZona.setText(zona);
        edtNombreNucleo.setText(nucleo);
        edtNombreFinca.setText(finca);
    }

    @SuppressLint("LongLogTag")
    public void actualizarDatosInfoLugarCargue() {
        ListasCargueModel.InfoLugarCargue infoLugarCargue = new ListasCargueModel.InfoLugarCargue();

        infoLugarCargue.setHoraEntrada(edtHora.getText().toString());
        infoLugarCargue.setFecha(edtFecha.getText().toString());
        infoLugarCargue.setTipoCargue(edtTipoCargue.getText().toString());
        infoLugarCargue.setNombreZona(edtNombreZona.getText().toString());
        infoLugarCargue.setNombreNucleo(edtNombreNucleo.getText().toString());
        infoLugarCargue.setNombreFinca(edtNombreFinca.getText().toString());

        if (isNetworkAvailable()){
            if(idListaPos != null){
                db.collection(pathLista)
                        .document(idListaPos)
                        .update("Item_1_Informacion_lugar_cargue" ,infoLugarCargue)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Datos actualizados con éxito", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "No se actualizaron los datos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        else if(!isNetworkAvailable()){
            actualizarEstadoLista();

            ListasCargueModel listasCargueModel1 = new ListasCargueModel();

            int listId = getArguments().getInt("list_id", -1);

            if (listId == -1) {
                Toast.makeText(getContext(), "ID no válido", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            } else {
                dbLocal = new ListasCargueDataBaseHelper(requireContext());
                listasCargueModel1 = dbLocal.getListById(listId);
                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            }

            if (listasCargueModel1 != null) {
                listasCargueModel1.getItem_1_Informacion_lugar_cargue().setHoraEntrada(edtHora.getText().toString());
                listasCargueModel1.getItem_1_Informacion_lugar_cargue().setFecha(edtFecha.getText().toString());
                listasCargueModel1.getItem_1_Informacion_lugar_cargue().setTipoCargue(edtTipoCargue.getText().toString());
                listasCargueModel1.getItem_1_Informacion_lugar_cargue().setNombreNucleo(edtNombreNucleo.getText().toString());
                listasCargueModel1.getItem_1_Informacion_lugar_cargue().setNombreZona(edtNombreZona.getText().toString());
                listasCargueModel1.getItem_1_Informacion_lugar_cargue().setNombreFinca(edtNombreFinca.getText().toString());

                dbLocal.updateList(listasCargueModel1);
            }
        }
    }

    private void obtenerDesplegablesInfoLugarCargue(){

        db.collection("desplegables infoLugar")
                .get(Source.DEFAULT)
                .addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                List<String> tiposList = new ArrayList<>();
                List<String> nombresZona = new ArrayList<>();
                List<String> nombresNucleo = new ArrayList<>();
                List<String> nombresFinca = new ArrayList<>();

                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){

                    //Obtiene los datos de firestore
                    List<String> tipos = (List<String>) documentSnapshot.get("tipo de cargue");
                    List<String> nombresZ = (List<String>) documentSnapshot.get("nombre zona");
                    List<String> nombresN = (List<String>) documentSnapshot.get("nombre nucleo");
                    List<String> nombresF = (List<String>) documentSnapshot.get("nombre finca");

                    //Se pasan los datos obtenidos a las ArrayList declaradas previamente
                    if (tipos != null){
                        tiposList.addAll(tipos);
                    }

                    if(nombresZ != null){
                        nombresZona.addAll(nombresZ);
                    }

                    if(nombresN != null){
                        nombresNucleo.addAll(nombresN);
                    }

                    if(nombresF != null){
                        nombresFinca.addAll(nombresF);
                    }
                }

                Context context = getContext();
                if (context != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, tiposList);
                    edtTipoCargue.setAdapter(adapter);

                    ArrayAdapter<String> adapterNombreZona = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, nombresZona);
                    edtNombreZona.setAdapter(adapterNombreZona);

                    ArrayAdapter<String> adapterNombreNucleo = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, nombresNucleo);
                    edtNombreNucleo.setAdapter(adapterNombreNucleo);

                    ArrayAdapter<String> adapterNombreFinca = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, nombresFinca);
                    edtNombreFinca.setAdapter(adapterNombreFinca);

                }
            }
            else {
                Toast.makeText(getContext(), "Se están recopilando algunos datos", Toast.LENGTH_SHORT).show();
                //Exception exception = task.getException();
            }
        });
    }

    public void abrirHora(View view) {
        Calendar horaCalendario = Calendar.getInstance();

        int hora = horaCalendario.get(Calendar.HOUR_OF_DAY);
        int min = horaCalendario.get(Calendar.MINUTE);
        TimePickerDialog tmd = new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String formatoHora;
                if (hourOfDay >= 12) {
                    formatoHora = "pm";
                } else {
                    formatoHora = "am";
                }

                // Convertir la hora al formato de 12 horas
                int hour = hourOfDay % 12;
                if (hour == 0) {
                    hour = 12;
                }

                // Actualizar el texto en edtHora
                edtHora.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, formatoHora));
            }
        }, hora, min, false);
        tmd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        tmd.show();
    }

    private void showMessage(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }

    private boolean verificarCampos() {
        if(TextUtils.isEmpty(edtFecha.getText())){
            showMessage("Debes ingresar la fecha");
            return false;
        }

        if(TextUtils.isEmpty(edtHora.getText())){
            showMessage("Debes ingresar la hora");
            return false;
        }

        if(TextUtils.isEmpty(edtTipoCargue.getText())){
            showMessage("Debes seleccionar el tipo de cargue");
            return false;
        }

        if(TextUtils.isEmpty(edtNombreZona.getText())){
            showMessage("Debes seleccionar la zona");
            return false;
        }

        if(TextUtils.isEmpty(edtNombreFinca.getText())){
            showMessage("Debes seleccionar la finca");
            return false;
        }
        return true;
    }

    public interface OnEstadoCamposEditarInfoLugarCargueListener {
        void onEstadoEditarCamposInfoLugarCargue(boolean camposCompletos);
    }
    private OnEstadoCamposEditarInfoLugarCargueListener listener;

    private void actualizarEstadoLista() {
        boolean camposCompletos = verificarCampos();

        if (listener != null) {
            listener.onEstadoEditarCamposInfoLugarCargue(camposCompletos);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnEstadoCamposEditarInfoLugarCargueListener) {
            listener = (OnEstadoCamposEditarInfoLugarCargueListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEstadoEditarCamposActualizadoListener");
        }
    }
}