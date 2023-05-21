package com.example.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import java.security.AccessControlContext


class onayMailTekrarGonderFragment : DialogFragment() {

    lateinit var emailEdittext:EditText
    lateinit var sifreEditText: EditText
    lateinit var mContext: FragmentActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.fragment_dialog, container, false)
        emailEdittext=view.findViewById(R.id.etDialogMail)
        sifreEditText=view.findViewById(R.id.etDialogSifre)
        mContext=requireActivity()
        var btnIptal=view.findViewById<Button>(R.id.btnDialogIptal)
        var btnGonder=view.findViewById<Button>(R.id.btnDialogGonder)


        btnIptal.setOnClickListener(){
            dialog?.dismiss()
        }
        btnGonder.setOnClickListener(){
            if (emailEdittext.text.toString().isNotEmpty()&&sifreEditText.text.toString().isNotEmpty()){
                girisYapveOnayMailiniTekrarGonder(emailEdittext.text.toString(),sifreEditText.text.toString())
            }else{
                Toast.makeText(mContext,"Boş Alanları Doldurunuz",Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun girisYapveOnayMailiniTekrarGonder(email: String, sifre: String) {
        var credential=EmailAuthProvider.getCredential(email,sifre)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    onayMailiniTekrarGonder()
                    dialog?.dismiss()
                }else{
                    Toast.makeText(mContext,"Email veya Şifre Hatalı",Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun onayMailiniTekrarGonder() {
        var kullanici=FirebaseAuth.getInstance().currentUser
        if (kullanici !=null){
            kullanici.sendEmailVerification()
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {

                        if (p0.isSuccessful){
                            Toast.makeText(mContext, "Mail Kutunuzu Kontrol Edin ve Maili Onaylayin  ", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(mContext, "Onay Kodu Gönderilemedi   "+p0.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }
}
