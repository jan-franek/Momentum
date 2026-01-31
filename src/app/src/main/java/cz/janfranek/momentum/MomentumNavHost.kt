package cz.janfranek.momentum

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.janfranek.momentum.ui.ViewModelFactory
import cz.janfranek.momentum.ui.screens.AddHabitScreen
import cz.janfranek.momentum.ui.screens.DashboardScreen
import cz.janfranek.momentum.ui.screens.HabitDetailScreen
import cz.janfranek.momentum.ui.viewmodel.AddHabitViewModel
import cz.janfranek.momentum.ui.viewmodel.DashboardViewModel
import cz.janfranek.momentum.ui.viewmodel.HabitDetailViewModel

/**
 * The main application composable that sets up navigation and screens.
 */
@Composable
fun MomentumNavHost() {
	val navController = rememberNavController()
	val context = LocalContext.current
	val app = context.applicationContext as MomentumApplication
	val factory = ViewModelFactory(app.repository)

	NavHost(
		navController = navController,
		startDestination = "dashboard"
	) {

		// SCREEN 1: DASHBOARD
		composable("dashboard") {
			val viewModel: DashboardViewModel = viewModel(factory = factory)

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
			val viewModel: AddHabitViewModel = viewModel(factory = factory)

			AddHabitScreen(
				viewModel = viewModel,
				onBack = {
					navController.popBackStack()
				}
			)
		}

		// SCREEN 3: HABIT DETAIL
		composable(
			route = "detail/{habitId}",
			arguments = listOf(navArgument("habitId") {
				type = NavType.IntType
			})
		) { backStackEntry ->
			val viewModel: HabitDetailViewModel = viewModel(factory = factory)

			HabitDetailScreen(
				viewModel = viewModel,
				onBack = { navController.popBackStack() }
			)
		}
	}
}
