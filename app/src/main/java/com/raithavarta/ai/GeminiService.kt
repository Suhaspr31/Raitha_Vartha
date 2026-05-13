package com.raithavarta.ai

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig

class GeminiService(apiKey: String) {
    private val safetySettings = listOf(
        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH)
    )

    private val config = generationConfig {
        temperature = 0.3f
        topK = 32
        topP = 1f
        maxOutputTokens = 768
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = apiKey,
        safetySettings = safetySettings,
        generationConfig = config
    )

    suspend fun getDiseaseExplanation(plant: String, disease: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val prompt = """
                    Explain this plant disease in Kannada:
                    Plant: $plant
                    Disease: $disease
                    Give the cause and 2 clear, actionable solutions. 
                    Keep it professional and helpful for a farmer. 
                    Limit the total response to 4 sentences.
                """.trimIndent()
                
                val response = generativeModel.generateContent(prompt)
                response.text ?: "ರೋಗದ ವಿವರ ಲಭ್ಯವಿಲ್ಲ. ನಂತರ ಪ್ರಯತ್ನಿಸಿ. (Explanation unavailable.)"
            } catch (e: Exception) {
                e.printStackTrace()
                "ಸಂಪರ್ಕ ದೋಷ: ವಿವರಗಳನ್ನು ಪಡೆಯಲು ಸಾಧ್ಯವಾಗಲಿಲ್ಲ. (Connection error.)"
            }
        }
    }

    suspend fun summarizeToKannada(text: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val prompt = "Translate and summarize the following text into clear, simple Kannada suitable for farmers:\n$text"
                val response = generativeModel.generateContent(prompt)
                response.text ?: "ಅನುವಾದ ಲಭ್ಯವಿಲ್ಲ. (Translation unavailable.)"
            } catch (e: Exception) {
                e.printStackTrace()
                "ಅನುವಾದ ವಿಫಲವಾಗಿದೆ. (Translation failed.)"
            }
        }
    }
}
