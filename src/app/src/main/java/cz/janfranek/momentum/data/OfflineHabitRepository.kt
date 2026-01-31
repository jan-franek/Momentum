package cz.janfranek.momentum.data

import kotlinx.coroutines.flow.Flow

class OfflineHabitRepository(
	private val habitDao: HabitDao,
	private val habitEntryDao: HabitEntryDao
) : HabitRepository {
	override fun getAllHabitsStream(): Flow<List<Habit>> = habitDao.getAllHabitsStream()

	override fun getHabitStream(id: Int): Flow<Habit?> = habitDao.getHabitStream(id)

	override fun getEntriesStream(habitId: Int): Flow<List<HabitEntry>> =
		habitEntryDao.getEntriesStream(habitId)

	override fun getEntriesForDateRangeStream(start: Long, end: Long): Flow<List<HabitEntry>> =
		habitEntryDao.getEntriesForDateRangeStream(start, end)

	override suspend fun insertHabit(habit: Habit) = habitDao.insert(habit)

	override suspend fun insertEntry(entry: HabitEntry) = habitEntryDao.insert(entry)

	override suspend fun updateHabit(habit: Habit) = habitDao.update(habit)

	override suspend fun deleteHabit(habit: Habit) = habitDao.delete(habit)

	override suspend fun deleteEntry(entry: HabitEntry) = habitEntryDao.delete(entry)
}
