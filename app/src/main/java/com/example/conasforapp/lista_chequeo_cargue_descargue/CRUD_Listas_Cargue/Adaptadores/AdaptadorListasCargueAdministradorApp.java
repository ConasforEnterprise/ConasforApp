package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Adaptadores;//package com.example.conasforapp.lista_chequeo_cargue_descargue.Items_lista;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conasforapp.R;
import com.example.conasforapp.modelos.ListasCargueModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListasCargueAdministradorApp extends RecyclerView.Adapter<AdaptadorListasCargueAdministradorApp.MyViewHolder>{
    private Context mContext;
    private List<ListasCargueModel> mData;
    private SparseBooleanArray selectedItems;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private List<ListasCargueModel> oldListas = new ArrayList<>();

    public AdaptadorListasCargueAdministradorApp(Context mContext, List<ListasCargueModel> mData){
        this.mContext = mContext;
        this.mData = mData;
        this.selectedItems = new SparseBooleanArray();
    }

    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public void mostrarListasCoincidentes(List<ListasCargueModel> listasCoincidentes) {
        mData = listasCoincidentes; // Actualizar los datos del adaptador con las listas coincidentes
        notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado y se debe actualizar la vista
    }

    BroadcastReceiver actualizarListaFiltradaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Aquí va la lógica para actualizar la lista filtrada
            List<ListasCargueModel> listasFiltradas = (List<ListasCargueModel>) intent.getSerializableExtra("listaFiltrada");
            if (listasFiltradas != null) { // Verificar si la lista filtrada no es null
                mData.clear(); // Limpiar los datos actuales del adaptador
                mData.addAll(listasFiltradas); // Agregar los nuevos datos
                notifyDataSetChanged();
            }
        }
    };

    IntentFilter intentFilter = new IntentFilter("actualizar_lista_filtrada");

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    // Método para obtener un item de la lista filtrada
    public ListasCargueModel getItem(int position) {
        return mData.get(position);
    }

    public void actualizarListas(List<ListasCargueModel> nuevasListas) {
        ListaDiffCallback diffCallback = new ListaDiffCallback();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldListas.size();
            }

            @Override
            public int getNewListSize() {
                return nuevasListas.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return diffCallback.areItemsTheSame(oldListas.get(oldItemPosition), nuevasListas.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return diffCallback.areContentsTheSame(oldListas.get(oldItemPosition), nuevasListas.get(newItemPosition));
            }
        });

        oldListas = new ArrayList<>(nuevasListas);
        mData = nuevasListas;
        diffResult.dispatchUpdatesTo(this);
    }

    // Clase que implementa DiffUtil.ItemCallback<ListaModel>
    class ListaDiffCallback extends DiffUtil.ItemCallback<ListasCargueModel> {
        @Override
        public boolean areItemsTheSame(@NonNull ListasCargueModel oldItem, @NonNull ListasCargueModel newItem) {
            // Devuelve true si los objetos representan el mismo elemento
            return oldItem.getId_lista().equals(newItem.getId_lista());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ListasCargueModel oldItem, @NonNull ListasCargueModel newItem) {
            // Devuelve true si los contenidos de los objetos son iguales
            return oldItem.equals(newItem);
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    @NonNull
    @Override
    public AdaptadorListasCargueAdministradorApp.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_listas_cargue_admin,parent,false);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(actualizarListaFiltradaReceiver, intentFilter);
        return new AdaptadorListasCargueAdministradorApp.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorListasCargueAdministradorApp.MyViewHolder holder, int position) {

        String uid = currentUser.getUid();
        Log.d("TAG ID","ID : " + uid);

        if (mData != null && position >= 0 && position < mData.size()) {

            ListasCargueModel listas = mData.get(position);

            holder.txtNombreConductor.setText(listas.getItem_2_Informacion_del_conductor().getNombreConductor());
            holder.txtFinca.setText(listas.getItem_1_Informacion_lugar_cargue().getNombreFinca());
            holder.txtHoraLlegada.setText(listas.getItem_1_Informacion_lugar_cargue().getHoraEntrada());
            holder.txtHoraSalida.setText(listas.getItem_4_Estado_cargue().getHoraSalida());
            //holder.txtNumeroLista.setText(listas.getLista_numero());
            holder.txtFecha.setText(listas.getItem_1_Informacion_lugar_cargue().getFecha());
            holder.txtNombreSupervisor.setText(listas.getNombre());
            holder.txtPlacaVehiculo.setText(listas.getItem_3_Informacion_vehiculo().getPlaca());

            String urlImagen = listas.getFotoUsuario();


            Log.d("TAG URL","URL IMAGES : " + urlImagen);

            if (!urlImagen.isEmpty()) {
            Picasso.with(mContext)
                    .load(urlImagen)
                    .resize(100,100)
                    .centerInside()
                    .into(holder.imgFotoSupervisor);
            }

            else {
                holder.imgFotoSupervisor.setImageResource(R.drawable.icono_foto_usuario);
                Toast.makeText(mContext, "Hay usuarios sin foto de perfil", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumeroLista, txtFecha, txtHoraLlegada, txtHoraSalida, txtFinca, txtNombreConductor, txtNombreSupervisor;
        TextView txtPlacaVehiculo;
        CardView cVListasCargueDescargueAdmin;
        ImageView imgFotoSupervisor;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombreSupervisor = itemView.findViewById(R.id.txtNombreSupervisorAdmin);
            //txtNumeroLista = itemView.findViewById(R.id.txtNumeroListaCardAdmin);
            txtFecha = itemView.findViewById(R.id.txtFechaListasAdmin);
            txtHoraLlegada = itemView.findViewById(R.id.txtHoraEntradaCardAdmin);
            txtHoraSalida = itemView.findViewById(R.id.txtHoraSalidaCardAdmin);
            txtFinca = itemView.findViewById(R.id.txtNombreFincaCardAdmin);
            txtNombreConductor = itemView.findViewById(R.id.txtNombreConductorCardAdmin);
            imgFotoSupervisor = itemView.findViewById(R.id.imgUsuarioCardAdmin);
            txtPlacaVehiculo = itemView.findViewById(R.id.txtPlacaCardAdmin);

            cVListasCargueDescargueAdmin = itemView.findViewById(R.id.cardViewListasAdmin);
            cVListasCargueDescargueAdmin.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    toggleSelection(getAdapterPosition());

                    return false;
                }
            });
        }
    }
}

