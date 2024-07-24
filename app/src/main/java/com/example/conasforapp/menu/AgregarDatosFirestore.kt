package com.example.conasforapp.menu

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.conasforapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class AgregarDatosFirestore : Fragment() {

    lateinit var rootView : View
    lateinit var frame_DatosNuevos : FrameLayout
    var bottomSheetBehavior: BottomSheetBehavior<View>? = null
    private lateinit var btnAddTipoCargue : Button
    private lateinit var btnAddZona : Button
    private lateinit var btnAddNucleo : Button
    private lateinit var btnAddFinca : Button
    private lateinit var btnAddCargo : Button
    private lateinit var btnAddEps : Button
    private lateinit var btnAddArl : Button
    private lateinit var btnAddAfp : Button
    private lateinit var btnAddVehiculo : Button
    private lateinit var btnAgregarDatos : Button
    private lateinit var edtDatosNuevos : TextInputEditText
    private lateinit var txtDatosNuevos : TextInputLayout

    private val db = FirebaseFirestore.getInstance()
    private lateinit var txtEncabezado: TextView
    private lateinit var imgAtras: ImageView
    private var lottieAnimationAddDatosFirestore: LottieAnimationView? = null
    private lateinit var cardAddDatosFirestore : CardView

    private var currentArray: String? = null
    private var currentCollection: String? = null
    private var currentDocumentId: String? = null
    private val documentIdDesInfoConductor = "oOhezIl7QpWqs9bZNSOQ"
    private val documentIdDesInfoVehiculo = "d1KKNClWMOL3E238PtSN"
    private val documentIdDesInfoLugar = "YPs7ZJGi8OptLJCsEG7z"
    private val documentIdCargos = "MTTociTuW5T70RF7UvGp"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_agregar_datos_firestore, container, false)

        frame_DatosNuevos = rootView.findViewById(R.id.frameDatosNuevos)

        //imgAtras = rootView.findViewById(R.id.imgBtnAtrasAdicionarDatos)
        btnAddTipoCargue = rootView.findViewById(R.id.btnAddTipoCargue)
        btnAddZona = rootView.findViewById(R.id.btnAddZona)
        btnAddNucleo = rootView.findViewById(R.id.btnAddNucleo)
        btnAddFinca = rootView.findViewById(R.id.btnAddFinca)
        btnAddCargo = rootView.findViewById(R.id.btnAddCargo)
        btnAddEps = rootView.findViewById(R.id.btnAddEPS)
        btnAddArl = rootView.findViewById(R.id.btnAddARL)
        btnAddAfp = rootView.findViewById(R.id.btnAddAFP)
        btnAddVehiculo = rootView.findViewById(R.id.btnAddVehiculo)
        btnAgregarDatos = rootView.findViewById(R.id.btnAgregarDatosNuevos)
        txtEncabezado = rootView.findViewById(R.id.txtEncabezadoDatosNuevos)
        txtDatosNuevos = rootView.findViewById(R.id.txtDatosNuevos)
        edtDatosNuevos = rootView.findViewById(R.id.edtDatosNuevos)

        bottomSheetBehavior = BottomSheetBehavior.from(frame_DatosNuevos)
        bottomSheetBehavior!!.setPeekHeight(0)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior!!.isDraggable = true
        bottomSheetBehavior!!.isHideable = true

        lottieAnimationAddDatosFirestore = rootView.findViewById(R.id.lottieAnimationAddDatosFirestore)
        cardAddDatosFirestore = rootView.findViewById(R.id.cardAddDatosFirestore)

        btnAddTipoCargue.setOnClickListener{
            openBottomSheet("tipo de cargue", "desplegables infoLugar",documentIdDesInfoLugar)
            edtDatosNuevos.text!!.clear()
            //txtDatosNuevos.hint = "Nuevo tipo de cargue"
            val newIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.icono_tipo_cargue2) }
            txtDatosNuevos.startIconDrawable = newIcon
        }

        btnAddZona.setOnClickListener{
            openBottomSheet("nombre zona", "desplegables infoLugar",documentIdDesInfoLugar)
            edtDatosNuevos.text!!.clear()
            //txtDatosNuevos.hint = "Nueva zona"
            val newIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.icono_nombre_zona2) }
            txtDatosNuevos.startIconDrawable = newIcon
        }

        btnAddNucleo.setOnClickListener{
            openBottomSheet("nombre nucleo", "desplegables infoLugar",documentIdDesInfoLugar)
            edtDatosNuevos.text!!.clear()
            //txtDatosNuevos.hint = "Nuevo núcleo"
            val newIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.icono_nombre_nucleo) }
            txtDatosNuevos.startIconDrawable = newIcon
        }

        btnAddFinca.setOnClickListener{
            openBottomSheet("nombre finca", "desplegables infoLugar",documentIdDesInfoLugar)
            edtDatosNuevos.text!!.clear()
            //txtDatosNuevos.hint = "Nueva finca"
            val newIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.icono_nombre_finca2) }
            txtDatosNuevos.startIconDrawable = newIcon
        }

        btnAddCargo.setOnClickListener{
            openBottomSheet("cargo", "Cargos",documentIdCargos)
            edtDatosNuevos.text!!.clear()
            //txtDatosNuevos.hint = "Nuevo cargo"
            val newIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.icono_usuario) }
            txtDatosNuevos.startIconDrawable = newIcon
        }

        btnAddEps.setOnClickListener {
            openBottomSheet("EPS", "desplegables info conductor",documentIdDesInfoConductor)
            edtDatosNuevos.text!!.clear()
            //txtDatosNuevos.hint = "Nueva EPS"
            val newIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.icono_usuario) }
            txtDatosNuevos.startIconDrawable = newIcon
        }

        btnAddArl.setOnClickListener {
            openBottomSheet("ARL", "desplegables info conductor",documentIdDesInfoConductor)
            edtDatosNuevos.text!!.clear()
            //txtDatosNuevos.hint = "Nueva ARL"
            val newIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.icono_usuario) }
            txtDatosNuevos.startIconDrawable = newIcon
        }

        btnAddAfp.setOnClickListener {
            openBottomSheet("AFP", "desplegables info conductor",documentIdDesInfoConductor)
            edtDatosNuevos.text!!.clear()
            //txtDatosNuevos.hint = "Nueva AFP"
            val newIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.icono_usuario) }
            txtDatosNuevos.startIconDrawable = newIcon
        }

        btnAddVehiculo.setOnClickListener {
            openBottomSheet("vehiculo", "desplegables info vehiculo",documentIdDesInfoVehiculo)
            edtDatosNuevos.text!!.clear()
            //txtDatosNuevos.hint = "Nuevo vehículo"
            val newIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.icono_tipo_vehiculo) }
            txtDatosNuevos.startIconDrawable = newIcon
        }

        btnAgregarDatos.setOnClickListener {
            updateData()
        }

        return rootView
    }

    private fun openBottomSheet(arrayName: String, collectionName: String, documentId: String) {
        currentArray = arrayName
        currentCollection = collectionName
        currentDocumentId = documentId // Guarda el ID del documento actual
        txtEncabezado.text = "Agregar $arrayName"

        if(arrayName.equals("EPS") || arrayName.equals("ARL") || arrayName.equals("AFP")){
            txtDatosNuevos.hint = "Nueva $arrayName"
        }
        else{
            txtDatosNuevos.hint = "Nuevo $arrayName"
        }

        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
    }

    // Actualiza la función updateData para usar currentCollection y currentDocumentId
    private fun updateData() {
        val nuevoDato = edtDatosNuevos.text.toString()
        if (nuevoDato.isNotEmpty() && currentArray != null && currentCollection != null && currentDocumentId != null) {
            db.collection(currentCollection!!).document(currentDocumentId!!)
                .update(currentArray!!, FieldValue.arrayUnion(nuevoDato))
                .addOnSuccessListener {
                    // Mostrar la animación de Lottie
                    lottieAnimationAddDatosFirestore?.visibility = View.VISIBLE
                    lottieAnimationAddDatosFirestore?.playAnimation()
                    cardAddDatosFirestore?.visibility = View.VISIBLE

                    // Ocultar el formulario de datos
                    //frameDatosNuevos?.visibility = View.GONE

                    // Esperar a que termine la animación de Lottie antes de ocultar los elementos
                    Handler().postDelayed({
                        lottieAnimationAddDatosFirestore?.visibility = View.GONE
                        cardAddDatosFirestore?.visibility = View.GONE

                        // Mostrar nuevamente el formulario de datos
                        //frameDatosNuevos?.visibility = View.VISIBLE

                        // Restablecer el estado del BottomSheet
                        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                        edtDatosNuevos?.text?.clear()
                    }, lottieAnimationAddDatosFirestore?.duration ?: 2000L)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }
    }

}