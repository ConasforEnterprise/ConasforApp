package com.example.conasforapp.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.conasforapp.Main.MainActivity;
import com.example.conasforapp.R;
import com.example.conasforapp.modelos.UsuariosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
public class HomeFragment extends Fragment {
    private View view;
    private ImageButton imgDesplegar, imgDesplegarTips;
    private TextView txtDescripcionNosotros, txtUsuarioRegistrado;
    private LinearLayout lLayoutNosotros;
    private LinearLayout lLayoutCardsTips;
    private ViewPager2 viewPager2;
    private UsuariosModel usuariosModel = new UsuariosModel();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db;

    // Nombre de la preferencia compartida para el nombre de usuario
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        imgDesplegar = view.findViewById(R.id.btnDesplegarNosotros);
        imgDesplegarTips = view.findViewById(R.id.imgDesplegarTips);
        txtDescripcionNosotros = view.findViewById(R.id.txtDescripcionNosotros);
        //lLayoutNosotros = view.findViewById(R.id.LlayoutCarruselImagenes);
        lLayoutCardsTips = view.findViewById(R.id.lLayoutCardsTips);
        txtUsuarioRegistrado = view.findViewById(R.id.txtUsuarioRegistrado);

        db = FirebaseFirestore.getInstance();

        imgDesplegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtDescripcionNosotros.getVisibility() == View.VISIBLE) {
                    txtDescripcionNosotros.setVisibility(View.GONE);
                    //lLayoutNosotros.setVisibility(View.GONE);
                    imgDesplegar.setImageResource(R.drawable.icono_desplegar_abajo_verde);

                } else {
                    txtDescripcionNosotros.setVisibility(View.VISIBLE);
                    //lLayoutNosotros.setVisibility(View.VISIBLE);
                    imgDesplegar.setImageResource(R.drawable.icono_desplegar_arriba_verde);
                }
            }
        });

        imgDesplegarTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lLayoutCardsTips.getVisibility() == View.VISIBLE) {
                    lLayoutCardsTips.setVisibility(View.GONE);

                    imgDesplegarTips.setImageResource(R.drawable.icono_desplegar_abajo_verde);

                } else {
                    lLayoutCardsTips.setVisibility(View.VISIBLE);
                    imgDesplegarTips.setImageResource(R.drawable.icono_desplegar_arriba_verde);
                }
            }
        });

        // Intentar cargar el nombre de usuario desde la caché

        String cachedUsername = loadUsernameFromCache();
        if (cachedUsername != null) {
            txtUsuarioRegistrado.setText(cachedUsername);
        }


        //sliderCards();
        obtenerUsuarios();

        return view;
    }


    // Método para cargar el nombre de usuario desde la caché
    private String loadUsernameFromCache() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, null);
    }

    // Método para guardar el nombre de usuario en la caché
    private void saveUsernameToCache(String username) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }


    /*
    private void sliderCards() {

        viewPager2 = view.findViewById(R.id.viewPagerImgSliderHome);

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {

                float r = (1 - Math.abs(position)) / 5; //1 -
                page.setScaleY(0.20f + r + 0.5f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
    }

     */

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
                            //Muestra el nombre del usuario en la vista Home

                            String nombreUsuario = document.getString("nombre");
                            txtUsuarioRegistrado.setText(nombreUsuario);

                            //saveUsernameToCache(nombreUsuario);

                        } else {

                            // Documento no encontrado
                            txtUsuarioRegistrado.setText("Usuario no encontrado");
                        }
                    } else {

                        // Error al obtener el documento
                        txtUsuarioRegistrado.setText("Error al obtener el usuario");
                    }
                }
            });
        } else {

            // El usuario no está autenticado, puedes manejarlo según tus necesidades
            txtUsuarioRegistrado.setText("Usuario no autenticado");
        }
    }

   public void onStart(){
       super.onStart();
       FirebaseUser user = mAuth.getCurrentUser();
       if(user == null){
           irLogin();
       }
   }
    private void logOut(){
        mAuth.signOut();
        irLogin();

    }

    private void irLogin(){
        Intent login = new Intent(getContext(), MainActivity.class);
        startActivity(login);
        //finish();
    }
}