package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import mx.utng.shared.data.db.LecturaFC
import mx.utng.shared.data.db.SmartHealthDB
// Importamos el R del módulo de TV para encontrar el contenedor del fragmento
import mx.utng.smarthealthmonitor.tv.R

class DetailFragment : DetailsSupportFragment(), OnActionClickedListener {

    companion object {
        const val ARG_LECTURA_ID = "lectura_id"
        const val ACTION_PLAY = 1L
        const val ACTION_BACK = 2L

        private const val TEST_AUDIO_URL =
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"

        fun newInstance(lecturaId: Int): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().also {
                    it.putInt(ARG_LECTURA_ID, lecturaId)
                }
            }
        }
    }

    private var lecturaActual: LecturaFC? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt(ARG_LECTURA_ID) ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            // Intentar buscar en la Base de Datos
            val lectura = SmartHealthDB.getDatabase(requireContext())
                .lecturaDao().obtenerPorId(id)

            if (lectura != null) {
                lecturaActual = lectura
                construirDetalle(lectura)
            } else {
                // FALLBACK: Si es la lectura id = 0 en tiempo real, creamos un objeto temporal
                // para que la interfaz NO se quede en blanco y muestre datos.
                // Ajusta los parámetros según las propiedades exactas de tu modelo 'LecturaFC'
                val lecturaTemporal = LecturaFC(
                    id = 0,
                    bpm = 80, // Puedes pasar un valor por defecto o serializar el objeto completo en los argumentos
                    hora = "En tiempo real"
                )
                lecturaActual = lecturaTemporal
                construirDetalle(lecturaTemporal)
            }
        }
    }
    private fun construirDetalle(lectura: LecturaFC) {
        val selector = ClassPresenterSelector()
        val dpPresenter = FullWidthDetailsOverviewRowPresenter(
            DetailsDescriptionPresenter()
        )
        dpPresenter.setOnActionClickedListener(this)
        selector.addClassPresenter(DetailsOverviewRow::class.java, dpPresenter)

        val row = DetailsOverviewRow(lectura)

        val iconRes = if (lectura.esNormal)
            android.R.drawable.ic_menu_compass
        else
            android.R.drawable.ic_dialog_alert
        row.imageDrawable = ContextCompat.getDrawable(requireContext(), iconRes)

        val actions = ArrayObjectAdapter()
        actions.add(Action(ACTION_PLAY, "▶ Reproducir alerta"))
        actions.add(Action(ACTION_BACK, "← Volver al historial"))
        row.actionsAdapter = actions

        val adapter = ArrayObjectAdapter(selector)
        adapter.add(row)
        this.adapter = adapter
    }

    override fun onActionClicked(action: Action) {
        when (action.id) {
            ACTION_PLAY -> {
                // CORREGIDO: Se extraen de manera segura los datos desde la propiedad de la clase
                val lectura = lecturaActual ?: return

                val playback = PlaybackFragment.newInstance(
                    url = TEST_AUDIO_URL,
                    title = "Alerta FC ${lectura.valorBpm} bpm"
                )

                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_browse_fragment, playback)
                    .addToBackStack(null)
                    .commit()
            }
            ACTION_BACK -> requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}