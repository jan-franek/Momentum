package cz.janfranek.momentum.ui

import androidx.annotation.StringRes
import cz.janfranek.momentum.R
import cz.janfranek.momentum.data.HabitType

@StringRes
fun HabitType.getLabelResourceId(): Int {
	return when (this) {
		HabitType.DAILY -> R.string.dashboard_type_daily
		HabitType.WEEKLY -> R.string.dashboard_type_weekly
	}
}
