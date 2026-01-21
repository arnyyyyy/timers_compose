package com.arno.timers_compose.core

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferencesManager(context: Context) {
        private val prefs: SharedPreferences = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        )

        fun setSkipAuth(skipAuth: Boolean) {
                prefs.edit {
                        putBoolean(KEY_SKIP_AUTH, skipAuth)
                }
        }

        fun isAuthSkipped(): Boolean {
                return prefs.getBoolean(KEY_SKIP_AUTH, false)
        }

        fun clearSkipAuth() {
                prefs.edit {
                        remove(KEY_SKIP_AUTH)
                }
        }

        companion object {
                private const val PREFS_NAME = "user_preferences"
                private const val KEY_SKIP_AUTH = "skip_auth"
        }
}
