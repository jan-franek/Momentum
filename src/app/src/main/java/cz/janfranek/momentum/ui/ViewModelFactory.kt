package cz.janfranek.momentum.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import cz.janfranek.momentum.data.HabitRepository
import cz.janfranek.momentum.ui.viewmodel.AddHabitViewModel
import cz.janfranek.momentum.ui.viewmodel.DashboardViewModel
import cz.janfranek.momentum.ui.viewmodel.HabitDetailViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: HabitRepository) : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
		return when {
			modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
				DashboardViewModel(repository) as T
			}

			modelClass.isAssignableFrom(HabitDetailViewModel::class.java) -> {
				val savedStateHandle = extras.createSavedStateHandle()
				HabitDetailViewModel(savedStateHandle, repository) as T
			}

			modelClass.isAssignableFrom(AddHabitViewModel::class.java) -> {
				AddHabitViewModel(repository) as T
			}

			else -> throw IllegalArgumentException("Unknown ViewModel class")
		}
	}
}
