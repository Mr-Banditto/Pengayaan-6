package com.raditya.miniplayer

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment

class EffectsFragment : Fragment() {
    private lateinit var soundPool: SoundPool
    private var sShoot = 0; private var sBoom = 0; private var sCoin = 0
    private var isLoaded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_effects, container, false)

        // Setup SoundPool (Pakai requireContext())
        val attr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        soundPool = SoundPool.Builder().setMaxStreams(6).setAudioAttributes(attr).build()

        // Load Audio
        sShoot = soundPool.load(requireContext(), R.raw.shoot, 1)
        sBoom = soundPool.load(requireContext(), R.raw.boom, 1)
        sCoin = soundPool.load(requireContext(), R.raw.coin, 1)
        soundPool.setOnLoadCompleteListener { _, _, _ -> isLoaded = true }

        // Klik Tombol
        v.findViewById<Button>(R.id.btnShoot).setOnClickListener { if(isLoaded) soundPool.play(sShoot, 1f, 1f, 1, 0, 1f) }
        v.findViewById<Button>(R.id.btnBoom).setOnClickListener { if(isLoaded) soundPool.play(sBoom, 1f, 1f, 1, 0, 1f) }
        v.findViewById<Button>(R.id.btnCoin).setOnClickListener { if(isLoaded) soundPool.play(sCoin, 1f, 1f, 1, 0, 1f) }
        v.findViewById<Button>(R.id.btnSimultan).setOnClickListener {
            if(isLoaded) {
                soundPool.play(sShoot, 1f, 1f, 1, 0, 1f)
                soundPool.play(sBoom, 1f, 1f, 1, 0, 1f)
                soundPool.play(sCoin, 1f, 1f, 1, 0, 1f)
            }
        }
        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        soundPool.release() // Bebaskan memori hardware
    }
}