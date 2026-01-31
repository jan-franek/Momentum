package cz.janfranek.momentum.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cz.janfranek.momentum.R
import cz.janfranek.momentum.data.HabitType

@Composable
fun HabitForm(
	name: String, onNameChange: (String) -> Unit,
	type: HabitType, onTypeChange: (HabitType) -> Unit,
	target: String, onTargetChange: (String) -> Unit,
	unit: String, onUnitChange: (String) -> Unit,
	batchSize: String, onBatchSizeChange: (String) -> Unit,
	isUnitEditable: Boolean = true // Default is true (for Add screen)
) {
	Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
		// Name
		OutlinedTextField(
			value = name,
			onValueChange = onNameChange,
			label = { Text(stringResource(R.string.new_habit_label_name)) },
			placeholder = { Text(stringResource(R.string.new_habit_placeholder_name)) },
			modifier = Modifier.fillMaxWidth(),
			singleLine = true
		)

		// Type Selection
		Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			FilterChip(
				selected = type == HabitType.DAILY,
				onClick = { onTypeChange(HabitType.DAILY) },
				label = { Text(stringResource(R.string.new_habit_type_daily)) },
				leadingIcon = {
					if (type == HabitType.DAILY) {
						Icon(
							painterResource(R.drawable.ic_check),
							null
						)
					}
				}
			)
			FilterChip(
				selected = type == HabitType.WEEKLY,
				onClick = { onTypeChange(HabitType.WEEKLY) },
				label = { Text(stringResource(R.string.new_habit_type_weekly)) },
				leadingIcon = {
					if (type == HabitType.WEEKLY) {
						Icon(
							painterResource(R.drawable.ic_check),
							null
						)
					}
				}
			)
		}

		Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			// Target
			OutlinedTextField(
				value = target,
				onValueChange = { if (it.all { c -> c.isDigit() }) onTargetChange(it) },
				label = { Text(stringResource(R.string.new_habit_label_target)) },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
				modifier = Modifier.weight(1f),
				singleLine = true
			)

			// Unit (Enabled based on parameter)
			OutlinedTextField(
				value = unit,
				onValueChange = onUnitChange,
				enabled = isUnitEditable, // Controlled here
				label = { Text(stringResource(R.string.new_habit_label_unit)) },
				placeholder = { Text(stringResource(R.string.new_habit_placeholder_unit)) },
				modifier = Modifier.weight(1f),
				singleLine = true
			)
		}

		// Batch Size
		OutlinedTextField(
			value = batchSize,
			onValueChange = { if (it.all { c -> c.isDigit() }) onBatchSizeChange(it) },
			label = { Text(stringResource(R.string.new_habit_label_batch_size)) },
			placeholder = { Text(stringResource(R.string.new_habit_placeholder_batch_size)) },
			keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
			modifier = Modifier.fillMaxWidth(),
			singleLine = true
		)
	}
}
