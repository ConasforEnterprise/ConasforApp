package com.example.conasforapp.Main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.conasforapp.R
import com.example.conasforapp.Usuarios.LoginFragment
import com.example.conasforapp.Usuarios.RegistroFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*
        // Hacer que la barra de estado sea transparente
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }


        // Usar iconos y texto oscuros en la barra de estado
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor: View = window.decorView
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }


        // Configurar la barra de navegación (opcional)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor = Color.parseColor("#4FCA16")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
         */

        val frameLogin : FrameLayout = findViewById(R.id.frameMainActivity)

        val loginFragment = LoginFragment()
        val registroFragment = RegistroFragment()

        val btnLogin = findViewById<Button>(R.id.btnLoginMain)
        val btnRegistro = findViewById<Button>(R.id.btnRegistroMain)

       bottomSheetBehavior = BottomSheetBehavior.from(frameLogin)

       bottomSheetBehavior!!.setPeekHeight(0)
       bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
       bottomSheetBehavior!!.isHideable = true
       bottomSheetBehavior!!.setExpandedOffset(0)


        btnLogin.setOnClickListener{
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheet(loginFragment)
        }

        btnRegistro.setOnClickListener{
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheet(registroFragment)
        }
    }

    private fun bottomSheet(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameMainActivity,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

     private fun toogle(){
         if (bottomSheetBehavior!!.getState() == BottomSheetBehavior.STATE_HIDDEN) {
             bottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_EXPANDED)
         } else {
             bottomSheetBehavior!!.setState(BottomSheetBehavior.STATE_HIDDEN)
         }
    }
}