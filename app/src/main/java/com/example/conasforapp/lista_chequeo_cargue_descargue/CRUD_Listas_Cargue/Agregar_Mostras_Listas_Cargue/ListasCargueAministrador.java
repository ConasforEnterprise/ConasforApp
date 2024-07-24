package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Leer_Listas_Cargue.LeerListasCargue;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Adaptadores.AdaptadorListasCargueAdministradorApp;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListasCargueAministrador extends Fragment {
    View rootView;
    Toolbar toolbarCargue;
    private DocumentSnapshot lastVisible;
    private static final int LIMIT = 4; // Número de documentos por página
    private List<ListasCargueModel> listasCargue = new ArrayList<>();
    private List<ListasCargueModel> todasLasListas = new ArrayList<>();
    private ActionMode actionMode;
    private String pathLista = "Listas chequeo cargue descargue";
    public static String cargo;
    RecyclerView rVListasAdmin;
    public static String idPosicionLista = "";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progressBar;
    Chip chipNombreSupervisor,chipFinca,chipFecha, chipNombreConductor,chipTodas;
    ChipGroup chipGroup;
    private AdaptadorListasCargueAdministradorApp listasAminCargueDescargueRecycler;
    public ArrayList<String> supervisoresList = new ArrayList<>();
    private String nombreS = "",finca;
    private Date fechaSeleccionada = new Date();
    public ArrayList<String> fincasList = new ArrayList<>();
    List<String> criteriosBusqueda = new ArrayList<>();
    public static final String LISTA_FILTRADA = "listaFiltrada";
    private ListenerRegistration loadMoreDataListener;

    public ListasCargueAministrador() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_listas_cargue_administrador, container, false);
        toolbarCargue = rootView.findViewById(R.id.toolbarListaCargueAdmin2);

        // Configurar el Toolbar
        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbarCargue);
            (activity.getSupportActionBar()).setTitle("Listas de cargue de madera");
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.baseline_arrow_back_24_black));
        }
        rVListasAdmin = rootView.findViewById(R.id.rVListasCargueLeer2);

        ViewStub chipGroupStub = rootView.findViewById(R.id.chipGroupStubLeer2);
        View inflatedView = chipGroupStub.inflate();

        chipGroup = inflatedView.findViewById(R.id.chipGroupNuevo);

        chipNombreSupervisor = chipGroup.findViewById(R.id.chipNombreSupervisor);
        chipFinca = chipGroup.findViewById(R.id.chipFinca);
        chipFecha = chipGroup.findViewById(R.id.chipFecha);
        chipNombreConductor = chipGroup.findViewById(R.id.chipNombreConductor);
        chipNombreConductor.setVisibility(View.GONE);
        chipTodas = chipGroup.findViewById(R.id.chipTodas);

        chipTodas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarChips();
            }
        });

        progressBar = rootView.findViewById(R.id.progressBarListasAdmin);

        chipFechas();
        obtenerFincas();
        obtenerUsuariosSupervisores();
        listasAdministradorApp();

        fechaSeleccionada = null;
        return rootView;
    }


    //------------------------------------------- OBTENER TODAS LAS LISTAS ---------------------------------------------//

                            //*********** Método para obtener listas iniciales paginadas***********//
    private void listasAdministradorApp() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference listaRef = db.collection(pathLista);

        Query query = listaRef.whereNotEqualTo("cargo", "Administrador")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(LIMIT);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("TagListas", "Error al obtener listas", e);
                    Toast.makeText(getContext(), "Ocurrió un error al obtener las listas", Toast.LENGTH_SHORT).show();
                    return;
                }

                listasCargue.clear(); // Limpia la lista actual para actualizarla

                for (QueryDocumentSnapshot doc : snapshots) {
                    ListasCargueModel listasCargueModel = doc.toObject(ListasCargueModel.class);
                    listasCargueModel.setId_lista(doc.getId());
                    listasCargue.add(listasCargueModel);
                }

                if (!snapshots.isEmpty()) {
                    lastVisible = snapshots.getDocuments().get(snapshots.size() - 1);
                }

                ordenarListaPorFechaHoraDescendente(listasCargue);

                recyclerListasAdmin(); // Actualiza el RecyclerView
            }
        });
    }

                            //*********** Método para cargar las demás listas paginadas ***********//
    private void loadMoreData() {
        if (lastVisible != null) {
            progressBar.setVisibility(View.VISIBLE);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference listaRef = db.collection(pathLista);

            Query nextQuery = listaRef.whereNotEqualTo("cargo", "Administrador")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(LIMIT)
                    .startAfter(lastVisible);

            // Detén el listener anterior si existe
            if (loadMoreDataListener != null) {
                loadMoreDataListener.remove();
            }

            loadMoreDataListener = nextQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot snapshots,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.e("TagListas", "Error al obtener más listas", e);
                        Toast.makeText(getContext(), "Ocurrió un error al obtener más listas", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    if (snapshots != null && !snapshots.isEmpty()) {
                        for (QueryDocumentSnapshot doc : snapshots) {
                            ListasCargueModel listasCargueModel = doc.toObject(ListasCargueModel.class);
                            listasCargueModel.setId_lista(doc.getId());
                            if (!listasCargue.contains(listasCargueModel)) {
                                listasCargue.add(listasCargueModel);
                            }
                        }

                        lastVisible = snapshots.getDocuments().get(snapshots.size() - 1);
                        listasAminCargueDescargueRecycler.notifyDataSetChanged();
                    }

                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

                            //*********** Método para mostrar las listas paginadas ***********//
    private void recyclerListasAdmin() {
        listasAminCargueDescargueRecycler = new AdaptadorListasCargueAdministradorApp(getContext(), listasCargue);
        rVListasAdmin.setAdapter(listasAminCargueDescargueRecycler);
        rVListasAdmin.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rVListasAdmin.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rVListasAdmin, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //ListaModel listas = listasCargue.get(position);
                ListasCargueModel listas = listasAminCargueDescargueRecycler.getItem(position);
                // Clic normal CardView
                if (actionMode == null) {
                    AgregarMostrarListas.listasCargueModel = listas;
                    idPosicionLista = listasCargue.get(position).getId_lista();

                    String nombreSuprvisor = listas.getNombre();
                    String finca = listas.getItem_1_Informacion_lugar_cargue().getNombreFinca();
                    String fechaLista = listas.getItem_1_Informacion_lugar_cargue().getFecha();

                    Intent intentLeer = new Intent(getContext(), LeerListasCargue.class);
                    intentLeer.putExtra("nombre supervisor", nombreSuprvisor);
                    intentLeer.putExtra("finca",finca);
                    intentLeer.putExtra("fecha creacion", fechaLista);
                    startActivity(intentLeer);
                    Animatoo.INSTANCE.animateSlideLeft(getContext());

                }
                else {
                    // Lógica para seleccionar/deseleccionar elementos durante el modo de acción contextual
                    listasAminCargueDescargueRecycler.toggleSelection(position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (actionMode == null) {
                    actionMode = getActivity().startActionMode(actionModeCallback);
                    view.setSelected(true);
                    listasAminCargueDescargueRecycler.toggleSelection(position);
                }
            }
        }));
        rVListasAdmin.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == listasCargue.size() - 1) {
                    loadMoreData();
                }
            }
        });
    }
    //----------------------------------------- FIN OBTENER TODAS LAS LISTAS -------------------------------------------//



    //----------------------------------------- FILTROS LISTAS ADMINISTRADOR APP----------------------------------------//

                                    //*********** Métodos para obtener datos en chips ***********//
    private void obtenerFincas(){ //fincasCallback callback
        db.collection("desplegables infoLugar").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                ArrayList<String> nombresFinca = new ArrayList<>();
                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){ //Obtiene los datos de firestore

                    List<String> nombresF = (List<String>) documentSnapshot.get("nombre finca");
                    if(nombresF != null){
                        nombresFinca.addAll(nombresF);
                        chipFincas(nombresFinca);
                    }

                }
                ArrayAdapter<String> adapterNombreFinca = new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,nombresFinca);
            }
            else {
                Exception exception = task.getException();
            }
        });
    }
    private void obtenerUsuariosSupervisores(){
        ArrayList<String> usuariosList = new ArrayList<>();
        db.collection("Usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String cargoUsuario = document.getString("cargo");
                            String nombreUsuario = document.getString("nombre"); // Ajusta este campo según la estructura de tu documento
                            //nombreUsuarioSupervisorStatic = nombreUsuario;
                            if(!"Administrador".equals(cargoUsuario)){
                                usuariosList.add(nombreUsuario);
                                chipSupervisores(usuariosList);
                            }
                            Log.d("USUARIOS SUPER", "Nombre de usuario: " + nombreUsuario);
                        }
                    } else {
                        // Maneja cualquier error que ocurra al obtener los usuarios
                        Log.w("", "Error getting documents.", task.getException());
                    }
                });
    }

                                        //*********** Métodos Chips ************-//
    private void chipSupervisores(ArrayList<String> nombresSupervisores){
        chipNombreSupervisor.setTag(nombresSupervisores);
        chipNombreSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supervisoresList = (ArrayList<String>) chipNombreSupervisor.getTag();
                if (supervisoresList != null && !supervisoresList.isEmpty()) {
                    // Crear el PopupWindow
                    LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.card_chips_supervisores, null);
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    // Configurar el listener para manejar los clics en los elementos de la lista
                    ListView listViewSupervisores = popupView.findViewById(R.id.listViewSupervisores);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, supervisoresList);
                    listViewSupervisores.setAdapter(adapter);

                    // Bandera para rastrear si se seleccionó un nombre
                    final boolean[] nombreSeleccionado = {false}; // Variable final

                    // Configurar el listener para manejar los clics en los elementos de la lista
                    listViewSupervisores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Obtener el nombre seleccionado
                            nombreS = supervisoresList.get(position);
                            // Actualizar el texto del chip de nombres con el nombre seleccionado
                            chipNombreSupervisor.setText(nombreS);

                            filtrarListasCargueAdministrador(nombreS,finca,fechaSeleccionada);
                            // Marcar que se seleccionó un nombre
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
                                Log.d("NOMBRE SELECCIONADO","NOMBRE SELECCIONADO : "+nombreSeleccionado);
                                // Ocultar la lista de nombres si no se seleccionó ningún nombre
                                listViewSupervisores.setVisibility(View.GONE);
                                //cVFiltroNombres.setVisibility(View.GONE);
                            }
                        }
                    });

                    // Mostrar el PopupWindow cerca del chip de nombres
                    popupWindow.showAsDropDown(chipNombreSupervisor);
                } else {
                    Toast.makeText(getContext(), "No hay nombres disponibles", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void chipFincas(List<String> fincas){
        chipFinca.setTag(fincas);
        chipFinca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fincasList = (ArrayList<String>) chipFinca.getTag();
                if (fincasList != null && !fincasList.isEmpty()) {
                    // Crear el PopupWindow
                    LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.card_chips_fincas, null);
                    PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    // Configurar el listener para manejar los clics en los elementos de la lista
                    ListView listViewFincas = popupView.findViewById(R.id.listViewFincas);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, fincasList);
                    listViewFincas.setAdapter(adapter);

                    // Bandera para rastrear si se seleccionó un nombre
                    final boolean[] fincaSeleccionada = {false}; // Variable final

                    // Configurar el listener para manejar los clics en los elementos de la lista
                    listViewFincas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Obtener el nombre seleccionado
                            finca = fincas.get(position);

                            // Actualizar el texto del chip de nombres con el nombre seleccionado
                            chipFinca.setText(finca);

                            filtrarListasCargueAdministrador(nombreS,finca,fechaSeleccionada);
                            // Marcar que se seleccionó un nombre
                            fincaSeleccionada[0] = true;
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
                            if (!fincaSeleccionada[0]) {
                                Log.d("FINCA SELECCIONADA", "FINCA SELECCIONADA : " + fincaSeleccionada);
                                // Ocultar la lista de nombres si no se seleccionó ningún nombre
                                listViewFincas.setVisibility(View.GONE);
                            } else {

                            }
                        }
                    });

                    // Mostrar el PopupWindow cerca del chip de nombres
                    popupWindow.showAsDropDown(chipFinca);
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

                        filtrarListasCargueAdministrador(nombreS,finca,fechaSeleccionada);
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

                                        //********** Filtrar listas **************//
    private void filtrarListasCargueAdministrador(String nombreSupervisor, String finca, Date fecha){
        db.collection("Listas chequeo cargue descargue").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    todasLasListas.clear();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        ListasCargueModel lista = documentSnapshot.toObject(ListasCargueModel.class);
                        //lista.setId_lista(documentSnapshot.getId());
                        todasLasListas.add(lista);
                    }
                    List<ListasCargueModel> filteredList = new ArrayList<>(todasLasListas);

                    boolean filtroAplicado = false;

                    // Filtrar por fecha
                    if (fecha != null) {
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
                                    calFecha.setTime(fecha);

                                    return calLista.get(Calendar.YEAR) == calFecha.get(Calendar.YEAR) &&
                                            calLista.get(Calendar.MONTH) == calFecha.get(Calendar.MONTH) &&
                                            calLista.get(Calendar.DAY_OF_MONTH) == calFecha.get(Calendar.DAY_OF_MONTH);
                                })
                                .collect(Collectors.toList());
                        filtroAplicado = true;
                    }

                    // Filtrar por nombre de supervisor
                    if (nombreSupervisor != null && !nombreSupervisor.isEmpty()) {
                        filteredList = filteredList.stream()
                                .filter(lista -> {
                                    String supervisorNombre = lista.getNombre();
                                    Log.d("SUPERVISOR NOMBRE EN FILTRO ADMIN", "SUPERVISOR NOMBRE EN FILTRO ADMIN : " + supervisorNombre);
                                    return supervisorNombre != null && supervisorNombre.equals(nombreSupervisor);
                                })
                                .collect(Collectors.toList());
                        filtroAplicado = true;
                    }

                    // Filtrar por finca
                    if (finca != null && !finca.isEmpty()) {
                        filteredList = filteredList.stream()
                                .filter(lista -> finca.equals(lista.getItem_1_Informacion_lugar_cargue().getNombreFinca()))
                                .collect(Collectors.toList());
                        filtroAplicado = true;
                    }

                    if (!filtroAplicado) {
                        Toast.makeText(getContext(), "Proporcione al menos un argumento para filtrar", Toast.LENGTH_SHORT).show();
                    } else if (filteredList.isEmpty()) {
                        Toast.makeText(getContext(), "No se encontraron listas coincidentes", Toast.LENGTH_SHORT).show();
                    } else {
                        mostrarListasFiltradasAdministrador(filteredList);
                    }
                }
            }
        });
    }
    private void mostrarListasFiltradasAdministrador(List<ListasCargueModel> listasCoincidentes) {
        ordenarListaPorFechaHoraDescendente(listasCoincidentes);
        if (listasCoincidentes.isEmpty()) {
            // Si no hay listas coincidentes, puedes mostrar un mensaje indicando que no se encontraron resultados
            Toast.makeText(getContext(), "Ingresa todos los filtros", Toast.LENGTH_SHORT).show();
        }

        else{
            listasAminCargueDescargueRecycler = new AdaptadorListasCargueAdministradorApp(getContext(), listasCoincidentes);
            rVListasAdmin.setAdapter(listasAminCargueDescargueRecycler);
            listasAminCargueDescargueRecycler.mostrarListasCoincidentes(listasCoincidentes);
            listasAminCargueDescargueRecycler.notifyDataSetChanged();
            rVListasAdmin.setVisibility(View.VISIBLE);

            // Envía un Broadcast con los datos filtrados
            Intent intent = new Intent("actualizar_lista_filtrada");
            intent.putExtra(LISTA_FILTRADA, (Serializable) listasCoincidentes);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        }
    }

                                    //*********** Limpiar listas filtradas ***********//
    private void limpiarChips() {
        lastVisible = null;

        listasCargue.clear();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setText(""); // Limpiar el texto del chip
        }

        // Limpiar los filtros
        criteriosBusqueda.clear();

        Toast.makeText(getContext(), "Se limpiaron los filtros", Toast.LENGTH_SHORT).show();

        if (chipFecha.getText().toString().isEmpty() || criteriosBusqueda.isEmpty()) {
            nombreS = null;
            finca = null;
            fechaSeleccionada = null;
            cargarListasIniciales();

            listasAminCargueDescargueRecycler.notifyDataSetChanged();

            chipFecha.setText("Fecha");
            chipFinca.setText("Fincas");
            chipNombreSupervisor.setText("Nombre supervisor");
            chipNombreConductor.setText("Nombre Conductor");
            chipTodas.setText("Todas");
        }
    }

    private void cargarListasIniciales() {
        Query query = db.collection(pathLista)
                .whereNotEqualTo("cargo", "Administrador")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(LIMIT);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listasCargue.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ListasCargueModel lista = document.toObject(ListasCargueModel.class);
                        lista.setId_lista(document.getId());
                        listasCargue.add(lista);
                    }
                    ordenarListaPorFechaHoraDescendente(listasCargue);
                    listasAminCargueDescargueRecycler.actualizarListas(listasCargue);
                    listasAminCargueDescargueRecycler.notifyDataSetChanged();

                    if (!task.getResult().isEmpty()) {
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    }
                }
            }
        });
    }

    //-------------------------------------- FIN FILTROS LISTAS ADMINISTRADOR APP --------------------------------------//

    private void ordenarListaPorFechaHoraDescendente(List<ListasCargueModel> listasCoincidentes) {
        if (listasCoincidentes == null || listasCoincidentes.isEmpty()) {
            // Lista vacía o nula, no hay nada que ordenar
            return;
        }
        Collections.sort(listasCoincidentes, new Comparator<ListasCargueModel>() {
            @Override
            public int compare(ListasCargueModel lista1, ListasCargueModel lista2) {
                String fechaString1 = lista1.getItem_1_Informacion_lugar_cargue().getFecha();
                String fechaString2 = lista2.getItem_1_Informacion_lugar_cargue().getFecha();
                String horaString1 = lista1.getItem_1_Informacion_lugar_cargue().getHoraEntrada();
                String horaString2 = lista2.getItem_1_Informacion_lugar_cargue().getHoraEntrada();

                // Convertir las fechas de String a Date
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                try {
                    Date fechaHora1 = dateFormat.parse(fechaString1 + " " + horaString1);
                    Date fechaHora2 = dateFormat.parse(fechaString2 + " " + horaString2);

                    // Ordenar en orden descendente para tener las fechas actuales primero
                    return fechaHora2.compareTo(fechaHora1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
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

    //-----------------------------------------------METODO ACCION MENU CONTEXTUAL-----------------------------------//
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

            Toast.makeText(getContext(), "Actualmente está deshabilitada esta función", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // Limpiar las selecciones al salir del modo de acción contextual
            listasAminCargueDescargueRecycler.clearSelections();
            actionMode = null;
            if (toolbarCargue != null) {
                toolbarCargue.setVisibility(View.VISIBLE);
                toolbarCargue.setTranslationX(-toolbarCargue.getWidth());
                toolbarCargue.animate()
                        .translationX(0)  // Desplazar de vuelta a su posición original
                        .setDuration(300)  // Duración de la animación
                        .start();
            }
        }
    };
    //--------------------------------------- FIN MÉTODO ACCION MENU CONTEXTUAL-------------------------------------//
}