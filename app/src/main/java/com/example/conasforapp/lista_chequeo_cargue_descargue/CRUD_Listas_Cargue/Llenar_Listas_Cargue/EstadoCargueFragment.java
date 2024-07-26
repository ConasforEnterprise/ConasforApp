package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Llenar_Listas_Cargue;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;

import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.ContentResolver;

import pub.devrel.easypermissions.EasyPermissions;

public class EstadoCargueFragment extends Fragment {
    private View view;
    private TextInputEditText edtHoraSalida;
    private ImageView imgCapturaFoto;
    private Button btnAgregarActualizarDatos, btnTomarFotoCamion, btnSubirFotoCamion;
    private RadioGroup rdgMaderaNoSuperaAlturaMampara,
            rdgMaderaNoSuperaParales, rdgNoMaderaAtravieseMampara,
            rdgParalesMismaAltura, rdgNingunaUndSobrepasaParales,
            rdgCadaBancoAseguradoEslingas, rdgCarroceriaParlesBuenEstado,
            rdgConductorSalioCinturon, rdgParalesAbatiblesAseguradosEstrobos;
    private FirebaseFirestore db;
    private Map<String, Object> datosEstadoCargue = new HashMap<>();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private String pathLista = "Listas chequeo cargue descargue";
    private ListasCargueModel.EstadoDelCargue estadoCargueModel = new ListasCargueModel.EstadoDelCargue();
    private String idListaCreada = AgregarMostrarListas.idListaStatic;
    String base64Image = "";
    String nombre ="";
    String cargo = "";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String uid = currentUser.getUid();
    String pathUsuarios = "Usuarios";
    private static final int CODE_GALERY = 500;
    String respuestMaderaNoSuperaAlturaMampara,respuestaMaderaNoSuperaParales, respuestaNoMaderaAtravieseMampara,
            respuestaParalesMismaAltura,respuestaParalesNingunaUndSobrepasaParales,respuestaCadaBancoAseguradoEslingas,
            respuestaCarroceriaParlesBuenEstado,respuestaConductorSalioCinturon,respuestaParalesAbatiblesAseguradosEstrobos;

    private DatosListener4 datosListener4;
    private String idRecibido = null;
    private String horaRecibido  = null;
    private String fechaRecibido  = null;
    private String tipoCargueRecibido  = null;
    private String zonaRecibido  = null;
    private String nucleoRecibido  = null;
    private String fincaRecibido  = null;
    private String nombreConductorRecibido, cedulaRecibido, lugarExpedicionRecibido, licConduccionRecibido,
            polizaRCERecibido,epsResRecibido,arlResRecibido,afpResRecibido, cualEpsRecibido,cualArlRecibido,
            cualAfpRecibido;
    private String placaRecibido,vehiculoRecibido, tarjetaPropiedadRecibido,soatRecibido,revisionTecnicomecanicaRecibido,
            lucesAltasRecibido,lucesBajasRecibido,direccionalesRecibido, sonidoReversaRecibido,reversaRecibido,stopRecibido,
            retrovisoresRecibido,plumillasRecibido,estadoPanoramicosRecibido,espejosRecibido,bocinaRecibido,cinturonRecibido,
            frenoRecibido,llantasRecibido,botiquinRecibido,extintorABCRecibido,botasRecibido,chalecoRecibido,cascoRecibido,
            carroceriaRecibido,dosEslingasBancoRecibido,dosConosReflectivosRecibido, paralesRecibido, observacionesRecibido;

    Uri urlImagen;
    private ListasCargueDataBaseHelper dbLocal;
    public static String image;
    Boolean estadoBtn = false;

