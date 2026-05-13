package com.raithavarta.ui.screens

import android.Manifest
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.raithavarta.viewmodel.HomeWeatherState
import com.raithavarta.viewmodel.HomeWeatherViewModel
import com.raithavarta.viewmodel.WeatherData
import kotlinx.coroutines.delay

// ─── Entry Point ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val weatherViewModel: HomeWeatherViewModel = viewModel()
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Request permission once, then fetch
    LaunchedEffect(locationPermission.status.isGranted) {
        if (!locationPermission.status.isGranted) {
            locationPermission.launchPermissionRequest()
        } else {
            weatherViewModel.fetchWeather(hasPermission = true)
        }
    }
    // Also trigger fetch once permission is granted
    LaunchedEffect(locationPermission.status) {
        weatherViewModel.fetchWeather(hasPermission = locationPermission.status.isGranted)
    }

    HeroScreen(navController, weatherViewModel)
}

// ─── HeroScreen ──────────────────────────────────────────────────────────────
@Composable
fun HeroScreen(navController: NavController, weatherViewModel: HomeWeatherViewModel) {
    val weatherState by weatherViewModel.state.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Background ExoPlayer – muted, looping
    val bgPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(
                Uri.parse("https://videos.pexels.com/video-files/856976/856976-hd_1920_1080_25fps.mp4")
            ))
            repeatMode    = Player.REPEAT_MODE_ALL
            playWhenReady = true
            volume        = 0f
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            bgPlayer.release()
        }
    }

    // Fade-in background video when ready
    var bgReady by remember { mutableStateOf(false) }
    DisposableEffect(bgPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) bgReady = true
            }
        }
        bgPlayer.addListener(listener)
        onDispose { bgPlayer.removeListener(listener) }
    }
    val videoAlpha by animateFloatAsState(
        targetValue   = if (bgReady) 1f else 0f,
        animationSpec = tween(1200),
        label         = "BgVideoFade"
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Dark fallback background ──────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0A1F0A), Color(0xFF0D2B1A), Color(0xFF071510))
                    )
                )
        )

        // ── Ambient floating particles ────────────────────────────────────
        ParticleCanvas(modifier = Modifier.fillMaxSize())

        // ── Background video (only behind the hero hero viewport height) ──
        VideoBackground(
            exoPlayer = bgPlayer,
            modifier  = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)    // only behind the hero portion
                .alpha(videoAlpha)
        )

        // ── Gradient overlay ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f  to Color.Black.copy(alpha = 0.50f),
                            0.30f to Color.Black.copy(alpha = 0.10f),
                            0.55f to Color.Black.copy(alpha = 0.20f),
                            1.0f  to Color(0xFF071510)
                        )
                    )
                )
        )

        // ── Scrollable content on top ─────────────────────────────────────
        Column(
            modifier              = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {

            // TOP BAR
            TopStatusBar()

            // HERO TEXT + STATS
            HeroContent(navController = navController)

            Spacer(Modifier.height(24.dp))

            // FEATURE PILLS – properly wrapped
            FeaturePillsRow()

            Spacer(Modifier.height(16.dp))

            // SCROLL HINT ARROW
            ArrowCanvas(modifier = Modifier.size(48.dp))

            Spacer(Modifier.height(20.dp))



            // ── WEATHER SECTION DIVIDER ────────────────────────────────────
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HorizontalDivider(
                    modifier  = Modifier.weight(1f),
                    color     = Color(0xFF4ADE80).copy(alpha = 0.25f),
                    thickness = 1.dp
                )
                Text(
                    text       = "🌦️ Live Weather",
                    color      = Color(0xFF4ADE80),
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(
                    modifier  = Modifier.weight(1f),
                    color     = Color(0xFF4ADE80).copy(alpha = 0.25f),
                    thickness = 1.dp
                )
            }

            Spacer(Modifier.height(16.dp))

            // ── WEATHER CARD ───────────────────────────────────────────────
            WeatherSection(
                state      = weatherState,
                onRefresh  = { weatherViewModel.fetchWeather(hasPermission = true) },
                modifier   = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(48.dp))
        }
    }
}

// ─── VideoBackground (muted, behind hero) ────────────────────────────────────
@Composable
fun VideoBackground(exoPlayer: ExoPlayer, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player        = exoPlayer
                useController = false
                resizeMode    = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
        },
        update   = { it.player = exoPlayer },
        modifier = modifier
    )
}


