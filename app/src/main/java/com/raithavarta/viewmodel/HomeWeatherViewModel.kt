package com.raithavarta.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Locale

// ─── Data Classes ─────────────────────────────────────────────────────────────
data class WeatherData(
    val locationName: String,
    val temperatureC: String,
    val feelsLikeC: String,
    val humidity: String,
    val windKmph: String,
    val condition: String,
    val conditionEmoji: String,
    val uvIndex: String,
    val visibility: String,
    val sunrise: String,
    val sunset: String
)

sealed class HomeWeatherState {
    object Loading : HomeWeatherState()
    data class Success(val data: WeatherData) : HomeWeatherState()
    data class Error(val message: String) : HomeWeatherState()
    object PermissionRequired : HomeWeatherState()
}

// ─── ViewModel ────────────────────────────────────────────────────────────────
class HomeWeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<HomeWeatherState>(HomeWeatherState.Loading)
    val state: StateFlow<HomeWeatherState> = _state.asStateFlow()

    @SuppressLint("MissingPermission")
    fun fetchWeather(hasPermission: Boolean) {
        if (!hasPermission) {
            _state.value = HomeWeatherState.PermissionRequired
            return
        }

        _state.value = HomeWeatherState.Loading

        val fusedClient = LocationServices.getFusedLocationProviderClient(getApplication<Application>())
        val cts = CancellationTokenSource()

        fusedClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val lat = location.latitude
                        val lon = location.longitude

                        // Reverse geocode to get city name
                        val cityName = try {
                            val geocoder = Geocoder(getApplication(), Locale.getDefault())
                            @Suppress("DEPRECATION")
                            val addresses = geocoder.getFromLocation(lat, lon, 1)
                            addresses?.firstOrNull()?.let { addr ->
                                addr.locality ?: addr.subAdminArea ?: addr.adminArea ?: "Your Location"
                            } ?: "Your Location"
                        } catch (e: Exception) {
                            "Your Location"
                        }

                        fetchWeatherForCoords(lat, lon, cityName)
                    }
                } else {
                    // Fall back to IP-based location
                    viewModelScope.launch(Dispatchers.IO) {
                        fetchWeatherIpBased()
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch(Dispatchers.IO) {
                    fetchWeatherIpBased()
                }
            }
    }

    private fun fetchWeatherForCoords(lat: Double, lon: Double, cityName: String) {
        try {
            val client = OkHttpClient()
            val url = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m&daily=sunrise,sunset,uv_index_max&timezone=auto"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string()

            if (response.isSuccessful && body != null) {
                parseAndEmit(body, cityName)
            } else {
                _state.value = HomeWeatherState.Error("Weather fetch failed")
            }
        } catch (e: Exception) {
            _state.value = HomeWeatherState.Error(e.message ?: "Network error")
        }
    }

    private fun fetchWeatherIpBased() {
        try {
            val client = OkHttpClient()
            val ipReq = Request.Builder().url("http://ip-api.com/json/").build()
            val ipResp = client.newCall(ipReq).execute()
            val ipBody = ipResp.body?.string()
            
            var lat = 12.9716 // Default Bangalore
            var lon = 77.5946
            var city = "Detected Location"
            
            if (ipResp.isSuccessful && ipBody != null) {
                val ipJson = JsonParser.parseString(ipBody).asJsonObject
                if (ipJson.get("status").asString == "success") {
                    lat = ipJson.get("lat").asDouble
                    lon = ipJson.get("lon").asDouble
                    city = ipJson.get("city").asString
                }
            }
            
            val url = "https://api.open-meteo.com/v1/forecast?latitude=$lat&longitude=$lon&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m&daily=sunrise,sunset,uv_index_max&timezone=auto"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string()

            if (response.isSuccessful && body != null) {
                parseAndEmit(body, city)
            } else {
                _state.value = HomeWeatherState.Error("Weather unavailable")
            }
        } catch (e: Exception) {
            _state.value = HomeWeatherState.Error(e.message ?: "Network error")
        }
    }

    private fun parseAndEmit(json: String, cityName: String) {
        try {
            val root = JsonParser.parseString(json).asJsonObject
            val current = root.getAsJsonObject("current")
            val daily = root.getAsJsonObject("daily")

            val tempC = current.get("temperature_2m").asString
            val feelsLike = current.get("apparent_temperature").asString
            val humidity = current.get("relative_humidity_2m").asString
            val wind = current.get("wind_speed_10m").asString
            val weatherCode = current.get("weather_code").asInt
            
            val uvIndex = daily.getAsJsonArray("uv_index_max").get(0).asString
            val sunriseRaw = daily.getAsJsonArray("sunrise").get(0).asString
            val sunsetRaw = daily.getAsJsonArray("sunset").get(0).asString
            
            val sunrise = sunriseRaw.split("T").last()
            val sunset = sunsetRaw.split("T").last()

            val (condition, emoji) = codeToConditionAndEmoji(weatherCode)

            _state.value = HomeWeatherState.Success(
                WeatherData(
                    locationName = cityName,
                    temperatureC = tempC,
                    feelsLikeC = feelsLike,
                    humidity = humidity,
                    windKmph = wind,
                    condition = condition,
                    conditionEmoji = emoji,
                    uvIndex = uvIndex,
                    visibility = "10",
                    sunrise = sunrise,
                    sunset = sunset
                )
            )
        } catch (e: Exception) {
            _state.value = HomeWeatherState.Error("Parse error: ${e.message}")
        }
    }

    private fun codeToConditionAndEmoji(code: Int): Pair<String, String> {
        return when (code) {
            0 -> "Clear sky" to "☀️"
            1, 2, 3 -> "Partly cloudy" to "⛅"
            45, 48 -> "Fog" to "🌫️"
            51, 53, 55 -> "Drizzle" to "🌧️"
            56, 57 -> "Freezing Drizzle" to "🌧️"
            61, 63, 65 -> "Rain" to "🌧️"
            66, 67 -> "Freezing Rain" to "🌧️"
            71, 73, 75 -> "Snow" to "❄️"
            77 -> "Snow grains" to "❄️"
            80, 81, 82 -> "Rain showers" to "🌧️"
            85, 86 -> "Snow showers" to "❄️"
            95 -> "Thunderstorm" to "⛈️"
            96, 99 -> "Thunderstorm with hail" to "⛈️"
            else -> "Sunny" to "🌤️"
        }
    }
}
