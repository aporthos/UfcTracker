package com.portes.ufctracker.core.data.repositories

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesRepository {

    companion object {
        private const val NICKNAME = "nickname"
    }

    private val preferences =
        context.getSharedPreferences("GLOBAL_PREFERENCES_NAME", Context.MODE_PRIVATE)

    override fun saveNickname(nickname: String) {
        preferences.edit {
            putString(NICKNAME, nickname)
        }
    }

    override fun getNickname(): String = preferences.getString(NICKNAME, "").orEmpty()
}

interface PreferencesRepository {
    fun saveNickname(nickname: String)
    fun getNickname(): String
}
