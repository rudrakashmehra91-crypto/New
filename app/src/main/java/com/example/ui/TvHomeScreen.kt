package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Close
import coil.compose.AsyncImage

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TvHomeScreen() {
    var showSettings by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A0A0E), Color(0xFF050505))
                )
            )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            SideNavigationRail(onSettingsClick = { showSettings = true })

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 64.dp)
            ) {
                item {
                    HeroBanner()
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    ContinueWatchingRow() // prioritized
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    AppLaunchersRow()
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    WidgetsRow()
                }
            }
        }
        
        AnimatedVisibility(
            visible = showSettings,
            enter = fadeIn() + slideInHorizontally { it },
            exit = fadeOut() + slideOutHorizontally { it }
        ) {
            SettingsOverlay(onClose = { showSettings = false })
        }
    }
}

@Composable
fun SettingsOverlay(onClose: () -> Unit) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BackHandler {
        onClose()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000))
            .clickable { onClose() },
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(400.dp)
                .background(Color(0xFF16161A))
                .padding(32.dp)
                .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { },
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                FocusableHeroButton(modifier = Modifier.focusRequester(focusRequester), text = "Close", isPrimary = false, onClick = onClose)
            }
            Spacer(modifier = Modifier.height(16.dp))
            FocusableSettingsItem("Network & Internet", "Connected to 5GHz WiFi")
            FocusableSettingsItem("Display & Sound", "4K HDR, Dolby Atmos")
            FocusableSettingsItem("Apps", "Manage installed apps")
            FocusableSettingsItem("System", "Updates, Sleep Timer, About")
        }
    }
}

