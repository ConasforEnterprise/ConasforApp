package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Llenar_Listas_Cargue;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoVehiculoFragment extends Fragment {
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
    private Button btnAgregarDatosInfoVehiculo;
    Map<String, String> datosInfoVehiculo = new HashMap<>();
    private String pathLista = "Listas chequeo cargue descargue";
    private ListasCargueModel.InfoDelVehiculo infoVehiculoModel = new ListasCargueModel.InfoDelVehiculo();
    private DatosListener3 datosListener3;
    private String idRecibido = null, horaRecibido = null, fechaRecibido = null, tipoCargueRecibido = null,
            zonaRecibido = null, nucleoRecibido = null, fincaRecibido = null;
    private String nombreConductorRecibido, cedulaRecibido, lugarExpedicionRecibido, licConduccionRecibido,
            polizaRCERecibido, epsResRecibido, arlResRecibido, afpResRecibido, cualEpsRecibido,
            cualArlRecibido, cualAfpRecibido, placaRecibido, vehiculoRecibido;
    private final String respuestaBien = "Bien";
    private final String respuestaMal = "Mal";
    private final String respuestaSi = "Si";
    private final String respuestaNo = "No";
    public InfoVehiculoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info_vehiculo, container, false);

        edtPlaca = view.findViewById(R.id.edtPlaca);
        edtVehiculo = view.findViewById(R.id.edtVehiculo);
        edtObservacionesInfoVehiculo = view.findViewById(R.id.edtObservacionesInfoVehiculo);

        db = FirebaseFirestore.getInstance();

        rdgTarjetaPropiedad = view.findViewById(R.id.rdgTarjetaPropiedad);
        rdgSoat = view.findViewById(R.id.rdgSOATVigente);
        rdgRevisionTecnicomecanica = view.findViewById(R.id.rdgRevisionTecnicomecanica);
        rdgLucesAltas = view.findViewById(R.id.rdgLucesAltas);
        rdgLucesBajas = view.findViewById(R.id.rdgLucesBajas);
        rdgDireccionales = view.findViewById(R.id.rdgDireccionales);
        rdgSonidoReversa = view.findViewById(R.id.rdgSonidoReversa);
        rdgReversa = view.findViewById(R.id.rdgReversa);
        rdgStop = view.findViewById(R.id.rdgStop);
        rdgRetrovisores = view.findViewById(R.id.rdgRetrovisores);
        rdgPlumillas = view.findViewById(R.id.rdgPlumillas);
        rdgEstadoPanoramicos = view.findViewById(R.id.rdgEstadoPanoramicos);
        rdgEspejos = view.findViewById(R.id.rdgEspejos);
        rdgBocina = view.findViewById(R.id.rdgBocina);
        rdgCinturon = view.findViewById(R.id.rdgCinturon);
        rdgFreno = view.findViewById(R.id.rdgFreno);
        rdgLlantas = view.findViewById(R.id.rdgLlantas);
        rdgBotiquin = view.findViewById(R.id.rdgBotiquin);
        rdgExtintorABC = view.findViewById(R.id.rdgExtintorABC);
        rdgBotas = view.findViewById(R.id.rdgBotas);
        rdgChaleco = view.findViewById(R.id.rdgChaleco);
        rdgCasco = view.findViewById(R.id.rdgCasco);
        rdgCarroceria = view.findViewById(R.id.rdgCarroceria);
        rdgDosEslingasPorBanco = view.findViewById(R.id.rdgDosEslingasBanco);
        rdgDosConosReflectivos = view.findViewById(R.id.rdgDosConosReflectivos);
        rdgParales = view.findViewById(R.id.rdgParalesIgualLongitud);
        edtObservacionesInfoVehiculo = view.findViewById(R.id.edtObservacionesInfoVehiculo);

        btnAgregarDatosInfoVehiculo = view.findViewById(R.id.btnAgregarDatosInfoVehiculo);
        btnAgregarDatosInfoVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarDatosInfoVehiculo();
            }
        });

        obtenerDatosInfoVehiculo();

        return view;
    }

    public interface DatosListener3 {
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
                             String conductorSalioLugarCinturon, String paralesAbatiblesAseguradosEstrobos, String fotoCamion,
                             String nombreSupervisorFirma, String nombreDespachadorFirma, String nombreConductorFirma, String nombreOperadorFirma,
                             String firmaSupervisor, String firmaDespachador, String firmaConductor, String firmaOperador);
    }

    interface DatosListener4 {
        void recibirDatos4(String id, String horaEntrada, String fecha, String tipoCargue, String zona, String nucleo,
                           String finca, String nombreConductor, String cedula, String lugarExpedicion, String licConduccion,
                           String polizaRCE, String epsRes, String arlRes, String afpRes, String cualEps, String cualArl, String cualAfp,
                           String placa, String vehiculo, String tarjetaPropiedad, String soat, String revisionTecnicomecanica,
                           String lucesAltas, String lucesBajas, String direccionales, String sonidoReversa, String reversa, String stop,
                           String retrovisores, String plumillas, String estadoPanoramicos, String espejos, String bocina, String cinturon,
                           String freno, String llantas, String botiquin, String extintorABC, String botas, String chaleco, String casco,
                           String carroceria, String dosEslingasBanco, String dosConosReflectivos, String parales, String observaciones);
    }

    public void recibirDatos3(String id, String horaEntrada, String fecha, String tipoCargue, String zona, String nucleo, String finca, String nombreConductor, String cedula, String lugarExpedicion, String licConduccion, String polizaRCE, String epsRes, String arlRes, String afpRes, String cualEps, String cualArl, String cualAfp) {
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

        /*
        Log.d("ID RECIBIDO ", "ID RECIBIDO INFO CONDUCTOR F3: " + idRecibido);
        Log.d("HORA RECIBIDA", "HORA RECIBIDA F3: " + horaRecibido);
        Log.d("FECHA RECIBIDA", "FECHA RECIBIDA F3: " + fechaRecibido);
        Log.d("TIPO CARGUE RECIBIDO", "TIPO CARGUE RECIBIDO F3: " + tipoCargueRecibido);
        Log.d("ZONA RECIBIDA", "ZONA RECIBIDA F3: " + zonaRecibido);
        Log.d("NUCLEO RECIBIDO", "NUCLEO RECIBIDO F3: " + nucleoRecibido);
        Log.d("CONDUCTOR RECIBIDA", "CONDUCTOR RECIBIDA F3: " + nombreConductorRecibido);
        Log.d("CEDULA RECIBIDA", "CEDULA RECIBIDA F3: " + cedulaRecibido);
        Log.d("LUGAR EXPEDICION RECIBIDA", "FINCA RECIBIDA F3: " + lugarExpedicionRecibido);
        Log.d("LIC CONDUCCION RECIBIDA", "LUGAR EXPEDICION RECIBIDA F3: " + licConduccionRecibido);
        Log.d("POLIZA RECIBIDA", "POLIZA RECIBIDA F3: " + polizaRCERecibido);
        Log.d("EPS RES RECIBIDA", "EPS RES RECIBIDA F3: " + epsResRecibido);
        Log.d("ARL RES RECIBIDA", "ARL RES RECIBIDA F3: " + arlResRecibido);
        Log.d("AFP RES RECIBIDA", "AFP RES RECIBIDA F3: " + afpResRecibido);
        Log.d("CUAL EPS RECIBIDA", "CUAL EPSRECIBIDA F3: " + cualEpsRecibido);
        Log.d("CUAL ARL RECIBIDA", "CUAL ARL RECIBIDA F3: " + cualArlRecibido);
        Log.d("CUAL AFP RECIBIDA", "CUAL AFP RECIBIDA F3: " + cualAfpRecibido);

         */
    }

    private void agregarDatosInfoVehiculo() {

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

        if (rdgTarjetaPropiedadId == R.id.rbSiTarjetaPropiedad) {
            infoVehiculoModel.setTarjetaPropiedad(respuestaSi);

        } else if (rdgTarjetaPropiedadId == R.id.rbNoTarjetaPropiedad) {
            infoVehiculoModel.setTarjetaPropiedad(respuestaNo);
        }

        if (rdgSoatId == R.id.rbSiSOATVigente) {
            infoVehiculoModel.setSoatVigente(respuestaSi);

        } else if (rdgSoatId == R.id.rbNoSOATVigente) {
            infoVehiculoModel.setSoatVigente(respuestaNo);
        }

        if (rdgRevisionTecnicomecanicaId == R.id.rbSiRevisionTecnicomecanica) {
            infoVehiculoModel.setRevisionTecnicomecanica(respuestaSi);

        } else if (rdgRevisionTecnicomecanicaId == R.id.rbNoRevisionTecnicomecanica) {
            infoVehiculoModel.setRevisionTecnicomecanica(respuestaNo);
        }

        if (rdgLucesAltasId == R.id.rbBienLucesAltas) {
            infoVehiculoModel.setLucesAltas(respuestaBien);

        } else if (rdgLucesAltasId == R.id.rbMalLucesAltas) {
            infoVehiculoModel.setLucesAltas(respuestaMal);
        }

        if (rdgLucesBajasId == R.id.rbBienLucesBajas) {
            infoVehiculoModel.setLucesBajas(respuestaBien);

        } else if (rdgLucesBajasId == R.id.rbMalLucesBajas) {
            infoVehiculoModel.setLucesBajas(respuestaMal);
        }

        if (rdgDireccionalesId == R.id.rbBienDireccionales) {
            infoVehiculoModel.setDireccionales(respuestaBien);

        } else if (rdgDireccionalesId == R.id.rbMalDireccionales) {
            infoVehiculoModel.setDireccionales(respuestaMal);
        }

        if (rdgSonidoReversaId == R.id.rbBienSonidoReversa) {
            infoVehiculoModel.setSonidoReversa(respuestaBien);

        } else if (rdgSonidoReversaId == R.id.rbMalSonidoReversa) {
            infoVehiculoModel.setSonidoReversa(respuestaMal);
        }

        if (rdgReversaId == R.id.rbBienReversa) {
            infoVehiculoModel.setReversa(respuestaBien);

        } else if (rdgReversaId == R.id.rbMalReversa) {
            infoVehiculoModel.setReversa(respuestaMal);
        }

        if (rdgStopId == R.id.rbBienStop) {
            infoVehiculoModel.setStop(respuestaBien);

        } else if (rdgStopId == R.id.rbMalStop) {
            infoVehiculoModel.setStop(respuestaMal);
        }

        if (rdgRetrovisoresId == R.id.rbBienRetrovisores) {
            infoVehiculoModel.setRetrovisores(respuestaBien);

        } else if (rdgRetrovisoresId == R.id.rbMalRetrovisores) {
            infoVehiculoModel.setRetrovisores(respuestaMal);
        }

        if (rdgPlumillasId == R.id.rbBienPlumillas) {
            infoVehiculoModel.setPlumillas(respuestaBien);

        } else if (rdgPlumillasId == R.id.rbMalPlumillas) {
            infoVehiculoModel.setPlumillas(respuestaMal);
        }

        if (rdgEstadoPanoramicosId == R.id.rbBienEstadoPanoramicos) {
            infoVehiculoModel.setEstadoPanoramicos(respuestaBien);

        } else if (rdgEstadoPanoramicosId == R.id.rbMalEstadoPanoramicos) {
            infoVehiculoModel.setEstadoPanoramicos(respuestaMal);
        }

        if (rdgEspejosId == R.id.rbBienEspejos) {
            infoVehiculoModel.setEspejos(respuestaBien);

        } else if (rdgEspejosId == R.id.rbMalEspejos) {
            infoVehiculoModel.setEspejos(respuestaMal);
        }

        if (rdgBocinaId == R.id.rbBienBocina) {
            infoVehiculoModel.setBocina(respuestaBien);

        } else if (rdgBocinaId == R.id.rbMalBocina) {
            infoVehiculoModel.setBocina(respuestaMal);
        }

        if (rdgCinturonId == R.id.rbBienCinturon) {
            infoVehiculoModel.setCinturon(respuestaBien);

        } else if (rdgCinturonId == R.id.rbMalCinturon) {
            infoVehiculoModel.setCinturon(respuestaMal);
        }

        if (rdgFrenoId == R.id.rbBienFreno) {
            infoVehiculoModel.setFreno(respuestaBien);

        } else if (rdgFrenoId == R.id.rbMalFreno) {
            infoVehiculoModel.setFreno(respuestaMal);
        }

        if (rdgLlantasId == R.id.rbBienLlantas) {
            infoVehiculoModel.setLlantas(respuestaBien);

        } else if (rdgLlantasId == R.id.rbMalLlantas) {
            infoVehiculoModel.setLlantas(respuestaMal);
        }

        if (rdgBotiquinId == R.id.rbBienBotiquin) {
            infoVehiculoModel.setBotiquin(respuestaBien);

        } else if (rdgBotiquinId == R.id.rbMalBotiquin) {
            infoVehiculoModel.setBotiquin(respuestaMal);
        }

        if (rdgExtintorABCId == R.id.rbBienExtintorABC) {
            infoVehiculoModel.setExtintorABC(respuestaBien);

        } else if (rdgExtintorABCId == R.id.rbMalExtintorABC) {
            infoVehiculoModel.setExtintorABC(respuestaMal);
        }

        if (rdgBotasId == R.id.rbBienBotas) {
            infoVehiculoModel.setBotas(respuestaBien);

        } else if (rdgBotasId == R.id.rbMalBotas) {
            infoVehiculoModel.setBotas(respuestaMal);
        }

        if (rdgChalecoId == R.id.rbBienChaleco) {
            infoVehiculoModel.setChaleco(respuestaBien);

        } else if (rdgChalecoId == R.id.rbMalChaleco) {
            infoVehiculoModel.setChaleco(respuestaMal);
        }

        if (rdgCascoId == R.id.rbBienCasco) {
            infoVehiculoModel.setCasco(respuestaBien);

        } else if (rdgCascoId == R.id.rbMalCasco) {
            infoVehiculoModel.setCasco(respuestaMal);
        }

        if (rdgCarroceriaId == R.id.rbBienCarroceria) {
            infoVehiculoModel.setCarroceria(respuestaBien);

        } else if (rdgCarroceriaId == R.id.rbMalCarroceria) {
            infoVehiculoModel.setCarroceria(respuestaMal);
        }

        if (rdgDosEslingasPorBancoId == R.id.rbBienDosEslingasBanco) {
            infoVehiculoModel.setDosEslingasBanco(respuestaBien);

        } else if (rdgDosEslingasPorBancoId == R.id.rbMalDosEslingasBanco) {
            infoVehiculoModel.setDosEslingasBanco(respuestaMal);
        }

        if (rdgDosConosReflectivosId == R.id.rbBienDosConosReflectivos) {
            infoVehiculoModel.setDosConosReflectivos(respuestaBien);

        } else if (rdgDosConosReflectivosId == R.id.rbMalDosConosReflectivos) {
            infoVehiculoModel.setDosConosReflectivos(respuestaMal);
        }

        if (rdgParalesId == R.id.rbBienParalesIgualLongitud) {
            infoVehiculoModel.setParales(respuestaBien);

        } else if (rdgParalesId == R.id.rbMalParalesIgualLongitud) {
            infoVehiculoModel.setParales(respuestaMal);
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

        datosInfoVehiculo.put("placa", edtPlaca.getText().toString());
        datosInfoVehiculo.put("vehiculo", edtVehiculo.getText().toString());
        datosInfoVehiculo.put("tarjetaPropiedad", respuestaTarjetaPropiedad);
        datosInfoVehiculo.put("revisionTecnicomecanica", respuestaRevisionTecnicomecanica);
        datosInfoVehiculo.put("soatVigente", respuestaSoat);
        datosInfoVehiculo.put("lucesAltas", respuestaLucesAltas);
        datosInfoVehiculo.put("lucesBajas", respuestaucesBajas);
        datosInfoVehiculo.put("direccionales", respuestaDireccionales);
        datosInfoVehiculo.put("sonidoReversa", respuestaSonidoReversa);
        datosInfoVehiculo.put("reversa", respuestaReversa);
        datosInfoVehiculo.put("stop", respuestaStop);
        datosInfoVehiculo.put("retrovisores", respuestaRetrovisores);
        datosInfoVehiculo.put("plumillas", respuestaPlumillas);
        datosInfoVehiculo.put("estadoPanoramicos", respuestaEstadoPanoramicos);
        datosInfoVehiculo.put("espejos", respuestaEspejos);
        datosInfoVehiculo.put("bocina", respuestaBocina);
        datosInfoVehiculo.put("cinturon", respuestaCinturon);
        datosInfoVehiculo.put("freno", respuestaFreno);
        datosInfoVehiculo.put("llantas", respuestaLlantas);
        datosInfoVehiculo.put("botiquin", respuestaBotiquin);
        datosInfoVehiculo.put("extintorABC", respuestaExtintorABC);
        datosInfoVehiculo.put("botas", respuestaBotas);
        datosInfoVehiculo.put("chaleco", respuestaChaleco);
        datosInfoVehiculo.put("casco", respuestaCasco);
        datosInfoVehiculo.put("carroceria", respuestaCarroceria);
        datosInfoVehiculo.put("dosEslingasBanco", respuestaDosEslingasPorBanco);
        datosInfoVehiculo.put("dosConosReflectivos", respuestaDosConosReflectivos);
        datosInfoVehiculo.put("parales", respuestaParales);
        datosInfoVehiculo.put("observacionesCamion", edtObservacionesInfoVehiculo.getText().toString());

        Log.d("Datos Info Conductor ", "Datos Info Conductor : " + datosInfoVehiculo);

        //String id_lista = AgregarListaCargueDescargue.idListaStatic;
        String id_lista = AgregarMostrarListas.idListaStatic;
        Log.d("TAG ID LISTA EN INFO CONDUCTOR", "ID LISTA EN INFO CONDUCTOR : " + id_lista);

        if (isNetworkAvailable()) {
            if (id_lista != null) {
                db.collection(pathLista)
                        .document(id_lista)
                        .update("Item_3_Informacion_vehiculo", datosInfoVehiculo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                actualizarEstadoLista();

                                Toast.makeText(getContext(), "Datos almacenados correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "No se almacenaros los datos", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        else {
            actualizarEstadoLista();
            String id = idRecibido;
            String hora = horaRecibido;
            String fecha = fechaRecibido;
            String tipoCargue = tipoCargueRecibido;
            String zona = zonaRecibido;
            String nucleo = nucleoRecibido;
            String finca = fincaRecibido;

            String nombreConductor = nombreConductorRecibido;
            String cedula = cedulaRecibido;
            String lugarExpedicion = lugarExpedicionRecibido;
            String licConduccion = licConduccionRecibido;
            String polizaRCE = polizaRCERecibido;
            String epsRes = epsResRecibido;
            String arlRes = arlResRecibido;
            String afpRes = afpResRecibido;
            String cualEps = cualEpsRecibido;
            String cualArl = cualArlRecibido;
            String cualAfp = cualAfpRecibido;

            String placa = edtPlaca.getText().toString();
            String vehiculo = edtVehiculo.getText().toString();
            String tarjeta = respuestaTarjetaPropiedad;
            String observaciones = edtObservacionesInfoVehiculo.getText().toString();

            /*
            Log.d("PLACA", "PLACA EN INFO VEHICULO : " + placa);
            Log.d("VEHICULO", "VEHICULO EN INFO VEHICULO : " + vehiculo);
            Log.d("TARJETA PROPIEDAD", "TARJETA PROPIEDAD : " + respuestaTarjetaPropiedad);
            Log.d("SOAT", "SOAT : " + respuestaSoat);
            Log.d("REV. TECNICOMECANICA", "REV. TECNICOMECANICA : " + respuestaRevisionTecnicomecanica);
            Log.d("LUCES ALTAS", "LUCES ALTAS : " + respuestaLucesAltas);
            Log.d("LUCES BAJAS", "LUCES BAJAS : " + respuestaucesBajas);
            Log.d("DIRECCIONALES", "DIRECCIONALES : " + respuestaDireccionales);
            Log.d("SONIDO REVERSA", "SONIDO REVERSA : " + respuestaSonidoReversa);
            Log.d("REVERSA", "REVERSA : " + respuestaReversa);
            Log.d("STOP", "STOP : " + respuestaStop);
            Log.d("RETROVISORES", "RETROVISORES : " + respuestaRetrovisores);
            Log.d("PLUMILLAS", "PLUMILLAS : " + respuestaPlumillas);
            Log.d("ESTADO PANORAMICOS", "ESTADO PANORAMICOS : " + respuestaEstadoPanoramicos);
            Log.d("ESPEJOS", "ESPEJOS : " + respuestaEspejos);
            Log.d("BOCINA", "BOCINA : " + respuestaBocina);
            Log.d("CINTURON", "CINTURON : " + respuestaCinturon);
            Log.d("FRENO", "FRENO : " + respuestaFreno);
            Log.d("LLANTAS", "LLANTAS : " + respuestaLlantas);
            Log.d("BOTIQUIN", "BOTIQUIN : " + respuestaBotiquin);
            Log.d("EXTINTOR ABC", "EXTINTOR ABC : " + respuestaExtintorABC);
            Log.d("BOTAS", "BOTAS : " + respuestaBotas);
            Log.d("CHALECO", "CHALECO : " + respuestaChaleco);
            Log.d("CASCO", "CASCO : " + respuestaCasco);
            Log.d("CARROCERIA", "CARROCERIA : " + respuestaCarroceria);
            Log.d("DOS CONOS REFLECTIVOS", "DOS CONOS REFLECTIVOS : " + respuestaDosConosReflectivos);
            Log.d("PARALES", "PARALES : " + respuestaParales);
            Log.d("OBSERVACIONES", "OBSERVACIONES : " + observaciones);

             */

            if (requireActivity() instanceof DatosListener4) {
                ((DatosListener4) requireActivity()).recibirDatos4(id, hora, fecha, tipoCargue, zona,
                        nucleo, finca, nombreConductor, cedula, lugarExpedicion, licConduccion, polizaRCE, epsRes, arlRes, afpRes,
                        cualEps, cualArl, cualAfp, placa, vehiculo, tarjeta, respuestaSoat, respuestaRevisionTecnicomecanica,
                        respuestaLucesAltas, respuestaucesBajas, respuestaDireccionales, respuestaSonidoReversa, respuestaReversa, respuestaStop,
                        respuestaRetrovisores, respuestaPlumillas, respuestaEstadoPanoramicos, respuestaEspejos, respuestaBocina, respuestaCinturon,
                        respuestaFreno, respuestaLlantas, respuestaBotiquin, respuestaExtintorABC, respuestaBotas, respuestaChaleco, respuestaCasco,
                        respuestaCarroceria, respuestaDosEslingasPorBanco, respuestaDosConosReflectivos, respuestaParales, observaciones);
            }

            if (datosListener3 != null) {
                datosListener3.onDatosEnviados(id, hora, fecha, tipoCargue, zona, nucleo, finca,
                        nombreConductor, cedula, lugarExpedicion, licConduccion, polizaRCE, epsRes, arlRes, afpRes, cualEps, cualArl, cualAfp,
                        placa, vehiculo, tarjeta, respuestaSoat, respuestaRevisionTecnicomecanica, respuestaLucesAltas, respuestaucesBajas,
                        respuestaDireccionales, respuestaSonidoReversa, respuestaReversa, respuestaStop, respuestaRetrovisores,
                        respuestaPlumillas, respuestaEstadoPanoramicos, respuestaEspejos, respuestaBocina, respuestaCinturon, respuestaFreno,
                        respuestaLlantas, respuestaBotiquin, respuestaExtintorABC, respuestaBotas, respuestaChaleco, respuestaCasco,
                        respuestaCarroceria, respuestaDosEslingasPorBanco, respuestaDosConosReflectivos, respuestaParales, observaciones, "",
                        "", "", "", "", "",
                        "", "", "", "",
                        "", "", "", "", "", "",
                        "", "", ""); //, "".getBytes());
                Toast.makeText(getContext(), "Datos almacenados correctamente", Toast.LENGTH_SHORT).show();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public interface OnEstadoCamposActualizadoListener3 {
        void onEstadoCamposActualizado3(boolean camposCompletos);
    }

    private OnEstadoCamposActualizadoListener3 listener3;

    private void actualizarEstadoLista() {
        boolean camposCompletos = verificarCampos();

        if (listener3 != null) {
            listener3.onEstadoCamposActualizado3(camposCompletos);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        datosListener3 = null;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DatosListener3) {
            datosListener3 = (DatosListener3) context;
        } else {
            throw new RuntimeException("La actividad debe implementar la interfaz DatosListener 3");
        }

        if (context instanceof OnEstadoCamposActualizadoListener3) {
            listener3 = (OnEstadoCamposActualizadoListener3) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnEstadoCamposInfoVehiculoListener");
        }
    }
}

