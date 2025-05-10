package com.karebet.smarthome.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karebet.smarthome.model.RelayInfo
import com.karebet.smarthome.model.RelayViewModel

// ────────────────────────────────────────────────────────────────────────────────
//  Color & Theme Definitions
// ────────────────────────────────────────────────────────────────────────────────
private val PrimaryGradient = listOf(Color(0xFF2962FF), Color(0xFF0D47A1))
private val CardOverlay = Color(0xFFF5F9FF)
private val OnTint = Color(0xFF43A047)
private val OffTint = Color(0xFFE65100)

private val ActiveTrackColor = Color(0xFF2962FF)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleControlScreen(viewModel: RelayViewModel, modifier: Modifier = Modifier) {
    val enabled by viewModel.scheduleEnabled.collectAsState()
    val onTime by viewModel.onTime.collectAsState()
    val offTime by viewModel.offTime.collectAsState()
    val scheduleRelays by viewModel.scheduleRelays.collectAsState()

    var showOnPicker by remember { mutableStateOf(false) }
    var showOffPicker by remember { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = CardOverlay)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(PrimaryGradient))
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("Penjadwalan Otomatis", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text("Aktifkan sesuai waktu tertentu", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
                            }
                        }
                    }
                }
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (enabled) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (enabled) OnTint else OffTint,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = if (enabled) "Status: Aktif" else "Status: Nonaktif",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                        Switch(
                            checked = enabled,
                            onCheckedChange = viewModel::toggleSchedule,
                            modifier = Modifier.scale(1.2f),
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

            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        "Pengaturan Waktu",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2962FF)
                    )
                    DisplayTimeRow("Waktu ON", onTime) { showOnPicker = true }
                    DisplayTimeRow("Waktu OFF", offTime) { showOffPicker = true }
                }
            }

            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Pilih Relay untuk Dijadwalkan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2962FF)
                    )
                    relayInfoMap.forEach { (key, info) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                info.activeIcon,
                                contentDescription = info.name,
                                tint = if (scheduleRelays[key] == true) Color(0xFF2962FF) else Color(0xFF9E9E9E),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                info.name,
                                modifier = Modifier.weight(1f),
                                fontWeight = if (scheduleRelays[key] == true) FontWeight.Medium else FontWeight.Normal
                            )
                            Switch(
                                checked = scheduleRelays[key] == true,
                                onCheckedChange = { viewModel.setRelayScheduled(key, it) },
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
            }
        }
    }

    if (showOnPicker) {
        TimePickerDialog("Pilih Waktu ON", { h, m -> viewModel.setOnTime(formatTo12Hour(h, m)); showOnPicker = false }) { showOnPicker = false }
    }

    if (showOffPicker) {
        TimePickerDialog("Pilih Waktu OFF", { h, m -> viewModel.setOffTime(formatTo12Hour(h, m)); showOffPicker = false }) { showOffPicker = false }
    }
}

@Composable
private fun DisplayTimeRow(label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardOverlay)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
        AssistChip(
            onClick = onClick,
            label = { Text(value) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = Color.White,
                labelColor = Color(0xFF2962FF)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(title: String, onTimeSelected: (Int, Int) -> Unit, onDismiss: () -> Unit) {
    val state = rememberTimePickerState()
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onTimeSelected(state.hour, state.minute) }) {
                Text("Pilih", color = Color(0xFF2962FF))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = Color(0xFF616161))
            }
        },
        title = { Text(title, color = Color(0xFF2962FF), fontWeight = FontWeight.Medium) },
        text = { TimePicker(state = state) },
        shape = RoundedCornerShape(28.dp)
    )
}

fun formatTo12Hour(hour: Int, minute: Int): String {
    val h = if (hour % 12 == 0) 12 else hour % 12
    val ampm = if (hour < 12) "AM" else "PM"
    val m = minute.toString().padStart(2, '0')
    return "$h:$m $ampm"
}