package cz.janfranek.momentum.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cz.janfranek.momentum.data.Habit

/**
 * A dialog for editing an existing habit.
 */
@Composable
fun EditHabitDialog(habit: Habit, onDismiss: () -> Unit, onSave: (String, Int, Int) -> Unit) {
	var name by remember { mutableStateOf(habit.name) }
	var targetStr by remember { mutableStateOf(habit.target.toString()) }
	var batchStr by remember { mutableStateOf(habit.batchSize.toString()) }

	AlertDialog(
		onDismissRequest = onDismiss,
		title = { Text("Edit Habit") },
		text = {
			Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
				OutlinedTextField(
					value = targetStr,
					onValueChange = { targetStr = it },
					label = { Text("Target") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
				)
				OutlinedTextField(
					value = batchStr,
					onValueChange = { batchStr = it },
					label = { Text("Batch Size") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
				)
			}
		},
		confirmButton = {
			Button(onClick = {
				onSave(
					name,
					targetStr.toIntOrNull() ?: habit.target,
					batchStr.toIntOrNull() ?: habit.batchSize
				)
			}) { Text("Save") }
		},
		dismissButton = {
			TextButton(onClick = onDismiss) { Text("Cancel") }
		}
	)
}
