package com.raithavarta.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.interaction.MutableInteractionSource

data class CropCategory(val id: String, val nameKannada: String, val imageUrl: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SasyaLibraryScreen(
    onCategorySelected: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    // Dynamic Data for Categories synchronized with Firestore tips
    val allCategories = listOf(
        CropCategory("Wheat", "ಗೋದೂಮಿ (Wheat)", "https://images.unsplash.com/photo-1501004318641-b39e6451bec6"),
        CropCategory("Maize", "ಮೆಕ್ಕೆಜೋಳ (Maize)", "https://images.unsplash.com/photo-1464226184884-fa280b87c399"),
        CropCategory("Tomato", "ಟೊಮೆಟೊ (Tomato)", "https://images.unsplash.com/photo-1592924357228-91a4daadcfea"),
        CropCategory("Potato", "ಆಲೂಗಡ್ಡೆ (Potato)", "https://images.unsplash.com/photo-1589927986089-35812388d1f4"),
        CropCategory("Onion", "ಈರುಳ್ಳಿ (Onion)", "https://images.unsplash.com/photo-1500382017468-9049fed747ef"),
        CropCategory("Chilli", "ಮೆಣಸಿನಕಾಯಿ (Chilli)", "https://images.unsplash.com/photo-1502082553048-f009c37129b9"),
        CropCategory("Cotton", "ಹತ್ತಿ (Cotton)", "https://images.unsplash.com/photo-1501004318641-b39e6451bec6"),
        CropCategory("Sugarcane", "ಕಬ್ಬು (Sugarcane)", "https://images.unsplash.com/photo-1622312658908-01d011bb2d6f"),
        CropCategory("Rice", "ಭತ್ತ (Rice)", "https://images.unsplash.com/photo-1590682680695-43b964a3ae17")
    )
    
    val filteredCategories = if (searchQuery.isBlank()) {
        allCategories
    } else {
        allCategories.filter {
            it.nameKannada.contains(searchQuery, ignoreCase = true) ||
            it.id.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Search Bar with Voice Input
        TopAppBar(
            title = {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("ಹುಡುಕಿ (Search)") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                    trailingIcon = { 
                        IconButton(onClick = { /* Handle Voice Search */ }) {
                            Icon(Icons.Filled.Mic, contentDescription = "Voice Search")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true
                )
            },
            actions = {
                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Profile",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (filteredCategories.isEmpty()) {
                item {
                    Text("ಯಾವುದೇ ಬೆಳೆಗಳು ಕಂಡುಬಂದಿಲ್ಲ (No crops found)", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onBackground)
                }
            } else {
                items(filteredCategories) { category ->
                    CategoryCard(category = category, onClick = { onCategorySelected(category.id) })
                }
            }
        }
    }
}

@Composable
fun CategoryCard(category: CropCategory, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f)

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.foundation.LocalIndication.current
            ) { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.nameKannada,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            // High contrast overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 100f
                        )
                    )
            )

            Text(
                text = category.nameKannada,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }
}
