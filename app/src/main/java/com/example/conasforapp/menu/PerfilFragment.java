package com.example.conasforapp.menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.conasforapp.R;
import com.example.conasforapp.Usuarios.EditarPerfil;
import com.example.conasforapp.modelos.UsuariosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.squareup.picasso.Picasso;

public class PerfilFragment extends Fragment{
    private View view;
    private FloatingActionButton fbtEditarPerfilSupervisor;
    private TextView txtNombre, txtCargo, txtCedula, txtFinca, txtCorreo;
    private ImageView imgFotoUsuario;
    private RelativeLayout rLayoutFinca, rLayoutCedula;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String pathUsuarios = "Usuarios";
    Intent editar;
    UsuariosModel usuariosModel;
    String uid = "";
    String cargo = null;
    //private CacheManager cacheManager;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            obtenerDatosUsuario(); // Lógica para manejar la recepción del broadcast de editarPerfil
        }
    };

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Registrar el receptor de broadcast para recibir las actualizaciones de datos del perfil
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("actualizacion_datos_perfil"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);

        //Carga Firestore correctamente
        FirebaseApp.initializeApp(getContext());

        //Persistencia de datos : Firebase almacena una copia de los datos de los datos
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        txtNombre = view.findViewById(R.id.txtNombreUsuario);
        txtCargo = view.findViewById(R.id.txtCargoUsuario);
        txtFinca = view.findViewById(R.id.txtFincaUsuario);
        txtCorreo = view.findViewById(R.id.txtCorreoUsuario);
        txtCedula = view.findViewById(R.id.txtCedulaUsuario);
        imgFotoUsuario = view.findViewById(R.id.imgFotoPerfil);
        fbtEditarPerfilSupervisor = view.findViewById(R.id.ftbEditarPerfilSupervisor);
        rLayoutFinca = view.findViewById(R.id.relativeLayoutFinca);
        rLayoutCedula = view.findViewById(R.id.rlayoutCedula);

        fbtEditarPerfilSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentEditarDatos();
            }
        });

        obtenerDatosUsuario();
        return view;
    }

    @Override
    public void onDestroy() {
        // Deregistrar el receptor de broadcast al destruir el fragmento
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private void intentEditarDatos(){

        editar = new Intent(getActivity(), EditarPerfil.class);
        editar.putExtra("nombreUsuario", usuariosModel.getNombre().toString());
        editar.putExtra("cargoUsuario", usuariosModel.getCargo());
        editar.putExtra("fincaUsuario", usuariosModel.getFinca());
        editar.putExtra("correoUsuario", usuariosModel.getCorreo());
        editar.putExtra("cedulaUsuario", usuariosModel.getCedula());
        editar.putExtra("fotoUsuario", usuariosModel.getFotoUsuario());
        startActivity(editar);
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
                            usuariosModel = document.toObject(UsuariosModel.class);
                            cargo = usuariosModel.getCargo();

                            if("Administrador".equals(cargo)){
                                rLayoutFinca.setVisibility(View.GONE);
                                rLayoutCedula.setVisibility(View.GONE);
                            }

                            if (!usuariosModel.getNombre().isEmpty() || !usuariosModel.getCargo().isEmpty()
                                    || !usuariosModel.getCedula().isEmpty() || usuariosModel.getCorreo().isEmpty()
                                    || !usuariosModel.getFinca().isEmpty()) {
                                setDatosUsuario();
                            } else {
                                txtNombre.setText("Nombre no disponible");
                                txtCargo.setText("Cargo no disponible");
                                txtFinca.setText("Finca no disponible");
                                txtCedula.setText("Cédula no disponible");
                                txtCorreo.setText("Correo no disponible");
                            }
                        } else {
                            // Si el documento no existe, maneja la situación de alguna manera apropiada
                            txtNombre.setText("Usuario no encontrado");
                        }
                    } else {
                        // Maneja el caso de error en la tarea
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

    private void setDatosUsuario(){
        String fotoUsuarioUrl = usuariosModel.getFotoUsuario();

        if (!fotoUsuarioUrl.isEmpty()) {
            Picasso.with(getContext())
                    .load(fotoUsuarioUrl)
                    .resize(150,150)
                    .into(imgFotoUsuario);
        }
        else
        {
            //imgFotoUsuario.setImageResource(R.drawable.icono_foto_usuario);
            Toast.makeText(getContext(), "No has subido foto de perfil", Toast.LENGTH_SHORT).show();
        }
        txtNombre.setText(usuariosModel.getNombre());
        txtCargo.setText(usuariosModel.getCargo());
        txtFinca.setText(usuariosModel.getFinca());
        txtCedula.setText(usuariosModel.getCedula());
        txtCorreo.setText(usuariosModel.getCorreo());
    }
}