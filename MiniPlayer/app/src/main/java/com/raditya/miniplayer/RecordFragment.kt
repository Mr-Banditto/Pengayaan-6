package com.raditya.miniplayer

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import java.io.File

class RecordFragment : Fragment() {
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var isRecording = false
    private lateinit var path: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_record, container, false)
        val btnStart = v.findViewById<Button>(R.id.btnStartRecord)
        val btnPlay = v.findViewById<Button>(R.id.btnPlayRecord)
        val tvStatus = v.findViewById<TextView>(R.id.tvRecordStatus)

        path = "${requireContext().externalCacheDir?.absolutePath}/mini_record.mp4"

        btnStart.setOnClickListener {
            if (!isRecording) {
                recorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                    setOutputFile(path)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                    prepare(); start()
                }
                btnStart.text = "STOP RECORD"
                tvStatus.text = "Recording..."
            } else {
                recorder?.stop(); recorder?.release(); recorder = null
                btnStart.text = "START RECORD"
                tvStatus.text = "Recording Saved!"
            }
            isRecording = !isRecording
        }

        btnPlay.setOnClickListener {
            if (File(path).exists()) {
                player = MediaPlayer().apply { setDataSource(path); prepare(); start() }
            } else Toast.makeText(requireContext(), "No record found", Toast.LENGTH_SHORT).show()
        }
        return v
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recorder?.release(); player?.release()
    }
}