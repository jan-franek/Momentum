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
	suspend fun insert(habit: Habit)

	@Update
	suspend fun update(habit: Habit)

	@Delete
	suspend fun delete(habit: Habit)

	@Query(
		"""
		SELECT *
		FROM habit
		WHERE id = :id
		"""
	)
	fun getHabitStream(id: Int): Flow<Habit?>

	@Query(
		"""
		SELECT *
		FROM habit
		"""
	)
	fun getAllHabitsStream(): Flow<List<Habit>>

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


}
