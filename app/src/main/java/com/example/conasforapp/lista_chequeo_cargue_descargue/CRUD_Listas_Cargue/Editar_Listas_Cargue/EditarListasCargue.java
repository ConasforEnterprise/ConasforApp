package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Editar_Listas_Cargue;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.MisListasCargue;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class EditarListasCargue extends AppCompatActivity implements
        EditarInfoLugarCargue.OnEstadoCamposEditarInfoLugarCargueListener,
        EditarInfoConductor.OnEstadoEditarInfoConductor,
        EditarInfoVehiculo.OnEstadoCamposEditarInfoVehiculo,
        EditarEstadoCargue.OnEstadoCamposEditarEstadoCargue,
        EditarFirmasCargue.OnEstadoCamposEditarFirmas
        {
    private Fragment lista_item1 ,lista_item2,lista_item3, lista_item4,lista_item5;
    private TextView txtIdLista, txtFechaEditar;
    private LinearLayout lLayoutIDEditar;
    private FrameLayout bottom_sheet_deplegable_Editar;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private FloatingActionButton fabItem1, fabItem2, fabItem3, fabItem4, fabItem5;
    Button btnGuardarDatosEditados;
    private String pathLista = "Listas chequeo cargue descargue";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idLista = null;
    private ListasCargueModel listasCargueModel = new ListasCargueModel();
    private ListasCargueDataBaseHelper dbLocal;
    private Button btnRegresar;
    private LottieAnimationView lottieAnimationButtonEdit;
    private TextView txtListaEditada;
    private CardView cardListaEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_listas_cargue);

        txtIdLista = findViewById(R.id.txtIdListaEditarListas);
        txtFechaEditar = findViewById(R.id.txtFechaEditar);
        btnRegresar = findViewById(R.id.btnRegresarEditar);
        lLayoutIDEditar = findViewById(R.id.lLayoutIDListaEditarCargue);

        //Asignación de los Fragmentos de cada item de la lista de Cargue y Descargue
        lista_item1 = new EditarInfoLugarCargue();
        lista_item2 = new EditarInfoConductor();
        lista_item3 = new EditarInfoVehiculo();
        lista_item4 = new EditarEstadoCargue();
        lista_item5 = new EditarFirmasCargue();

        //Floating Action Button para desplegar los ítems a llenar
        fabItem1 = findViewById(R.id.fabItem1CargueDescargueEditar);
        fabItem2 = findViewById(R.id.fabItem2CargueDescargueEditar);
        fabItem3 = findViewById(R.id.fabItem3CargueDescargueEditar);
        fabItem4 = findViewById(R.id.fabItem4CargueDescargueEditar);
        fabItem5 = findViewById(R.id.fabItem5CargueDescargueEditar);

        //BackDrop o FrameLayout que se despliega cuando se da clic en algún item de la lista
        bottom_sheet_deplegable_Editar = findViewById(R.id.bottomSheetPadreEditar);

        lottieAnimationButtonEdit = findViewById(R.id.lottieAnimationButtonEdit);
        txtListaEditada = findViewById(R.id.txtlistaEditada);
        cardListaEditar = findViewById(R.id.cardListaEditar);
        btnGuardarDatosEditados = findViewById(R.id.btnGuardarDatosCargueDescargueEditar);

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_deplegable_Editar);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        btnGuardarDatosEditados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFragmentManager().popBackStack();
                        finish();
                        Animatoo.INSTANCE.animateSlideRight(EditarListasCargue.this);
                    }
                },1000);

                lottieAnimationButtonEdit.setVisibility(View.VISIBLE);
                lottieAnimationButtonEdit.playAnimation();
                txtListaEditada.setVisibility(View.VISIBLE);
                cardListaEditar.setVisibility(View.VISIBLE);
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAtras();
            }
        });


        fabItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    infoLugarCargue();
                }
                else {
                    Intent intent = getIntent();
                    int listId = intent.getIntExtra("list_id", -1);

                    if (listId == -1) {
                        Toast.makeText(EditarListasCargue.this, "ID no válido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    infoLugarCargueBDLocal(String.valueOf(listId));
                    toggleBottomSheet();
                }
            }
        });
        fabItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    infoConductor();
                }
                else {
                    Intent intent = getIntent();
                    int listId = intent.getIntExtra("list_id", -1);

                    if (listId == -1) {
                        Toast.makeText(EditarListasCargue.this, "ID no válido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    infoConductorBDLocal(String.valueOf(listId));
                    toggleBottomSheet();
                }
            }
        });
        fabItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    infoVehiculo();
                }
                else {
                    Intent intent = getIntent();
                    int listId = intent.getIntExtra("list_id", -1);

                    if (listId == -1) {
                        Toast.makeText(EditarListasCargue.this, "ID no válido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    infoVehiculoBDLocal(String.valueOf(listId));
                    toggleBottomSheet();
                }
            }
        });
        fabItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    estadoCargue();
                }
                else {
                    Intent intent = getIntent();
                    int listId = intent.getIntExtra("list_id", -1);

                    if (listId == -1) {
                        Toast.makeText(EditarListasCargue.this, "ID no válido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    estadoCargueBDLocal(String.valueOf(listId));
                    toggleBottomSheet();
                }
            }
        });
        fabItem5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    firmasNombres();
                }
                else {
                    Intent intent = getIntent();
                    int listId = intent.getIntExtra("list_id", -1);
                    Log.d("ID LISTA", "ID LISTA : " + listId);

                    if (listId == -1) {
                        Toast.makeText(EditarListasCargue.this, "ID no válido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    firmasNombresBDLocal(String.valueOf(listId));
                    toggleBottomSheet();
                }
            }
        });

        dbLocal = new ListasCargueDataBaseHelper(this);

        if(isNetworkAvailable()) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                idLista = extras.getString("idPos");

                lista_item1 = EditarInfoLugarCargue.newInstance(idLista);
                lista_item2 = EditarInfoConductor.newInstance(idLista);
                lista_item3 = EditarInfoVehiculo.newInstance(idLista);
                lista_item4 = EditarEstadoCargue.newInstance(idLista);
                lista_item5 = EditarFirmasCargue.newInstance(idLista);
            }
            obtenerListaFirestore();
            txtIdLista.setText(idLista);
            txtFechaEditar.setText(AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getFecha());
        }

        else
        {
            Intent intent = getIntent();
            int listId = intent.getIntExtra("list_id", -1);

            listasCargueModel = dbLocal.getListById(listId);
            txtFechaEditar.setText(listasCargueModel.getItem_1_Informacion_lugar_cargue().getFecha());
            lLayoutIDEditar.setVisibility(View.GONE);

            obtenerListaBDLocal(listId);

            if (listId == -1) {
                Toast.makeText(EditarListasCargue.this, "ID no válido", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                volverAtras();
            }
        };
        EditarListasCargue.this.getOnBackPressedDispatcher().addCallback(EditarListasCargue.this, callback);
    }

    private void procesarLista(ListasCargueModel listasCargueModel) {
        ListasCargueModel.InfoLugarCargue informacionLugarCargue = listasCargueModel.getItem_1_Informacion_lugar_cargue();
        ListasCargueModel.InfoDelConductor informacionConductor = listasCargueModel.getItem_2_Informacion_del_conductor();
        ListasCargueModel.InfoDelVehiculo informacionVehiculo = listasCargueModel.getItem_3_Informacion_vehiculo();
        ListasCargueModel.EstadoDelCargue estadoCargue = listasCargueModel.getItem_4_Estado_cargue();
        String nombreFirma1 = listasCargueModel.getNombre_Supervisor();
        String nombreFirma2 = listasCargueModel.getNombre_Despachador();
        String nombreFirma3 = listasCargueModel.getNombre_Conductor();
        String nombreFirma4 = listasCargueModel.getNombre_Operador();
        String firma1 = listasCargueModel.getFirma_Supervisor();
        String firma2 = listasCargueModel.getFirma_Despachador();
        String firma3 = listasCargueModel.getFirma_Conductor();
        String firma4 = listasCargueModel.getFirma_Operador();

        // Verificar campos de información del lugar de carga
        if (informacionLugarCargue.getFecha() != null &&
                !informacionLugarCargue.getFecha().isEmpty() &&
                (informacionLugarCargue.getHoraEntrada() != null &&
                        !informacionLugarCargue.getHoraEntrada().isEmpty() &&
                        !informacionLugarCargue.getTipoCargue().isEmpty() &&
                        !informacionLugarCargue.getNombreZona().isEmpty() &&
                        !informacionLugarCargue.getNombreNucleo().isEmpty() &&
                        !informacionLugarCargue.getNombreFinca().isEmpty())){
            fabItem1.setImageResource(R.drawable.baseline_check_24);
        }
        else{
            fabItem1.setImageResource(R.drawable.icono_num_uno_listas);
        }

        Log.d("nombre conductor en editar","nombre conductor en editar" + informacionConductor.getNombreConductor().toString());

        // Verificar campos de información del conductor
        if (!informacionConductor.getNombreConductor().isEmpty() &&
                !informacionConductor.getCedula().isEmpty() &&
                !informacionConductor.getLugarExpedicion().isEmpty() &&
                !informacionConductor.getLicConduccionRes().isEmpty() &&
                !informacionConductor.getEpsRes().isEmpty() &&
                !informacionConductor.getCualEPS().isEmpty() &&
                !informacionConductor.getArlRes().isEmpty() &&
                !informacionConductor.getCualARL().isEmpty() &&
                !informacionConductor.getAfpRes().isEmpty() &&
                !informacionConductor.getCualAFP().isEmpty()){
            fabItem2.setImageResource(R.drawable.baseline_check_24);
        }
        else{
            fabItem2.setImageResource(R.drawable.icono_num_dos_listas);
        }

        // Verificar campos de información del vehículo
        if (!informacionVehiculo.getPlaca().isEmpty() &&
                !informacionVehiculo.getVehiculo().isEmpty() &&
                !informacionVehiculo.getTarjetaPropiedad().isEmpty() &&
                !informacionVehiculo.getSoatVigente().isEmpty() &&
                !informacionVehiculo.getRevisionTecnicomecanica().isEmpty() &&
                !informacionVehiculo.getLucesAltas().isEmpty() &&
                !informacionVehiculo.getLucesBajas().isEmpty() &&
                !informacionVehiculo.getDireccionales().isEmpty() &&
                !informacionVehiculo.getSonidoReversa().isEmpty() &&
                !informacionVehiculo.getReversa().isEmpty() &&
                !informacionVehiculo.getStop().isEmpty() &&
                !informacionVehiculo.getRetrovisores().isEmpty() &&
                !informacionVehiculo.getPlumillas().isEmpty() &&
                !informacionVehiculo.getEstadoPanoramicos().isEmpty() &&
                !informacionVehiculo.getEspejos().isEmpty() &&
                !informacionVehiculo.getBocina().isEmpty() &&
                !informacionVehiculo.getCinturon().isEmpty() &&
                !informacionVehiculo.getFreno().isEmpty() &&
                !informacionVehiculo.getLlantas().isEmpty() &&
                !informacionVehiculo.getBotiquin().isEmpty() &&
                !informacionVehiculo.getExtintorABC().isEmpty() &&
                !informacionVehiculo.getBotas().isEmpty() &&
                !informacionVehiculo.getChaleco().isEmpty() &&
                !informacionVehiculo.getCasco().isEmpty() &&
                !informacionVehiculo.getCarroceria().isEmpty() &&
                !informacionVehiculo.getDosEslingasBanco().isEmpty()&&
                !informacionVehiculo.getDosConosReflectivos().isEmpty() &&
                !informacionVehiculo.getParales().isEmpty()){
            fabItem3.setImageResource(R.drawable.baseline_check_24);
        }
        else{
            fabItem3.setImageResource(R.drawable.icono_num_tres_listas);
        }

        // Verificar campos Estado del cargue
        if (estadoCargue.getHoraSalida() != null && !estadoCargue.getHoraSalida().isEmpty() &&
                estadoCargue.getFotoCamion() != null && !estadoCargue.getFotoCamion().isEmpty() &&
                !estadoCargue.getMaderaNoSuperaMampara().isEmpty() &&
                !estadoCargue.getMaderaNoSuperaParales().isEmpty() &&
                !estadoCargue.getNoMaderaAtraviesaMampara().isEmpty() &&
                !estadoCargue.getParalesMismaAltura().isEmpty() &&
                !estadoCargue.getNingunaUndSobrepasaParales().isEmpty() &&
                !estadoCargue.getCadaBancoAseguradoEslingas().isEmpty() &&
                !estadoCargue.getConductorSalioLugarCinturon().isEmpty() &&
                !estadoCargue.getParalesAbatiblesAseguradosEstrobos().isEmpty()){
            fabItem4.setImageResource(R.drawable.baseline_check_24);
        }

        else{
            fabItem4.setImageResource(R.drawable.icono_num_cuatro_listas);
        }

        // Verificar campos Firmas y Nombres de Firmas
        if (firma1 != null && !firma1.isEmpty() &&
                firma2 != null && !firma2.isEmpty() &&
                firma3 != null && !firma3.isEmpty() &&
                firma4 != null && !firma4.isEmpty() &&
                nombreFirma1 != null && !nombreFirma1.isEmpty() &&
                nombreFirma2 != null && !nombreFirma2.isEmpty() &&
                nombreFirma3 != null && !nombreFirma3.isEmpty() &&
                nombreFirma4 != null && !nombreFirma4.isEmpty()){
            fabItem5.setImageResource(R.drawable.baseline_check_24);
        }

        else{
            fabItem5.setImageResource(R.drawable.icono_num_cinco_listas);
        }
    }
    private void obtenerListaFirestore(){
        String idPosLis = MisListasCargue.idPosicionLista;
            db.collection(pathLista).document(idPosLis).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
                            procesarLista(listasCargueModel);
                        }
                    }
                }
            });
    }
    private void obtenerListaBDLocal(Integer listId){
        listasCargueModel = dbLocal.getListById(listId);
        procesarLista(listasCargueModel);
    }

    private void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    //--------------------------------------- CARGAR FRAGMENTO SIN CONEXION A INTERNET ---------------------------------//
    private void loadFragmentWithoutConnextion(Fragment fragment,Integer IdList) {
        Bundle bundle = new Bundle();
        bundle.putInt("list_id", IdList);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottomSheetPadreEditar, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void infoLugarCargueBDLocal(String idLista) {
        loadFragmentWithoutConnextion(lista_item1, Integer.valueOf(idLista));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        toggleBottomSheet();

    }

    private void infoConductorBDLocal(String idLista) {
        loadFragmentWithoutConnextion(lista_item2, Integer.valueOf(idLista));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        toggleBottomSheet();

    }

    private void infoVehiculoBDLocal(String idLista) {
        loadFragmentWithoutConnextion(lista_item3, Integer.valueOf(idLista));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        toggleBottomSheet();

    }

    private void estadoCargueBDLocal(String idLista) {
        loadFragmentWithoutConnextion(lista_item4, Integer.valueOf(idLista));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        toggleBottomSheet();

    }
    private void firmasNombresBDLocal(String idLista) {
        loadFragmentWithoutConnextion(lista_item5, Integer.valueOf(idLista));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        toggleBottomSheet();

    }
    //--------------------------------------- FIN CARGAR FRAGMENTO SIN CONEXION A INTERNET -----------------------------//



    //------------------------------------ CARGAR FRAGMENTO CON CONEXION A INTERNET ------------------------------------//

    private void loadFragmentWithConnection(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottomSheetPadreEditar, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void infoLugarCargue() {
        Toast.makeText(EditarListasCargue.this, "Presionó Información del lugar donde se cargará", Toast.LENGTH_SHORT).show();
        loadFragmentWithConnection(lista_item1);
        toggleBottomSheet();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void infoConductor() {
        Toast.makeText(EditarListasCargue.this, "Presionó información del conductor", Toast.LENGTH_SHORT).show();
        loadFragmentWithConnection(lista_item2);
        toggleBottomSheet();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }
    private void infoVehiculo() {
        Toast.makeText(EditarListasCargue.this, "Presionó información del vehículo", Toast.LENGTH_SHORT).show();
        loadFragmentWithConnection(lista_item3);
        toggleBottomSheet();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void estadoCargue() {
        Toast.makeText(EditarListasCargue.this, "Presionó estado del cargue", Toast.LENGTH_SHORT).show();
        loadFragmentWithConnection(lista_item4);
        toggleBottomSheet();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void firmasNombres() {
        Toast.makeText(EditarListasCargue.this, "Presionó firmas y nombre", Toast.LENGTH_SHORT).show();
        loadFragmentWithConnection(lista_item5);
        toggleBottomSheet();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    //---------------------------------- FIN CARGAR FRAGMENTO CON CONEXION A INTERNET ----------------------------------//


    //----------------------------------------- ESTADO ITEM EDITAR LISTA ---------------------------------------------//
    public void onEstadoEditarCamposInfoLugarCargue(boolean camposCompletos) {
                if (camposCompletos) {
                    fabItem1.setImageResource(R.drawable.baseline_check_24);
                } else {
                    fabItem1.setImageResource(R.drawable.icono_num_uno_listas);
                }
            }
    public void onEstadoEditarInfoConductor(boolean camposCompletos) {
                if (camposCompletos) {
                    fabItem2.setImageResource(R.drawable.baseline_check_24);
                }
                else {
                    fabItem2.setImageResource(R.drawable.icono_num_dos_listas);
                }
            }
    public void onEstadoCamposEditarInfoVehiculo(boolean camposCompletos) {
                if (camposCompletos) {
                    fabItem3.setImageResource(R.drawable.baseline_check_24);
                }
                else {
                    fabItem3.setImageResource(R.drawable.icono_num_tres_listas);
                }
            }
    public void onEstadoCamposEditarEstadoCargue(boolean camposCompletos) {
                if (camposCompletos) {
                    fabItem4.setImageResource(R.drawable.baseline_check_24);
                }
                else {
                    fabItem4.setImageResource(R.drawable.icono_num_cuatro_listas);
                }
            }
    public void onEstadoCamposEditarFirmas(boolean camposCompletos) {
                if (camposCompletos) {
                    fabItem5.setImageResource(R.drawable.baseline_check_24);
                }
                else {
                    fabItem5.setImageResource(R.drawable.icono_num_cinco_listas);
                }
            }

    //----------------------------------------- FIN ESTADO ITEM EDITAR LISTA ------------------------------------------//

    //---------------------------------------- MÉTODO CONEXIÓN A INTERNET ---------------------------------------------//
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    //---------------------------------------- FIN MÉTODO CONEXIÓN A INTERNET -----------------------------------------//

    private void volverAtras(){
        finish();
        Animatoo.INSTANCE.animateSlideRight(EditarListasCargue.this);
        clearBackStack();
    }

    private void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}

