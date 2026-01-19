package cz.janfranek.momentum.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cz.janfranek.momentum.data.Habit
import cz.janfranek.momentum.data.HabitDao
import cz.janfranek.momentum.data.HabitEntry
import cz.janfranek.momentum.data.HabitType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * UI state for a single habit, including its progress.
 */
data class HabitUiState(
	val habit: Habit,
	val progress: Int = 0
)

/**
 * ViewModel for managing habits and their entries.
 */
class HabitViewModel(private val dao: HabitDao) : ViewModel() {
	//region Time Helpers
	private val zone = ZoneId.systemDefault()

	private val startOfDay: Long
		get() = LocalDate.now()
			.atStartOfDay(zone).toInstant().toEpochMilli()

	private val endOfDay: Long
		get() = LocalDate.now().plusDays(1)
			.atStartOfDay(zone).toInstant().toEpochMilli()

	private val startOfWeek: Long
		get() = LocalDate.now()
			.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
			.atStartOfDay(zone).toInstant().toEpochMilli()

	private val endOfWeek: Long
		get() = LocalDate.now().plusWeeks(1)
			.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
			.atStartOfDay(zone).toInstant().toEpochMilli()
	//endregion

	//region The Main Pipeline
	// We listen to TWO streams: Habits and Today's Entries
	// Whenever either changes, we re-calculate the list
	val uiState: StateFlow<List<HabitUiState>> = combine(
		dao.getAllHabits(),
		dao.getEntriesForDateRange(startOfWeek, endOfWeek)
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

			// 4. Sum the "amount" column
			val totalProgress = relevantEntries.sumOf { it.amount }

			HabitUiState(
				habit = habit,
				progress = totalProgress
			)
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000), // Keep alive for 5s after UI hides
		initialValue = emptyList()
	)
	//endregion

	//region User Actions
	fun addHabit(name: String, type: HabitType, target: Int, unit: String, batchSize: Int) {
		viewModelScope.launch {
			dao.insertHabit(
				Habit(
					name = name,
					type = type,
					target = target,
					unit = unit,
					batchSize = batchSize
				)
			)
		}
	}

	fun incrementHabit(habitId: Int, amount: Int) {
		viewModelScope.launch {
			val entry = HabitEntry(
				habitId = habitId,
				timestamp = System.currentTimeMillis(),
				amount = amount
			)
			dao.insertEntry(entry)
		}
	}

	fun deleteHabit(habit: Habit) {
		viewModelScope.launch {
			dao.deleteHabit(habit)
		}
	}

	fun undoLastEntry(habitId: Int) {
		viewModelScope.launch {
			val habit = uiState.value.find { it.habit.id == habitId }?.habit ?: return@launch

			// Determine the current time range
			val cutoffTime = when (habit.type) {
				HabitType.DAILY -> startOfDay
				HabitType.WEEKLY -> startOfWeek
			}

			// Try to find an entry in that zone
			val entryToDelete = dao.getLastEntrySince(habitId, cutoffTime)

			// Only delete if we found one
			if (entryToDelete != null) {
				dao.deleteEntry(entryToDelete)
			}
		}
	}

	fun getHabitStream(id: Int): Flow<Habit?> = dao.getHabitStream(id)

	fun getEntriesStream(id: Int): Flow<List<HabitEntry>> = dao.getEntriesStream(id)

	fun updateHabit(habit: Habit) {
		viewModelScope.launch {
			dao.updateHabit(habit)
		}
	}

	fun deleteEntry(entry: HabitEntry) {
		viewModelScope.launch {
			dao.deleteEntry(entry)
		}
	}
	//endregion
}

// The Factory
class HabitViewModelFactory(private val dao: HabitDao) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
			@Suppress("UNCHECKED_CAST")
			return HabitViewModel(dao) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}
