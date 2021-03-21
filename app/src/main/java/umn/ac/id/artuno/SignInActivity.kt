package umn.ac.id.artuno

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSignIn.setOnClickListener {
            if (etUsername.text?.toString().equals("uasmobile", true) &&
                etPassword.text?.toString().equals("uasmobilegenap", false)) {
                startActivity(Intent(this, MusicList::class.java)
                    .putExtra("status", "success"))
            } else {
                etUsername.error = "Invalid Username"
                etPassword.error = "Invalid Password"
            }
        }

        etUsername.addTextChangedListener {
            if (it.toString().equals("uasmobile", false)) etUsername.error = null
            else etUsername.error = "Invalid Username"
        }

        etPassword.addTextChangedListener {
            if (it.toString().equals("uasmobilegenap", true)) etPassword.error = null
            else etPassword.error = "Invalid Password"
        }
    }


}