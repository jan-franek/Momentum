package cz.janfranek.momentum.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.janfranek.momentum.R
import cz.janfranek.momentum.data.HabitType
import cz.janfranek.momentum.ui.components.HabitCard
import cz.janfranek.momentum.ui.viewmodel.DashboardViewModel

/**
 * The main dashboard screen displaying habits.
 */
@OptIn(ExperimentalMaterial3Api::class) // Needed for TopAppBar in some versions
@Composable
fun DashboardScreen(
	viewModel: DashboardViewModel,
	onAddHabitClick: () -> Unit,
	onHabitClick: (Int) -> Unit
) {
	val habits by viewModel.uiState.collectAsState()

	// 0 = Daily, 1 = Weekly
	var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

	// Filter the list based on the selected tab
	val visibleHabits = habits.filter {
		if (selectedTabIndex == 0) it.habit.type == HabitType.DAILY
		else it.habit.type == HabitType.WEEKLY
	}

	Scaffold(
		topBar = {
			Column {
				CenterAlignedTopAppBar(title = { Text(stringResource(R.string.app_name)) })

				// THE TABS
				SecondaryTabRow(selectedTabIndex = selectedTabIndex) {
					Tab(
						selected = selectedTabIndex == 0,
						onClick = { selectedTabIndex = 0 },
						text = { Text(stringResource(R.string.dashboard_type_daily)) }
					)
					Tab(
						selected = selectedTabIndex == 1,
						onClick = { selectedTabIndex = 1 },
						text = { Text(stringResource(R.string.dashboard_type_weekly)) }
					)
				}
			}
		},
		floatingActionButton = {
			FloatingActionButton(onClick = onAddHabitClick) {
				Icon(
					painter = painterResource(id = R.drawable.ic_add),
					contentDescription = stringResource(R.string.dashboard_button_description_new_habit)
				)
			}
		}
	) { innerPadding ->
		// THE LIST
		LazyColumn(
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize(),
			contentPadding = PaddingValues(
				top = 16.dp,
				start = 16.dp,
				end = 16.dp,
				// Add extra padding at the bottom so the last item clears the FloatingActionButton
				bottom = 88.dp
			),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			items(visibleHabits) { uiState ->
				HabitCard(
					state = uiState,
					onTrackClick = {
						viewModel.incrementHabit(uiState.habit.id, uiState.habit.batchSize)
					},
					onClick = { onHabitClick(uiState.habit.id) }
				)
			}

			// Empty state
			if (visibleHabits.isEmpty()) {
				item {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(32.dp),
						contentAlignment = Alignment.Center
					) {
						Text(
							text = if (selectedTabIndex == 0) stringResource(R.string.dashboard_no_daily_habits_message)
							else stringResource(R.string.dashboard_no_weekly_habits_message),
							style = MaterialTheme.typography.bodyLarge,
							color = MaterialTheme.colorScheme.onSurfaceVariant
						)
					}
				}
			}
		}
	}
}
