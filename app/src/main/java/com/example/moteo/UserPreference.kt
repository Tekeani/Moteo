package com.example.moteo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property pour créer un DataStore unique par instance de Context
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

/**
 * Classe pour gérer les préférences utilisateur avec DataStore
 */
class UserPreferences(private val context: Context) {

    companion object {
        private val PSEUDO_KEY = stringPreferencesKey("pseudo")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")
    }

    /**
     * Sauvegarde les identifiants utilisateur en cas d'option "Se souvenir de moi"
     */
    suspend fun saveUserCredentials(pseudo: String, password: String, rememberMe: Boolean) {
        context.dataStore.edit { preferences ->
            if (rememberMe) {
                preferences[PSEUDO_KEY] = pseudo
                preferences[PASSWORD_KEY] = password
            } else {
                preferences.remove(PSEUDO_KEY)
                preferences.remove(PASSWORD_KEY)
            }
            preferences[REMEMBER_ME_KEY] = rememberMe
        }
    }

    /**
     * Récupère les identifiants utilisateur stockés
     */
    val userCredentialsFlow: Flow<UserCredentials> = context.dataStore.data.map { preferences ->
        UserCredentials(
            pseudo = preferences[PSEUDO_KEY] ?: "",
            password = preferences[PASSWORD_KEY] ?: "",
            rememberMe = preferences[REMEMBER_ME_KEY] ?: false
        )
    }

    /**
     * Efface les données de l'utilisateur (à utiliser pour la déconnexion)
     */
    suspend fun clearUserData() {
        context.dataStore.edit { preferences ->
            preferences.remove(PSEUDO_KEY)
            preferences.remove(PASSWORD_KEY)
            preferences.remove(REMEMBER_ME_KEY)
        }
    }
}

/**
 * Classe représentant les identifiants de l'utilisateur
 */
data class UserCredentials(
    val pseudo: String,
    val password: String,
    val rememberMe: Boolean
)