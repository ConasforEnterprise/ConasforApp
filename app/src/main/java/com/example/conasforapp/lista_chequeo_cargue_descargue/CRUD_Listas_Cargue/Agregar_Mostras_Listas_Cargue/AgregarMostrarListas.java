package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.Sincronizar_Listas_Cargue.ListasCargueSyncManager2;
import com.example.conasforapp.lista_chequeo_cargue_descargue.Sincronizar_Listas_Cargue.SyncService;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Llenar_Listas_Cargue.LlenarListasCargue;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.example.conasforapp.modelos.UsuariosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarMostrarListas extends AppCompatActivity {
    Toolbar toolbarCargue;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String pathUsuarios = "Usuarios";
    UsuariosModel usuariosModel = new UsuariosModel();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Fragment listasCargueAministrador, misListasCargue,misListasCargueBDLocal;
    FloatingActionButton fbAgregarListaCargueDescargue;
    public static String idListaStatic;
    Map<String, Object> datosCrearLista = new HashMap<>();
    private int cap_contador = 0;
    private String pathLista = "Listas chequeo cargue descargue";

    public static String cargo;
    private String idDoc = "";
    public static String idPosicionLista = "";

    public static ListasCargueModel listasCargueModel = new ListasCargueModel();
    public static Boolean estadoFButton = false;
    private ListasCargueSyncManager2 syncManager2;
    private ListasCargueDataBaseHelper dbLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_mostrar_listas);
        fbAgregarListaCargueDescargue = findViewById(R.id.fbAgregarListaCargue);

        listasCargueAministrador = new ListasCargueAministrador(); //--Utilizando
        misListasCargue = new MisListasCargue(); //---Utilizando
        misListasCargueBDLocal = new MisListasCargueBDLocal(); //--Utilizando

        dbLocal = new ListasCargueDataBaseHelper(this);

        obtenerUsuario();

        if(isNetworkAvailable()){
            verificarYSincronizarListas();
            syncManager2 = new ListasCargueSyncManager2(this);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                Animatoo.INSTANCE.animateSlideRight(AgregarMostrarListas.this);
            }
        };
        AgregarMostrarListas.this.getOnBackPressedDispatcher().addCallback(AgregarMostrarListas.this, callback);
    }

    public void iniciarServicio() {
        startService(new Intent(this, SyncService.class));
    }

    private boolean hayListasPendientesPorSincronizar() {
        List<ListasCargueModel> listasLocales = dbLocal.getCompleteList();
        Log.d("Número Listas pendientes","Número Listas pendientes : "+listasLocales.size());
        return !listasLocales.isEmpty();
    }

    private void verificarYSincronizarListas() {
        if (hayListasPendientesPorSincronizar()) {
            iniciarServicio();
        }
    }

    private void agregarLista(String nombreUser, String cargoUser, String id_user, String fotoPerfil) {
        fbAgregarListaCargueDescargue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estadoFButton = true;
                cap_contador = ContadorListaCargueDescargue.incrementoContador();
                datosCrearLista.put("lista_numero", String.valueOf(cap_contador));
                datosCrearLista.put("id_usuario", id_user);
                datosCrearLista.put("nombre", nombreUser);
                datosCrearLista.put("cargo", cargoUser);
                datosCrearLista.put("fotoUsuario", fotoPerfil);
                datosCrearLista.put("timestamp", FieldValue.serverTimestamp());

                if (isNetworkAvailable()) {
                    db.collection(pathLista).add(datosCrearLista).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            idDoc = documentReference.getId();
                            Log.d("ID_DOC","ID_DOC : "+idDoc);
                            idListaStatic = idDoc;

                            iniciarActividadLlenarListas();
                            Toast.makeText(AgregarMostrarListas.this, "Lista # " + cap_contador, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AgregarMostrarListas.this, "Falló : algo ocurrió ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    iniciarActividadLlenarListas();
                    Toast.makeText(AgregarMostrarListas.this, "Inició llenar listas sin conexión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void iniciarActividadLlenarListas() {
        Intent llenarLista = new Intent(AgregarMostrarListas.this, LlenarListasCargue.class);
        startActivity(llenarLista);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void backOnBackPressed(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentListasCargue, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void obtenerUsuario() {
        if (currentUser != null) {
            // El usuario está autenticado
            String uid = currentUser.getUid();
            db.collection(pathUsuarios).document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            usuariosModel = document.toObject(UsuariosModel.class);
                            cargo = usuariosModel.getCargo();

                            String nombreUser = usuariosModel.getNombre();
                            String fotoPerfil = usuariosModel.getFotoUsuario();
                            agregarLista(nombreUser, cargo, uid, fotoPerfil);

                            if(isNetworkAvailable() && !"Administrador".equals(cargo)){
                                loadFragment(misListasCargue);
                            }
                            else if(isNetworkAvailable() && "Administrador".equals(cargo)){
                                loadFragment(listasCargueAministrador);
                                fbAgregarListaCargueDescargue.setVisibility(View.INVISIBLE);
                            }
                            else if(!isNetworkAvailable()){
                                loadFragment(misListasCargueBDLocal);
                            }
                        }
                    }
                }
            });
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        if(isNetworkAvailable()){
            syncManager2.syncListWithFirestore();
        }
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();

    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}