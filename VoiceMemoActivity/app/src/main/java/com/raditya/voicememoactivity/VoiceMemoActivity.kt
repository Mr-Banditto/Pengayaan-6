package com.raditya.voicememoactivity

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.raditya.voicememoactivity.R
import java.io.File

class VoiceMemoActivity : AppCompatActivity() {

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var isRecording = false
    private var outputFile: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_memo)

        val btnRekam = findViewById<Button>(R.id.btnRekam)
        val btnPutar = findViewById<Button>(R.id.btnPutar)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        // Tentukan tempat menyimpan file rekamannya
        outputFile = "${externalCacheDir?.absolutePath}/rekaman_saya.mp4"

        // Cek & Minta Izin Mikrofon saat aplikasi dibuka
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 101)
        }

        // Logika Tombol Rekam
        btnRekam.setOnClickListener {
            if (!isRecording) {
                mulaiRekam()
                btnRekam.text = "Berhenti Rekam"
                tvStatus.text = "Sedang Merekam..."
            } else {
                hentiRekam()
                btnRekam.text = "Mulai Rekam"
                tvStatus.text = "Rekaman Tersimpan"
            }
            isRecording = !isRecording
        }

        // Logika Tombol Putar
        btnPutar.setOnClickListener {
            putarHasil()
        }
    }

    private fun mulaiRekam() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(outputFile)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            prepare()
            start()
        }
    }

    private fun hentiRekam() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    private fun putarHasil() {
        if (File(outputFile).exists()) {
            player = MediaPlayer().apply {
                setDataSource(outputFile)
                prepare()
                start()
            }
        } else {
            Toast.makeText(this, "Belum ada rekaman!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recorder?.release()
        player?.release()
    }
}