package com.org.basshead.utils.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * Extension function to handle navigation state.
 * Uses LaunchedEffect for proper auto-clearing on the UI thread.
 */
@Composable
fun UiState.Navigate.handleNavigation(
    onNavigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
    onClearNavigation: () -> Unit,
) {
    LaunchedEffect(this.route) {
        when (val route = this@handleNavigation.route) {
            is Route.Back -> {
                onNavigate("back", null, null)
            }
            is Route.InternalDirection -> {
                onNavigate(route.destination, route.popUpTp, route.inclusive)
            }
        }
        // Clear navigation after handling - this runs on the UI dispatcher
        onClearNavigation()
    }
}