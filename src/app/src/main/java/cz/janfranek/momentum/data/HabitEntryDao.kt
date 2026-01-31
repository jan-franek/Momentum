package cz.janfranek.momentum.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitEntryDao {
	@Insert
	suspend fun insert(entry: HabitEntry)

	@Delete
	suspend fun delete(entry: HabitEntry)

	// Get full history for one habit (newest entries first)
	@Query(
		"""
		SELECT *
		FROM habit_entry
		WHERE habitId = :habitId
		ORDER BY timestamp DESC
		"""
	)
	fun getEntriesStream(habitId: Int): Flow<List<HabitEntry>>

	// Get ALL entries between two times
	@Query(
		"""
    SELECT * 
    FROM habit_entry
    WHERE timestamp >= :start
    AND timestamp <= :end
    """
	)
	fun getEntriesForDateRangeStream(start: Long, end: Long): Flow<List<HabitEntry>>
}
