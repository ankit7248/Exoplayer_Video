package com.example.exoplayer_video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.exoplayer_video.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Using View Binding

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: videoAdapter
    private var videos = ArrayList<Video>()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videos.add(
            Video(
                "https://player.vimeo.com/external/135736646.sd.mp4?s=db2924c48ef91f17fc05da74603d5f89&profile_id=164"
            )
        )

        videos.add(
            Video(
                "https://cdn.pixabay.com/vimeo/155244112/Islands%20-%202119.mp4?width=1920&hash=25b78882309844628594f1a650413dce16ba0a75"
            )
        )

        videos.add(
            Video(
                "https://cdn.pixabay.com/vimeo/250154065/Sea%20-%2013704.mp4?width=1280&hash=d3688d126b9e833eccd3e970419ca2887dee5773"
            )
        )

        videos.add(
            Video(
                "https://player.vimeo.com/external/135736646.sd.mp4?s=db2924c48ef91f17fc05da74603d5f89&profile_id=164"
            )
        )

        adapter =
            videoAdapter(this@MainActivity, videos, object : videoAdapter.onVideoPreparedListener {

                override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {

                    exoPlayerItems.add(exoPlayerItem)
                }
            })
        binding.ViewPager2.adapter = adapter

        binding.ViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if (previousIndex != -1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }

                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.ViewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.ViewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }

        }
    }
}