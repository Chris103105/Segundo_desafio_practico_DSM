package edu.udb.segundo_desafio_practico_dsm

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import edu.udb.segundo_desafio_practico_dsm.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()

            if (validateInputs(email, password)) {
                registerUserInFirebase(email, password)
            }
        }
    }

    private fun validateInputs(email: String, pass: String): Boolean {
        var isValid = true

        // Validar Correo
        if (email.isEmpty()) {
            binding.tilRegisterEmail.error = "Ingresa un correo electrónico"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilRegisterEmail.error = "El formato del correo no es válido"
            isValid = false
        } else {
            binding.tilRegisterEmail.error = null
        }

        // Validar Contraseña
        if (pass.isEmpty()) {
            binding.tilRegisterPassword.error = "Ingresa una contraseña"
            isValid = false
        } else if (pass.length < 6) {
            binding.tilRegisterPassword.error = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        } else {
            binding.tilRegisterPassword.error = null
        }

        return isValid
    }

    private fun registerUserInFirebase(email: String, pass: String) {

        binding.btnRegister.isEnabled = false

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show()

                    finish()
                } else {
                    binding.btnRegister.isEnabled = true
                    val errorMsg = task.exception?.message ?: "Error al registrar"
                    Toast.makeText(this, "Error: $errorMsg", Toast.LENGTH_LONG).show()
                }
            }
    }
}