package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Llenar_Listas_Cargue;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LlenarListasCargue extends AppCompatActivity implements
        InfoLugarCargueFragment.DatosListener,
        InfoConductorFragment.DatosListener2,
        InfoVehiculoFragment.DatosListener3,
        EstadoCargueFragment.DatosListener4,
        FirmasFragment.DatosListener5,
        InfoLugarCargueFragment.OnEstadoCamposActualizadoListener,
        InfoConductorFragment.OnEstadoCamposActualizadoListener2,
        InfoVehiculoFragment.OnEstadoCamposActualizadoListener3,
        EstadoCargueFragment.OnEstadoCamposActualizadoListener4,
        FirmasFragment.OnEstadoCamposActualizadoListener5{
    private FrameLayout bottom_sheet_deplegable;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private Fragment lista_item1 ,lista_item2,lista_item3, lista_item4,lista_item5;
    private TextView txtEncabezadoBSBListas, txtNumItem;
    private TextView txtInfoLugarCargue, txtInfoConductor, txtInfoVehiculo, txtEstadoCargue, txtFirmasNombres;
    private TextView txtIdLista;
    private LinearLayout lLayoutIDLlenar;
    private String idLista;
    private FloatingActionButton fabItem1, fabItem2, fabItem3, fabItem4, fabItem5;
    private ImageView imgNumItem;
    Button btnRegresar,btnGuardarDatosCargueDescargue;
    private String pathLista = "Listas chequeo cargue descargue";
    private String idRecibido,horaRecibido, fechaRecibido, tipoCargueRecibido, zonaRecibido, nucleoRecibido,fincaRecibido;
    private String nombreConductorRecibido,cedulaRecibido,lugarExpedicionRecibido,licConduccionRecibido,polizaRCERecibido,epsResRecibido,arlResRecibido,afpResRecibido,cualEpsRecibido, cualArlRecibido,cualAfpRecibido;
    private String placaRecibido,vehiculoRecibido,tarjetaPropiedadRecibido,soatRecibido,revisionTecnicomecanicaRecibido,
            lucesAltasRecibido, lucesBajasRecibido,direccionalesRecibido,sonidoReversaRecibido, reversaRecibido, stopRecibido, retrovisoresRecibido, plumillasRecibido,
            estadoPanoramicosRecibido,espejosRecibido,bocinaRecibido,cinturonRecibido,frenoRecibido,llantasRecibido,botiquinRecibido, extintorABCRecibido,botasRecibido,chalecoRecibido,cascoRecibido,carroceriaRecibido,
            dosEslingasBancoRecibido,dosConosReflectivosRecibido,paralesRecibido, observacionesRecibido;

    private String horaSalidaRecibido,maderaNoSuperaMamparaRecibido,maderaNoSuperaParalesRecibido,noMaderaAtravieseMamparaRecibido,
            paralesMismaAlturaRecibido,ningunaUndSobrepasaParalesRecibido,cadaBancoAseguradoEslingasRecibido,carroceriaParalesBuenEstadoRecibido,
            conductorSalioLugarCinturonRecibido,paralesAbatiblesAseguradosEstrobosRecibido, fotoCamionRecibido;

    private String nombreSupervisorFirmasRecibido, nombreDespachadorFirmasRecibido,nombreConductorFirmasRecibido,
            nombreOperadorFirmasRecibido,firmaDespachadorRecibido, firmaSupervisorRecibido, firmaConductorRecibido,
            firmaOperadorRecibido;
    private ListasCargueDataBaseHelper dbLocal;
    boolean camposCompletos;
    private LottieAnimationView lottieAnimationButton;
    private TextView txtListaCreada;

    private CardView cardListaCreada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llenar);

        TextView txtFechaActual = findViewById(R.id.txtFechaLista);
        Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formatoFecha.format(fechaActual);
        txtFechaActual.setText(fechaFormateada);

        dbLocal = new ListasCargueDataBaseHelper(this);

        txtIdLista = findViewById(R.id.id_listaLlenarListas);
        imgNumItem = findViewById(R.id.imgNumItem);
        lLayoutIDLlenar = findViewById(R.id.lLayoutIDLlenarCargue);

        //TextView que se modifica cuando dan clic en un item de la lista cargue y descargue
        txtEncabezadoBSBListas = findViewById(R.id.txtEncabezadoListaCargueDescargue);
        txtNumItem = findViewById(R.id.txtNumItem);

        fabItem1 = findViewById(R.id.fabItem1CargueDescargue);
        fabItem2 = findViewById(R.id.fabItem2CargueDescargue);
        fabItem3 = findViewById(R.id.fabItem3CargueDescargue);
        fabItem4 = findViewById(R.id.fabItem4CargueDescargue);
        fabItem5 = findViewById(R.id.fabItem5CargueDescargue);

        //Titulos item lista Cargue y Descargue
        txtInfoLugarCargue = findViewById(R.id.txtItemInfoLugarCargue);
        txtInfoConductor = findViewById(R.id.txtItemInfoConductor);
        txtInfoVehiculo = findViewById(R.id.txtItemInfoVehiculo);
        txtEstadoCargue = findViewById(R.id.txtItemEstadoCargue);
        txtFirmasNombres = findViewById(R.id.txtItemFirmasNombres);

        //Asignación de los Fragmentos de cada item de la lista de Cargue y Descargue
        lista_item1 = new InfoLugarCargueFragment();
        lista_item2 = new InfoConductorFragment();
        lista_item3 = new InfoVehiculoFragment();
        lista_item4 = new EstadoCargueFragment();
        lista_item5 = new FirmasFragment();

        //BackDrop o FrameLayout que se despliega cuando se da clic en algún item de la lista
        bottom_sheet_deplegable = findViewById(R.id.bottomSheetPadre);

        btnRegresar = findViewById(R.id.btnRegresarLlenar);
        btnGuardarDatosCargueDescargue = findViewById(R.id.btnGuardarDatosCargueDescargue);
        lottieAnimationButton = findViewById(R.id.lottieAnimationButton);
        txtListaCreada = findViewById(R.id.txtlistaCreada);
        cardListaCreada = findViewById(R.id.cardListaCreada);

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_deplegable);
        bottomSheetBehavior.setPeekHeight(0);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setDraggable(true);
        bottomSheetBehavior.setHideable(false);
        fabItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoLugarCargue();
            }
        });
        fabItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoConductor();
            }
        });
        fabItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoVehiculo();
            }
        });
        fabItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estadoCargue();
            }
        });
        fabItem5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firmasNombres();
            }
        });
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAtras();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        };

        LlenarListasCargue.this.getOnBackPressedDispatcher().addCallback(LlenarListasCargue.this, callback);

        if (isInternetAvailable()){
            idLista = AgregarMostrarListas.idListaStatic;
            txtIdLista.setText(idLista);
        }
        else{
            lLayoutIDLlenar.setVisibility(View.GONE);
        }

        btnGuardarDatosCargueDescargue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetAvailable()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Animatoo.INSTANCE.animateSlideRight(LlenarListasCargue.this);
                            finish();
                        }
                    },1000);
                }
                else {
                    datosBDLocal();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Animatoo.INSTANCE.animateSlideRight(LlenarListasCargue.this);
                            finish();
                        }
                    },1000);
                }
                Toast.makeText(LlenarListasCargue.this, "Lista creada con éxito", Toast.LENGTH_SHORT).show();

                lottieAnimationButton.setVisibility(View.VISIBLE);
                lottieAnimationButton.playAnimation();
                txtListaCreada.setVisibility(View.VISIBLE);
                cardListaCreada.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onEstadoCamposActualizado(boolean camposCompletos) {
        Log.d("CAMPOS COMPLETOS 1","CAMPOS COMPLETOS 1 : " + camposCompletos);
        this.camposCompletos = camposCompletos;
        // Actualiza el estado de la lista en función del estado de los campos
        if (camposCompletos) {
            fabItem1.setImageResource(R.drawable.baseline_check_24);
        }
        else {
            fabItem1.setImageResource(R.drawable.icono_num_uno_listas);
        }
    }
    public void onEstadoCamposActualizado2(boolean camposCompletos) {
        Log.d("CAMPOS COMPLETOS 2","CAMPOS COMPLETOS 2 : " + camposCompletos);

        this.camposCompletos = camposCompletos;
        if (camposCompletos) {
            fabItem2.setImageResource(R.drawable.baseline_check_24);
        }
        else {
            fabItem2.setImageResource(R.drawable.icono_num_dos_listas);
        }
    }
    public void onEstadoCamposActualizado3(boolean camposCompletos) {
        Log.d("CAMPOS COMPLETOS 3","CAMPOS COMPLETOS 3 : " + camposCompletos);

        this.camposCompletos = camposCompletos;
        if (camposCompletos) {
            fabItem3.setImageResource(R.drawable.baseline_check_24);
        }
        else {
            fabItem3.setImageResource(R.drawable.icono_num_tres_listas);
        }
    }
    public void onEstadoCamposActualizado4(boolean camposCompletos) {
        Log.d("CAMPOS COMPLETOS 4","CAMPOS COMPLETOS 4 : " + camposCompletos);

        this.camposCompletos = camposCompletos;
        if (camposCompletos) {
            fabItem4.setImageResource(R.drawable.baseline_check_24);
        }
        else {
            fabItem4.setImageResource(R.drawable.icono_num_cuatro_listas);
        }
    }
    public void onEstadoCamposActualizado5(boolean camposCompletos) {
        Log.d("CAMPOS COMPLETOS 5","CAMPOS COMPLETOS 5 : " + camposCompletos);

        this.camposCompletos = camposCompletos;
        if (camposCompletos) {
            fabItem5.setImageResource(R.drawable.baseline_check_24);
        }
        else {
            fabItem5.setImageResource(R.drawable.icono_num_cinco_listas);
        }
    }
    public void onDatosEnviados(String id, String hora, String fecha, String tipoCargue, String zona, String nucleo, String finca,
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
                                String firmaSupervisor, String firmaDespachador, String firmaConductor, String firmaOperador){ //,byte[] fotoCamion){

        this.idRecibido = id;
        this.fechaRecibido = fecha;
        this.horaRecibido = hora;
        this.tipoCargueRecibido = tipoCargue;
        this.zonaRecibido = zona;
        this.nucleoRecibido = nucleo;
        this.fincaRecibido = finca;

        this.nombreConductorRecibido = nombreConductor;
        this.cedulaRecibido = cedula;
        this.lugarExpedicionRecibido = lugarExpedicion;
        this.licConduccionRecibido = licConduccion;
        this.polizaRCERecibido = polizaRCE;
        this.epsResRecibido = epsRes;
        this.arlResRecibido = arlRes;
        this.afpResRecibido = afpRes;
        this.cualEpsRecibido = cualEps;
        this.cualArlRecibido = cualArl;
        this.cualAfpRecibido = cualAfp;

        this.placaRecibido = placa;
        this.vehiculoRecibido = vehiculo;
        this.tarjetaPropiedadRecibido = tarjetaPropiedad;
        this.soatRecibido = soat;
        this.revisionTecnicomecanicaRecibido = revisionTecnicomecanica;
        this.lucesAltasRecibido = lucesAltas;
        this.lucesBajasRecibido = lucesBajas;
        this.direccionalesRecibido = direccionales;
        this.sonidoReversaRecibido = sonidoReversa;
        this.reversaRecibido = reversa;
        this.stopRecibido = stop;
        this.retrovisoresRecibido = retrovisores;
        this.plumillasRecibido = plumillas;
        this.estadoPanoramicosRecibido = estadoPanoramicos;
        this.espejosRecibido = espejos;
        this.bocinaRecibido = bocina;
        this.cinturonRecibido = cinturon;
        this.frenoRecibido = freno;
        this.llantasRecibido = llantas;
        this.botiquinRecibido = botiquin;
        this.extintorABCRecibido = extintorABC;
        this.botasRecibido = botas;
        this.chalecoRecibido = chaleco;
        this.cascoRecibido = casco;
        this.carroceriaRecibido = carroceria;
        this.dosEslingasBancoRecibido = dosEslingasBanco;
        this.dosConosReflectivosRecibido = dosConosReflectivos;
        this.paralesRecibido = parales;
        this.observacionesRecibido = observaciones;

        this.horaSalidaRecibido = horaSalida;
        this.maderaNoSuperaMamparaRecibido = maderaNoSuperaMampara;
        this. maderaNoSuperaParalesRecibido = maderaNoSuperaParales;
        this.noMaderaAtravieseMamparaRecibido = noMaderaAtravieseMampara;
        this.paralesMismaAlturaRecibido = paralesMismaAltura;
        this.ningunaUndSobrepasaParalesRecibido = ningunaUndSobrepasaParales;
        this.cadaBancoAseguradoEslingasRecibido = cadaBancoAseguradoEslingas;
        this.carroceriaParalesBuenEstadoRecibido = carroceriaParalesBuenEstado;
        this.conductorSalioLugarCinturonRecibido = conductorSalioLugarCinturon;
        this.paralesAbatiblesAseguradosEstrobosRecibido = paralesAbatiblesAseguradosEstrobos;
        this.fotoCamionRecibido = fotoCamion;
        //this.fotoCamionRecibido = decodedBytes;

        this.nombreSupervisorFirmasRecibido = nombreSupervisorFirma;
        this.nombreDespachadorFirmasRecibido = nombreDespachadorFirma;
        this.nombreConductorFirmasRecibido = nombreConductorFirma;
        this.nombreOperadorFirmasRecibido = nombreOperadorFirma;
        this.firmaSupervisorRecibido = firmaSupervisor;
        this.firmaDespachadorRecibido = firmaDespachador;
        this.firmaConductorRecibido = firmaConductor;
        this.firmaOperadorRecibido = firmaOperador;
    }
    private void datosBDLocal(){
        //ITEM 1
        ListasCargueModel.InfoLugarCargue item1 = new ListasCargueModel().getItem_1_Informacion_lugar_cargue();
        item1.setFecha(fechaRecibido.toString());
        item1.setHoraEntrada(horaRecibido.toString());
        item1.setTipoCargue(tipoCargueRecibido.toString());
        item1.setNombreZona(zonaRecibido.toString());
        item1.setNombreNucleo(nucleoRecibido.toString());
        item1.setNombreFinca(fincaRecibido.toString());

        //ITEM 2
        ListasCargueModel.InfoDelConductor item2 = new ListasCargueModel().getItem_2_Informacion_del_conductor();
        item2.setNombreConductor(nombreConductorRecibido.toString());
        item2.setCedula(cedulaRecibido.toString());
        item2.setLugarExpedicion(lugarExpedicionRecibido.toString());
        item2.setLicConduccionRes(licConduccionRecibido.toString());
        item2.setPolizaRCERes(polizaRCERecibido.toString());
        item2.setEpsRes(epsResRecibido.toString());
        item2.setArlRes(arlResRecibido.toString());
        item2.setAfpRes(afpResRecibido.toString());
        item2.setCualARL(cualArlRecibido.toString());
        item2.setCualEPS(cualEpsRecibido.toString());
        item2.setCualAFP(cualAfpRecibido.toString());

        //ITEM 3
        ListasCargueModel.InfoDelVehiculo item3 = new ListasCargueModel().getItem_3_Informacion_vehiculo();
        item3.setPlaca(placaRecibido.toString());
        item3.setVehiculo(vehiculoRecibido.toString());
        item3.setTarjetaPropiedad(tarjetaPropiedadRecibido.toString());
        item3.setSoatVigente(soatRecibido.toString());
        item3.setRevisionTecnicomecanica(revisionTecnicomecanicaRecibido.toString());
        item3.setLucesAltas(lucesAltasRecibido.toString());
        item3.setLucesBajas(lucesBajasRecibido.toString());
        item3.setDireccionales(direccionalesRecibido.toString());
        item3.setSonidoReversa(sonidoReversaRecibido.toString());
        item3.setReversa(reversaRecibido.toString());
        item3.setStop(stopRecibido.toString());
        item3.setRetrovisores(retrovisoresRecibido.toString());
        item3.setPlumillas(plumillasRecibido.toString());
        item3.setEstadoPanoramicos(estadoPanoramicosRecibido.toString());
        item3.setEspejos(espejosRecibido.toString());
        item3.setBocina(bocinaRecibido.toString());
        item3.setCinturon(cinturonRecibido.toString());
        item3.setFreno(frenoRecibido.toString());
        item3.setLlantas(llantasRecibido.toString());
        item3.setBotiquin(botiquinRecibido.toString());
        item3.setExtintorABC(extintorABCRecibido.toString());
        item3.setBotas(botasRecibido.toString());
        item3.setChaleco(chalecoRecibido.toString());
        item3.setCasco(cascoRecibido.toString());
        item3.setCarroceria(carroceriaRecibido.toString());
        item3.setDosEslingasBanco(dosEslingasBancoRecibido.toString());
        item3.setDosConosReflectivos(dosConosReflectivosRecibido.toString());
        item3.setParales(paralesRecibido.toString());
        item3.setObservacionesCamion(observacionesRecibido.toString());

        //ITEM 4
        ListasCargueModel.EstadoDelCargue item4 = new ListasCargueModel().getItem_4_Estado_cargue();
        item4.setHoraSalida(horaSalidaRecibido.toString());
        item4.setMaderaNoSuperaMampara(maderaNoSuperaMamparaRecibido.toString());
        item4.setMaderaNoSuperaParales(maderaNoSuperaParalesRecibido.toString());
        item4.setNoMaderaAtraviesaMampara(noMaderaAtravieseMamparaRecibido.toString());
        item4.setParalesMismaAltura(paralesMismaAlturaRecibido.toString());
        item4.setNingunaUndSobrepasaParales(ningunaUndSobrepasaParalesRecibido.toString());
        item4.setCadaBancoAseguradoEslingas(cadaBancoAseguradoEslingasRecibido.toString());
        item4.setCarroceriaParalesBuenEstado(carroceriaParalesBuenEstadoRecibido.toString());
        item4.setConductorSalioLugarCinturon(conductorSalioLugarCinturonRecibido.toString());
        item4.setParalesAbatiblesAseguradosEstrobos(paralesAbatiblesAseguradosEstrobosRecibido.toString());
        item4.setFotoCamion(fotoCamionRecibido);

        //ITEM FIRMAS
        ListasCargueModel firmas = new ListasCargueModel();
        firmas.setNombre_Supervisor(nombreSupervisorFirmasRecibido);
        firmas.setNombre_Despachador(nombreDespachadorFirmasRecibido);
        firmas.setNombre_Conductor(nombreConductorFirmasRecibido);
        firmas.setNombre_Operador(nombreOperadorFirmasRecibido);
        firmas.setFirma_Supervisor(firmaSupervisorRecibido);
        firmas.setFirma_Despachador(firmaDespachadorRecibido);
        firmas.setFirma_Conductor(firmaConductorRecibido);
        firmas.setFirma_Operador(firmaOperadorRecibido);
        dbLocal.insertList(item1,item2,item3,item4,firmas);//,fotoCamionRecibido)  ,base64Image);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bottomSheetInformacion, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // Pasar los datos al fragmento AddData2Fragment si es necesario
        if (fragment instanceof InfoConductorFragment) {
            ((InfoConductorFragment) fragment).recibirDatos2(idRecibido, horaRecibido, fechaRecibido, tipoCargueRecibido, zonaRecibido, nucleoRecibido, fincaRecibido);
        }

        if (fragment instanceof InfoVehiculoFragment) {
            ((InfoVehiculoFragment) fragment).recibirDatos3(idRecibido, horaRecibido, fechaRecibido, tipoCargueRecibido,
                    zonaRecibido, nucleoRecibido, fincaRecibido,nombreConductorRecibido,cedulaRecibido,
                    lugarExpedicionRecibido,licConduccionRecibido,polizaRCERecibido,epsResRecibido,arlResRecibido,
                    afpResRecibido,cualEpsRecibido,cualArlRecibido,cualAfpRecibido);
        }

        if (fragment instanceof EstadoCargueFragment) {
            ((EstadoCargueFragment) fragment).recibirDatos4(idRecibido, horaRecibido, fechaRecibido, tipoCargueRecibido,
                    zonaRecibido, nucleoRecibido, fincaRecibido,nombreConductorRecibido,cedulaRecibido,
                    lugarExpedicionRecibido,licConduccionRecibido,polizaRCERecibido,epsResRecibido,arlResRecibido,
                    afpResRecibido,cualEpsRecibido,cualArlRecibido,cualAfpRecibido,placaRecibido,vehiculoRecibido,tarjetaPropiedadRecibido,
                    soatRecibido,revisionTecnicomecanicaRecibido,lucesAltasRecibido,lucesBajasRecibido,direccionalesRecibido,sonidoReversaRecibido,
                    reversaRecibido,stopRecibido, retrovisoresRecibido,plumillasRecibido,estadoPanoramicosRecibido,espejosRecibido,bocinaRecibido,
                    cinturonRecibido,frenoRecibido,llantasRecibido,botiquinRecibido,extintorABCRecibido,botasRecibido,chalecoRecibido,cascoRecibido,
                    carroceriaRecibido,dosEslingasBancoRecibido,dosConosReflectivosRecibido,paralesRecibido,observacionesRecibido);
        }

        if (fragment instanceof FirmasFragment) {
            ((FirmasFragment) fragment).recibirDatos5(idRecibido, horaRecibido, fechaRecibido, tipoCargueRecibido,
                    zonaRecibido, nucleoRecibido, fincaRecibido,nombreConductorRecibido,cedulaRecibido,
                    lugarExpedicionRecibido,licConduccionRecibido,polizaRCERecibido,epsResRecibido,arlResRecibido,
                    afpResRecibido,cualEpsRecibido,cualArlRecibido,cualAfpRecibido,placaRecibido,vehiculoRecibido,tarjetaPropiedadRecibido,
                    soatRecibido,revisionTecnicomecanicaRecibido,lucesAltasRecibido,lucesBajasRecibido,direccionalesRecibido,sonidoReversaRecibido,
                    reversaRecibido,stopRecibido, retrovisoresRecibido,plumillasRecibido,estadoPanoramicosRecibido,espejosRecibido,bocinaRecibido,
                    cinturonRecibido,frenoRecibido,llantasRecibido,botiquinRecibido,extintorABCRecibido,botasRecibido,chalecoRecibido,cascoRecibido,
                    carroceriaRecibido,dosEslingasBancoRecibido,dosConosReflectivosRecibido,paralesRecibido,observacionesRecibido,horaSalidaRecibido,
                    maderaNoSuperaMamparaRecibido,maderaNoSuperaParalesRecibido,noMaderaAtravieseMamparaRecibido,paralesMismaAlturaRecibido,
                    ningunaUndSobrepasaParalesRecibido, cadaBancoAseguradoEslingasRecibido,carroceriaParalesBuenEstadoRecibido,
                    conductorSalioLugarCinturonRecibido,paralesAbatiblesAseguradosEstrobosRecibido,fotoCamionRecibido);
        }
    }

    private void toggleBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }
    private void infoLugarCargue() {
        Toast.makeText(LlenarListasCargue.this, "Presionó Información del lugar donde se cargará", Toast.LENGTH_SHORT).show();

        loadFragment(lista_item1);

        txtEncabezadoBSBListas.setText(txtInfoLugarCargue.getText());
        imgNumItem.setImageResource(R.drawable.icono_num_uno_listas);
        txtNumItem.setText("1");

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        toggleBottomSheet();
    }
    private void infoConductor() {
        Toast.makeText(LlenarListasCargue.this, "Presionó información del conductor", Toast.LENGTH_SHORT).show();

        loadFragment(lista_item2);
        txtNumItem.setText("2");

        txtEncabezadoBSBListas.setText(txtInfoConductor.getText());
        imgNumItem.setImageResource(R.drawable.icono_num_dos_listas);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void infoVehiculo() {
        Toast.makeText(LlenarListasCargue.this, "Presionó información del vehículo", Toast.LENGTH_SHORT).show();

        loadFragment(lista_item3);
        txtNumItem.setText("3");

        txtEncabezadoBSBListas.setText(txtInfoVehiculo.getText());
        imgNumItem.setImageResource(R.drawable.icono_num_tres_listas);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void estadoCargue() {
        Toast.makeText(LlenarListasCargue.this, "Presionó estado del cargue", Toast.LENGTH_SHORT).show();

        loadFragment(lista_item4);
        txtNumItem.setText("4");

        txtEncabezadoBSBListas.setText(txtEstadoCargue.getText());
        imgNumItem.setImageResource(R.drawable.icono_num_cuatro_listas);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void firmasNombres() {

        Toast.makeText(LlenarListasCargue.this, "Presionó firmas y nombre", Toast.LENGTH_SHORT).show();

        loadFragment(lista_item5);
        txtNumItem.setText("5");

        txtEncabezadoBSBListas.setText(txtFirmasNombres.getText());
        imgNumItem.setImageResource(R.drawable.icono_num_cinco_listas);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    private void eliminarListaNoCreada(String id_lista) {
        if(isInternetAvailable()){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(pathLista)
                    .document(id_lista)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(LlenarListasCargue.this, "No se creó la lista", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void volverAtras(){
        if(isInternetAvailable()){
            eliminarListaNoCreada(idLista);
        }
        Animatoo.INSTANCE.animateSlideLeft(LlenarListasCargue.this);
        finish();
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) LlenarListasCargue.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connectivityManager.getActiveNetwork();
                if (network == null) return false;
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }
}