    public EstadoCargueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_estado_cargue, container, false);

        edtHoraSalida = view.findViewById(R.id.edtHoraSalidaSitio);
        imgCapturaFoto = view.findViewById(R.id.imgFotoCamion);
        btnTomarFotoCamion  = view.findViewById(R.id.btnTomarFotoCamion);
        btnSubirFotoCamion = view.findViewById(R.id.btnSubirFotoCamion);
        btnAgregarActualizarDatos = view.findViewById(R.id.btnAgregarActualizarEstadoCargue);

        db = FirebaseFirestore.getInstance();
        dbLocal = new ListasCargueDataBaseHelper(getContext());

        rdgMaderaNoSuperaAlturaMampara = view.findViewById(R.id.rdgMaderaNoSuperaAlturaMampara);
        rdgMaderaNoSuperaParales = view.findViewById(R.id.rdgMaderaNoSuperaParales);
        rdgNoMaderaAtravieseMampara = view.findViewById(R.id.rdgMaderaAtravieseMampara);
        rdgParalesMismaAltura = view.findViewById(R.id.rdgParalesMismaAltura);
        rdgNingunaUndSobrepasaParales = view.findViewById(R.id.rdgNingunaUndSobrepasaParales);
        rdgCadaBancoAseguradoEslingas = view.findViewById(R.id.rdgCadaBancoAseguradoEslingas);
        rdgCarroceriaParlesBuenEstado = view.findViewById(R.id.rdgCarroceriaParalesBuenEstado);
        rdgConductorSalioCinturon = view.findViewById(R.id.rdgConductorSalioCinturon);
        rdgParalesAbatiblesAseguradosEstrobos = view.findViewById(R.id.rdgParalesAbatiblesAseguradosEstrobos);

        btnTomarFotoCamion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (EasyPermissions.hasPermissions(getContext(), android.Manifest.permission.CAMERA)) {
                    Intent open_camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(open_camara, 100);
                    Toast.makeText(getContext(), "Estas intentando acceder a la cámara", Toast.LENGTH_SHORT).show();
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

        btnSubirFotoCamion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFoto();
            }
        });
        edtHoraSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                abrirHora(v);
            }
        });

        btnAgregarActualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estadoBtn = true;
                if(isNetworkAvailable()){
                    if(idListaCreada != null){
                        agregarDatosFirestore();
                    }
                }
                else {
                    agregarDatosBDLocal(image);
                }
            }
        });

        obtenerDatosUsuario();

        return view;
    }

    public interface DatosListener4{
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
                             String firmaSupervisor, String firmaDespachador, String firmaConductor, String firmaOperador);  //,byte[] fotoCamion);
    }

    public void recibirDatos4(String id, String horaEntrada, String fecha, String tipoCargue, String zona, String nucleo,
                              String finca, String nombreConductor, String cedula, String lugarExpedicion, String licConduccion,
                              String polizaRCE, String epsRes, String arlRes, String afpRes, String cualEps, String cualArl, String cualAfp,
                              String placa,String vehiculo,String tarjetaPropiedad, String soat, String revisionTecnicomecanica,
                              String lucesAltas, String lucesBajas, String direccionales, String sonidoReversa, String reversa, String stop,
                              String retrovisores, String plumillas,String estadoPanoramicos, String espejos, String bocina, String cinturon,
                              String freno, String llantas,String botiquin, String extintorABC, String botas, String chaleco, String casco,
                              String carroceria,String dosEslingasBanco, String dosConosReflectivos, String parales, String observaciones){

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
    }

    interface DatosListener5 {
        void recibirDatos5(String id, String horaEntrada, String fecha, String tipoCargue, String zona, String nucleo,
                           String finca, String nombreConductor, String cedula, String lugarExpedicion, String licConduccion,
                           String polizaRCE, String epsRes, String arlRes, String afpRes, String cualEps, String cualArl, String cualAfp,
                           String placa, String vehiculo, String tarjetaPropiedad, String soat, String revisionTecnicomecanica,
                           String lucesAltas, String lucesBajas, String direccionales, String sonidoReversa, String reversa, String stop,
                           String retrovisores, String plumillas, String estadoPanoramicos, String espejos, String bocina, String cinturon,
                           String freno, String llantas, String botiquin, String extintorABC, String botas, String chaleco, String casco,
                           String carroceria, String dosEslingasBanco, String dosConosReflectivos, String parales, String observaciones,
                           String horaSalida, String respuestMaderaNoSuperaAlturaMampara, String respuestaMaderaNoSuperaParales,
                           String respuestaNoMaderaAtravieseMampara, String respuestaParalesMismaAltura, String respuestaParalesNingunaUndSobrepasaParales,
                           String respuestaCadaBancoAseguradoEslingas, String respuestaCarroceriaParlesBuenEstado,
                           String respuestaConductorSalioCinturon, String respuestaParalesAbatiblesAseguradosEstrobos,
                           String imagenCamion);
    }

    private void cargarDatosParaSuabirEstadoCargue(){

        int rdgMaderaNoSuperaAlturaMamparaId = rdgMaderaNoSuperaAlturaMampara.getCheckedRadioButtonId();
        int rdgMaderaNoSuperaParalesId = rdgMaderaNoSuperaParales.getCheckedRadioButtonId();
        int rdgNoMaderaAtravieseMamparaId = rdgNoMaderaAtravieseMampara.getCheckedRadioButtonId();
        int rdgParalesMismaAlturaId = rdgParalesMismaAltura.getCheckedRadioButtonId();
        int rdgNingunaUndSobrepasaParalesId = rdgNingunaUndSobrepasaParales.getCheckedRadioButtonId();
        int rdgCadaBancoAseguradoEslingasId = rdgCadaBancoAseguradoEslingas.getCheckedRadioButtonId();
        int rdgCarroceriaParlesBuenEstadoId = rdgCarroceriaParlesBuenEstado.getCheckedRadioButtonId();
        int rdgConductorSalioCinturonId = rdgConductorSalioCinturon.getCheckedRadioButtonId();
        int rdgParalesAbatiblesAseguradosEstrobosId = rdgParalesAbatiblesAseguradosEstrobos.getCheckedRadioButtonId();

        // Verificar qué RadioButton fue seleccionado y actualizar el modelo
        if (rdgMaderaNoSuperaAlturaMamparaId == R.id.rbSiMaderaNoSuperaAlturaMampara) {
            estadoCargueModel.setMaderaNoSuperaMampara("Si");

        } else if (rdgMaderaNoSuperaAlturaMamparaId == R.id.rbNoMaderaNoSuperaAlturaMampara) {
            estadoCargueModel.setMaderaNoSuperaMampara("No");
        }

        if (rdgMaderaNoSuperaParalesId == R.id.rbSiMaderaNoSuperaParales) {
            estadoCargueModel.setMaderaNoSuperaParales("Si");


        } else if (rdgMaderaNoSuperaParalesId == R.id.rbNoMaderaNoSuperaParales) {
            estadoCargueModel.setMaderaNoSuperaParales("No");
        }

        if (rdgNoMaderaAtravieseMamparaId == R.id.rbSiMaderaAtravieseMampara) {
            estadoCargueModel.setNoMaderaAtraviesaMampara("Si");

        } else if (rdgNoMaderaAtravieseMamparaId == R.id.rbNoMaderaAtravieseMampara) {
            estadoCargueModel.setNoMaderaAtraviesaMampara("No");
        }

        if (rdgParalesMismaAlturaId == R.id.rbSiParalesMismaAltura) {
            estadoCargueModel.setParalesMismaAltura("Si");

        } else if (rdgParalesMismaAlturaId == R.id.rbNoParalesMismaAltura) {
            estadoCargueModel.setParalesMismaAltura("No");
        }

        if (rdgNingunaUndSobrepasaParalesId == R.id.rbSiNingunaUndSobrepasaParales) {
            estadoCargueModel.setNingunaUndSobrepasaParales("Si");

        } else if (rdgNingunaUndSobrepasaParalesId == R.id.rbNoNingunaUndSobrepasaParales) {
            estadoCargueModel.setNingunaUndSobrepasaParales("No");
        }

        if (rdgCadaBancoAseguradoEslingasId == R.id.rbSiCadaBancoAseguradoEslingas) {
            estadoCargueModel.setCadaBancoAseguradoEslingas("Si");

        } else if (rdgCadaBancoAseguradoEslingasId == R.id.rbNoCadaBancoAseguradoEslingas) {
            estadoCargueModel.setCadaBancoAseguradoEslingas("No");
        }

        if (rdgCarroceriaParlesBuenEstadoId == R.id.rbSiCarroceriaParalesBuenEstado) {
            estadoCargueModel.setCarroceriaParalesBuenEstado("Si");

        } else if (rdgCarroceriaParlesBuenEstadoId == R.id.rbNoCarroceriaParalesBuenEstado) {
            estadoCargueModel.setCarroceriaParalesBuenEstado("No");
        }

        if (rdgConductorSalioCinturonId == R.id.rbSiConductorSalioCinturon) {
            estadoCargueModel.setConductorSalioLugarCinturon("Si");

        } else if (rdgConductorSalioCinturonId  == R.id.rbNoConductorSalioCinturon) {
            estadoCargueModel.setConductorSalioLugarCinturon("No");
        }

        if (rdgParalesAbatiblesAseguradosEstrobosId == R.id.rbSiParalesAbatiblesAseguradosEstrobos) {
            estadoCargueModel.setParalesAbatiblesAseguradosEstrobos("Si");

        } else if (rdgParalesAbatiblesAseguradosEstrobosId  == R.id.rbNoParalesAbatiblesAseguradosEstrobos) {
            estadoCargueModel.setParalesAbatiblesAseguradosEstrobos("No");
        }

        respuestMaderaNoSuperaAlturaMampara = estadoCargueModel.getMaderaNoSuperaMampara();
        respuestaMaderaNoSuperaParales = estadoCargueModel.getMaderaNoSuperaParales();
        respuestaNoMaderaAtravieseMampara = estadoCargueModel.getNoMaderaAtraviesaMampara();
        respuestaParalesMismaAltura = estadoCargueModel.getParalesMismaAltura();
        respuestaParalesNingunaUndSobrepasaParales = estadoCargueModel.getNingunaUndSobrepasaParales();
        respuestaCadaBancoAseguradoEslingas = estadoCargueModel.getCadaBancoAseguradoEslingas();
        respuestaCarroceriaParlesBuenEstado = estadoCargueModel.getCarroceriaParalesBuenEstado();
        respuestaConductorSalioCinturon = estadoCargueModel.getConductorSalioLugarCinturon();
        respuestaParalesAbatiblesAseguradosEstrobos = estadoCargueModel.getParalesAbatiblesAseguradosEstrobos();

        datosEstadoCargue.put("horaSalida",edtHoraSalida.getText().toString());
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
    }

    @SuppressLint("LongLogTag")
    private void agregarDatosFirestore(){

        cargarDatosParaSuabirEstadoCargue();

        Log.d("TAG ID LISTA CREADA EN ESTADO DE CARGUE","ID LISTA CREADA EN ESTADO DE CARGUE : "+ idListaCreada);

        if(isNetworkAvailable()){
            if(idListaCreada != null ){
                db.collection(pathLista)
                        .document(idListaCreada)
                        .update("Item_4_Estado_cargue", datosEstadoCargue).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Datos almacenados correctamente", Toast.LENGTH_SHORT).show();
                                actualizarEstadoLista();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "No se almacenaron los datos", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void agregarDatosBDLocal(String imagenCamion){
        actualizarEstadoLista();

        String horaSalida = edtHoraSalida.getText().toString();
        cargarDatosParaSuabirEstadoCargue();

        if (requireActivity() instanceof DatosListener5) {
            ((DatosListener5) requireActivity()).recibirDatos5(idRecibido,horaRecibido,fechaRecibido, tipoCargueRecibido,zonaRecibido,
                    nucleoRecibido,fincaRecibido,nombreConductorRecibido,cedulaRecibido,lugarExpedicionRecibido,licConduccionRecibido,
                    polizaRCERecibido,epsResRecibido,arlResRecibido,afpResRecibido, cualEpsRecibido,cualArlRecibido,cualAfpRecibido,
                    placaRecibido,vehiculoRecibido,tarjetaPropiedadRecibido,soatRecibido, revisionTecnicomecanicaRecibido,
                    lucesAltasRecibido,lucesBajasRecibido,direccionalesRecibido,sonidoReversaRecibido,reversaRecibido,stopRecibido,
                    reversaRecibido,plumillasRecibido,estadoPanoramicosRecibido,espejosRecibido,bocinaRecibido,cinturonRecibido,
                    frenoRecibido,llantasRecibido,botiquinRecibido,extintorABCRecibido,botasRecibido,chalecoRecibido,cascoRecibido,
                    carroceriaRecibido,dosEslingasBancoRecibido,dosConosReflectivosRecibido,paralesRecibido,observacionesRecibido,
                    horaSalida,respuestMaderaNoSuperaAlturaMampara,respuestaMaderaNoSuperaParales,respuestaNoMaderaAtravieseMampara,
                    respuestaParalesMismaAltura,respuestaParalesNingunaUndSobrepasaParales,respuestaCadaBancoAseguradoEslingas,
                    respuestaCarroceriaParlesBuenEstado,respuestaConductorSalioCinturon, respuestaParalesAbatiblesAseguradosEstrobos,
                    imagenCamion);
        }

        if (datosListener4 != null) {
            datosListener4.onDatosEnviados(idRecibido, horaRecibido, fechaRecibido, tipoCargueRecibido, zonaRecibido, nucleoRecibido, fincaRecibido,
                    nombreConductorRecibido,cedulaRecibido,lugarExpedicionRecibido,licConduccionRecibido,polizaRCERecibido,epsResRecibido,
                    arlResRecibido,afpResRecibido,cualEpsRecibido,cualArlRecibido,cualAfpRecibido,placaRecibido,vehiculoRecibido,tarjetaPropiedadRecibido,
                    soatRecibido,revisionTecnicomecanicaRecibido,lucesAltasRecibido,lucesBajasRecibido,direccionalesRecibido,sonidoReversaRecibido,
                    reversaRecibido,stopRecibido, retrovisoresRecibido,plumillasRecibido,estadoPanoramicosRecibido,espejosRecibido,bocinaRecibido,
                    cinturonRecibido,frenoRecibido,llantasRecibido,botiquinRecibido,extintorABCRecibido,botasRecibido,chalecoRecibido,cascoRecibido,
                    carroceriaRecibido,dosEslingasBancoRecibido,dosConosReflectivosRecibido,paralesRecibido,observacionesRecibido,
                    horaSalida,respuestMaderaNoSuperaAlturaMampara,respuestaMaderaNoSuperaParales,respuestaNoMaderaAtravieseMampara,respuestaParalesMismaAltura,
                    respuestaParalesNingunaUndSobrepasaParales,respuestaCadaBancoAseguradoEslingas,respuestaCarroceriaParlesBuenEstado,respuestaConductorSalioCinturon,
                    respuestaParalesAbatiblesAseguradosEstrobos,imagenCamion,"","","","",
                    "","","","");
            Toast.makeText(getContext(), "Datos almacenados correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerDatosUsuario(){
        if (currentUser != null) {
            // El usuario está autenticado
            uid = currentUser.getUid();
            db.collection(pathUsuarios).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UsuariosModel usuariosModel = document.toObject(UsuariosModel.class);
                            nombre = usuariosModel.getNombre();
                            cargo = usuariosModel.getCargo();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //--------------------------------------------------------- IMAGEN DEL CAMION -------------------------------------//
    private void cargarFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK); //MediaStore.Images.Media.EXTERNAL_CONTENT_URI   ---
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),CODE_GALERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //OBTIENE LA IMAGEN AL TOMAR FOTO
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if (photo != null) {
                imgCapturaFoto.setImageBitmap(photo);
                base64Image = convertBitmapToBase64(photo); // Convierte la imagen a base64
                image = base64Image;
                if(isNetworkAvailable()){
                    uploadImageToStorage(base64Image);// Sube la imagen a Firestore
                }
                Toast.makeText(getContext(), "Se obtuvo la imagen correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No se pudo obtener la imagen de la cámara", Toast.LENGTH_SHORT).show();
            }
        }

        //CARGA LA IMAGEN DESDE GALERIA DEL CELULAR
        else if(requestCode == CODE_GALERY && resultCode == RESULT_OK && data != null){

            Calendar calHoy = Calendar.getInstance();
            calHoy.set(Calendar.HOUR_OF_DAY, 0);
            calHoy.set(Calendar.MINUTE, 0);
            calHoy.set(Calendar.SECOND, 0);
            calHoy.set(Calendar.MILLISECOND, 0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(calHoy.getTime());

            urlImagen = data.getData();
            imgCapturaFoto.setImageURI(urlImagen);

            pathFotoCamion = "foto_camión_" + nombre + "_" + cargo + "_" + formattedDate + "_" + "ID_Usuario:_" +uid + "_" +"ID_Lista:_"+ idListaCreada + "_" + ".jpg";

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            // Referencia al archivo en el Firebase Storage
            StorageReference imagenRef = storageRef.child("Fotos Camion Cargue Descargue/" + formattedDate + "/" + pathFotoCamion);

            if(isNetworkAvailable()){
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
                                    datosEstadoCargue.put("fotoCamion",downloadUri);
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Ocurrió un error inesperado", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                String base64Image = "";
                try {
                    InputStream inputStream = getContext().getContentResolver().openInputStream(urlImagen);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    // Escalar el bitmap
                    int maxSize = 1024; // Tamaño máximo de 1024x1024 píxeles
                    bitmap = scaleBitmap(bitmap, maxSize);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream); // Reducir calidad a 80%
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    Log.d("BASE64", "BASE64 : " + base64Image);
                    image = base64Image;
                    Log.d("IMAGE FOTO CARGADA", "IMAGE FOTO CARGADA : " + image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Método para reducir el tamaño de la imagen que se carga de la galería antes de subirse a la base de datos
    private Bitmap scaleBitmap(Bitmap bitmap, int maxSize) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scale = Math.min((float) maxSize / width, (float) maxSize / height);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }



    // Método para convertir un Bitmap a base64 (puedes ajustar esto según tus necesidades)
    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private String pathFotoCamion; // Variable de instancia para almacenar el nombre de archivo

    //SUBE LA IMAGEN AL STORAGE DE FIREBASE
    private void uploadImageToStorage(String base64Image) {

        if (pathFotoCamion == null) {
            Calendar calHoy = Calendar.getInstance();
            calHoy.set(Calendar.HOUR_OF_DAY, 0);
            calHoy.set(Calendar.MINUTE, 0);
            calHoy.set(Calendar.SECOND, 0);
            calHoy.set(Calendar.MILLISECOND, 0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(calHoy.getTime());

            pathFotoCamion = "foto_camión_" + nombre + "_" + cargo + "_" + formattedDate + "_" + "ID_Usuario:_" +uid + "_" +"ID_Lista:_"+ idListaCreada + "_" + ".jpg";

            // Convierte la cadena Base64 de nuevo a datos binarios
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            // Referencia al archivo en el Firebase Storage
            StorageReference imagenRef = storageRef.child("Fotos Camion Cargue Descargue/" + formattedDate + "/" + pathFotoCamion);

            UploadTask uploadTask = imagenRef.putBytes(decodedBytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Obtiene la URL de la imagen almacenada en Firebase Storage
                    imagenRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                        // Almacena la URL en Firestore
                        uploadImageUrlToFirestore(downloadUri.toString());
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

    //SUBE LA IMAGEN A LA COLECCION DE LAS LISTAS
    private void uploadImageUrlToFirestore(String imageUrl) {
        DocumentReference docRef = db.collection(pathLista).document(idListaCreada);
        datosEstadoCargue.put("fotoCamion", imageUrl);
        docRef.update("Item_4_Estado_cargue",datosEstadoCargue).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Imagen cargada", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMessage(String msj) {
        Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show();
    }

    private boolean verificarCampos() {

        if (TextUtils.isEmpty(edtHoraSalida.getText())) {
            showMessage("Debes ingresar la hora de salida del vehículo");
            return false;
        }

        if (rdgMaderaNoSuperaAlturaMampara.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en La madera no supera la altura de la mampara");
            return false;
        }

        if (rdgMaderaNoSuperaParales.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en La madera no supera ninguno de los parales");
            return false;
        }

        if (rdgNoMaderaAtravieseMampara.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en No hay madera que atraviese la mampara");
            return false;
        }

        if (rdgParalesMismaAltura.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en Los parales tienen la misma altura");
            return false;
        }
        if (rdgNingunaUndSobrepasaParales.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en Ninguna de las unidades de madera sobrepasa lateralmente los parales");
            return false;
        }

        if (rdgCadaBancoAseguradoEslingas.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en Cada banco está asegurado con 2 eslingas");
            return false;
        }
        if (rdgCarroceriaParlesBuenEstado.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en La carrocería y los parales están en buen estado y sin signos de golpes");
            return false;
        }
        if (rdgConductorSalioCinturon.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en EL conductor salió del lugar utilizando el cinturón");
            return false;
        }
        if (rdgParalesAbatiblesAseguradosEstrobos.getCheckedRadioButtonId() == -1) {
            showMessage("Debes seleccionar una opción en Los parales abatibles se encuentran asegurados con estrobos");
            return false;
        }

        if(imgCapturaFoto == null){
            showMessage("Debes tomar o subir la foto del camión");
            return false;
        }
        return true;
    }

    public interface OnEstadoCamposActualizadoListener4 {
        void onEstadoCamposActualizado4(boolean camposCompletos);
    }

    private OnEstadoCamposActualizadoListener4 listener4;

    private void actualizarEstadoLista() {
        boolean camposCompletos = verificarCampos();

        if (listener4 != null) {
            listener4.onEstadoCamposActualizado4(camposCompletos);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        datosListener4 = null;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DatosListener4) {
            datosListener4 = (DatosListener4) context;
        } else {
            throw new RuntimeException("La actividad debe implementar la interfaz DatosListener 4");
        }

        if (context instanceof OnEstadoCamposActualizadoListener4) {
            listener4 = (OnEstadoCamposActualizadoListener4) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEstadoCamposEstadoCargueListener");
        }
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
}