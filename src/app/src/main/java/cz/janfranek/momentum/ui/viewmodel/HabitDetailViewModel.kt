package cz.janfranek.momentum.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.janfranek.momentum.data.Habit
import cz.janfranek.momentum.data.HabitEntry
import cz.janfranek.momentum.data.HabitRepository
import cz.janfranek.momentum.data.HabitType
import cz.janfranek.momentum.ui.state.HistoryStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

class HabitDetailViewModel(
	savedStateHandle: SavedStateHandle,
	private val repository: HabitRepository
) : ViewModel() {

	// Retrieve the ID passed via Navigation route "habit_detail/{habitId}"
	private val habitId: Int = checkNotNull(savedStateHandle["habitId"])

	// Get the specific habit
	val habitState: StateFlow<Habit?> = repository.getHabitStream(habitId)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = null
		)

	private val entriesStream = repository.getEntriesStream(habitId)

	// Get history for this specific habit
	val historyState: StateFlow<List<HabitEntry>> = entriesStream
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5000),
			initialValue = emptyList()
		)

	val historyStripState: StateFlow<List<HistoryStatus>> = combine(
		habitState,
		entriesStream
	) { habit, entries ->
		if (habit == null) emptyList()
		else calculateHistory(habit, entries)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = emptyList()
	)

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
				val weekNum =
					dateInWeekToCheck.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())

				list.add(HistoryStatus("$weekNum.", totalAmount >= habit.target))
			}
		}
		return list
	}

	fun deleteHabit() {
		viewModelScope.launch {
			habitState.value?.let { repository.deleteHabit(it) }
		}
	}

	fun deleteEntry(entry: HabitEntry) {
		viewModelScope.launch {
			repository.deleteEntry(entry)
		}
	}

	fun updateHabit(habit: Habit) {
		viewModelScope.launch {
			repository.updateHabit(habit)
		}
	}
}
