package cz.janfranek.momentum.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import cz.janfranek.momentum.R
import cz.janfranek.momentum.data.Habit

/**
 * A dialog for editing an existing habit.
 */
@Composable
fun EditHabitDialog(
	habit: Habit,
	onDismiss: () -> Unit,
	onSave: (Habit) -> Unit
) {
	var name by rememberSaveable { mutableStateOf(habit.name) }
	var type by rememberSaveable { mutableStateOf(habit.type) }
	var target by rememberSaveable { mutableStateOf(habit.target.toString()) }
	var batchSize by rememberSaveable { mutableStateOf(habit.batchSize.toString()) }
	val unit = habit.unit

	AlertDialog(
		onDismissRequest = onDismiss,
		title = { Text(stringResource(R.string.detail_title_edit_habit)) },
		text = {
			HabitForm(
				name = name, onNameChange = { name = it },
				type = type, onTypeChange = { type = it },
				target = target, onTargetChange = { target = it },
				unit = unit, onUnitChange = { },
				batchSize = batchSize, onBatchSizeChange = { batchSize = it },
				isUnitEditable = false
			)
		},
		confirmButton = {
			Button(onClick = {
				onSave(
					habit.copy(
						name = name,
						type = type,
						target = target.toIntOrNull() ?: habit.target,
						unit = habit.unit, // Ensure original unit is passed back
						batchSize = batchSize.toIntOrNull() ?: habit.batchSize
					)
				)
			}) { Text(stringResource(R.string.save)) }
		},
		dismissButton = {
			TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
		}
	)
}
