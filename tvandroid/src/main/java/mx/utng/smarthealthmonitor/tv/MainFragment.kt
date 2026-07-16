package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import mx.utng.shared.data.db.LecturaFC

class MainFragment : BrowseSupportFragment() {

    private val viewModel: TvViewModel by viewModels {
        TvViewModelFactory(
            context = requireContext().applicationContext
        )
    }
    private lateinit var estadisticasAdapter: ArrayObjectAdapter
    private lateinit var historialAdapter: ArrayObjectAdapter
    private lateinit var rowsAdapter: ArrayObjectAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = "SmartHealth TV"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(requireContext(), R.color.sh_primary)

        cargarFilas()
        observarDatos()

        setOnItemViewClickedListener { _, item, _, _ ->
            if (item is mx.utng.shared.data.db.LecturaFC) {
                val detail = DetailFragment.newInstance(item.id)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.main_browse_fragment, detail)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun observarDatos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { uiState ->
                    estadisticasAdapter.clear()
                    estadisticasAdapter.addAll(0, uiState.estadisticas)

                    historialAdapter.clear()
                    historialAdapter.addAll(0, uiState.lecturas)
                }
            }
        }
    }

    private fun cargarFilas() {
        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

        estadisticasAdapter = ArrayObjectAdapter(FCCardPresenter())
        historialAdapter = ArrayObjectAdapter(FCCardPresenter())

        rowsAdapter.add(ListRow(HeaderItem(0, "Estado Actual (3 dispositivos)"), estadisticasAdapter))
        rowsAdapter.add(ListRow(HeaderItem(1, "Historial Completo"), historialAdapter))

        this.adapter = rowsAdapter
    }
}