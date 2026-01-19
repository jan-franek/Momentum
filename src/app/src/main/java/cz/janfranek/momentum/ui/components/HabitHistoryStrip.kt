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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.janfranek.momentum.R
import cz.janfranek.momentum.data.Habit
import cz.janfranek.momentum.data.HabitEntry
import cz.janfranek.momentum.data.HabitType
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * Data class to hold the status of one circle
 */
data class HistoryStatus(
	val label: String,
	val isMet: Boolean
)

/**
 * A composable that displays a habit's history over the last 7 days or weeks.
 */
@Composable
fun HabitHistoryStrip(
	habit: Habit,
	entries: List<HabitEntry>
) {
	// 1. Calculate the last 7 periods (Days or Weeks)
	val historyItems = remember(habit, entries) {
		calculateHistory(habit, entries)
	}

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

// --- LOGIC HELPER ---
/**
 * Calculates the history status for the last 7 periods (days or weeks) based on habit entries.
 */
private fun calculateHistory(habit: Habit, entries: List<HabitEntry>): List<HistoryStatus> {
	val zone = ZoneId.systemDefault()
	val today = LocalDate.now()
	val list = mutableListOf<HistoryStatus>()

	// We look back 6 periods + today = 7 total
	for (i in 6 downTo 0) {
		if (habit.type == HabitType.DAILY) {
			// --- DAILY LOGIC ---
			val dateToCheck = today.minusDays(i.toLong())

			// Get entries for this specific day
			val dayStart = dateToCheck.atStartOfDay(zone).toInstant().toEpochMilli()
			val dayEnd = dateToCheck.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()

			val totalAmount = entries
				.filter { it.timestamp in dayStart until dayEnd }
				.sumOf { it.amount }

			// Get first letter of the day of the week
			val label = dateToCheck.dayOfWeek.getDisplayName(
				TextStyle.NARROW,
				Locale.getDefault()
			)

			list.add(HistoryStatus(label, totalAmount >= habit.target))

		} else {
			// --- WEEKLY LOGIC ---
			val dateInWeekToCheck = today.minusWeeks(i.toLong())
			val fieldISO = WeekFields.of(Locale.getDefault()).dayOfWeek()

			val startOfWeek = dateInWeekToCheck.with(fieldISO, 1)
				.atStartOfDay(zone).toInstant().toEpochMilli()
			val endOfWeek = dateInWeekToCheck.with(fieldISO, 7).plusDays(1)
				.atStartOfDay(zone).toInstant().toEpochMilli()

			val totalAmount = entries
				.filter { it.timestamp in startOfWeek until endOfWeek }
				.sumOf { it.amount }

			// Week Number
			val weekNum = dateInWeekToCheck.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())

			list.add(HistoryStatus("$weekNum.", totalAmount >= habit.target))
		}
	}
	return list
}
