import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val weather: List<WeatherDescription>
)

@Serializable
data class WeatherDescription(
    val main: String,
    val description: String
)
