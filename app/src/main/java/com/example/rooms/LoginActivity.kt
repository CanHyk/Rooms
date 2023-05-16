package com.example.rooms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rooms.databinding.ActivityLoginBinding
import com.example.rooms.databinding.ActivityRegisterBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}