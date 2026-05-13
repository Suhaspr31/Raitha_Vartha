package com.raithavarta.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raithavarta.viewmodel.VendorDashboardViewModel

@Composable
fun VendorDashboardScreen(viewModel: VendorDashboardViewModel) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
    
    val uploadState by viewModel.uploadState.collectAsState()

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedPdfUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Vendor Dashboard",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tip Title (e.g., Tomato Blight Advice)") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { pdfPickerLauncher.launch("application/pdf") },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(if (selectedPdfUri != null) "PDF Selected" else "Select Advisory PDF")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { 
                selectedPdfUri?.let { uri ->
                    viewModel.processAndUploadPdf(context, uri, title)
                }
            },
            enabled = selectedPdfUri != null && title.isNotBlank() && uploadState !is VendorDashboardViewModel.UploadState.Processing,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Generate Tip & Upload")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (uploadState) {
            is VendorDashboardViewModel.UploadState.Processing -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = (uploadState as VendorDashboardViewModel.UploadState.Processing).message,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            is VendorDashboardViewModel.UploadState.Success -> {
                Text(
                    text = (uploadState as VendorDashboardViewModel.UploadState.Success).message,
                    color = MaterialTheme.colorScheme.primary
                )
                LaunchedEffect(Unit) {
                    title = ""
                    selectedPdfUri = null
                    // Delaying state reset could be added here
                }
            }
            is VendorDashboardViewModel.UploadState.Error -> {
                Text(
                    text = (uploadState as VendorDashboardViewModel.UploadState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {}
        }
    }
}
