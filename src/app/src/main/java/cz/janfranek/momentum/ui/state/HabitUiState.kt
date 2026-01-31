package cz.janfranek.momentum.ui.state

import cz.janfranek.momentum.data.Habit

/**
 * UI state for a single habit card on the dashboard.
 * It combines the static Habit data with the dynamic progress calculated from entries.
 */
data class HabitUiState(
	val habit: Habit,
	val progress: Int = 0
)
