package uz.iskandarbek.vtt

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var button: Button
    private lateinit var textView: TextView
    private lateinit var copyButton: ImageView
    private lateinit var eraser: ImageView

    private val REQUEST_RECORD_AUDIO_PERMISSION = 200

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.speak)
        textView = findViewById(R.id.text)
        copyButton = findViewById(R.id.copy)
        eraser = findViewById(R.id.eraser)
        eraser.setOnClickListener {
            textView.text = "Javob so'z bu yerda ko'rinadi"
        }

        // Runtime ruxsatini tekshirish va so'rash
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_RECORD_AUDIO_PERMISSION
            )
        }

        // SpeechRecognizer ob'ektini yaratish va sozlash
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                // Qo'shimcha sozlashlar
            }

            override fun onBeginningOfSpeech() {
                // Foydalanuvchi gapirishni boshladi
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Ovozni o'lchash
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Qo'shimcha sozlashlar
            }

            override fun onEndOfSpeech() {
                // Foydalanuvchi gapirishni tugatdi
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_NETWORK -> "Internet ulanishini yo'q"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Internet kutilmoqda"
                    SpeechRecognizer.ERROR_AUDIO -> "Gapirishda muammo"
                    SpeechRecognizer.ERROR_SERVER -> "Serverda muammo"
                    SpeechRecognizer.ERROR_CLIENT -> "Foydalanuvchida muammo"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Gapirish kutilmoqda"
                    SpeechRecognizer.ERROR_NO_MATCH -> "Qaytadan urining!"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Yozishni tuggalng"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                    else -> "Xatolik"
                }
                Toast.makeText(this@MainActivity, "Xatolik: $errorMessage", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResults(results: Bundle?) {
                val resultList = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val resultText = resultList?.joinToString("\n") ?: "Hech narsa tanilmadi"
                // Matnni TextView'ga qo'shish
                textView.text = resultText
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // Qisman natijalar
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Qo'shimcha voqealar
            }
        })

        button.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Gapiring...")
            try {
                speechRecognizer.startListening(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Ovoz tanish xatolik: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        copyButton.setOnClickListener {
            if (textView.text == "Javob so'z bu yerda ko'rinadi") {
                Toast.makeText(this, "Nusxalashga hech narsa yo'q", Toast.LENGTH_SHORT).show()
            } else {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Ovozdan olingan matn", textView.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "Matn nusxalandi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Ruxsat berilmagan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
