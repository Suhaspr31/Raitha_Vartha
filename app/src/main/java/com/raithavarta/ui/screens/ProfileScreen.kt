package com.raithavarta.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.raithavarta.data.local.entity.CropAnalysisRecord
import com.raithavarta.data.local.entity.FlashCard
import com.raithavarta.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser
    val history by viewModel.history.collectAsState()
    val savedTips by viewModel.savedTips.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    
    // Refresh data when screen opens
    LaunchedEffect(Unit) {
        viewModel.refreshSavedTips()
        viewModel.fetchFirestoreProfile()
    }
    
    var selectedTab by remember { mutableStateOf(0) }
    var selectedHistoryRecord by remember { mutableStateOf<CropAnalysisRecord?>(null) }
    var selectedTip by remember { mutableStateOf<FlashCard?>(null) }

    // Dialog for History Detail
    selectedHistoryRecord?.let { record ->
        AlertDialog(
            onDismissRequest = { selectedHistoryRecord = null },
            confirmButton = {
                TextButton(onClick = { selectedHistoryRecord = null }) {
                    Text("Close (ಮುಚ್ಚು)")
                }
            },
            title = { Text("ವಿಶ್ಲೇಷಣೆ ವಿವರ (Scan Detail)", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("ಸಸ್ಯ (Plant): ${record.plant}", fontWeight = FontWeight.SemiBold)
                    Text("ರೋಗ (Disease): ${record.disease}", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = record.remedyKannada, fontSize = 16.sp, lineHeight = 24.sp)
                }
            }
        )
    }

    // Dialog for Saved Tip Detail
    selectedTip?.let { tip ->
        AlertDialog(
            onDismissRequest = { selectedTip = null },
            confirmButton = {
                TextButton(onClick = { selectedTip = null }) {
                    Text("Close (ಮುಚ್ಚು)")
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = tip.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = tip.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = tip.descriptionKannada, fontSize = 16.sp, lineHeight = 24.sp)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ಖಾತೆ ವಿವರ (Account Settings)", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { 
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.Red)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // User Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(45.dp)
                    )
                }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = userName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Text(
                        text = user?.email ?: "No Email",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val displayRole = when(userRole) {
                        "CUSTOMER" -> "Farmer (ರೈತ)"
                        "VENDOR" -> "Expert/Vendor (ವ್ಯಾಪಾರಿ)"
                        "SELLER" -> "Seller (ಮಾರಾಟಗಾರ)"
                        else -> userRole
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(20.dp),
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Verified $displayRole",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                    }
                }
            }

            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("ಇತಿಹಾಸ (History)") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("ಉಳಿಸಿದವು (Saved)") }
                )
            }

            // Content
            if (selectedTab == 0) {
                HistoryList(history) { selectedHistoryRecord = it }
            } else {
                SavedTipsList(savedTips) { selectedTip = it }
            }
        }
    }
}

@Composable
fun HistoryList(history: List<CropAnalysisRecord>, onItemClick: (CropAnalysisRecord) -> Unit) {
    if (history.isEmpty()) {
        EmptyState("ಯಾವುದೇ ವಿಶ್ಲೇಷಣೆ ಕಂಡುಬಂದಿಲ್ಲ", Icons.Default.History)
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(history) { record ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onItemClick(record) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Eco, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "ರೋಗ: ${record.disease}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = record.remedyKannada, fontSize = 14.sp, color = Color.Gray, maxLines = 2)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(record.timestamp)),
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedTipsList(tips: List<FlashCard>, onItemClick: (FlashCard) -> Unit) {
    if (tips.isEmpty()) {
        EmptyState("ಉಳಿಸಿದ ಯಾವುದೇ ಮಾಹಿತಿ ಇಲ್ಲ", Icons.Default.BookmarkBorder)
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(tips) { tip ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onItemClick(tip) },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = tip.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = tip.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = tip.descriptionKannada, fontSize = 12.sp, maxLines = 1, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = text, color = Color.Gray)
    }
}
