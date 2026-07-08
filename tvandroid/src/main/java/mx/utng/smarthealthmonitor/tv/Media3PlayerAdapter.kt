package mx.utng.smarthealthmonitor.tv

import android.content.Context
import androidx.leanback.media.PlayerAdapter
import androidx.leanback.widget.PlaybackControlsRow
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class Media3PlayerAdapter(
    private val context: Context,
    private val player: ExoPlayer
) : PlayerAdapter() {

    private val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            callback.onPlayStateChanged(this@Media3PlayerAdapter)
            callback.onCurrentPositionChanged(this@Media3PlayerAdapter)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_READY -> {
                    callback.onPreparedStateChanged(this@Media3PlayerAdapter)
                    callback.onDurationChanged(this@Media3PlayerAdapter)
                }
                Player.STATE_ENDED -> callback.onPlayStateChanged(this@Media3PlayerAdapter)
                else -> Unit
            }
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            callback.onCurrentPositionChanged(this@Media3PlayerAdapter)
        }
    }

    init {
        player.addListener(listener)
    }

    override fun getDuration(): Long =
        if (player.duration == androidx.media3.common.C.TIME_UNSET) -1 else player.duration

    override fun getCurrentPosition(): Long = player.currentPosition

    override fun isPlaying(): Boolean = player.isPlaying

    override fun play() {
        player.playWhenReady = true
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun seekTo(positionInMs: Long) {
        player.seekTo(positionInMs)
    }

    override fun getBufferedPosition(): Long = player.bufferedPosition

    override fun getSupportedActions(): Long {
        // Valores nativos constantes de Leanback para PlaybackControlsRow
        val playPause = 1L      // ACTION_PLAY_PAUSE
        val rewind = 16L        // ACTION_REWIND
        val fastForward = 64L   // ACTION_FAST_FORWARD

        return playPause or rewind or fastForward
    }

    fun release() {
        player.removeListener(listener)
    }
}