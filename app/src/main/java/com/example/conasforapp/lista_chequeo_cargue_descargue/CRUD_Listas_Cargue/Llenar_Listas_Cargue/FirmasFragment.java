package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Llenar_Listas_Cargue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.UsuariosModel;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FirmasFragment extends Fragment {
    private View view;
    private SignaturePad sPadFirmaSupervisor, sPadFirmaDespachador, sPadFirmaConductor, sPadFirmaOperadorCargue;
    private Button btnAgregarFirmas;
    private ImageButton btnLimpiarFirmaSupervisor, btnAceptarFirmaSupervisor, btnLimpiarFirmaDespachador, btnAceptarFirmaDespachador, btnLimpiarFirmaConductor, btnAceptarFirmaConductor, btnLimpiarFirmaOperador, btnAceptarFirmaOperador;
    private Button btnFirmarSupervisor, btnFirmarDespachador, btnFirmarConductor, btnFirmarOperador;
    private RelativeLayout rlOpcFirmaSupervisor, rlOpcFirmaDespachador, rlOpcFirmaConductor, rlOpcFirmaOperador;
    private FrameLayout frameFirmaSupervisor, frameFirmaDespachador, frameFirmaConductor, frameFirmaOperador;
    private TextInputEditText edtNombreSupervisor, edtNombreDespachador,edtNombreConductor,edtNombreOperador;
    private FirebaseFirestore db;
    private StorageReference storage;
    private String pathLista = "Listas chequeo cargue descargue";
    //public String id_lista = AgregarListaCargueDescargue.idListaStatic;
    public String id_lista = AgregarMostrarListas.idListaStatic;
    private Bitmap firmaSupervisor = null, firmaDespachador = null, firmaConductor = null, firmaOperador = null;
    UsuariosModel usuariosModel = new UsuariosModel();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uid = currentUser.getUid();
    String pathUsuarios = "Usuarios";
    public String nombreSupervisor = "";
    private Map<String, Object> nombresFirmas = new HashMap<>();
    Map<String, Object> firmas = new HashMap<>();
    String pathFirmasCargueDescargue = "Firmas_Cargue_Descargue/";
    private DatosListener5 datosListener5;
    private String idRecibido = null,horaRecibido  = null,fechaRecibido  = null,tipoCargueRecibido  = null,
            zonaRecibido  = null, nucleoRecibido  = null, fincaRecibido  = null;
    private String nombreConductorRecibido, cedulaRecibido, lugarExpedicionRecibido, licConduccionRecibido,
            polizaRCERecibido,epsResRecibido,arlResRecibido,afpResRecibido, cualEpsRecibido,cualArlRecibido,
            cualAfpRecibido;
    private String placaRecibido,vehiculoRecibido, tarjetaPropiedadRecibido,soatRecibido,revisionTecnicomecanicaRecibido,
            lucesAltasRecibido,lucesBajasRecibido,direccionalesRecibido, sonidoReversaRecibido,reversaRecibido,stopRecibido,
            retrovisoresRecibido,plumillasRecibido,estadoPanoramicosRecibido,espejosRecibido,bocinaRecibido,cinturonRecibido,
            frenoRecibido,llantasRecibido,botiquinRecibido,extintorABCRecibido,botasRecibido,chalecoRecibido,cascoRecibido,
            carroceriaRecibido,dosEslingasBancoRecibido,dosConosReflectivosRecibido, paralesRecibido, observacionesRecibido;

    private String horaSalidaRecibido, maderaNoSuperaMamparaRecibido, maderaNoSuperaParalesRecibido, noMaderaAtraviesaMamparaRecibido,
            paralesMismaAlturaRecibido, ningunaUndSobrepasaParalesRecibido, cadaBancoAseguradoEslingasRecibido,
            carroceriaParalesBuenEstadoRecibido, conductorSalioLugarCinturonRecibido, paralesAbatiblesAseguradosEstrobosRecibido,
            fotoCamionRecibido;
    public static String firmaSupervisorStatic,firmaDespachadorStatic,firmaConductorStatic,firmaOperadorStatic;
    public FirmasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_firmas_prueba, container, false);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance().getReference();

        edtNombreSupervisor = view.findViewById(R.id.edtNombreSupervisorFirmas);
        edtNombreDespachador = view.findViewById(R.id.edtNombreDespachadorFirmas);
        edtNombreConductor = view.findViewById(R.id.edtNombreConductorFirmas);
        edtNombreOperador = view.findViewById(R.id.edtNombreOperadorFirmas);

        btnLimpiarFirmaSupervisor = view.findViewById(R.id.btnLimpiarFirmaSupervisorPrueba);
        btnAceptarFirmaSupervisor = view.findViewById(R.id.btnAceptarFirmaSupervisorPrueba);

        btnLimpiarFirmaDespachador = view.findViewById(R.id.btnLimpiarFirmaDespachadorPrueba);
        btnAceptarFirmaDespachador = view.findViewById(R.id.btnAceptarFirmaDespachadorPrueba);

        btnLimpiarFirmaConductor =view.findViewById(R.id.btnLimpiarFirmaConductorPrueba);
        btnAceptarFirmaConductor = view.findViewById(R.id.btnAceptarFirmaConductorPrueba);

        btnLimpiarFirmaOperador =view.findViewById(R.id.btnLimpiarFirmaOperadorPrueba);
        btnAceptarFirmaOperador = view.findViewById(R.id.btnAceptarFirmaOperadorPrueba);

        rlOpcFirmaSupervisor = view.findViewById(R.id.rlOpcFirmaSupervisorPrueba);
        rlOpcFirmaSupervisor.setVisibility(View.GONE);

        rlOpcFirmaDespachador = view.findViewById(R.id.rlOpcFirmaDespachadorPrueba);
        rlOpcFirmaDespachador.setVisibility(View.GONE);

        rlOpcFirmaConductor = view.findViewById(R.id.rlOpcFirmaConductorPrueba);
        rlOpcFirmaConductor.setVisibility(View.GONE);

        rlOpcFirmaOperador = view.findViewById(R.id.rlOpcFirmaOperadorPrueba);
        rlOpcFirmaOperador.setVisibility(View.GONE);

        sPadFirmaSupervisor = view.findViewById(R.id.sPadFirmaSupervisorPrueba);
        sPadFirmaSupervisor.setEnabled(false);

        sPadFirmaDespachador = view.findViewById(R.id.sPadFirmaDespachadorPrueba);
        sPadFirmaDespachador.setEnabled(false);

        sPadFirmaConductor = view.findViewById(R.id.sPadFirmaConductorPrueba);
        sPadFirmaConductor.setEnabled(false);

        sPadFirmaOperadorCargue = view.findViewById(R.id.sPadFirmaOperadorCarguePrueba);
        sPadFirmaOperadorCargue.setEnabled(false);

        frameFirmaSupervisor = view.findViewById(R.id.frameFirmaSupervisorPrueba);
        frameFirmaDespachador = view.findViewById(R.id.frameFirmaDespachadorPrueba);
        frameFirmaConductor = view.findViewById(R.id.frameFirmaConductorPrueba);
        frameFirmaOperador = view.findViewById(R.id.frameFirmaOperadorPrueba);

        btnFirmarSupervisor = view.findViewById(R.id.btnFirmarSupervisorPrueba);
        btnFirmarDespachador = view.findViewById(R.id.btnFirmarDespachadorPrueba);
        btnFirmarConductor = view.findViewById(R.id.btnFirmarConductorPrueba);
        btnFirmarOperador = view.findViewById(R.id.btnFirmarOperadorPrueba);

        btnAgregarFirmas = view.findViewById(R.id.btnAgregarFirmasPrueba);
        btnAgregarFirmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEstadoLista();

                firmaSupervisor = sPadFirmaSupervisor.getSignatureBitmap();
                firmaConductor = sPadFirmaConductor.getSignatureBitmap();
                firmaDespachador = sPadFirmaDespachador.getSignatureBitmap();
                firmaOperador = sPadFirmaOperadorCargue.getSignatureBitmap();

                if(isNetworkAvailable()){
                    enviarFirma1(firmaSupervisor);
                    enviarFirma2(firmaDespachador);
                    enviarFirma3(firmaConductor);
                    enviarFirma4(firmaOperador);

                    nombresFirmas();
                    obtenerDatosUsuario();
                }
                else {
                    agregarDatosBDLocal(firmaSupervisor,firmaDespachador,firmaConductor,firmaOperador);
                }
                Toast.makeText(getContext(), "Firmas guardadas con éxito", Toast.LENGTH_SHORT).show();
                //limpiarCampos();
            }
        });
        firmaSupervisor();
        firmaDespachador();
        firmaConductor();
        firmaOperador();
        return view;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public interface DatosListener5{
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
                             String conductorSalioLugarCinturon, String paralesAbatiblesAseguradosEstrobos,String fotoCamion,
                             String nombreSupervisorFirma, String nombreDespachadorFirma, String nombreConductorFirma, String nombreOperadorFirma,
                             String firmaSupervisor, String firmaDespachador, String firmaConductor, String firmaOperador);//,byte[] fotoCamion);
    }

    public void recibirDatos5(String id, String horaEntrada, String fecha, String tipoCargue, String zona, String nucleo,
                              String finca, String nombreConductor, String cedula, String lugarExpedicion, String licConduccion,
                              String polizaRCE, String epsRes, String arlRes, String afpRes, String cualEps, String cualArl, String cualAfp,
                              String placa,String vehiculo,String tarjetaPropiedad, String soat, String revisionTecnicomecanica,
                              String lucesAltas, String lucesBajas, String direccionales, String sonidoReversa, String reversa, String stop,
                              String retrovisores, String plumillas,String estadoPanoramicos, String espejos, String bocina, String cinturon,
                              String freno, String llantas,String botiquin, String extintorABC, String botas, String chaleco, String casco,
                              String carroceria,String dosEslingasBanco, String dosConosReflectivos, String parales, String observaciones,
                              String horaSalida, String maderaNoSuperaMampara, String maderaNoSuperaParales, String noMaderaAtravieseMampara,
                              String paralesMismaAltura, String ningunaUndSobrepasaParales, String cadaBancoAseguradoEslingas,
                              String carroceriaParalesBuenEstado, String conductorSalioLugarCinturon,
                              String paralesAbatiblesAseguradosEstrobos,String fotoCamion){

        this.idRecibido = id;
        this.horaRecibido = horaEntrada;
        this.fechaRecibido = fecha;
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
        this.maderaNoSuperaParalesRecibido = maderaNoSuperaParales;
        this.noMaderaAtraviesaMamparaRecibido = noMaderaAtravieseMampara;
        this.paralesMismaAlturaRecibido = paralesMismaAltura;
        this.ningunaUndSobrepasaParalesRecibido = ningunaUndSobrepasaParales;
        this.cadaBancoAseguradoEslingasRecibido = cadaBancoAseguradoEslingas;
        this.carroceriaParalesBuenEstadoRecibido = carroceriaParalesBuenEstado;
        this.conductorSalioLugarCinturonRecibido = conductorSalioLugarCinturon;
        this.paralesAbatiblesAseguradosEstrobosRecibido = paralesAbatiblesAseguradosEstrobos;
        this.fotoCamionRecibido = fotoCamion;

        /*
        Log.d("HORA ENTRADA RECIBIDA EN FIRMAS FRAGMENT","HORA ENTRADA RECIBIDA EN FIRMAS FRAGMENT : " + horaEntrada);
        Log.d("DATOS RECIBIDOS ESTADO DEL CARGUE","DATOS RECIBIDOS ESTADO DEL CARGUE : ");
        Log.d("HORA SALIDA EN FIRMAS","HORA SALIDA EN FIRMAS : " + horaSalidaRecibido);
        Log.d("MADERA NO SUPERA MAMPARA EN FIRMAS","MADERA NO SUPERA MAMPARA EN FIRMAS : " + maderaNoSuperaMamparaRecibido);
        Log.d("MADERA NO SUPERA PARALES","MADERA NO SUPERA PARALES : " + maderaNoSuperaParalesRecibido);
        Log.d("NO HAY MADERA QUE ATRAVIESE MAMPARA","NO HAY MADERA QUE ATRAVIESE MAMPARA " + noMaderaAtraviesaMamparaRecibido);
        Log.d("PARALES MISMA ALTURA","PARALES MISMA ALTURA : " + paralesMismaAlturaRecibido);
        Log.d("NINGUNA UND SOBREPASA PARALES","NINGUNA UND SOBREPASA PARALES : " + ningunaUndSobrepasaParalesRecibido);
        Log.d("CADA BANCO ASEGURADO","CADA BANCO ASEGURADO : " + cadaBancoAseguradoEslingasRecibido);
        Log.d("CARROCERIA PARALES BUEN ESTADO","CARROCERIA PARALES BUEN ESTADO : " + carroceriaParalesBuenEstadoRecibido);
        Log.d("CONDUCTOR SALIO CINTURON","CONDUCTOR SALIO CINTURON : " + conductorSalioLugarCinturonRecibido);
        Log.d("PARALES ABATIBLES ASEGURADOS","PARALES ABATIBLES ASEGURADOS : " + paralesAbatiblesAseguradosEstrobosRecibido);
        Log.d("FOTO CAMION EN FIRMAS","FOTO CAMION EN FIRMAS : " + fotoCamionRecibido);

         */
    }

    private void limpiarCampos() {
        sPadFirmaSupervisor.clear();
        sPadFirmaDespachador.clear();
        sPadFirmaConductor.clear();
        sPadFirmaOperadorCargue.clear();
        // Limpiar otros campos si es necesario
    }

    private void firmaSupervisor(){
        btnFirmarSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFirmarSupervisor.setVisibility(View.INVISIBLE);
                frameFirmaSupervisor.setVisibility(View.INVISIBLE);
                sPadFirmaSupervisor.setEnabled(true);//Habilita la firma
                sPadFirmaSupervisor.setMinWidth(3f); // Ancho mínimo del trazo
                sPadFirmaSupervisor.setMaxWidth(3f); // Ancho máximo del trazo
                rlOpcFirmaSupervisor.setVisibility(View.VISIBLE); //Habilita el menú de opciones de la firma del supervisor
            }
        });
        sPadFirmaSupervisor.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {// Se llama cuando se comienza a escribir
            }
            @Override
            public void onSigned() {// Se llama cuando el usuario ha terminado de escribir
                btnAceptarFirmaSupervisor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Firma aceptada", Toast.LENGTH_SHORT).show();
                        frameFirmaSupervisor.setVisibility(View.VISIBLE);
                        btnFirmarSupervisor.setVisibility(View.VISIBLE);
                        sPadFirmaSupervisor.setEnabled(false);
                        rlOpcFirmaSupervisor.setVisibility(View.GONE);
                    }
                });
            }
            @Override
            public void onClear() {// Se llama cuando se borra la firma
            }
        });
        btnLimpiarFirmaSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPadFirmaSupervisor.clear(); //Limpia la firma
            }
        });
    }
    private void firmaDespachador(){
        btnFirmarDespachador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFirmarDespachador.setVisibility(View.INVISIBLE);
                frameFirmaDespachador.setVisibility(View.INVISIBLE);

                //Habilita la firma
                sPadFirmaDespachador.setEnabled(true);

                // Ajustar parámetros para mejorar la experiencia de escritura
                sPadFirmaDespachador.setMinWidth(3f); // Ancho mínimo del trazo
                sPadFirmaDespachador.setMaxWidth(3f); // Ancho máximo del trazo

                rlOpcFirmaDespachador.setVisibility(View.VISIBLE);
            }
        });

        sPadFirmaDespachador.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                btnAceptarFirmaDespachador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getContext(), "Firma aceptada", Toast.LENGTH_SHORT).show();

                        frameFirmaDespachador.setVisibility(View.VISIBLE);
                        btnFirmarDespachador.setVisibility(View.VISIBLE);
                        sPadFirmaDespachador.setEnabled(false);
                        rlOpcFirmaDespachador.setVisibility(View.GONE);
                    }
                });
            }
            @Override
            public void onClear() {

            }
        });

        btnLimpiarFirmaDespachador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Limpia la firma
                sPadFirmaDespachador.clear();
            }
        });
    }
    private void firmaConductor(){
        btnFirmarConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnFirmarConductor.setVisibility(View.INVISIBLE);
                frameFirmaConductor.setVisibility(View.INVISIBLE);

                //Habilita la firma
                sPadFirmaConductor.setEnabled(true);

                // Ajustar parámetros para mejorar la experiencia de escritura
                sPadFirmaConductor.setMinWidth(3f); // Ancho mínimo del trazo
                sPadFirmaConductor.setMaxWidth(3f); // Ancho máximo del trazo

                rlOpcFirmaConductor.setVisibility(View.VISIBLE);

            }
        });

        sPadFirmaConductor.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                btnAceptarFirmaConductor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Firma aceptada", Toast.LENGTH_SHORT).show();
                        frameFirmaConductor.setVisibility(View.VISIBLE);
                        btnFirmarConductor.setVisibility(View.VISIBLE);
                        sPadFirmaConductor.setEnabled(false);
                        rlOpcFirmaConductor.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onClear() {

            }
        });

        btnLimpiarFirmaConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Limpia la firma
                sPadFirmaConductor.clear();
            }
        });
    }
    private void firmaOperador(){
        btnFirmarOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFirmarOperador.setVisibility(View.INVISIBLE);
                frameFirmaOperador.setVisibility(View.INVISIBLE);
                sPadFirmaOperadorCargue.setEnabled(true);//Habilita la firma
                sPadFirmaOperadorCargue.setMinWidth(3f); // Ancho mínimo del trazo
                sPadFirmaOperadorCargue.setMaxWidth(3f); // Ancho máximo del trazo
                rlOpcFirmaOperador.setVisibility(View.VISIBLE);
            }
        });
        sPadFirmaOperadorCargue.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }
            @Override
            public void onSigned() {
                btnAceptarFirmaOperador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Firma aceptada", Toast.LENGTH_SHORT).show();
                        frameFirmaOperador.setVisibility(View.VISIBLE);
                        btnFirmarOperador.setVisibility(View.VISIBLE);
                        sPadFirmaOperadorCargue.setEnabled(false);
                        rlOpcFirmaOperador.setVisibility(View.GONE);
                    }
                });
            }
            @Override
            public void onClear() {
            }
        });

        btnLimpiarFirmaOperador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Limpia la firma
                sPadFirmaOperadorCargue.clear();
            }
        });
    }
    private void obtenerDatosUsuario(){
        if (currentUser != null) {
            // El usuario está autenticado
            String id_Usuario = currentUser.getUid();
            db.collection(pathUsuarios).document(id_Usuario).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            usuariosModel = document.toObject(UsuariosModel.class);
                            nombreSupervisor = usuariosModel.getNombre();
                            Log.d("Nombre SUUPERVISOR","Nombre SUUPERVISOR" + nombreSupervisor);
                            edtNombreSupervisor.setText(nombreSupervisor);
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
    public void nombresFirmas(){
        nombresFirmas.put("nombre_Supervisor",edtNombreSupervisor.getText().toString());
        nombresFirmas.put("nombre_Despachador",edtNombreDespachador.getText().toString());
        nombresFirmas.put("nombre_Conductor",edtNombreConductor.getText().toString());
        nombresFirmas.put("nombre_Operador",edtNombreOperador.getText().toString());

        db.collection(pathLista)
                .document(id_lista)
                .update(nombresFirmas).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Datos almacenados correctamente", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "No se almacenaron los datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void agregarDatosBDLocal(Bitmap firma1, Bitmap firma2, Bitmap firma3,Bitmap firma4){
        String nombreSupervisor = edtNombreSupervisor.getText().toString();
        String nombreDespachador = edtNombreDespachador.getText().toString();
        String nombreConductor = edtNombreConductor.getText().toString();
        String nombreOperador = edtNombreOperador.getText().toString();


        String firma1Base64 = convertBitmapToBase64(firma1);
        String firma2Base64 = convertBitmapToBase64(firma2);
        String firma3Base64 = convertBitmapToBase64(firma3);
        String firma4Base64 = convertBitmapToBase64(firma4);

        /*
        // Redimensionar los Bitmap a un tamaño más manejable
        firma1 = getResizedBitmap(firma1, 200, 200); // Ajusta el tamaño según tus necesidades
        firma2 = getResizedBitmap(firma2, 200, 200);
        firma3 = getResizedBitmap(firma3, 200, 200);
        firma4 = getResizedBitmap(firma4, 200, 200);

        String firma1Base64 = convertBitmapToBase64(firma1);
        String firma2Base64 = convertBitmapToBase64(firma2);
        String firma3Base64 = convertBitmapToBase64(firma3);
        String firma4Base64 = convertBitmapToBase64(firma4);

         */

        if (datosListener5 != null) {
            datosListener5.onDatosEnviados(idRecibido, horaRecibido, fechaRecibido, tipoCargueRecibido, zonaRecibido, nucleoRecibido, fincaRecibido,
                    nombreConductorRecibido,cedulaRecibido,lugarExpedicionRecibido,licConduccionRecibido,polizaRCERecibido,epsResRecibido,
                    arlResRecibido,afpResRecibido,cualEpsRecibido,cualArlRecibido,cualAfpRecibido,placaRecibido,vehiculoRecibido,tarjetaPropiedadRecibido,
                    soatRecibido,revisionTecnicomecanicaRecibido,lucesAltasRecibido,lucesBajasRecibido,direccionalesRecibido,sonidoReversaRecibido,
                    reversaRecibido,stopRecibido, retrovisoresRecibido,plumillasRecibido,estadoPanoramicosRecibido,espejosRecibido,bocinaRecibido,
                    cinturonRecibido,frenoRecibido,llantasRecibido,botiquinRecibido,extintorABCRecibido,botasRecibido,chalecoRecibido,cascoRecibido,
                    carroceriaRecibido,dosEslingasBancoRecibido,dosConosReflectivosRecibido,paralesRecibido,observacionesRecibido,
                    horaSalidaRecibido,maderaNoSuperaMamparaRecibido,maderaNoSuperaParalesRecibido,noMaderaAtraviesaMamparaRecibido,paralesMismaAlturaRecibido,
                    ningunaUndSobrepasaParalesRecibido,cadaBancoAseguradoEslingasRecibido,carroceriaParalesBuenEstadoRecibido,conductorSalioLugarCinturonRecibido,
                    paralesAbatiblesAseguradosEstrobosRecibido,fotoCamionRecibido,nombreSupervisor,nombreDespachador,nombreConductor,nombreOperador,
                    firma1Base64,firma2Base64,firma3Base64,firma4Base64); //,"".getBytes()); //Arrays.toString(decodedBytes).getBytes());
        }

        // Liberar memoria después de usar los Bitmap
        recycleBitmap(firma1);
        recycleBitmap(firma2);
        recycleBitmap(firma3);
        recycleBitmap(firma4);
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BITMAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] firmaBytes = baos.toByteArray();
        return Base64.encodeToString(firmaBytes, Base64.DEFAULT);
    }

    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public void enviarFirma1(Bitmap signature1) {
        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calHoy.getTime());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature1.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        //String id_lista = AgregarListaCargueDescargue.idListaStatic;
        String id_lista = AgregarMostrarListas.idListaStatic;
        //String finca = AgregarListaCargueDescargue.listaModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();
        String finca = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();

        String fileName = "firma_supervisor_" + "_" + formattedDate + "_ID_USUARIO:_"+  uid + "_ID_LISTA:_" + id_lista + ".png";  // Generar un nombre único para el archivo en el Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference(); // Referencia al Firebase Storage
        StorageReference firmaRef = storageRef.child(pathFirmasCargueDescargue + formattedDate + "/" + fileName); // Referencia al archivo en el Firebase Storage

        firmaRef.putBytes(data) // Subir la firma al Firebase Storage
                .addOnSuccessListener(taskSnapshot -> {
                    firmaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString(); // Obtener la URL de descarga del archivo
                        guardarFirmaEnFirestore(downloadUrl);  // Guardar la URL de descarga en Firestore
                        firmaSupervisorStatic =  downloadUrl;
                        Log.d("FIRMA SUPERVISOR STATIC EN ENVIAR 1","FIRMA SUPERVISOR STATIC EN ENVIAR 1 : " +firmaSupervisorStatic);
                    });
                })
                .addOnFailureListener(exception -> {
                    // Manejar el error
                    Toast.makeText(getContext(), "Error al guardar la firma", Toast.LENGTH_SHORT).show();
                });
    }
    private void guardarFirmaEnFirestore(String downloadUrl) {
        // Crear un nuevo documento en la colección 'firmas' en Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> firmaData1 = new HashMap<>();

        firmaData1.put("firma_Supervisor", downloadUrl);
        firmaData1.put("timestamp", FieldValue.serverTimestamp()); // Opcional, para registrar la fecha de la firma

        firestore.collection(pathLista).document(id_lista).update(firmaData1)
                .addOnSuccessListener(documentReference -> {

                    //Toast.makeText(getContext(), "Firma guardada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al guardar firma", Toast.LENGTH_SHORT).show();
                });
    }
    public void enviarFirma2(Bitmap signature2) {

        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calHoy.getTime());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature2.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        //String id_lista = AgregarListaCargueDescargue.idListaStatic;
        //String finca = AgregarListaCargueDescargue.listaModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();
        String finca = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();

        // Generar un nombre único para el archivo en el Storage
        String fileName = "firma_despachador_" + "_" + formattedDate + "_ID_USUARIO:_"+  uid + "_ID_LISTA:_" + id_lista + ".png";
        // Referencia al Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Referencia al archivo en el Firebase Storage
        StorageReference firmaRef = storageRef.child(pathFirmasCargueDescargue + formattedDate + "/" + fileName);

        // Subir la firma al Firebase Storage
        firmaRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    firmaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString(); // Obtener la URL de descarga del archivo
                        guardarFirma2EnFirestore(downloadUrl); // Guardar la URL de descarga en Firestore
                        firmaDespachadorStatic = downloadUrl;
                    });
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getContext(), "Error al subir la firma", Toast.LENGTH_SHORT).show();
                });
    }
    private void guardarFirma2EnFirestore(String downloadUrl) {
        // Crear un nuevo documento en la colección 'firmas' en Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> firmaData2 = new HashMap<>();

        firmaData2.put("firma_Despachador", downloadUrl);
        firmaData2.put("timestamp", FieldValue.serverTimestamp()); // Opcional, para registrar la fecha de la firma

        firestore.collection(pathLista).document(id_lista).update(firmaData2)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al guardar la firma en Firestore
                    //Toast.makeText(getContext(), "Firma guardada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error al guardar la firma en Firestore
                    Toast.makeText(getContext(), "Error al guardar firma", Toast.LENGTH_SHORT).show();
                });
    }
    public void enviarFirma3(Bitmap signature3) {

        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calHoy.getTime());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature3.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        //String id_lista = AgregarListaCargueDescargue.idListaStatic;
        String nombreConductor = edtNombreConductor.getText().toString();
        //String finca = AgregarListaCargueDescargue.listaModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();
        String finca = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();

        // Generar un nombre único para el archivo en el Storage
        String fileName = "firma_conductor_" +  "_" + formattedDate + "_ID_USUARIO:_"+  uid + "_ID_LISTA:_" + id_lista + ".png";
        // Referencia al Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Referencia al archivo en el Firebase Storage
        StorageReference firmaRef = storageRef.child(pathFirmasCargueDescargue + formattedDate + "/" + fileName);

        // Subir la firma al Firebase Storage
        firmaRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    firmaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString(); // Obtener la URL de descarga del archivo
                        guardarFirma3EnFirestore(downloadUrl);  // Guardar la URL de descarga en Firestore
                        firmaConductorStatic = downloadUrl;
                    });
                })
                .addOnFailureListener(exception -> {
                    // Manejar el error
                    Toast.makeText(getContext(), "Error al guardar firma", Toast.LENGTH_SHORT).show();
                });
    }
    private void guardarFirma3EnFirestore(String downloadUrl) {
        // Crear un nuevo documento en la colección 'firmas' en Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> firmaData3 = new HashMap<>();

        firmaData3.put("firma_Conductor", downloadUrl);
        firmaData3.put("timestamp", FieldValue.serverTimestamp()); // Opcional, para registrar la fecha de la firma

        firestore.collection(pathLista).document(id_lista).update(firmaData3)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al guardar la firma en Firestore
                    //Toast.makeText(getContext(), "Firma guardada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al guardar firma", Toast.LENGTH_SHORT).show();
                });
    }
    public void enviarFirma4(Bitmap signature4) {

        Calendar calHoy = Calendar.getInstance();
        calHoy.set(Calendar.HOUR_OF_DAY, 0);
        calHoy.set(Calendar.MINUTE, 0);
        calHoy.set(Calendar.SECOND, 0);
        calHoy.set(Calendar.MILLISECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(calHoy.getTime());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signature4.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        //String id_lista = AgregarListaCargueDescargue.idListaStatic;

        String nombreOperador = edtNombreOperador.getText().toString();
        //String finca = AgregarListaCargueDescargue.listaModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();
        String finca = AgregarMostrarListas.listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();

        // Generar un nombre único para el archivo en el Storage
        String fileName = "firma_operador_" + "_" + formattedDate + "_ID_USUARIO:_"+  uid + "_ID_LISTA:_" + id_lista + ".png";
        // Referencia al Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        // Referencia al archivo en el Firebase Storage
        StorageReference firmaRef = storageRef.child(pathFirmasCargueDescargue + formattedDate + "/" + fileName);

        // Subir la firma al Firebase Storage
        firmaRef.putBytes(data)
                .addOnSuccessListener(taskSnapshot -> {
                    firmaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString(); // Obtener la URL de descarga del archivo
                        guardarFirma4EnFirestore(downloadUrl); // Guardar la URL de descarga en Firestore
                        firmaOperadorStatic = downloadUrl;
                    });
                })
                .addOnFailureListener(exception -> {
                    // Manejar el error
                    Toast.makeText(getContext(), "Error al guardar firma", Toast.LENGTH_SHORT).show();
                });
    }
    private void guardarFirma4EnFirestore(String downloadUrl) {
        // Crear un nuevo documento en la colección 'firmas' en Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        Map<String, Object> firmaData3 = new HashMap<>();

        firmaData3.put("firma_Operador", downloadUrl);
        firmaData3.put("timestamp", FieldValue.serverTimestamp()); // Opcional, para registrar la fecha de la firma

        firestore.collection(pathLista).document(id_lista).update(firmaData3)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al guardar la firma en Firestore
                    //Toast.makeText(getContext(), "Firma guardada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(getContext(), "Error al guardar firma", Toast.LENGTH_SHORT).show();
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

        if (TextUtils.isEmpty(edtNombreDespachador.getText())) {
            showMessage("Debes ingresar el nombre del despachador");
            return false;
        }

        if (TextUtils.isEmpty(edtNombreConductor.getText())) {
            showMessage("Debes ingresar el nombre del conductor");
            return false;
        }

        if (TextUtils.isEmpty(edtNombreOperador.getText())) {
            showMessage("Debes ingresar el nombre del operador de cargue");
            return false;
        }

        if(sPadFirmaSupervisor == null){
            showMessage("Debe firmar el supervisor");
            return false;
        }

        if(sPadFirmaDespachador == null){
            showMessage("Debe firmar el despachador");
            return false;
        }

        if(sPadFirmaConductor == null){
            showMessage("Debe firmar el conductor");
            return false;
        }

        if(sPadFirmaOperadorCargue == null){
            showMessage("Debe firmar el operador de cargue");
            return false;
        }
        return true;
    }

    public interface OnEstadoCamposActualizadoListener5 {
        void onEstadoCamposActualizado5(boolean camposCompletos);
    }
    private OnEstadoCamposActualizadoListener5 listener5;

    private void actualizarEstadoLista() {
        boolean camposCompletos = verificarCampos();
        if (listener5 != null) {
            listener5.onEstadoCamposActualizado5(camposCompletos);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        datosListener5 = null;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EstadoCargueFragment.DatosListener4) {
            datosListener5 = (DatosListener5) context;
        } else {
            throw new RuntimeException("La actividad debe implementar la interfaz DatosListener 4");
        }

        if (context instanceof OnEstadoCamposActualizadoListener5) {
            listener5 = (OnEstadoCamposActualizadoListener5) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEstadoCamposFirmasListener");
        }
    }
}