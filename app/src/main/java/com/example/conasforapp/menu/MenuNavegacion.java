package com.example.conasforapp.menu;

import static android.app.PendingIntent.getActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

//import com.blogspot.atifsoftwares.animatoolib.Animatoo;
//import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue.AgregarMostrarListas;
import com.example.conasforapp.Main.MainActivity;
import com.example.conasforapp.modelos.UsuariosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class MenuNavegacion extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    HomeFragment homeFragment = new HomeFragment();
    ListasFragment listasFragment = new ListasFragment();
    PerfilFragment perfilFragment = new PerfilFragment();
    AgregarDatosFirestore agregarDatosFirestore = new AgregarDatosFirestore();
    Dashboard_Firebase_Fragment dashboardFirebase = new Dashboard_Firebase_Fragment();
    private MeowBottomNavigation meowBottonNavigation;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String cargo = null;
    private TextView txtNombre, txtCargo,txtCorreo;
    private ImageView imgFotoPerfil;
    UsuariosModel usuariosModel;
    private FrameLayout fLayoutConexionInternet;
    private TextView txtTextoConexionInternet;
    private ConnectivityReceiver connectivityReceiver;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Lógica para manejar la recepción del broadcast
            obtenerUsuarios();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navegacion);


        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navView = findViewById(R.id.nav_view);
        meowBottonNavigation = findViewById(R.id.bottom_navigation);

        View headerView = navView.getHeaderView(0);

        txtNombre = headerView.findViewById(R.id.txtNavNombreUsuario);
        txtCargo = headerView.findViewById(R.id.txtNavCargoUsuario);
        txtCorreo = headerView.findViewById(R.id.txtNavCorreoUsuario);
        imgFotoPerfil = headerView.findViewById(R.id.imgNavFotoPerfil);

        //lLayoutConexionInternet = findViewById(R.id.lLayoutConexionInternet);
        fLayoutConexionInternet = findViewById(R.id.fLayoutConexionInternet);
        txtTextoConexionInternet = findViewById(R.id.txtConexionInternet);

        if(isConnectedToInternet()){
            //lLayoutConexionInternet.setVisibility(View.GONE);
            fLayoutConexionInternet.setVisibility(View.GONE);
            txtTextoConexionInternet.setText("Hay conexión de nuevo");
            Animatoo.INSTANCE.animateSlideUp(MenuNavegacion.this);
        }
        else {
            //lLayoutConexionInternet.setVisibility(View.VISIBLE);
            fLayoutConexionInternet.setVisibility(View.VISIBLE);
            txtTextoConexionInternet.setText("Sin conexión a internet");
            Animatoo.INSTANCE.animateSlideDown(MenuNavegacion.this);
        }

        //BottomNavigationView navigationVIew = findViewById(R.id.bottom_navigation);
        //navigationVIew.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //menuNavHorizontal(cargo);
        menuNavLateral();
        obtenerUsuarios();

        // Registro del receptor de conectividad
        connectivityReceiver = new ConnectivityReceiver(this);
    }

    private void setDatosUsuario() {
        String fotoUsuarioUrl = usuariosModel.getFotoUsuario();

        if (!fotoUsuarioUrl.isEmpty()) {
            Picasso.with(MenuNavegacion.this)
                    .load(fotoUsuarioUrl)
                    .resize(100, 100)
                    .into(imgFotoPerfil);
        } else {
            Toast.makeText(MenuNavegacion.this, "No has subido foto de perfil", Toast.LENGTH_SHORT).show();
        }

        txtNombre.setText(usuariosModel.getNombre());
        txtCargo.setText(usuariosModel.getCargo());
        txtCorreo.setText(usuariosModel.getCorreo());
    }

    private void obtenerUsuarios() {
        if (currentUser != null) {
            // El usuario está autenticado
            String uid = currentUser.getUid();
            db.collection("Usuarios").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            usuariosModel = document.toObject(UsuariosModel.class);
                            if (!usuariosModel.getNombre().isEmpty() || !usuariosModel.getCargo().isEmpty() || !usuariosModel.getCedula().isEmpty() || usuariosModel.getCorreo().isEmpty()) {
                                setDatosUsuario();
                            }

                            cargo = usuariosModel.getCargo();
                            menuNavHorizontal(cargo);
                        } else {
                            // Documento no encontrado
                            txtNombre.setText("Usuario no encontrado");
                            txtCargo.setText("Cargo no encontrado");
                        }
                    } else {
                        // Error al obtener el documento
                        txtNombre.setText("Error al obtener el usuario");
                        txtCargo.setText("Error al obtener el cargo");
                    }
                }
            });
        }
        else {
            // El usuario no está autenticado, puedes manejarlo según tus necesidades
            txtNombre.setText("Usuario no autenticado");
            txtCargo.setText("Cargo no asignado");
        }
    }

    /*
    private void menuNavHorizontal(){
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("fragmentToLoad")) {
            String fragmentToLoad = intent.getStringExtra("fragmentToLoad");
            switch (fragmentToLoad) {
                case "firstFragment":
                    loadFragment(homeFragment);
                    break;
                case "secondFragment":
                    loadFragment(listasFragment);
                    break;
                case "thirdFragment":
                    loadFragment(perfilFragment);
                    break;
            }
        } else {
            loadFragment(homeFragment);
        }
    }

     */

    /*
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            //Intent intent = getIntent();
            //Home Page
            if (item.getItemId() == R.id.homeFragment) {
                loadFragment(homeFragment);
                return true;
            }

            //Listas de chequeo
            else if (item.getItemId() == R.id.listasFragment) {
                loadFragment(listasFragment);
                return true;
            }

            //Perfil Usuario
            else if (item.getItemId() == R.id.perfilFragment) {
                loadFragment(perfilFragment);
                return true;
            }
            return false;
        }
    };
     */

    private void menuNavHorizontal(String cargo){
        meowBottonNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.icono_home));
        meowBottonNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.lista_verificacion_dos));
        meowBottonNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_usuario));

        if("Administrador".equals(cargo)){
            meowBottonNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_bd_add));
            meowBottonNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_firebase));
        }
        // Inicia la opción 1 por defecto, es decir la home
        meowBottonNavigation.show(1,true);

        meowBottonNavigation.setOnClickMenuListener(item -> {
            // Handle item selection here
            switch (item.getId()) {
                case 1:
                    loadFragment(homeFragment);
                    //getWindow().setNavigationBarColor(Color.parseColor("#4FCA16"));
                    break;
                case 2:
                    loadFragment(listasFragment);
                    //getWindow().setNavigationBarColor(Color.parseColor("#4FCA16"));
                    break;
                case 3:
                    loadFragment(perfilFragment);
                    //getWindow().setNavigationBarColor(Color.parseColor("#4FCA16"));
                    break;

                case 4:
                    loadFragment(agregarDatosFirestore);
                    //getWindow().setNavigationBarColor(Color.parseColor("#4FCA16"));
                    break;

                case 5:
                    loadFragment(dashboardFirebase);
                    //getWindow().setNavigationBarColor(Color.parseColor("#4FCA16"));
                    break;
            }
            return null;
        });
        loadFragment(homeFragment);
    }


   private void menuNavLateral(){
       /*-----------------------------Tool Bar-------------------------------------------*/

       setSupportActionBar(toolbar);

       /*-----------------------------Navigation Drawer Menu-------------------------------------------*/
       navView.bringToFront();
       ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(MenuNavegacion.this, drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
       drawerLayout.addDrawerListener(toogle);
       toogle.syncState();

       navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {

               int id = item.getItemId();

               if(id == R.id.nav_exit)
               {
                   logOut();
                   Intent intent = new Intent(MenuNavegacion.this, MainActivity.class);
                   startActivity(intent);
                   Animatoo.INSTANCE.animateSlideRight(MenuNavegacion.this);
                   //MenuNavegacion.this.finish();
               }
               else if(id == R.id.navCargueDescargue){
                   if("Administrador".equals(cargo)){
                       Intent intent = new Intent(MenuNavegacion.this, AgregarMostrarListas.class);
                       startActivity(intent);
                   }
                   else{
                       Intent intent = new Intent(MenuNavegacion.this, AgregarMostrarListas.class);
                       startActivity(intent);
                   }

                   Animatoo.INSTANCE.animateSlideLeft(MenuNavegacion.this);
                   drawerLayout.closeDrawer(GravityCompat.START);
               }
               return true;
           }
       });
   }

    public void loadFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed(){

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    public void onStart(){
        super.onStart();
        // Verificar si el usuario está autenticado con Firebase
        FirebaseUser user = mAuth.getCurrentUser();
        //Log.d("CURRENT USER ","CURRENT USER  : " + user.getUid());
        if (user == null) {
            irLogin();
        }
    }
    private void logOut(){
        mAuth.signOut();
        irLogin();
    }

    private void irLogin(){
        Intent login = new Intent(MenuNavegacion.this, MainActivity.class);
        startActivity(login);
        finish();
    }

    private boolean isConnectedToInternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager) MenuNavegacion.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            //lLayoutConexionInternet.setVisibility(View.GONE);
            fLayoutConexionInternet.setVisibility(View.GONE);
            txtTextoConexionInternet.setText("Hay conexión de nuevo");
        } else {
            //lLayoutConexionInternet.setVisibility(View.VISIBLE);
            fLayoutConexionInternet.setVisibility(View.VISIBLE);
            txtTextoConexionInternet.setText("Sin conexión a internet");
        }
    }
}