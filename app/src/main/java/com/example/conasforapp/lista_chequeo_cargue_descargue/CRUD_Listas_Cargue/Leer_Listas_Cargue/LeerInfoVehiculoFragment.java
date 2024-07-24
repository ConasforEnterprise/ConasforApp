package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Leer_Listas_Cargue;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class LeerInfoVehiculoFragment extends Fragment {
    View rootView;
    private TextInputEditText edtPlaca, edtVehiculo;
    private FirebaseFirestore db;
    private RadioGroup rdgTarjetaPropiedad, rdgSoat, rdgRevisionTecnicomecanica,
            rdgLucesAltas, rdgLucesBajas, rdgDireccionales, rdgSonidoReversa, rdgReversa,
            rdgStop, rdgRetrovisores, rdgPlumillas, rdgEstadoPanoramicos, rdgEspejos,
            rdgBocina, rdgCinturon, rdgFreno, rdgLlantas, rdgBotiquin, rdgExtintorABC, rdgBotas,
            rdgChaleco, rdgCasco, rdgCarroceria, rdgDosEslingasPorBanco, rdgDosConosReflectivos,
            rdgParales;

    private TextInputEditText edtObservacionesInfoVehiculo;

    private String pathLista = "Listas chequeo cargue descargue";

    private ListasCargueDataBaseHelper dbLocal;

    private ListasCargueModel listasCargueModel = new ListasCargueModel();
    private String tarjetaPropiedadRes, soatRes, revisionTecnicomecanicaRes, lucesAltasRes, lucesBajasRes,
            direccionalesRes, sonidoReversaRes,reversaRes, stopRes, retrovisoresRes, plumillasRes,
            estadoPanoramicosRes, espejosRes, bocinaRes, cinturonRes, frenoRes, llantasRes, botiquinRes,
            extintorABCRes, botasRes, chalecoRes, cascoRes, carroceriaRes, dosEslingasPorBancoRes,
            dosConosReflectivosRes, paralesRes;

    private static final String ARG_LIST_ID = "list_id";

    private static int listIdLocal= 0;

    public static LeerInfoVehiculoFragment newInstance(int listId) {
        Log.d("LIST ID LEER INFO VEHICULO NEW INSTANCE","LIST ID LEER INFO VEHICULO NEW INSTANCE : " + listId);
        listIdLocal = listId;
        LeerInfoVehiculoFragment fragment = new LeerInfoVehiculoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    public LeerInfoVehiculoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_leer_info_vehiculo, container, false);

        db = FirebaseFirestore.getInstance();
        dbLocal = new ListasCargueDataBaseHelper(requireContext());

        edtPlaca = rootView.findViewById(R.id.edtPlacaLeerCD);
        edtVehiculo = rootView.findViewById(R.id.edtVehiculoLeerCD);
        edtObservacionesInfoVehiculo = rootView.findViewById(R.id.edtObservacionesInfoVehiculoLeerCD);

        rdgTarjetaPropiedad = rootView.findViewById(R.id.rdgTarjetaPropiedadLeerCD);
        rdgSoat = rootView.findViewById(R.id.rdgSOATVigenteLeerCD);
        rdgRevisionTecnicomecanica = rootView.findViewById(R.id.rdgRevisionTecnicomecanicaLeerCD);
        rdgLucesAltas = rootView.findViewById(R.id.rdgLucesAltasLeerCD);
        rdgLucesBajas = rootView.findViewById(R.id.rdgLucesBajasLeerCD);
        rdgDireccionales = rootView.findViewById(R.id.rdgDireccionalesLeerCD);
        rdgSonidoReversa = rootView.findViewById(R.id.rdgSonidoReversaLeerCD);
        rdgReversa = rootView.findViewById(R.id.rdgReversaLeerCD);
        rdgStop = rootView.findViewById(R.id.rdgStopLeerCD);
        rdgRetrovisores = rootView.findViewById(R.id.rdgRetrovisoresLeerCD);
        rdgPlumillas = rootView.findViewById(R.id.rdgPlumillasLeerCD);
        rdgEstadoPanoramicos = rootView.findViewById(R.id.rdgEstadoPanoramicosLeerCD);
        rdgEspejos = rootView.findViewById(R.id.rdgEspejosLeerCD);
        rdgBocina = rootView.findViewById(R.id.rdgBocinaLeerCD);
        rdgCinturon = rootView.findViewById(R.id.rdgCinturonLeerCD);
        rdgFreno = rootView.findViewById(R.id.rdgFrenoLeerCD);
        rdgLlantas = rootView.findViewById(R.id.rdgLlantasLeerCD);
        rdgBotiquin = rootView.findViewById(R.id.rdgBotiquinLeerCD);
        rdgExtintorABC = rootView.findViewById(R.id.rdgExtintorABCLeerCD);
        rdgBotas = rootView.findViewById(R.id.rdgBotasLeerCD);
        rdgChaleco = rootView.findViewById(R.id.rdgChalecoLeerCD);
        rdgCasco = rootView.findViewById(R.id.rdgCascoLeerCD);
        rdgCarroceria = rootView.findViewById(R.id.rdgCarroceriaLeerCD);
        rdgDosEslingasPorBanco = rootView.findViewById(R.id.rdgDosEslingasBancoLeerCD);
        rdgDosConosReflectivos = rootView.findViewById(R.id.rdgDosConosReflectivosLeerCD);
        rdgParales = rootView.findViewById(R.id.rdgParalesIgualLongitudLeerCD);
        edtObservacionesInfoVehiculo = rootView.findViewById(R.id.edtObservacionesInfoVehiculoLeerCD);

        int colorNegro = ContextCompat.getColor(getContext(), R.color.black);
        edtPlaca.setTextColor(colorNegro);
        edtVehiculo.setTextColor(colorNegro);
        edtObservacionesInfoVehiculo.setTextColor(colorNegro);

        if(isNetworkAvailable()){
            loadDataFirebase();
        }
        else {
            loadDataDBLocal(listIdLocal);

            Log.d("LIST ID LOCAL VEHICUALO","LIST ID LOCAL VEHICUALO : "+listIdLocal);
        }

        return rootView;
    }

    private void resRadiobutton(){
        if(isNetworkAvailable()){
            tarjetaPropiedadRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getTarjetaPropiedad();
            soatRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getSoatVigente();
            revisionTecnicomecanicaRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getRevisionTecnicomecanica();
            lucesAltasRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getLucesAltas();
            lucesBajasRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getLucesBajas();
            direccionalesRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getDireccionales();
            sonidoReversaRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getSonidoReversa();
            reversaRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getReversa();
            stopRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getStop();
            retrovisoresRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getRetrovisores();
            plumillasRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getPlumillas();
            estadoPanoramicosRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getEstadoPanoramicos();
            espejosRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getEspejos();
            bocinaRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getBocina();
            cinturonRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getCinturon();
            frenoRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getFreno();
            llantasRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getLlantas();
            botiquinRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getBotiquin();
            extintorABCRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getExtintorABC();
            botasRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getBotas();
            chalecoRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getChaleco();
            cascoRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getCasco();
            carroceriaRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getCarroceria();
            dosEslingasPorBancoRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getDosEslingasBanco();
            dosConosReflectivosRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getDosConosReflectivos();
            paralesRes = AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getParales();
        }
        else {
            tarjetaPropiedadRes = listasCargueModel.getItem_3_Informacion_vehiculo().getTarjetaPropiedad();
            soatRes = listasCargueModel.getItem_3_Informacion_vehiculo().getSoatVigente();
            revisionTecnicomecanicaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getRevisionTecnicomecanica();
            lucesAltasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getLucesAltas();
            lucesBajasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getLucesBajas();
            direccionalesRes = listasCargueModel.getItem_3_Informacion_vehiculo().getDireccionales();
            sonidoReversaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getSonidoReversa();
            reversaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getReversa();
            stopRes = listasCargueModel.getItem_3_Informacion_vehiculo().getStop();
            retrovisoresRes = listasCargueModel.getItem_3_Informacion_vehiculo().getRetrovisores();
            plumillasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getPlumillas();
            estadoPanoramicosRes = listasCargueModel.getItem_3_Informacion_vehiculo().getEstadoPanoramicos();
            espejosRes = listasCargueModel.getItem_3_Informacion_vehiculo().getEspejos();
            bocinaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getBocina();
            cinturonRes = listasCargueModel.getItem_3_Informacion_vehiculo().getCinturon();
            frenoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getFreno();
            llantasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getLlantas();
            botiquinRes = listasCargueModel.getItem_3_Informacion_vehiculo().getBotiquin();
            extintorABCRes = listasCargueModel.getItem_3_Informacion_vehiculo().getExtintorABC();
            botasRes = listasCargueModel.getItem_3_Informacion_vehiculo().getBotas();
            chalecoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getChaleco();
            cascoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getCasco();
            carroceriaRes = listasCargueModel.getItem_3_Informacion_vehiculo().getCarroceria();
            dosEslingasPorBancoRes = listasCargueModel.getItem_3_Informacion_vehiculo().getDosEslingasBanco();
            dosConosReflectivosRes = listasCargueModel.getItem_3_Informacion_vehiculo().getDosConosReflectivos();
            paralesRes = listasCargueModel.getItem_3_Informacion_vehiculo().getParales();
        }

        //Se chequea el valor de selección que tienen los radiobutton
        if ("Si".equals(tarjetaPropiedadRes)) {
            rdgTarjetaPropiedad.check(R.id.rbSiTarjetaPropiedadLeerCD);

        } else if ("No".equals(tarjetaPropiedadRes)) {
            rdgTarjetaPropiedad.check(R.id.rbNoTarjetaPropiedadLeerCD);
        }

        if ("Si".equals(soatRes)) {
            rdgSoat.check(R.id.rbSiSOATVigenteLeerCD);

        } else if ("No".equals(soatRes)) {
            rdgSoat.check(R.id.rbNoSOATVigenteLeerCD);
        }

        if ("Si".equals(revisionTecnicomecanicaRes)) {
            rdgRevisionTecnicomecanica.check(R.id.rbSiRevisionTecnicomecanicaLeerCD);

        } else if ("No".equals(revisionTecnicomecanicaRes)) {
            rdgRevisionTecnicomecanica.check(R.id.rbNoRevisionTecnicomecanicaLeerCD);
        }

        if ("Bien".equals(lucesAltasRes)) {
            rdgLucesAltas.check(R.id.rbBienLucesAltasLeerCD);

        } else if ("Mal".equals(lucesAltasRes)) {
            rdgLucesAltas.check(R.id.rbMalLucesAltasLeerCD);
        }

        if ("Bien".equals(lucesBajasRes)) {
            rdgLucesBajas.check(R.id.rbBienLucesBajasLeerCD);

        } else if ("Mal".equals(lucesBajasRes)) {
            rdgLucesBajas.check(R.id.rbMalLucesBajasLeerCD);
        }

        if ("Bien".equals(direccionalesRes)) {
            rdgDireccionales.check(R.id.rbBienDireccionalesLeerCD);

        } else if ("Mal".equals(direccionalesRes)) {
            rdgDireccionales.check(R.id.rbMalDireccionalesLeerCD);
        }

        if ("Bien".equals(sonidoReversaRes)) {
            rdgSonidoReversa.check(R.id.rbBienSonidoReversaLeerCD);

        } else if ("Mal".equals(sonidoReversaRes)) {
            rdgSonidoReversa.check(R.id.rbMalSonidoReversaLeerCD);
        }

        if ("Bien".equals(reversaRes)) {
            rdgReversa.check(R.id.rbBienReversaLeerCD);

        } else if ("Mal".equals(reversaRes)) {
            rdgReversa.check(R.id.rbMalReversaLeerCD);
        }

        if ("Bien".equals(stopRes)) {
            rdgStop.check(R.id.rbBienStopLeerCD);

        } else if ("Mal".equals(stopRes)) {
            rdgStop.check(R.id.rbMalStopLeerCD);
        }


        if ("Bien".equals(retrovisoresRes)) {
            rdgRetrovisores.check(R.id.rbBienRetrovisoresLeerCD);

        } else if ("Mal".equals(retrovisoresRes)) {
            rdgRetrovisores.check(R.id.rbMalRetrovisoresLeerCD);
        }

        if ("Bien".equals(plumillasRes)) {
            rdgPlumillas.check(R.id.rbBienPlumillasLeerCD);

        } else if ("Mal".equals(plumillasRes)) {
            rdgPlumillas.check(R.id.rbMalPlumillasLeerCD);
        }

        if ("Bien".equals(estadoPanoramicosRes)) {
            rdgEstadoPanoramicos.check(R.id.rbBienEstadoPanoramicosLeerCD);

        } else if ("Mal".equals(estadoPanoramicosRes)) {
            rdgEstadoPanoramicos.check(R.id.rbMalEstadoPanoramicosLeerCD);
        }

        if ("Bien".equals(espejosRes)) {
            rdgEspejos.check(R.id.rbBienEspejosLeerCD);

        } else if ("Mal".equals(espejosRes)) {
            rdgEspejos.check(R.id.rbMalEspejosLeerCD);
        }

        if ("Bien".equals(bocinaRes)) {
            rdgBocina.check(R.id.rbBienBocinaLeerCD);

        } else if ("Mal".equals(bocinaRes)) {
            rdgBocina.check(R.id.rbMalBocinaLeerCD);
        }

        if ("Bien".equals(cinturonRes)) {
            rdgCinturon.check(R.id.rbBienCinturonLeerCD);

        } else if ("Mal".equals(cinturonRes)) {
            rdgCinturon.check(R.id.rbMalCinturonLeerCD);
        }

        if ("Bien".equals(frenoRes)) {
            rdgFreno.check(R.id.rbBienFrenoLeerCD);

        } else if ("Mal".equals(frenoRes)) {
            rdgFreno.check(R.id.rbMalFrenoLeerCD);
        }

        if ("Bien".equals(llantasRes)) {
            rdgLlantas.check(R.id.rbBienLlantasLeerCD);

        } else if ("Mal".equals(llantasRes)) {
            rdgLlantas.check(R.id.rbMalLlantasLeerCD);
        }

        if ("Bien".equals(botiquinRes)) {
            rdgBotiquin.check(R.id.rbBienBotiquinLeerCD);

        } else if ("Mal".equals(botiquinRes)) {
            rdgBotiquin.check(R.id.rbMalBotiquinLeerCD);
        }

        if ("Bien".equals(extintorABCRes)) {
            rdgExtintorABC.check(R.id.rbBienExtintorABCLeerCD);

        } else if ("Mal".equals(extintorABCRes)) {
            rdgExtintorABC.check(R.id.rbMalExtintorABCLeerCD);
        }

        if ("Bien".equals(botasRes)) {
            rdgBotas.check(R.id.rbBienBotasLeerCD);

        } else if ("Mal".equals(botasRes)) {
            rdgBotas.check(R.id.rbMalBotasLeerCD);
        }

        if ("Bien".equals(chalecoRes)) {
            rdgChaleco.check(R.id.rbBienChalecoLeerCD);

        } else if ("Mal".equals(chalecoRes)) {
            rdgChaleco.check(R.id.rbMalChalecoLeerCD);
        }

        if ("Bien".equals(cascoRes)) {
            rdgCasco.check(R.id.rbBienCascoLeerCD);

        } else if ("Mal".equals(cascoRes)) {
            rdgCasco.check(R.id.rbMalCascoLeerCD);
        }

        if ("Bien".equals(carroceriaRes)) {
            rdgCarroceria.check(R.id.rbBienCarroceriaLeerCD);

        } else if ("Mal".equals(carroceriaRes)) {
            rdgCarroceria.check(R.id.rbMalCarroceriaLeerCD);
        }

        if ("Bien".equals(dosEslingasPorBancoRes)) {
            rdgDosEslingasPorBanco.check(R.id.rbBienDosEslingasBancoLeerCD);

        } else if ("Mal".equals(dosEslingasPorBancoRes)) {
            rdgDosEslingasPorBanco.check(R.id.rbMalDosEslingasBancoLeerCD);
        }

        if ("Bien".equals(dosConosReflectivosRes)) {
            rdgDosConosReflectivos.check(R.id.rbBienDosConosReflectivosLeerCD);

        } else if ("Mal".equals(dosConosReflectivosRes)) {
            rdgDosConosReflectivos.check(R.id.rbMalDosEslingasBancoLeerCD);
        }

        if ("Bien".equals(paralesRes)) {
            rdgParales.check(R.id.rbBienParalesIgualLongitudLeerCD);

        } else if ("Mal".equals(paralesRes)) {
            rdgParales.check(R.id.rbMalParalesIgualLongitudLeerCD);
        }
    }


    private void loadDataFirebase() {
        edtPlaca.setText(AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getPlaca());
        edtVehiculo.setText(AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getVehiculo());
        edtObservacionesInfoVehiculo.setText(AgregarMostrarListas.listasCargueModel.getItem_3_Informacion_vehiculo().getObservacionesCamion());
        resRadiobutton();
    }

    private void loadDataDBLocal(Integer listId) {
        listasCargueModel = dbLocal.getListById(listId);

        edtPlaca.setText(listasCargueModel.getItem_3_Informacion_vehiculo().getPlaca());
        edtVehiculo.setText(listasCargueModel.getItem_3_Informacion_vehiculo().getVehiculo());
        edtObservacionesInfoVehiculo.setText(listasCargueModel.getItem_3_Informacion_vehiculo().getObservacionesCamion());
        resRadiobutton();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}