package com.example.vamosrachar

import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : ComponentActivity() {
    private lateinit var btnSend: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Adjust by theme
         * */
        val isDarkTheme =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        val btnVoice = findViewById<ImageButton>(R.id.btnVoice)
        val btnSend = findViewById<ImageButton>(R.id.btnSend)
        val inputPrice = findViewById<ConstraintLayout>(R.id.inputPrice)
        val inputPeople = findViewById<ConstraintLayout>(R.id.inputPeople)
        if (isDarkTheme) {
            btnVoice.setImageResource(R.drawable.volumedark)
            btnSend.setImageResource(R.drawable.senddark)
            inputPrice.setBackgroundResource(R.drawable.rounded_border_dark)
            inputPeople.setBackgroundResource(R.drawable.rounded_border_dark)
        } else {
            btnVoice.setImageResource(R.drawable.volume)
            btnSend.setImageResource(R.drawable.send)
            inputPrice.setBackgroundResource(R.drawable.rounded_border)
            inputPeople.setBackgroundResource(R.drawable.rounded_border)
        }
    }
}