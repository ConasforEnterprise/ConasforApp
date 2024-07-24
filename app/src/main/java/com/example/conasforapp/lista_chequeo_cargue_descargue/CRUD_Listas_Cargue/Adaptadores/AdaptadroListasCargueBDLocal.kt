package com.example.conasforapp.lista_chequeo_cargue_descargue.CRUD_Listas_Cargue.Adaptadores

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.conasforapp.R
import com.example.conasforapp.modelos.ListasCargueModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AdaptadroListasCargueBDLocal(private var listas: MutableList<ListasCargueModel>, private val context:Context) :
RecyclerView.Adapter<AdaptadroListasCargueBDLocal.ListaViewHolder>() {

    val selectedItems: SparseBooleanArray? = null
    private val oldListas: MutableList<ListasCargueModel> = mutableListOf()
    interface OnListClickListener {
        fun onListClick(position: Int, lista: ListasCargueModel)
    }

    var onListClickListener: OnListClickListener? = null

    // Método para obtener un item de la lista filtrada
    fun getItem(position: Int): ListasCargueModel {
        return listas[position]
    }

    class ListaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtConductor : TextView = itemView.findViewById(R.id.txtNombreConductorCard)
        val txtPlaca : TextView = itemView.findViewById(R.id.txtPlacaVehiculoCard)
        val txtHoraEntrada : TextView = itemView.findViewById(R.id.txtHoraEntradaCard)
        val txtHoraSalida : TextView = itemView.findViewById(R.id.txtHoraSalidaCard)
        //val txtNumeroLista  : TextView = itemView.findViewById(R.id.txtNumeroListaCard)
        val txtFecha  : TextView = itemView.findViewById(R.id.txtFechaListaCargueCard)
        val txtEstadoLista  : TextView = itemView.findViewById(R.id.txtEstadoLista)
        val cVListasCargueDescargue : CardView = itemView.findViewById(R.id.cardViewListaCreada)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_listas_chequeo, parent, false)
        LocalBroadcastManager.getInstance(context).registerReceiver(actualizarListaFiltradaReceiver, intentFilter)
        return ListaViewHolder(view)
    }

    override fun getItemCount(): Int = listas.size

    override fun onBindViewHolder(holder: ListaViewHolder, position: Int) {
        val listas = listas[position]

        estadoLista(listas)
        holder.txtConductor.text = listas.Item_2_Informacion_del_conductor.nombreConductor
        holder.txtPlaca.text = listas.Item_3_Informacion_vehiculo.placa
        holder.txtHoraEntrada.text = listas.Item_1_Informacion_lugar_cargue.horaEntrada
        holder.txtFecha.text = listas.Item_1_Informacion_lugar_cargue.fecha
        holder.txtHoraSalida.text = listas.Item_4_Estado_cargue.horaSalida
        //holder.txtNumeroLista.text = listas.id_lista_local.toString()
        holder.txtEstadoLista.text = listas.estadoLista ?: "Estado no disponible"

        holder.cVListasCargueDescargue.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val lista = listas
                onListClickListener?.onListClick(position, lista)
            }
        }

        holder.cVListasCargueDescargue.setOnLongClickListener(OnLongClickListener {
            toggleSelection(holder.adapterPosition)
            false
        })
    }

    private val actualizarListaFiltradaReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val listasFiltradas = intent.getSerializableExtra("listaFiltrada") as List<ListasCargueModel>?
            if (listasFiltradas != null) {
                Log.d("LISTAS FILTRADAS ADAPTER DB LOCAL", "LISTAS FILTRADAS ADAPTER DB LOCAL: $listasFiltradas")
                listas.clear()
                listas.addAll(listasFiltradas)
                notifyDataSetChanged()
            }
        }
    }

    var intentFilter: IntentFilter = IntentFilter("actualizar_lista_filtrada")

    fun toggleSelection(position: Int) {
        if (selectedItems?.get(position, false) == true) {
            selectedItems.delete(position)
        } else {
            selectedItems?.put(position, true)
        }
        notifyItemChanged(position)
    }

    fun refreshData(newListas : List<ListasCargueModel>){
        //listas = newListas
        listas.clear()
        listas.addAll(newListas)
        notifyDataSetChanged()
    }

    // Método para establecer los nuevos datos en el adaptador
    fun setListas(newListas: List<ListasCargueModel>) {
        val diffCallback = ListaDiffCallback(oldListas, newListas)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        oldListas.clear()
        oldListas.addAll(newListas)

        diffResult.dispatchUpdatesTo(this)
    }

    // Clase que implementa DiffUtil.Callback en lugar de ItemCallback para listas completas
    class ListaDiffCallback(
        private val oldList: List<ListasCargueModel>,
        private val newList: List<ListasCargueModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            //return oldList[oldItemPosition].id_lista_local == newList[newItemPosition].id_lista_local
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    private fun estadoLista(listaModel: ListasCargueModel) {
        // Obtener datos de la lista
        val informacionLugarCargue = listaModel.Item_1_Informacion_lugar_cargue
        val informacionConductor = listaModel.Item_2_Informacion_del_conductor
        val informacionVehiculo = listaModel.Item_3_Informacion_vehiculo
        val estadoCargue = listaModel.Item_4_Estado_cargue
        val nombreFirma1 = listaModel.nombre_Supervisor
        val nombreFirma2 = listaModel.nombre_Despachador
        val nombreFirma3 = listaModel.nombre_Conductor
        val nombreFirma4 = listaModel.nombre_Operador
        val firma1 = listaModel.firma_Supervisor
        val firma2 = listaModel.firma_Despachador
        val firma3 = listaModel.firma_Conductor
        val firma4 = listaModel.firma_Operador

        //Obtener Fecha (Actual y Fecha creación lista)
        val fechaActual = LocalDate.now() //Fecha actual
        val fechaString = informacionLugarCargue.fecha //Fecha Lista
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy") //Formato Fecha
        var fechaLista: LocalDate? = null

        if (fechaString != null && !fechaString.isEmpty()) {
            fechaLista = LocalDate.parse(fechaString, formatter)
        }

        var camposLlenos = 0

        // Verificar campos de información del lugar de carga (6)
        if (informacionLugarCargue.fecha != null && !informacionLugarCargue.fecha!!.isEmpty()) camposLlenos++
        if (informacionLugarCargue.horaEntrada != null && !informacionLugarCargue.horaEntrada!!.isEmpty()) camposLlenos++
        if (!informacionLugarCargue.tipoCargue.isEmpty()) camposLlenos++
        if (!informacionLugarCargue.nombreZona.isEmpty()) camposLlenos++
        if (!informacionLugarCargue.nombreNucleo.isEmpty()) camposLlenos++
        if (!informacionLugarCargue.nombreFinca.isEmpty()) camposLlenos++

        // Verificar campos de información del conductor (10)
        if (!informacionConductor.nombreConductor.isEmpty()) camposLlenos++
        if (!informacionConductor.cedula.isEmpty()) camposLlenos++
        if (!informacionConductor.lugarExpedicion.isEmpty()) camposLlenos++
        if (!informacionConductor.licConduccionRes.isEmpty()) camposLlenos++
        if (!informacionConductor.epsRes.isEmpty()) camposLlenos++
        if (!informacionConductor.cualEPS.isEmpty()) camposLlenos++
        if (!informacionConductor.arlRes.isEmpty()) camposLlenos++
        if (!informacionConductor.cualARL.isEmpty()) camposLlenos++
        if (!informacionConductor.afpRes.isEmpty()) camposLlenos++
        if (!informacionConductor.cualAFP.isEmpty()) camposLlenos++


        // Verificar campos de información del vehículo (28)
        if (!informacionVehiculo.placa.isEmpty()) camposLlenos++
        if (!informacionVehiculo.vehiculo.isEmpty()) camposLlenos++
        if (!informacionVehiculo.tarjetaPropiedad.isEmpty()) camposLlenos++
        if (!informacionVehiculo.soatVigente.isEmpty()) camposLlenos++
        if (!informacionVehiculo.revisionTecnicomecanica.isEmpty()) camposLlenos++
        if (!informacionVehiculo.lucesAltas.isEmpty()) camposLlenos++
        if (!informacionVehiculo.lucesBajas.isEmpty()) camposLlenos++
        if (!informacionVehiculo.direccionales.isEmpty()) camposLlenos++
        if (!informacionVehiculo.sonidoReversa.isEmpty()) camposLlenos++
        if (!informacionVehiculo.reversa.isEmpty()) camposLlenos++
        if (!informacionVehiculo.stop.isEmpty()) camposLlenos++
        if (!informacionVehiculo.retrovisores.isEmpty()) camposLlenos++
        if (!informacionVehiculo.plumillas.isEmpty()) camposLlenos++
        if (!informacionVehiculo.estadoPanoramicos.isEmpty()) camposLlenos++
        if (!informacionVehiculo.espejos.isEmpty()) camposLlenos++
        if (!informacionVehiculo.bocina.isEmpty()) camposLlenos++
        if (!informacionVehiculo.cinturon.isEmpty()) camposLlenos++
        if (!informacionVehiculo.freno.isEmpty()) camposLlenos++
        if (!informacionVehiculo.llantas.isEmpty()) camposLlenos++
        if (!informacionVehiculo.botiquin.isEmpty()) camposLlenos++
        if (!informacionVehiculo.extintorABC.isEmpty()) camposLlenos++
        if (!informacionVehiculo.botas.isEmpty()) camposLlenos++
        if (!informacionVehiculo.chaleco.isEmpty()) camposLlenos++
        if (!informacionVehiculo.casco.isEmpty()) camposLlenos++
        if (!informacionVehiculo.carroceria.isEmpty()) camposLlenos++
        if (!informacionVehiculo.dosEslingasBanco.isEmpty()) camposLlenos++
        if (!informacionVehiculo.dosConosReflectivos.isEmpty()) camposLlenos++
        if (!informacionVehiculo.parales.isEmpty()) camposLlenos++
        val observaciones = informacionVehiculo.observacionesCamion

        // Verificar campos Estado del cargue (10)
        if (estadoCargue.horaSalida != null && !estadoCargue.horaSalida!!.isEmpty()) camposLlenos++
        if (estadoCargue.fotoCamion != null && !estadoCargue.fotoCamion!!.isEmpty()) camposLlenos++
        if (!estadoCargue.maderaNoSuperaMampara.isEmpty()) camposLlenos++
        if (!estadoCargue.maderaNoSuperaParales.isEmpty()) camposLlenos++
        if (!estadoCargue.noMaderaAtraviesaMampara.isEmpty()) camposLlenos++
        if (!estadoCargue.paralesMismaAltura.isEmpty()) camposLlenos++
        if (!estadoCargue.ningunaUndSobrepasaParales.isEmpty()) camposLlenos++
        if (!estadoCargue.cadaBancoAseguradoEslingas.isEmpty()) camposLlenos++
        if (!estadoCargue.conductorSalioLugarCinturon.isEmpty()) camposLlenos++
        if (!estadoCargue.paralesAbatiblesAseguradosEstrobos.isEmpty()) camposLlenos++

        // Verificar campos Firmas y Nombres de Firmas(8)
        if (firma1 != null && !firma1.isEmpty()) camposLlenos++
        if (firma2 != null && !firma2.isEmpty()) camposLlenos++
        if (firma3 != null && !firma3.isEmpty()) camposLlenos++
        if (firma4 != null && !firma4.isEmpty()) camposLlenos++
        if (nombreFirma1 != null && !nombreFirma1.isEmpty()) camposLlenos++
        if (nombreFirma2 != null && !nombreFirma2.isEmpty()) camposLlenos++
        if (nombreFirma3 != null && !nombreFirma3.isEmpty()) camposLlenos++
        if (nombreFirma4 != null && !nombreFirma4.isEmpty()) camposLlenos++

        // Determinar el estado de la lista
        val estado: String
        var fechaEnProceso = false
        if (fechaLista != null) {
            fechaEnProceso = fechaLista.isEqual(fechaActual)
        }

        estado =
            if (camposLlenos == 62 && (observaciones.isEmpty() || !observaciones.isEmpty())) { // Todos los campos llenos
                "Completa"
            } else if (camposLlenos > 0 && fechaEnProceso) {
                "En proceso"
            } else if ((camposLlenos < 62) && (fechaLista != null) && fechaLista.isBefore(
                    fechaActual
                ) && (observaciones.isEmpty() || !observaciones.isEmpty())
            ) {
                "Incompleta"
            } else {
                "Vacía"
            }
        listaModel.estadoLista = estado
        Log.d("ESTADO LISTA MODEL", "ESTADO LISTA MODEL : " + listaModel.estadoLista)
    }
}
