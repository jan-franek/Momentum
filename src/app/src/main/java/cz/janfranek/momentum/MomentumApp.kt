package cz.janfranek.momentum

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.janfranek.momentum.data.MomentumDatabase
import cz.janfranek.momentum.ui.screens.AddHabitScreen
import cz.janfranek.momentum.ui.screens.DashboardScreen
import cz.janfranek.momentum.ui.screens.HabitDetailScreen
import cz.janfranek.momentum.ui.viewmodel.HabitViewModel
import cz.janfranek.momentum.ui.viewmodel.HabitViewModelFactory

/**
 * The main application composable that sets up navigation and screens.
 */
@Composable
fun MomentumApp() {
	val navController = rememberNavController()

	val context = LocalContext.current
	val database = MomentumDatabase.getDatabase(context)
	val viewModel: HabitViewModel = viewModel(
		factory = HabitViewModelFactory(database.habitDao())
	)

	NavHost(
		navController = navController,
		startDestination = "dashboard"
	) {

		// SCREEN 1: DASHBOARD
		composable("dashboard") {
			DashboardScreen(
				viewModel = viewModel,
				onAddHabitClick = {
					navController.navigate("add_habit")
				},
				onHabitClick = { habitId ->
					navController.navigate("detail/$habitId")
				}
			)
		}

		// SCREEN 2: ADD HABIT
		composable("add_habit") {
			AddHabitScreen(
				onSave = { name, type, target, unit, batchSize ->
					viewModel.addHabit(name, type, target, unit, batchSize)
					navController.popBackStack()
				},
				onCancel = {
					navController.popBackStack()
				}
			)
		}

		// SCREEN 3: HABIT DETAIL
		composable(
			route = "detail/{habitId}",
			arguments = listOf(androidx.navigation.navArgument("habitId") {
				type = androidx.navigation.NavType.IntType
			})
		) { backStackEntry ->
			val habitId = backStackEntry.arguments?.getInt("habitId") ?: return@composable

			HabitDetailScreen(
				habitId = habitId,
				viewModel = viewModel,
				onBack = { navController.popBackStack() },
				onDeleteSuccess = { navController.popBackStack() }
			)
		}
	}
}
