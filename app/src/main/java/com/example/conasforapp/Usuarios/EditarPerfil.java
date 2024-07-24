package com.example.conasforapp.Usuarios;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.conasforapp.R;
import com.example.conasforapp.menu.MenuNavegacion;
import com.example.conasforapp.menu.PerfilFragment;
import com.example.conasforapp.modelos.UsuariosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
public class EditarPerfil extends AppCompatActivity {
    TextInputEditText edtNombre, edtCargo, edtFinca, edtCedula, edtCorreo;
    TextInputLayout txtFinca, txtCedula;
    Button btnActualizarDatosPerfil;
    String nombreRecibido = "",cargoRecibido = "",fincaRecibido = "",correoRecibido = "",cedulaRecibido = "";
    FirebaseFirestore db;
    String pathUsuarios = "Usuarios";
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    ProgressDialog progressDialog = null;
    String uid = currentUser.getUid();
    private static final int CODE_GALERY = 500;
    private Uri urlImagen;
    ImageView imgFotoUsuario;
    String fotoPerfilRecibido = "";
    public Map<String, Object> editarUsuario = new HashMap<>();
    public String downloadUri = "";
    Boolean estadoGuardarCambios = false;
    private FloatingActionButton fBSubirFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        db = FirebaseFirestore.getInstance();

        txtFinca = findViewById(R.id.txtFincaEditarPerfil);
        txtCedula = findViewById(R.id.txtCedulaEditarPerfil);

        edtNombre = findViewById(R.id.edtNombreUsuarioEditar);
        edtCargo = findViewById(R.id.edtCargoUsuarioEditar);
        edtFinca = findViewById(R.id.edtFincaEditarPerfil);
        edtCorreo = findViewById(R.id.edtCorreoUsuarioEditar);
        edtCedula = findViewById(R.id.edtCedulaUsuarioEditar);
        imgFotoUsuario = findViewById(R.id.imgFotoMascotaEditar);
        btnActualizarDatosPerfil = findViewById(R.id.btnGuardarEditarPerfil);
        fBSubirFoto = findViewById(R.id.fBSubirFoto);

        fBSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFoto();
            }
        });

        obtenerDatosUsuario();
    }

    // Método para notificar al fragmento de perfil que los datos han cambiado
    private void notifyProfileFragment() {
        Intent intent = new Intent("actualizacion_datos_perfil");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void obtenerDatosUsuario(){

        //Datos recibidos
        Intent intent = getIntent();
        nombreRecibido = intent.getStringExtra("nombreUsuario");
        cargoRecibido = intent.getStringExtra("cargoUsuario");
        fincaRecibido = intent.getStringExtra("fincaUsuario");
        correoRecibido = intent.getStringExtra("correoUsuario");
        cedulaRecibido = intent.getStringExtra("cedulaUsuario");
        fotoPerfilRecibido = intent.getStringExtra("fotoUsuario");


        if("Administrador".equals(cargoRecibido)){
            txtFinca.setVisibility(View.GONE);
            txtCedula.setVisibility(View.GONE);
        }

        //Se muestran los datos en los TextInputLayout
        edtNombre.setText(nombreRecibido);
        edtCargo.setText(cargoRecibido);
        edtFinca.setText(fincaRecibido);
        edtCedula.setText(cedulaRecibido);
        edtCorreo.setText(correoRecibido);

        if(!fotoPerfilRecibido.isEmpty()){
            Picasso.with(EditarPerfil.this)
                    .load(fotoPerfilRecibido)
                    .into(imgFotoUsuario);
        }

        else {
            Toast.makeText(EditarPerfil.this, "No hay fotos para actualizar", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Agrega una foto", Toast.LENGTH_SHORT).show();
        }

        btnActualizarDatosPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarDatosUsuario();
                estadoGuardarCambios = true;
            }
        });

    }

    private void editarDatosUsuario(){

        editarUsuario.put("nombre",edtNombre.getText().toString());
        editarUsuario.put("cargo",edtCargo.getText().toString());
        editarUsuario.put("finca",edtFinca.getText().toString());
        editarUsuario.put("cedula",edtCedula.getText().toString());
        editarUsuario.put("contrasena",edtCorreo.getText().toString());

        showLoading("Actualizando usuario");
        db.collection(pathUsuarios).document(uid).update(editarUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(EditarPerfil.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();

                Intent perfil = new Intent(EditarPerfil.this, MenuNavegacion.class);
                perfil.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                perfil.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                perfil.putExtra("fragmentToLoad", "PerfilFragment");
                Animatoo.INSTANCE.animateSlideLeft(EditarPerfil.this);
                startActivity(perfil);
                EditarPerfil.this.finish();

                notifyProfileFragment();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarPerfil.this, "Error al actualizar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void cargarFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK); //MediaStore.Images.Media.EXTERNAL_CONTENT_URI   ---
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),CODE_GALERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == CODE_GALERY){
                if (data!=null){

                    urlImagen = data.getData();
                    imgFotoUsuario.setImageURI(urlImagen);

                    Intent intent = getIntent();
                    nombreRecibido = intent.getStringExtra("nombreUsuario");
                    cargoRecibido = intent.getStringExtra("cargoUsuario");

                    String rutaFotoPerfil = "foto_perfil_" + nombreRecibido + "_" + cargoRecibido + ".jpg";
                    //urlImagen.getLastPathSegment()
                    StorageReference imagenRef = FirebaseStorage.getInstance().getReference().child("Fotos Usuarios Registrados").child(rutaFotoPerfil);
                    imagenRef.putFile(urlImagen).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            if(uriTask.isSuccessful()){
                                uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        downloadUri = uri.toString();
                                        editarUsuario.put("fotoUsuario",downloadUri);
                                        Log.d("TAG FOTO PERFIL","FOTO PERFIL : " + downloadUri);
                                        //Toast.makeText(EditarPerfil.this, "Imagen subida con éxito a firestore", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //Toast.makeText(getContext(), "Falló al subir imagen", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        }
    }

    public void onBackPressed() {

        finish();
        super.onBackPressed();

        // super.onBackPressed(); // Esta línea cerraría la actividad por defecto
    }

    private void showLoading(String msg){

        if (progressDialog == null && !isFinishing()) {
            progressDialog = new ProgressDialog(EditarPerfil.this);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

    private void closeLoading(){
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showMessage(String msj){
        Toast.makeText(EditarPerfil.this, msj, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeLoading();
    }
}