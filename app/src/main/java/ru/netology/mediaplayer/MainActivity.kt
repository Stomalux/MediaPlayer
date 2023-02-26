package ru.netology.mediaplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    private val observer = MediaLifecycleObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycle.addObserver(observer)


        findViewById<View>(R.id.play).setOnClickListener {

           observer.apply {
                   // resources.openRawResourceFd(R.raw.a1).use { afd ->
                   //mediaPlayer?.setDataSource(afd.fileDescriptor,afd.startOffset,afd.length)
                   mediaPlayer?.setDataSource("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
           }.play()

        }
    }
}