@Composable
fun FocusableSettingsItem(title: String, subtitle: String) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val scale by animateFloatAsState(if (isFocused) 1.05f else 1.0f, label = "scale")
    val bgColor by animateColorAsState(if (isFocused) Color.White else Color(0xFF2A2A35), label = "bg")
    val titleColor by animateColorAsState(if (isFocused) Color.Black else Color.White, label = "title")
    val subtitleColor by animateColorAsState(if (isFocused) Color.DarkGray else Color(0xFFAAAAAA), label = "sub")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .focusable(interactionSource = interactionSource)
            .clickable(interactionSource = interactionSource, indication = null) { }
            .padding(16.dp)
    ) {
        Text(title, color = titleColor, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(subtitle, color = subtitleColor, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun SideNavigationRail(onSettingsClick: () -> Unit) {
    var isSidebarFocused by remember { mutableStateOf(false) }
    val width by animateDpAsState(
        targetValue = if (isSidebarFocused) 220.dp else 88.dp,
        animationSpec = tween(durationMillis = 300),
        label = "width"
    )

    Column(
        modifier = Modifier
            .width(width)
            .fillMaxSize()
            .background(Color(0x80000000)) // Frosted/cinematic translucent feel
            .onFocusChanged { isSidebarFocused = it.hasFocus }
            .padding(vertical = 40.dp, horizontal = 16.dp),
        horizontalAlignment = if (isSidebarFocused) Alignment.Start else Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        FocusableSidebarItem(icon = Icons.Filled.Search, label = "Search", isSidebarFocused = isSidebarFocused)
        FocusableSidebarItem(icon = Icons.Filled.Home, label = "Home", isSidebarFocused = isSidebarFocused)
        FocusableSidebarItem(icon = Icons.Filled.PlayArrow, label = "Live", isSidebarFocused = isSidebarFocused)
        FocusableSidebarItem(icon = Icons.Filled.List, label = "Movies", isSidebarFocused = isSidebarFocused)
        Spacer(modifier = Modifier.weight(1f))
        FocusableSidebarItem(icon = Icons.Filled.Settings, label = "Settings", isSidebarFocused = isSidebarFocused, onClick = onSettingsClick)
    }
}

@Composable
fun FocusableSidebarItem(icon: ImageVector, label: String, isSidebarFocused: Boolean, onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.2f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    val elevation by animateDpAsState(if (isFocused) 12.dp else 0.dp, label = "elevation")
    
    val bgColor by animateColorAsState(if (isFocused) Color.White else Color.Transparent, label = "bgColor")
    val iconColor by animateColorAsState(if (isFocused) Color.Black else Color(0xFF9CA3AF), label = "iconColor")

    Row(
        modifier = Modifier
            .scale(scale)
            .shadow(elevation, CircleShape, spotColor = Color.White)
            .clip(CircleShape)
            .background(bgColor)
            .focusable(interactionSource = interactionSource)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(if (isSidebarFocused) PaddingValues(start = 14.dp, end = 24.dp, top = 14.dp, bottom = 14.dp) else PaddingValues(14.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
        AnimatedVisibility(
            visible = isSidebarFocused,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Text(
                text = label,
                color = iconColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun HeroBanner() {
    var currentTime by remember { mutableStateOf(getCurrentTime()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = getCurrentTime()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp) // Cinematic wide aspect
            .padding(top = 40.dp, end = 56.dp, start = 32.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.tv_hero_background_1780074522588),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Dark bloom/gradient from bottom for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC000000)),
                        startY = 200f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(40.dp)
        ) {
            Text(
                text = "BLADE RUNNER 2049",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sci-Fi · 2h 44m · 4K HDR",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFAAAAAA),
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FocusableHeroButton(text = "Resume Play", isPrimary = true)
                FocusableHeroButton(text = "Details", isPrimary = false)
            }
        }
        
        // Ambient time display
        Text(
            text = currentTime,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(32.dp),
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date())
}

@Composable
fun FocusableHeroButton(modifier: Modifier = Modifier, text: String, isPrimary: Boolean, onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    val outlineState = if (isFocused) 3.dp else 0.dp

    val bgColor by animateColorAsState(if (isPrimary) {
        if (isFocused) Color.White else Color.White.copy(alpha = 0.2f)
    } else {
        if (isFocused) Color.White else Color.Black.copy(alpha = 0.4f)
    }, label = "bg")
    
    val textColor by animateColorAsState(if (isFocused) Color.Black else Color.White, label = "text")
    
    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(
                width = outlineState,
                color = if (isFocused) Color.White else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .focusable(interactionSource = interactionSource)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .padding(horizontal = 28.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
    }
}

data class AppInfo(val name: String, val color: Color, val logoUrl: String)

@Composable
fun AppLaunchersRow() {
    val apps = listOf(
        AppInfo("Netflix", Color(0xFFE50914), "https://logo.clearbit.com/netflix.com"),
        AppInfo("Prime Video", Color(0xFF00A8E1), "https://logo.clearbit.com/primevideo.com"),
        AppInfo("YouTube", Color(0xFFFF0000), "https://logo.clearbit.com/youtube.com"),
        AppInfo("Hulu", Color(0xFF1CE783), "https://logo.clearbit.com/hulu.com"),
        AppInfo("Disney+", Color(0xFF006E99), "https://logo.clearbit.com/disneyplus.com"),
        AppInfo("Max", Color(0xFF3F00FF), "https://logo.clearbit.com/max.com"),
        AppInfo("Spotify", Color(0xFF1DB954), "https://logo.clearbit.com/spotify.com")
    )

    Column(modifier = Modifier.padding(start = 32.dp)) {
        Text(
            text = "Your Apps",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(end = 56.dp, top = 8.dp, bottom = 24.dp) // breathing room for scale bloom
        ) {
            items(apps) { app ->
                FocusableAppCard(app = app)
            }
        }
    }
}

@Composable
fun FocusableAppCard(app: AppInfo) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    val elevation by animateDpAsState(if (isFocused) 20.dp else 0.dp, label = "elevation")
    val outlineAlpha by animateFloatAsState(if (isFocused) 1f else 0f, label = "outline")

    Box(
        modifier = Modifier
            .width(180.dp)
            .height(110.dp)
            .scale(scale)
            .shadow(elevation, RoundedCornerShape(16.dp), spotColor = app.color, ambientColor = app.color)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 3.dp,
                color = Color.White.copy(alpha = outlineAlpha),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(app.color.copy(alpha = 0.8f), app.color)
                )
            )
            .focusable(interactionSource = interactionSource)
            .clickable(interactionSource = interactionSource, indication = null) { },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = app.logoUrl,
            contentDescription = app.name,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit,
            error = painterResource(id = R.drawable.ic_launcher_foreground) // Fallback
        )
        if (isFocused) {
            Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.1f)))
        }
    }
}

@Composable
fun ContinueWatchingRow() {
    Column(modifier = Modifier.padding(start = 32.dp)) {
        Text(
            text = "Continue Watching",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(end = 56.dp, top = 8.dp, bottom = 24.dp)
        ) {
            items(5) { index ->
                FocusableContentCard("Episode ${index + 3}", "The Enigma Saga · S2 E${index + 3}")
            }
        }
    }
}

@Composable
fun FocusableContentCard(title: String, subtitle: String) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    val elevation by animateDpAsState(if (isFocused) 24.dp else 0.dp, label = "elevation")
    val outlineAlpha by animateFloatAsState(if (isFocused) 1f else 0f, label = "outline")
    val cardAlpha by animateFloatAsState(if (isFocused) 1f else 0.8f, label = "cardAlpha")
    val textAlpha by animateFloatAsState(if (isFocused) 1f else 0.5f, label = "textAlpha")

    Column(
        modifier = Modifier
            .width(280.dp)
            .scale(scale)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(158.dp)
                .shadow(elevation, RoundedCornerShape(16.dp), spotColor = Color.White, ambientColor = Color.White)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = 3.dp,
                    color = Color.White.copy(alpha = outlineAlpha),
                    shape = RoundedCornerShape(16.dp)
                )
                .background(Color(0xFF1E1E2E).copy(alpha = cardAlpha))
                .focusable(interactionSource = interactionSource)
                .clickable(interactionSource = interactionSource, indication = null) { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = Color.White.copy(alpha = textAlpha),
                modifier = Modifier.size(56.dp)
            )
            if (isFocused) {
               Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.05f)))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = textAlpha),
            maxLines = 1,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFAAAAAA).copy(alpha = textAlpha),
            maxLines = 1
        )
    }
}

