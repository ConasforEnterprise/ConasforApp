package com.example.conasforapp.Usuarios

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.conasforapp.R
import com.example.conasforapp.Main.MainActivity
import com.example.conasforapp.modelos.UsuariosModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import java.util.regex.Pattern

class RegistroFragment : Fragment() {

    private lateinit var rootView : View
    var db: FirebaseFirestore? = null
    var btnAgregarFotoUsuarioRegistro: FloatingActionButton? = null

    var txtContrasena: TextInputLayout? = null
    var edtNombreRegistro: TextInputEditText? = null
    var edtCedulaRegistro:TextInputEditText? = null
    var edtCorreoRegistro:TextInputEditText? = null

    var edtContrasenaRegistro: TextInputEditText? = null
    var edtFincaRegistro:TextInputEditText? = null
    var btnRegistroUsuario: Button? = null

    var atcCargo: AutoCompleteTextView? = null
    var mAuth: FirebaseAuth? = null
    val pathUsuarios = "Usuarios"
    private var nombre: String? = null
    private var cargo:String? = null
    private var cedula:String? = null
    private var finca:String? = null
    private var correo:String? = null
    var contrasena:String? = null
    var imgFotoUsuarioRegistro: ImageView? = null
    val datosUsuarioRegistro: HashMap<String, Any> = HashMap()
    var downloadUri = ""
    var fotoPerfilUri: Uri? = null

    class RegistroUsuario {
        companion object {
            const val CODE_GALERY = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_registro, container, false)

        db = FirebaseFirestore.getInstance()

        mAuth = FirebaseAuth.getInstance()

        txtContrasena = rootView.findViewById(R.id.txtContrasenaRegistro)
        edtNombreRegistro = rootView.findViewById(R.id.edtNombreRegistro)
        atcCargo = rootView.findViewById(R.id.atcCargoRegistro)
        edtFincaRegistro = rootView.findViewById(R.id.edtFincaRegistro)
        edtCedulaRegistro = rootView.findViewById(R.id.edtCedulaRegistro)
        edtCorreoRegistro = rootView.findViewById(R.id.edtCorreoRegistro)
        edtContrasenaRegistro = rootView.findViewById(R.id.edtContrasenaRegistro)
        btnRegistroUsuario = rootView.findViewById(R.id.btnRegistroUsuario)
        imgFotoUsuarioRegistro = rootView.findViewById(R.id.imgFotoUusuarioRegistro)
        btnAgregarFotoUsuarioRegistro = rootView.findViewById(R.id.btnAgregarFotoUsuarioRegistro)

        btnRegistroUsuario!!.setOnClickListener {
            if (verificarCampos()) {
                registroUsuario()
            }
        }

        btnAgregarFotoUsuarioRegistro!!.setOnClickListener { cargarFoto() }

        obtenerCargos()

        return rootView
    }


