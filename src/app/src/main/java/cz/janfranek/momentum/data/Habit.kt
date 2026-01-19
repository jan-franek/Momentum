package cz.janfranek.momentum.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Type of habit: daily or weekly.
 */
enum class HabitType {
	DAILY,
	WEEKLY
}

/**
 * Represents a habit to be tracked.
 */
@Entity(tableName = "habit")
data class Habit(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,

	val name: String,
	val type: HabitType,
	val target: Int,
	val unit: String,
	val batchSize: Int = 1
)
