package com.karebet.smarthome

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.LocalDrink
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val relayInfoMap = mapOf(
    "relay1" to RelayInfo("Lampu Depan", Icons.Filled.Lightbulb, Icons.Outlined.Lightbulb, Color(0xFFFFB74D)),
    "relay2" to RelayInfo("Kipas Angin", Icons.Filled.WbSunny, Icons.Outlined.WbSunny, Color(0xFF64B5F6)),
    "relay3" to RelayInfo("Pompa Air", Icons.Filled.WaterDrop, Icons.Outlined.WaterDrop, Color(0xFF4FC3F7)),
    "relay4" to RelayInfo("Lampu Belakang", Icons.Filled.Lightbulb, Icons.Outlined.Lightbulb, Color(0xFFFFB74D)),
    "relay5" to RelayInfo("AC Ruang Tamu", Icons.Filled.AcUnit, Icons.Outlined.AcUnit, Color(0xFF81D4FA)),
    "relay6" to RelayInfo("CCTV", Icons.Filled.CameraAlt, Icons.Outlined.CameraAlt, Color(0xFFE57373)),
    "relay7" to RelayInfo("Dispenser", Icons.Filled.LocalDrink, Icons.Outlined.LocalDrink, Color(0xFF9575CD)),
    "relay8" to RelayInfo("Kulkas", Icons.Filled.Kitchen, Icons.Outlined.Kitchen, Color(0xFF4DB6AC))
)

@Composable
fun ManualControlScreen(viewModel: RelayViewModel) {
    val relayStates by viewModel.relayStates.collectAsState()
    val listState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HomeHeader(
            connectedDevicesCount = relayStates.count { it.value },
            totalDevices = relayStates.size,
            onToggleAll = { viewModel.toggleAllRelay(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(relayStates.toList()) { (key, value) ->
                val relayData = relayInfoMap[key] ?: RelayInfo(key, Icons.Filled.Lightbulb, Icons.Outlined.Lightbulb, MaterialTheme.colorScheme.primary)

                RelayCardItem(
                    name = relayData.name,
                    isOn = value,
                    activeIcon = relayData.activeIcon,
                    inactiveIcon = relayData.inactiveIcon,
                    onToggle = { viewModel.toggleRelay(key, it) }
                )
            }
        }
    }
}

@Composable
fun HomeHeader(
    connectedDevicesCount: Int,
    totalDevices: Int,
    onToggleAll: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Smart Home",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Kontrol Manual Perangkat",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (connectedDevicesCount > 0) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = "Status",
                            tint = if (connectedDevicesCount > 0) Color(0xFF4CAF50) else Color(0xFFFF9800),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (connectedDevicesCount > 0)
                                "Aktif: $connectedDevicesCount dari $totalDevices"
                            else "Standby: 0 dari $totalDevices",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                    Row {
                        TextButton(onClick = { onToggleAll(true) }) {
                            Text("Hidupkan", color = Color(0xFF2196F3))
                        }
                        TextButton(onClick = { onToggleAll(false) }) {
                            Text("Matikan", color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RelayCardItem(
    name: String,
    isOn: Boolean,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    onToggle: (Boolean) -> Unit
) {
    val switchScale by animateFloatAsState(
        targetValue = if (isOn) 1.05f else 1.0f,
        animationSpec = tween(250),
        label = "scaleAnim"
    )

    val cardColor by animateColorAsState(
        targetValue = if (isOn) Color(0xFFE3F2FD) else Color.White,
        animationSpec = tween(250),
        label = "cardColorAnim"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(!isOn) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isOn) activeIcon else inactiveIcon,
                    contentDescription = name,
                    tint = Color(0xFF1976D2), // biru soft
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Medium
                )
            }

            Switch(
                checked = isOn,
                onCheckedChange = { onToggle(it) },
                modifier = Modifier.scale(switchScale),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF1976D2),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }
    }
}


data class RelayInfo(
    val name: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val color: Color
)
