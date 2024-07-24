package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Llenar_Listas_Cargue;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class InfoLugarCargueFragment extends Fragment {
    View rootView;
    private Button btnChequeadoItem1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog = null;
    private TextInputEditText edtFecha, edtHora;
    private AutoCompleteTextView edtTipoCargue, edtNombreZona, edtNombreNucleo, edtNombreFinca;
    Dialog dialog;
    private String pathLista = "Listas chequeo cargue descargue";
    private ListasCargueModel.InfoLugarCargue  infoLugarCargueModel = new ListasCargueModel.InfoLugarCargue();
    //private EstadoListaHandler estadoListaHandler;
    private String fecha,horaEntrada,tipoCargue,zona,nucleo,finca;
    private DatosListener datosListener;
    public interface DatosListener {
        void onEstadoCamposActualizado(boolean camposCompletos);

        void onDatosEnviados(String id, String hora, String fecha, String tipoCargue, String zona, String nucleo, String finca,
                             String nombreConductor, String cedula, String lugarExpedicion, String licConduccion,
                             String polizaRCE, String epsRes, String arlRes, String afpRes, String cualEps, String cualArl, String cualAfp,
                             String placa, String vehiculo, String tarjetaPropiedad, String soat, String revisionTecnicomecanica, String lucesAltas,
                             String lucesBajas, String direccionales, String sonidoReversa, String reversa, String stop, String retrovisores,
                             String plumillas, String estadoPanoramicos, String espejos, String bocina, String cinturon, String freno, String llantas,
                             String botiquin, String extintorABC, String botas, String chaleco, String casco, String carroceria, String dosEslingasBanco,
                             String dosConosReflectivos, String parales, String observaciones,
                             String horaSalida, String maderaNoSuperaMampara, String maderaNoSuperaParales, String noMaderaAtravieseMampara,
                             String paralesMismaAltura, String ningunaUndSobrepasaParales, String cadaBancoAseguradoEslingas, String carroceriaParalesBuenEstado,
                             String conductorSalioLugarCinturon, String paralesAbatiblesAseguradosEstrobos, String fotoCamion,
                             String nombreSupervisorFirma, String nombreDespachadorFirma, String nombreConductorFirma, String nombreOperadorFirma,
                             String firmaSupervisor, String firmaDespachador, String firmaConductor, String firmaOperador); //,byte[] fotoCamion);
    }

    interface DatosListener2 {
        void recibirDatos2(String id, String hora, String fecha, String tipoCargue, String zona, String nucleo, String finca);
    }

    public interface OnEstadoCamposActualizadoListener {
        void onEstadoCamposActualizado(boolean camposCompletos);
    }

    private OnEstadoCamposActualizadoListener listener;

    public InfoLugarCargueFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_info_lugar_cargue, container, false);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment lista_item1 = new InfoLugarCargueFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        btnChequeadoItem1 = rootView.findViewById(R.id.btnChequeadoItem1);
        edtHora = rootView.findViewById(R.id.edtHoraLlegada);
        edtFecha = rootView.findViewById(R.id.edtFecha);
        edtTipoCargue = rootView.findViewById(R.id.edtTipoCargue);
        edtNombreZona = rootView.findViewById(R.id.edtNombreZona);
        edtNombreNucleo = rootView.findViewById(R.id.edtNombreNucleo);
        edtNombreFinca = rootView.findViewById(R.id.edtNombreFinca);

        //estadoListaHandler = new ViewModelProvider(requireActivity()).get(EstadoListaHandler.class);

        /*
        estadoListaHandler.getContadorDatos().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer nuevoContador) {
                String estado = obtenerEstadoLista(nuevoContador);
                Log.d("Estado", "El nuevo contador es: " + nuevoContador);
                Log.d("Estado", "Estado de la lista: " + estado);
            }
        });
        */

        edtHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirHora(v);
            }
        });

        int colorNegro = ContextCompat.getColor(getContext(), R.color.black);
        Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formatoFecha.format(fechaActual);
        edtFecha.setText(fechaFormateada);
        edtFecha.setTextColor(colorNegro);
        edtFecha.setEnabled(false);

        btnChequeadoItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarDatos();

                //if (verificarCampos()){

                    if(isNetworkAvailable()){
                        enviarDatosFirebase();
                        actualizarEstadoLista();
                    }
                    else {
                        enviarDatosBDLocal();
                        actualizarEstadoLista();
                    }
                //}
                //estadoListaHandler.agregarDato();
            }
        });
        obtenerDesplegablesInfoLugarCargue();
        verificarCampos();
        return rootView;
    }

    private void cargarDatos(){
       fecha = edtFecha.getText().toString();
       horaEntrada = edtHora.getText().toString();
       tipoCargue = edtTipoCargue.getText().toString();
       zona = edtNombreZona.getText().toString();
       nucleo = edtNombreNucleo.getText().toString();
       finca = edtNombreFinca.getText().toString();

       infoLugarCargueModel.setFecha(fecha);
       infoLugarCargueModel.setHoraEntrada(horaEntrada);
       infoLugarCargueModel.setTipoCargue(tipoCargue);
       infoLugarCargueModel.setNombreZona(zona);
       infoLugarCargueModel.setNombreNucleo(nucleo);
       infoLugarCargueModel.setNombreFinca(finca);
    }

    private void enviarDatosBDLocal(){
        String id = UUID.randomUUID().toString();

        if (requireActivity() instanceof DatosListener2) {
            ((DatosListener2) requireActivity()).recibirDatos2(id,horaEntrada,fecha, tipoCargue,zona,nucleo,finca);
        }

        if (datosListener != null) {
            datosListener.onDatosEnviados(id, horaEntrada, fecha, tipoCargue, zona, nucleo, finca,
                    "","","","","",
                    "","","","","","","","","","",
                    "" ,"","","","","","",
                    "","","","","","","","","",
                    "","","","","","","","","",
                    "", "","","","",
                    "", "","","",
                    "","","","","",
                    "","","","",""); //,"".getBytes());
            Toast.makeText(getContext(), "Datos almacenados correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("LongLogTag")
    private void enviarDatosFirebase(){
        String id_lista = AgregarMostrarListas.idListaStatic;
        if (isNetworkAvailable()) { // Verifica si hay conexión a Internet
            db.collection(pathLista).document(id_lista).update("Item_1_Informacion_lugar_cargue", infoLugarCargueModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Datos almacenados correctamente", Toast.LENGTH_SHORT).show();

                    // Aquí puedes llamar al método writeDataToExcel de la clase ExcelUtils
                    //File excelFile = new File(getContext().getExternalFilesDir(null), "LISTA_CHEQUEO_CARGUE_NUEVA.xlsx");
                    //try {
                    //    ExcelJava.createExcelFromTemplate(getContext(), infoLugarCargueModel.getFecha(),
                    //            infoLugarCargueModel.getHoraEntrada(), infoLugarCargueModel.getTipoCargue(),
                    //            infoLugarCargueModel.getNombreZona(), infoLugarCargueModel.getNombreNucleo(),
                    //            infoLugarCargueModel.getNombreFinca());
                    //} catch (IOException e) {
                    //    e.printStackTrace();
                    //}
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "No se almacenaron los datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getContext(), "Trabajando offline. Los cambios se guardarán cuando recuperes la conexión a Internet.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para verificar la disponibilidad de la red
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void abrirHora(View view){
        Calendar horaCalendario = Calendar.getInstance();

        int hora = horaCalendario.get(Calendar.HOUR_OF_DAY);
        int min = horaCalendario.get(Calendar.MINUTE);
        TimePickerDialog tmd = new TimePickerDialog(getContext(),R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String formatoHora;
                if(hourOfDay >=12) {
                    formatoHora = "pm";
                }
                else{
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
        }, hora,min,false);
        tmd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        tmd.show();
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

    private void actualizarEstadoLista() {
        boolean camposCompletos = verificarCampos();
        if (listener != null) {
            listener.onEstadoCamposActualizado(camposCompletos);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        datosListener = null;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DatosListener) {
            datosListener = (DatosListener) context;
        } else {
            throw new RuntimeException("La actividad debe implementar la interfaz DatosListener");
        }

        if (context instanceof OnEstadoCamposActualizadoListener) {
            listener = (OnEstadoCamposActualizadoListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEstadoCamposActualizadoListener");
        }
    }
}