package edu.udb.segundo_desafio_practico_dsm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import edu.udb.segundo_desafio_practico_dsm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configuración de ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()

        // 1. Revisar si el usuario guardó su correo y clave previamente
        checkSession()

        // 2. Evento del boton Iniciar Sesion
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, pass)
            }
        }


        binding.tvForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Escribe tu correo arriba para recuperar la contraseña", Toast.LENGTH_LONG).show()
            } else {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "¡Listo! Revisa tu bandeja de entrada", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        // 4. Evento registrar para ir a la otra pantalla
        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Si el inicio de sesión es exitoso, guardamos o borramos los datos dependiendo de si la casilla recordarme esta marcada
                    saveSessionPref(email, pass, binding.cbRememberMe.isChecked)

                    Toast.makeText(this, "Acceso concedido", Toast.LENGTH_SHORT).show()


                    val intent = Intent(this, PanelActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error: Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun saveSessionPref(email: String, pass: String, isRemembered: Boolean) {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)

        if (isRemembered) {

            sharedPref.edit()
                .putString("savedEmail", email)
                .putString("savedPassword", pass)
                .putBoolean("isRemembered", true)
                .apply()
        } else {

            sharedPref.edit().clear().apply()
        }
    }

    private fun checkSession() {
        val sharedPref = getSharedPreferences("AppSession", Context.MODE_PRIVATE)
        val isRemembered = sharedPref.getBoolean("isRemembered", false)


        if (isRemembered) {
            val savedEmail = sharedPref.getString("savedEmail", "")
            val savedPassword = sharedPref.getString("savedPassword", "")

            binding.etEmail.setText(savedEmail)
            binding.etPassword.setText(savedPassword)
            binding.cbRememberMe.isChecked = true
        }
    }
}