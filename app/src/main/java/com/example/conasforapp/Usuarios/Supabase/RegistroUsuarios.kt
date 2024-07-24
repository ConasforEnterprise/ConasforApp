package com.example.conasforapp.Usuarios.Supabase

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.conasforapp.R
import com.google.android.material.textfield.TextInputEditText
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email


class RegistroUsuarios : AppCompatActivity() {
    private lateinit var client: SupabaseClient  // Cliente de Supabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_usuarios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnRegistrar = findViewById<Button>(R.id.btnRegistroUsuario2)
        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }

        getData()
    }

    fun getClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://zopubgpfftyohbnjaemh.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpvcHViZ3BmZnR5b2hibmphZW1oIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTkyNTE0MDEsImV4cCI6MjAzNDgyNzQwMX0.pUwXvAoo8CbYSZvXMaokbc2hPRX9MFjH19f42BkTcb4"
        ) {
            install(Postgrest)
            install(GoTrue)
            //install(io.github.jan.supabase.postgrest.Postgrest)
        }
    }

    /*
    fun getData(){
        lifecycleScope.launch {
            try {
                val client = getClient()
                val supabaseResponse = client.postgrest["Usuarios"].select()
                val jsonString = supabaseResponse.body.toString()
                val json = Json { isLenient = true }
                val data = json.decodeFromString<List<User>>(jsonString)
                Log.d("supabase", data.toString())
                for (user in data) {
                    val userId = user.id
                    val userNombreApellidos = user.nombre_apellidos
                    Log.d("supabase", "ID: $userId, Nombre y apellidos: $userNombreApellidos")
                }
            } catch (e: Exception) {
                Log.e("supabase", "Error al obtener datos: ${e.message}", e)
            }
        }
    }

     */


    fun getData(){
        lifecycleScope.launch {
            val client = getClient()
            val supabaseResponse = client.postgrest["Usuarios"].select()
            val data = supabaseResponse.decodeList<User>()
            Log.d("supabase",data.toString())

            for (user in data) {
                val userId = user.id  // Acceder al ID del usuario
                val userNombreApellidos = user.nombre_apellidos  // Acceder al nombre y apellidos del usuario

                Log.d("supabase", "ID: $userId, Nombre y apellidos: $userNombreApellidos")
            }
        }
    }


    private fun registrarUsuario() {
        val client = getClient()
        val nombreApellidos = findViewById<TextInputEditText>(R.id.edtNombreRegistro2).text.toString()
        val email = findViewById<TextInputEditText>(R.id.edtCorreoRegistro2).text.toString()
        val password = findViewById<TextInputEditText>(R.id.edtContrasenaRegistro2).text.toString()

        lifecycleScope.launch {
            try {
                // Registrar el usuario en Supabase Auth
                val user = client.gotrue.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }

                // Obtener el ID del usuario de la respuesta
                val userId = user.id

                if (userId != null) {
                    val userIdInt = userId.hashCode()
                    val newUser = User(id = 0, nombre_apellidos = nombreApellidos, correo = email, contrasena = password)
                    client.postgrest["Usuarios"].insert(newUser)
                    Toast.makeText(this@RegistroUsuarios, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    // Si no tenemos un ID de usuario, algo salió mal en el registro
                    Toast.makeText(this@RegistroUsuarios, "Error en el registro: No se pudo obtener el ID del usuario", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RegistroUsuarios", "Error al registrar usuario", e)
                Toast.makeText(this@RegistroUsuarios, "Error al registrar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*
    private fun registerUser(email: String, password: String) {
        val client = getClient()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClientInstance.client.auth.signUp(email, password)
                withContext(Dispatchers.Main) {
                    if (response.error == null) {
                        Toast.makeText(this@RegistroUsuarios, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        // Navegar a la pantalla de inicio de sesión
                    } else {
                        Toast.makeText(this@RegistroUsuarios, response.error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegistroUsuarios, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

     */

    @Serializable
    data class User(
        val id : Int,
        val nombre_apellidos : String = "",
        val correo : String = "",
        val contrasena : String = ""
    )
}

/*
    private fun registrarUsuario() {
        val nuevoUsuario = User(
            nombre_apellidos = "Juan Pérez",
        )

        lifecycleScope.launch {
            try {
                // Insertar el nuevo usuario en la tabla 'Usuarios' de Supabase
                val response = client.postgrest["Usuarios"].insert(nuevoUsuario)
                if (response.error == null) {
                    Log.d("Supabase", "Usuario registrado exitosamente: ${response.data}")
                } else {
                    Log.e("Supabase", "Error al registrar usuario: ${response.error}")
                }
            } catch (e: Exception) {
                Log.e("Supabase", "Error en la solicitud de registro: ${e.message}")
                // Manejar el error según sea necesario
            }
        }
    }

     */