// ─── Top Status Bar (no LIVE badge) ──────────────────────────────────────────
@Composable
fun TopStatusBar() {
    var alpha by remember { mutableStateOf(0f) }
    LaunchedEffect(Unit) { delay(400); alpha = 1f }
    val animatedAlpha by animateFloatAsState(
        targetValue   = alpha,
        animationSpec = tween(1000),
        label         = "TopBarAlpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .alpha(animatedAlpha),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        // Logo pill – centred
        Row(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(50))
                .border(1.dp, Color(0xFF4ADE80).copy(alpha = 0.4f), RoundedCornerShape(50))
                .padding(horizontal = 18.dp, vertical = 8.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector        = Icons.Default.Eco,
                contentDescription = null,
                tint               = Color(0xFF4ADE80),
                modifier           = Modifier.size(16.dp)
            )
            Text(
                text       = "ರೈಥ ವರ್ತ",
                color      = Color.White,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ─── HeroContent ─────────────────────────────────────────────────────────────
@Composable
fun HeroContent(navController: NavController) {

    var showWord1 by remember { mutableStateOf(false) }
    var showWord2 by remember { mutableStateOf(false) }
    var showWord3 by remember { mutableStateOf(false) }
    var showSub   by remember { mutableStateOf(false) }
    var showBtn   by remember { mutableStateOf(false) }
    var showStats by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300);  showWord1 = true
        delay(180);  showWord2 = true
        delay(180);  showWord3 = true
        delay(250);  showSub   = true
        delay(300);  showBtn   = true
        delay(300);  showStats = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier            = Modifier.padding(horizontal = 24.dp)
    ) {




        // Title – word by word
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedVisibility(visible = showWord1, enter = fadeIn(tween(500)) + slideInVertically { 40 }) {
                Text("ಬೆಳೆಯಿರಿ", fontSize = 54.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 56.sp)
            }
            AnimatedVisibility(visible = showWord2, enter = fadeIn(tween(500)) + slideInVertically { 40 }) {
                Text("Smarter,", fontSize = 50.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4ADE80), lineHeight = 52.sp, fontStyle = FontStyle.Italic)
            }
            AnimatedVisibility(visible = showWord3, enter = fadeIn(tween(500)) + slideInVertically { 40 }) {
                Text("Earn More 🌾", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 44.sp)
            }
        }

        Spacer(Modifier.height(18.dp))

        // Subtitle
        AnimatedVisibility(visible = showSub, enter = fadeIn(tween(700)) + slideInVertically { 30 }) {
            Text(
                text       = "AI-powered daily tips in Kannada.\nBetter crops. Better income. Every day.",
                fontSize   = 15.sp,
                color      = Color.White.copy(0.75f),
                textAlign  = TextAlign.Center,
                lineHeight = 22.sp
            )
        }

        Spacer(Modifier.height(28.dp))

        // CTA Buttons
        AnimatedVisibility(visible = showBtn, enter = fadeIn(tween(700)) + slideInVertically { 30 }) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Button(
                    onClick        = { navController.navigate("tips") },
                    shape          = RoundedCornerShape(50),
                    colors         = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                    contentPadding = PaddingValues(horizontal = 28.dp, vertical = 14.dp),
                    elevation      = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Text("ಸಲಹೆ ನೋಡಿ →", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                OutlinedButton(
                    onClick        = { navController.navigate("tips") },
                    shape          = RoundedCornerShape(50),
                    border         = BorderStroke(1.5.dp, Color(0xFF4ADE80).copy(0.7f)),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text("Explore", color = Color(0xFF4ADE80), fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // Stats Row
        AnimatedVisibility(visible = showStats, enter = fadeIn(tween(700)) + slideInVertically { 30 }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(0.07f), RoundedCornerShape(20.dp))
                    .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(20.dp))
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                StatItem(Icons.Default.Eco,       "50K+", "Farmers")
                StatDivider()
                StatItem(Icons.Default.LightMode, "365",  "Tips/Year")
                StatDivider()
                StatItem(Icons.Default.WaterDrop, "12+",  "Crops")
            }
        }
    }
}

// ─── StatItem ────────────────────────────────────────────────────────────────
@Composable
fun StatItem(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = Color(0xFF4ADE80), modifier = Modifier.size(18.dp))
        Spacer(Modifier.height(4.dp))
        Text(value, color = Color.White,              fontWeight = FontWeight.Bold,   fontSize = 16.sp)
        Text(label, color = Color.White.copy(0.55f),  fontSize   = 11.sp)
    }
}

@Composable
fun StatDivider() {
    Box(Modifier.width(1.dp).height(40.dp).background(Color.White.copy(0.15f)))
}

