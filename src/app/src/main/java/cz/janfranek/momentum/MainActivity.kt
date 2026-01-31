package cz.janfranek.momentum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cz.janfranek.momentum.ui.theme.MomentumTheme

/**
 * The main activity of the Momentum app.
 */
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			MomentumTheme {
				MomentumNavHost()
			}
		}
	}
}
