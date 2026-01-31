package cz.janfranek.momentum.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository for habits and their entries.
 */
interface HabitRepository {
	fun getAllHabitsStream(): Flow<List<Habit>>
	fun getHabitStream(id: Int): Flow<Habit?>
	fun getEntriesStream(habitId: Int): Flow<List<HabitEntry>>
	fun getEntriesForDateRangeStream(start: Long, end: Long): Flow<List<HabitEntry>>

	suspend fun insertHabit(habit: Habit)
	suspend fun insertEntry(entry: HabitEntry)
	suspend fun updateHabit(habit: Habit)
	suspend fun deleteHabit(habit: Habit)
	suspend fun deleteEntry(entry: HabitEntry)
}