// ─── Feature Pills Row ────────────────────────────────────────────────────────
// 3 pills: arranged in a centred wrap (2 + 1 below to avoid overflow)
@Composable
fun FeaturePillsRow() {
    // "💰 Mandi Rates" removed per user request
    val pills = listOf("🌱 Crop Tips", "🌦️ Weather", "🐛 Pest Alert")

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(1400); visible = true }

    AnimatedVisibility(
        visible = visible,
        enter   = fadeIn(tween(600)) + slideInVertically { 20 }
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // First row – all 3 pills centred
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                pills.forEachIndexed { idx, text ->
                    if (idx > 0) Spacer(Modifier.width(10.dp))
                    PillChip(text)
                }
            }
        }
    }
}

@Composable
fun PillChip(text: String) {
    Text(
        text       = text,
        color      = Color(0xFFD1FAE5),
        fontSize   = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier   = Modifier
            .background(Color(0xFF4ADE80).copy(0.12f), RoundedCornerShape(50))
            .border(1.dp, Color(0xFF4ADE80).copy(0.3f), RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    )
}

// ─── Particle Canvas ─────────────────────────────────────────────────────────
@Composable
fun ParticleCanvas(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "Particles")
    val progress by transition.animateFloat(
        initialValue  = 0f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
        label         = "ParticleProgress"
    )

    val particles = remember {
        listOf(
            Triple(0.1f, 0.2f, 3f), Triple(0.8f, 0.15f, 2f), Triple(0.5f, 0.3f, 4f),
            Triple(0.25f, 0.7f, 2.5f), Triple(0.7f, 0.6f, 3.5f), Triple(0.4f, 0.85f, 2f),
            Triple(0.9f, 0.45f, 3f), Triple(0.15f, 0.5f, 2f), Triple(0.6f, 0.1f, 4f),
            Triple(0.35f, 0.4f, 2.5f)
        )
    }

    Canvas(modifier = modifier) {
        particles.forEachIndexed { i, (xRatio, yRatio, radius) ->
            val phase   = (progress + i * 0.1f) % 1f
            val yOff    = (phase * 60f) - 30f
            val a       = if (phase < 0.5f) phase * 2f else (1f - phase) * 2f
            drawCircle(
                color  = Color(0xFF4ADE80).copy(alpha = a * 0.4f),
                radius = radius.dp.toPx(),
                center = Offset(size.width * xRatio, size.height * yRatio + yOff)
            )
        }
    }
}

// ─── Arrow Canvas ─────────────────────────────────────────────────────────────
@Composable
fun ArrowCanvas(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "Arrow")
    val yOffset by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 10f,
        animationSpec = infiniteRepeatable(tween(800, easing = EaseInOut), RepeatMode.Reverse),
        label         = "ArrowBounce"
    )
    val arrowAlpha by infiniteTransition.animateFloat(
        initialValue  = 0.4f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(tween(800, easing = EaseInOut), RepeatMode.Reverse),
        label         = "ArrowAlpha"
    )

    Canvas(modifier = modifier) {
        val w  = size.width
        val h  = size.height
        val cx = w / 2f
        val base = h * 0.55f + yOffset

        val path = Path().apply {
            moveTo(cx - 14f, base - 16f)
            lineTo(cx,        base)
            lineTo(cx + 14f, base - 16f)
            moveTo(cx - 10f, base - 32f)
            lineTo(cx,        base - 16f)
            lineTo(cx + 10f, base - 32f)
        }

        drawPath(
            path  = path,
            color = Color(0xFF4ADE80).copy(alpha = arrowAlpha),
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

// ─── PreviewCard (stub – unused) ─────────────────────────────────────────────
@Composable
fun PreviewCard(modifier: Modifier = Modifier) {
    Box(modifier = modifier.clip(RoundedCornerShape(24.dp)).background(Color(0xFF1A2F1A)))
}

// ─── WeatherSection ───────────────────────────────────────────────────────────
@Composable
fun WeatherSection(
    state: HomeWeatherState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when (state) {
            is HomeWeatherState.Loading -> WeatherLoadingCard()

            is HomeWeatherState.PermissionRequired -> WeatherPermissionCard(onRefresh)

            is HomeWeatherState.Error -> WeatherErrorCard(state.message, onRefresh)

            is HomeWeatherState.Success -> WeatherSuccessCard(state.data, onRefresh)
        }
    }
}

// ── Loading ───────────────────────────────────────────────────────────────────
@Composable
fun WeatherLoadingCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(0.07f))
            .border(1.dp, Color(0xFF4ADE80).copy(0.25f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color(0xFF4ADE80), modifier = Modifier.size(32.dp), strokeWidth = 3.dp)
            Spacer(Modifier.height(12.dp))
            Text("Fetching your weather...", color = Color.White.copy(0.6f), fontSize = 13.sp)
        }
    }
}

