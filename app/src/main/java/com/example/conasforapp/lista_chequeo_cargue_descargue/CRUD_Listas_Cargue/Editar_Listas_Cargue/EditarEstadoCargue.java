package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Editar_Listas_Cargue;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Adaptadores.AdaptadorMisListasCargue;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.example.conasforapp.modelos.UsuariosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;

public class EditarEstadoCargue extends Fragment {
    private View view;
    private TextInputEditText edtHoraSalida;
    private ImageView imgCapturaFoto, imgCerrarDesplegableEstadoCargue;
    private Button btnActualizarDatosEstadoCargue, btnTomarFoto, btnSubirFotoCamionEditar;
    private RadioGroup rdgMaderaNoSuperaAlturaMampara,
            rdgMaderaNoSuperaParales, rdgNoMaderaAtravieseMampara,
            rdgParalesMismaAltura, rdgNingunaUndSobrepasaParales,
            rdgCadaBancoAseguradoEslingas, rdgCarroceriaParlesBuenEstado,
            rdgConductorSalioCinturon, rdgParalesAbatiblesAseguradosEstrobos;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private Map<String, Object> datosEstadoCargue = new HashMap<>();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private String pathLista = "Listas chequeo cargue descargue";
    private ListasCargueModel.EstadoDelCargue estadoCargueModel = new ListasCargueModel.EstadoDelCargue();
    String id_lista = AgregarMostrarListas.idPosicionLista;
    String nombre ="",cargo = "",finca="";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uid = currentUser.getUid();
    FrameLayout framePadreEstadoCargue,frameHijoEstadoCargue;
    String pathUsuarios = "Usuarios";
    private String pathFotoCamion;
    private static final int CODE_GALERY = 500;
    String base64Image = "";
    private int listId = -1;
    private ListasCargueDataBaseHelper dbLocal;
    private ListasCargueModel listasCargueModel = new ListasCargueModel();
    String idListaPos = null;
    AdaptadorMisListasCargue listasCargueDescargueRecycler;
    private String horaSalida;
    private static String foto = null;
    public EditarEstadoCargue() {
        // Required empty public constructor
    }
    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_editar_estado_cargue, container, false);

        edtHoraSalida = view.findViewById(R.id.edtHoraSalidaSitioEditar);
        imgCapturaFoto = view.findViewById(R.id.imgFotoCamionEditar);
        btnTomarFoto = view.findViewById(R.id.btnTomarFotoCamionEditar);
        btnSubirFotoCamionEditar = view.findViewById(R.id.btnSubirFotoCamionEditar);
        btnActualizarDatosEstadoCargue = view.findViewById(R.id.btnActualizarDatosEstadoCargueEditar);
        imgCerrarDesplegableEstadoCargue = view.findViewById(R.id.imgCerrarEstadoCargueEditar);
        framePadreEstadoCargue = view.findViewById(R.id.frameEstadoCargueEditar);
        frameHijoEstadoCargue = view.findViewById(R.id.frameEstadoCargueHijoEdit);

        db = FirebaseFirestore.getInstance();

        rdgMaderaNoSuperaAlturaMampara = view.findViewById(R.id.rdgMaderaNoSuperaAlturaMamparaEditar);
        rdgMaderaNoSuperaParales = view.findViewById(R.id.rdgMaderaNoSuperaParalesEditar);
        rdgNoMaderaAtravieseMampara = view.findViewById(R.id.rdgMaderaAtravieseMamparaEditar);
        rdgParalesMismaAltura = view.findViewById(R.id.rdgParalesMismaAlturaEditar);
        rdgNingunaUndSobrepasaParales = view.findViewById(R.id.rdgNingunaUndSobrepasaParalesEditar);
        rdgCadaBancoAseguradoEslingas = view.findViewById(R.id.rdgCadaBancoAseguradoEslingasEditar);
        rdgCarroceriaParlesBuenEstado = view.findViewById(R.id.rdgCarroceriaParalesBuenEstadoEditar);
        rdgConductorSalioCinturon = view.findViewById(R.id.rdgConductorSalioCinturonEditar);
        rdgParalesAbatiblesAseguradosEstrobos = view.findViewById(R.id.rdgParalesAbatiblesAseguradosEstrobosEditar);

        btnActualizarDatosEstadoCargue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actualizarDatosEstadoCargue();
            }
        });

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (EasyPermissions.hasPermissions(getContext(), android.Manifest.permission.CAMERA)) {
                    Intent open_camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(open_camara, 100);
                } else {
                    EasyPermissions.requestPermissions(
                            (Activity) getContext(),
                            "Esta aplicación necesita permisos de cámara para tomar fotos.",
                            REQUEST_CAMERA_PERMISSION,
                            android.Manifest.permission.CAMERA
                    );
                }
            }
        });

        btnSubirFotoCamionEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFoto();
            }
        });

        edtHoraSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirHora(v);
            }
        });

        Log.d("TAG ID EN EDIT ESTADO CARGUE","ID EN EDIT ESTADO CARGUE : " + id_lista);

        /*
        // Obtener el intent que inició esta actividad
        Intent intent = getActivity().getIntent();
        Boolean valorEstado = intent.getBooleanExtra("estado clic",true);
        Log.d("TAG ESTADO CLIC", "ESTADO CLIC : " + valorEstado);

         */

        /*
        imgCerrarDesplegableEstadoCargue.setOnClickListener(new View.OnClickListener() {
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
            }
            dbLocal = new ListasCargueDataBaseHelper(getContext());
            loadDataDBLocal(listId);
        }
        else {
            //loadData();
            obtenerLista();

            // Obtener los argumentos del Bundle
            Bundle args = getArguments();
            if (args != null) {
                idListaPos = args.getString("idPos");
                Log.d("ID LISTA POS EN INFO CONDUCTOR","ID LISTA POS EN INFO CONDUCTOR : "+idListaPos);

            }
        }
        //obtenerDatosUsuario();
        //loadDataFirebase();
        return view;
    }

    public static EditarEstadoCargue newInstance(String idLista) {
        Bundle args = new Bundle();
        args.putString("idPos", idLista);
        EditarEstadoCargue fragment = new EditarEstadoCargue();
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

        edtHoraSalida.setText(listasCargueModel.getItem_4_Estado_cargue().getHoraSalida());
        String maderaNoSuperaMamparaRes = listasCargueModel.getItem_4_Estado_cargue().getMaderaNoSuperaMampara();
        String maderaNoSuperaParalesRes = listasCargueModel.getItem_4_Estado_cargue().getMaderaNoSuperaParales();
        String NoMaderaAtraviesaMamparaRes = listasCargueModel.getItem_4_Estado_cargue().getNoMaderaAtraviesaMampara();
        String paralesMismaAlturaRes = listasCargueModel.getItem_4_Estado_cargue().getParalesMismaAltura();
        String ningunaUndSobrepasaParalesRes = listasCargueModel.getItem_4_Estado_cargue().getNingunaUndSobrepasaParales();
        String cadaBancoAseguradoEslingasRes = listasCargueModel.getItem_4_Estado_cargue().getCadaBancoAseguradoEslingas();
        String carroceriaParalesBuenEstadoRes = listasCargueModel.getItem_4_Estado_cargue().getCarroceriaParalesBuenEstado();
        String conductorSalioLugarCinturonRes = listasCargueModel.getItem_4_Estado_cargue().getConductorSalioLugarCinturon();
        String paralesAbatiblesAseguradosEstrobosRes = listasCargueModel.getItem_4_Estado_cargue().getParalesAbatiblesAseguradosEstrobos();

        //Se chequea el valor de selección que tienen los radiobutton
        if ("Si".equals(maderaNoSuperaMamparaRes)) {
            rdgMaderaNoSuperaAlturaMampara.check(R.id.rbSiMaderaNoSuperaAlturaMamparaEditar);
        }
        else if ("No".equals(maderaNoSuperaMamparaRes)) {
            rdgMaderaNoSuperaAlturaMampara.check(R.id.rbNoMaderaNoSuperaAlturaMamparaEditar);
        }

        if ("Si".equals(maderaNoSuperaParalesRes)) {
            rdgMaderaNoSuperaParales.check(R.id.rbSiMaderaNoSuperaParalesEditar);
        }
        else if ("No".equals(maderaNoSuperaParalesRes)) {
            rdgMaderaNoSuperaParales.check(R.id.rbNoMaderaNoSuperaParalesEditar);
        }

        if ("Si".equals(NoMaderaAtraviesaMamparaRes)) {
            rdgNoMaderaAtravieseMampara.check(R.id.rbSiMaderaAtravieseMamparaEditar);

        }
        else if ("No".equals(NoMaderaAtraviesaMamparaRes )) {
            rdgNoMaderaAtravieseMampara.check(R.id.rbNoMaderaAtravieseMamparaEditar);
        }

        if ("Si".equals(paralesMismaAlturaRes)) {
            rdgParalesMismaAltura.check(R.id.rbSiParalesMismaAlturaEditar);

        }
        else if ("No".equals(paralesMismaAlturaRes)) {
            rdgParalesMismaAltura.check(R.id.rbNoParalesMismaAlturaEditar);
        }

        if ("Si".equals(ningunaUndSobrepasaParalesRes)) {
            rdgNingunaUndSobrepasaParales.check(R.id.rbSiNingunaUndSobrepasaParalesEditar);
        }
        else if ("No".equals(ningunaUndSobrepasaParalesRes)) {
            rdgNingunaUndSobrepasaParales.check(R.id.rbNoNingunaUndSobrepasaParalesEditar);
        }

        if ("Si".equals(cadaBancoAseguradoEslingasRes)) {
            rdgCadaBancoAseguradoEslingas.check(R.id.rbSiCadaBancoAseguradoEslingasEditar);
        }
        else if ("No".equals(cadaBancoAseguradoEslingasRes)) {
            rdgCadaBancoAseguradoEslingas.check(R.id.rbNoCadaBancoAseguradoEslingasEditar);
        }

        if ("Si".equals(carroceriaParalesBuenEstadoRes)) {
            rdgCarroceriaParlesBuenEstado.check(R.id.rbSiCarroceriaParalesBuenEstadoEditar);
        }
        else if ("No".equals(carroceriaParalesBuenEstadoRes)) {
            rdgCarroceriaParlesBuenEstado.check(R.id.rbNoCarroceriaParalesBuenEstadoEditar);
        }

        if ("Si".equals(conductorSalioLugarCinturonRes)) {
            rdgConductorSalioCinturon.check(R.id.rbSiConductorSalioCinturonEditar);
        }
        else if ("No".equals(conductorSalioLugarCinturonRes)) {
            rdgConductorSalioCinturon.check(R.id.rbNoConductorSalioCinturonEditar);
        }

        if ("Si".equals(paralesAbatiblesAseguradosEstrobosRes)) {
            rdgParalesAbatiblesAseguradosEstrobos.check(R.id.rbSiParalesAbatiblesAseguradosEstrobosEditar);
        }
        else if ("No".equals(paralesAbatiblesAseguradosEstrobosRes)) {
            rdgParalesAbatiblesAseguradosEstrobos.check(R.id.rbNoParalesAbatiblesAseguradosEstrobosEditar);
        }

        String urlFotoCamion = listasCargueModel.getItem_4_Estado_cargue().getFotoCamion();

        if(urlFotoCamion!= null){
            String trimmedBase64String = urlFotoCamion.replaceAll("\\s", "");
            if(!trimmedBase64String.equals("")){
                byte[] decodedString = Base64.decode(trimmedBase64String, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgCapturaFoto.setImageBitmap(decodedByte);
            }
            else {
                imgCapturaFoto.setImageResource(R.drawable.icono_imagen_foto_camion);
                Toast.makeText(getContext(), "No se ha tomado ninguna foto", Toast.LENGTH_SHORT).show();
            }
            Log.d("TRIMMEDBASE64","TRIMMEDBASE64 : " + trimmedBase64String);
        }
        else {
            imgCapturaFoto.setImageResource(R.drawable.icono_imagen_foto_camion);
            Toast.makeText(getContext(), "No se ha tomado ninguna foto", Toast.LENGTH_SHORT).show();
        }

        /*
        int colorVerdeOscuro = ContextCompat.getColor(getContext(), R.color.verde_oscuro);
        int colorBlanco = ContextCompat.getColor(getContext(),R.color.white);

        Date fechaActualDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_actual_date");
        Date fechaListaDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_lista_date");

        if(cargo.equals("Administrador") || (fechaActualDate != null && fechaListaDate != null && !fechaActualDate.equals(fechaListaDate)) && !cargo.equals("Administrador")){
            RadioButton rbSiMaderaNoSuperaAlturaMamparaEditar = view.findViewById(R.id.rbSiMaderaNoSuperaAlturaMamparaEditar);
            RadioButton rbNoMaderaNoSuperaAlturaMamparaEditar = view.findViewById(R.id.rbNoMaderaNoSuperaAlturaMamparaEditar);
            RadioButton rbSiMaderaNoSuperaParalesEditar =  view.findViewById(R.id.rbSiMaderaNoSuperaParalesEditar);
            RadioButton rbNoMaderaNoSuperaParalesEditar = view.findViewById(R.id.rbNoMaderaNoSuperaParalesEditar);
            RadioButton rbSiMaderaAtravieseMamparaEditar =  view.findViewById(R.id.rbSiMaderaAtravieseMamparaEditar);
            RadioButton rbNoMaderaAtravieseMamparaEditar = view.findViewById(R.id.rbNoMaderaAtravieseMamparaEditar);
            RadioButton rbSiParalesMismaAlturaEditar = view.findViewById(R.id.rbSiParalesMismaAlturaEditar);
            RadioButton rbNoParalesMismaAlturaEditar = view.findViewById(R.id.rbNoParalesMismaAlturaEditar);
            RadioButton rbSiNingunaUndSobrepasaParalesEditar = view.findViewById(R.id.rbSiNingunaUndSobrepasaParalesEditar);
            RadioButton rbNoNingunaUndSobrepasaParalesEditar = view.findViewById(R.id.rbNoNingunaUndSobrepasaParalesEditar);
            RadioButton rbSiCadaBancoAseguradoEslingasEditar = view.findViewById(R.id.rbSiCadaBancoAseguradoEslingasEditar);
            RadioButton rbNoCadaBancoAseguradoEslingasEditar = view.findViewById(R.id.rbNoCadaBancoAseguradoEslingasEditar);
            RadioButton rbSiCarroceriaParalesBuenEstadoEditar = view.findViewById(R.id.rbSiCarroceriaParalesBuenEstadoEditar);
            RadioButton rbNoCarroceriaParalesBuenEstadoEditar = view.findViewById(R.id.rbNoCarroceriaParalesBuenEstadoEditar);
            RadioButton rbSiConductorSalioCinturonEditar = view.findViewById(R.id.rbSiConductorSalioCinturonEditar);
            RadioButton rbNoConductorSalioCinturonEditar = view.findViewById(R.id.rbNoConductorSalioCinturonEditar);
            RadioButton rbSiParalesAbatiblesAseguradosEstrobosEditar = view.findViewById(R.id.rbSiParalesAbatiblesAseguradosEstrobosEditar);
            RadioButton rbNoParalesAbatiblesAseguradosEstrobosEditar = view.findViewById(R.id.rbNoParalesAbatiblesAseguradosEstrobosEditar);

            rbSiMaderaNoSuperaAlturaMamparaEditar.setEnabled(false);
            rbNoMaderaNoSuperaAlturaMamparaEditar.setEnabled(false);
            rbSiMaderaNoSuperaParalesEditar.setEnabled(false);
            rbNoMaderaNoSuperaParalesEditar.setEnabled(false);
            rbSiMaderaAtravieseMamparaEditar.setEnabled(false);
            rbNoMaderaAtravieseMamparaEditar.setEnabled(false);
            rbSiParalesMismaAlturaEditar.setEnabled(false);
            rbNoParalesMismaAlturaEditar.setEnabled(false);
            rbSiNingunaUndSobrepasaParalesEditar.setEnabled(false);
            rbNoNingunaUndSobrepasaParalesEditar.setEnabled(false);
            rbSiCadaBancoAseguradoEslingasEditar.setEnabled(false);
            rbNoCadaBancoAseguradoEslingasEditar.setEnabled(false);
            rbSiCarroceriaParalesBuenEstadoEditar.setEnabled(false);
            rbNoCarroceriaParalesBuenEstadoEditar.setEnabled(false);
            rbSiConductorSalioCinturonEditar.setEnabled(false);
            rbNoConductorSalioCinturonEditar.setEnabled(false);
            rbSiParalesAbatiblesAseguradosEstrobosEditar.setEnabled(false);
            rbNoParalesAbatiblesAseguradosEstrobosEditar.setEnabled(false);

            btnTomarFoto.setVisibility(View.GONE);
            btnSubirFotoCamionEditar.setVisibility(View.GONE);

            imgCerrarDesplegableEstadoCargue.setVisibility(View.GONE);
            framePadreEstadoCargue.setBackgroundColor(colorVerdeOscuro);
            frameHijoEstadoCargue.setBackgroundColor(colorBlanco);
        }

         */
    }


    /*
    private void loadDataFirebase(){
        String cargo = AgregarMostrarListas.cargo;

        edtHoraSalida.setText(AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getHoraSalida());
        String maderaNoSuperaMamparaRes = AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getMaderaNoSuperaMampara();
        String maderaNoSuperaParalesRes = AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getMaderaNoSuperaParales();
        String NoMaderaAtraviesaMamparaRes = AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getNoMaderaAtraviesaMampara();
        String paralesMismaAlturaRes = AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getParalesMismaAltura();
        String ningunaUndSobrepasaParalesRes = AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getNingunaUndSobrepasaParales();
        String cadaBancoAseguradoEslingasRes = AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getCadaBancoAseguradoEslingas();
        String carroceriaParalesBuenEstadoRes = AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getCarroceriaParalesBuenEstado();
        String conductorSalioLugarCinturonRes = AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getConductorSalioLugarCinturon();
        String paralesAbatiblesAseguradosEstrobosRes = AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getParalesAbatiblesAseguradosEstrobos();

        //Se chequea el valor de selección que tienen los radiobutton
        if ("Si".equals(maderaNoSuperaMamparaRes)) {
            rdgMaderaNoSuperaAlturaMampara.check(R.id.rbSiMaderaNoSuperaAlturaMamparaEditar);
        }
        else if ("No".equals(maderaNoSuperaMamparaRes)) {
            rdgMaderaNoSuperaAlturaMampara.check(R.id.rbNoMaderaNoSuperaAlturaMamparaEditar);
        }

        if ("Si".equals(maderaNoSuperaParalesRes)) {
            rdgMaderaNoSuperaParales.check(R.id.rbSiMaderaNoSuperaParalesEditar);
        }
        else if ("No".equals(maderaNoSuperaParalesRes)) {
            rdgMaderaNoSuperaParales.check(R.id.rbNoMaderaNoSuperaParalesEditar);
        }

        if ("Si".equals(NoMaderaAtraviesaMamparaRes)) {
            rdgNoMaderaAtravieseMampara.check(R.id.rbSiMaderaAtravieseMamparaEditar);
        }
        else if ("No".equals(NoMaderaAtraviesaMamparaRes )) {
            rdgNoMaderaAtravieseMampara.check(R.id.rbNoMaderaAtravieseMamparaEditar);
        }

        if ("Si".equals(paralesMismaAlturaRes)) {
            rdgParalesMismaAltura.check(R.id.rbSiParalesMismaAlturaEditar);
        }
        else if ("No".equals(paralesMismaAlturaRes)) {
            rdgParalesMismaAltura.check(R.id.rbNoParalesMismaAlturaEditar);
        }

        if ("Si".equals(ningunaUndSobrepasaParalesRes)) {
            rdgNingunaUndSobrepasaParales.check(R.id.rbSiNingunaUndSobrepasaParalesEditar);
        }
        else if ("No".equals(ningunaUndSobrepasaParalesRes)) {
            rdgNingunaUndSobrepasaParales.check(R.id.rbNoNingunaUndSobrepasaParalesEditar);
        }

        if ("Si".equals(cadaBancoAseguradoEslingasRes)) {
            rdgCadaBancoAseguradoEslingas.check(R.id.rbSiCadaBancoAseguradoEslingasEditar);
        }
        else if ("No".equals(cadaBancoAseguradoEslingasRes)) {
            rdgCadaBancoAseguradoEslingas.check(R.id.rbNoCadaBancoAseguradoEslingasEditar);
        }

        if ("Si".equals(carroceriaParalesBuenEstadoRes)) {
            rdgCarroceriaParlesBuenEstado.check(R.id.rbSiCarroceriaParalesBuenEstadoEditar);
        }
        else if ("No".equals(carroceriaParalesBuenEstadoRes)) {
            rdgCarroceriaParlesBuenEstado.check(R.id.rbNoCarroceriaParalesBuenEstadoEditar);
        }

        if ("Si".equals(conductorSalioLugarCinturonRes)) {
            rdgConductorSalioCinturon.check(R.id.rbSiConductorSalioCinturonEditar);
        }
        else if ("No".equals(conductorSalioLugarCinturonRes)) {
            rdgConductorSalioCinturon.check(R.id.rbNoConductorSalioCinturonEditar);
        }

        if ("Si".equals(paralesAbatiblesAseguradosEstrobosRes)) {
            rdgParalesAbatiblesAseguradosEstrobos.check(R.id.rbSiParalesAbatiblesAseguradosEstrobosEditar);
        }
        else if ("No".equals(paralesAbatiblesAseguradosEstrobosRes)) {
            rdgParalesAbatiblesAseguradosEstrobos.check(R.id.rbNoParalesAbatiblesAseguradosEstrobosEditar);
        }

        //obtenerImagenDeStorageYAgregarAColeccion();

        String urlFotoCamion = AgregarMostrarListas.listaModel.getItem_4_Estado_cargue().getFotoCamion();
        Log.d("EditarEstadoCargue", "URL de la imagen: " + urlFotoCamion);

        if (urlFotoCamion != null && !urlFotoCamion.equals("")){
            Picasso.with(view.getContext())
                    .load(urlFotoCamion)
                    //.resize(400, 400)
                    .into(imgCapturaFoto);
        }


        else {
            imgCapturaFoto.setImageResource(R.drawable.icono_imagen_foto_camion);
            Toast.makeText(getContext(), "No se ha tomado ninguna foto", Toast.LENGTH_SHORT).show();
        }



        int colorVerdeOscuro = ContextCompat.getColor(getContext(), R.color.verde_oscuro);
        int colorBlanco = ContextCompat.getColor(getContext(), R.color.white);

        Date fechaActualDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_actual_date");
        Date fechaListaDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_lista_date");

        Log.d("CARGO","CARGO : " + cargo);

        if (("Administrador".equals(cargo) || (fechaActualDate != null && fechaListaDate != null && !fechaActualDate.equals(fechaListaDate))) ) {
            edtHoraSalida.setEnabled(false);
            edtHoraSalida.setTextColor(colorVerdeOscuro);

            RadioButton maderaNoSuperaAlturaMamparaSi = view.findViewById(R.id.rbSiMaderaNoSuperaAlturaMamparaEditar);
            RadioButton maderaNoSuperaParalesSi = view.findViewById(R.id.rbSiMaderaNoSuperaParalesEditar);
            RadioButton maderaNoAtraviezaMamparaSi = view.findViewById(R.id.rbSiMaderaAtravieseMamparaEditar);
            RadioButton paralesMismaAlturaSi = view.findViewById(R.id.rbSiParalesMismaAlturaEditar);
            RadioButton ningunaUndSobrepasaParalesSi = view.findViewById(R.id.rbSiNingunaUndSobrepasaParalesEditar);
            RadioButton cadaBancoAseguradoEslingasSi = view.findViewById(R.id.rbSiCadaBancoAseguradoEslingasEditar);
            RadioButton carroceriaParalesBuenEstadoSi = view.findViewById(R.id.rbSiCarroceriaParalesBuenEstadoEditar);
            RadioButton conductorSalioCinturonSi = view.findViewById(R.id.rbSiConductorSalioCinturonEditar);
            RadioButton paralesAseguradosEstrobosSi = view.findViewById(R.id.rbSiParalesAbatiblesAseguradosEstrobosEditar);

            RadioButton maderaNoSuperaAlturaMamparaNo = view.findViewById(R.id.rbNoMaderaNoSuperaAlturaMamparaEditar);
            RadioButton maderaNoSuperaParalesNo = view.findViewById(R.id.rbNoMaderaNoSuperaParalesEditar);
            RadioButton maderaNoAtraviezaMamparaNo = view.findViewById(R.id.rbNoMaderaAtravieseMamparaEditar);
            RadioButton paralesMismaAlturaNo = view.findViewById(R.id.rbNoParalesMismaAlturaEditar);
            RadioButton ningunaUndSobrepasaParalesNo = view.findViewById(R.id.rbNoNingunaUndSobrepasaParalesEditar);
            RadioButton cadaBancoAseguradoEslingasNo = view.findViewById(R.id.rbNoCadaBancoAseguradoEslingasEditar);
            RadioButton carroceriaParalesBuenEstadoNo = view.findViewById(R.id.rbNoCarroceriaParalesBuenEstadoEditar);
            RadioButton conductorSalioCinturonNo = view.findViewById(R.id.rbNoConductorSalioCinturonEditar);
            RadioButton paralesAseguradosEstrobosNo = view.findViewById(R.id.rbNoParalesAbatiblesAseguradosEstrobosEditar);

            maderaNoSuperaAlturaMamparaSi.setEnabled(false);
            maderaNoSuperaAlturaMamparaNo.setEnabled(false);
            maderaNoSuperaParalesSi.setEnabled(false);
            maderaNoSuperaParalesNo.setEnabled(false);
            maderaNoAtraviezaMamparaSi.setEnabled(false);
            maderaNoAtraviezaMamparaNo.setEnabled(false);
            paralesMismaAlturaSi.setEnabled(false);
            paralesMismaAlturaNo.setEnabled(false);
            ningunaUndSobrepasaParalesSi.setEnabled(false);
            ningunaUndSobrepasaParalesNo.setEnabled(false);
            cadaBancoAseguradoEslingasSi.setEnabled(false);
            cadaBancoAseguradoEslingasNo.setEnabled(false);
            carroceriaParalesBuenEstadoSi.setEnabled(false);
            carroceriaParalesBuenEstadoNo.setEnabled(false);
            conductorSalioCinturonSi.setEnabled(false);
            conductorSalioCinturonNo.setEnabled(false);
            paralesAseguradosEstrobosSi.setEnabled(false);
            paralesAseguradosEstrobosNo.setEnabled(false);

            btnTomarFoto.setVisibility(View.GONE);
            btnActualizarDatosEstadoCargue.setVisibility(View.GONE);
            btnSubirFotoCamionEditar.setVisibility(View.GONE);
            imgCerrarDesplegableEstadoCargue.setVisibility(View.GONE);
            framePadreEstadoCargue.setBackgroundColor(colorVerdeOscuro);
            frameHijoEstadoCargue.setBackgroundColor(colorBlanco);
        }

    }
     */
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

                        edtHoraSalida.setText(listasCargueModel.getItem_4_Estado_cargue().getHoraSalida());
                        String maderaNoSuperaMamparaRes = listasCargueModel.getItem_4_Estado_cargue().getMaderaNoSuperaMampara();
                        String maderaNoSuperaParalesRes = listasCargueModel.getItem_4_Estado_cargue().getMaderaNoSuperaParales();
                        String NoMaderaAtraviesaMamparaRes = listasCargueModel.getItem_4_Estado_cargue().getNoMaderaAtraviesaMampara();
                        String paralesMismaAlturaRes = listasCargueModel.getItem_4_Estado_cargue().getParalesMismaAltura();
                        String ningunaUndSobrepasaParalesRes = listasCargueModel.getItem_4_Estado_cargue().getNingunaUndSobrepasaParales();
                        String cadaBancoAseguradoEslingasRes = listasCargueModel.getItem_4_Estado_cargue().getCadaBancoAseguradoEslingas();
                        String carroceriaParalesBuenEstadoRes = listasCargueModel.getItem_4_Estado_cargue().getCarroceriaParalesBuenEstado();
                        String conductorSalioLugarCinturonRes = listasCargueModel.getItem_4_Estado_cargue().getConductorSalioLugarCinturon();
                        String paralesAbatiblesAseguradosEstrobosRes = listasCargueModel.getItem_4_Estado_cargue().getParalesAbatiblesAseguradosEstrobos();

                        //Se chequea el valor de selecci&oacute;n que tienen los radiobutton
                        if ("Si".equals(maderaNoSuperaMamparaRes)) {
                            rdgMaderaNoSuperaAlturaMampara.check(R.id.rbSiMaderaNoSuperaAlturaMamparaEditar);
                        }
                        else if ("No".equals(maderaNoSuperaMamparaRes)) {
                            rdgMaderaNoSuperaAlturaMampara.check(R.id.rbNoMaderaNoSuperaAlturaMamparaEditar);
                        }

                        if ("Si".equals(maderaNoSuperaParalesRes)) {
                            rdgMaderaNoSuperaParales.check(R.id.rbSiMaderaNoSuperaParalesEditar);
                        }
                        else if ("No".equals(maderaNoSuperaParalesRes)) {
                            rdgMaderaNoSuperaParales.check(R.id.rbNoMaderaNoSuperaParalesEditar);
                        }

                        if ("Si".equals(NoMaderaAtraviesaMamparaRes)) {
                            rdgNoMaderaAtravieseMampara.check(R.id.rbSiMaderaAtravieseMamparaEditar);
                        }
                        else if ("No".equals(NoMaderaAtraviesaMamparaRes )) {
                            rdgNoMaderaAtravieseMampara.check(R.id.rbNoMaderaAtravieseMamparaEditar);
                        }

                        if ("Si".equals(paralesMismaAlturaRes)) {
                            rdgParalesMismaAltura.check(R.id.rbSiParalesMismaAlturaEditar);
                        }
                        else if ("No".equals(paralesMismaAlturaRes)) {
                            rdgParalesMismaAltura.check(R.id.rbNoParalesMismaAlturaEditar);
                        }

                        if ("Si".equals(ningunaUndSobrepasaParalesRes)) {
                            rdgNingunaUndSobrepasaParales.check(R.id.rbSiNingunaUndSobrepasaParalesEditar);
                        }
                        else if ("No".equals(ningunaUndSobrepasaParalesRes)) {
                            rdgNingunaUndSobrepasaParales.check(R.id.rbNoNingunaUndSobrepasaParalesEditar);
                        }

                        if ("Si".equals(cadaBancoAseguradoEslingasRes)) {
                            rdgCadaBancoAseguradoEslingas.check(R.id.rbSiCadaBancoAseguradoEslingasEditar);
                        }
                        else if ("No".equals(cadaBancoAseguradoEslingasRes)) {
                            rdgCadaBancoAseguradoEslingas.check(R.id.rbNoCadaBancoAseguradoEslingasEditar);
                        }

                        if ("Si".equals(carroceriaParalesBuenEstadoRes)) {
                            rdgCarroceriaParlesBuenEstado.check(R.id.rbSiCarroceriaParalesBuenEstadoEditar);
                        }
                        else if ("No".equals(carroceriaParalesBuenEstadoRes)) {
                            rdgCarroceriaParlesBuenEstado.check(R.id.rbNoCarroceriaParalesBuenEstadoEditar);
                        }

                        if ("Si".equals(conductorSalioLugarCinturonRes)) {
                            rdgConductorSalioCinturon.check(R.id.rbSiConductorSalioCinturonEditar);
                        }
                        else if ("No".equals(conductorSalioLugarCinturonRes)) {
                            rdgConductorSalioCinturon.check(R.id.rbNoConductorSalioCinturonEditar);
                        }

                        if ("Si".equals(paralesAbatiblesAseguradosEstrobosRes)) {
                            rdgParalesAbatiblesAseguradosEstrobos.check(R.id.rbSiParalesAbatiblesAseguradosEstrobosEditar);
                        }
                        else if ("No".equals(paralesAbatiblesAseguradosEstrobosRes)) {
                            rdgParalesAbatiblesAseguradosEstrobos.check(R.id.rbNoParalesAbatiblesAseguradosEstrobosEditar);
                        }

                        String urlFotoCamion = listasCargueModel.getItem_4_Estado_cargue().getFotoCamion();
                        Log.d("EditarEstadoCargue", "URL de la imagen: " + urlFotoCamion);

                        if (urlFotoCamion != null && !urlFotoCamion.equals("")){
                            Picasso.with(view.getContext())
                                    .load(urlFotoCamion)
                                    //.resize(400, 400)
                                    .into(imgCapturaFoto);
                        }
                        else {
                            imgCapturaFoto.setImageResource(R.drawable.icono_imagen_foto_camion);
                            Toast.makeText(getContext(), "No se ha tomado ninguna foto", Toast.LENGTH_SHORT).show();
                        }

                        /*
                        int colorVerdeOscuro = ContextCompat.getColor(getContext(), R.color.verde_oscuro);
                        int colorBlanco = ContextCompat.getColor(getContext(), R.color.white);

                        Date fechaActualDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_actual_date");
                        Date fechaListaDate = (Date) getActivity().getIntent().getSerializableExtra("fecha_lista_date");

                        Log.d("CARGO","CARGO : " + cargo);

                        if (("Administrador".equals(cargo) || (fechaActualDate != null && fechaListaDate != null && !fechaActualDate.equals(fechaListaDate))) ) {
                            edtHoraSalida.setEnabled(false);
                            edtHoraSalida.setTextColor(colorVerdeOscuro);

                            RadioButton maderaNoSuperaAlturaMamparaSi = view.findViewById(R.id.rbSiMaderaNoSuperaAlturaMamparaEditar);
                            RadioButton maderaNoSuperaParalesSi = view.findViewById(R.id.rbSiMaderaNoSuperaParalesEditar);
                            RadioButton maderaNoAtraviezaMamparaSi = view.findViewById(R.id.rbSiMaderaAtravieseMamparaEditar);
                            RadioButton paralesMismaAlturaSi = view.findViewById(R.id.rbSiParalesMismaAlturaEditar);
                            RadioButton ningunaUndSobrepasaParalesSi = view.findViewById(R.id.rbSiNingunaUndSobrepasaParalesEditar);
                            RadioButton cadaBancoAseguradoEslingasSi = view.findViewById(R.id.rbSiCadaBancoAseguradoEslingasEditar);
                            RadioButton carroceriaParalesBuenEstadoSi = view.findViewById(R.id.rbSiCarroceriaParalesBuenEstadoEditar);
                            RadioButton conductorSalioCinturonSi = view.findViewById(R.id.rbSiConductorSalioCinturonEditar);
                            RadioButton paralesAseguradosEstrobosSi = view.findViewById(R.id.rbSiParalesAbatiblesAseguradosEstrobosEditar);

                            RadioButton maderaNoSuperaAlturaMamparaNo = view.findViewById(R.id.rbNoMaderaNoSuperaAlturaMamparaEditar);
                            RadioButton maderaNoSuperaParalesNo = view.findViewById(R.id.rbNoMaderaNoSuperaParalesEditar);
                            RadioButton maderaNoAtraviezaMamparaNo = view.findViewById(R.id.rbNoMaderaAtravieseMamparaEditar);
                            RadioButton paralesMismaAlturaNo = view.findViewById(R.id.rbNoParalesMismaAlturaEditar);
                            RadioButton ningunaUndSobrepasaParalesNo = view.findViewById(R.id.rbNoNingunaUndSobrepasaParalesEditar);
                            RadioButton cadaBancoAseguradoEslingasNo = view.findViewById(R.id.rbNoCadaBancoAseguradoEslingasEditar);
                            RadioButton carroceriaParalesBuenEstadoNo = view.findViewById(R.id.rbNoCarroceriaParalesBuenEstadoEditar);
                            RadioButton conductorSalioCinturonNo = view.findViewById(R.id.rbNoConductorSalioCinturonEditar);
                            RadioButton paralesAseguradosEstrobosNo = view.findViewById(R.id.rbNoParalesAbatiblesAseguradosEstrobosEditar);

                            maderaNoSuperaAlturaMamparaSi.setEnabled(false);
                            maderaNoSuperaAlturaMamparaNo.setEnabled(false);
                            maderaNoSuperaParalesSi.setEnabled(false);
                            maderaNoSuperaParalesNo.setEnabled(false);
                            maderaNoAtraviezaMamparaSi.setEnabled(false);
                            maderaNoAtraviezaMamparaNo.setEnabled(false);
                            paralesMismaAlturaSi.setEnabled(false);
                            paralesMismaAlturaNo.setEnabled(false);
                            ningunaUndSobrepasaParalesSi.setEnabled(false);
                            ningunaUndSobrepasaParalesNo.setEnabled(false);
                            cadaBancoAseguradoEslingasSi.setEnabled(false);
                            cadaBancoAseguradoEslingasNo.setEnabled(false);
                            carroceriaParalesBuenEstadoSi.setEnabled(false);
                            carroceriaParalesBuenEstadoNo.setEnabled(false);
                            conductorSalioCinturonSi.setEnabled(false);
                            conductorSalioCinturonNo.setEnabled(false);
                            paralesAseguradosEstrobosSi.setEnabled(false);
                            paralesAseguradosEstrobosNo.setEnabled(false);

                            btnTomarFoto.setVisibility(View.GONE);
                            btnActualizarDatosEstadoCargue.setVisibility(View.GONE);
                            btnSubirFotoCamionEditar.setVisibility(View.GONE);
                            imgCerrarDesplegableEstadoCargue.setVisibility(View.GONE);
                            framePadreEstadoCargue.setBackgroundColor(colorVerdeOscuro);
                            frameHijoEstadoCargue.setBackgroundColor(colorBlanco);
                        }

                         */
                    }
                }
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void actualizarDatosEstadoCargue(){
        horaSalida = edtHoraSalida.getText().toString();

        int rdgMaderaNoSuperaAlturaMamparaId = rdgMaderaNoSuperaAlturaMampara.getCheckedRadioButtonId();
        int rdgMaderaNoSuperaParalesId = rdgMaderaNoSuperaParales.getCheckedRadioButtonId();
        int rdgNoMaderaAtravieseMamparaId = rdgNoMaderaAtravieseMampara.getCheckedRadioButtonId();
        int rdgParalesMismaAlturaId = rdgParalesMismaAltura.getCheckedRadioButtonId();
        int rdgNingunaUndSobrepasaParalesId = rdgNingunaUndSobrepasaParales.getCheckedRadioButtonId();
        int rdgCadaBancoAseguradoEslingasId = rdgCadaBancoAseguradoEslingas.getCheckedRadioButtonId();
        int rdgCarroceriaParlesBuenEstadoId = rdgCarroceriaParlesBuenEstado.getCheckedRadioButtonId();
        int rdgConductorSalioCinturonId = rdgConductorSalioCinturon.getCheckedRadioButtonId();
        int rdgParalesAbatiblesAseguradosEstrobosId = rdgParalesAbatiblesAseguradosEstrobos.getCheckedRadioButtonId();

        // Verificar qu&eacute; RadioButton fue seleccionado y actualizar el modelo
        if (rdgMaderaNoSuperaAlturaMamparaId == R.id.rbSiMaderaNoSuperaAlturaMamparaEditar) {
            estadoCargueModel.setMaderaNoSuperaMampara("Si");
        } else if (rdgMaderaNoSuperaAlturaMamparaId == R.id.rbNoMaderaNoSuperaAlturaMamparaEditar) {
            estadoCargueModel.setMaderaNoSuperaMampara("No");
        }

        if (rdgMaderaNoSuperaParalesId == R.id.rbSiMaderaNoSuperaParalesEditar) {
            estadoCargueModel.setMaderaNoSuperaParales("Si");
        } else if (rdgMaderaNoSuperaParalesId == R.id.rbNoMaderaNoSuperaParalesEditar) {
            estadoCargueModel.setMaderaNoSuperaParales("No");
        }

        if (rdgNoMaderaAtravieseMamparaId == R.id.rbSiMaderaAtravieseMamparaEditar) {
            estadoCargueModel.setNoMaderaAtraviesaMampara("Si");
        } else if (rdgNoMaderaAtravieseMamparaId == R.id.rbNoMaderaAtravieseMamparaEditar) {
            estadoCargueModel.setNoMaderaAtraviesaMampara("No");
        }

        if (rdgParalesMismaAlturaId == R.id.rbSiParalesMismaAlturaEditar) {
            estadoCargueModel.setParalesMismaAltura("Si");
        } else if (rdgParalesMismaAlturaId == R.id.rbNoParalesMismaAlturaEditar) {
            estadoCargueModel.setParalesMismaAltura("No");
        }

        if (rdgNingunaUndSobrepasaParalesId == R.id.rbSiNingunaUndSobrepasaParalesEditar) {
            estadoCargueModel.setNingunaUndSobrepasaParales("Si");
        } else if (rdgNingunaUndSobrepasaParalesId == R.id.rbNoNingunaUndSobrepasaParalesEditar) {
            estadoCargueModel.setNingunaUndSobrepasaParales("No");
        }

        if (rdgCadaBancoAseguradoEslingasId == R.id.rbSiCadaBancoAseguradoEslingasEditar) {
            estadoCargueModel.setCadaBancoAseguradoEslingas("Si");
        } else if (rdgCadaBancoAseguradoEslingasId == R.id.rbNoCadaBancoAseguradoEslingasEditar) {
            estadoCargueModel.setCadaBancoAseguradoEslingas("No");
        }

        if (rdgCarroceriaParlesBuenEstadoId == R.id.rbSiCarroceriaParalesBuenEstadoEditar) {
            estadoCargueModel.setCarroceriaParalesBuenEstado("Si");
        } else if (rdgCarroceriaParlesBuenEstadoId == R.id.rbNoCarroceriaParalesBuenEstadoEditar) {
            estadoCargueModel.setCarroceriaParalesBuenEstado("No");
        }

        if (rdgConductorSalioCinturonId == R.id.rbSiConductorSalioCinturonEditar) {
            estadoCargueModel.setConductorSalioLugarCinturon("Si");
        } else if (rdgConductorSalioCinturonId  == R.id.rbNoConductorSalioCinturonEditar) {
            estadoCargueModel.setConductorSalioLugarCinturon("No");
        }

        if (rdgParalesAbatiblesAseguradosEstrobosId == R.id.rbSiParalesAbatiblesAseguradosEstrobosEditar) {
            estadoCargueModel.setParalesAbatiblesAseguradosEstrobos("Si");
        } else if (rdgParalesAbatiblesAseguradosEstrobosId  == R.id.rbNoParalesAbatiblesAseguradosEstrobosEditar) {
            estadoCargueModel.setParalesAbatiblesAseguradosEstrobos("No");
        }

        Log.d("FOTO STATIC","FOTO STATIC : "+ foto);

        String respuestMaderaNoSuperaAlturaMampara = estadoCargueModel.getMaderaNoSuperaMampara();
        String respuestaMaderaNoSuperaParales = estadoCargueModel.getMaderaNoSuperaParales();
        String respuestaNoMaderaAtravieseMampara = estadoCargueModel.getNoMaderaAtraviesaMampara();
        String respuestaParalesMismaAltura = estadoCargueModel.getParalesMismaAltura();
        String respuestaParalesNingunaUndSobrepasaParales = estadoCargueModel.getNingunaUndSobrepasaParales();
        String respuestaCadaBancoAseguradoEslingas = estadoCargueModel.getCadaBancoAseguradoEslingas();
        String respuestaCarroceriaParlesBuenEstado = estadoCargueModel.getCarroceriaParalesBuenEstado();
        String respuestaConductorSalioCinturon = estadoCargueModel.getConductorSalioLugarCinturon();
        String respuestaParalesAbatiblesAseguradosEstrobos = estadoCargueModel.getParalesAbatiblesAseguradosEstrobos();

        datosEstadoCargue.put("horaSalida",horaSalida);
        datosEstadoCargue.put("fotoCamion", foto);
        datosEstadoCargue.put("maderaNoSuperaMampara",respuestMaderaNoSuperaAlturaMampara);
        datosEstadoCargue.put("maderaNoSuperaParales",respuestaMaderaNoSuperaParales);
        datosEstadoCargue.put("noMaderaAtraviesaMampara",respuestaNoMaderaAtravieseMampara);
        datosEstadoCargue.put("paralesMismaAltura",respuestaParalesMismaAltura);
        datosEstadoCargue.put("ningunaUndSobrepasaParales",respuestaParalesNingunaUndSobrepasaParales);
        datosEstadoCargue.put("cadaBancoAseguradoEslingas",respuestaCadaBancoAseguradoEslingas);
        datosEstadoCargue.put("carroceriaParalesBuenEstado",respuestaCarroceriaParlesBuenEstado);
        datosEstadoCargue.put("conductorSalioLugarCinturon",respuestaConductorSalioCinturon);
        datosEstadoCargue.put("paralesAbatiblesAseguradosEstrobos",respuestaParalesAbatiblesAseguradosEstrobos);

        Log.d("Datos Info Conductor ","Datos Info Conductor : " + datosEstadoCargue);
        Log.d("TAG ID LISTA EN INFO CONDUCTOR","ID LISTA EN INFO CONDUCTOR : "+ id_lista);

        if(isNetworkAvailable()){
            actualizarEstadoLista();
            //if(id_lista != null){
            if(idListaPos != null){
                db.collection(pathLista)
                        .document(idListaPos)
                        .update("Item_4_Estado_cargue", datosEstadoCargue).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "No actualizaron los datos", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }else {
            actualizarEstadoLista();
            int listId = getArguments().getInt("list_id", -1);
            Log.d("ID LISTA", "ID LISTA : " + listId);

            //Toast.makeText(requireContext(), "ID: " + listId, Toast.LENGTH_SHORT).show();
            if (listId == -1) {
                Toast.makeText(getContext(), "ID no v&aacute;lido", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            } else {
                dbLocal = new ListasCargueDataBaseHelper(requireContext());
                listasCargueModel = dbLocal.getListById(listId);
                Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
            }

            if (listasCargueModel != null) {
                listasCargueModel.getItem_4_Estado_cargue().setHoraSalida(edtHoraSalida.getText().toString());
                listasCargueModel.getItem_4_Estado_cargue().setMaderaNoSuperaMampara(respuestMaderaNoSuperaAlturaMampara);
                listasCargueModel.getItem_4_Estado_cargue().setMaderaNoSuperaParales(respuestaMaderaNoSuperaParales);
                listasCargueModel.getItem_4_Estado_cargue().setNoMaderaAtraviesaMampara(respuestaNoMaderaAtravieseMampara);
                listasCargueModel.getItem_4_Estado_cargue().setParalesMismaAltura(respuestaParalesMismaAltura);
                listasCargueModel.getItem_4_Estado_cargue().setNingunaUndSobrepasaParales(respuestaParalesNingunaUndSobrepasaParales);
                listasCargueModel.getItem_4_Estado_cargue().setCadaBancoAseguradoEslingas(respuestaCadaBancoAseguradoEslingas);
                listasCargueModel.getItem_4_Estado_cargue().setCarroceriaParalesBuenEstado(respuestaCarroceriaParlesBuenEstado);
                listasCargueModel.getItem_4_Estado_cargue().setConductorSalioLugarCinturon(respuestaConductorSalioCinturon);
                listasCargueModel.getItem_4_Estado_cargue().setParalesAbatiblesAseguradosEstrobos(respuestaParalesAbatiblesAseguradosEstrobos);

                dbLocal.updateList(listasCargueModel);
            }
        }
    }
    private void obtenerDatosUsuario(){
        if (currentUser != null) {
            uid = currentUser.getUid(); // El usuario est&aacute; autenticado
            db.collection(pathUsuarios).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UsuariosModel usuariosModel = document.toObject(UsuariosModel.class);
                            nombre = usuariosModel.getNombre();
                            cargo = usuariosModel.getCargo();
                            finca = usuariosModel.getFinca();
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void cargarFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK); //MediaStore.Images.Media.EXTERNAL_CONTENT_URI   ---
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicaci&oacute;n"),CODE_GALERY);
    }

    private String convertBitmapToBase64(Bitmap bitmap) { // M&eacute;todo para convertir Bitmap a base64
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    //Se obtiene la imgaen al tomar la foto
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                imgCapturaFoto.setImageBitmap(photo);
                base64Image = convertBitmapToBase64(photo); // Convierte la imagen a base64 (puedes usar tu propia l&oacute;gica)

                if(isNetworkAvailable()){
                    uploadImageToStorage(base64Image); // Sube la imagen a Firestore
                }
                else {
                    updateImageToDBLocal(base64Image); // Sube la imagen a la base de datos local
                }
                Toast.makeText(getContext(), "Se obtuvo la imagen correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No se pudo obtener la imagen de la c&aacute;mara", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == CODE_GALERY && resultCode == RESULT_OK && data != null){

            Calendar calHoy = Calendar.getInstance();
            calHoy.set(Calendar.HOUR_OF_DAY, 0);
            calHoy.set(Calendar.MINUTE, 0);
            calHoy.set(Calendar.SECOND, 0);
            calHoy.set(Calendar.MILLISECOND, 0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(calHoy.getTime());

            Uri urlImagen = data.getData();
            imgCapturaFoto.setImageURI(urlImagen);

            pathFotoCamion = "foto_camion_" + nombre + "_" + cargo + "_" + formattedDate + "_" + "ID_Usuario__" +uid + "_" +"ID_Lista__"+ idListaPos + "_" + ".jpg";
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imagenRef = storageRef.child("Fotos Camion Cargue Descargue/" + formattedDate + "/" + finca + "/" + pathFotoCamion);  // Referencia al archivo en el Firebase Storage
            //storageRef = FirebaseStorage.getInstance().getReference().child("Fotos Camion Cargue Descargue/").child(pathFotoCamion);
            imagenRef.putFile(urlImagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful());
                    if(uriTask.isSuccessful()){
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String downloadUri = uri.toString();

                                foto = downloadUri;
                                //datosEstadoCargue.put("fotoCamion",downloadUri);
                                Log.d("TAG FOTO PERFIL","FOTO PERFIL : " + downloadUri);
                                //Toast.makeText(getContext(), "Imagen cargada", Toast.LENGTH_SHORT).show();
                                //uploadImageUrlToFirestore(downloadUri);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //Toast.makeText(getContext(), "Fall&oacute; al subir imagen", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Ocurri&oacute; un error inesperado", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateImageToDBLocal(String base64Image){
        int listId = getArguments().getInt("list_id", -1);
        dbLocal = new ListasCargueDataBaseHelper(requireContext());

        if (listId == -1) {
            requireActivity().finish();
        }else {
            dbLocal = new ListasCargueDataBaseHelper(requireContext());
            listasCargueModel = dbLocal.getListById(listId);
            Toast.makeText(getContext(), "Se obtuvo la imagen correctamente", Toast.LENGTH_SHORT).show();
        }
        listasCargueModel = dbLocal.getListById(listId);
        listasCargueModel.getItem_4_Estado_cargue().setFotoCamion(base64Image);
        dbLocal.updateList(listasCargueModel);
    }

    private void uploadImageToStorage(String base64Image) {
        if (pathFotoCamion == null) {
            Calendar calHoy = Calendar.getInstance();
            calHoy.set(Calendar.HOUR_OF_DAY, 0);
            calHoy.set(Calendar.MINUTE, 0);
            calHoy.set(Calendar.SECOND, 0);
            calHoy.set(Calendar.MILLISECOND, 0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(calHoy.getTime());

            pathFotoCamion = "foto_camión_" + nombre + "_" + cargo + "_" + formattedDate + "_" + "ID_Usuario__" +uid + "_" +"ID_Lista__"+ idListaPos + "_" + ".jpg";
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);// Convierte la cadena Base64 de nuevo a datos binarios
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imagenRef = storageRef.child("Fotos Camion Cargue Descargue/" + formattedDate + "/" + finca + "/" + pathFotoCamion); // Referencia al archivo en el Firebase Storage

            // Sube los datos binarios a Firebase Storage
            //storageRef = FirebaseStorage.getInstance().getReference().child("Fotos Camion Cargue Descargue/").child(pathFotoCamion);
            UploadTask uploadTask = imagenRef.putBytes(decodedBytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Obtiene la URL de la imagen almacenada en Firebase Storage
                    imagenRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            foto = downloadUri.toString();
                            // Almacena la URL en Firestore
                            //uploadImageUrlToFirestore(downloadUri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error al subir la imagen a Firebase Storage", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void uploadImageUrlToFirestore(String imageUrl) {
        DocumentReference docRef = db.collection(pathLista).document(idListaPos);
        datosEstadoCargue.put("fotoCamion", imageUrl);
        datosEstadoCargue.put("horaSalida", horaSalida);
        docRef.update("Item_4_Estado_cargue",datosEstadoCargue).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Imagen subida a Firestore correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void abrirHora(View view){
        Calendar horaCalendario = Calendar.getInstance();

        int hora = horaCalendario.get(Calendar.HOUR_OF_DAY);
        int min = horaCalendario.get(Calendar.MINUTE);
        TimePickerDialog tmd = new TimePickerDialog(getContext(),R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String formatoHora;
                if(hourOfDay >=12)
                {
                    formatoHora = "pm";
                }
                else {
                    formatoHora = "am";
                }
                // Convertir la hora al formato de 12 horas
                int hour = hourOfDay % 12;
                if (hour == 0) {
                    hour = 12;
                }
                // Actualizar el texto en edtHora
                edtHoraSalida.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, formatoHora));
            }
        }, hora,min,false);
        tmd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        tmd.show();
    }

    private void showMessage(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }

    private boolean verificarCampos() {

        if (TextUtils.isEmpty(edtHoraSalida.getText())) {
            showMessage("Debes ingresar la hora de salida del veh&iacute;culo");
            return false;
        }

        if (rdgMaderaNoSuperaAlturaMampara.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opci&oacute;n en La madera no supera la altura de la mampara");
            return false;
        }

        if (rdgMaderaNoSuperaParales.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opci&oacute;n en La madera no supera ninguno de los parales");
            return false;
        }

        if (rdgNoMaderaAtravieseMampara.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opci&oacute;n en No hay madera que atraviese la mampara");
            return false;
        }

        if (rdgParalesMismaAltura.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opci&oacute;n en Los parales tienen la misma altura");
            return false;
        }
        if (rdgNingunaUndSobrepasaParales.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opci&oacute;n en Ninguna de las unidades de madera sobrepasa lateralmente los parales");
            return false;
        }

        if (rdgCadaBancoAseguradoEslingas.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opci&oacute;n en Cada banco est&aacute; asegurado con 2 eslingas");
            return false;
        }
        if (rdgCarroceriaParlesBuenEstado.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opci&oacute;n en La carrocer&iacute;a y los parales est&aacute;n en buen estado y sin signos de golpes");
            return false;
        }
        if (rdgConductorSalioCinturon.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opci&oacute;n en EL conductor sali&oacute; del lugar utilizando el cintur&oacute;n");
            return false;
        }
        if (rdgParalesAbatiblesAseguradosEstrobos.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opci&oacute;n en Los parales abatibles se encuentran asegurados con estrobos");
            return false;
        }

        if(imgCapturaFoto == null){
            showMessage("Debes tomar o subir la foto del cami&oacute;n");
            return false;
        }
        return true;
    }

    public interface OnEstadoCamposEditarEstadoCargue {
        void onEstadoCamposEditarEstadoCargue(boolean camposCompletos);
    }

    private OnEstadoCamposEditarEstadoCargue listener4;

    private void actualizarEstadoLista() {
        boolean camposCompletos = verificarCampos();

        if (listener4 != null) {
            listener4.onEstadoCamposEditarEstadoCargue(camposCompletos);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnEstadoCamposEditarEstadoCargue) {
            listener4 = (OnEstadoCamposEditarEstadoCargue) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEstadoCamposEstadoCargueListener");
        }
    }
}