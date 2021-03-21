package umn.ac.id.artuno

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.activity_music_detail.*
import umn.ac.id.artuno.MusicList.Companion.musicList
import java.io.File

class MusicDetail : AppCompatActivity() {
    companion object {
        lateinit var musicData: MutableList<Music>
        var mediaPlayer: MediaPlayer = MediaPlayer()
        lateinit var uri: Uri
    }

    var position = -1
    private val handler: Handler = Handler()
    private var prevT: Thread? = null
    private var playT: Thread? = null
    private var nextT: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        position = intent.getIntExtra("position", -1)
        musicData = musicList

        btnToggleMusic.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_pause_24)
        uri = Uri.fromFile(File(musicData[position].path))

        mediaPlayer.stop()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer.create(this, uri)
        mediaPlayer.start()

        musicSlider.valueTo = (mediaPlayer.duration / 1000).toFloat()
        displayMusicLength()

        tvMusicTitle.text = musicData[position].title

        musicSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                mediaPlayer.seekTo((value * 1000).toInt())
            }
        })

        this.runOnUiThread(object: Runnable {
            override fun run() {
                val currentPosition = mediaPlayer.currentPosition.div(1000)
                musicSlider.value = currentPosition.toFloat()
                tvMusicLength.text = formatDuration(currentPosition)
                handler.postDelayed(this, 1000)
            }
        })

        tvMusicTitle.isSelected = true
    }

    override fun onResume() {
        prevT = object : Thread() {
            override fun run() {
                super.run()
                btnPreviousMusic.setOnClickListener { previousButtonClicked() }
            }
        }
        (prevT as Thread).start()
        playT = object : Thread() {
            override fun run() {
                super.run()
                btnToggleMusic.setOnClickListener { toggleButtonClicked() }
            }
        }
        (playT as Thread).start()
        nextT = object : Thread() {
            override fun run() {
                super.run()
                btnNextMusic.setOnClickListener { nextButtonClicked() }
            }
        }
        (nextT as Thread).start()
        super.onResume()
    }

    private fun displayMusicLength() {
        val musicLength: Int = (musicData[position].duration / 1000)
        tvMusicLength.text = formatDuration(musicLength)
    }

    private fun formatDuration(currentPosition: Int): String {
        val seconds = (currentPosition % 60).toString()
        val minutes = (currentPosition / 60).toString()
        val totalOut = "0$minutes.$seconds"
        val totalNew = "0$minutes.0$seconds"
        return if (seconds.length == 1) totalNew
        else totalOut
    }

    private fun previousButtonClicked() {
        mediaPlayer.stop()
        mediaPlayer.release()
        position = if (position - 1 < 0) musicData.size - 1 else position - 1
        uri = Uri.parse(musicData[position].path)
        mediaPlayer = MediaPlayer.create(applicationContext, uri)
        displayMusicLength()
        tvMusicTitle.text = musicData[position].title
        musicSlider.valueTo = (mediaPlayer.duration / 1000).toFloat()
        this.runOnUiThread(object : Runnable {
            override fun run() {
                musicSlider.value = (mediaPlayer.currentPosition / 1000).toFloat()
                handler.postDelayed(this, 1000)
            }
        })
        if (mediaPlayer.isPlaying) {
            btnToggleMusic.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_pause_24)
            mediaPlayer.start()
        } else btnToggleMusic.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_play_arrow_24)
    }

    private fun toggleButtonClicked() {
        if (mediaPlayer.isPlaying) {
            btnToggleMusic.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_play_arrow_24)
            mediaPlayer.pause()
            musicSlider.valueTo = (mediaPlayer.duration / 1000).toFloat()
            this.runOnUiThread(object : Runnable {
                override fun run() {
                    musicSlider.value = (mediaPlayer.currentPosition / 1000).toFloat()
                    handler.postDelayed(this, 1000)
                }
            })
        } else {
            btnToggleMusic.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_pause_24)
            mediaPlayer.start()
            musicSlider.valueTo = (mediaPlayer.duration / 1000).toFloat()
            this.runOnUiThread(object : Runnable {
                override fun run() {
                    musicSlider.value = (mediaPlayer.currentPosition / 1000).toFloat()
                    handler.postDelayed(this, 1000)
                }
            })
        }
    }

    private fun nextButtonClicked() {
        mediaPlayer.stop()
        mediaPlayer.release()
        position = (position + 1) % musicData.size
        uri = Uri.parse(musicData[position].path)
        mediaPlayer = MediaPlayer.create(this, uri)
        displayMusicLength()
        tvMusicTitle.text = musicData[position].title
        musicSlider.valueTo = (mediaPlayer.duration / 1000).toFloat()
        this.runOnUiThread(object : Runnable {
            override fun run() {
                musicSlider.value = (mediaPlayer.currentPosition / 1000).toFloat()
                handler.postDelayed(this, 1000)
            }
        })
        if (mediaPlayer.isPlaying) {
            btnToggleMusic.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_pause_24)
            mediaPlayer.start()
        } else btnToggleMusic.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_play_arrow_24)
    }
}