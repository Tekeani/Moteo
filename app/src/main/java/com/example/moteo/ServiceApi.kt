package com.example.moteo

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

/**
 * Service qui gère les appels API pour l'authentification et les données météo
 */
class ApiService {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        // Configuration des timeouts
        engine {
            connectTimeout = 60_000
            socketTimeout = 60_000
        }
    }

    // URL de base de l'API - à remplacer par votre URL réelle en production
    // Pour le développement local avec émulateur Android, utilisez 10.0.2.2 au lieu de localhost
    private val baseUrl = "http://10.0.2.2:8080"

    /**
     * Enregistre un nouvel utilisateur
     */
    suspend fun registerUser(user: User): Result<UserResponse> = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = client.post("$baseUrl/users/register") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            val userResponse: UserResponse = response.body()
            Log.d("ApiService", "Register response: $userResponse")

            if (response.status.isSuccess()) {
                Result.success(userResponse)
            } else {
                Result.failure(Exception(userResponse.message))
            }
        } catch (e: Exception) {
            Log.e("ApiService", "Register error", e)
            Result.failure(e)
        }
    }

    /**
     * Connecte un utilisateur existant
     */
    suspend fun loginUser(pseudo: String, password: String): Result<UserResponse> = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = client.post("$baseUrl/users/login") {
                contentType(ContentType.Application.Json)
                setBody(User(pseudo = pseudo, password = password, city = ""))
            }

            val userResponse: UserResponse = response.body()
            Log.d("ApiService", "Login response: $userResponse")

            if (response.status.isSuccess()) {
                Result.success(userResponse)
            } else {
                Result.failure(Exception(userResponse.message))
            }
        } catch (e: Exception) {
            Log.e("ApiService", "Login error", e)
            Result.failure(e)
        }
    }

    /**
     * Récupère les informations météo en fonction de la ville
     * Note: Cette méthode sera implémentée ultérieurement avec une API météo externe
     */
    suspend fun getWeatherInfo(city: String): Result<String> = withContext(Dispatchers.IO) {
        // Simulation d'une réponse météo - à remplacer par une véritable API météo plus tard
        try {
            // Simuler un délai de réseau
            kotlinx.coroutines.delay(1000)

            // Pour l'instant, retourne un message statique
            val weatherInfo = "Il fait beau aujourd'hui à $city ! Vous pouvez sortir votre moto en toute sécurité."
            Result.success(weatherInfo)
        } catch (e: Exception) {
            Log.e("ApiService", "Weather error", e)
            Result.failure(e)
        }
    }
}