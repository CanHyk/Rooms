package com.example.rooms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.rooms.databinding.ActivityKullaniciAyarlariBinding
import com.example.rooms.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class KullaniciAyarlariActivity : AppCompatActivity() {
    lateinit var  binding: ActivityKullaniciAyarlariBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityKullaniciAyarlariBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var kullanici=FirebaseAuth.getInstance().currentUser!!

        binding.etDetayName.setText(kullanici.displayName.toString())
        binding.etDetayMail.setText(kullanici.email.toString())

        binding.btnSifreGonder.setOnClickListener(){
            FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().currentUser?.email.toString())
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Toast.makeText(this@KullaniciAyarlariActivity,"Şifre Sıfırlama Maili Göonderildi", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@KullaniciAyarlariActivity,"Hata Oluştu :"+task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.btnDegisiklikleriKaydet.setOnClickListener(){
            if (binding.etDetayName.text.toString().isNotEmpty() && binding.etDetayMail.text.toString().isNotEmpty()){

                if (!binding.etDetayName.text.toString().equals((kullanici.displayName.toString()))){
                    var bildileriGuncelle=UserProfileChangeRequest.Builder()
                        .setDisplayName(binding.etDetayName.text.toString()).build()
                    kullanici.updateProfile(bildileriGuncelle)
                        .addOnCompleteListener { task->
                            if (task.isSuccessful){
                                Toast.makeText(this@KullaniciAyarlariActivity,"Değişiklikler Yapıldı", Toast.LENGTH_SHORT).show()

                            }
                        }
                }

            }else{
                Toast.makeText(this@KullaniciAyarlariActivity,"Boş Alanları Doldurunuz", Toast.LENGTH_SHORT).show()

            }

        }

    }
}