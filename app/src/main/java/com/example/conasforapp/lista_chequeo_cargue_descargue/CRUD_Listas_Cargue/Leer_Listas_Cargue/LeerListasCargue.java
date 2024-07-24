package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Leer_Listas_Cargue;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.conasforapp.R;

public class LeerListasCargue extends AppCompatActivity {
    private Fragment leer_item1 ,leer_item2,leer_item3, leer_item4,leer_item5;
    FragmentManager fragmentManager;
    Button btnAtrasLeer;
    private ScrollView scrollViewLeerListas;
    private TextView txtFecha, txtFinca, txtResponsable;
    Button btnAceptarListaLeida;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leer_listas_cd);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scrollViewLeerListas = findViewById(R.id.scrollLeerListasCargue);
        fragmentManager = getSupportFragmentManager();

        txtFecha = findViewById(R.id.txtFechaLeerCD);
        txtFinca = findViewById(R.id.txtFincLeerCD);
        txtResponsable = findViewById(R.id.txtResponsableLeerCD);

        btnAtrasLeer = findViewById(R.id.btnAtrasLeerCD);
        btnAceptarListaLeida = findViewById(R.id.btnAceptarListaLeida);

        btnAtrasLeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });

        btnAceptarListaLeida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });

        //Asignación de los Fragmentos de cada item de la lista de Cargue y Descargue
        leer_item1 = new LeerInfoLugarFragment();
        leer_item2 = new LeerInfoConductorFragment();
        leer_item3 = new LeerInfoVehiculoFragment();
        leer_item4 = new LeerEstadoCargueFragment();
        leer_item5 = new LeerFirmasFragment();

        loadFragment(leer_item1,R.id.frameInfoLugarCargue);

        new Handler().postDelayed(() -> loadFragment(leer_item2, R.id.frameInfoConductor), 1000);
        new Handler().postDelayed(() -> loadFragment(leer_item3, R.id.frameInfoVehiculo), 2000);
        new Handler().postDelayed(() -> loadFragment(leer_item4, R.id.frameEstadoCargue), 3000);
        new Handler().postDelayed(() -> loadFragment(leer_item5, R.id.frameFirmas), 4000);

        scrollViewLeerListas.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) scrollViewLeerListas.getChildAt(scrollViewLeerListas.getChildCount() - 1);
                int diff = (view.getBottom() - (scrollViewLeerListas.getHeight() + scrollViewLeerListas.getScrollY()));
                if (diff == 0) {
                    // has reached the bottom
                }
            }
        });

        if (!isNetworkAvailable()) {
            Bundle extrasBDLocal = getIntent().getExtras();
            int listId = extrasBDLocal.getInt("list_id");
            String responsable = extrasBDLocal.getString("nombre supervisor");
            String finca = extrasBDLocal.getString("finca");
            String fechaLista = extrasBDLocal.getString("fecha creacion");

            txtFecha.setText(fechaLista);
            txtFinca.setText(finca);
            txtResponsable.setText(responsable);

            leer_item1 = LeerInfoLugarFragment.newInstance(listId);
            leer_item2 = LeerInfoConductorFragment.newInstance(listId);
            leer_item3 = LeerInfoVehiculoFragment.newInstance(listId);
            leer_item4 = LeerEstadoCargueFragment.newInstance(listId);
            leer_item5 = LeerFirmasFragment.newInstance(listId);

            if (listId == -1) {
                Toast.makeText(LeerListasCargue.this, "ID no válido", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Intent intent = getIntent();
            String responsable = intent.getStringExtra("nombre supervisor");
            String finca = intent.getStringExtra("finca");
            String fecha = intent.getStringExtra("fecha creacion");

            txtFecha.setText(fecha);
            txtFinca.setText(finca);
            txtResponsable.setText(responsable);
        }
    }

    private void regresar() {
        if (!isFinishing() && fragmentManager != null) {
            fragmentManager.beginTransaction().remove(leer_item1).commitAllowingStateLoss();
            fragmentManager.beginTransaction().remove(leer_item2).commitAllowingStateLoss();
            fragmentManager.beginTransaction().remove(leer_item3).commitAllowingStateLoss();
            fragmentManager.beginTransaction().remove(leer_item4).commitAllowingStateLoss();
            fragmentManager.beginTransaction().remove(leer_item5).commitAllowingStateLoss();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        new Handler().post(() -> {
            finish();
            Animatoo.INSTANCE.animateSlideRight(LeerListasCargue.this);
        });
    }

    private void loadFragment(Fragment fragment, int frameId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

