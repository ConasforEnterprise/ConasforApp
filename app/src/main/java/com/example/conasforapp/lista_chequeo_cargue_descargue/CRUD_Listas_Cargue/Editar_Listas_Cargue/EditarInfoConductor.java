package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Editar_Listas_Cargue;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EditarInfoConductor extends Fragment {
    private View view;
    private TextInputEditText edtNombreConductor, edtCedula, edtLugarExpedicion;
    private Button btnActualizarDatosInfoConductor;
    private AutoCompleteTextView atcEps, atcArl, atcAfp;
    private RadioGroup rdgLicConduccion, rdgPolizaRCE, rdgEPS, rdgARL, rdgAFP;
    private FirebaseFirestore db;
    private String pathLista = "Listas chequeo cargue descargue";
    private ListasCargueModel listasCargueModel = new ListasCargueModel();
    private ImageView imgCerrarDesplegableInfoConductor;
    Map<String, Object> editarDatosInfoConductor = new HashMap<>();
    FrameLayout framePadreInfoConductor,frameHijoInfoConductor;
    private int listId = -1;
    private ListasCargueDataBaseHelper dbLocal;
    String idListaPos = null;
    private String respuestaSi = "Si",respuestaNo = "No";
    public EditarInfoConductor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_editar_info_conductor, container, false);

        db = FirebaseFirestore.getInstance();

        edtNombreConductor = view.findViewById(R.id.edtNombreConductorEditar);
        edtCedula = view.findViewById(R.id.edtCedulaConductorEditar);
        edtLugarExpedicion = view.findViewById(R.id.edtLugarExpedicionConductorEditar);

        rdgLicConduccion = view.findViewById(R.id.rdgLicConduccionEditar);
        rdgPolizaRCE = view.findViewById(R.id.rdgPolizaRCEEditar);

        rdgEPS = view.findViewById(R.id.rdgEPSEditar);
        rdgARL = view.findViewById(R.id.rdgARLEditar);
        rdgAFP = view.findViewById(R.id.rdgAFPEditar);

        atcEps = view.findViewById(R.id.atcEPSEditar);
        atcArl = view.findViewById(R.id.atcARLEditar);
        atcAfp = view.findViewById(R.id.atcAFPEditar);

        framePadreInfoConductor = view.findViewById(R.id.frameInfoConductorEditar);
        frameHijoInfoConductor = view.findViewById(R.id.frameInfoConductorHijoEdit);

        imgCerrarDesplegableInfoConductor = view.findViewById(R.id.imgCerrarInfoConductorEditar);

        btnActualizarDatosInfoConductor = view.findViewById(R.id.btnActualizarDatosInfoConductor);
        btnActualizarDatosInfoConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatosInfoConductor();
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

        if(!isNetworkAvailable()) {
            int listId = getArguments().getInt("list_id", -1);
            Log.d("ID LISTA", "ID LISTA : " + listId);
            Toast.makeText(requireContext(), "ID: " + listId, Toast.LENGTH_SHORT).show();
            if (listId == -1) {
                requireActivity().finish();
            }
            dbLocal = new ListasCargueDataBaseHelper(getContext());
            loadDataDBLocal(listId);
        }
        else{
            loadDataFirebase();
            //obtenerLista();

            // Obtener los argumentos del Bundle
            Bundle args = getArguments();
            if (args != null) {
                idListaPos = args.getString("idPos");
                Log.d("ID LISTA POS EN INFO CONDUCTOR","ID LISTA POS EN INFO CONDUCTOR : " + idListaPos);
            }
        }

        obtenerDesplegablesInfoConductor();

        return view;
    }

    public static EditarInfoConductor newInstance(String idLista) {
        Bundle args = new Bundle();
        args.putString("idPos", idLista);
        EditarInfoConductor fragment = new EditarInfoConductor();
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
    private void loadDataDBLocal(Integer listId){
        listasCargueModel = dbLocal.getListById(listId);

        edtNombreConductor.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getNombreConductor());
        edtCedula.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCedula());
        edtLugarExpedicion.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getLugarExpedicion());
        String licConduccionRes = listasCargueModel.getItem_2_Informacion_del_conductor().getLicConduccionRes();
        String polizaRCERes = listasCargueModel.getItem_2_Informacion_del_conductor().getPolizaRCERes();
        String EPSRes = listasCargueModel.getItem_2_Informacion_del_conductor().getEpsRes();
        String ARLRes = listasCargueModel.getItem_2_Informacion_del_conductor().getArlRes();
        String AFPRes = listasCargueModel.getItem_2_Informacion_del_conductor().getAfpRes();

        //Se chequea el valor de selección que tienen los radiobutton
        if (respuestaSi.equals(licConduccionRes)) {
            rdgLicConduccion.check(R.id.rbSiLicConduccionEditar);
        }
        else if (respuestaNo.equals(licConduccionRes)) {
            rdgLicConduccion.check(R.id.rbNoLicConduccionEditar);
        }
        if (respuestaSi.equals(polizaRCERes)) {
            rdgPolizaRCE.check(R.id.rbSiPolizaEditar);
        }
        else if (respuestaNo.equals(polizaRCERes)) {
            rdgPolizaRCE.check(R.id.rbNoPolizaEditar);
        }
        if (respuestaSi.equals(EPSRes)) {
            rdgEPS.check(R.id.rbSiEPSEditar);
        }
        else if (respuestaNo.equals(EPSRes)) {
            rdgEPS.check(R.id.rbNoEPSEditar);
        }
        if (respuestaSi.equals(ARLRes)) {
            rdgARL.check(R.id.rbSiARLEditar);
        }
        else if (respuestaNo.equals(ARLRes)) {
            rdgARL.check(R.id.rbNoARLEditar);
        }

        if (respuestaSi.equals(AFPRes)) {
            rdgAFP.check(R.id.rbSiAFPEditar);
        }
        else if (respuestaNo.equals(AFPRes)) {
            rdgAFP.check(R.id.rbNoAFPEditar);
        }
        atcEps.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCualEPS());
        atcArl.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCualARL());
        atcAfp.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCualAFP());
    }
    private void loadDataFirebase(){
        //String cargo = AgregarListaCargueDescargue.cargo;
        String cargo = AgregarMostrarListas.cargo;
        Log.d("CARGO EN LOAD DATA EDIT INFO COND","CARGO EN LOAD DATA EDIT INFO COND : "+cargo);

        int colorVerdeOscuro = ContextCompat.getColor(getContext(), R.color.verde_oscuro);
        int colorBlanco = ContextCompat.getColor(getContext(), R.color.white);

        edtNombreConductor.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getNombreConductor());
        edtCedula.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getCedula());
        edtLugarExpedicion.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getLugarExpedicion());

        //Se obtiene el valor actual de los radiobutton
        String licConduccionRes = AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getLicConduccionRes();
        String polizaRCERes = AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getPolizaRCERes();
        String EPSRes = AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getEpsRes();
        String ARLRes = AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getArlRes();
        String AFPRes = AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getAfpRes();

        //Se chequea el valor de selección que tienen los radiobutton
        if (respuestaSi.equals(licConduccionRes)) {
            rdgLicConduccion.check(R.id.rbSiLicConduccionEditar);

        }
        else if (respuestaNo.equals(licConduccionRes)) {
            rdgLicConduccion.check(R.id.rbNoLicConduccionEditar);

        }

        if (respuestaSi.equals(polizaRCERes)) {
            rdgPolizaRCE.check(R.id.rbSiPolizaEditar);

        }
        else if (respuestaNo.equals(polizaRCERes)) {
            rdgPolizaRCE.check(R.id.rbNoPolizaEditar);
        }

        if (respuestaSi.equals(EPSRes)) {
            rdgEPS.check(R.id.rbSiEPSEditar);

        }
        else if (respuestaNo.equals(EPSRes)) {
            rdgEPS.check(R.id.rbNoEPSEditar);
        }

        if (respuestaSi.equals(ARLRes)) {
            rdgARL.check(R.id.rbSiARLEditar);

        }
        else if (respuestaNo.equals(ARLRes)) {
            rdgARL.check(R.id.rbNoARLEditar);
        }


        if (respuestaSi.equals(AFPRes)) {
            rdgAFP.check(R.id.rbSiAFPEditar);

        }
        else if (respuestaNo.equals(AFPRes)) {
            rdgAFP.check(R.id.rbNoAFPEditar);
        }

        atcEps.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getCualEPS());
        atcAfp.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getCualAFP());
        atcArl.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getCualARL());

        RadioButton rbSiLicConduccionEditar = view.findViewById(R.id.rbSiLicConduccionEditar);
        RadioButton rbNoLicConduccionEditar = view.findViewById(R.id.rbNoLicConduccionEditar);
        RadioButton rbSipolizaRCERes = view.findViewById(R.id.rbSiPolizaEditar);
        RadioButton rbNopolizaRCERes = view.findViewById(R.id.rbNoPolizaEditar);
        RadioButton rbSiEps = view.findViewById(R.id.rbSiEPSEditar);
        RadioButton rbNoEps = view.findViewById(R.id.rbNoEPSEditar);
        RadioButton rbSiArl = view.findViewById(R.id.rbSiARLEditar);
        RadioButton rbNoArl = view.findViewById(R.id.rbNoARLEditar);
        RadioButton rbSiAfp = view.findViewById(R.id.rbSiAFPEditar);
        RadioButton rbNoAfp = view.findViewById(R.id.rbNoAFPEditar);

        Date fechaActualDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_actual_date");
        Date fechaListaDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_lista_date");
        Log.d("Fecha act","Fecha act : "+fechaActualDate);
        Log.d("Fecha list","Fecha list : "+fechaListaDate);

        if("Administrador".equals(cargo) || (fechaActualDate != null && fechaListaDate != null && !fechaActualDate.equals(fechaListaDate)) && !"Administrador".equals(cargo)){
            edtNombreConductor.setEnabled(false);
            edtNombreConductor.setTextColor(colorVerdeOscuro);

            edtCedula.setEnabled(false);
            edtCedula.setTextColor(colorVerdeOscuro);

            edtLugarExpedicion.setEnabled(false);
            edtLugarExpedicion.setTextColor(colorVerdeOscuro);

            rbSiLicConduccionEditar.setEnabled(false);
            rbNoLicConduccionEditar.setEnabled(false);
            rbSipolizaRCERes.setEnabled(false);
            rbNopolizaRCERes.setEnabled(false);
            rbSiEps.setEnabled(false);
            rbNoEps.setEnabled(false);
            rbSiArl.setEnabled(false);
            rbNoArl.setEnabled(false);
            rbSiAfp.setEnabled(false);
            rbNoAfp.setEnabled(false);

            atcEps.setEnabled(false);
            atcEps.setDropDownHeight(0);//Para deshabilitar el desplegable
            atcEps.setTextColor(colorVerdeOscuro);

            atcAfp.setEnabled(false);
            atcAfp.setDropDownHeight(0);//Para deshabilitar el desplegable
            atcAfp.setTextColor(colorVerdeOscuro);

            atcArl.setEnabled(false);
            atcArl.setDropDownHeight(0);//Para deshabilitar el desplegable
            atcArl.setTextColor(colorVerdeOscuro);

            btnActualizarDatosInfoConductor.setVisibility(View.GONE);
            imgCerrarDesplegableInfoConductor.setVisibility(View.GONE);

            framePadreInfoConductor.setBackgroundColor(colorVerdeOscuro);
            frameHijoInfoConductor.setBackgroundColor(colorBlanco);
        }
    }
    private void radioButtonId(){
        int rdgLicConduccionId = rdgLicConduccion.getCheckedRadioButtonId();
        int rdgPolizaRCEId = rdgPolizaRCE.getCheckedRadioButtonId();
        int rdgEpsId = rdgEPS.getCheckedRadioButtonId();
        int rdgArlId = rdgARL.getCheckedRadioButtonId();
        int rdgAfpId = rdgAFP.getCheckedRadioButtonId();

        // Verificar qué RadioButton fue seleccionado y actualizar el modelo
        if (rdgLicConduccionId == R.id.rbSiLicConduccionEditar) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setLicConduccionRes(respuestaSi);

        } else if (rdgLicConduccionId == R.id.rbNoLicConduccionEditar) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setLicConduccionRes(respuestaNo);
        }

        if (rdgPolizaRCEId == R.id.rbSiPolizaEditar) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setPolizaRCERes(respuestaSi);

        } else if (rdgPolizaRCEId == R.id.rbNoPolizaEditar) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setPolizaRCERes(respuestaNo);
        }

        if (rdgEpsId == R.id.rbSiEPSEditar) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setEpsRes(respuestaSi);

        } else if (rdgEpsId == R.id.rbNoEPSEditar) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setEpsRes(respuestaNo);

        }
        if (rdgArlId == R.id.rbSiARLEditar) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setArlRes(respuestaSi);

        } else if (rdgArlId == R.id.rbNoARLEditar) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setArlRes(respuestaNo);
        }

        if (rdgAfpId == R.id.rbSiAFPEditar) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setAfpRes(respuestaSi);

        } else if (rdgAfpId == R.id.rbNoAFPEditar) {
            listasCargueModel.getItem_2_Informacion_del_conductor().setAfpRes(respuestaNo);
        }
    }
    private void actualizarDatosInfoConductor(){

        radioButtonId();

        String respuestaLicConduccion = listasCargueModel.getItem_2_Informacion_del_conductor().getLicConduccionRes();
        String respuestaPoliza = listasCargueModel.getItem_2_Informacion_del_conductor().getPolizaRCERes();
        String respuestaEps = listasCargueModel.getItem_2_Informacion_del_conductor().getEpsRes();
        String respuestaArl = listasCargueModel.getItem_2_Informacion_del_conductor().getArlRes();
        String respuestaAfp = listasCargueModel.getItem_2_Informacion_del_conductor().getAfpRes();

        editarDatosInfoConductor.put("nombreConductor", Objects.requireNonNull(edtNombreConductor.getText()).toString());
        editarDatosInfoConductor.put("cedula", Objects.requireNonNull(edtCedula.getText()).toString());
        editarDatosInfoConductor.put("lugarExpedicion", Objects.requireNonNull(edtLugarExpedicion.getText()).toString());
        editarDatosInfoConductor.put("licConduccionRes",respuestaLicConduccion);
        editarDatosInfoConductor.put("polizaRCERes",respuestaPoliza);
        editarDatosInfoConductor.put("epsRes",respuestaEps);
        editarDatosInfoConductor.put("arlRes",respuestaArl);
        editarDatosInfoConductor.put("afpRes",respuestaAfp);
        editarDatosInfoConductor.put("cualEPS", Objects.requireNonNull(atcEps.getText()).toString());
        editarDatosInfoConductor.put("cualAFP", Objects.requireNonNull(atcAfp.getText()).toString());
        editarDatosInfoConductor.put("cualARL", Objects.requireNonNull(atcArl.getText()).toString());

        Log.d("IdListaPos","IdListaPos InfoConductor: " + idListaPos);
        if(isNetworkAvailable()){
            //if(id_lista != null){
            if(idListaPos != null){
                db.collection(pathLista)
                        .document(idListaPos)
                        .update("Item_2_Informacion_del_conductor", editarDatosInfoConductor).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        else{
            actualizarEstadoLista();
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
                listasCargueModel.getItem_2_Informacion_del_conductor().setNombreConductor(edtNombreConductor.getText().toString());
                listasCargueModel.getItem_2_Informacion_del_conductor().setCedula(edtCedula.getText().toString());
                listasCargueModel.getItem_2_Informacion_del_conductor().setLugarExpedicion(edtLugarExpedicion.getText().toString());
                listasCargueModel.getItem_2_Informacion_del_conductor().setLicConduccionRes(respuestaLicConduccion);
                listasCargueModel.getItem_2_Informacion_del_conductor().setPolizaRCERes(respuestaPoliza);
                listasCargueModel.getItem_2_Informacion_del_conductor().setEpsRes(respuestaEps);
                listasCargueModel.getItem_2_Informacion_del_conductor().setCualEPS(atcEps.getText().toString());
                listasCargueModel.getItem_2_Informacion_del_conductor().setArlRes(respuestaArl);
                listasCargueModel.getItem_2_Informacion_del_conductor().setCualARL(atcArl.getText().toString());
                listasCargueModel.getItem_2_Informacion_del_conductor().setAfpRes(respuestaAfp);
                listasCargueModel.getItem_2_Informacion_del_conductor().setCualAFP(atcAfp.getText().toString());

                dbLocal.updateList(listasCargueModel);
            }

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
                        listasCargueModel.getItem_2_Informacion_del_conductor().getNombreConductor();
                        String cargo = AgregarMostrarListas.cargo;
                        Log.d("CARGO EN LOAD DATA EDIT INFO COND", "CARGO EN LOAD DATA EDIT INFO COND : " + cargo);

                        int colorVerdeOscuro = ContextCompat.getColor(getContext(), R.color.verde_oscuro);
                        int colorBlanco = ContextCompat.getColor(getContext(), R.color.white);

                        edtNombreConductor.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getNombreConductor());
                        edtCedula.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCedula());
                        edtLugarExpedicion.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getLugarExpedicion());

                        //Se obtiene el valor actual de los radiobutton
                        String licConduccionRes = listasCargueModel.getItem_2_Informacion_del_conductor().getLicConduccionRes();
                        String polizaRCERes = listasCargueModel.getItem_2_Informacion_del_conductor().getPolizaRCERes();
                        String EPSRes = listasCargueModel.getItem_2_Informacion_del_conductor().getEpsRes();
                        String ARLRes = listasCargueModel.getItem_2_Informacion_del_conductor().getArlRes();
                        String AFPRes = listasCargueModel.getItem_2_Informacion_del_conductor().getAfpRes();

                        //Se chequea el valor de selección que tienen los radiobutton
                        if (respuestaSi.equals(licConduccionRes)) {
                            rdgLicConduccion.check(R.id.rbSiLicConduccionEditar);

                        } else if (respuestaNo.equals(licConduccionRes)) {
                            rdgLicConduccion.check(R.id.rbNoLicConduccionEditar);
                        }

                        if (respuestaSi.equals(polizaRCERes)) {
                            rdgPolizaRCE.check(R.id.rbSiPolizaEditar);

                        } else if (respuestaNo.equals(polizaRCERes)) {
                            rdgPolizaRCE.check(R.id.rbNoPolizaEditar);
                        }

                        if (respuestaSi.equals(EPSRes)) {
                            rdgEPS.check(R.id.rbSiEPSEditar);

                        } else if (respuestaNo.equals(EPSRes)) {
                            rdgEPS.check(R.id.rbNoEPSEditar);
                        }

                        if (respuestaSi.equals(ARLRes)) {
                            rdgARL.check(R.id.rbSiARLEditar);

                        } else if (respuestaNo.equals(ARLRes)) {
                            rdgARL.check(R.id.rbNoARLEditar);
                        }


                        if (respuestaSi.equals(AFPRes)) {
                            rdgAFP.check(R.id.rbSiAFPEditar);

                        } else if (respuestaNo.equals(AFPRes)) {
                            rdgAFP.check(R.id.rbNoAFPEditar);
                        }

                        atcEps.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCualEPS());
                        atcAfp.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCualAFP());
                        atcArl.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCualARL());

                        RadioButton rbSiLicConduccionEditar = view.findViewById(R.id.rbSiLicConduccionEditar);
                        RadioButton rbNoLicConduccionEditar = view.findViewById(R.id.rbNoLicConduccionEditar);
                        RadioButton rbSipolizaRCERes = view.findViewById(R.id.rbSiPolizaEditar);
                        RadioButton rbNopolizaRCERes = view.findViewById(R.id.rbNoPolizaEditar);
                        RadioButton rbSiEps = view.findViewById(R.id.rbSiEPSEditar);
                        RadioButton rbNoEps = view.findViewById(R.id.rbNoEPSEditar);
                        RadioButton rbSiArl = view.findViewById(R.id.rbSiARLEditar);
                        RadioButton rbNoArl = view.findViewById(R.id.rbNoARLEditar);
                        RadioButton rbSiAfp = view.findViewById(R.id.rbSiAFPEditar);
                        RadioButton rbNoAfp = view.findViewById(R.id.rbNoAFPEditar);

                        Date fechaActualDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_actual_date");
                        Date fechaListaDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_lista_date");
                        Log.d("Fecha act", "Fecha act : " + fechaActualDate);
                        Log.d("Fecha list", "Fecha list : " + fechaListaDate);

                        if ("Administrador".equals(cargo) || (fechaActualDate != null && fechaListaDate != null && !fechaActualDate.equals(fechaListaDate)) && !"Administrador".equals(cargo)) {
                            edtNombreConductor.setEnabled(false);
                            edtNombreConductor.setTextColor(colorVerdeOscuro);

                            edtCedula.setEnabled(false);
                            edtCedula.setTextColor(colorVerdeOscuro);

                            edtLugarExpedicion.setEnabled(false);
                            edtLugarExpedicion.setTextColor(colorVerdeOscuro);

                            rbSiLicConduccionEditar.setEnabled(false);
                            rbNoLicConduccionEditar.setEnabled(false);
                            rbSipolizaRCERes.setEnabled(false);
                            rbNopolizaRCERes.setEnabled(false);
                            rbSiEps.setEnabled(false);
                            rbNoEps.setEnabled(false);
                            rbSiArl.setEnabled(false);
                            rbNoArl.setEnabled(false);
                            rbSiAfp.setEnabled(false);
                            rbNoAfp.setEnabled(false);

                            atcEps.setEnabled(false);
                            atcEps.setDropDownHeight(0);//Para deshabilitar el desplegable
                            atcEps.setTextColor(colorVerdeOscuro);

                            atcAfp.setEnabled(false);
                            atcAfp.setDropDownHeight(0);//Para deshabilitar el desplegable
                            atcAfp.setTextColor(colorVerdeOscuro);

                            atcArl.setEnabled(false);
                            atcArl.setDropDownHeight(0);//Para deshabilitar el desplegable
                            atcArl.setTextColor(colorVerdeOscuro);

                            btnActualizarDatosInfoConductor.setVisibility(View.GONE);
                            imgCerrarDesplegableInfoConductor.setVisibility(View.GONE);

                            framePadreInfoConductor.setBackgroundColor(colorVerdeOscuro);
                            frameHijoInfoConductor.setBackgroundColor(colorBlanco);
                        }
                    }
                }
            }
        });
    }

    private void obtenerDesplegablesInfoConductor() {
        if (!isNetworkAvailable()) {
            Toast.makeText(getContext(), "Recopilando datos.", Toast.LENGTH_SHORT).show();
        }

        db.collection("desplegables info conductor")
                .get(Source.DEFAULT)// Primero intenta obtener datos del caché
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> AFPList = new ArrayList<>();
                        List<String> ARLList = new ArrayList<>();
                        List<String> EPSList = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            // Obtiene los datos de firestore
                            List<String> AFP = (List<String>) documentSnapshot.get("AFP");
                            List<String> ARL = (List<String>) documentSnapshot.get("ARL");
                            List<String> EPS = (List<String>) documentSnapshot.get("EPS");

                            // Se pasan los datos obtenidos a las ArrayList declaradas previamente
                            if (AFP != null) {
                                AFPList.addAll(AFP);
                            }

                            if (ARL != null) {
                                ARLList.addAll(ARL);
                            }

                            if (EPS != null) {
                                EPSList.addAll(EPS);
                            }
                        }

                        Context context = getContext();
                        if (context != null) {
                            ArrayAdapter<String> adapterAFP = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, AFPList);
                            atcAfp.setAdapter(adapterAFP);

                            ArrayAdapter<String> adapterARL = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, ARLList);
                            atcArl.setAdapter(adapterARL);

                            ArrayAdapter<String> adapterEPS = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, EPSList);
                            atcEps.setAdapter(adapterEPS);
                        }
                    } else {
                        // Manejo del error cuando no se puede acceder a los datos
                        Toast.makeText(getContext(), "Error al obtener datos. Verifique su conexión a internet.", Toast.LENGTH_SHORT).show();
                    }
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
    public interface OnEstadoEditarInfoConductor {
        void onEstadoEditarInfoConductor(boolean camposCompletos);
    }
    private OnEstadoEditarInfoConductor listener2;
    private void actualizarEstadoLista() {
        boolean camposCompletos = verificarCampos();
        if (listener2 != null) {
            listener2.onEstadoEditarInfoConductor(camposCompletos);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnEstadoEditarInfoConductor) {
            listener2 = (OnEstadoEditarInfoConductor) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEstadoEditarCamposInfoConductorListener");
        }
    }
}