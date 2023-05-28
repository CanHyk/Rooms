package com.example.rooms

import android.content.Intent
import android.media.MediaPlayer.OnCompletionListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.rooms.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMyAuthStateListener()


        binding.tvkayitol?.setOnClickListener() {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvoMailTekrarGonder.setOnClickListener {
            var dialogGoster=onayMailTekrarGonderFragment()
            dialogGoster.show(supportFragmentManager,"Diyaloğu Göster")
        }

        binding.tvSFreTekrarYolla.setOnClickListener(){
            var dialogSifreyiTekrarGonder=SifremiUnuttumDialogFragment()
            dialogSifreyiTekrarGonder.show(supportFragmentManager,"gosterdialogsifre")
        }

        binding.btngirisyap.setOnClickListener() {

            if (binding.etmail.text.isNotEmpty() && binding.etsifre.text.isNotEmpty()) {
                progressBarGoster()

                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.etmail.text.toString(),
                    binding.etsifre.text.toString()
                )
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                        override fun onComplete(p0: Task<AuthResult>) {
                            if (p0.isSuccessful) {
                                progressBarGizle()


                                    if (!p0.result.user!!.isEmailVerified){
                                        //FirebaseAuth.getInstance().signOut()
                                        Toast.makeText(this@LoginActivity, "Giriş Başarılı : " + FirebaseAuth.getInstance().currentUser?.email, Toast.LENGTH_SHORT).show()
                                        var intent=Intent(this@LoginActivity,MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                            } else {
                                progressBarGizle()
                                Toast.makeText(this@LoginActivity, "Giriş Hatası : " + p0.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            } else {
                Toast.makeText(this@LoginActivity, "Boş Alanları Doldurunuz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun progressBarGoster() {
        binding.progressBarLogin!!.visibility = View.VISIBLE
    }

    private fun progressBarGizle() {
        binding.progressBarLogin!!.visibility = View.INVISIBLE
    }

    private fun initMyAuthStateListener() {
        mAuthStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var kullanici = p0.currentUser

                if (kullanici != null) {
                    if (kullanici.isEmailVerified) {
                        Toast.makeText(this@LoginActivity, "Mail Onayı Başarılı. Giriş Yapılabilir", Toast.LENGTH_SHORT).show()
                        var intent=Intent(this@LoginActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Lütfen Mail Adresinizi Onaylayın", Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener)
    }
}