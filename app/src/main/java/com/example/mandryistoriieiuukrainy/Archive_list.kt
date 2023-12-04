package com.example.mandryistoriieiuukrainy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.mandryistoriieiuukrainy.databinding.ActivitySelectHistoryBinding
import com.example.mandryistoriieiuukrainy.databinding.ListArchiveBinding

class Archive_list : AppCompatActivity() {
    private lateinit var binding: ListArchiveBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ListArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        // Перевірка версії Android для визначення методу встановлення повноекранного режиму
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Для Android 11 (API рівень 30) та вище
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Для старіших версій Android
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        // Обробник для кнопки "Скіфи"
        binding.buttonScythians.setOnClickListener {
            val intent = Intent(this, ArchiveScythian::class.java)
            startActivity(intent)
        }

        binding.buttonReturnToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Обробник для кнопки "Історія 1"
        //binding.buttonHistory1.setOnClickListener {
        // код для переходу до історії 1
    }

    // обробники для інших кнопок тут
}