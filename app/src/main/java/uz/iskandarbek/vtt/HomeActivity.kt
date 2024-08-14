package uz.iskandarbek.vtt

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.iskandarbek.vtt.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private val binding by lazy{ActivityHomeBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.apply {
            voiceToText.setOnClickListener {
                startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            }
            textToVoice.setOnClickListener {
                startActivity(Intent(this@HomeActivity, TextToVoiceActivity::class.java))
            }

        }
    }
}