package cz.janfranek.momentum.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cz.janfranek.momentum.R
import cz.janfranek.momentum.data.HabitType

/**
 * Screen for adding a new habit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
	onSave: (String, HabitType, Int, String, Int) -> Unit,
	onCancel: () -> Unit
) {
	// Local State
	var name by remember { mutableStateOf("") }
	var type by remember { mutableStateOf(HabitType.DAILY) }
	var unit by remember { mutableStateOf("") }
	var targetStr by remember { mutableStateOf("") }
	var batchStr by remember { mutableStateOf("") }

	val isValid = name.isNotBlank()
		&& targetStr.toIntOrNull() != null
		&& batchStr.toIntOrNull() != null

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(stringResource(R.string.new_habit_title)) }
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.padding(16.dp)
				.fillMaxSize(),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {

			// Name
			OutlinedTextField(
				value = name,
				onValueChange = { name = it },
				label = { Text(stringResource(R.string.new_habit_label_name)) },
				placeholder = { Text(stringResource(R.string.new_habit_placeholder_name)) },
				modifier = Modifier.fillMaxWidth(),
				singleLine = true
			)

			// Type
			Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
				FilterChip(
					selected = type == HabitType.DAILY,
					onClick = { type = HabitType.DAILY },
					label = { Text(stringResource(R.string.new_habit_type_daily)) },
					leadingIcon = {
						if (type == HabitType.DAILY) {
							Icon(
								painterResource(R.drawable.ic_check),
								contentDescription = null
							)
						}
					}
				)
				FilterChip(
					selected = type == HabitType.WEEKLY,
					onClick = { type = HabitType.WEEKLY },
					label = { Text(stringResource(R.string.new_habit_type_weekly)) },
					leadingIcon = {
						if (type == HabitType.WEEKLY) {
							Icon(
								painterResource(R.drawable.ic_check),
								contentDescription = null
							)
						}
					}
				)
			}

			Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
				// Target (Number)
				OutlinedTextField(
					value = targetStr,
					onValueChange = { if (it.all { char -> char.isDigit() }) targetStr = it },
					label = { Text("Target") },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
					modifier = Modifier.weight(1f),
					singleLine = true
				)

				// Unit (Text)
				OutlinedTextField(
					value = unit,
					onValueChange = { unit = it },
					label = { Text(stringResource(R.string.new_habit_label_unit)) },
					placeholder = { Text(stringResource(R.string.new_habit_placeholder_unit)) },
					modifier = Modifier.weight(1f),
					singleLine = true
				)
			}

			// Default Batch Size
			OutlinedTextField(
				value = batchStr,
				onValueChange = { if (it.all { char -> char.isDigit() }) batchStr = it },
				label = { Text(stringResource(R.string.new_habit_label_batch_size)) },
				placeholder = { Text(stringResource(R.string.new_habit_placeholder_batch_size)) },
				supportingText = { Text(stringResource(R.string.new_habit_hint_batch_size)) },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
				modifier = Modifier.fillMaxWidth(),
				singleLine = true
			)

			Spacer(modifier = Modifier.weight(1f))

			Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
				OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
					Text(stringResource(R.string.cancel))
				}
				Button(
					onClick = {
						onSave(
							name,
							type,
							targetStr.toInt(),
							unit,
							batchStr.toInt()
						)
					},
					enabled = isValid,
					modifier = Modifier.weight(1f)
				) {
					Text(stringResource(R.string.save))
				}
			}
		}
	}
}