@Composable
fun WidgetsRow() {
    Column(modifier = Modifier.padding(start = 32.dp)) {
        Text(
            text = "Information & Status",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(end = 56.dp, top = 8.dp, bottom = 24.dp)
        ) {
            item {
                FocusableWidget(title = "Local Weather", info = "72°F · Clear Night")
            }
            item {
                FocusableWidget(title = "Network", info = "Connected · 5GHz")
            }
            item {
                FocusableWidget(title = "Audio System", info = "Dolby Atmos Sync")
            }
        }
    }
}

@Composable
fun FocusableWidget(title: String, info: String) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    val elevation by animateDpAsState(if (isFocused) 16.dp else 0.dp, label = "elevation")
    val outlineAlpha by animateFloatAsState(if (isFocused) 1f else 0f, label = "outline")

    Box(
        modifier = Modifier
            .width(280.dp)
            .height(120.dp)
            .scale(scale)
            .shadow(elevation, RoundedCornerShape(16.dp), spotColor = Color.White)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 3.dp,
                color = Color.White.copy(alpha = outlineAlpha),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
               brush = Brush.verticalGradient(
                   colors = listOf(Color(0xFF2A2A35), Color(0xFF14141A))
               )
            )
            .focusable(interactionSource = interactionSource)
            .clickable(interactionSource = interactionSource, indication = null) { },
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFAAAAAA),
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = info,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
        if (isFocused) {
            Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.05f)))
        }
    }
}
