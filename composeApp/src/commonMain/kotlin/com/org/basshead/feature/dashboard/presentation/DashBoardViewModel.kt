package com.org.basshead.feature.dashboard.presentation

import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.dashboard.model.EmptyFestivalSuggestionState
import com.org.basshead.feature.dashboard.model.FestivalSuggestionState
import com.org.basshead.utils.ui.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DashBoardViewModel(val dashBoardInteractor: DashBoardInteractor) : BaseViewModel<FestivalSuggestionState>(EmptyFestivalSuggestionState) {

    init {

        baseViewModelScope.launch {
//           val result =  async { dashBoardInteractor.getFestivalSuggestions() }
//           val resultUf = async { dashBoardInteractor.getUserFestivals() }
            val resultUh = async { dashBoardInteractor.getDailyHeadbangs() }

//            val resultS = result.await()
//            val resultUfs = resultUf.await()
            val resultUhS = resultUh.await()

            val resut = resultUhS
        }
    }
}