    private fun registroUsuario() {
        nombre = edtNombreRegistro!!.text.toString()
        cargo = atcCargo!!.text.toString()
        cedula = edtCedulaRegistro!!.text.toString()
        finca = edtFincaRegistro!!.text.toString()
        correo = edtCorreoRegistro!!.text.toString().trim { it <= ' ' }
        contrasena = edtContrasenaRegistro!!.text.toString()

        Log.d("RegistroUsuario", "Correo electrónico proporcionado: $correo")

        if (!nombre!!.isEmpty() && !cargo!!.isEmpty() && !cedula!!.isEmpty() && !correo!!.isEmpty() && !contrasena!!.isEmpty()) {
            if (isEmailValido(correo) && contrasena!!.length >= 6) {
                crearUsuario(nombre!!, cargo!!, cedula!!,finca!!, correo!!, contrasena!!)
            } else {
                Toast.makeText(context, "La contraseña debe tener al menos 6 caractéres", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Debes completar todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun crearUsuario(
        nombre: String,
        cargo: String,
        cedula: String,
        finca: String,
        correo: String,
        contrasena: String
    ) {
        mAuth!!.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val id = mAuth!!.currentUser!!.uid

                datosUsuarioRegistro["nombre"] = nombre
                datosUsuarioRegistro["cargo"] = cargo
                datosUsuarioRegistro["cedula"] = cedula
                datosUsuarioRegistro["finca"] = finca
                datosUsuarioRegistro["correo"] = correo
                datosUsuarioRegistro["contrasena"] = contrasena

                db!!.collection(pathUsuarios).document(id).set(datosUsuarioRegistro)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val usuariosModel = UsuariosModel()
                            usuariosModel.id_usuario_firebase = id
                            usuariosModel.nombre = nombre
                            usuariosModel.cargo = cargo
                            usuariosModel.finca = finca
                            usuariosModel.cedula = cedula
                            usuariosModel.correo = correo
                            usuariosModel.contrasena = contrasena

                            val mainActivity = Intent(context, MainActivity::class.java)
                            startActivity(mainActivity)
                            Toast.makeText(context, "Se registró el usuario con éxito", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context,"No se pudo registrar el usuario en Firestore", Toast.LENGTH_SHORT).show()
                            Log.e("RegistroUsuario", "Error al registrar usuario en Firestore: " + task.exception!!.message)
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, "No se pudo registrar el usuario en Firebase Authentication", Toast.LENGTH_SHORT).show()
                        Log.e("RegistroUsuario", "Error al registrar usuario en Firebase Authentication: " + task.exception!!.message)
                    }
            } else {
                Toast.makeText(context, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show()
                Log.e("RegistroUsuario", "Error al registrar usuario: " + task.exception!!.message)
            }
        }.addOnFailureListener { e ->
            Log.e("RegistroUsuario", "Error onFailure: " + e.message)
        }
    }

    private fun cargarFoto() {
        val nombreUsuario = edtNombreRegistro!!.text.toString().trim { it <= ' ' }
        val cargoUsuario = atcCargo!!.text.toString().trim { it <= ' ' }

        if (!nombreUsuario.isEmpty() && !cargoUsuario.isEmpty()) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            startActivityForResult(
                Intent.createChooser(intent, "Seleccione la Aplicación"),
                RegistroUsuario.CODE_GALERY
            )
        } else {
            Toast.makeText(context, "Debes ingresar un nombre y un cargo para cargar la foto", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RegistroUsuario.CODE_GALERY) {
                if (data != null) {
                    val urlImagen = data.data
                    imgFotoUsuarioRegistro!!.setImageURI(urlImagen)
                    fotoPerfilUri = urlImagen

                    val nombreUsuario = edtNombreRegistro!!.text.toString()
                    val cargoUsuario = atcCargo!!.text.toString()

                    val rutaFotoPerfil =
                        "foto_perfil_" + nombreUsuario + "_" + cargoUsuario + ".jpg"

                    val imagenRef = FirebaseStorage.getInstance().reference.child("Fotos Usuarios Registrados")
                        .child(rutaFotoPerfil)

                    imagenRef.putFile(urlImagen!!).addOnSuccessListener { taskSnapshot ->
                        val uriTask = taskSnapshot.storage.downloadUrl
                        while (!uriTask.isSuccessful);
                        if (uriTask.isSuccessful) {
                            uriTask.addOnSuccessListener { uri ->
                                downloadUri = uri.toString()
                                datosUsuarioRegistro.put("fotoUsuario", downloadUri)
                                Log.d("TAG FOTO PERFIL", "FOTO PERFIL : $downloadUri")
                                Toast.makeText(context, "Imagen cargada", Toast.LENGTH_SHORT).show()
                            }.addOnSuccessListener {
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Ocurrió un error inesperado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun obtenerCargos() {
        db!!.collection("Cargos").get()
            .addOnSuccessListener(object : OnSuccessListener<QuerySnapshot> {
                var cargosList: MutableList<String> = ArrayList()
                override fun onSuccess(queryDocumentSnapshots: QuerySnapshot) {
                    for (document in queryDocumentSnapshots) {
                        val cargo = document["cargo"] as List<String>?
                        if (cargo != null) {
                            cargosList.addAll(cargo)
                        }
                    }

                    // Configurar el ArrayAdapter para el AutocompleteTextView
                    val adapter = context?.let {
                        ArrayAdapter<String>(
                            it,
                            android.R.layout.simple_dropdown_item_1line,
                            cargosList
                        )
                    }

                    // Obtener referencia al AutocompleteTextView desde el layout
                    val autoCompleteTextView: AutoCompleteTextView =
                        rootView.findViewById<AutoCompleteTextView>(R.id.atcCargoRegistro)

                    // Establecer el ArrayAdapter en el AutocompleteTextView
                    autoCompleteTextView.setAdapter(adapter)
                }
            })
            .addOnFailureListener { e -> // Manejar errores
                println("Error al obtener los datos: $e")
            }
    }

    private fun verificarCampos(): Boolean {
        if (TextUtils.isEmpty(edtNombreRegistro!!.text)) {
            showMessage("Debes ingresar tu nombre")
            return false
        } else if (TextUtils.isEmpty(atcCargo!!.text)) {
            showMessage("Debes ingresar tu cargo en la empresa")
            return false
        } else if (TextUtils.isEmpty(edtCedulaRegistro!!.text)) {
            showMessage("Debes ingresar tu número de cédula")
            return false
        } else if (TextUtils.isEmpty(edtCorreoRegistro!!.text)) {
            showMessage("Debes ingresar un correo electrónico")
            return false
        } else if (TextUtils.isEmpty(edtContrasenaRegistro!!.text)) {
            showMessage("Debes ingresar una contraseña de 6 caracteres mínimo")
            return false
        } else if (fotoPerfilUri == null) {
            showMessage("Debes agregar una foto de perfil")
            return false
        } else {
            Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun showMessage(msj: String) {
        Toast.makeText(context, msj, Toast.LENGTH_SHORT).show()
    }

    fun isEmailValido(correo: String?): Boolean {
        val expresion = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expresion, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(correo)
        return matcher.matches()
    }
}