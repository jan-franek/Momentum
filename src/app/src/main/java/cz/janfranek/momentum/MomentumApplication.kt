package cz.janfranek.momentum

import android.app.Application
import cz.janfranek.momentum.data.HabitRepository
import cz.janfranek.momentum.data.MomentumDatabase
import cz.janfranek.momentum.data.OfflineHabitRepository

class MomentumApplication : Application() {
	private val database by lazy { MomentumDatabase.getDatabase(this) }
	
	val repository: HabitRepository by lazy {
		OfflineHabitRepository(database.habitDao(), database.habitEntryDao())
	}
}
