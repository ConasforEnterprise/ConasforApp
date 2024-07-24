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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class LeerEstadoCargueFragment extends Fragment {
    View rootView;

    private TextInputEditText edtHoraSalida;
    private ImageView imgCapturaFoto;
    private RadioGroup rdgMaderaNoSuperaAlturaMampara,
            rdgMaderaNoSuperaParales, rdgNoMaderaAtravieseMampara,
            rdgParalesMismaAltura, rdgNingunaUndSobrepasaParales,
            rdgCadaBancoAseguradoEslingas, rdgCarroceriaParlesBuenEstado,
            rdgConductorSalioCinturon, rdgParalesAbatiblesAseguradosEstrobos;
    private FirebaseFirestore db;
    private String pathLista = "Listas chequeo cargue descargue";
    private ListasCargueDataBaseHelper dbLocal;
    private ListasCargueModel listasCargueModel = new ListasCargueModel();
    String maderaNoSuperaMamparaRes, maderaNoSuperaParalesRes, NoMaderaAtraviesaMamparaRes, paralesMismaAlturaRes,
            ningunaUndSobrepasaParalesRes,cadaBancoAseguradoEslingasRes, carroceriaParalesBuenEstadoRes,
            conductorSalioLugarCinturonRes, paralesAbatiblesAseguradosEstrobosRes;

    private static int listIdLocal= 0;
    private static final String ARG_LIST_ID = "list_id";

    // Método estático para crear una instancia del fragmento con argumentos
    public static LeerEstadoCargueFragment newInstance(int listId) {
        Log.d("LIST ID LEER INFO CONDUCTOR NEW INSTANCE","LIST ID LEER INFO CONDUCTOR NEW INSTANCE : " + listId);
        listIdLocal = listId;
        LeerEstadoCargueFragment fragment = new LeerEstadoCargueFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LIST_ID, listId);
        fragment.setArguments(args);
        return fragment;
    }

    public LeerEstadoCargueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_leer_estado_cargue, container, false);

        db = FirebaseFirestore.getInstance();
        dbLocal = new ListasCargueDataBaseHelper(requireContext());

        edtHoraSalida = rootView.findViewById(R.id.edtHoraSalidaSitioLeer);
        imgCapturaFoto = rootView.findViewById(R.id.imgFotoCamionLeer);
        rdgMaderaNoSuperaAlturaMampara = rootView.findViewById(R.id.rdgMaderaNoSuperaAlturaMamparaLeer);
        rdgMaderaNoSuperaParales = rootView.findViewById(R.id.rdgMaderaNoSuperaParalesLeer);
        rdgNoMaderaAtravieseMampara = rootView.findViewById(R.id.rdgMaderaAtravieseMamparaLeer);
        rdgParalesMismaAltura = rootView.findViewById(R.id.rdgParalesMismaAlturaLeer);
        rdgNingunaUndSobrepasaParales = rootView.findViewById(R.id.rdgNingunaUndSobrepasaParalesLeer);
        rdgCadaBancoAseguradoEslingas = rootView.findViewById(R.id.rdgCadaBancoAseguradoEslingasLeer);
        rdgCarroceriaParlesBuenEstado = rootView.findViewById(R.id.rdgCarroceriaParalesBuenEstadoLeer);
        rdgConductorSalioCinturon = rootView.findViewById(R.id.rdgConductorSalioCinturonLeer);
        rdgParalesAbatiblesAseguradosEstrobos = rootView.findViewById(R.id.rdgParalesAbatiblesAseguradosEstrobosLeer);

        int colorNegro = ContextCompat.getColor(getContext(), R.color.black);
        edtHoraSalida.setTextColor(colorNegro);

        if(isNetworkAvailable()){
            loadDataFirestore();
        }
        else{
            loadDataDBLocal(listIdLocal);
            Log.d("ID LEER ESTADO CARGUE","ID LEER ESTADO CARGUE : " + listIdLocal);
        }

        return rootView;
    }

    private void resRadioButton(){
        if(isNetworkAvailable()){
            maderaNoSuperaMamparaRes = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getMaderaNoSuperaMampara();
            maderaNoSuperaParalesRes = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getMaderaNoSuperaParales();
            NoMaderaAtraviesaMamparaRes = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getNoMaderaAtraviesaMampara();
            paralesMismaAlturaRes = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getParalesMismaAltura();
            ningunaUndSobrepasaParalesRes = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getNingunaUndSobrepasaParales();
            cadaBancoAseguradoEslingasRes = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getCadaBancoAseguradoEslingas();
            carroceriaParalesBuenEstadoRes = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getCarroceriaParalesBuenEstado();
            conductorSalioLugarCinturonRes = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getConductorSalioLugarCinturon();
            paralesAbatiblesAseguradosEstrobosRes = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getParalesAbatiblesAseguradosEstrobos();
        }
        else {
            maderaNoSuperaMamparaRes = listasCargueModel.getItem_4_Estado_cargue().getMaderaNoSuperaMampara();
            maderaNoSuperaParalesRes = listasCargueModel.getItem_4_Estado_cargue().getMaderaNoSuperaParales();
            NoMaderaAtraviesaMamparaRes = listasCargueModel.getItem_4_Estado_cargue().getNoMaderaAtraviesaMampara();
            paralesMismaAlturaRes = listasCargueModel.getItem_4_Estado_cargue().getParalesMismaAltura();
            ningunaUndSobrepasaParalesRes = listasCargueModel.getItem_4_Estado_cargue().getNingunaUndSobrepasaParales();
            cadaBancoAseguradoEslingasRes = listasCargueModel.getItem_4_Estado_cargue().getCadaBancoAseguradoEslingas();
            carroceriaParalesBuenEstadoRes = listasCargueModel.getItem_4_Estado_cargue().getCarroceriaParalesBuenEstado();
            conductorSalioLugarCinturonRes = listasCargueModel.getItem_4_Estado_cargue().getConductorSalioLugarCinturon();
            paralesAbatiblesAseguradosEstrobosRes = listasCargueModel.getItem_4_Estado_cargue().getParalesAbatiblesAseguradosEstrobos();
        }

        //Se chequea el valor de selección que tienen los radiobutton
        if ("Si".equals(maderaNoSuperaMamparaRes)) {
            rdgMaderaNoSuperaAlturaMampara.check(R.id.rbSiMaderaNoSuperaAlturaMamparaLeer);
        } else if ("No".equals(maderaNoSuperaMamparaRes)) {
            rdgMaderaNoSuperaAlturaMampara.check(R.id.rbNoMaderaNoSuperaAlturaMamparaLeer);
        }

        if ("Si".equals(maderaNoSuperaParalesRes)) {
            rdgMaderaNoSuperaParales.check(R.id.rbSiMaderaNoSuperaParalesLeer);
        } else if ("No".equals(maderaNoSuperaParalesRes)) {
            rdgMaderaNoSuperaParales.check(R.id.rbNoMaderaNoSuperaParalesLeer);
        }

        if ("Si".equals(NoMaderaAtraviesaMamparaRes)) {
            rdgNoMaderaAtravieseMampara.check(R.id.rbSiMaderaAtravieseMamparaLeer);
        } else if ("No".equals(NoMaderaAtraviesaMamparaRes)) {
            rdgNoMaderaAtravieseMampara.check(R.id.rbNoMaderaAtravieseMamparaLeer);
        }

        if ("Si".equals(paralesMismaAlturaRes)) {
            rdgParalesMismaAltura.check(R.id.rbSiParalesMismaAlturaLeer);
        } else if ("No".equals(paralesMismaAlturaRes)) {
            rdgParalesMismaAltura.check(R.id.rbNoParalesMismaAlturaLeer);
        }

        if ("Si".equals(ningunaUndSobrepasaParalesRes)) {
            rdgNingunaUndSobrepasaParales.check(R.id.rbSiNingunaUndSobrepasaParalesLeer);
        } else if ("No".equals(ningunaUndSobrepasaParalesRes)) {
            rdgNingunaUndSobrepasaParales.check(R.id.rbNoNingunaUndSobrepasaParalesLeer);
        }

        if ("Si".equals(cadaBancoAseguradoEslingasRes)) {
            rdgCadaBancoAseguradoEslingas.check(R.id.rbSiCadaBancoAseguradoEslingasLeer);
        } else if ("No".equals(cadaBancoAseguradoEslingasRes)) {
            rdgCadaBancoAseguradoEslingas.check(R.id.rbNoCadaBancoAseguradoEslingasLeer);
        }

        if ("Si".equals(carroceriaParalesBuenEstadoRes)) {
            rdgCarroceriaParlesBuenEstado.check(R.id.rbSiCarroceriaParalesBuenEstadoLeer);
        } else if ("No".equals(carroceriaParalesBuenEstadoRes)) {
            rdgCarroceriaParlesBuenEstado.check(R.id.rbNoCarroceriaParalesBuenEstadoLeer);
        }

        if ("Si".equals(conductorSalioLugarCinturonRes)) {
            rdgConductorSalioCinturon.check(R.id.rbSiConductorSalioCinturonLeer);
        } else if ("No".equals(conductorSalioLugarCinturonRes)) {
            rdgConductorSalioCinturon.check(R.id.rbNoConductorSalioCinturonLeer);
        }

        if ("Si".equals(paralesAbatiblesAseguradosEstrobosRes)) {
            rdgParalesAbatiblesAseguradosEstrobos.check(R.id.rbSiParalesAbatiblesAseguradosEstrobosLeer);
        } else if ("No".equals(paralesAbatiblesAseguradosEstrobosRes)) {
            rdgParalesAbatiblesAseguradosEstrobos.check(R.id.rbNoParalesAbatiblesAseguradosEstrobosLeer);
        }
    }

    private void loadDataFirestore() {
        edtHoraSalida.setText(AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getHoraSalida());
        String urlFotoCamion = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getFotoCamion();
        Log.d("EditarEstadoCargue", "URL de la imagen: " + urlFotoCamion);

        if (urlFotoCamion != null && !urlFotoCamion.equals("")) {
            Picasso.with(rootView.getContext())
                    .load(urlFotoCamion)
                    //.resize(400, 400)
                    .into(imgCapturaFoto);
        } else {
            imgCapturaFoto.setImageResource(R.drawable.icono_imagen_foto_camion);
            Toast.makeText(getContext(), "No se ha tomado ninguna foto", Toast.LENGTH_SHORT).show();
        }

        resRadioButton();
    }

    private void loadDataDBLocal(Integer listId) {
        listasCargueModel = dbLocal.getListById(listId);

        edtHoraSalida.setText(listasCargueModel.getItem_4_Estado_cargue().getHoraSalida());

        String urlFotoCamion = listasCargueModel.getItem_4_Estado_cargue().getFotoCamion();

        if (urlFotoCamion != null) {
            String trimmedBase64String = urlFotoCamion.replaceAll("\\s", "");
            if (!trimmedBase64String.equals("")) {
                byte[] decodedString = Base64.decode(trimmedBase64String, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgCapturaFoto.setImageBitmap(decodedByte);
            } else {
                imgCapturaFoto.setImageResource(R.drawable.icono_imagen_foto_camion);
                Toast.makeText(getContext(), "No se ha tomado ninguna foto", Toast.LENGTH_SHORT).show();
            }
            Log.d("TRIMMEDBASE64", "TRIMMEDBASE64 : " + trimmedBase64String);
        } else {
            imgCapturaFoto.setImageResource(R.drawable.icono_imagen_foto_camion);
            Toast.makeText(getContext(), "No se ha tomado ninguna foto", Toast.LENGTH_SHORT).show();
        }
        resRadioButton();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}