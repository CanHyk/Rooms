package com.example.rooms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.rooms.databinding.ActivityKullaniciAyarlariBinding
import com.example.rooms.databinding.ActivityMainBinding
import com.google.firebase.auth.EmailAuthProvider
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
            if (binding.etDetayName.text.toString().isNotEmpty()){

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

        binding.btnSifreveyaMailGuncelle.setOnClickListener(){
            if (binding.etDetaySifre.text.toString().isNotEmpty()) {

                var credential=EmailAuthProvider.getCredential(kullanici.email.toString(),binding.etDetaySifre.text.toString())
                kullanici.reauthenticate(credential).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        binding.guncellelayout.visibility=View.VISIBLE
                        binding.btnMailGuncelle.setOnClickListener(){
                            mailAdresiniGuncelle()
                        }
                        binding.btnSifreGuncelle.setOnClickListener(){
                            sifreAdresiniGuncelle()
                        }

                    }else{
                        Toast.makeText(this@KullaniciAyarlariActivity,"Geçerli Şifre Yanlış", Toast.LENGTH_SHORT).show()
                        binding.guncellelayout.visibility=View.INVISIBLE
                    }
                }
            }else{
                Toast.makeText(this@KullaniciAyarlariActivity,"Güncelleme için Geçerli Şifrenizi Yazınız", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sifreAdresiniGuncelle() {
        var kullanici=FirebaseAuth.getInstance().currentUser!!

        if (kullanici!=null){
            kullanici.updatePassword(binding.etYeniSifre.text.toString())
                .addOnCompleteListener { task->
                    Toast.makeText(this@KullaniciAyarlariActivity,"Şifre Başarıyla Güncellendi", Toast.LENGTH_SHORT).show()

                }
        }
    }

    private fun mailAdresiniGuncelle() {
        var kullanici=FirebaseAuth.getInstance().currentUser

        if (kullanici!=null){

            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(binding.etYeniMail.text.toString())
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        if(task.getResult().signInMethods?.size==1){
                            Toast.makeText(this@KullaniciAyarlariActivity,"Kullanilan Bir Mail Adresi Girdiniz", Toast.LENGTH_SHORT).show()
                        }else{
                            kullanici.updateEmail(binding.etYeniMail.text.toString())
                            .addOnCompleteListener { task->
                                FirebaseAuth.getInstance().signOut()
                                loginSayfasinaYonlendir()
                                Toast.makeText(this@KullaniciAyarlariActivity,"Mail Adresi Başarıyla Güncellendi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(this@KullaniciAyarlariActivity,"Mail Adresi Güncelleme Başarısız", Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }

    fun loginSayfasinaYonlendir(){
        var intent=Intent(this@KullaniciAyarlariActivity,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}