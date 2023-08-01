package com.portes.ufctracker.feature.fightbets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.portes.ufctracker.core.common.models.Result
import com.portes.ufctracker.core.domain.usecase.CategoryFightBets
import com.portes.ufctracker.core.domain.usecase.GetFightBetsListUseCase
import com.portes.ufctracker.core.domain.usecase.InfoLastFight
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FightBetsListViewModel @Inject constructor(
    private val getFightBetsListUseCase: GetFightBetsListUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _fightBetsListUiState =
        MutableStateFlow<FightBetsListUiState>(FightBetsListUiState.Loading)
    val uiState: StateFlow<FightBetsListUiState> = _fightBetsListUiState.asStateFlow()

    private var categoryFightBets = mutableListOf<CategoryFightBets>()

    init {
        viewModelScope.launch {
            getFightsBets()
        }
    }

    private suspend fun getFightsBets() {
        getFightBetsListUseCase(Unit).collect { result ->
            when (result) {
                Result.Loading -> {
                    _isRefreshing.update { true }
                    _fightBetsListUiState.update { FightBetsListUiState.Loading }
                }
                is Result.Success -> {
                    _isRefreshing.update { false }
                    categoryFightBets = result.data.categoryFightBets.toMutableList()
                    _fightBetsListUiState.update { FightBetsListUiState.Success(result.data) }
                }
                is Result.Error -> {
                    _isRefreshing.update { false }
                    _fightBetsListUiState.update { FightBetsListUiState.Error(result.exception) }
                }
            }
        }
    }

    fun shareResults(): String {
        var share = ""
        categoryFightBets.forEach {
            share += "${it.name} ${it.fightsWins} - ${it.fightsLost}, \n"
        }
        return share
    }

    fun onRefresh() {
        viewModelScope.launch {
            getFightsBets()
        }
    }
}

sealed interface FightBetsListUiState {
    object Loading : FightBetsListUiState
    data class Success(var lastFight: InfoLastFight) : FightBetsListUiState
    data class Error(val error: String? = null) : FightBetsListUiState
}
