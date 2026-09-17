package edu.udb.segundo_desafio_practico_dsm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import edu.udb.segundo_desafio_practico_dsm.databinding.ActivityPanelBinding

class PanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPanelBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.tvLoggedUser.text = "Logged in as: ${currentUser.email}"
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnLogout.setOnClickListener {
            // 1. Cerrar sesión internamente en Firebase
            auth.signOut()

            // 2. Regresar a la pantalla de Login

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


            finish()
        }
    }
}