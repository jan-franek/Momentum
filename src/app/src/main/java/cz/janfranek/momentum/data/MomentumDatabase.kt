package cz.janfranek.momentum.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room database for the Momentum app.
 */
@Database(entities = [Habit::class, HabitEntry::class], version = 1, exportSchema = false)
abstract class MomentumDatabase : RoomDatabase() {
	abstract fun habitDao(): HabitDao
	abstract fun habitEntryDao(): HabitEntryDao

	companion object {
		@Volatile
		private var INSTANCE: MomentumDatabase? = null

		fun getDatabase(context: Context): MomentumDatabase {
			return INSTANCE ?: synchronized(this) {
				Room.databaseBuilder(
					context.applicationContext,
					MomentumDatabase::class.java,
					"momentum_database" // The actual filename on the phone
				)
					.build()
					.also { INSTANCE = it }
			}
		}
	}
}
