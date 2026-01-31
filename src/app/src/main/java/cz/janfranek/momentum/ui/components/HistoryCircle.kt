package cz.janfranek.momentum.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.janfranek.momentum.ui.state.HistoryStatus

/**
 * A composable that displays a single history circle indicating whether the habit was met.
 */
@Composable
fun HistoryCircle(item: HistoryStatus) {
	val backgroundColor = when {
		item.isMet -> MaterialTheme.colorScheme.primary
		else -> MaterialTheme.colorScheme.surfaceVariant
	}

	val contentColor = when {
		item.isMet -> MaterialTheme.colorScheme.onPrimary
		else -> MaterialTheme.colorScheme.onSurfaceVariant
	}

	Column(horizontalAlignment = Alignment.CenterHorizontally) {
		Box(
			modifier = Modifier
				.size(32.dp)
				.clip(CircleShape)
				.background(backgroundColor),
			contentAlignment = Alignment.Center
		) {
			if (item.isMet) {
				Text("✓", color = contentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
			}
		}

		Spacer(modifier = Modifier.height(4.dp))

		Text(
			text = item.label,
			style = MaterialTheme.typography.labelSmall,
			color = MaterialTheme.colorScheme.onSurface
		)
	}
}
