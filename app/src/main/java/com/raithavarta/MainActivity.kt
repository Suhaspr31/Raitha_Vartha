package com.raithavarta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.raithavarta.data.local.AppDatabase
import com.raithavarta.repository.FlashCardRepository
import com.raithavarta.ui.navigation.AppNavigation
import com.raithavarta.ui.theme.RaithaVartaTheme
import com.raithavarta.ai.GeminiService
import com.raithavarta.ai.GroqService
import com.raithavarta.viewmodel.CameraViewModel
import com.raithavarta.viewmodel.ProfileViewModel
import com.raithavarta.viewmodel.ProfileViewModelFactory
import com.raithavarta.viewmodel.CameraViewModelFactory
import com.raithavarta.viewmodel.FarmerDashboardViewModel
import com.raithavarta.viewmodel.FarmerDashboardViewModelFactory
import com.raithavarta.viewmodel.SpoorthiViewModel

import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Initialize dependencies (Normally done via DI like Hilt/Dagger)
        val database = AppDatabase.getDatabase(applicationContext)
        val flashCardRepository = FlashCardRepository(database.flashCardDao())
        val groqService = GroqService(BuildConfig.GROQ_API_KEY)
        val geminiService = GeminiService(BuildConfig.GEMINI_API_KEY)

        setContent {
            RaithaVartaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val farmerDashboardViewModel: FarmerDashboardViewModel = viewModel(
                        factory = FarmerDashboardViewModelFactory(flashCardRepository)
                    )
                    val spoorthiViewModel: SpoorthiViewModel = viewModel()
                    val cameraViewModel: CameraViewModel = viewModel(
                        factory = CameraViewModelFactory(groqService, geminiService, database.cropAnalysisDao())
                    )
                    val profileViewModel: ProfileViewModel = viewModel(
                        factory = ProfileViewModelFactory(database.cropAnalysisDao(), database.flashCardDao(), applicationContext)
                    )
 
                    AppNavigation(
                        farmerDashboardViewModel = farmerDashboardViewModel,
                        spoorthiViewModel = spoorthiViewModel,
                        cameraViewModel = cameraViewModel,
                        profileViewModel = profileViewModel
                    )

                    // Temporary: Upload bulk tips & success stories to Firestore
                    com.raithavarta.data.remote.FirestoreUtils.uploadInitialTips()
                    com.raithavarta.data.remote.FirestoreUtils.uploadSuccessStories()
                }
            }
        }
    }
}
