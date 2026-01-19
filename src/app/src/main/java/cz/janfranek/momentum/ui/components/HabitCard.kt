package cz.janfranek.momentum.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cz.janfranek.momentum.R
import cz.janfranek.momentum.ui.viewmodel.HabitUiState

/**
 * A card component that displays a habit's information and progress.
 */
@Composable
fun HabitCard(
	state: HabitUiState,
	onTrackClick: () -> Unit,
	onClick: () -> Unit
) {
	val habit = state.habit
	val isFinished = state.progress >= habit.target

	Card(
		onClick = onClick,
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Row(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			// --- LEFT SIDE: Text ---
			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = habit.name,
					style = MaterialTheme.typography.titleMedium
				)

				Spacer(modifier = Modifier.height(4.dp)) // Tiny gap

				// A simple progress bar
				// progress expects a float between 0.0 and 1.0
				LinearProgressIndicator(
					progress = { (state.progress / habit.target.toFloat()).coerceIn(0f, 1f) },
					modifier = Modifier.fillMaxWidth(0.8f),
					color = if (isFinished) {
						MaterialTheme.colorScheme.primary
					} else {
						MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
					},
					trackColor = MaterialTheme.colorScheme.surfaceVariant
				)

				// The Text Counter
				Text(
					text = if (isFinished) "${state.progress} / ${habit.target} ${habit.unit} - Completed!"
					else "${state.progress} / ${habit.target} ${habit.unit}",

					style = MaterialTheme.typography.bodySmall,

					fontWeight = if (isFinished) FontWeight.Bold else null,

					color = if (isFinished) MaterialTheme.colorScheme.primary
					else MaterialTheme.colorScheme.onSurfaceVariant
				)
			}

			// --- RIGHT SIDE: The Track Button ---
			Row(verticalAlignment = Alignment.CenterVertically) {
				FilledIconButton(onClick = onTrackClick) {
					Icon(
						painter = painterResource(id = R.drawable.ic_add),
						contentDescription = "Increment"
					)
				}
			}
		}
	}
}
