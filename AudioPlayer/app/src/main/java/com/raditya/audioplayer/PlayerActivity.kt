package com.raditya.audioplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {

    // 1. Variabel utama
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var seekBar: SeekBar
    private lateinit var tvCurrent: TextView
    private lateinit var tvDurasi: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // 2. Inisialisasi View
        seekBar = findViewById(R.id.seekBar)
        tvCurrent = findViewById(R.id.tvCurrent)
        tvDurasi = findViewById(R.id.tvDurasi)

        // 3. Menyiapkan MediaPlayer
        setupMediaPlayer()

        // 4. Tombol PLAY
        findViewById<Button>(R.id.btnPlay).setOnClickListener {
            mediaPlayer?.let { mp ->
                if (!mp.isPlaying) {
                    mp.start()
                    updateSeekBar()
                }
            }
        }

        // 5. Tombol PAUSE
        findViewById<Button>(R.id.btnPause).setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }

        // 6. Tombol STOP
        findViewById<Button>(R.id.btnStop).setOnClickListener {
            resetPlayer()
        }

        // 7. Geser SeekBar manual
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                    tvCurrent.text = formatWaktu(progress)
                }
            }
            override fun onStartTrackingTouch(sb: SeekBar) {}
            override fun onStopTrackingTouch(sb: SeekBar) {}
        })
    }

    // Fungsi untuk memuat file audio
    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.apple)
        mediaPlayer?.let { mp ->
            // Mengatur durasi di layar saat file sudah siap
            seekBar.max = mp.duration
            tvDurasi.text = formatWaktu(mp.duration)

            // Jika musik selesai dengan sendirinya, balik ke posisi awal
            mp.setOnCompletionListener {
                resetPlayer()
            }
        }
    }

    // Fungsi menggerakkan slider secara otomatis
    private fun updateSeekBar() {
        handler.postDelayed({
            mediaPlayer?.let { mp ->
                if (mp.isPlaying) {
                    seekBar.progress = mp.currentPosition
                    tvCurrent.text = formatWaktu(mp.currentPosition)
                    updateSeekBar() // Mengulang setiap 0.5 detik
                }
            }
        }, 500)
    }

    // Fungsi RESET (Stop musik dan balik ke detik 0)
    private fun resetPlayer() {
        mediaPlayer?.let { mp ->
            if (mp.isPlaying) {
                mp.pause()
            }
            mp.seekTo(0) // Balikkan kaset ke awal
        }
        handler.removeCallbacksAndMessages(null) // Berhentikan pergerakan slider
        seekBar.progress = 0
        tvCurrent.text = "0:00"
    }

    // Fungsi pengubah angka ke format 0:00
    private fun formatWaktu(ms: Int): String {
        val detik = (ms / 1000) % 60
        val menit = ms / 60000
        return "%d:%02d".format(menit, detik)
    }

    // 8. Membersihkan memori saat aplikasi ditutup
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer?.release()
        mediaPlayer = null
    }
}