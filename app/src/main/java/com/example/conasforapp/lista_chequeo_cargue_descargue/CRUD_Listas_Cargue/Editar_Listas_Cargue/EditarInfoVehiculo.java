package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Editar_Listas_Cargue;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditarInfoVehiculo extends Fragment {
    private View view;
    private AutoCompleteTextView edtVehiculo;
    private TextInputEditText edtPlaca;
    private FirebaseFirestore db;
    private RadioGroup rdgTarjetaPropiedad, rdgSoat, rdgRevisionTecnicomecanica,
            rdgLucesAltas, rdgLucesBajas, rdgDireccionales, rdgSonidoReversa, rdgReversa,
            rdgStop, rdgRetrovisores, rdgPlumillas, rdgEstadoPanoramicos, rdgEspejos,
            rdgBocina, rdgCinturon, rdgFreno, rdgLlantas, rdgBotiquin, rdgExtintorABC, rdgBotas,
            rdgChaleco, rdgCasco, rdgCarroceria, rdgDosEslingasPorBanco, rdgDosConosReflectivos,
            rdgParales;

    private TextInputEditText edtObservacionesInfoVehiculo;
    private Button btnActualizarDatosInfoVehiculo;
    private ImageView imgCerrarDesplegableInfoVehiculo;
    Map<String, String> datosInfoVehiculo= new HashMap<>();
    private String pathLista = "Listas chequeo cargue descargue";
    private ListasCargueModel.InfoDelVehiculo  infoVehiculoModel = new ListasCargueModel.InfoDelVehiculo();
    FrameLayout framePadreInfoVehiculo,frameHijoInfoVehiculo;
    private int listId = -1;
    private ListasCargueDataBaseHelper dbLocal;
    private ListasCargueModel listasCargueModel = new ListasCargueModel();
    String idListaPos = null;

    public EditarInfoVehiculo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_editar_vehiculo, container, false);

        edtPlaca = view.findViewById(R.id.edtPlacaEditar);
        edtVehiculo = view.findViewById(R.id.edtVehiculoEditar);
        edtObservacionesInfoVehiculo = view.findViewById(R.id.edtObservacionesInfoVehiculoEditar);
        //imgCerrarDesplegableInfoVehiculo = view.findViewById(R.id.imgCerrarInfoVehiculoEditar);
        framePadreInfoVehiculo = view.findViewById(R.id.frameInfoVehiculoEditar);
        frameHijoInfoVehiculo = view.findViewById(R.id.frameInfoVehiculoHijoEdit);

        db = FirebaseFirestore.getInstance();

        rdgTarjetaPropiedad = view.findViewById(R.id.rdgTarjetaPropiedadEditar);
        rdgSoat = view.findViewById(R.id.rdgSOATVigenteEditar);
        rdgRevisionTecnicomecanica = view.findViewById(R.id.rdgRevisionTecnicomecanicaEditar);
        rdgLucesAltas = view.findViewById(R.id.rdgLucesAltasEditar);
        rdgLucesBajas = view.findViewById(R.id.rdgLucesBajasEditar);
        rdgDireccionales = view.findViewById(R.id.rdgDireccionalesEditar);
        rdgSonidoReversa = view.findViewById(R.id.rdgSonidoReversaEditar);
        rdgReversa = view.findViewById(R.id.rdgReversaEditar);
        rdgStop = view.findViewById(R.id.rdgStopEditar);
        rdgRetrovisores = view.findViewById(R.id.rdgRetrovisoresEditar);
        rdgPlumillas = view.findViewById(R.id.rdgPlumillasEditar);
        rdgEstadoPanoramicos = view.findViewById(R.id.rdgEstadoPanoramicosEditar);
        rdgEspejos = view.findViewById(R.id.rdgEspejosEditar);
        rdgBocina = view.findViewById(R.id.rdgBocinaEditar);
        rdgCinturon = view.findViewById(R.id.rdgCinturonEditar);
        rdgFreno = view.findViewById(R.id.rdgFrenoEditar);
        rdgLlantas = view.findViewById(R.id.rdgLlantasEditar);
        rdgBotiquin = view.findViewById(R.id.rdgBotiquinEditar);
        rdgExtintorABC = view.findViewById(R.id.rdgExtintorABCEditar);
        rdgBotas = view.findViewById(R.id.rdgBotasEditar);
        rdgChaleco = view.findViewById(R.id.rdgChalecoEditar);
        rdgCasco = view.findViewById(R.id.rdgCascoEditar);
        rdgCarroceria = view.findViewById(R.id.rdgCarroceriaEditar);
        rdgDosEslingasPorBanco = view.findViewById(R.id.rdgDosEslingasBancoEditar);
        rdgDosConosReflectivos = view.findViewById(R.id.rdgDosConosReflectivosEditar);
        rdgParales = view.findViewById(R.id.rdgParalesIgualLongitudEditar);
        edtObservacionesInfoVehiculo = view.findViewById(R.id.edtObservacionesInfoVehiculoEditar);

        btnActualizarDatosInfoVehiculo = view.findViewById(R.id.btnActualizarDatosInfoVehiculo);
        btnActualizarDatosInfoVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatosInfoVehiculo();
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
            obtenerLista();

            // Obtener los argumentos del Bundle
            Bundle args = getArguments();
            if (args != null) {
                idListaPos = args.getString("idPos");
                Log.d("ID LISTA POS EN INFO VEHICULO","ID LISTA POS EN INFO VEHICULO : "+idListaPos);
            }
        }

        obtenerDatosInfoVehiculo();

        return view;
    }

    public static EditarInfoVehiculo newInstance(String idLista) {
        Bundle args = new Bundle();
        args.putString("idPos", idLista);
        EditarInfoVehiculo fragment = new EditarInfoVehiculo();
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

        edtPlaca.setText(listasCargueModel.getItem_3_Informacion_vehiculo().getPlaca());
        edtVehiculo.setText(listasCargueModel.getItem_3_Informacion_vehiculo().getVehiculo());
        String tarjetaPropiedadRes = listasCargueModel.getItem_3_Informacion_vehiculo().getTarjetaPropiedad();
        String soatRes = listasCargueModel.getItem_3_Informacion_vehiculo().getSoatVigente();
        String revisionTecnicomecanicaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getRevisionTecnicomecanica();
        String lucesAltasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getLucesAltas();
        String lucesBajasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getLucesBajas();
        String direccionalesRes = listasCargueModel.getItem_3_Informacion_vehiculo().getDireccionales();
        String sonidoReversaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getSonidoReversa();
        String reversaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getReversa();
        String stopRes = listasCargueModel.getItem_3_Informacion_vehiculo().getStop();
        String retrovisoresRes = listasCargueModel.getItem_3_Informacion_vehiculo().getRetrovisores();
        String plumillasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getPlumillas();
        String estadoPanoramicosRes = listasCargueModel.getItem_3_Informacion_vehiculo().getEstadoPanoramicos();
        String espejosRes = listasCargueModel.getItem_3_Informacion_vehiculo().getEspejos();
        String bocinaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getBocina();
        String cinturonRes = listasCargueModel.getItem_3_Informacion_vehiculo().getCinturon();
        String frenoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getFreno();
        String llantasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getLlantas();
        String botiquinRes = listasCargueModel.getItem_3_Informacion_vehiculo().getBotiquin();
        String extintorABCRes = listasCargueModel.getItem_3_Informacion_vehiculo().getExtintorABC();
        String botasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getBotas();
        String chalecoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getChaleco();
        String cascoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getCasco();
        String carroceriaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getCarroceria();
        String dosEslingasPorBancoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getDosEslingasBanco();
        String dosConosReflectivosRes = listasCargueModel.getItem_3_Informacion_vehiculo().getDosConosReflectivos();
        String paralesRes = listasCargueModel.getItem_3_Informacion_vehiculo().getParales();
        edtObservacionesInfoVehiculo.setText(listasCargueModel.getItem_3_Informacion_vehiculo().getObservacionesCamion());

        //Se chequea el valor de selección que tienen los radiobutton
        if ("Si".equals(tarjetaPropiedadRes)) {
            rdgTarjetaPropiedad.check(R.id.rbSiTarjetaPropiedadEditar);

        }
        else if ("No".equals(tarjetaPropiedadRes)) {
            rdgTarjetaPropiedad.check(R.id.rbNoTarjetaPropiedadEditar);
        }

        if ("Si".equals(soatRes)) {
            rdgSoat.check(R.id.rbSiSOATVigenteEditar);

        }
        else if ("No".equals(soatRes)) {
            rdgSoat.check(R.id.rbNoSOATVigenteEditar);
        }

        if ("Si".equals(revisionTecnicomecanicaRes)) {
            rdgRevisionTecnicomecanica.check(R.id.rbSiRevisionTecnicomecanicaEditar);

        }
        else if ("No".equals(revisionTecnicomecanicaRes)) {
            rdgRevisionTecnicomecanica.check(R.id.rbNoRevisionTecnicomecanicaEditar);
        }


        if ("Bien".equals(lucesAltasRes)) {
            rdgLucesAltas.check(R.id.rbBienLucesAltasEditar);

        }
        else if ("Mal".equals(lucesAltasRes)) {
            rdgLucesAltas.check(R.id.rbMalLucesAltasEditar);
        }


        if ("Bien".equals(lucesBajasRes)) {
            rdgLucesBajas.check(R.id.rbBienLucesBajasEditar);

        }
        else if ("Mal".equals(lucesBajasRes)) {
            rdgLucesBajas.check(R.id.rbMalLucesBajasEditar);
        }

        if ("Bien".equals(direccionalesRes)) {
            rdgDireccionales.check(R.id.rbBienDireccionalesEditar);

        }
        else if ("Mal".equals(direccionalesRes)) {
            rdgDireccionales.check(R.id.rbMalDireccionalesEditar);
        }


        if ("Bien".equals(sonidoReversaRes)) {
            rdgSonidoReversa.check(R.id.rbBienSonidoReversaEditar);

        }
        else if ("Mal".equals(sonidoReversaRes)) {
            rdgSonidoReversa.check(R.id.rbMalSonidoReversaEditar);
        }


        if ("Bien".equals(reversaRes)) {
            rdgReversa.check(R.id.rbBienReversaEditar);

        }
        else if ("Mal".equals(reversaRes)) {
            rdgReversa.check(R.id.rbMalReversaEditar);
        }

        if ("Bien".equals(stopRes)) {
            rdgStop.check(R.id.rbBienStopEditar);

        }
        else if ("Mal".equals(stopRes)) {
            rdgStop.check(R.id.rbMalStopEditar);
        }

        if ("Bien".equals(retrovisoresRes)) {
            rdgRetrovisores.check(R.id.rbBienRetrovisoresEditar);

        }
        else if ("Mal".equals(retrovisoresRes)) {
            rdgRetrovisores.check(R.id.rbMalRetrovisoresEditar);
        }


        if ("Bien".equals(plumillasRes)) {
            rdgPlumillas.check(R.id.rbBienPlumillasEditar);

        }
        else if ("Mal".equals(plumillasRes)) {
            rdgPlumillas.check(R.id.rbMalPlumillasEditar);
        }

        if ("Bien".equals(estadoPanoramicosRes)) {
            rdgEstadoPanoramicos.check(R.id.rbBienEstadoPanoramicosEditar);

        }
        else if ("Mal".equals(estadoPanoramicosRes)) {
            rdgEstadoPanoramicos.check(R.id.rbMalEstadoPanoramicosEditar);
        }

        if ("Bien".equals(espejosRes)) {
            rdgEspejos.check(R.id.rbBienEspejosEditar);

        }
        else if ("Mal".equals(espejosRes)) {
            rdgEspejos.check(R.id.rbMalEspejosEditar);
        }

        if ("Bien".equals(bocinaRes)) {
            rdgBocina.check(R.id.rbBienBocinaEditar);

        }
        else if ("Mal".equals(bocinaRes)) {
            rdgBocina.check(R.id.rbMalBocinaEditar);
        }

        if ("Bien".equals(cinturonRes)) {
            rdgCinturon.check(R.id.rbBienCinturonEditar);

        }
        else if ("Mal".equals(cinturonRes)) {
            rdgCinturon.check(R.id.rbMalCinturonEditar);
        }

        if ("Bien".equals(frenoRes)) {
            rdgFreno.check(R.id.rbBienFrenoEditar);

        }
        else if ("Mal".equals(frenoRes)) {
            rdgFreno.check(R.id.rbMalFrenoEditar);
        }

        if ("Bien".equals(llantasRes)) {
            rdgLlantas.check(R.id.rbBienLlantasEditar);

        }
        else if ("Mal".equals(llantasRes)) {
            rdgLlantas.check(R.id.rbMalLlantasEditar);
        }

        if ("Bien".equals(botiquinRes)) {
            rdgBotiquin.check(R.id.rbBienBotiquinEditar);

        }
        else if ("Mal".equals(botiquinRes)) {
            rdgBotiquin.check(R.id.rbMalBotiquinEditar);
        }

        if ("Bien".equals(extintorABCRes)) {
            rdgExtintorABC.check(R.id.rbBienExtintorABCEditar);

        }
        else if ("Mal".equals(extintorABCRes)) {
            rdgExtintorABC.check(R.id.rbMalExtintorABCEditar);
        }

        if ("Bien".equals(botasRes)) {
            rdgBotas.check(R.id.rbBienBotasEditar);

        }
        else if ("Mal".equals(botasRes)) {
            rdgBotas.check(R.id.rbMalBotasEditar);
        }

        if ("Bien".equals(chalecoRes)) {
            rdgChaleco.check(R.id.rbBienChalecoEditar);

        }
        else if ("Mal".equals(chalecoRes)) {
            rdgChaleco.check(R.id.rbMalChalecoEditar);
        }

        if ("Bien".equals(cascoRes)) {
            rdgCasco.check(R.id.rbBienCascoEditar);

        }
        else if ("Mal".equals(cascoRes)) {
            rdgCasco.check(R.id.rbMalCascoEditar);
        }

        if ("Bien".equals(carroceriaRes)) {
            rdgCarroceria.check(R.id.rbBienCarroceriaEditar);

        }
        else if ("Mal".equals(carroceriaRes)) {
            rdgCarroceria.check(R.id.rbMalCarroceriaEditar);
        }

        if ("Bien".equals(dosEslingasPorBancoRes)) {
            rdgDosEslingasPorBanco.check(R.id.rbBienDosEslingasBancoEditar);
        }
        else if ("Mal".equals(dosEslingasPorBancoRes)) {
            rdgDosEslingasPorBanco.check(R.id.rbMalDosEslingasBancoEditar);
        }

        if ("Bien".equals(dosConosReflectivosRes)) {
            rdgDosConosReflectivos.check(R.id.rbBienDosConosReflectivosEditar);
        }
        else if ("Mal".equals(dosConosReflectivosRes)) {
            rdgDosConosReflectivos.check(R.id.rbMalDosEslingasBancoEditar);
        }

        if ("Bien".equals(paralesRes)) {
            rdgParales.check(R.id.rbBienParalesIgualLongitudEditar);
        }
        else if ("Mal".equals(paralesRes)) {
            rdgParales.check(R.id.rbMalParalesIgualLongitudEditar);
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

                        edtPlaca.setText(listasCargueModel.getItem_3_Informacion_vehiculo().getPlaca());
                        edtVehiculo.setText(listasCargueModel.getItem_3_Informacion_vehiculo().getVehiculo());

                        String tarjetaPropiedadRes = listasCargueModel.getItem_3_Informacion_vehiculo().getTarjetaPropiedad();
                        String soatRes = listasCargueModel.getItem_3_Informacion_vehiculo().getSoatVigente();
                        String revisionTecnicomecanicaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getRevisionTecnicomecanica();
                        String lucesAltasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getLucesAltas();
                        String lucesBajasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getLucesBajas();
                        String direccionalesRes = listasCargueModel.getItem_3_Informacion_vehiculo().getDireccionales();
                        String sonidoReversaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getSonidoReversa();
                        String reversaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getReversa();
                        String stopRes = listasCargueModel.getItem_3_Informacion_vehiculo().getStop();
                        String retrovisoresRes = listasCargueModel.getItem_3_Informacion_vehiculo().getRetrovisores();
                        String plumillasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getPlumillas();
                        String estadoPanoramicosRes = listasCargueModel.getItem_3_Informacion_vehiculo().getEstadoPanoramicos();
                        String espejosRes = listasCargueModel.getItem_3_Informacion_vehiculo().getEspejos();
                        String bocinaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getBocina();
                        String cinturonRes = listasCargueModel.getItem_3_Informacion_vehiculo().getCinturon();
                        String frenoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getFreno();
                        String llantasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getLlantas();
                        String botiquinRes = listasCargueModel.getItem_3_Informacion_vehiculo().getBotiquin();
                        String extintorABCRes = listasCargueModel.getItem_3_Informacion_vehiculo().getExtintorABC();
                        String botasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getBotas();
                        String chalecoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getChaleco();
                        String cascoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getCasco();
                        String carroceriaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getCarroceria();
                        String dosEslingasPorBancoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getDosEslingasBanco();
                        String dosConosReflectivosRes = listasCargueModel.getItem_3_Informacion_vehiculo().getDosConosReflectivos();
                        String paralesRes = listasCargueModel.getItem_3_Informacion_vehiculo().getParales();
                        edtObservacionesInfoVehiculo.setText(listasCargueModel.getItem_3_Informacion_vehiculo().getObservacionesCamion());

                        //Se chequea el valor de selección que tienen los radiobutton
                        if ("Si".equals(tarjetaPropiedadRes)) {
                            rdgTarjetaPropiedad.check(R.id.rbSiTarjetaPropiedadEditar);

                        }
                        else if ("No".equals(tarjetaPropiedadRes)) {
                            rdgTarjetaPropiedad.check(R.id.rbNoTarjetaPropiedadEditar);
                        }

                        if ("Si".equals(soatRes)) {
                            rdgSoat.check(R.id.rbSiSOATVigenteEditar);

                        }
                        else if ("No".equals(soatRes)) {
                            rdgSoat.check(R.id.rbNoSOATVigenteEditar);
                        }

                        if ("Si".equals(revisionTecnicomecanicaRes)) {
                            rdgRevisionTecnicomecanica.check(R.id.rbSiRevisionTecnicomecanicaEditar);

                        }
                        else if ("No".equals(revisionTecnicomecanicaRes)) {
                            rdgRevisionTecnicomecanica.check(R.id.rbNoRevisionTecnicomecanicaEditar);
                        }


                        if ("Bien".equals(lucesAltasRes)) {
                            rdgLucesAltas.check(R.id.rbBienLucesAltasEditar);

                        }
                        else if ("Mal".equals(lucesAltasRes)) {
                            rdgLucesAltas.check(R.id.rbMalLucesAltasEditar);
                        }


                        if ("Bien".equals(lucesBajasRes)) {
                            rdgLucesBajas.check(R.id.rbBienLucesBajasEditar);

                        }
                        else if ("Mal".equals(lucesBajasRes)) {
                            rdgLucesBajas.check(R.id.rbMalLucesBajasEditar);
                        }

                        if ("Bien".equals(direccionalesRes)) {
                            rdgDireccionales.check(R.id.rbBienDireccionalesEditar);

                        }
                        else if ("Mal".equals(direccionalesRes)) {
                            rdgDireccionales.check(R.id.rbMalDireccionalesEditar);
                        }


                        if ("Bien".equals(sonidoReversaRes)) {
                            rdgSonidoReversa.check(R.id.rbBienSonidoReversaEditar);

                        }
                        else if ("Mal".equals(sonidoReversaRes)) {
                            rdgSonidoReversa.check(R.id.rbMalSonidoReversaEditar);
                        }


                        if ("Bien".equals(reversaRes)) {
                            rdgReversa.check(R.id.rbBienReversaEditar);

                        }
                        else if ("Mal".equals(reversaRes)) {
                            rdgReversa.check(R.id.rbMalReversaEditar);
                        }

                        if ("Bien".equals(stopRes)) {
                            rdgStop.check(R.id.rbBienStopEditar);

                        }
                        else if ("Mal".equals(stopRes)) {
                            rdgStop.check(R.id.rbMalStopEditar);
                        }

                        if ("Bien".equals(retrovisoresRes)) {
                            rdgRetrovisores.check(R.id.rbBienRetrovisoresEditar);

                        }
                        else if ("Mal".equals(retrovisoresRes)) {
                            rdgRetrovisores.check(R.id.rbMalRetrovisoresEditar);
                        }


                        if ("Bien".equals(plumillasRes)) {
                            rdgPlumillas.check(R.id.rbBienPlumillasEditar);

                        }
                        else if ("Mal".equals(plumillasRes)) {
                            rdgPlumillas.check(R.id.rbMalPlumillasEditar);
                        }

                        if ("Bien".equals(estadoPanoramicosRes)) {
                            rdgEstadoPanoramicos.check(R.id.rbBienEstadoPanoramicosEditar);

                        }
                        else if ("Mal".equals(estadoPanoramicosRes)) {
                            rdgEstadoPanoramicos.check(R.id.rbMalEstadoPanoramicosEditar);
                        }

                        if ("Bien".equals(espejosRes)) {
                            rdgEspejos.check(R.id.rbBienEspejosEditar);

                        }
                        else if ("Mal".equals(espejosRes)) {
                            rdgEspejos.check(R.id.rbMalEspejosEditar);
                        }

                        if ("Bien".equals(bocinaRes)) {
                            rdgBocina.check(R.id.rbBienBocinaEditar);

                        }
                        else if ("Mal".equals(bocinaRes)) {
                            rdgBocina.check(R.id.rbMalBocinaEditar);
                        }

                        if ("Bien".equals(cinturonRes)) {
                            rdgCinturon.check(R.id.rbBienCinturonEditar);

                        }
                        else if ("Mal".equals(cinturonRes)) {
                            rdgCinturon.check(R.id.rbMalCinturonEditar);
                        }

                        if ("Bien".equals(frenoRes)) {
                            rdgFreno.check(R.id.rbBienFrenoEditar);

                        }
                        else if ("Mal".equals(frenoRes)) {
                            rdgFreno.check(R.id.rbMalFrenoEditar);
                        }

                        if ("Bien".equals(llantasRes)) {
                            rdgLlantas.check(R.id.rbBienLlantasEditar);

                        }
                        else if ("Mal".equals(llantasRes)) {
                            rdgLlantas.check(R.id.rbMalLlantasEditar);
                        }

                        if ("Bien".equals(botiquinRes)) {
                            rdgBotiquin.check(R.id.rbBienBotiquinEditar);

                        }
                        else if ("Mal".equals(botiquinRes)) {
                            rdgBotiquin.check(R.id.rbMalBotiquinEditar);
                        }

                        if ("Bien".equals(extintorABCRes)) {
                            rdgExtintorABC.check(R.id.rbBienExtintorABCEditar);

                        }
                        else if ("Mal".equals(extintorABCRes)) {
                            rdgExtintorABC.check(R.id.rbMalExtintorABCEditar);
                        }

                        if ("Bien".equals(botasRes)) {
                            rdgBotas.check(R.id.rbBienBotasEditar);

                        }
                        else if ("Mal".equals(botasRes)) {
                            rdgBotas.check(R.id.rbMalBotasEditar);
                        }

                        if ("Bien".equals(chalecoRes)) {
                            rdgChaleco.check(R.id.rbBienChalecoEditar);

                        }
                        else if ("Mal".equals(chalecoRes)) {
                            rdgChaleco.check(R.id.rbMalChalecoEditar);
                        }

                        if ("Bien".equals(cascoRes)) {
                            rdgCasco.check(R.id.rbBienCascoEditar);

                        }
                        else if ("Mal".equals(cascoRes)) {
                            rdgCasco.check(R.id.rbMalCascoEditar);
                        }

                        if ("Bien".equals(carroceriaRes)) {
                            rdgCarroceria.check(R.id.rbBienCarroceriaEditar);

                        }
                        else if ("Mal".equals(carroceriaRes)) {
                            rdgCarroceria.check(R.id.rbMalCarroceriaEditar);
                        }

                        if ("Bien".equals(dosEslingasPorBancoRes)) {
                            rdgDosEslingasPorBanco.check(R.id.rbBienDosEslingasBancoEditar);
                        }
                        else if ("Mal".equals(dosEslingasPorBancoRes)) {
                            rdgDosEslingasPorBanco.check(R.id.rbMalDosEslingasBancoEditar);
                        }

                        if ("Bien".equals(dosConosReflectivosRes)) {
                            rdgDosConosReflectivos.check(R.id.rbBienDosConosReflectivosEditar);
                        }
                        else if ("Mal".equals(dosConosReflectivosRes)) {
                            rdgDosConosReflectivos.check(R.id.rbMalDosEslingasBancoEditar);
                        }

                        if ("Bien".equals(paralesRes)) {
                            rdgParales.check(R.id.rbBienParalesIgualLongitudEditar);
                        }
                        else if ("Mal".equals(paralesRes)) {
                            rdgParales.check(R.id.rbMalParalesIgualLongitudEditar);
                        }
                    }
                }
            }
        });
    }

    private void loadDataFirebase() {

        edtPlaca.setText(AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getPlaca());
        edtVehiculo.setText(AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getVehiculo());
        edtObservacionesInfoVehiculo.setText(AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getObservacionesCamion());

        String tarjetaPropiedadRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getTarjetaPropiedad();
        String soatRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getSoatVigente();
        String revisionTecnicomecanicaRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getRevisionTecnicomecanica();
        String lucesAltasRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getLucesAltas();
        String lucesBajasRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getLucesBajas();
        String direccionalesRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getDireccionales();
        String sonidoReversaRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getSonidoReversa();
        String reversaRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getReversa();
        String stopRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getStop();
        String retrovisoresRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getRetrovisores();
        String plumillasRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getPlumillas();
        String estadoPanoramicosRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getEstadoPanoramicos();
        String espejosRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getEspejos();
        String bocinaRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getBocina();
        String cinturonRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getCinturon();
        String frenoRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getFreno();
        String llantasRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getLlantas();
        String botiquinRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getBotiquin();
        String extintorABCRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getExtintorABC();
        String botasRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getBotas();
        String chalecoRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getChaleco();
        String cascoRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getCasco();
        String carroceriaRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getCarroceria();
        String dosEslingasPorBancoRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getDosEslingasBanco();
        String dosConosReflectivosRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getDosConosReflectivos();
        String paralesRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getParales();

        //Se chequea el valor de selección que tienen los radiobutton
        if ("Si".equals(tarjetaPropiedadRes)) {
            rdgTarjetaPropiedad.check(R.id.rbSiTarjetaPropiedadEditar);

        }
        else if ("No".equals(tarjetaPropiedadRes)) {
            rdgTarjetaPropiedad.check(R.id.rbNoTarjetaPropiedadEditar);
        }

        if ("Si".equals(soatRes)) {
            rdgSoat.check(R.id.rbSiSOATVigenteEditar);

        }
        else if ("No".equals(soatRes)) {
            rdgSoat.check(R.id.rbNoSOATVigenteEditar);
        }

        if ("Si".equals(revisionTecnicomecanicaRes)) {
            rdgRevisionTecnicomecanica.check(R.id.rbSiRevisionTecnicomecanicaEditar);

        }
        else if ("No".equals(revisionTecnicomecanicaRes)) {
            rdgRevisionTecnicomecanica.check(R.id.rbNoRevisionTecnicomecanicaEditar);
        }


        if ("Bien".equals(lucesAltasRes)) {
            rdgLucesAltas.check(R.id.rbBienLucesAltasEditar);

        }
        else if ("Mal".equals(lucesAltasRes)) {
            rdgLucesAltas.check(R.id.rbMalLucesAltasEditar);
        }


        if ("Bien".equals(lucesBajasRes)) {
            rdgLucesBajas.check(R.id.rbBienLucesBajasEditar);

        }
        else if ("Mal".equals(lucesBajasRes)) {
            rdgLucesBajas.check(R.id.rbMalLucesBajasEditar);
        }

        if ("Bien".equals(direccionalesRes)) {
            rdgDireccionales.check(R.id.rbBienDireccionalesEditar);

        }
        else if ("Mal".equals(direccionalesRes)) {
            rdgDireccionales.check(R.id.rbMalDireccionalesEditar);
        }


        if ("Bien".equals(sonidoReversaRes)) {
            rdgSonidoReversa.check(R.id.rbBienSonidoReversaEditar);

        }
        else if ("Mal".equals(sonidoReversaRes)) {
            rdgSonidoReversa.check(R.id.rbMalSonidoReversaEditar);
        }


        if ("Bien".equals(reversaRes)) {
            rdgReversa.check(R.id.rbBienReversaEditar);

        }
        else if ("Mal".equals(reversaRes)) {
            rdgReversa.check(R.id.rbMalReversaEditar);
        }

        if ("Bien".equals(stopRes)) {
            rdgStop.check(R.id.rbBienStopEditar);

        }
        else if ("Mal".equals(stopRes)) {
            rdgStop.check(R.id.rbMalStopEditar);
        }


        if ("Bien".equals(retrovisoresRes)) {
            rdgRetrovisores.check(R.id.rbBienRetrovisoresEditar);

        }
        else if ("Mal".equals(retrovisoresRes)) {
            rdgRetrovisores.check(R.id.rbMalRetrovisoresEditar);
        }


        if ("Bien".equals(plumillasRes)) {
            rdgPlumillas.check(R.id.rbBienPlumillasEditar);

        }
        else if ("Mal".equals(plumillasRes)) {
            rdgPlumillas.check(R.id.rbMalPlumillasEditar);
        }

        if ("Bien".equals(estadoPanoramicosRes)) {
            rdgEstadoPanoramicos.check(R.id.rbBienEstadoPanoramicosEditar);

        }
        else if ("Mal".equals(estadoPanoramicosRes)) {
            rdgEstadoPanoramicos.check(R.id.rbMalEstadoPanoramicosEditar);
        }

        if ("Bien".equals(espejosRes)) {
            rdgEspejos.check(R.id.rbBienEspejosEditar);

        }
        else if ("Mal".equals(espejosRes)) {
            rdgEspejos.check(R.id.rbMalEspejosEditar);
        }

        if ("Bien".equals(bocinaRes)) {
            rdgBocina.check(R.id.rbBienBocinaEditar);

        }
        else if ("Mal".equals(bocinaRes)) {
            rdgBocina.check(R.id.rbMalBocinaEditar);
        }

        if ("Bien".equals(cinturonRes)) {
            rdgCinturon.check(R.id.rbBienCinturonEditar);

        }
        else if ("Mal".equals(cinturonRes)) {
            rdgCinturon.check(R.id.rbMalCinturonEditar);
        }

        if ("Bien".equals(frenoRes)) {
            rdgFreno.check(R.id.rbBienFrenoEditar);

        }
        else if ("Mal".equals(frenoRes)) {
            rdgFreno.check(R.id.rbMalFrenoEditar);
        }

        if ("Bien".equals(llantasRes)) {
            rdgLlantas.check(R.id.rbBienLlantasEditar);

        }
        else if ("Mal".equals(llantasRes)) {
            rdgLlantas.check(R.id.rbMalLlantasEditar);
        }

        if ("Bien".equals(botiquinRes)) {
            rdgBotiquin.check(R.id.rbBienBotiquinEditar);

        }
        else if ("Mal".equals(botiquinRes)) {
            rdgBotiquin.check(R.id.rbMalBotiquinEditar);
        }

        if ("Bien".equals(extintorABCRes)) {
            rdgExtintorABC.check(R.id.rbBienExtintorABCEditar);

        }
        else if ("Mal".equals(extintorABCRes)) {
            rdgExtintorABC.check(R.id.rbMalExtintorABCEditar);
        }

        if ("Bien".equals(botasRes)) {
            rdgBotas.check(R.id.rbBienBotasEditar);

        }
        else if ("Mal".equals(botasRes)) {
            rdgBotas.check(R.id.rbMalBotasEditar);
        }

        if ("Bien".equals(chalecoRes)) {
            rdgChaleco.check(R.id.rbBienChalecoEditar);

        }
        else if ("Mal".equals(chalecoRes)) {
            rdgChaleco.check(R.id.rbMalChalecoEditar);
        }

        if ("Bien".equals(cascoRes)) {
            rdgCasco.check(R.id.rbBienCascoEditar);

        }
        else if ("Mal".equals(cascoRes)) {
            rdgCasco.check(R.id.rbMalCascoEditar);
        }

        if ("Bien".equals(carroceriaRes)) {
            rdgCarroceria.check(R.id.rbBienCarroceriaEditar);

        }
        else if ("Mal".equals(carroceriaRes)) {
            rdgCarroceria.check(R.id.rbMalCarroceriaEditar);
        }

        if ("Bien".equals(dosEslingasPorBancoRes)) {
            rdgDosEslingasPorBanco.check(R.id.rbBienDosEslingasBancoEditar);

        }
        else if ("Mal".equals(dosEslingasPorBancoRes)) {
            rdgDosEslingasPorBanco.check(R.id.rbMalDosEslingasBancoEditar);
        }

        if ("Bien".equals(dosConosReflectivosRes)) {
            rdgDosConosReflectivos.check(R.id.rbBienDosConosReflectivosEditar);

        }
        else if ("Mal".equals(dosConosReflectivosRes)) {
            rdgDosConosReflectivos.check(R.id.rbMalDosEslingasBancoEditar);
        }

        if ("Bien".equals(paralesRes)) {
            rdgParales.check(R.id.rbBienParalesIgualLongitudEditar);

        }
        else if ("Mal".equals(paralesRes)) {
            rdgParales.check(R.id.rbMalParalesIgualLongitudEditar);
        }
    }

    private void actualizarDatosInfoVehiculo(){

        int rdgTarjetaPropiedadId = rdgTarjetaPropiedad.getCheckedRadioButtonId();
        int rdgSoatId = rdgSoat.getCheckedRadioButtonId();
        int rdgRevisionTecnicomecanicaId = rdgRevisionTecnicomecanica.getCheckedRadioButtonId();
        int rdgLucesAltasId = rdgLucesAltas.getCheckedRadioButtonId();
        int rdgLucesBajasId = rdgLucesBajas.getCheckedRadioButtonId();
        int rdgDireccionalesId = rdgDireccionales.getCheckedRadioButtonId();
        int rdgSonidoReversaId = rdgSonidoReversa.getCheckedRadioButtonId();
        int rdgReversaId = rdgReversa.getCheckedRadioButtonId();
        int rdgStopId = rdgStop.getCheckedRadioButtonId();
        int rdgRetrovisoresId = rdgRetrovisores.getCheckedRadioButtonId();
        int rdgPlumillasId = rdgPlumillas.getCheckedRadioButtonId();
        int rdgEstadoPanoramicosId = rdgEstadoPanoramicos.getCheckedRadioButtonId();
        int rdgEspejosId = rdgEspejos.getCheckedRadioButtonId();
        int rdgBocinaId = rdgBocina.getCheckedRadioButtonId();
        int rdgCinturonId = rdgCinturon.getCheckedRadioButtonId();
        int rdgFrenoId = rdgFreno.getCheckedRadioButtonId();
        int rdgLlantasId = rdgLlantas.getCheckedRadioButtonId();
        int rdgBotiquinId = rdgBotiquin.getCheckedRadioButtonId();
        int rdgExtintorABCId = rdgExtintorABC.getCheckedRadioButtonId();
        int rdgBotasId = rdgBotas.getCheckedRadioButtonId();
        int rdgChalecoId = rdgChaleco.getCheckedRadioButtonId();
        int rdgCascoId = rdgCasco.getCheckedRadioButtonId();
        int rdgCarroceriaId = rdgCarroceria.getCheckedRadioButtonId();
        int rdgDosEslingasPorBancoId = rdgDosEslingasPorBanco.getCheckedRadioButtonId();
        int rdgDosConosReflectivosId = rdgDosConosReflectivos.getCheckedRadioButtonId();
        int rdgParalesId = rdgParales.getCheckedRadioButtonId();

        // Verificar qué RadioButton fue seleccionado y actualizar el modelo

        if (rdgTarjetaPropiedadId == R.id.rbSiTarjetaPropiedadEditar) {
            infoVehiculoModel.setTarjetaPropiedad("Si");

        } else if (rdgTarjetaPropiedadId == R.id.rbNoTarjetaPropiedadEditar) {
            infoVehiculoModel.setTarjetaPropiedad("No");
        }

        if (rdgSoatId == R.id.rbSiSOATVigenteEditar) {
            infoVehiculoModel.setSoatVigente("Si");

        } else if (rdgSoatId == R.id.rbNoSOATVigenteEditar) {
            infoVehiculoModel.setSoatVigente("No");
        }

        if (rdgRevisionTecnicomecanicaId == R.id.rbSiRevisionTecnicomecanicaEditar) {
            infoVehiculoModel.setRevisionTecnicomecanica("Si");

        } else if (rdgRevisionTecnicomecanicaId == R.id.rbNoRevisionTecnicomecanicaEditar) {
            infoVehiculoModel.setRevisionTecnicomecanica("No");
        }

        if (rdgLucesAltasId == R.id.rbBienLucesAltasEditar) {
            infoVehiculoModel.setLucesAltas("Bien");

        } else if (rdgLucesAltasId == R.id.rbMalLucesAltasEditar) {
            infoVehiculoModel.setLucesAltas("Mal");
        }

        if (rdgLucesBajasId == R.id.rbBienLucesBajasEditar) {
            infoVehiculoModel.setLucesBajas("Bien");

        } else if (rdgLucesBajasId == R.id.rbMalLucesBajasEditar) {
            infoVehiculoModel.setLucesBajas("Mal");
        }

        if (rdgDireccionalesId == R.id.rbBienDireccionalesEditar) {
            infoVehiculoModel.setDireccionales("Bien");

        } else if (rdgDireccionalesId == R.id.rbMalDireccionalesEditar) {
            infoVehiculoModel.setDireccionales("Mal");
        }

        if (rdgSonidoReversaId == R.id.rbBienSonidoReversaEditar) {
            infoVehiculoModel.setSonidoReversa("Bien");

        } else if (rdgSonidoReversaId  == R.id.rbMalSonidoReversaEditar) {
            infoVehiculoModel.setSonidoReversa("Mal");
        }

        if (rdgReversaId == R.id.rbBienReversaEditar) {
            infoVehiculoModel.setReversa("Bien");

        } else if (rdgReversaId == R.id.rbMalReversaEditar) {
            infoVehiculoModel.setReversa("Mal");
        }

        if (rdgStopId == R.id.rbBienStopEditar) {
            infoVehiculoModel.setStop("Bien");

        } else if (rdgStopId == R.id.rbMalStopEditar) {
            infoVehiculoModel.setStop("Mal");
        }

        if (rdgRetrovisoresId == R.id.rbBienRetrovisoresEditar) {
            infoVehiculoModel.setRetrovisores("Bien");

        } else if (rdgRetrovisoresId == R.id.rbMalRetrovisoresEditar) {
            infoVehiculoModel.setRetrovisores("Mal");
        }

        if (rdgPlumillasId == R.id.rbBienPlumillasEditar) {
            infoVehiculoModel.setPlumillas("Bien");

        } else if (rdgPlumillasId == R.id.rbMalPlumillasEditar) {
            infoVehiculoModel.setPlumillas("Mal");
        }

        if (rdgEstadoPanoramicosId == R.id.rbBienEstadoPanoramicosEditar) {
            infoVehiculoModel.setEstadoPanoramicos("Bien");

        } else if (rdgEstadoPanoramicosId == R.id.rbMalEstadoPanoramicosEditar) {
            infoVehiculoModel.setEstadoPanoramicos("Mal");
        }

        if (rdgEspejosId == R.id.rbBienEspejosEditar) {
            infoVehiculoModel.setEspejos("Bien");

        } else if (rdgEspejosId == R.id.rbMalEspejosEditar) {
            infoVehiculoModel.setEspejos("Mal");
        }

        if (rdgBocinaId == R.id.rbBienBocinaEditar) {
            infoVehiculoModel.setBocina("Bien");

        } else if (rdgBocinaId == R.id.rbMalBocinaEditar) {
            infoVehiculoModel.setBocina("Mal");
        }

        if (rdgCinturonId == R.id.rbBienCinturonEditar) {
            infoVehiculoModel.setCinturon("Bien");

        } else if (rdgCinturonId == R.id.rbMalCinturonEditar) {
            infoVehiculoModel.setCinturon("Mal");
        }

        if (rdgFrenoId == R.id.rbBienFrenoEditar) {
            infoVehiculoModel.setFreno("Bien");

        } else if (rdgFrenoId == R.id.rbMalFrenoEditar) {
            infoVehiculoModel.setFreno("Mal");
        }

        if (rdgLlantasId == R.id.rbBienLlantasEditar) {
            infoVehiculoModel.setLlantas("Bien");

        } else if (rdgLlantasId == R.id.rbMalLlantasEditar) {
            infoVehiculoModel.setLlantas("Mal");
        }

        if (rdgBotiquinId == R.id.rbBienBotiquinEditar) {
            infoVehiculoModel.setBotiquin("Bien");

        } else if (rdgBotiquinId == R.id.rbMalBotiquinEditar) {
            infoVehiculoModel.setBotiquin("Mal");
        }

        if (rdgExtintorABCId == R.id.rbBienExtintorABCEditar) {
            infoVehiculoModel.setExtintorABC("Bien");

        } else if (rdgExtintorABCId == R.id.rbMalExtintorABCEditar) {
            infoVehiculoModel.setExtintorABC("Mal");
        }

        if (rdgBotasId == R.id.rbBienBotasEditar) {
            infoVehiculoModel.setBotas("Bien");

        } else if (rdgBotasId == R.id.rbMalBotasEditar) {
            infoVehiculoModel.setBotas("Mal");
        }

        if (rdgChalecoId == R.id.rbBienChalecoEditar) {
            infoVehiculoModel.setChaleco("Bien");

        } else if (rdgChalecoId == R.id.rbMalChalecoEditar) {
            infoVehiculoModel.setChaleco("Mal");
        }

        if (rdgCascoId == R.id.rbBienCascoEditar) {
            infoVehiculoModel.setCasco("Bien");

        } else if (rdgCascoId == R.id.rbMalCascoEditar) {
            infoVehiculoModel.setCasco("Mal");
        }

        if (rdgCarroceriaId== R.id.rbBienCarroceriaEditar) {
            infoVehiculoModel.setCarroceria("Bien");

        } else if (rdgCarroceriaId == R.id.rbMalCarroceriaEditar) {
            infoVehiculoModel.setCarroceria("Mal");
        }

        if (rdgDosEslingasPorBancoId == R.id.rbBienDosEslingasBancoEditar) {
            infoVehiculoModel.setDosEslingasBanco("Bien");

        } else if (rdgDosEslingasPorBancoId == R.id.rbMalDosEslingasBancoEditar) {
            infoVehiculoModel.setDosEslingasBanco("Mal");
        }

        if (rdgDosConosReflectivosId == R.id.rbBienDosConosReflectivosEditar) {
            infoVehiculoModel.setDosConosReflectivos("Bien");

        } else if (rdgDosConosReflectivosId == R.id.rbMalDosConosReflectivosEditar) {
            infoVehiculoModel.setDosConosReflectivos("Mal");
        }

        if (rdgParalesId == R.id.rbBienParalesIgualLongitudEditar) {
            infoVehiculoModel.setParales("Bien");

        } else if (rdgParalesId == R.id.rbMalParalesIgualLongitudEditar) {
            infoVehiculoModel.setParales("Mal");
        }

        String respuestaTarjetaPropiedad = infoVehiculoModel.getTarjetaPropiedad();
        String respuestaSoat = infoVehiculoModel.getSoatVigente();
        String respuestaRevisionTecnicomecanica = infoVehiculoModel.getRevisionTecnicomecanica();
        String respuestaLucesAltas = infoVehiculoModel.getLucesAltas();
        String respuestaucesBajas = infoVehiculoModel.getLucesBajas();
        String respuestaDireccionales = infoVehiculoModel.getDireccionales();
        String respuestaSonidoReversa = infoVehiculoModel.getSonidoReversa();
        String respuestaReversa = infoVehiculoModel.getReversa();
        String respuestaStop = infoVehiculoModel.getStop();
        String respuestaRetrovisores = infoVehiculoModel.getRetrovisores();
        String respuestaPlumillas = infoVehiculoModel.getPlumillas();
        String respuestaEstadoPanoramicos = infoVehiculoModel.getEstadoPanoramicos();
        String respuestaEspejos = infoVehiculoModel.getEspejos();
        String respuestaBocina = infoVehiculoModel.getBocina();
        String respuestaCinturon = infoVehiculoModel.getCinturon();
        String respuestaFreno = infoVehiculoModel.getFreno();
        String respuestaLlantas = infoVehiculoModel.getLlantas();
        String respuestaBotiquin = infoVehiculoModel.getBotiquin();
        String respuestaExtintorABC = infoVehiculoModel.getExtintorABC();
        String respuestaBotas = infoVehiculoModel.getBotas();
        String respuestaChaleco = infoVehiculoModel.getChaleco();
        String respuestaCasco = infoVehiculoModel.getCasco();
        String respuestaCarroceria = infoVehiculoModel.getCarroceria();
        String respuestaDosEslingasPorBanco = infoVehiculoModel.getDosEslingasBanco();
        String respuestaDosConosReflectivos = infoVehiculoModel.getDosConosReflectivos();
        String respuestaParales = infoVehiculoModel.getParales();

        datosInfoVehiculo.put("placa",edtPlaca.getText().toString());
        datosInfoVehiculo.put("vehiculo",edtVehiculo.getText().toString());
        datosInfoVehiculo.put("tarjetaPropiedad",respuestaTarjetaPropiedad);
        datosInfoVehiculo.put("revisionTecnicomecanica",respuestaRevisionTecnicomecanica);
        datosInfoVehiculo.put("soatVigente",respuestaSoat);
        datosInfoVehiculo.put("lucesAltas",respuestaLucesAltas);
        datosInfoVehiculo.put("lucesBajas",respuestaucesBajas);
        datosInfoVehiculo.put("direccionales",respuestaDireccionales);
        datosInfoVehiculo.put("sonidoReversa",respuestaSonidoReversa);
        datosInfoVehiculo.put("reversa",respuestaReversa);
        datosInfoVehiculo.put("stop",respuestaStop);
        datosInfoVehiculo.put("retrovisores",respuestaRetrovisores);
        datosInfoVehiculo.put("plumillas",respuestaPlumillas);
        datosInfoVehiculo.put("estadoPanoramicos",respuestaEstadoPanoramicos);
        datosInfoVehiculo.put("espejos",respuestaEspejos);
        datosInfoVehiculo.put("bocina",respuestaBocina);
        datosInfoVehiculo.put("cinturon",respuestaCinturon);
        datosInfoVehiculo.put("freno",respuestaFreno);
        datosInfoVehiculo.put("llantas",respuestaLlantas);
        datosInfoVehiculo.put("botiquin",respuestaBotiquin);
        datosInfoVehiculo.put("extintorABC",respuestaExtintorABC);
        datosInfoVehiculo.put("botas",respuestaBotas);
        datosInfoVehiculo.put("chaleco",respuestaChaleco);
        datosInfoVehiculo.put("casco",respuestaCasco);
        datosInfoVehiculo.put("carroceria",respuestaCarroceria);
        datosInfoVehiculo.put("dosEslingasBanco",respuestaDosEslingasPorBanco);
        datosInfoVehiculo.put("dosConosReflectivos",respuestaDosConosReflectivos);
        datosInfoVehiculo.put("parales",respuestaParales);
        datosInfoVehiculo.put("observacionesCamion",edtObservacionesInfoVehiculo.getText().toString());

        if(isNetworkAvailable()){
            actualizarEstadoLista();
            //if(id_lista != null){
            if(idListaPos != null){
                db.collection(pathLista)
                        .document(idListaPos)
                        .update("Item_3_Informacion_vehiculo", datosInfoVehiculo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Datos actualizados con éxito", Toast.LENGTH_SHORT).show();
                                //loadDataFirebase();

                                //listasCargueDescargueRecycler.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "No se actualizaron los datos", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        }
        else {
            actualizarEstadoLista();
            int listId = getArguments().getInt("list_id", -1);
            Log.d("ID LISTA", "ID LISTA : " + listId);

            if (listId == -1) {
                Toast.makeText(getContext(), "ID no válido", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            } else {
                dbLocal = new ListasCargueDataBaseHelper(requireContext());
                listasCargueModel = dbLocal.getListById(listId);
                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            }
            if (listasCargueModel != null) {

                listasCargueModel.getItem_3_Informacion_vehiculo().setPlaca(edtPlaca.getText().toString());
                listasCargueModel.getItem_3_Informacion_vehiculo().setVehiculo(edtVehiculo.getText().toString());
                listasCargueModel.getItem_3_Informacion_vehiculo().setTarjetaPropiedad(respuestaTarjetaPropiedad);
                listasCargueModel.getItem_3_Informacion_vehiculo().setSoatVigente(respuestaRevisionTecnicomecanica);
                listasCargueModel.getItem_3_Informacion_vehiculo().setRevisionTecnicomecanica(respuestaSoat);
                listasCargueModel.getItem_3_Informacion_vehiculo().setLucesAltas(respuestaLucesAltas);
                listasCargueModel.getItem_3_Informacion_vehiculo().setLucesBajas(respuestaucesBajas);
                listasCargueModel.getItem_3_Informacion_vehiculo().setDireccionales(respuestaDireccionales);
                listasCargueModel.getItem_3_Informacion_vehiculo().setSonidoReversa(respuestaSonidoReversa);
                listasCargueModel.getItem_3_Informacion_vehiculo().setReversa(respuestaReversa);
                listasCargueModel.getItem_3_Informacion_vehiculo().setStop(respuestaStop);
                listasCargueModel.getItem_3_Informacion_vehiculo().setRetrovisores(respuestaRetrovisores);
                listasCargueModel.getItem_3_Informacion_vehiculo().setPlumillas(respuestaPlumillas);
                listasCargueModel.getItem_3_Informacion_vehiculo().setEstadoPanoramicos(respuestaEstadoPanoramicos);
                listasCargueModel.getItem_3_Informacion_vehiculo().setEspejos(respuestaEspejos);
                listasCargueModel.getItem_3_Informacion_vehiculo().setBocina(respuestaBocina);
                listasCargueModel.getItem_3_Informacion_vehiculo().setCinturon(respuestaCinturon);
                listasCargueModel.getItem_3_Informacion_vehiculo().setFreno(respuestaFreno);
                listasCargueModel.getItem_3_Informacion_vehiculo().setLlantas(respuestaLlantas);
                listasCargueModel.getItem_3_Informacion_vehiculo().setBotiquin(respuestaBotiquin);
                listasCargueModel.getItem_3_Informacion_vehiculo().setExtintorABC(respuestaExtintorABC);
                listasCargueModel.getItem_3_Informacion_vehiculo().setBotas(respuestaBotas);
                listasCargueModel.getItem_3_Informacion_vehiculo().setChaleco(respuestaChaleco);
                listasCargueModel.getItem_3_Informacion_vehiculo().setCasco(respuestaCasco);
                listasCargueModel.getItem_3_Informacion_vehiculo().setCarroceria(respuestaCarroceria);
                listasCargueModel.getItem_3_Informacion_vehiculo().setDosEslingasBanco(respuestaDosEslingasPorBanco);
                listasCargueModel.getItem_3_Informacion_vehiculo().setDosConosReflectivos(respuestaDosConosReflectivos);
                listasCargueModel.getItem_3_Informacion_vehiculo().setParales(respuestaParales);
                listasCargueModel.getItem_3_Informacion_vehiculo().setObservacionesCamion(edtObservacionesInfoVehiculo.getText().toString());
                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                dbLocal.updateList(listasCargueModel);
            }
        }
    }

    private void obtenerDatosInfoVehiculo(){
        db.collection("desplegables info vehiculo")
                //.get(Source.CACHE)
                .get(Source.DEFAULT)
                .addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                List<String> vehiculoList = new ArrayList<>();

                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){

                    List<String> vehiculos = (List<String>) documentSnapshot.get("vehiculo");

                    if(vehiculos != null){
                        vehiculoList.addAll(vehiculos);
                    }
                }

                Context context = getContext();
                if (context != null) {
                    ArrayAdapter<String> adapterVehiculo = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, vehiculoList);
                    edtVehiculo.setAdapter(adapterVehiculo);
                }
            }
            else {
                Toast.makeText(getContext(), "Se están recopilando algunos datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMessage(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }

    private boolean verificarCampos() {
        if (TextUtils.isEmpty(edtPlaca.getText())) {
            showMessage("Debes ingresar la placa del vehículo");
            return false;
        }

        if (TextUtils.isEmpty(edtVehiculo.getText())) {
            showMessage("Debes ingresar el tipo de vehículo");
            return false;
        }

        if (rdgTarjetaPropiedad.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en la Tarjeta de propiedad");
            return false;
        }

        if (rdgSoat.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Soat");
            return false;
        }

        if (rdgRevisionTecnicomecanica.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Revisión tecnicomecánica");
            return false;
        }

        if (rdgLucesAltas.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en las Luces altas");
            return false;
        }
        if (rdgLucesBajas.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en las Luces bajas");
            return false;
        }

        if (rdgDireccionales.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en las Direccionales");
            return false;
        }
        if (rdgSonidoReversa.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Sonido de reversa");
            return false;
        }
        if (rdgReversa.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en la Reversa");
            return false;
        }
        if (rdgStop.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Stop");
            return false;
        }
        if (rdgRetrovisores.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en los Retrovisores");
            return false;
        }
        if (rdgPlumillas.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en las Plumillas");
            return false;
        }
        if (rdgEstadoPanoramicos.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Estado de panorámicos");
            return false;
        }
        if (rdgEspejos.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en los Espejos");
            return false;
        }
        if (rdgBocina.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en la Bocina");
            return false;
        }
        if (rdgCinturon.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Cinturón");
            return false;
        }
        if (rdgFreno.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Freno");
            return false;
        }
        if (rdgLlantas.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en las Llantas");
            return false;
        }
        if (rdgBotiquin.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Botiquín");
            return false;
        }
        if (rdgExtintorABC.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Extintor ABC");
            return false;
        }

        if (rdgBotas.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en las Botas");
            return false;
        }
        if (rdgChaleco.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Chaleco");
            return false;
        }

        if (rdgCasco.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en el Casco");
            return false;
        }
        if (rdgCarroceria.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en la Carrocería");
            return false;
        }
        if (rdgDosEslingasPorBanco.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en Dos eslingas por banco");
            return false;
        }
        if (rdgDosConosReflectivos.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en Dos conos reflectivos");
            return false;
        }
        if (rdgParales.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en Parales");
            return false;
        }
        return true;
    }

    public interface OnEstadoCamposEditarInfoVehiculo {
        void onEstadoCamposEditarInfoVehiculo(boolean camposCompletos);
    }

    private OnEstadoCamposEditarInfoVehiculo listener3;

    private void actualizarEstadoLista() {
        boolean camposCompletos = verificarCampos();

        if (listener3 != null) {
            listener3.onEstadoCamposEditarInfoVehiculo(camposCompletos);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnEstadoCamposEditarInfoVehiculo) {
            listener3 = (OnEstadoCamposEditarInfoVehiculo) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEstadoCamposInfoVehiculoListener");
        }
    }
}