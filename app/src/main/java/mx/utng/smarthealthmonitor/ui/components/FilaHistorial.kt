package mx.utng.smarthealthmonitor.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import mx.utng.shared.data.db.LecturaFC

@Composable
fun FilaHistorial(
    lectura: LecturaFC,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${lectura.valorBpm} bpm",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (lectura.esNormal)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(8.dp))
            val syncIcon = if (lectura.sincronizado) Icons.Default.Check else Icons.Default.Warning
            val syncTint = if (lectura.sincronizado) Color(0xFF4CAF50) else Color(0xFFFF9800)
            Icon(
                imageVector = syncIcon,
                contentDescription = if (lectura.sincronizado) "Sincronizado" else "Pendiente de sincronizar",
                tint = syncTint,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = lectura.hora,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
}
