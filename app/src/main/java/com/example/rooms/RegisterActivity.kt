package com.example.rooms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.rooms.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kayitolButton.setOnClickListener() {
            if (binding.etMail.text.isNotEmpty() &&binding.etSifre.text.isNotEmpty() && binding.etSifre2.text.isNotEmpty()) {

                    if(binding.etSifre.text.toString().equals(binding.etSifre2.text.toString())) {
                        yeniuyekayit(binding.etMail.text.toString(),binding.etSifre.text.toString())
                    }else{
                        Toast.makeText(this,"Şifreler Aynı Değil",Toast.LENGTH_SHORT).show()
                    }
            }else {
                Toast.makeText(this, "Boş Alanları Doldurunuz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun yeniuyekayit(mail: String, sifre: String) {
        progressBarGoster()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,sifre)
            .addOnCompleteListener(object: OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {
                    if(p0.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Kayıt Başarılı ID: "+ FirebaseAuth.getInstance().currentUser?.uid, Toast.LENGTH_SHORT).show()
                        onayMailiGonder()
                        FirebaseAuth.getInstance().signOut()

                    }else{
                        Toast.makeText(this@RegisterActivity, "Hatalı Kayıt. "+p0.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        progressBarGizle()

    }

    private fun onayMailiGonder (){
        var kullanici=FirebaseAuth.getInstance().currentUser
        if (kullanici !=null){
            kullanici.sendEmailVerification()
                .addOnCompleteListener(object :OnCompleteListener<Void>{
                    override fun onComplete(p0: Task<Void>) {

                        if (p0.isSuccessful){
                            Toast.makeText(this@RegisterActivity, "Mail Kutunuzu Kontrol Edin ve Onaylayin  ", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@RegisterActivity, "Onay Kodu Gönderilemedi   "+p0.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                })
        }
    }
    private fun progressBarGoster(){
        binding.progressBar!!.visibility= View.VISIBLE
    }

    private fun progressBarGizle(){
        binding.progressBar!!.visibility= View.INVISIBLE
    }
}