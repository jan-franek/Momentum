package cz.janfranek.momentum.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.janfranek.momentum.R
import cz.janfranek.momentum.data.Habit
import cz.janfranek.momentum.data.HabitType
import cz.janfranek.momentum.ui.state.HistoryStatus

/**
 * A composable that displays a habit's history over the last 7 days or weeks.
 */
@Composable
fun HabitHistoryStrip(
	habit: Habit,
	historyItems: List<HistoryStatus>
) {
	Column(modifier = Modifier.fillMaxWidth()) {
		Text(
			text = if (habit.type == HabitType.DAILY) stringResource(R.string.detail_stat_last_7_days)
			else stringResource(R.string.detail_stat_last_7_weeks),
			style = MaterialTheme.typography.titleMedium
		)

		Spacer(modifier = Modifier.height(8.dp))

		// 2. Render the Row of Circles
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			historyItems.forEach { item ->
				HistoryCircle(item)
			}
		}
	}
}
