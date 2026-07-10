package mx.utng.smarthealthmonitor.cast

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.media.CastMediaOptions
import com.google.android.gms.cast.CastMediaControlIntent

class CastOptionsProvider : OptionsProvider {

    override fun getCastOptions(context: Context): CastOptions =
        CastOptions.Builder()
            .setReceiverApplicationId(
                CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID
            )
            .build()

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider> =
        emptyList()
}

// Commit sugerido:
// git add app/src/main/java/mx/utng/smarthealthmonitor/cast/CastOptionsProvider.kt
// git commit -m "feat(cast): add CastOptionsProvider and AndroidManifest meta-data"