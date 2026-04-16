package com.raditya.miniplayer

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val volBar = findViewById<SeekBar>(R.id.volumeSeekBar)
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // 1. Setting Volume Global (Requirement Latihan Gabungan)
        val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        volBar.max = maxVol
        volBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        volBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        // 2. Navigasi Pindah Halaman
        pindahHalaman(MusicFragment()) // Default saat buka aplikasi

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_music -> pindahHalaman(MusicFragment())
                R.id.nav_effects -> pindahHalaman(EffectsFragment())
                R.id.nav_record -> pindahHalaman(RecordFragment())
            }
            true
        }
    }

    private fun pindahHalaman(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, fragment)
            .commit()
    }
}