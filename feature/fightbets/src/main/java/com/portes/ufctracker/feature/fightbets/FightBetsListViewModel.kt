package com.portes.ufctracker.feature.fightbets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.domain.usecase.CategoryFightBets
import com.portes.ufctracker.core.domain.usecase.GetFightBetsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FightBetsListViewModel @Inject constructor(
    getFightBetsListUseCase: GetFightBetsListUseCase
) : ViewModel() {

    val uiState: StateFlow<FightBetsListUiState> =
        getFightBetsListUseCase(Unit).map { result ->
            when (result) {
                is Result.Success -> FightBetsListUiState.Success(result.data)
                is Result.Error -> FightBetsListUiState.Error(result.exception)
                Result.Loading -> FightBetsListUiState.Loading
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = FightBetsListUiState.Loading,
            started = SharingStarted.WhileSubscribed(),
        )
}

sealed interface FightBetsListUiState {
    object Loading : FightBetsListUiState
    data class Success(var events: List<CategoryFightBets>) : FightBetsListUiState
    data class Error(val error: String? = null) : FightBetsListUiState
}
