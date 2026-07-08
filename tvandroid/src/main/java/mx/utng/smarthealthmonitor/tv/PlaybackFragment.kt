package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import android.view.View
import androidx.leanback.app.PlaybackSupportFragment
import androidx.leanback.app.PlaybackSupportFragmentGlueHost
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class PlaybackFragment : PlaybackSupportFragment() {

    private lateinit var player: ExoPlayer
    private lateinit var playerAdapter: Media3PlayerAdapter
    private var glue: PlaybackTransportControlGlue<Media3PlayerAdapter>? = null

    companion object {
        const val ARG_URL = "media_url"
        const val ARG_TITLE = "media_title"

        fun newInstance(url: String, title: String = "Alerta"): PlaybackFragment =
            PlaybackFragment().apply {
                arguments = Bundle().also {
                    it.putString(ARG_URL, url)
                    it.putString(ARG_TITLE, title)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString(ARG_URL) ?: return
        val title = arguments?.getString(ARG_TITLE) ?: ""

        // 1. Inicializar ExoPlayer (Media3)
        player = ExoPlayer.Builder(requireContext()).build()

        // 2. Vincular con Leanback usando nuestro adaptador intermedio
        playerAdapter = Media3PlayerAdapter(requireContext(), player)

        val transportGlue = PlaybackTransportControlGlue(requireContext(), playerAdapter).apply {
            this.title = title
            this.subtitle = "SmartHealth Monitor"
            host = PlaybackSupportFragmentGlueHost(this@PlaybackFragment)
            isControlsOverlayAutoHideEnabled = true
        }
        glue = transportGlue

        // 3. Preparar el recurso multimedia y reproducir
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.playWhenReady = true
    }

    override fun onPause() {
        super.onPause()
        if (::player.isInitialized) {
            player.pause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::playerAdapter.isInitialized) playerAdapter.release()
        if (::player.isInitialized) player.release()
        glue = null
    }
}