package cz.janfranek.momentum.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a single entry of progress for a habit.
 */
@Entity(
	tableName = "habit_entry",
	foreignKeys = [
		ForeignKey(
			entity = Habit::class,
			parentColumns = ["id"],
			childColumns = ["habitId"],
			onDelete = ForeignKey.CASCADE
		)
	],
	indices = [Index("habitId")]
)
data class HabitEntry(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0,

	val habitId: Int,
	val timestamp: Long,
	val amount: Int = 1
)
