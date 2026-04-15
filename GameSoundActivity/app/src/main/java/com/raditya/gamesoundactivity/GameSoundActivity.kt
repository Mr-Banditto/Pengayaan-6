package com.example.gamesound

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.raditya.gamesoundactivity.R

class GameSoundActivity : AppCompatActivity() {
    private lateinit var soundPool: SoundPool
    private var soundShoot = 0 // ID suara tembakan
    private var soundBoom = 0 // ID suara ledakan
    private var soundCoin = 0 // ID suara koin
    private var isLoaded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_sound)
        // Konfigurasi AudioAttributes sesuai use case GAME
        val audioAttr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        // Bangun SoundPool: max 6 stream simultan
        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .setAudioAttributes(audioAttr)
            .build()
        // Load semua suara ke memori
        soundShoot = soundPool.load(this, R.raw.shoot, 1)
        soundBoom = soundPool.load(this, R.raw.boom, 1)
        soundCoin = soundPool.load(this, R.raw.coin, 1)
        // Set listener — jangan putar sebelum semua ter-load
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) isLoaded = true
        }
        // Tombol Tembak
        findViewById<Button>(R.id.btnShoot).setOnClickListener {
            if (isLoaded) playSound(soundShoot)
        }
        // Tombol Ledakan
        findViewById<Button>(R.id.btnBoom).setOnClickListener {
            if (isLoaded) playSound(soundBoom)
        }
        // Tombol Koin
        findViewById<Button>(R.id.btnCoin).setOnClickListener {
            if (isLoaded) playSound(soundCoin)
        }
        // Demo simultan: tekan semua sekaligus
        findViewById<Button>(R.id.btnSimultan).setOnClickListener {
            if (isLoaded) {
                playSound(soundShoot)
                playSound(soundBoom)
                playSound(soundCoin)
            }
        }
    }
    /**
     * Memutar suara dari SoundPool
     * Parameter play(): soundID, volLeft, volRight, priority, loop, rate
     * loop = 0 (tidak loop), loop = -1 (loop selamanya)
     * rate = 1.0f (kecepatan normal), range 0.5f - 2.0f
     */
    private fun playSound(soundId: Int) {
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }
    override fun onDestroy() {
        super.onDestroy()
        soundPool.release() // Wajib: bebaskan semua audio di memori
    }
}