package com.example.vamosrachar

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.text.DecimalFormat
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private lateinit var textToSpeech: TextToSpeech

    private lateinit var btnVoice: ImageButton
    private lateinit var btnSend: ImageButton
    private lateinit var edPrice: EditText
    private lateinit var edPeople: EditText
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textToSpeech = TextToSpeech(this, this)
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
                if(result.text != "Informe valores válidos!" && result.text != ""){
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "Vamos rachar: sua parte na conta é ${result.text}!")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            }
        })

        btnVoice.setOnClickListener(View.OnClickListener {
            val price = edPrice.text.toString()
            val people = edPeople.text.toString()

            if (price.isNotEmpty() && people.isNotEmpty()) {
                if(result.text != "Informe valores válidos!" && result.text != "") {
                    speakResult("A conta deu ${result.text} pra cada um!")
                }
            }
        })
    }

    inner class TextChangeListener : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        @SuppressLint("SetTextI18n")
        override fun afterTextChanged(s: Editable?) {
            val price = edPrice.text.toString()
            val people = edPeople.text.toString()
            Log.d("TEXT", "$price and $people")

            if (price.isNotEmpty() && people.isNotEmpty()) {
                val df = DecimalFormat("#.00")
                val priceInput = edPrice.text.toString()
                val peopleInput = edPeople.text.toString()

                val price = if (priceInput.isNotEmpty()) priceInput.toDouble() else 0.0
                val people = if (peopleInput.isNotEmpty()) peopleInput.toDouble() else 0.0

                if (price == 0.0 || people == 0.0) {
                    val resultValue = "Informe valores válidos!"
                    result.text = resultValue
                } else {
                    val resultValue = df.format(price/people)
                    result.text = "R$$resultValue"
                }
            }

        }
    }

    /**
     * Speak the result
     */
    private fun speakResult(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val locale = Locale.getDefault()
            val result = textToSpeech.setLanguage(locale)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}
