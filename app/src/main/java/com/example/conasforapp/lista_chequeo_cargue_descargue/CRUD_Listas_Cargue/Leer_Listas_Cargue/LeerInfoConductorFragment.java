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
import com.example.conasforapp.databinding.FragmentLeerInfoConductorBinding;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.material.textfield.TextInputEditText;


public class LeerInfoConductorFragment extends Fragment {
    private FragmentLeerInfoConductorBinding binding;
    private TextInputEditText edtNombreConductor, edtCedula, edtLugarExpedicion,
            edtEps,edtArl,edtAfp;
    private RadioGroup rdgLicConduccion, rdgPolizaRCE, rdgEPS, rdgARL, rdgAFP;
    private ListasCargueModel listasCargueModel = new ListasCargueModel();
    private ListasCargueDataBaseHelper dbLocal;
    private String respuestaSi = "Si",respuestaNo = "No";
    String licConduccionRes, polizaRCERes, EPSRes, ARLRes, AFPRes;
    private static int listIdLocal= 0;
    private static final String ARG_LIST_ID = "list_id";


    // Método estático para crear una instancia del fragmento con argumentos
    public static LeerInfoConductorFragment newInstance(int listId) {
        Log.d("LIST ID LEER INFO CONDUCTOR NEW INSTANCE","LIST ID LEER INFO CONDUCTOR NEW INSTANCE : " + listId);
        listIdLocal = listId;
        LeerInfoConductorFragment fragment = new LeerInfoConductorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    public LeerInfoConductorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //rootView = inflater.inflate(R.layout.fragment_leer_info_conductor, container, false);
        binding = FragmentLeerInfoConductorBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        dbLocal = new ListasCargueDataBaseHelper(requireContext());

        edtNombreConductor = binding.edtNombreConductorLeerCD;
        edtCedula = binding.edtCedulaConductorLeerCD;
        edtLugarExpedicion = binding.edtLugarExpedicionConductorLeerCD;
        rdgLicConduccion = binding.rdgLicConduccionLeerCD;
        rdgPolizaRCE = binding.rdgPolizaRCELeerCD;

        rdgEPS = binding.rdgEPSLeerCD;
        rdgARL = binding.rdgARLLeerCD;
        rdgAFP = binding.rdgAFPLeerCD;

        edtEps = binding.atcEPSLeerCD;
        edtArl = binding.atcARLLeerCD;
        edtAfp = binding.atcAFPLeerCD;

        int colorNegro = ContextCompat.getColor(getContext(), R.color.black);

        edtNombreConductor.setTextColor(colorNegro);
        edtCedula.setTextColor(colorNegro);
        edtLugarExpedicion.setTextColor(colorNegro);
        edtEps.setTextColor(colorNegro);
        edtAfp.setTextColor(colorNegro);
        edtArl.setTextColor(colorNegro);

        if (!isNetworkAvailable()){
            loadDataDBLocal(listIdLocal);
        }
        else
        {
            loadDataFirebase();
        }

        return rootView;
    }

    private void resRadioButton(){
        if(isNetworkAvailable()){
            licConduccionRes = AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getLicConduccionRes();
            polizaRCERes = AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getPolizaRCERes();
            EPSRes = AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getEpsRes();
            ARLRes = AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getArlRes();
            AFPRes = AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getAfpRes();
        }
        else
        {
            licConduccionRes = listasCargueModel.getItem_2_Informacion_del_conductor().getLicConduccionRes();
            polizaRCERes = listasCargueModel.getItem_2_Informacion_del_conductor().getPolizaRCERes();
            EPSRes = listasCargueModel.getItem_2_Informacion_del_conductor().getEpsRes();
            ARLRes = listasCargueModel.getItem_2_Informacion_del_conductor().getArlRes();
            AFPRes = listasCargueModel.getItem_2_Informacion_del_conductor().getAfpRes();
        }

        //Se chequea el valor de selección que tienen los radiobutton
        if (respuestaSi.equals(licConduccionRes)) {
            rdgLicConduccion.check(R.id.rbSiLicConduccionLeerCD);

        } else if (respuestaNo.equals(licConduccionRes)) {
            rdgLicConduccion.check(R.id.rbNoLicConduccionLeerCD);
        }

        if (respuestaSi.equals(polizaRCERes)) {
            rdgPolizaRCE.check(R.id.rbSiPolizaLeerCD);

        } else if (respuestaNo.equals(polizaRCERes)) {
            rdgPolizaRCE.check(R.id.rbNoPolizaLeerCD);
        }

        if (respuestaSi.equals(EPSRes)) {
            rdgEPS.check(R.id.rbSiEPSLeerCD);

        } else if (respuestaNo.equals(EPSRes)) {
            rdgEPS.check(R.id.rbNoEPSLeerCD);
        }

        if (respuestaSi.equals(ARLRes)) {
            rdgARL.check(R.id.rbSiARLLeerCD);

        } else if (respuestaNo.equals(ARLRes)) {
            rdgARL.check(R.id.rbNoARLLeerCD);
        }

        if (respuestaSi.equals(AFPRes)) {
            rdgAFP.check(R.id.rbSiAFPLeerCD);

        } else if (respuestaNo.equals(AFPRes)) {
            rdgAFP.check(R.id.rbNoAFPLeerCD);
        }
    }

    private void loadDataFirebase() {
        edtNombreConductor.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getNombreConductor());
        edtCedula.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getCedula());
        edtLugarExpedicion.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getLugarExpedicion());

        resRadioButton();

        edtEps.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getCualEPS());
        edtAfp.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getCualAFP());
        edtArl.setText(AgregarMostrarListas.listasCargueModel.getItem_2_Informacion_del_conductor().getCualARL());
    }

    private void loadDataDBLocal(Integer listId){
        listasCargueModel = dbLocal.getListById(listId);

        edtNombreConductor.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getNombreConductor());
        edtCedula.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCedula());
        edtLugarExpedicion.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getLugarExpedicion());

        resRadioButton();

        edtEps.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCualEPS());
        edtArl.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCualARL());
        edtAfp.setText(listasCargueModel.getItem_2_Informacion_del_conductor().getCualAFP());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}