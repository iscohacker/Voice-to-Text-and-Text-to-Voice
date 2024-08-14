package uz.iskandarbek.vtt

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class TextToVoiceActivity : AppCompatActivity() {
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var editText: EditText
    private lateinit var speakButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_to_voice)

        editText = findViewById(R.id.edit_text)
        speakButton = findViewById(R.id.speak_button)

        textToSpeech = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                // O'zbek tilini tanlash
                val result = textToSpeech.setLanguage(Locale("tr"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "O'zbek tili qo'llab-quvvatlanmaydi")
                } else {
                    // Ovozni sozlash (pitch va tezlik)
                    textToSpeech.setPitch(1.0f)
                    textToSpeech.setSpeechRate(1.0f)
                }
            } else {
                Log.e("TTS", "TextToSpeech xizmati ishga tushmadi")
            }
        }

        // Tugma bosilganda matnni ovozga aylantirish
        speakButton.setOnClickListener {
            val text = editText.text.toString()
            if (text.isNotEmpty()) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Log.e("TTS", "Matn kiritilmadi")
            }
        }
    }

    override fun onDestroy() {
        // TextToSpeech resurslarini tozalash
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}