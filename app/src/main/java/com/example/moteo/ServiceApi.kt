package com.example.moteo

import android.util.Log
import com.example.moteo.User
import com.example.moteo.LoginRequest
import com.example.moteo.UserResponse
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

class ApiService {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        engine {
            connectTimeout = 60_000
            socketTimeout = 60_000
        }
    }

    private val baseUrl = "http://10.0.2.2:8080"

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

    suspend fun loginUser(pseudo: String, password: String): Result<UserResponse> = withContext(Dispatchers.IO) {
        try {
            val userLogin = LoginRequest(pseudo, password)
            val response: HttpResponse = client.post("$baseUrl/users/login") {
                contentType(ContentType.Application.Json)
                setBody(userLogin)
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

    suspend fun updateUser(user: User): Result<UserResponse> = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = client.put("$baseUrl/users/update") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            val userResponse: UserResponse = response.body()
            Log.d("ApiService", "Update response: $userResponse")

            if (response.status.isSuccess()) {
                Result.success(userResponse)
            } else {
                Result.failure(Exception(userResponse.message))
            }
        } catch (e: Exception) {
            Log.e("ApiService", "Update error", e)
            Result.failure(e)
        }
    }

    suspend fun getWeatherInfo(city: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            kotlinx.coroutines.delay(1000)
            val weatherInfo = "Il fait beau aujourd'hui à $city ! Vous pouvez sortir votre moto en toute sécurité."
            Result.success(weatherInfo)
        } catch (e: Exception) {
            Log.e("ApiService", "Weather error", e)
            Result.failure(e)
        }
    }

    // --- Ajout pour météo avec OpenWeatherMap ---

    @kotlinx.serialization.Serializable
    data class WeatherResponse(
        val weather: List<WeatherDescription>
    )

    @kotlinx.serialization.Serializable
    data class WeatherDescription(
        val main: String,
        val description: String
    )

    suspend fun getWeather(city: String): Result<WeatherResponse> = withContext(Dispatchers.IO) {
        val apiKey = "619e23a1838ec55b4f222b07b358b2a9"
        val url = "https://api.openweathermap.org/data/2.5/weather"

        try {
            val response: HttpResponse = client.get(url) {
                parameter("q", city)
                parameter("appid", apiKey)
                parameter("lang", "fr")
                parameter("units", "metric")
            }

            val weatherResponse: WeatherResponse = response.body()
            Log.d("ApiService", "Weather API response: $weatherResponse")

            Result.success(weatherResponse)
        } catch (e: Exception) {
            Log.e("ApiService", "Weather API error", e)
            Result.failure(e)
        }
    }

    // --- Nouvelle méthode pour récupérer le profil utilisateur ---

    @kotlinx.serialization.Serializable
    data class UserProfileResponse(
        val pseudo: String,
        val city: String? = null
    )

    suspend fun getUserProfile(pseudo: String): Result<UserProfileResponse> = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = client.get("$baseUrl/users/profile") {
                parameter("pseudo", pseudo)
            }

            val userProfile: UserProfileResponse = response.body()
            Log.d("ApiService", "UserProfile response: $userProfile")

            if (response.status.isSuccess()) {
                Result.success(userProfile)
            } else {
                Result.failure(Exception("Erreur récupération profil"))
            }
        } catch (e: Exception) {
            Log.e("ApiService", "UserProfile error", e)
            Result.failure(e)
        }
    }
}
