package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Adaptadores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.conasforapp.R;
import com.example.conasforapp.modelos.ListasCargueModel;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorMisListasCargue extends RecyclerView.Adapter<AdaptadorMisListasCargue.MyViewHolder> {
    private Context mContext;
    private List<ListasCargueModel> mData;
    private SparseBooleanArray selectedItems;
    private List<ListasCargueModel> listasCargueModelList = new ArrayList<>();
    private List<ListasCargueModel> listasDatos = new ArrayList<>();
    private List<ListasCargueModel> oldListas = new ArrayList<>();

    public interface OnListClickListener {
        void onListClick(int position, ListasCargueModel lista);
    }
    private OnListClickListener onListClickListener;

    //Constructor
    public AdaptadorMisListasCargue(Context mContext, List <ListasCargueModel> mData){
        this.mContext = mContext;
        this.mData = mData;
        //-----this.mData = new ArrayList<>(mData);
        this.selectedItems = new SparseBooleanArray();
        this.oldListas = new ArrayList<>(mData);
        this.onListClickListener = onListClickListener;
    }

    // Método para mostrar las listas coincidentes
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


    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public AdaptadorMisListasCargue.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_listas_chequeo,parent,false);

        LocalBroadcastManager.getInstance(mContext).registerReceiver(actualizarListaFiltradaReceiver, intentFilter);
        return new MyViewHolder(view);
    }

    // Método para establecer los nuevos datos en el adaptador
    public void setListas(List<ListasCargueModel> newListas) {
        ListaDiffCallback diffCallback = new ListaDiffCallback();

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldListas.size();
            }

            @Override
            public int getNewListSize() {
                return newListas.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return diffCallback.areItemsTheSame(oldListas.get(oldItemPosition), newListas.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return diffCallback.areContentsTheSame(oldListas.get(oldItemPosition), newListas.get(newItemPosition));
            }
        });

        oldListas = new ArrayList<>(newListas);
        mData = newListas;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (listasDatos != null && position >= 0 && position < mData.size()) {
        //if (mData != null && position >= 0 && position < mData.size()) {
            ListasCargueModel listas = mData.get(position);

            int numeroLista = mData.size() - position;
            int colorRojo = ContextCompat.getColor(mContext, R.color.rojo);
            int colorNegro = ContextCompat.getColor(mContext, R.color.black);

            String estadoLista = listas.getEstadoLista();
            listas.setLista_numero(String.valueOf(numeroLista));

            holder.txtNombreConductor.setText(listas.getItem_2_Informacion_del_conductor().getNombreConductor());
            holder.txtHoraLlegada.setText(listas.getItem_1_Informacion_lugar_cargue().getHoraEntrada());
            holder.txtHoraSalida.setText(listas.getItem_4_Estado_cargue().getHoraSalida());
            holder.txtFecha.setText(listas.getItem_1_Informacion_lugar_cargue().getFecha());

            holder.txtPlacaVehiculo.setText(listas.getItem_3_Informacion_vehiculo().getPlaca());

            if(estadoLista.equals("Incompleta")){
                holder.txtEstadoLista.setTextColor(colorRojo);
                holder.txtEstadoLista.setText(listas.getEstadoLista());
            }
            else{
                holder.txtEstadoLista.setTextColor(colorNegro);
                holder.txtEstadoLista.setText(listas.getEstadoLista());
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ListasCargueModel getItemListas(int position) {
        if (position < 0 || position >= mData.size()) {
            throw new IndexOutOfBoundsException("Posición inválida: " + position);
        }
        return mData.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNumeroLista, txtFecha, txtHoraLlegada, txtHoraSalida, txtFinca, txtNombreConductor, txtNombreSupervisor;
        CardView cVListasCargueDescargue;
        TextView txtPlacaVehiculo, txtEstadoLista;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //txtNumeroLista = itemView.findViewById(R.id.txtNumeroListaCard);
            txtFecha = itemView.findViewById(R.id.txtFechaListaCargueCard);
            txtHoraLlegada = itemView.findViewById(R.id.txtHoraEntradaCard);
            txtHoraSalida = itemView.findViewById(R.id.txtHoraSalidaCard);
            txtNombreConductor = itemView.findViewById(R.id.txtNombreConductorCard);
            txtPlacaVehiculo = itemView.findViewById(R.id.txtPlacaVehiculoCard);
            txtEstadoLista = itemView.findViewById(R.id.txtEstadoLista);
            cVListasCargueDescargue = itemView.findViewById(R.id.cardViewListaCreada);

            cVListasCargueDescargue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onListClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            ListasCargueModel lista = mData.get(position);
                            onListClickListener.onListClick(position, lista);
                        }
                    }
                }
            });
            cVListasCargueDescargue.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    toggleSelection(getAdapterPosition());
                    return false;
                }
            });
        }
    }
}
