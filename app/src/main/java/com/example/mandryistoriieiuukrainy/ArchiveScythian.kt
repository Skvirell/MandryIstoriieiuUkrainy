package com.example.mandryistoriieiuukrainy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.mandryistoriieiuukrainy.databinding.ScythianArchiveBinding

class ArchiveScythian : AppCompatActivity() {
    private lateinit var binding: ScythianArchiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ScythianArchiveBinding.inflate(layoutInflater)
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

        // Прикріплення обробника подій до кнопки
        binding.buttonReturnToArchive.setOnClickListener {
            val intent = Intent(this, Archive_list::class.java)
            startActivity(intent)
        }
    }
}
