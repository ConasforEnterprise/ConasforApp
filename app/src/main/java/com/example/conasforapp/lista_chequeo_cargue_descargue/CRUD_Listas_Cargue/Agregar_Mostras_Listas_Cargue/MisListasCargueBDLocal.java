package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Adaptadores.AdaptadroListasCargueBDLocal;
import com.example.conasforapp.lista_chequeo_cargue_descargue.BD_LOCAL_Listas_Cargue.ListasCargueDataBaseHelper;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Editar_Listas_Cargue.EditarListasCargue;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Leer_Listas_Cargue.LeerListasCargue;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MisListasCargueBDLocal extends Fragment {
    View rootView;
    Toolbar toolbarCargue;
    RecyclerView rvListasBDLocal;
    private AdaptadroListasCargueBDLocal listasCargueAdapter;
    private ListasCargueDataBaseHelper dbLocal;
    Chip chipNombreSupervisor,chipFinca,chipFecha, chipNombreConductor,chipTodas;
    private String nombreC = "";
    ChipGroup chipGroup;
    List<String> criteriosBusqueda = new ArrayList<>();
    private List<ListasCargueModel> listasCargueBDLocal = new ArrayList<>();
    private Date fechaSeleccionada = new Date();
    private ActionMode actionMode;
    AlertDialog alertDialog;
    private static final String DIALOG_STATE = "dialogState";
    private boolean isDialogShowing = false;
    public static final int MENU_ITEM_ELIMINAR = R.id.menu_item_eliminar;
    int Id_ListaLocal = 0;
    private List<ListasCargueModel> todasLasListas = new ArrayList<>();
    private ConnectivityReceiver connectivityReceiver;

    public MisListasCargueBDLocal() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_mis_listas_cargue_bd_local, container, false);
        toolbarCargue = rootView.findViewById(R.id.toolbarListaCargueBDLocal);

        // Configurar el Toolbar
        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbarCargue);
            (activity.getSupportActionBar()).setTitle("Listas de cargue de madera");
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.baseline_arrow_back_24_black));
        }

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

        if(!isNetworkAvailable()){
            dbLocal = new ListasCargueDataBaseHelper(getContext());
            rvListasBDLocal = rootView.findViewById(R.id.rVListasCargueCrearBDLocal);
            recyclerListasBDLocal();

            ViewStub chipGroupStub = rootView.findViewById(R.id.chipGroupStubCrearBDLocal);
            View inflatedView = chipGroupStub.inflate();
            chipGroup = inflatedView.findViewById(R.id.chipGroupNuevo);

            chipFinca = chipGroup.findViewById(R.id.chipFinca);
            chipFecha = chipGroup.findViewById(R.id.chipFecha);
            chipNombreConductor = chipGroup.findViewById(R.id.chipNombreConductor);
            chipNombreSupervisor = chipGroup.findViewById(R.id.chipNombreSupervisor);
            chipNombreSupervisor.setVisibility(View.GONE);
            chipFinca.setVisibility(View.GONE);
            chipTodas = chipGroup.findViewById(R.id.chipTodas);

            chipTodas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    limpiarChips();
                }
            });

            if (savedInstanceState != null) {
                isDialogShowing = savedInstanceState.getBoolean(DIALOG_STATE, false);
                if (isDialogShowing) {

                }
            }
            chipFechas();
            obtenerDatosBDLocal();
        }

        fechaSeleccionada = null;

        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private Date parseFecha(String fechaString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Manejar clic en la flecha de regreso
            atrasListas();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void atrasListas(){

        Animatoo.INSTANCE.animateSlideLeft(getContext());
        getActivity().finish();
    }

    //--------------------------------------- MOSTAR LISTAS BASE DATOS LOCAL -----------------------------------------//
    private void obtenerDatosBDLocal() {
        listasCargueBDLocal = dbLocal.getCompleteList();
        ArrayList<String> nombresConductores = new ArrayList<>();

        for (ListasCargueModel lista : listasCargueBDLocal) {
            String nombreConductor = lista.getItem_2_Informacion_del_conductor().getNombreConductor().toString();
            Id_ListaLocal = lista.getId_lista_local();
            if (!nombresConductores.contains(nombreConductor)) {
                nombresConductores.add(nombreConductor);
            }
        }
        listasCargueAdapter.refreshData(dbLocal.getCompleteList());
        chipConductores(nombresConductores);
    }

    private void recyclerListasBDLocal(){
        listasCargueAdapter = new AdaptadroListasCargueBDLocal(listasCargueBDLocal,getContext());
        rvListasBDLocal.setAdapter(listasCargueAdapter);
        rvListasBDLocal.setLayoutManager(new GridLayoutManager(getContext(), 1));

        rvListasBDLocal.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvListasBDLocal, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ListasCargueModel listasCargueModel = listasCargueAdapter.getItem(position);
                String fechaLista = listasCargueModel.getItem_1_Informacion_lugar_cargue().getFecha();

                // Clic normal de las CardView
                if (actionMode == null) {
                    try {
                        // Formato Date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date fechaListaDate = dateFormat.parse(fechaLista);

                        // Fecha actual Calendar Format
                        Calendar calHoy = Calendar.getInstance();
                        calHoy.set(Calendar.HOUR_OF_DAY, 0);
                        calHoy.set(Calendar.MINUTE, 0);
                        calHoy.set(Calendar.SECOND, 0);
                        calHoy.set(Calendar.MILLISECOND, 0);

                        //Fecha lista Calendar Format
                        Calendar calFechaLista = Calendar.getInstance();
                        calFechaLista.setTime(fechaListaDate);
                        calFechaLista.set(Calendar.HOUR_OF_DAY, 0);
                        calFechaLista.set(Calendar.MINUTE, 0);
                        calFechaLista.set(Calendar.SECOND, 0);
                        calFechaLista.set(Calendar.MILLISECOND, 0);

                        int idPosicionLista = listasCargueAdapter.getItem(position).getId_lista_local();

                        if (fechaListaDate != null && calFechaLista.before(calHoy)) {
                            String nombreSuprvisor = AgregarMostrarListas.listasCargueModel.getNombre();
                            String finca = listasCargueModel.getItem_1_Informacion_lugar_cargue().getNombreFinca();

                            Intent intentLeer = new Intent(getContext(), LeerListasCargue.class);
                            intentLeer.putExtra("list_id",idPosicionLista);
                            intentLeer.putExtra("nombre supervisor", nombreSuprvisor);
                            intentLeer.putExtra("finca", finca);
                            intentLeer.putExtra("fecha creacion", fechaLista);

                            startActivity(intentLeer);
                            Animatoo.INSTANCE.animateSlideLeft(getContext());

                            Toast.makeText(getContext(), "Lista modo lectura", Toast.LENGTH_SHORT).show();
                        }

                        else if (fechaListaDate != null && calFechaLista.equals(calHoy)) {
                            Intent intentActualizar = new Intent(getContext(), EditarListasCargue.class);
                            intentActualizar.putExtra("list_id", listasCargueModel.getId_lista_local());
                            startActivity(intentActualizar);
                            Animatoo.INSTANCE.animateSlideLeft(getContext());
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    // Lógica para seleccionar/deseleccionar elementos durante el modo de acción contextual
                    listasCargueAdapter.toggleSelection(position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

                if (actionMode != null) {
                    return;
                }
                // Start the CAB using the ActionMode.Callback defined above
                actionMode = getActivity().startActionMode(actionModeCallback);
                view.setSelected(true);
                Id_ListaLocal = listasCargueAdapter.getItem(position).getId_lista_local();

                // Lógica para seleccionar el elemento al mantener presionado
                listasCargueAdapter.toggleSelection(position);
            }
        }));
    }
    //------------------------------------- FIN MOSTAR LISTAS BASE DATOS LOCAL ---------------------------------------//


    //------------------------------------- FILTRAR LISTAS BASE DATOS LOCAL ------------------------------------------//

    private void chipConductores(ArrayList<String> nombresConductores){
        chipNombreConductor.setTag(nombresConductores);
        chipNombreConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> nombres = (ArrayList<String>) chipNombreConductor.getTag();
                if (nombres != null && !nombres.isEmpty()) {
                    // Crear el PopupWindow
                    LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.card_chips_conductores, null);
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    // Configurar el listener para manejar los clics en los elementos de la lista
                    ListView listViewConductores = popupView.findViewById(R.id.listViewConductores);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, nombres);
                    listViewConductores.setAdapter(adapter);

                    // Bandera para rastrear si se seleccionó un nombre
                    final boolean[] nombreSeleccionado = {false}; // Variable final

                    // Configurar el listener para manejar los clics en los elementos de la lista
                    listViewConductores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Obtener el nombre seleccionado
                            nombreC = nombres.get(position);
                            // Actualizar el texto del chip de nombres con el nombre seleccionado
                            Log.d("NOMBRE CONDUCTOR EN LISTVIEW CONDUCTORES","NOMBRE CONDUCTOR EN LISTVIEW CONDUCTORES : "+ nombreC);
                            chipNombreConductor.setText(nombreC);

                            buscarListasBDLocal(nombreC,fechaSeleccionada);
                            nombreSeleccionado[0] = true;
                            // Cerrar el PopupWindow
                            popupWindow.dismiss();
                        }
                    });
                    // Agregar un OnDismissListener al PopupWindow
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setFocusable(true);
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    // Agregar un OnDismissListener al PopupWindow
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            // Verificar si se seleccionó un nombre
                            if (!nombreSeleccionado[0]) {
                                Log.d("NOMBRE SELECCIONADO","NOMBRE SELECCIONADO : " + nombreSeleccionado);
                                // Ocultar la lista de nombres si no se seleccionó ningún nombre
                                listViewConductores.setVisibility(View.GONE);
                            }
                        }
                    });

                    // Mostrar el PopupWindow cerca del chip de nombres
                    popupWindow.showAsDropDown(chipNombreConductor);
                } else {
                    Toast.makeText(getContext(), "No hay nombres disponibles", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void chipFechas(){
        chipFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear el PopupWindow
                LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.card_chips_fecha, null);
                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // Obtener una referencia al CalendarView
                CalendarView calendarView = popupView.findViewById(R.id.calendarView);

                // Configurar un listener para manejar la selección de fecha
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        // Obtener la fecha seleccionada
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);

                        fechaSeleccionada = calendar.getTime();

                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String fechaFormateada = formatoFecha.format(fechaSeleccionada);
                        chipFecha.setText(fechaFormateada);

                        buscarListasBDLocal(nombreC,fechaSeleccionada);

                        // Cerrar el PopupWindow
                        popupWindow.dismiss();
                    }
                });

                // Agregar un OnDismissListener al PopupWindow
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Mostrar el PopupWindow cerca del chip de fecha
                popupWindow.showAsDropDown(chipFecha);
            }
        });
    }

    private void  buscarListasBDLocal(String nombreConductor, Date fechaSeleccionada) {
        //guardarEstadoFiltro(nombreConductor, fechaSeleccionada);
        todasLasListas.clear();

        for (ListasCargueModel lista : listasCargueBDLocal) {
            todasLasListas.add(lista);
        }

        boolean filtroAplicado = false;

        List<ListasCargueModel> filteredList = new ArrayList<>(todasLasListas);

        // Filtrar por fecha
        if (fechaSeleccionada != null) {
            filteredList = filteredList.stream()
                    .filter(lista -> {
                        String fechaString = lista.getItem_1_Informacion_lugar_cargue().getFecha();
                        Log.d("FECHA FILTRO ADMIN", "FECHA FILTRO ADMIN : " + fechaString);
                        if (fechaString == null) {
                            return false;
                        }
                        Calendar calLista = Calendar.getInstance();
                        calLista.setTime(parseFecha(fechaString));
                        Calendar calFecha = Calendar.getInstance();
                        calFecha.setTime(fechaSeleccionada);

                        return calLista.get(Calendar.YEAR) == calFecha.get(Calendar.YEAR) &&
                                calLista.get(Calendar.MONTH) == calFecha.get(Calendar.MONTH) &&
                                calLista.get(Calendar.DAY_OF_MONTH) == calFecha.get(Calendar.DAY_OF_MONTH);
                    })
                    .collect(Collectors.toList());
            filtroAplicado = true;
        }

        // Filtrar por nombre de supervisor
        if (nombreConductor != null && !nombreConductor.isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(lista -> {
                        String conductorNombre = lista.getItem_2_Informacion_del_conductor().getNombreConductor();
                        Log.d("conductor NOMBRE EN FILTRO ADMIN", "conductor NOMBRE EN FILTRO ADMIN : " + conductorNombre);
                        return conductorNombre != null && conductorNombre.equals(nombreConductor);
                    })
                    .collect(Collectors.toList());
            filtroAplicado = true;
        }

        if (!filtroAplicado) {
            Toast.makeText(getContext(), "Proporcione al menos un argumento para filtrar", Toast.LENGTH_SHORT).show();
        } else if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No se encontraron listas coincidentes", Toast.LENGTH_SHORT).show();
        } else {
            mostrarListasCoincidentes(filteredList);
        }
    }

    private void mostrarListasCoincidentes(List<ListasCargueModel> listasCoincidentes) {
        if (listasCoincidentes.isEmpty()) {
            Toast.makeText(getContext(), "Ingresa todos los filtros", Toast.LENGTH_SHORT).show();
        }
        listasCargueAdapter = new AdaptadroListasCargueBDLocal(listasCoincidentes,getContext());
        rvListasBDLocal.setAdapter(listasCargueAdapter );
    }


    private void limpiarChips() {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setText(""); // Limpiar el texto del chip
        }

        // Limpiar los filtros
        criteriosBusqueda.clear();

        Toast.makeText(getContext(), "Se limpiaron los filtros", Toast.LENGTH_SHORT).show();

        if (chipFecha.getText().toString().isEmpty() || criteriosBusqueda.isEmpty()) {
            // Restablecer las listas
            nombreC = null;
            fechaSeleccionada = null;
            obtenerDatosBDLocal();

            chipFecha.setText("Fecha");
            chipFinca.setText("Fincas");
            chipNombreSupervisor.setText("Nombre supervisor");
            chipNombreConductor.setText("Nombre Conductor");
        }
    }

    //-------------------------------------- FIN FILTRAR LISTAS BASE DATOS LOCAL -------------------------------------//
    private void popEliminarLista() {
        Log.d("Id_ListaLocal","Id_ListaLocal : "+Id_ListaLocal);
        if (isAdded() && getActivity() != null && !getActivity().isFinishing() && !getActivity().isDestroyed()) {
            // Crear el AlertDialog.Builder y mostrar el diálogo
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Eliminar Lista");
            builder.setMessage("¿Estás seguro de que deseas eliminar esta lista?");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    if (Id_ListaLocal != 0) {
                        dbLocal.deleteListById(Id_ListaLocal);
                        listasCargueAdapter.refreshData(dbLocal.getCompleteList());
                    } else {
                        Toast.makeText(getContext(), "No se ha seleccionado ninguna lista para eliminar", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getContext(), "Lista eliminada con éxito", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    // Acción cuando se hace clic en Cancelar
                    dialogInterface.dismiss(); // Cerrar el diálogo
                }
            });

            // Crear y mostrar el diálogo
            alertDialog = builder.create();
            alertDialog.show();
            isDialogShowing = true;
        }
    }
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.contextual_menu, menu); // Reemplaza con el ID de tu menú contextual
            if (toolbarCargue != null) {
                toolbarCargue.animate()
                        .translationX(-toolbarCargue.getWidth())  // Desplazar hacia la izquierda
                        .setDuration(300)  // Duración de la animación
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                toolbarCargue.setVisibility(View.GONE);
                            }
                        })
                        .start();
            }

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // Puedes realizar ajustes adicionales aquí si es necesario
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
            if (menuItem.getItemId() == MENU_ITEM_ELIMINAR) {
                if (isAdded() && getActivity() != null && !getActivity().isFinishing() && !getActivity().isDestroyed()) {
                    popEliminarLista();
                }
                mode.finish(); // Cierra el modo de acción contextual después de mostrar el diálogo
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // Limpiar las selecciones al salir del modo de acción contextual
            //listasCargueDescargueRecycler.clearSelections();
            actionMode = null;

            if (toolbarCargue != null) {
                toolbarCargue.setVisibility(View.VISIBLE);
                toolbarCargue.setTranslationX(-toolbarCargue.getWidth());
                toolbarCargue.animate()
                        .translationX(0)  // Desplazar de vuelta a su posición original
                        .setDuration(300)  // Duración de la animación
                        .start();
            }
            //toolbarCargue.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        //restaurarEstadoFiltro();
        connectivityReceiver = new ConnectivityReceiver(new ConnectivityReceiver.ConnectivityReceiverListener() {
            @Override
            public void onNetworkConnectionChanged(boolean isConnected) {
                if (!isConnected) {
                    obtenerDatosBDLocal();
                }
            }
        });
        requireActivity().registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(connectivityReceiver);
    }

    // BroadcastReceiver class for connectivity change
    public class ConnectivityReceiver extends BroadcastReceiver {
        private ConnectivityReceiverListener listener;

        public ConnectivityReceiver(ConnectivityReceiverListener listener) {
            this.listener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
            if (listener != null) {
                listener.onNetworkConnectionChanged(isConnected);
            }
        }

        public interface ConnectivityReceiverListener {
            void onNetworkConnectionChanged(boolean isConnected);
        }
    }
}
