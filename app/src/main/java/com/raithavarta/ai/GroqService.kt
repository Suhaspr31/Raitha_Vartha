package com.raithavarta.ai

import android.graphics.Bitmap
import android.util.Base64
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class GroqService(private val apiKey: String) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val maxSize = 512
        val width = bitmap.width
        val height = bitmap.height
        if (width <= maxSize && height <= maxSize) return bitmap

        val aspectRatio = width.toFloat() / height.toFloat()
        val newWidth: Int
        val newHeight: Int
        if (width > height) {
            newWidth = maxSize
            newHeight = (maxSize / aspectRatio).toInt()
        } else {
            newHeight = maxSize
            newWidth = (maxSize * aspectRatio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun getBase64Image(bitmap: Bitmap): String {
        val resized = resizeBitmap(bitmap)
        val stream = ByteArrayOutputStream()
        resized.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    suspend fun analyzePlantDisease(bitmap: Bitmap): Pair<String, String> {
        return withContext(Dispatchers.IO) {
            try {
                val base64Image = getBase64Image(bitmap)
                
                val prompt = "Identify plant and disease. Respond ONLY in JSON format: { \"plant\": \"\", \"disease\": \"\" }. If no plant detected, respond with { \"plant\": \"none\", \"disease\": \"none\" }"

                val requestBody = mapOf(
                    "model" to "meta-llama/llama-4-scout-17b-16e-instruct",
                    "messages" to listOf(
                        mapOf(
                            "role" to "user", 
                            "content" to listOf(
                                mapOf(
                                    "type" to "text",
                                    "text" to prompt
                                ),
                                mapOf(
                                    "type" to "image_url",
                                    "image_url" to mapOf(
                                        "url" to "data:image/jpeg;base64,$base64Image"
                                    )
                                )
                            )
                        )
                    )
                )

                val jsonBody = gson.toJson(requestBody)
                val request = Request.Builder()
                    .url("https://api.groq.com/openai/v1/chat/completions")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .post(jsonBody.toRequestBody(mediaType))
                    .build()

                val response = client.newCall(request).execute()
                val body = response.body?.string()
                if (response.isSuccessful && body != null) {
                    val map = gson.fromJson(body, Map::class.java)
                    val choices = map["choices"] as List<*>
                    val firstChoice = choices[0] as Map<*, *>
                    val message = firstChoice["message"] as Map<*, *>
                    val content = message["content"] as String
                    
                    try {
                        val startIndex = content.indexOf("{")
                        val endIndex = content.lastIndexOf("}")
                        if (startIndex == -1 || endIndex == -1) {
                            return@withContext Pair("error", "No JSON found in response: $content")
                        }
                        val jsonObject = JSONObject(content.substring(startIndex, endIndex + 1))
                        val plant = jsonObject.getString("plant")
                        val disease = jsonObject.getString("disease")
                        Pair(plant, disease)
                    } catch (e: Exception) {
                        Pair("error", "JSON Parsing error: ${e.message} - Content: $content")
                    }
                } else {
                    Pair("error", "API Error: ${response.code} - ${body ?: "null"}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Pair("error", "Exception: ${e.message}")
            }
        }
    }

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

                val requestBody = mapOf(
                    "model" to "llama-3.3-70b-versatile",
                    "messages" to listOf(
                        mapOf(
                            "role" to "user",
                            "content" to prompt
                        )
                    )
                )

                val jsonBody = gson.toJson(requestBody)
                val request = Request.Builder()
                    .url("https://api.groq.com/openai/v1/chat/completions")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("User-Agent", "Mozilla/5.0")
                    .post(jsonBody.toRequestBody(mediaType))
                    .build()

                val response = client.newCall(request).execute()
                val body = response.body?.string()
                if (response.isSuccessful && body != null) {
                    val map = gson.fromJson(body, Map::class.java)
                    val choices = map["choices"] as List<*>
                    if (choices.isNotEmpty()) {
                        val firstChoice = choices[0] as Map<*, *>
                        val message = firstChoice["message"] as Map<*, *>
                        message["content"] as? String ?: "ರೋಗದ ವಿವರ ಲಭ್ಯವಿಲ್ಲ. ನಂತರ ಪ್ರಯತ್ನಿಸಿ."
                    } else {
                        "ರೋಗದ ವಿವರ ಲಭ್ಯವಿಲ್ಲ. ನಂತರ ಪ್ರಯತ್ನಿಸಿ."
                    }
                } else {
                    "ಸಂಪರ್ಕ ದೋಷ: ವಿವರಗಳನ್ನು ಪಡೆಯಲು ಸಾಧ್ಯವಾಗಲಿಲ್ಲ. (API Error)"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "ಸಂಪರ್ಕ ದೋಷ: ವಿವರಗಳನ್ನು ಪಡೆಯಲು ಸಾಧ್ಯವಾಗಲಿಲ್ಲ. (Connection error.)"
            }
        }
    }
}
