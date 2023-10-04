package com.example.vamosrachar

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.text.DecimalFormat

class MainActivity : ComponentActivity() {
    private lateinit var btnVoice: ImageButton
    private lateinit var btnSend: ImageButton
    private lateinit var edPrice: EditText
    private lateinit var edPeople: EditText
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnVoice = findViewById(R.id.btnVoice)
        btnSend = findViewById(R.id.btnSend)
        edPrice = findViewById(R.id.edPrice)
        edPeople = findViewById(R.id.edPeople)
        result = findViewById(R.id.result)

        /**
         * Adjust components by theme
         */
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
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

        /**
         * TextWatchers
         */
        edPrice.addTextChangedListener(TextChangeListener())
        edPeople.addTextChangedListener(TextChangeListener())

        /**
         * Start Intents by button events
         */
        btnSend.setOnClickListener(View.OnClickListener {
            val price = edPrice.text.toString()
            val people = edPeople.text.toString()

            if (price.isNotEmpty() && people.isNotEmpty()) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Vamos rachar: sua parte na conta é ${result.text}!")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        })
    }

    inner class TextChangeListener : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val price = edPrice.text.toString()
            val people = edPeople.text.toString()

            if (price.isNotEmpty() && people.isNotEmpty()) {
                val df = DecimalFormat("#.00")
                val resultValue = df.format(calculate())
                result.text = "R$$resultValue"
            }
        }
    }

    private fun calculate(): Double {
        val price = Integer.parseInt(edPrice.text.toString())
        val people = Integer.parseInt(edPeople.text.toString())
        return (price * 1.0)/(people * 1.0)
    }
}
