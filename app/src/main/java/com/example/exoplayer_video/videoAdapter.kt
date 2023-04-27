package com.example.exoplayer_video
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.exoplayer_video.databinding.ReelVideoBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource



class videoAdapter(val context: Context, var video: ArrayList<Video> ,var videoPreparedListener: onVideoPreparedListener) :
    RecyclerView.Adapter<videoAdapter.videoViewHolder>() {

    class videoViewHolder(
        val binding: ReelVideoBinding,
        var context: Context,
        var videoPreparedListener: onVideoPreparedListener
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var exoplayer: ExoPlayer
        private lateinit var mediaSource: MediaSource

        fun setVideoPath(url: String){
            exoplayer = ExoPlayer.Builder(context).build()
            exoplayer.addListener(object : Player.Listener{

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(context, "Can't play this Video",Toast.LENGTH_SHORT).show()

                }

                @Deprecated("Deprecated in Java")
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)
                    if(playbackState == Player.STATE_BUFFERING){
                        binding.ProgressBar.visibility = View.VISIBLE
                    }
                    else if (playbackState == Player.STATE_READY){
                        binding.ProgressBar.visibility = View.GONE
                    }
                }

            })
            binding.ExoplayerView.player = exoplayer

            exoplayer.seekTo(0)
            exoplayer.repeatMode = Player.REPEAT_MODE_ONE

            val dataSourceFactory = DefaultDataSource.Factory(context)

            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                MediaItem.fromUri(Uri.parse(url)))

            exoplayer.setMediaSource(mediaSource)
            exoplayer.prepare()

            if (absoluteAdapterPosition == 0){
                exoplayer.playWhenReady = true
                exoplayer.play()
            }

            videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoplayer,absoluteAdapterPosition))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): videoViewHolder {
        val view = ReelVideoBinding.inflate (LayoutInflater.from(context),parent,false)
        return videoViewHolder(view,context,videoPreparedListener)
    }

    override fun onBindViewHolder(holder: videoViewHolder, position: Int) {
        val model  = video[position]
        holder.setVideoPath(model.url)
    }

    override fun getItemCount(): Int {
        return video.size
    }

    interface onVideoPreparedListener{
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }
}
