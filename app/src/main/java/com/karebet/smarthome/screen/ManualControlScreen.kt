package com.karebet.smarthome.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bathroom
import androidx.compose.material.icons.filled.BedroomChild
import androidx.compose.material.icons.filled.BedroomParent
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Bathroom
import androidx.compose.material.icons.outlined.BedroomChild
import androidx.compose.material.icons.outlined.BedroomParent
import androidx.compose.material.icons.outlined.DeviceUnknown
import androidx.compose.material.icons.outlined.DevicesOther
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karebet.smarthome.model.RelayInfo
import com.karebet.smarthome.model.RelayViewModel

// ────────────────────────────────────────────────────────────────────────────────
//  Color & Theme Definitions - Synchronized with ScheduleControlScreen
// ────────────────────────────────────────────────────────────────────────────────
private val PrimaryBlue = Color(0xFF2962FF)
private val LightCardOn = Color(0xFFE3F2FD)
private val TurnOnAllColor = Color(0xFF43A047)
private val TurnOffAllColor = Color(0xFFE65100)

private val ActiveTrackColor = PrimaryBlue
private val ActiveThumbColor = Color.White
private val InactiveTrackColor = Color(0xFFDFE6FF)
private val InactiveThumbColor = Color.White

private val relayInfoMap = mapOf(
    "relay1" to RelayInfo("Lampu Depan", Icons.Filled.Lightbulb, Icons.Outlined.Lightbulb),
    "relay2" to RelayInfo("Lampu Ruang Tamu", Icons.Filled.WbSunny, Icons.Outlined.WbSunny),
    "relay3" to RelayInfo("Lampu Kamar Adik", Icons.Filled.BedroomChild, Icons.Outlined.BedroomChild),
    "relay4" to RelayInfo("Lampu Kamar Ezi", Icons.Filled.BedroomParent, Icons.Outlined.BedroomParent),
    "relay5" to RelayInfo("Lampu Dapur", Icons.Filled.Kitchen, Icons.Outlined.Kitchen),
    "relay6" to RelayInfo("Lampu Kamar Mandi", Icons.Filled.Bathroom, Icons.Outlined.Bathroom),
    "relay7" to RelayInfo("Lain - Lain", Icons.Filled.DevicesOther, Icons.Outlined.DevicesOther),
    "relay8" to RelayInfo("Lain - Lain", Icons.Filled.DeviceUnknown, Icons.Outlined.DeviceUnknown)
)

@Composable
fun ManualControlScreen(viewModel: RelayViewModel) {
    val relayStates by viewModel.relayStates.collectAsState()

    val connectedDevices by remember(relayStates) {
        derivedStateOf { relayStates.count { it.value } }
    }

    val listState = rememberLazyListState()
    val hapticFeedback = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        HomeHeader(connectedDevices, relayStates.size)

        AllToggleButtons(
            onTurnOnAll = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.toggleAllRelay(true)
            },
            onTurnOffAll = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                viewModel.toggleAllRelay(false)
            }
        )

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(relayStates.toList(), key = { it.first }) { (key, isOn) ->
                val relayData = relayInfoMap[key] ?: RelayInfo(key, Icons.Filled.Lightbulb, Icons.Outlined.Lightbulb)
                RelayCardItem(
                    name = relayData.name,
                    isOn = isOn,
                    activeIcon = relayData.activeIcon,
                    inactiveIcon = relayData.inactiveIcon,
                    onToggle = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.toggleRelay(key, it)
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(connectedDevicesCount: Int, totalDevices: Int) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = PrimaryBlue),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Home, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Smart Home",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Kontrol Manual Perangkat",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (connectedDevicesCount > 0) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = "Status",
                    tint = if (connectedDevicesCount > 0) TurnOnAllColor else Color(0xFFFF9800),
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = if (connectedDevicesCount > 0)
                        "Aktif: $connectedDevicesCount dari $totalDevices"
                    else "Standby: 0 dari $totalDevices",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
private fun AllToggleButtons(onTurnOnAll: () -> Unit, onTurnOffAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActionButton(
            label = "HIDUPKAN SEMUA",
            background = TurnOnAllColor,
            onClick = onTurnOnAll,
            modifier = Modifier.weight(1f)
        )
        ActionButton(
            label = "MATIKAN SEMUA",
            background = TurnOffAllColor,
            onClick = onTurnOffAll,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ActionButton(label: String, background: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = label,
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RelayCardItem(
    name: String,
    isOn: Boolean,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    onToggle: (Boolean) -> Unit
) {
    // Smooth animations with better easing
    val switchScale by animateFloatAsState(
        targetValue = if (isOn) 1.05f else 1f,
        animationSpec = tween(250, easing = FastOutSlowInEasing),
        label = "switchScaleAnim"
    )

    val cardColor by animateColorAsState(
        targetValue = if (isOn) LightCardOn else Color.White,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "cardColorAnim"
    )

    val textColor by animateColorAsState(
        targetValue = if (isOn) PrimaryBlue else Color(0xFF424242),
        animationSpec = tween(300),
        label = "textColorAnim"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isOn) PrimaryBlue else Color(0xFF757575),
        animationSpec = tween(300),
        label = "iconColorAnim"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isOn) 2.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isOn) activeIcon else inactiveIcon,
                    contentDescription = name,
                    tint = iconColor,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = textColor,
                    fontWeight = if (isOn) FontWeight.Medium else FontWeight.Normal
                )
            }

            Switch(
                checked = isOn,
                onCheckedChange = { onToggle(it) },
                modifier = Modifier.scale(switchScale),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ActiveThumbColor,
                    checkedTrackColor = ActiveTrackColor,
                    uncheckedThumbColor = InactiveThumbColor,
                    uncheckedTrackColor = InactiveTrackColor
                )
            )
        }
    }
}