package com.org.basshead.utils.ui

import androidx.annotation.MainThread
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.basshead.utils.core.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus

@Immutable
sealed interface Route {
    data class Back(val source: String?): Route
    data class InternalDirection(val destination: String, val popUpTp: String? = null, val inclusive: Boolean = false): Route
}

@Immutable
sealed interface UiState<out T>{
    data class Error(val message: UiText): UiState<Nothing>
    data class Content<T>(val data: T, val isLoadingUi:  Boolean = true): UiState<T>
    data class Navigate(val route: Route): UiState<Route>
}

open class BaseViewModel<T>(initialContent: T): ViewModel() {
    private val _errorState: MutableStateFlow<UiState.Error?> = MutableStateFlow(null)
    private val _contentState: MutableStateFlow<UiState.Content<T>> =
        MutableStateFlow(UiState.Content(initialContent))
    private val _navState: MutableStateFlow<UiState.Navigate?> = MutableStateFlow(null)

    val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->

    }
    val baseViewModelScope = viewModelScope + coroutineExceptionHandler

    val state = combine(_contentState, _errorState, _navState) { content, error, navigation ->
        error ?: navigation ?: content
    }.distinctUntilChanged().filterNotNull().stateIn(
        baseViewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        UiState.Content(initialContent)
    )

    protected fun getContent() : T { return _contentState.value.data }

    @MainThread
    protected fun setContent(data: T){
        _errorState.update { null }
        _contentState.update{
            UiState.Content(data = data, isLoadingUi = false)
        }
    }

    @MainThread
    protected fun setLoading(){
        _errorState.update { null }
        _contentState.update {
            it.copy(isLoadingUi = true)
        }
    }

    @MainThread
    protected fun setError(msg: UiText){
        _errorState.update { UiState.Error(message = msg) }
    }

    @MainThread
    protected fun navigate(destination: String, popUpTp : String ? = null, inclusive: Boolean = false) {
        _navState.update { UiState.Navigate(Route.InternalDirection(destination = destination, popUpTp = popUpTp, inclusive = inclusive)) }
    }

    protected fun navigateBack(source: String? = null){
        _navState.update { UiState.Navigate(Route.Back(source = source)) }
    }
}