package com.raithavarta

import kotlinx.coroutines.runBlocking
import org.junit.Test
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

class GeminiKeyTest {
    @Test
    fun testApiKey() = runBlocking {
        val apiKey = "AIzaSyCTiVKCiHPRxpe2cVOrULRrxGTHVLi5iPw"
        val model = GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = apiKey
        )

        try {
            println("Sending request to Gemini...")
            val response = model.generateContent(
                content {
                    text("Hello, are you working? Reply with 'YES'.")
                }
            )
            println("✅ Success! Gemini says: ${response.text}")
        } catch (e: Exception) {
            println("❌ Error: ${e.localizedMessage}")
            e.printStackTrace()
        }
    }
}
