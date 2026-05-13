package com.raithavarta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raithavarta.data.local.entity.FlashCard
import com.raithavarta.repository.FlashCardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.raithavarta.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalCoroutinesApi::class)
class FarmerDashboardViewModel(
    private val repository: FlashCardRepository
) : ViewModel() {

    private val _cropFilter = MutableStateFlow<String?>(null)

    val flashCards: StateFlow<List<FlashCard>> = _cropFilter
        .flatMapLatest { cropId ->
            if (cropId == null) {
                repository.flashCards.map { list ->
                    val daySeed = System.currentTimeMillis() / (1000 * 60 * 60 * 24)
                    list.shuffled(java.util.Random(daySeed))
                }
            } else {
                repository.getFlashCardsByCrop(cropId)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // New Weather State
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    init {
        refreshCards()
        fetchWeatherAndGenerateTip()
    }

    fun refreshCards() {
        viewModelScope.launch {
            repository.refreshFlashCards()
        }
    }

    fun setCropFilter(cropId: String?) {
        _cropFilter.value = cropId
    }

    private fun fetchWeatherAndGenerateTip() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://wttr.in/?format=j1")
                    .build()
                
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                
                if (response.isSuccessful && responseBody != null) {
                    val jsonObject = JsonParser.parseString(responseBody).asJsonObject
                    val currentCondition = jsonObject.getAsJsonArray("current_condition").get(0).asJsonObject
                    val temp = currentCondition.get("temp_C").asString
                    val desc = currentCondition.getAsJsonArray("weatherDesc").get(0).asJsonObject.get("value").asString
                    
                    generateTip(temp, desc)
                } else {
                    _weatherState.value = WeatherState.Error("Failed to fetch weather")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _weatherState.value = WeatherState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun generateTip(temp: String, condition: String) {
        try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = BuildConfig.GEMINI_API_KEY
            )
            val prompt = "Given the current weather is $condition and temperature is $temp°C, provide a short, actionable farming tip in Kannada and English (separated by a newline). Keep it brief, suitable for a dashboard card."
            val response = generativeModel.generateContent(prompt)
            val tip = response.text?.trim() ?: "ಸಲಹೆ ಲಭ್ಯವಿಲ್ಲ. / Tip not available."
            
            _weatherState.value = WeatherState.Success(temp, condition, tip)
        } catch (e: Exception) {
            e.printStackTrace()
            _weatherState.value = WeatherState.Success(temp, condition, "ಮಣ್ಣಿನ ತೇವಾಂಶ ಕಾಪಾಡಿಕೊಳ್ಳಿ. / Maintain soil moisture.") // Fallback tip
        }
    }
}

sealed class WeatherState {
    object Loading : WeatherState()
    data class Success(val temperature: String, val condition: String, val tip: String) : WeatherState()
    data class Error(val message: String) : WeatherState()
}
