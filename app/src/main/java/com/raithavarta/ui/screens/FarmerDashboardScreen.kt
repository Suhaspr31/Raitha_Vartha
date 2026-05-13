package com.raithavarta.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.raithavarta.data.local.entity.FlashCard
import com.raithavarta.ui.theme.SoftEarthyGreen
import com.raithavarta.viewmodel.FarmerDashboardViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FarmerDashboardScreen(
    viewModel: FarmerDashboardViewModel,
    onProfileClick: () -> Unit
) {
    val flashCards by viewModel.flashCards.collectAsState()

    if (flashCards.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().background(SoftEarthyGreen), contentAlignment = Alignment.Center) {
            Text("No tips available / ಮಾಹಿತಿ ಲಭ್ಯವಿಲ್ಲ", color = com.raithavarta.ui.theme.DarkGreen)
        }
    } else {
        val pagerState = rememberPagerState(pageCount = { flashCards.size })

        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize().background(SoftEarthyGreen)
        ) { page ->
            FlashCardItem(flashCard = flashCards[page])
        }

        // Top Overlay for Profile
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding()
        ) {
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color.White.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}



@Composable
fun FlashCardItem(flashCard: FlashCard) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: "guest"
    val sharedPrefs = context.getSharedPreferences("saved_tips_$uid", android.content.Context.MODE_PRIVATE)
    var isSaved by remember { mutableStateOf(sharedPrefs.getBoolean(flashCard.id, false)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftEarthyGreen)
    ) {
        // Image background using Coil
        AsyncImage(
            model = flashCard.imageUrl.ifEmpty { "https://via.placeholder.com/600x800" },
            contentDescription = flashCard.title,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.6f).align(Alignment.TopCenter),
            contentScale = ContentScale.Crop
        )

        // Text content aligned to bottom with Minimalist White Background
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxHeight(0.45f)
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(24.dp)
        ) {
            Text(
                text = "Tip of the Day / ಇಂದಿನ ಸಲಹೆ",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = flashCard.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Text(
                text = flashCard.descriptionKannada,
                fontSize = 18.sp,
                lineHeight = 26.sp,
                color = Color.DarkGray,
                modifier = Modifier.weight(1f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                val context = androidx.compose.ui.platform.LocalContext.current
                val scope = rememberCoroutineScope()
                IconButton(onClick = { 
                    val shareIntent = android.content.Intent().apply {
                        action = android.content.Intent.ACTION_SEND
                        putExtra(android.content.Intent.EXTRA_TEXT, "${flashCard.title}\n\n${flashCard.descriptionKannada}\n\nRead more on Raitha Varta app!")
                        type = "text/plain"
                    }
                    context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Tip"))
                }) {
                    Icon(imageVector = Icons.Filled.Share, contentDescription = "Share to WhatsApp", tint = MaterialTheme.colorScheme.primary)
                }
                IconToggleButton(
                    checked = isSaved,
                    onCheckedChange = { saved ->
                        isSaved = saved 
                        sharedPrefs.edit().putBoolean(flashCard.id, saved).apply()
                        
                        // Save to Firebase
                        if (uid != "guest") {
                            scope.launch {
                                val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                                try {
                                    if (saved) {
                                        db.collection("users").document(uid)
                                            .collection("saved_tips").document(flashCard.id)
                                            .set(mapOf("savedAt" to System.currentTimeMillis()))
                                    } else {
                                        db.collection("users").document(uid)
                                            .collection("saved_tips").document(flashCard.id)
                                            .delete()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = "Save Tip",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
