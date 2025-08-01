package com.org.basshead.feature.main.presentation

import com.org.basshead.feature.main.model.MainUiState
import com.org.basshead.utils.ui.BaseViewModel

sealed interface MainActions {
    data class SelectTab(val tabIndex: Int) : MainActions
}

class MainViewModel : BaseViewModel<MainUiState>(MainUiState()) {

    fun onAction(action: MainActions) {
        when (action) {
            is MainActions.SelectTab -> selectTab(action.tabIndex)
        }
    }

    private fun selectTab(tabIndex: Int) {
        val currentState = getContent()
        setContent(currentState.copy(selectedTab = tabIndex))
    }

    fun getSelectedTab(): Int {
        return getContent().selectedTab
    }
}
