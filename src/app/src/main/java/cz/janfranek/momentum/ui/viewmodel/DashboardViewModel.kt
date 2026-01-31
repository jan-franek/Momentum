package cz.janfranek.momentum.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.janfranek.momentum.data.HabitEntry
import cz.janfranek.momentum.data.HabitRepository
import cz.janfranek.momentum.data.HabitType
import cz.janfranek.momentum.ui.state.HabitUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class DashboardViewModel(private val repository: HabitRepository) : ViewModel() {

	//region Time Helpers
	private val zone = ZoneId.systemDefault()

	private val startOfDay: Long
		get() = LocalDate.now().atStartOfDay(zone).toInstant().toEpochMilli()

	private val startOfWeek: Long
		get() = LocalDate.now()
			.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
			.atStartOfDay(zone).toInstant().toEpochMilli()

	private val endOfWeek: Long
		get() = LocalDate.now().plusWeeks(1)
			.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
			.atStartOfDay(zone).toInstant().toEpochMilli()
	//endregion

	// We listen to TWO streams: Habits and Today's Entries
	// Whenever either changes, we re-calculate the list
	val uiState: StateFlow<List<HabitUiState>> = combine(
		repository.getAllHabitsStream(),
		repository.getEntriesForDateRangeStream(startOfWeek, endOfWeek)
	) { habits, entries ->
		// 1. Pre-group entries by habitId
		val entriesByHabit = entries.groupBy { it.habitId }

		// 2. Map habits to UI state using the grouped map
		habits.map { habit ->
			val habitEntries = entriesByHabit[habit.id] ?: emptyList()

			// 3. Filter entries based on habit type
			val relevantEntries = when (habit.type) {
				HabitType.DAILY -> habitEntries.filter { it.timestamp >= startOfDay }
				HabitType.WEEKLY -> habitEntries // startOfWeek is already handled by the query
			}

			HabitUiState(
				habit = habit,
				progress = relevantEntries.sumOf { it.amount }
			)
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000), // Keep alive for 5s after UI hides
		initialValue = emptyList()
	)

	fun incrementHabit(habitId: Int, amount: Int) {
		viewModelScope.launch {
			repository.insertEntry(
				HabitEntry(habitId = habitId, timestamp = System.currentTimeMillis(), amount = amount)
			)
		}
	}
}
