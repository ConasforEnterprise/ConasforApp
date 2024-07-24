package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Llenar_Listas_Cargue;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InfoConductorFragment extends Fragment {
    private View view;
    private TextInputEditText edtNombreConductor, edtCedula, edtLugarExpedicion;
    private Button btnAgregarDatosInfoConductor;
    private AutoCompleteTextView atcEps, atcArl, atcAfp;
    private RadioGroup rdgLicConduccion, rdgPolizaRCE, rdgAfiliacionSGSSS, rdgEPS, rdgARL, rdgAFP;
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    Map<String, Object> datosInfoConductor = new HashMap<>();
    //private infoConductorModel infoConductor = new infoConductorModel();
    private ListasCargueModel listasCargueModel = new ListasCargueModel();
    private String idCapturado;
    private String pathLista = "Listas chequeo cargue descargue";

    private String idRecibido = null;
    private String horaRecibido  = null;
    private String fechaRecibido  = null;
    private String tipoCargueRecibido  = null;
    private String zonaRecibido  = null;
    private String nucleoRecibido  = null;
    private String fincaRecibido  = null;

    private RadioButton rbNoLicencia;

    private RadioButton rbLicConduccion, rbPolizaRCE, rbEps, rbArl, rbAfp;

    private DatosListener2 datosListener2;

    public InfoConductorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_info_conductor, container, false);

        edtNombreConductor = view.findViewById(R.id.edtNombreConductor);
        edtCedula = view.findViewById(R.id.edtCedulaConductor);
        edtLugarExpedicion = view.findViewById(R.id.edtLugarExpedicionConductor);

        rdgLicConduccion = view.findViewById(R.id.rdgLicConduccion);
        rdgPolizaRCE = view.findViewById(R.id.rdgPolizaRCE);
        rdgEPS = view.findViewById(R.id.rdgEPS);
        rdgARL = view.findViewById(R.id.rdgARL);
        rdgAFP = view.findViewById(R.id.rdgAFP);

        atcEps = view.findViewById(R.id.atcEPS);
        atcArl = view.findViewById(R.id.atcARL);
        atcAfp = view.findViewById(R.id.atcAFP);


        btnAgregarDatosInfoConductor = view.findViewById(R.id.btnAgregarDatosInfoConductor);
        btnAgregarDatosInfoConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarDatosInfoConductor();
                //verificarCampos();
            }
        });

        //obtenerDatosInfoConductor();
        obtenerDesplegablesInfoConductor();

        //Log.d("ID RECIBIDO","ID RECIBIDO INFO CONDUCTOR : "+idRecibido);

        return view;
    }

    public void recibirDatos2(String id, String horaEntrada, String fecha, String tipoCargue,String zona, String nucleo, String finca) {
        this.idRecibido = id;
        this.horaRecibido = horaEntrada;
        this.fechaRecibido = fecha;
        this.tipoCargueRecibido = tipoCargue;
        this.zonaRecibido = zona;
        this.nucleoRecibido = nucleo;
        this.fincaRecibido = finca;

        Log.d("ID RECIBIDO ","ID RECIBIDO INFO CONDUCTOR : "+ idRecibido);
        Log.d("HORA RECIBIDA","HORA RECIBIDA : " + horaRecibido);
        Log.d("FECHA RECIBIDA","FECHA RECIBIDA : " + fechaRecibido);
        Log.d("TIPO CARGUE RECIBIDO","TIPO CARGUE RECIBIDO : " + tipoCargueRecibido);
        Log.d("ZONA RECIBIDA","ZONA RECIBIDA : " + zonaRecibido);
        Log.d("NUCLEO RECIBIDO","NUCLEO RECIBIDO : " + nucleoRecibido);
        Log.d("FINCA RECIBIDA","FINCA RECIBIDA : " + fincaRecibido);
    }

    public interface DatosListener2{
        void onDatosEnviados(String id, String hora, String fecha, String tipoCargue, String zona, String nucleo, String finca,
                             String nombreConductor, String cedula, String lugarExpedicion,String licConduccion,
                             String polizaRCE, String epsRes, String arlRes, String afpRes, String cualEps, String cualArl, String cualAfp,
                             String placa,String vehiculo,String tarjetaPropiedad,String soat, String revisionTecnicomecanica,String lucesAltas,
                             String lucesBajas, String direccionales,String sonidoReversa, String reversa, String stop, String retrovisores,
                             String plumillas,String estadoPanoramicos, String espejos, String bocina, String cinturon, String freno,String llantas,
                             String botiquin, String extintorABC, String botas, String chaleco, String casco, String carroceria,String dosEslingasBanco,
                             String dosConosReflectivos, String parales, String observaciones,
                             String horaSalida, String maderaNoSuperaMampara, String maderaNoSuperaParales, String noMaderaAtravieseMampara,
                             String paralesMismaAltura, String ningunaUndSobrepasaParales,String cadaBancoAseguradoEslingas, String carroceriaParalesBuenEstado,
                             String conductorSalioLugarCinturon, String paralesAbatiblesAseguradosEstrobos,String fotoCamion,
                             String nombreSupervisorFirma, String nombreDespachadorFirma, String nombreConductorFirma, String nombreOperadorFirma,
                             String firmaSupervisor, String firmaDespachador, String firmaConductor, String firmaOperador);
    }

    interface DatosListener3 {
        void recibirDatos3(String id, String horaEntrada, String fecha, String tipoCargue,String zona,
                           String nucleo, String finca,String nombreConductor,String cedula, String lugarExpedicion,
                           String licConduccion, String polizaRCE, String epsRes,String arlRes, String afpRes,
                           String cualEps, String cualArl, String cualAfp);
    }

    private void agregarDatosInfoConductor(){

        int rdgLicConduccionId = rdgLicConduccion.getCheckedRadioButtonId();
        int rdgPolizaRCEId = rdgPolizaRCE.getCheckedRadioButtonId();
        int rdgEpsId = rdgEPS.getCheckedRadioButtonId();
        int rdgArlId = rdgARL.getCheckedRadioButtonId();
        int rdgAfpId = rdgAFP.getCheckedRadioButtonId();

        // Verificar qué RadioButton fue seleccionado y actualizar el modelo
        if (rdgLicConduccionId == R.id.rbSiLicConduccion) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setLicConduccionRes("Si");

        } else if (rdgLicConduccionId == R.id.rbNoLicConduccion) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setLicConduccionRes("No");

            rbNoLicencia = view.findViewById(R.id.rbNoLicConduccion);

            int colorDeseado = getResources().getColor(R.color.black);
            //ColorStateList colorStateList = ColorStateList.valueOf(colorDeseado);
            //rbNoLicencia.setButtonTintList(colorStateList);
            rbNoLicencia.setTextColor(colorDeseado);
        }

        if (rdgPolizaRCEId == R.id.rbSiPoliza) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setPolizaRCERes("Si");

        } else if (rdgPolizaRCEId == R.id.rbNoPoliza) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setPolizaRCERes("No");
        }

        if (rdgEpsId == R.id.rbSiEPS) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setEpsRes("Si");

        } else if (rdgEpsId == R.id.rbNoEPS) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setEpsRes("No");

        }
        if (rdgArlId == R.id.rbSiARL) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setArlRes("Si");

        } else if (rdgArlId == R.id.rbNoARL) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setArlRes("No");
        }

        if (rdgAfpId == R.id.rbSiAFP) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setAfpRes("Si");

        } else if (rdgAfpId == R.id.rbNoAFP) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setAfpRes("No");
        }

        String respuestaLicConduccion = listasCargueModel.getItem_2_Informacion_del_conductor().getLicConduccionRes();
        String respuestaPoliza = listasCargueModel.getItem_2_Informacion_del_conductor().getPolizaRCERes();
        String respuestaEps = listasCargueModel.getItem_2_Informacion_del_conductor().getEpsRes();
        String respuestaArl = listasCargueModel.getItem_2_Informacion_del_conductor().getArlRes();
        String respuestaAfp = listasCargueModel.getItem_2_Informacion_del_conductor().getAfpRes();

        datosInfoConductor.put("nombreConductor", Objects.requireNonNull(edtNombreConductor.getText()).toString());
        datosInfoConductor.put("cedula", Objects.requireNonNull(edtCedula.getText()).toString());
        datosInfoConductor.put("lugarExpedicion", Objects.requireNonNull(edtLugarExpedicion.getText()).toString());
        datosInfoConductor.put("licConduccionRes",respuestaLicConduccion);
        datosInfoConductor.put("polizaRCERes",respuestaPoliza);
        datosInfoConductor.put("epsRes",respuestaEps);
        datosInfoConductor.put("arlRes",respuestaArl);
        datosInfoConductor.put("afpRes",respuestaAfp);
        datosInfoConductor.put("cualEPS", Objects.requireNonNull(atcEps.getText()).toString());
        datosInfoConductor.put("cualARL", Objects.requireNonNull(atcArl.getText()).toString());
        datosInfoConductor.put("cualAFP", Objects.requireNonNull(atcAfp.getText()).toString());

        Log.d("Datos Info Conductor ","Datos Info Conductor : " + datosInfoConductor);

        //String id_lista = AgregarListaCargueDescargue.idListaStatic;
        String id_lista = AgregarMostrarListas.idListaStatic;
        Log.d("TAG ID LISTA EN INFO CONDUCTOR","ID LISTA EN INFO CONDUCTOR : "+ id_lista);

        if(isNetworkAvailable()){
            if(id_lista != null){
                db.collection(pathLista)
                    .document(id_lista)
                    .update("Item_2_Informacion_del_conductor", datosInfoConductor).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            actualizarEstadoLista();
                            Toast.makeText(getContext(), "Datos almacenados correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "No se almacenaron los datos", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        }
        else{
            actualizarEstadoLista();
            String id = idRecibido;
            String hora = horaRecibido;
            String fecha = fechaRecibido;
            String tipoCargue = tipoCargueRecibido;
            String zona = zonaRecibido;
            String nucleo = nucleoRecibido;
            String finca = fincaRecibido;

            String nombreConductor = edtNombreConductor.getText().toString();
            String cedula = edtCedula.getText().toString();
            String lugarExpedicion = edtLugarExpedicion.getText().toString();
            String licConduccion = respuestaLicConduccion;
            String polizaRCE = respuestaPoliza;
            String epsRes = respuestaEps;
            String arlRes = respuestaArl;
            String afpRes = respuestaAfp;
            String cualEps = atcEps.getText().toString();
            String cualArl = atcArl.getText().toString();
            String cualAfp = atcAfp.getText().toString();

            /*
            Log.d("ID","ID F2: " + id);
            Log.d("HORA ","HORA  F2: " + hora);
            Log.d("FEHCA","FEHCA F2: " + fecha);
            Log.d("TIPO CARGUE","TIPO CARGUE F2: " + tipoCargue);
            Log.d("ZONA","ZONA F2: " + zona);
            Log.d("NUCLEO","NUCLEO F2: " + nucleo);
            Log.d("FINCA","FINCA F2: " + finca);
            Log.d("CEDULA","CEDULA : " + cedula);
            Log.d("LUGAR EXPEDICION","LUGAR EXPEDICION : " + lugarExpedicion);
            Log.d("LIC CONDUCCION","LIC CONDUCCION : " + licConduccion);
            Log.d("POLIZA RCE","POLIZA RCE : " + polizaRCE);
            Log.d("EPS RES","EPS RES : " + epsRes);
            Log.d("ARL RES","ARL RES : " + arlRes);
            Log.d("AFP RES","AFP RES : " + afpRes);
            Log.d("CUAL EPS","CUAL EPS : " + cualEps);
            Log.d("CUAL ARL","CUAL ARL : " + cualArl);
            Log.d("CUAL AFP","CUAL AFP : " + cualAfp);

             */

            if (requireActivity() instanceof DatosListener3) {
                ((DatosListener3) requireActivity()).recibirDatos3(id,hora,fecha, tipoCargue,zona,
                        nucleo,finca,nombreConductor,cedula,lugarExpedicion,licConduccion,polizaRCE,epsRes,arlRes,afpRes,
                        cualEps,cualArl,cualAfp);
            }

            if (datosListener2 != null) {
                datosListener2.onDatosEnviados(id,hora,fecha, tipoCargue,zona, nucleo,finca,
                        nombreConductor,cedula,lugarExpedicion,licConduccion,polizaRCE,epsRes,arlRes,afpRes,cualEps,cualArl,cualAfp,
                        "","", "","","", "","","","",
                        "","","","","","","","","","","",
                        "","","","","","","","","","",
                        "","","","","",
                        "","","","","",
                        "","","","","","",
                        "","");//, "".getBytes());
                Toast.makeText(getContext(), "Datos almacenados correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void obtenerDesplegablesInfoConductor(){
        db.collection("desplegables info conductor")
                //.get(Source.CACHE)
                .get(Source.DEFAULT)
                .addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                List<String> AFPList = new ArrayList<>();
                List<String> ARLList = new ArrayList<>();
                List<String> EPSList = new ArrayList<>();

                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){

                    //Obtiene los datos de firestore
                    List<String> AFP = (List<String>) documentSnapshot.get("AFP");
                    List<String> ARL = (List<String>) documentSnapshot.get("ARL");
                    List<String> EPS = (List<String>) documentSnapshot.get("EPS");

                    //Se pasan los datos obtenidos a las ArrayList declaradas previamente
                    if (AFP != null){
                        AFPList.addAll(AFP);
                    }

                    if (ARL != null){
                        ARLList.addAll(ARL);
                    }

                    if (EPS != null){
                        EPSList.addAll(EPS);
                    }
                }

                Context context = getContext();
                if (context != null) {

                    ArrayAdapter<String> adapterAFP = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, AFPList);
                    atcAfp.setAdapter(adapterAFP);

                    ArrayAdapter<String> adapterARL = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, ARLList);
                    atcArl.setAdapter(adapterARL);

                    ArrayAdapter<String> adapterEPS = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, EPSList);
                    atcEps.setAdapter(adapterEPS);
                }
            }
            Toast.makeText(getContext(), "Se están recopilando algunos datos", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showMessage(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }
    private boolean verificarCampos() {
        if(TextUtils.isEmpty(edtNombreConductor.getText())){
            showMessage("Debes ingresar el nombre del conductor");
            return false;
        }

        if(TextUtils.isEmpty(edtCedula.getText())){
            showMessage("Debes ingresar la cédula");
            return false;
        }

        if(TextUtils.isEmpty(edtLugarExpedicion.getText())){
            showMessage("Debes seleccionar el lugar de expedición");
            return false;
        }

        if (rdgLicConduccion.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el RadioGroup");
            return false;
        }


        if (rdgPolizaRCE.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el RadioGroup");
            return false;
        }

        if (rdgEPS.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el RadioGroup");
            return false;
        }

        if (rdgARL.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el RadioGroup");
            return false;
        }
        if (rdgAFP.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el RadioGroup");
            return false;
        }


        if(TextUtils.isEmpty(atcEps.getText())){
            showMessage("Debes seleccionar la EPS");
            return false;
        }

        if(TextUtils.isEmpty(atcArl.getText())){
            showMessage("Debes seleccionar la ARL");
            return false;
        }

        if(TextUtils.isEmpty(atcAfp.getText())){
            showMessage("Debes seleccionar la AFP");
            return false;
        }
        return true;
    }

    public interface OnEstadoCamposActualizadoListener2 {
        void onEstadoCamposActualizado2(boolean camposCompletos);
    }

    private OnEstadoCamposActualizadoListener2 listener2;

    private void actualizarEstadoLista() {
        boolean camposCompletos = verificarCampos();
        if (listener2 != null) {
            listener2.onEstadoCamposActualizado2(camposCompletos);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        datosListener2 = null;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DatosListener2) {
            datosListener2 = (DatosListener2) context;
        } else {
            throw new RuntimeException("La actividad debe implementar la interfaz DatosListener");
        }

        if (context instanceof OnEstadoCamposActualizadoListener2) {
            listener2 = (OnEstadoCamposActualizadoListener2) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEstadoCamposInfoConductorListener");
        }
    }
}