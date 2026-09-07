package com.nv.navigation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.unit.LayoutDirection
import com.nv.navigation.core.map.NvMapPreview

private val NvBg = Color(0xFF061A2B)
private val NvPanel = Color(0xE80B2A44)
private val NvPanel2 = Color(0xF2143856)
private val NvCyan = Color(0xFF18C8FF)
private val NvGreen = Color(0xFF62E881)
private val NvAmber = Color(0xFFFFBE38)
private val NvRed = Color(0xFFFF586B)
private val NvMuted = Color(0xFFB8C7D2)

@Composable
fun NVApp() {
    MaterialTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            DriverScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DriverScreen() {
    var query by remember { mutableStateOf("") }
    var showRoutes by remember { mutableStateOf(false) }
    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(NvBg)) {
        val compact = maxWidth < 600.dp
        NvMapPreview(modifier = Modifier.fillMaxSize())
        Box(Modifier.fillMaxWidth().height(112.dp).align(Alignment.TopCenter).background(Color(0x99061A2B)))
        TextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.align(Alignment.TopCenter).padding(horizontal = if (compact) 12.dp else 140.dp, vertical = 12.dp).fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("نام مکان یا NV Code…") },
            leadingIcon = { Text("⌕", color = Color.White, fontSize = 24.sp) },
            trailingIcon = { if (query.isNotEmpty()) IconButton(onClick = { query = "" }) { Text("×", color = Color.White, fontSize = 24.sp) } },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = NvPanel, unfocusedContainerColor = NvPanel,
                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                focusedPlaceholderColor = NvMuted, unfocusedPlaceholderColor = NvMuted,
                focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(20.dp)
        )
        ManeuverHud(Modifier.align(Alignment.TopCenter).padding(top = 82.dp))
        FloatingTools(compact, Modifier.align(Alignment.CenterStart).padding(start = 10.dp))
        ContextCards(compact, Modifier.align(Alignment.CenterEnd).padding(end = 10.dp))
        SpeedHud(Modifier.align(Alignment.BottomStart).padding(start = 12.dp, bottom = 166.dp))
        DestinationStrip(Modifier.align(Alignment.BottomCenter).padding(start = 10.dp, end = 10.dp, bottom = 78.dp)) { showRoutes = true }
        BottomNavigationBar(Modifier.align(Alignment.BottomCenter).padding(8.dp))
        if (showRoutes) {
            ModalBottomSheet(onDismissRequest = { showRoutes = false }, containerColor = NvPanel2, contentColor = Color.White) {
                RoutesSheet { showRoutes = false }
            }
        }
    }
}

@Composable
private fun ManeuverHud(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.width(230.dp), color = Color(0xE0105B57), shape = RoundedCornerShape(22.dp), shadowElevation = 10.dp) {
        Row(Modifier.border(2.dp, Color(0xFF79F6D8), RoundedCornerShape(22.dp)).padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("↱", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.width(10.dp))
            Column { Text("۵۰۰ متر", color = Color.White, fontWeight = FontWeight.Black, fontSize = 20.sp); Text("خروجی بعدی", color = Color.White, fontSize = 13.sp) }
        }
    }
}

@Composable
private fun FloatingTools(compact: Boolean, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Tool("▰", "لایه‌ها", compact); Tool("⛽", "سوخت", compact); Tool("🍴", "غذا", compact); Tool("◉", "دیدنی", compact)
    }
}

