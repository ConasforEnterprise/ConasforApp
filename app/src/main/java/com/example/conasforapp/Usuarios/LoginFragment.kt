package com.example.conasforapp.Usuarios

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.helper.widget.MotionEffect
import androidx.fragment.app.Fragment
import com.example.conasforapp.R
import com.example.conasforapp.menu.MenuNavegacion
import com.example.conasforapp.modelos.UsuariosModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class LoginFragment : Fragment() {

    private lateinit var rootView : View
    private lateinit var btnIngresarApp: Button
    private lateinit var edtCorreo: TextInputEditText
    var edtContrasena:TextInputEditText? = null
    var mAuth: FirebaseAuth? = null
    var correo: String = ""
    var user: FirebaseUser? = null
    var contrasena:kotlin.String? = ""
    lateinit var txtRecuperarContrasena : TextView
    private var mGoogleSignInClient: GoogleSignInClient? = null

    class LoginUsuario {
        companion object {
            const val RC_SIGN_IN = 9001
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false)

        //Persistencia de datos Firebase inicializada
        context?.let { FirebaseApp.initializeApp(it) }
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings

        if (FirebaseFirestore.getInstance().firestoreSettings.isPersistenceEnabled) {
            Log.d("LoginUsuario", "Persistencia de datos habilitada")
        } else {
            Log.d("LoginUsuario", "No se pudo habilitar la persistencia de datos")
        }

        btnIngresarApp = rootView.findViewById(R.id.btnIngresarApp)
        edtCorreo = rootView.findViewById(R.id.edtCorreoLogin)
        edtContrasena = rootView.findViewById(R.id.edtContrasenaLogin)
        edtContrasena!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        txtRecuperarContrasena = rootView.findViewById(R.id.txtRecuperarConstrasena)

        mAuth = FirebaseAuth.getInstance()

        btnIngresarApp.setOnClickListener{
            correo = edtCorreo.getText().toString().trim { it <= ' ' }
            contrasena = edtContrasena!!.text.toString().trim { it <= ' ' }
            Login(correo, contrasena!!)
        }

        txtRecuperarContrasena.setOnClickListener{
            val email = edtCorreo.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(context, "Por favor ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mAuth?.sendPasswordResetEmail(email)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if(isConnectedToInternet()){
                            Toast.makeText(context, "Se envió un correo de recuperación a tu correo electrónico", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Configurar Google Sign In
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN) //.requestIdToken(getString(R.string.default_web_client_id))
                .requestIdToken("911163006858-4p7ek9pgqivr64oupi92cbmudj0c544a.apps.googleusercontent.com")
                .requestEmail()
                .build()

        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        // Se comprueba si el usuario ha iniciado sesión (no nulo) y se actualiza la interfaz de usuario.
        val currentUser = mAuth?.currentUser
        if (currentUser != null) {
            updateUI(currentUser)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LoginUsuario.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d(MotionEffect.TAG, "firebaseAuthWithGoogle:" + account.id)
                account.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(MotionEffect.TAG, "Google sign in failed", e)
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    irHome()
                    Log.d("TAG", "signInWithCredential:success")
                    user = mAuth?.currentUser
                    user?.let {
                        //val firebaseUserId = it.uid
                        updateUI(it)
                    }
                } else {
                    //Si el inicio de sesión falla, que se genere una excepción
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            })
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, LoginUsuario.RC_SIGN_IN)
    }

    private fun updateUI(user: FirebaseUser?) {
        var user: FirebaseUser? = user
        user = mAuth!!.currentUser

        if (user != null) {
            irHome()
        }
    }

    private fun irHome() {
        val menu = Intent(context, MenuNavegacion::class.java)
        startActivity(menu)
        activity?.finish()
    }

    private fun Login(correo: String, contrasena: String) {
        if (!correo.isEmpty() && !contrasena.isEmpty()) {
            if (isConnectedToInternet()) {
                mAuth!!.signInWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val menu = Intent(context, MenuNavegacion::class.java)
                            startActivity(menu)
                            Toast.makeText(context,
                                "Usuario autenticado con éxito",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(context,
                                "El usuario o la contraseña no coinciden",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context,
                            "No se pudo ingresar a Conasfor App",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            /*
            else {
                // Si no hay conexión a Internet, intenta autenticar al usuario utilizando los datos almacenados localmente en SQLite
                val manager = Manager(context)
                val firebaseUserId = ""
                Log.d("USER ID SIN CONEXION", "USER ID SIN CONEXION : $firebaseUserId")
                val usuario = manager.obtenerUsuario(firebaseUserId)
                //if (user != null) {
                if (usuario != null) {
                    val menuNoConexion = Intent(
                        context,
                        MenuNavegacion::class.java
                    )
                    startActivity(menuNoConexion)
                    Log.d("LoginUsuario", "No hay conexión a internet, intentando cargar MenuNavegacion sin conexión")
                } else {
                    // Si no se encuentra el usuario en la base de datos local, muestra un mensaje de error
                    Toast.makeText(
                        context,
                        "El usuario o la contraseña no coinciden",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

             */
        } else {
            Toast.makeText(context, "Por favor, ingrese correo y contraseña", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun isConnectedToInternet(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetworkInfo = connectivityManager?.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}