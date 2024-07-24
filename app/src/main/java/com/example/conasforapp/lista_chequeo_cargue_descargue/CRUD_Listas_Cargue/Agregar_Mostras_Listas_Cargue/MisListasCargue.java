package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Agregar_Mostras_Listas_Cargue;

import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.conasforapp.R;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Editar_Listas_Cargue.EditarListasCargue;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Leer_Listas_Cargue.LeerListasCargue;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Adaptadores.AdaptadorMisListasCargue;
import com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Llenar_Listas_Cargue.LlenarListasCargue;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.example.conasforapp.modelos.UsuariosModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MisListasCargue extends Fragment {
    View rootView;
    Toolbar toolbarCargue;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String pathLista = "Listas chequeo cargue descargue";
    private String pathUsuarios = "Usuarios";
    public static String cargo;
    public static String correo;
    private List<ListasCargueModel> listasCargue = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    UsuariosModel usuariosModel = new UsuariosModel();
    Chip chipNombreSupervisor,chipFinca,chipFecha, chipNombreConductor,chipTodas;
    private String nombreC = "";
    private Date fechaSeleccionada = new Date();
    List<String> criteriosBusqueda = new ArrayList<>();
    ChipGroup chipGroup;
    public static String idSeleccionado = "";
    public static String idPosicionLista = "";
    private String idDoc = "";
    Dialog dialog;
    private static final String DIALOG_STATE = "dialogState";
    private boolean isDialogShowing = false;
    AlertDialog alertDialog;
    private ActionMode actionMode;
    private AdaptadorMisListasCargue listasCargueDescargueRecycler;
    RecyclerView recyclerViewListas;
    public static final String LISTA_FILTRADA = "listaFiltrada";
    public static final int MENU_ITEM_ELIMINAR = R.id.menu_item_eliminar;
    private DocumentSnapshot lastVisible;
    private DocumentSnapshot firstVisible;  // Para manejar el desplazamiento hacia arriba
    private boolean isScrolling;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    List<ListasCargueModel> listasNoAdmin = new ArrayList<>();
    List<String> idListasNoAdmin = new ArrayList<>();
    private boolean isFiltering = false;
    private ProgressBar progressBar;
    private ListenerRegistration listenerRegistration;
    private CardView cardListaEliminada;
    private ListenerRegistration loadMoreListener;
    private LottieAnimationView lottieAnimationEliminar;
    private TextView txtListaEliminada;

    public MisListasCargue() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_listas_cargue_crear, container, false);
        toolbarCargue = rootView.findViewById(R.id.toolbarListaCargue);

        // Configurar el Toolbar
        if (getActivity() != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbarCargue);
            (activity.getSupportActionBar()).setTitle("Listas de cargue de madera");
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.baseline_arrow_back_24_black));
        }

        recyclerViewListas = rootView.findViewById(R.id.rVListasCargueCrear);
        progressBar = rootView.findViewById(R.id.progressBarListasCargueCrear);

        ViewStub chipGroupStub = rootView.findViewById(R.id.chipGroupStubCrear);
        View inflatedView = chipGroupStub.inflate();
        chipGroup = inflatedView.findViewById(R.id.chipGroupNuevo);

        chipFinca = chipGroup.findViewById(R.id.chipFinca);
        chipFecha = chipGroup.findViewById(R.id.chipFecha);
        chipNombreConductor = chipGroup.findViewById(R.id.chipNombreConductor);
        chipNombreSupervisor = chipGroup.findViewById(R.id.chipNombreSupervisor);
        chipTodas = chipGroup.findViewById(R.id.chipTodas);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.pop_up_eliminar_lista);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        cardListaEliminada = rootView.findViewById(R.id.cardListaEliminada);
        lottieAnimationEliminar = rootView.findViewById(R.id.lottieAnimationEliminar);
        txtListaEliminada = rootView.findViewById(R.id.txtlistaEliminada);

        if (savedInstanceState != null) {
            isDialogShowing = savedInstanceState.getBoolean(DIALOG_STATE, false);
            if (isDialogShowing) {
            }
        }

        recyclerViewListas.setItemAnimator(new DefaultItemAnimator());
        chipFinca.setVisibility(View.GONE);
        chipNombreSupervisor.setVisibility(View.GONE);

        chipTodas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((fechaSeleccionada != null && !fechaSeleccionada.equals(new Date(0))) ||
                        (nombreC != null && !nombreC.isEmpty())) {
                    limpiarChips();
                } else {
                    Toast.makeText(getContext(), "Esta opción se habilita cuando se filtren los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        obtenerUsuario();
        chipFechas();

        fechaSeleccionada = null;
        return rootView;
    }

    //-------------------------------------- OBTENER DATOS USUARIO LISTA ---------------------------------------------//
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

                            if(!"Administrador".equals(cargo)){
                                obtenerNombresConductores(uid);
                            }
                           listasUsuariosNoAdmin(cargo,uid);
                        }
                    }
                }
            });
        }
    }
    //----------------------------------- FIN OBTENER DATOS USUARIO ----------------------------------------------------//




    //------------------------------------------ OBTENER LISTAS USUARIO ----------------------------------------------//

                        //*********** Método para obtener listas iniciales paginadas***********//
    private void listasUsuariosNoAdmin(String cargo, String idUsuario) {
        listasCargue.clear();
        Query query = db.collection(pathLista)
                .whereEqualTo("id_usuario", idUsuario)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5);

        listenerRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("DEBUG", "Error getting documents: ", error);
                    Toast.makeText(getContext(), "Falló al obtener listas", Toast.LENGTH_SHORT).show();
                    return;
                }

                listasCargue.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    ListasCargueModel listasCargueModel = doc.toObject(ListasCargueModel.class);
                    listasCargueModel.setId_lista(doc.getId());
                    listasCargue.add(listasCargueModel);
                    estadoLista(listasCargueModel);
                }
                ordenarListaPorFechaHoraDescendente(listasCargue);
                listasCargueDescargueRecycler = new AdaptadorMisListasCargue(getContext(), listasCargue);
                listasCargueDescargueRecycler.setListas(listasCargue);
                recyclerViewListas.setAdapter(listasCargueDescargueRecycler);

                recyclerListasSupervisor(cargo, idUsuario);

                if (!value.isEmpty()) {
                    lastVisible = value.getDocuments().get(value.size() - 1);
                    firstVisible = value.getDocuments().get(0);
                    Log.d("DEBUG", "lastVisible set to: " + lastVisible.getId());
                    Log.d("DEBUG", "firstVisible set to: " + firstVisible.getId());
                } else {
                    Log.d("DEBUG", "No documents found in initial query");
                }

                RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            isScrolling = true;
                        }
                    }
                };
                recyclerViewListas.addOnScrollListener(onScrollListener);
            }
        });
    }

                        //*********** Método para cargar las demás listas paginadas ***********//
    private void loadMoreListasNoAdmin(){
        progressBar.setVisibility(View.VISIBLE);
        if (lastVisible != null) {
            if (currentUser != null) {
                String uid = currentUser.getUid();
                Query query = db.collection(pathLista)
                        .whereEqualTo("id_usuario", uid)
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .startAfter(lastVisible)
                        .limit(5);

                loadMoreListener = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("DEBUG", "Error getting documents: ", error);
                            Toast.makeText(getContext(), "Falló al obtener listas", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            return;
                        }

                        for (DocumentSnapshot doc : value.getDocuments()) {
                            ListasCargueModel listasCargueModel = doc.toObject(ListasCargueModel.class);
                            listasCargueModel.setId_lista(doc.getId());
                            listasCargue.add(listasCargueModel);
                            estadoLista(listasCargueModel);
                        }
                        ordenarListaPorFechaHoraDescendente(listasCargue);

                        if (!value.isEmpty()) {
                            lastVisible = value.getDocuments().get(value.size() - 1);
                        } else {
                            isLastPage = true;
                        }

                        listasCargueDescargueRecycler.notifyDataSetChanged();
                        isLoading = false;
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

                        //*********** Método para mostrar las listas paginadas ***********//
    private void recyclerListasSupervisor(String cargo, String idU) {//List<ListaModel> listasCargue

        boolean camposCompletos = false;
        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            camposCompletos = intent.getBooleanExtra("campos_completos", false);
        }
        recyclerViewListas.setLayoutManager(new GridLayoutManager(getContext(), 1));
        boolean finalCamposCompletos = camposCompletos;
        recyclerViewListas.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerViewListas, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ListasCargueModel listas = listasCargueDescargueRecycler.getItemListas(position);
                String fechaLista = listas.getItem_1_Informacion_lugar_cargue().getFecha();
                String contador = String.valueOf(listasCargue.size() - position);

                // Clic normal de las CardView
                if (actionMode == null) {
                    if (fechaLista != null) {
                        try {
                            // Formato Date
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            Date fechaListaDate = dateFormat.parse(fechaLista);

                            Date fechaActual2 = new Date();

                            // Fecha actual Calendar Format
                            Calendar calHoy = Calendar.getInstance();
                            //Date fechaActual = calHoy.getTime();
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

                            AgregarMostrarListas.listasCargueModel = listas;
                            idPosicionLista = listasCargue.get(position).getId_lista();
                            Log.d("ID POSICION LISTA", "ID POSICION LISTA : " + idPosicionLista);

                            if (fechaListaDate != null && calFechaLista.before(calHoy) && !cargo.equals("Administrador")) {

                                Log.d("Se inició leer lista", "Se inició leer lista");
                                String nombreSuprvisor = listas.getNombre();
                                String finca = listas.getItem_1_Informacion_lugar_cargue().getNombreFinca();

                                Intent intentLeer = new Intent(getContext(), LeerListasCargue.class);
                                intentLeer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intentLeer.putExtra("nombre supervisor", nombreSuprvisor);
                                intentLeer.putExtra("finca", finca);
                                intentLeer.putExtra("fecha creacion", fechaLista);
                                intentLeer.putExtra("fecha_lista_date", fechaListaDate);
                                intentLeer.putExtra("fecha_actual_date", fechaActual2);
                                intentLeer.putExtra("contador lista", contador);
                                startActivity(intentLeer);
                                Animatoo.INSTANCE.animateSlideLeft(getContext());

                                Toast.makeText(getContext(), "Lista modo lectura", Toast.LENGTH_SHORT).show();
                            }

                            else if (fechaListaDate != null && calFechaLista.equals(calHoy) && !cargo.equals("Administrador")) {

                                Log.d("Se inició actualizar lista", "Se inició actualizar lista");
                                Intent intentActualizar = new Intent(getContext(), EditarListasCargue.class);
                                intentActualizar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intentActualizar.putExtra("idPos", idPosicionLista);
                                intentActualizar.putExtra("campos_completos", finalCamposCompletos);
                                startActivity(intentActualizar);
                                Animatoo.INSTANCE.animateSlideLeft(getContext());
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //Cuando la lista no tiene fecha
                        Intent intentActualizar = new Intent(getContext(), EditarListasCargue.class);
                        startActivity(intentActualizar);
                    }
                } else {
                    // Lógica para seleccionar/deseleccionar elementos durante el modo de acción contextual
                    listasCargueDescargueRecycler.toggleSelection(position);
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
                idSeleccionado = listasCargue.get(position).getId_lista();

                // Lógica para seleccionar el elemento al mantener presionado
                listasCargueDescargueRecycler.toggleSelection(position);
            }
        }));

        recyclerViewListas.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

                if (isFiltering) {
                    // No hacer nada si estamos filtrando
                    return;
                }

                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == listasCargue.size() - 1) {
                    if (!isLoading && !isLastPage) {
                        isLoading = true;
                        loadMoreListasNoAdmin();
                    }
                }
            }
        });
    }
    //------------------------------------------ FIN OBTENER LISTAS USUARIO ------------------------------------------//




    //------------------------------------------------------ FILTROS -----------------------------------------------//

                //*********** Método para obtener los nombres de los conductores para chipConductores ************-//
    private void obtenerNombresConductores(String idUsuario) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference listasRef = db.collection(pathLista);
        listasRef.whereEqualTo("id_usuario", idUsuario).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    List<ListasCargueModel> listasCargueModels = new ArrayList<>();
                    ArrayList<String> nombresConductores = new ArrayList<>();

                    for (QueryDocumentSnapshot document : querySnapshot) {
                        ListasCargueModel lista = document.toObject(ListasCargueModel.class);
                        lista.setId_lista(document.getId());
                        listasCargueModels.add(lista);

                        String nombreConductor = lista.getItem_2_Informacion_del_conductor().getNombreConductor();
                        nombresConductores.add(nombreConductor);

                        chipConductores(nombresConductores);
                    }
                }
            }
        });
    }
                                        //*********** Métodos Chips ************-//
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
                    final boolean[] nombreSeleccionado = {false};

                    // Configurar el listener para manejar los clics en los elementos de la lista
                    listViewConductores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Obtener el nombre seleccionado
                            nombreC = nombres.get(position);

                            chipNombreConductor.setText(nombreC);

                            if(!"Administrador".equals(cargo)){
                                filtrarListas(nombreC,fechaSeleccionada);
                            }
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

                        if(!"Administrador".equals(cargo)){
                            filtrarListas(nombreC,fechaSeleccionada);
                        }
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
    private void filtrarListas(String nombreConductor, Date fechaSeleccionada) {
        String uid = currentUser.getUid();
        CollectionReference listasRef = db.collection(pathLista);
        listasRef.whereEqualTo("id_usuario", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    listasNoAdmin.clear();

                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        ListasCargueModel lista = documentSnapshot.toObject(ListasCargueModel.class);
                        listasNoAdmin.add(lista);
                        lista.setId_lista(documentSnapshot.getId());

                        idListasNoAdmin.add(lista.getId_lista());

                        estadoLista(lista);
                    }

                    isFiltering = true;
                    criteriosBusqueda.clear(); // Limpia la lista antes de agregar los nuevos criterios

                    if (!nombreConductor.isEmpty()) {
                        criteriosBusqueda.add(nombreConductor);
                    }

                    if (fechaSeleccionada != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        criteriosBusqueda.add(dateFormat.format(fechaSeleccionada));
                    }

                    List<ListasCargueModel> listasCoincidentes = new ArrayList<>();

                    if (criteriosBusqueda.isEmpty()) {

                        listasCoincidentes = new ArrayList<>(listasNoAdmin);
                    }
                    else
                    {
                        for (ListasCargueModel lista : listasNoAdmin) {
                            boolean coincideConTodos = true;
                            for (String criterio : criteriosBusqueda) {
                                boolean cumpleCriterio = false;

                                if (lista.getItem_2_Informacion_del_conductor().getNombreConductor().toLowerCase().contains(criterio.toLowerCase())) {
                                    cumpleCriterio = true;
                                }
                                else {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    String fechaListaString = lista.getItem_1_Informacion_lugar_cargue().getFecha();
                                    if (fechaListaString != null && !fechaListaString.isEmpty()) {
                                        try {
                                            Date fechaLista = dateFormat.parse(fechaListaString);
                                            if (!criterio.isEmpty()) {
                                                Date fechaCriterio = dateFormat.parse(criterio);
                                                if (fechaLista.equals(fechaCriterio)) {
                                                    cumpleCriterio = true;
                                                }
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                if (!cumpleCriterio) {
                                    coincideConTodos = false;
                                    break;
                                }
                            }

                            if (coincideConTodos) {
                                listasCoincidentes.add(lista);
                            }
                        }
                        mostrarListasCoincidentes(listasCoincidentes);
                    }
                }
            }
        });
    }

                                    //********* Mostrar listas filtradas **********//
    private void mostrarListasCoincidentes(List<ListasCargueModel> listasCoincidentes) {
        if (listasCoincidentes.isEmpty()) {
            Toast.makeText(getContext(), "Ingresa todos los filtros", Toast.LENGTH_SHORT).show();
        }

        listasCargueDescargueRecycler = new AdaptadorMisListasCargue(getContext(), listasCoincidentes);
        recyclerViewListas.setAdapter(listasCargueDescargueRecycler);
        listasCargueDescargueRecycler.mostrarListasCoincidentes(listasCoincidentes);
        recyclerViewListas.setVisibility(View.VISIBLE);

        // Envía un Broadcast con los datos filtrados
        Intent intent = new Intent("actualizar_lista_filtrada");
        intent.putExtra(LISTA_FILTRADA, (Serializable) listasCoincidentes);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

                                    //*********** Limpiar listas ***********//
    private void limpiarChips() {
        isFiltering = false;
        isLoading = false;
        isLastPage = false;
        lastVisible = null;

        listasCargue.clear();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setText(""); // Limpiar el texto del chip
        }

        // Limpiar los filtros
        criteriosBusqueda.clear();

        Toast.makeText(getContext(), "Se limpiaron los filtros", Toast.LENGTH_SHORT).show();

        if (chipFecha.getText().toString().isEmpty() || chipNombreConductor.getText().toString().isEmpty() || criteriosBusqueda.isEmpty()) {
            nombreC = null;
            fechaSeleccionada = null;

            cargarListasIniciales();

            listasCargueDescargueRecycler.notifyDataSetChanged();

            chipFecha.setText("Fecha");
            chipFinca.setText("Fincas");
            chipNombreSupervisor.setText("Nombre supervisor");
            chipNombreConductor.setText("Nombre Conductor");
            chipTodas.setText("Todas");
        }
    }

    private void cargarListasIniciales() {
        String uid = currentUser.getUid();
        Query query = db.collection(pathLista)
                .whereEqualTo("id_usuario", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listasCargue.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ListasCargueModel lista = document.toObject(ListasCargueModel.class);
                        lista.setId_lista(document.getId());
                        listasCargue.add(lista);
                        estadoLista(lista);
                    }
                    ordenarListaPorFechaHoraDescendente(listasCargue);
                    listasCargueDescargueRecycler.setListas(listasCargue);
                    listasCargueDescargueRecycler.notifyDataSetChanged();

                    if (!task.getResult().isEmpty()) {
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    }
                }
            }
        });
    }
    //------------------------------------------------------ FIN FILTROS -------------------------------------------//



    //------------------------------------------------- ELIMINAR LISTAS --------------------------------------------//
                            //*********** Método para eliminar lista de recyclerView ***********//
    private void eliminarLista() {
        if (isAdded() && getActivity() != null && !getActivity().isFinishing() && !getActivity().isDestroyed()) {
            // Crear el AlertDialog.Builder y mostrar el diálogo
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Eliminar Lista");
            builder.setMessage("¿Estás seguro de que deseas eliminar esta lista?");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    // Acción cuando se hace clic en Aceptar
                    if (!idSeleccionado.isEmpty()) { //idDoc != null &&
                        Log.d("ID SELECCIONADO EN AGREGAR","ID SELECCIONADO EN AGREGAR : " + idSeleccionado);
                        db.collection(pathLista).document(idSeleccionado).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                String urlFotoCamion = AgregarMostrarListas.listasCargueModel.getItem_4_Estado_cargue().getFotoCamion();

                                String firmaSupervisor = AgregarMostrarListas.listasCargueModel.getFirma_Supervisor();
                                String firmaDespachador = AgregarMostrarListas.listasCargueModel.getFirma_Despachador();
                                String firmaConductor = AgregarMostrarListas.listasCargueModel.getFirma_Conductor();
                                String firmaOperador = AgregarMostrarListas.listasCargueModel.getFirma_Operador();

                                Log.d("FIRMA SUPERVISOR","FIRMA SUPERVISOR : " + firmaSupervisor);
                                Log.d("FIRMA DESPACHADOR","FIRMA DESPACHADOR : " + firmaDespachador);
                                Log.d("FIRMA CONDUCTOR","FIRMA CONDUCTOR : " + firmaConductor);
                                Log.d("FIRMA OPERADOR","FIRMA OPERADOR : " + firmaOperador);

                                // Elimina firmas del Storage de Firebase
                                eliminarFirmasStorage(firmaSupervisor);
                                eliminarFirmasStorage(firmaDespachador);
                                eliminarFirmasStorage(firmaConductor);
                                eliminarFirmasStorage(firmaOperador);

                                // Elimina foto del camión del Storage de Firebase
                                eliminarFotoCamionStorage(urlFotoCamion);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                // Actualizar la lista local y notificar al adaptador
                                int posicionEliminar = -1;
                                for (int i = 0; i < listasCargue.size(); i++) {
                                    if (listasCargue.get(i).getId_lista().equals(idDoc)) {
                                        posicionEliminar = i;
                                        break;
                                    }
                                }

                                if (posicionEliminar != -1) {
                                    listasCargue.remove(posicionEliminar);
                                    //listasCargueDescargueRecycler.setListas2(listasCargue);
                                    listasCargueDescargueRecycler.notifyItemRemoved(posicionEliminar);
                                    listasCargueDescargueRecycler.notifyItemRangeChanged(posicionEliminar, listasCargue.size());
                                    listasCargueDescargueRecycler.notifyDataSetChanged();
                                }

                                // Limpiar el idDoc después de eliminar la lista
                                idSeleccionado = "";

                                cardListaEliminada.setVisibility(View.VISIBLE);
                                lottieAnimationEliminar.setVisibility(View.VISIBLE);
                                lottieAnimationEliminar.playAnimation();
                                txtListaEliminada.setVisibility(View.VISIBLE);

                                        // Añadir listener para ocultar la animación y el mensaje cuando termine
                                        lottieAnimationEliminar.addAnimatorListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {
                                                // No se necesita acción aquí
                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                // Ocultar animación y mensaje
                                                lottieAnimationEliminar.setVisibility(View.GONE);
                                                txtListaEliminada.setVisibility(View.GONE);
                                                cardListaEliminada.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {
                                                // No se necesita acción aquí
                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {
                                                // No se necesita acción aquí
                                            }
                                        });
                                    }
                                }, 1000);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "No se eliminó la lista", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
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

            //*********** Método para eliminar las firmas de la lista en el almacenamiento en Firestore ***********//
    private void eliminarFirmasStorage(String urlFirma) {
        if (urlFirma != null && !urlFirma.isEmpty()) {
            try {
                StorageReference referenciaArchivo = FirebaseStorage.getInstance().getReferenceFromUrl(urlFirma);
                referenciaArchivo.delete()
                        .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Firmas eliminadas del Storage", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "No se eliminaron las firmas del Storage", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error al eliminar la firma del Storage", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

            //*********** Método para eliminar la foto del camión en el almacenamiento en Firestore ***********//
    private void eliminarFotoCamionStorage(String urlFotoCamion){
        if (urlFotoCamion!= null && !urlFotoCamion.isEmpty()) {
            try{
                StorageReference referenciaArchivo = FirebaseStorage.getInstance().getReferenceFromUrl(urlFotoCamion);
                referenciaArchivo.delete().addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Foto del camión eliminada del Storage", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "No se eliminó la foto del camión del Storage", Toast.LENGTH_SHORT).show());
            }catch (Exception e) {
                Toast.makeText(getContext(), "Error al eliminar la firma del Storage", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    //----------------------------------------------- FIN ELIMINAR LISTAS -------------------------------------------//



    //----------------------- METODOS PARA ORDENAR Y VERIFICAR EL ESTADO DE LA LISTA --------------------------------//
    private void ordenarListaPorFechaHoraDescendente(List<ListasCargueModel> listasCoincidentes) {
        Collections.sort(listasCoincidentes, new Comparator<ListasCargueModel>() {
            @Override
            public int compare(ListasCargueModel lista1, ListasCargueModel lista2) {
                String fechaString1 = lista1.getItem_1_Informacion_lugar_cargue().getFecha();
                String fechaString2 = lista2.getItem_1_Informacion_lugar_cargue().getFecha();
                String horaString1 = lista1.getItem_1_Informacion_lugar_cargue().getHoraEntrada();
                String horaString2 = lista2.getItem_1_Informacion_lugar_cargue().getHoraEntrada();

                if (fechaString1 == null || fechaString1.isEmpty() || fechaString2 == null || fechaString2.isEmpty() ||
                        horaString1 == null || horaString1.isEmpty() || horaString2 == null || horaString2.isEmpty()) {
                    return 0;
                }

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
    private void estadoLista(ListasCargueModel listasCargueModel) {
        // Obtener datos de la lista
        ListasCargueModel.InfoLugarCargue informacionLugarCargue = listasCargueModel.getItem_1_Informacion_lugar_cargue();
        ListasCargueModel.InfoDelConductor informacionConductor = listasCargueModel.getItem_2_Informacion_del_conductor();
        ListasCargueModel.InfoDelVehiculo informacionVehiculo = listasCargueModel.getItem_3_Informacion_vehiculo();
        ListasCargueModel.EstadoDelCargue estadoCargue = listasCargueModel.getItem_4_Estado_cargue();
        String nombreFirma1 = listasCargueModel.getNombre_Supervisor();
        String nombreFirma2 = listasCargueModel.getNombre_Despachador();
        String nombreFirma3 = listasCargueModel.getNombre_Conductor();
        String nombreFirma4 = listasCargueModel.getNombre_Operador();
        String firma1 = listasCargueModel.getFirma_Supervisor();
        String firma2 = listasCargueModel.getFirma_Despachador();
        String firma3 = listasCargueModel.getFirma_Conductor();
        String firma4 = listasCargueModel.getFirma_Operador();

        //Obtener Fecha (Actual y Fecha creación lista)
        LocalDate fechaActual = LocalDate.now(); //Fecha actual
        String fechaString = informacionLugarCargue.getFecha(); //Fecha Lista
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy"); //Formato Fecha
        LocalDate fechaLista = null;

        if (fechaString != null && !fechaString.isEmpty()) {
            fechaLista = LocalDate.parse(fechaString, formatter);
        }

        int camposLlenos = 0;

        // Verificar campos de información del lugar de carga (6)
        if (informacionLugarCargue.getFecha() != null && !informacionLugarCargue.getFecha().isEmpty())
            camposLlenos++;
        if (informacionLugarCargue.getHoraEntrada() != null && !informacionLugarCargue.getHoraEntrada().isEmpty())
            camposLlenos++;
        if (!informacionLugarCargue.getTipoCargue().isEmpty())
            camposLlenos++;
        if (!informacionLugarCargue.getNombreZona().isEmpty())
            camposLlenos++;
        if (!informacionLugarCargue.getNombreNucleo().isEmpty())
            camposLlenos++;
        if (!informacionLugarCargue.getNombreFinca().isEmpty())
            camposLlenos++;

        // Verificar campos de información del conductor (10)
        if (!informacionConductor.getNombreConductor().isEmpty())
            camposLlenos++;
        if (!informacionConductor.getCedula().isEmpty()) camposLlenos++;
        if (!informacionConductor.getLugarExpedicion().isEmpty())
            camposLlenos++;
        if (!informacionConductor.getLicConduccionRes().isEmpty())
            camposLlenos++;
        if (!informacionConductor.getEpsRes().isEmpty()) camposLlenos++;
        if (!informacionConductor.getCualEPS().isEmpty()) camposLlenos++;
        if (!informacionConductor.getArlRes().isEmpty()) camposLlenos++;
        if (!informacionConductor.getCualARL().isEmpty()) camposLlenos++;
        if (!informacionConductor.getAfpRes().isEmpty()) camposLlenos++;
        if (!informacionConductor.getCualAFP().isEmpty()) camposLlenos++;

        // Verificar campos de información del vehículo (28)
        if (!informacionVehiculo.getPlaca().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getVehiculo().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getTarjetaPropiedad().isEmpty())
            camposLlenos++;
        if (!informacionVehiculo.getSoatVigente().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getRevisionTecnicomecanica().isEmpty())
            camposLlenos++;
        if (!informacionVehiculo.getLucesAltas().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getLucesBajas().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getDireccionales().isEmpty())
            camposLlenos++;
        if (!informacionVehiculo.getSonidoReversa().isEmpty())
            camposLlenos++;
        if (!informacionVehiculo.getReversa().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getStop().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getRetrovisores().isEmpty())
            camposLlenos++;
        if (!informacionVehiculo.getPlumillas().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getEstadoPanoramicos().isEmpty())
            camposLlenos++;
        if (!informacionVehiculo.getEspejos().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getBocina().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getCinturon().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getFreno().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getLlantas().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getBotiquin().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getExtintorABC().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getBotas().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getChaleco().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getCasco().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getCarroceria().isEmpty()) camposLlenos++;
        if (!informacionVehiculo.getDosEslingasBanco().isEmpty())
            camposLlenos++;
        if (!informacionVehiculo.getDosConosReflectivos().isEmpty())
            camposLlenos++;
        if (!informacionVehiculo.getParales().isEmpty()) camposLlenos++;
        String observaciones = informacionVehiculo.getObservacionesCamion();

        // Verificar campos Estado del cargue (10)
        if (estadoCargue.getHoraSalida() != null && !estadoCargue.getHoraSalida().isEmpty())
            camposLlenos++;
        if (estadoCargue.getFotoCamion() != null && !estadoCargue.getFotoCamion().isEmpty())
            camposLlenos++;
        if (!estadoCargue.getMaderaNoSuperaMampara().isEmpty())
            camposLlenos++;
        if (!estadoCargue.getMaderaNoSuperaParales().isEmpty())
            camposLlenos++;
        if (!estadoCargue.getNoMaderaAtraviesaMampara().isEmpty())
            camposLlenos++;
        if (!estadoCargue.getParalesMismaAltura().isEmpty()) camposLlenos++;
        if (!estadoCargue.getNingunaUndSobrepasaParales().isEmpty())
            camposLlenos++;
        if (!estadoCargue.getCadaBancoAseguradoEslingas().isEmpty())
            camposLlenos++;
        if (!estadoCargue.getConductorSalioLugarCinturon().isEmpty())
            camposLlenos++;
        if (!estadoCargue.getParalesAbatiblesAseguradosEstrobos().isEmpty())
            camposLlenos++;

        // Verificar campos Firmas y Nombres de Firmas(8)
        if (firma1 != null && !firma1.isEmpty()) camposLlenos++;
        if (firma2 != null && !firma2.isEmpty()) camposLlenos++;
        if (firma3 != null && !firma3.isEmpty()) camposLlenos++;
        if (firma4 != null && !firma4.isEmpty()) camposLlenos++;
        if (nombreFirma1 != null && !nombreFirma1.isEmpty()) camposLlenos++;
        if (nombreFirma2 != null && !nombreFirma2.isEmpty()) camposLlenos++;
        if (nombreFirma3 != null && !nombreFirma3.isEmpty()) camposLlenos++;
        if (nombreFirma4 != null && !nombreFirma4.isEmpty()) camposLlenos++;

        // Determinar el estado de la lista
        String estado;
        boolean fechaEnProceso = false;
        if (fechaLista != null) {
            fechaEnProceso = fechaLista.isEqual(fechaActual);
        }

        if (camposLlenos == 62 && (observaciones.isEmpty() || !observaciones.isEmpty())) { // Todos los campos llenos
            estado = "Completa";
        } else if (camposLlenos > 0 && fechaEnProceso) {
            estado = "En proceso";

        } else if ((camposLlenos < 62) && fechaLista != null && fechaLista.isBefore(fechaActual) && (observaciones.isEmpty() || !observaciones.isEmpty())) {
            estado = "Incompleta";
        } else {
            estado = "Vacía";
        }
        listasCargueModel.setEstadoLista(estado);
    }
    //----------------------- FIN METODOS PARA ORDENAR Y VERIFICAR EL ESTADO DE LA LISTA --------------------------------//



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
            if (menuItem.getItemId() == MENU_ITEM_ELIMINAR) {
                if (isAdded() && getActivity() != null && !getActivity().isFinishing() && !getActivity().isDestroyed()) {
                    eliminarLista();
                }
                mode.finish(); // Cierra el modo de acción contextual después de mostrar el diálogo
                return true;
            }
            return false;


            //Exportar lista en cola

            /*
            else if (menuItem.getItemId() == R.id.menu_item_exportar) {

                // Hacer algo para el caso de exportar
                mode.finish(); // Cierra el modo de acción contextual después de hacer algo
                return true;
            }
             */
            //else {
            //    return false;
            //}
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // Limpiar las selecciones al salir del modo de acción contextual
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
        if (loadMoreListener != null) {
            loadMoreListener.remove();
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
