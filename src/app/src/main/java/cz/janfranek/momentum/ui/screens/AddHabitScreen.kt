package cz.janfranek.momentum.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.janfranek.momentum.R
import cz.janfranek.momentum.ui.components.HabitForm
import cz.janfranek.momentum.ui.viewmodel.AddHabitViewModel

/**
 * Screen for adding a new habit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(
	viewModel: AddHabitViewModel,
	onBack: () -> Unit
) {
	val isValid = viewModel.name.isNotBlank() &&
		viewModel.target.toIntOrNull() != null &&
		viewModel.batchSize.toIntOrNull() != null

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
			HabitForm(
				name = viewModel.name, onNameChange = { viewModel.name = it },
				type = viewModel.type, onTypeChange = { viewModel.type = it },
				target = viewModel.target, onTargetChange = { viewModel.target = it },
				unit = viewModel.unit, onUnitChange = { viewModel.unit = it },
				batchSize = viewModel.batchSize, onBatchSizeChange = { viewModel.batchSize = it },
				isUnitEditable = true
			)

			Spacer(modifier = Modifier.weight(1f))

			// Buttons
			Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
				OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
					Text(stringResource(R.string.cancel))
				}
				Button(
					onClick = {
						viewModel.saveHabit()
						onBack()
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