// ── Permission Required ───────────────────────────────────────────────────────
@Composable
fun WeatherPermissionCard(onGrant: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(0.07f))
            .border(1.dp, Color(0xFF4ADE80).copy(0.25f), RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("📍", fontSize = 36.sp)
            Spacer(Modifier.height(8.dp))
            Text("Location Permission Needed", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(4.dp))
            Text("Allow location access to see weather for your farm", color = Color.White.copy(0.6f), fontSize = 13.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onGrant,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                shape  = RoundedCornerShape(50)
            ) {
                Text("Allow Location", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ── Error ─────────────────────────────────────────────────────────────────────
@Composable
fun WeatherErrorCard(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(0.07f))
            .border(1.dp, Color.Red.copy(0.3f), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("⚠️", fontSize = 30.sp)
            Spacer(Modifier.height(8.dp))
            Text("Weather unavailable", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(message, color = Color.White.copy(0.5f), fontSize = 11.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(12.dp))
            IconButton(
                onClick  = onRetry,
                modifier = Modifier
                    .background(Color(0xFF22C55E).copy(0.15f), CircleShape)
                    .border(1.dp, Color(0xFF22C55E).copy(0.4f), CircleShape)
            ) {
                Icon(Icons.Default.Refresh, "Retry", tint = Color(0xFF4ADE80))
            }
        }
    }
}

// ── Success ───────────────────────────────────────────────────────────────────
@Composable
fun WeatherSuccessCard(data: WeatherData, onRefresh: () -> Unit) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0E2918), Color(0xFF061A10))
                )
            )
            .border(1.5.dp, Color(0xFF4ADE80).copy(0.35f), RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.LocationOn, null, tint = Color(0xFF4ADE80), modifier = Modifier.size(16.dp))
                Text(data.locationName, color = Color(0xFFD1FAE5), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            IconButton(
                onClick  = onRefresh,
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF4ADE80).copy(0.1f), CircleShape)
            ) {
                Icon(Icons.Default.Refresh, "Refresh", tint = Color(0xFF4ADE80), modifier = Modifier.size(16.dp))
            }
        }

        // Big temp + emoji
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text       = data.conditionEmoji,
                fontSize   = 64.sp
            )
            Column {
                Text(
                    text       = "${data.temperatureC}°C",
                    color      = Color.White,
                    fontSize   = 52.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 54.sp
                )
                Text(
                    text       = data.condition,
                    color      = Color(0xFF4ADE80),
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text       = "Feels like ${data.feelsLikeC}°C",
                    color      = Color.White.copy(0.55f),
                    fontSize   = 12.sp
                )
            }
        }

        // Stats grid (2 x 2)
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherStatChip(
                modifier = Modifier.weight(1f),
                icon     = Icons.Default.WaterDrop,
                label    = "Humidity",
                value    = "${data.humidity}%"
            )
            WeatherStatChip(
                modifier = Modifier.weight(1f),
                icon     = Icons.Default.Air,
                label    = "Wind",
                value    = "${data.windKmph} km/h"
            )
        }
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeatherStatChip(
                modifier = Modifier.weight(1f),
                icon     = Icons.Default.WbSunny,
                label    = "UV Index",
                value    = data.uvIndex
            )
            WeatherStatChip(
                modifier = Modifier.weight(1f),
                icon     = Icons.Default.Visibility,
                label    = "Visibility",
                value    = "${data.visibility} km"
            )
        }

        // Sunrise / Sunset bar
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(0.05f), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("🌅", fontSize = 18.sp)
                Column {
                    Text("Sunrise", color = Color.White.copy(0.5f), fontSize = 10.sp)
                    Text(data.sunrise, color = Color(0xFFFBBF24), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            Box(Modifier.width(1.dp).height(32.dp).background(Color.White.copy(0.15f)))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("🌇", fontSize = 18.sp)
                Column {
                    Text("Sunset", color = Color.White.copy(0.5f), fontSize = 10.sp)
                    Text(data.sunset, color = Color(0xFFFF7849), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun WeatherStatChip(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = modifier
            .background(Color.White.copy(0.06f), RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, null, tint = Color(0xFF4ADE80), modifier = Modifier.size(16.dp))
        Column {
            Text(label, color = Color.White.copy(0.5f), fontSize = 10.sp)
            Text(value, color = Color.White,            fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