@Composable
private fun Tool(icon: String, label: String, compact: Boolean) {
    Surface(color = NvPanel, shape = RoundedCornerShape(16.dp), modifier = Modifier.width(if (compact) 56.dp else 68.dp)) {
        Column(Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 18.sp); Text(label, color = Color.White, fontSize = 8.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ContextCards(compact: Boolean, modifier: Modifier = Modifier) {
    Column(modifier.width(if (compact) 116.dp else 160.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        MiniCard("☁", "آب‌وهوا", "قم • بدون هشدار"); MiniCard("★", "دیدنی جلوتر", "برج آزادی • ۸۰۰م")
    }
}

@Composable
private fun MiniCard(icon: String, title: String, subtitle: String) {
    Surface(color = NvPanel, shape = RoundedCornerShape(15.dp)) {
        Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 16.sp); Spacer(Modifier.width(6.dp)); Column {
                Text(title, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = NvMuted, fontSize = 8.sp, maxLines = 1)
            }
        }
    }
}

@Composable
private fun SpeedHud(modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(shape = CircleShape, color = Color(0xE8082639), modifier = Modifier.size(76.dp)) {
            Box(Modifier.border(3.dp, NvGreen, CircleShape), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("۹۲", color = NvGreen, fontWeight = FontWeight.Black, fontSize = 24.sp); Text("km/h", color = Color.White, fontSize = 10.sp)
                }
            }
        }
        Surface(shape = CircleShape, color = Color.White, modifier = Modifier.size(44.dp)) {
            Box(Modifier.border(5.dp, NvRed, CircleShape), contentAlignment = Alignment.Center) { Text("100", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 13.sp) }
        }
    }
}

@Composable
private fun DestinationStrip(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(modifier = modifier.fillMaxWidth().clickable { onClick() }, color = NvPanel, shape = RoundedCornerShape(20.dp)) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Stat("۱۰:۳۶", "ETA"); Stat("۲۸ دقیقه", "مانده"); Stat("۱۴ km", "مسافت")
            Column(horizontalAlignment = Alignment.End) { Text("برج آزادی", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp); Text("مسیرها", color = NvCyan, fontSize = 9.sp) }
        }
    }
}

@Composable
private fun Stat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(value, color = Color.White, fontWeight = FontWeight.Black, fontSize = 13.sp); Text(label, color = NvMuted, fontSize = 8.sp) }
}

@Composable
private fun BottomNavigationBar(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = Color(0xF407243B), shape = RoundedCornerShape(24.dp)) {
        Row(Modifier.padding(horizontal = 12.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Nav("➤", "مسیریابی", true); Nav("⌕", "جستجو", false); Nav("♡", "علاقه‌مندی", false); Nav("↝", "مسیرها", false); Nav("☁", "آب‌وهوا", false); Nav("⚙", "تنظیمات", false)
        }
    }
}

@Composable
private fun Nav(icon: String, label: String, selected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(icon, color = if (selected) NvCyan else Color.White, fontSize = 20.sp); Text(label, color = if (selected) NvCyan else Color.White, fontSize = 8.sp) }
}

@Composable
private fun RoutesSheet(onStart: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("مسیرهای پیشنهادی NV", fontSize = 20.sp, fontWeight = FontWeight.Black)
        RouteCard("پیشنهاد هوشمند", "۲۸ دقیقه", "۱۴ کیلومتر", "ترافیک سبک", NvCyan)
        RouteCard("سریع‌ترین", "۳۴ دقیقه", "۱۶ کیلومتر", "ترافیک متوسط", NvAmber)
        RouteCard("کوتاه‌ترین", "۴۲ دقیقه", "۱۸ کیلومتر", "ترافیک سنگین", NvRed)
        Button(onClick = onStart, modifier = Modifier.fillMaxWidth().height(54.dp), colors = ButtonDefaults.buttonColors(containerColor = NvCyan), shape = RoundedCornerShape(18.dp)) {
            Text("شروع حرکت", color = Color(0xFF00131D), fontWeight = FontWeight.Black, fontSize = 17.sp)
        }
    }
}

@Composable
private fun RouteCard(title: String, time: String, distance: String, traffic: String, accent: Color) {
    Surface(color = Color(0xCC0B2B45), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.border(1.dp, accent.copy(alpha = .7f), RoundedCornerShape(18.dp)).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(11.dp).background(accent, CircleShape)); Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) { Text(title, color = Color.White, fontWeight = FontWeight.Bold); Text("$distance • $traffic", color = NvMuted, fontSize = 11.sp) }
            Text(time, color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
        }
    }
}
