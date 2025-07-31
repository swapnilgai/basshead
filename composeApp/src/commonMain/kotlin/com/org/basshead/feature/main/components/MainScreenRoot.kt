package com.org.basshead.feature.main.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.org.basshead.feature.main.model.MainUiState
import com.org.basshead.feature.main.presentation.MainActions
import com.org.basshead.feature.main.presentation.MainViewModel
import com.org.basshead.utils.ui.UiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreenRoot(
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    // Remember callback functions to avoid recomposition
    val onTabSelected = remember<(Int) -> Unit> {
        { tabIndex ->
            viewModel.onAction(MainActions.SelectTab(tabIndex))
        }
    }

    when (val currentState = state.value) {
        is UiState.Content -> {
            val mainUiState = currentState.data as MainUiState
            MainScreen(
                selectedTab = mainUiState.selectedTab,
                onTabSelected = onTabSelected,
                navigate = navigate,
                modifier = modifier,
            )
        }
        else -> {
            // Handle other states if needed - show default state
            MainScreen(
                selectedTab = 0,
                onTabSelected = onTabSelected,
                navigate = navigate,
                modifier = modifier,
            )
        }
    }
}
