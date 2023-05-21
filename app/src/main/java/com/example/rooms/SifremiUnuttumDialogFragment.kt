package com.example.rooms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth

class SifremiUnuttumDialogFragment : DialogFragment() {

    lateinit var emailEdittext: EditText
    lateinit var mContext: FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        var view=inflater.inflate(R.layout.fragment_sifremi_unuttum_dialog, container, false)
        mContext= requireActivity()
        emailEdittext=view.findViewById(R.id.etSıfreyıTekrarGonder)
        var btnIptal=view.findViewById<Button>(R.id.btnSıfreyıUnuttumIptal)
        btnIptal.setOnClickListener(){
            dialog?.dismiss()
        }
        var btnGonder=view.findViewById<Button>(R.id.btnSıfreyıUnuttumGonder)
        btnGonder.setOnClickListener(){
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailEdittext.text.toString())
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Toast.makeText(mContext,"Şifre Sıfırlama Maili Göonderildi",Toast.LENGTH_SHORT).show()
                        dialog?.dismiss()
                    }else{
                        Toast.makeText(mContext,"Hata Oluştu :"+task.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }
        }

        return view
    }


}