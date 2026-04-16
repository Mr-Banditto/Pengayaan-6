package com.raditya.miniplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.fragment.app.Fragment

class MusicFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seekBar: SeekBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // 1. "Nempelin" layout XML ke Fragment
        val view = inflater.inflate(R.layout.fragment_music, container, false)

        // 2. Hubungkan Tombol (Harus pakai view.findViewById)
        val btnPlay = view.findViewById<Button>(R.id.btnPlay)
        val btnPause = view.findViewById<Button>(R.id.btnPause)
        val btnStop = view.findViewById<Button>(R.id.btnStop)
        seekBar = view.findViewById(R.id.musicSeekBar)

        // 3. Siapkan MediaPlayer (Gunakan requireContext() sebagai ganti 'this')
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.apple)

        btnPlay.setOnClickListener { mediaPlayer?.start() }
        btnPause.setOnClickListener { mediaPlayer?.pause() }
        btnStop.setOnClickListener {
            mediaPlayer?.pause()
            mediaPlayer?.seekTo(0)
        }

        return view
    }

    // 4. Beres-beres saat pindah halaman agar musik tidak tabrakan
    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}