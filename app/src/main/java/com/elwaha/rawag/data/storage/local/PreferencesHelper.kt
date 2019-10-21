package com.elwaha.rawag.data.storage.local

import android.content.Context
import android.preference.PreferenceManager

class PreferencesHelper(private val context: Context) {

    companion object {
        private const val IS_LOGGED_IN = "isLoggedIn"
        private const val USER = "user"
        private const val LANGUAGE = "language"
    }

    private val preference = PreferenceManager.getDefaultSharedPreferences(context)

    var isLoggedIn = preference.getBoolean(IS_LOGGED_IN, false)
        set(value) = preference.edit().putBoolean(IS_LOGGED_IN, value).apply()

    var user = preference.getString(USER, null)
        set(value) = preference.edit().putString(USER, value).apply()


    var language = preference.getString(LANGUAGE, null)
        //    var language = preference.getString(LANGUAGE, Constants.Language.ARABIC.value)
        set(value) = preference.edit().putString(LANGUAGE, value).apply()

    fun clear() {
        val lang = language
        preference.edit().clear().apply()
        language = lang
    }
}