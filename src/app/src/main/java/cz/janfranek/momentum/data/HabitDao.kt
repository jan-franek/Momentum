package cz.janfranek.momentum.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for habits and their entries.
 */
@Dao
interface HabitDao {
	@Insert
	suspend fun insertHabit(habit: Habit)

	@Insert
	suspend fun insertEntry(entry: HabitEntry)

	@Update
	suspend fun updateHabit(habit: Habit)

	@Delete
	suspend fun deleteHabit(habit: Habit)

	@Delete
	suspend fun deleteEntry(entry: HabitEntry)

	@Query(
		"""
		SELECT *
		FROM habit
		WHERE id = :id
		"""
	)
	fun getHabitStream(id: Int): Flow<Habit?>

	// Get full history for one habit (newest first)
	@Query(
		"""
		SELECT *
		FROM habit_entry
		WHERE habitId = :habitId
		ORDER BY timestamp DESC
		"""
	)
	fun getEntriesStream(habitId: Int): Flow<List<HabitEntry>>

	@Query(
		"""
		SELECT *
		FROM habit
		"""
	)
	fun getAllHabits(): Flow<List<Habit>>

	// Sums the habit progress since a certain time
	@Query(
		"""
    SELECT SUM(amount)
    FROM habit_entry
    WHERE habitId = :habitId 
    AND timestamp >= :startTime
    """
	)
	fun getProgress(habitId: Int, startTime: Long): Flow<Int?>

	// Get ALL entries between two times
	@Query(
		"""
    SELECT * 
    FROM habit_entry
    WHERE timestamp >= :start
    AND timestamp <= :end
    """
	)
	fun getEntriesForDateRange(start: Long, end: Long): Flow<List<HabitEntry>>

	@Query(
		"""
    SELECT * FROM habit_entry
    WHERE habitId = :habitId
    AND timestamp >= :cutoff
    ORDER BY timestamp DESC
    LIMIT 1
		"""
	)
	suspend fun getLastEntrySince(habitId: Int, cutoff: Long): HabitEntry?
}
