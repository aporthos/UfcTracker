package com.portes.ufctracker.core.domain.usecase

import com.portes.ufctracker.core.data.repositories.PreferencesRepository
import javax.inject.Inject

class SaveNicknameUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(nickname: String) {
        if (preferencesRepository.getNickname().isEmpty()) {
            preferencesRepository.saveNickname(nickname)
        }
    }
}

class GetNicknameUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(): String = preferencesRepository.getNickname()
}
