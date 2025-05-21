import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val weather: List<WeatherDescription>
)

@Serializable
data class WeatherDescription(
    val main: String,       // Exemple : "Rain", "Snow", "Clear", "Drizzle", "Thunderstorm"
    val description: String // Exemple : "light rain", "heavy snow"
)
