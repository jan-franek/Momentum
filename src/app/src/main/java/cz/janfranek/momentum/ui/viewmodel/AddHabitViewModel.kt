package cz.janfranek.momentum.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.janfranek.momentum.data.Habit
import cz.janfranek.momentum.data.HabitRepository
import cz.janfranek.momentum.data.HabitType
import kotlinx.coroutines.launch

class AddHabitViewModel(
	private val repository: HabitRepository,
) : ViewModel() {

	// Form State
	var name by mutableStateOf("")
	var type by mutableStateOf(HabitType.DAILY)
	var target by mutableStateOf("")
	var unit by mutableStateOf("")
	var batchSize by mutableStateOf("")

	fun saveHabit() {
		val targetInt = target.toIntOrNull() ?: 0
		val batchInt = batchSize.toIntOrNull() ?: 0

		if (name.isNotBlank() && targetInt > 0) {
			viewModelScope.launch {
				repository.insertHabit(
					Habit(
						name = name,
						type = type,
						target = targetInt,
						unit = unit,
						batchSize = batchInt
					)
				)
			}
		}
	}
}
