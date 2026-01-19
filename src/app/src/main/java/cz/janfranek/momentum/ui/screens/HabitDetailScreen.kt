package cz.janfranek.momentum.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.janfranek.momentum.R
import cz.janfranek.momentum.ui.components.EditHabitDialog
import cz.janfranek.momentum.ui.components.HabitHistoryStrip
import cz.janfranek.momentum.ui.components.HistoryItem
import cz.janfranek.momentum.ui.getLabelResourceId
import cz.janfranek.momentum.ui.viewmodel.HabitViewModel

/**
 * Screen displaying the details of a specific habit, including its history and options to edit or delete it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
	habitId: Int,
	viewModel: HabitViewModel,
	onBack: () -> Unit,
	onDeleteSuccess: () -> Unit
) {
	// Collect the live data
	val habit by viewModel.getHabitStream(habitId).collectAsState(initial = null)
	val entries by viewModel.getEntriesStream(habitId).collectAsState(initial = emptyList())

	var showEditDialog by remember { mutableStateOf(false) }

	// Habit is null
	if (habit == null) {
		Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			CircularProgressIndicator()
		}
		return
	}

	val safeHabit = habit!!

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(safeHabit.name) },
				navigationIcon = {
					// Back Button
					IconButton(onClick = onBack) {
						Icon(
							painter = painterResource(id = R.drawable.ic_back),
							contentDescription = stringResource(R.string.back)
						)
					}
				},
				actions = {
					// Edit Button
					IconButton(onClick = { showEditDialog = true }) {
						Icon(
							painter = painterResource(id = R.drawable.ic_edit),
							contentDescription = stringResource(R.string.edit)
						)
					}
					// Delete Button
					IconButton(onClick = {
						viewModel.deleteHabit(safeHabit)
						onDeleteSuccess()
					}) {
						Icon(
							painter = painterResource(id = R.drawable.ic_delete),
							contentDescription = stringResource(R.string.delete)
						)
					}
				}
			)
		}
	) { innerPadding ->
		Column(
			modifier = Modifier
				.padding(innerPadding)
				.padding(16.dp)
		) {

			// Overview Section
			Card(
				modifier = Modifier.fillMaxWidth(),
				colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
			) {
				Column(modifier = Modifier.padding(16.dp)) {
					Text(
						"${stringResource(R.string.detail_label_target)}: ${safeHabit.target} ${safeHabit.unit}",
						style = MaterialTheme.typography.bodyMedium
					)
					Text(
						"${stringResource(R.string.detail_label_batch_size)}: ${safeHabit.batchSize} ${safeHabit.unit}",
						style = MaterialTheme.typography.bodyMedium
					)
					Text(
						"${stringResource(R.string.detail_label_type)}: ${stringResource(safeHabit.type.getLabelResourceId())}",
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}

			Spacer(modifier = Modifier.height(24.dp))

			// History Strip
			HabitHistoryStrip(habit = safeHabit, entries = entries)

			Spacer(modifier = Modifier.height(24.dp))

			Text(
				stringResource(R.string.detail_title_history),
				style = MaterialTheme.typography.titleLarge
			)

			Spacer(modifier = Modifier.height(8.dp))

			// Entry History List
			LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				items(entries) { entry ->
					HistoryItem(entry = entry, onDelete = { viewModel.deleteEntry(entry) })
				}
			}
		}
	}

	// Edit Dialog Popup
	if (showEditDialog) {
		EditHabitDialog(
			habit = safeHabit,
			onDismiss = { showEditDialog = false },
			onSave = { name, target, batch ->
				// Create a copy of the habit with new values and save it
				viewModel.updateHabit(safeHabit.copy(name = name, target = target, batchSize = batch))
				showEditDialog = false
			}
		)
	}
}
